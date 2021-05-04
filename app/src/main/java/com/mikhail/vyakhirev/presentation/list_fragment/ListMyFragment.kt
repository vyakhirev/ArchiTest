package com.mikhail.vyakhirev.presentation.list_fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.databinding.ListFragmentBinding
import com.mikhail.vyakhirev.presentation.adapters.ListAdapter
import com.mikhail.vyakhirev.presentation.adapters.MyLoadStateAdapter
import com.mikhail.vyakhirev.presentation.settings_fragment.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListMyFragment : Fragment() {

    private val viewModel: ListFragmentViewModel by viewModels()
    private lateinit var adapter: ListAdapter
    private var binding: ListFragmentBinding? = null
    private var searchJob: Job? = null
//    private var lastQuery = null

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val listFragmentBinding = ListFragmentBinding.inflate(inflater, container, false)
        binding = listFragmentBinding
        return binding!!.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        var query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        val lastQuery = viewModel.loadLastQuery()
        if (!lastQuery.isNullOrEmpty())
            query = lastQuery

        search(query)
        initSearch(query)

        viewModel.queryStat.observe(viewLifecycleOwner, {
            binding?.infoTV?.text = "$it" + getString(R.string.result_found_msg) + "\"$query\""
        })

        binding!!.retryButton.setOnClickListener { adapter.retry() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val query = binding?.searchET?.text.toString()
        outState.putString(LAST_SEARCH_QUERY, query)
    }

    private fun initAdapter() {

        adapter = ListAdapter(
            favorStarClickListener = {
                viewModel.favoriteSwitcher(it)
            },
            photoClickListener = {
//                viewModel.favoriteSwitcher(it)
                val direction = ListMyFragmentDirections.actionListMyFragmentToDetailFragment(it)
                view?.findNavController()?.navigate(direction)
                val query = binding?.searchET?.text.toString()
                viewModel.saveQuery(query)
            }
        )

        binding?.listPhotoRv?.layoutManager = GridLayoutManager(context, 2)

        binding?.listPhotoRv?.adapter = adapter.withLoadStateHeaderAndFooter(
            header = MyLoadStateAdapter { adapter.retry() },
            footer = MyLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->

            // show empty list
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyList(isListEmpty)

            // Only show the list if refresh succeeds.
            binding?.listPhotoRv?.isVisible = loadState.mediator?.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding?.progressBar?.isVisible = loadState.mediator?.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding?.retryButton?.isVisible = loadState.mediator?.refresh is LoadState.Error
            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    requireContext(),
                    "\uD83D\uDE28 Oops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        viewModel.saveQuery(query)
        searchJob = lifecycleScope.launch {
            viewModel.searchPhoto(query).collectLatest {
                adapter.submitData(it)
            }
        }

        viewModel.setQueryStat()
    }

    @InternalCoroutinesApi
    private fun initSearch(query: String) {
        binding?.searchET?.setText(query)
        viewModel.saveQuery(query)
        binding?.searchET?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updatePhotoListFromInput()
                true
            } else {
                false
            }
        }
        binding?.searchET?.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updatePhotoListFromInput()
                true
            } else {
                false
            }
        }

        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding?.listPhotoRv?.scrollToPosition(0) }
        }
    }

    private fun updatePhotoListFromInput() {
        binding?.searchET?.text?.trim().let {
            if (it!!.isNotEmpty()) {
                search(it.toString())
            }
        }
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            binding?.emptyList?.visibility = View.VISIBLE
            binding?.listPhotoRv?.visibility = View.GONE
        } else {
            binding?.emptyList?.visibility = View.GONE
            binding?.listPhotoRv?.visibility = View.VISIBLE
        }
    }


    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "C3PO"
    }
}