package com.mikhail.vyakhirev.presentation.favorites_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikhail.vyakhirev.databinding.FavoritesFragmentBinding
import com.mikhail.vyakhirev.presentation.adapters.favor.FavoritesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var adapter: FavoritesAdapter
    private var favoritesFragmentBinding: FavoritesFragmentBinding? = null
    private var getFavoritesJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FavoritesFragmentBinding.inflate(inflater, container, false)
        favoritesFragmentBinding = binding
        initAdapter(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFavorites()
        initLivedataObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        favoritesFragmentBinding = null
    }

    private fun initLivedataObservers() {
        viewModel.favorites.observe(viewLifecycleOwner, {
            adapter.update(it)
        })
    }

    private fun showEmptyList(show: Boolean, binding: FavoritesFragmentBinding) {
        if (show) {
//            binding.emptyList.visibility = View.VISIBLE
            binding.favorPhotoRV.visibility = View.GONE
        } else {
//            binding.emptyList.visibility = View.GONE
            binding.favorPhotoRV.visibility = View.VISIBLE
        }
    }

    private fun getFavorites() {
        getFavoritesJob?.cancel()
        getFavoritesJob = lifecycleScope.launch {
            viewModel.getFavorites()
        }
    }

    private fun initAdapter(binding: FavoritesFragmentBinding) {
        adapter = FavoritesAdapter(
            listOf(),
            photoClickListener = {},
            posListener = {},
            favorStarClickListener = {}
        )
        binding.favorPhotoRV.layoutManager = LinearLayoutManager(requireContext())
        binding.favorPhotoRV.adapter = adapter

    }

}
//    }
//    private fun initAdapter(binding: FavoritesFragmentBinding) {
//
//        adapter = ListAdapter(favorStarClickListener = {
//            viewModel.favoriteSwitcher(it)
//        }, posListener = {
//            adapter.notifyItemChanged(it)
//        },photoClickListener = {
//            val direction = ListMyFragmentDirections.actionListMyFragmentToDetailFragment(it)
//            view?.findNavController()?.navigate(direction)
////            val query = binding?.searchET?.text.toString()
////            viewModel.saveQuery(query)
//        })
//
//        binding.listPhotoRv.layoutManager = GridLayoutManager(context, 2)
//
//        binding.listPhotoRv.adapter = adapter.withLoadStateHeaderAndFooter(
//            header = MyLoadStateAdapter { adapter.retry() },
//            footer = MyLoadStateAdapter { adapter.retry() }
//        )
//        adapter.addLoadStateListener { loadState ->
//
//            // show empty list
//            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
//            showEmptyList(isListEmpty, binding)
//
//            // Only show the list if refresh succeeds.
//            binding.listPhotoRv.isVisible = loadState.mediator?.refresh is LoadState.NotLoading
//            // Show loading spinner during initial load or refresh.
//            binding.progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
//            // Show the retry state if initial load or refresh fails.
//            binding.retryButton.isVisible = loadState.mediator?.refresh is LoadState.Error
//            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
//            val errorState = loadState.source.append as? LoadState.Error
//                ?: loadState.source.prepend as? LoadState.Error
//                ?: loadState.append as? LoadState.Error
//                ?: loadState.prepend as? LoadState.Error
//            errorState?.let {
//                Toast.makeText(
//                    requireContext(),
//                    "\uD83D\uDE28 Oops ${it.error}",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//
//        }
//    }
//}