package com.mikhail.vyakhirev.presentation.favorites_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.databinding.FavoritesFragmentBinding
import com.mikhail.vyakhirev.presentation.adapters.favor.FavoritesAdapter
import com.mikhail.vyakhirev.presentation.list_fragment.ListMyFragmentDirections
import com.mikhail.vyakhirev.presentation.main_activity.MainActivity
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

        if (activity is MainActivity) {
            val  mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(View.VISIBLE)
            mainActivity.supportActionBar?.apply {
                title = getString(R.string.favorites_fragment_label)
                setDisplayHomeAsUpEnabled(false)
                setDisplayShowHomeEnabled(false)
            }
        }
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
                val direction = FavoritesFragmentDirections.actionFavoritesFragmentToDetailFragment(null,it)
                view?.findNavController()?.navigate(direction)
            },
            posListener = { adapter.notifyItemRemoved(it) },
            favorStarClickListener = { it?.let { it1 -> viewModel.favoriteSwitcher(it1) } }
        )
        binding.favorPhotoRV.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.favorPhotoRV.adapter = adapter
    }

}