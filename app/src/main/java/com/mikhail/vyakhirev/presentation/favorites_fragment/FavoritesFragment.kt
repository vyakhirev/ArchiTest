package com.mikhail.vyakhirev.presentation.favorites_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
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
    private var getFavoriteById: Job? = null

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
            photoClickListener = {
            },
            posListener = { adapter.notifyItemRemoved(it) },
            favorStarClickListener = { viewModel.favoriteSwitcher(it) }
        )
        binding.favorPhotoRV.layoutManager = GridLayoutManager(requireContext(),2)
        binding.favorPhotoRV.adapter = adapter
    }

}