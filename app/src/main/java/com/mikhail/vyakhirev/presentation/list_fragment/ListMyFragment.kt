package com.mikhail.vyakhirev.presentation.list_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.mikhail.vyakhirev.databinding.ListFragmentBinding
import com.mikhail.vyakhirev.presentation.adapters.ListAdapter
import com.mikhail.vyakhirev.presentation.adapters.MyLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListMyFragment : Fragment() {

    private val viewModel: ListFragmentViewModel by viewModels()
    private lateinit var adapter: ListAdapter
    private var listFragmentBinding: ListFragmentBinding? = null
    private var getFavoritesJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ListFragmentBinding.inflate(inflater, container, false)
        listFragmentBinding = binding
        initAdapter(binding)
        getFavorites()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listFragmentBinding = null
    }

    private fun showEmptyList(show: Boolean, binding: ListFragmentBinding) {
        if (show) {
            binding.emptyList.visibility = View.VISIBLE
            binding.listPhotoRv.visibility = View.GONE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.listPhotoRv.visibility = View.VISIBLE
        }
    }

    private fun getFavorites() {
        // Make sure we cancel the previous job before creating a new one
        getFavoritesJob?.cancel()
        getFavoritesJob = lifecycleScope.launch {
            viewModel.getFavorites().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun initAdapter(binding: ListFragmentBinding) {

        adapter = ListAdapter(bigPhotoClickListener = {
            viewModel.favoriteSwitcher(it)
        }, posListener = {
            adapter.notifyItemChanged(it)
        })

        binding.listPhotoRv.layoutManager = GridLayoutManager(context, 2)

        binding.listPhotoRv.adapter = adapter.withLoadStateHeaderAndFooter(
            header = MyLoadStateAdapter { adapter.retry() },
            footer = MyLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->

            // show empty list
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyList(isListEmpty, binding)

            // Only show the list if refresh succeeds.
            binding.listPhotoRv.isVisible = loadState.mediator?.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.mediator?.refresh is LoadState.Error
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
}