package com.mikhail.vyakhirev.presentation.detail_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.databinding.DetailFragmentBinding
import com.mikhail.vyakhirev.presentation.list_fragment.ListFragmentViewModel
import com.mikhail.vyakhirev.presentation.main_activity.MainActivity
import com.mikhail.vyakhirev.presentation.user_details_fragment.UserDetailsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()

    companion object {
        fun newInstance() = DetailFragment()
    }

    private var _binding: DetailFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: ListFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleTV.text = args.photoItem?.title ?: args.favoriteModel?.title
        val url = args.photoItem?.getFlickrImageLink('z') ?: args.favoriteModel?.imageUrl
        Glide.with(binding.bigPhotoIV)
            .load(url)
            .centerCrop()
            .into(binding.bigPhotoIV)

        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(View.GONE)
        }

        setHasOptionsMenu(true)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = getString(R.string.detail_fragment_tag)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val direction = if (args.favoriteModel == null)
                UserDetailsFragmentDirections.actionGlobalListMyFragment()
            else
                UserDetailsFragmentDirections.actionGlobalFavoritesFragment()

            view?.findNavController()?.navigate(direction)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}