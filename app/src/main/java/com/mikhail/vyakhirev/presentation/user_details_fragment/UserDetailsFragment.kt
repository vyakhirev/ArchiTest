package com.mikhail.vyakhirev.presentation.user_details_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.databinding.UserDetailFragmentBinding
import com.mikhail.vyakhirev.presentation.main_activity.MainActivity
import com.mikhail.vyakhirev.utils.extensions.loadImageFromLink
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailsFragment : Fragment() {

    private val viewModel: UserDetailViewModel by viewModels()

    private var binding: UserDetailFragmentBinding? = null
//    private var job: Job? = null
//    private var accountIcon: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val userDetailFragmentBinding =
            UserDetailFragmentBinding.inflate(inflater, container, false)
        binding = userDetailFragmentBinding
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadUserData(requireContext())

        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(View.GONE)
            setHasOptionsMenu(true)
            mainActivity.supportActionBar?.apply {
                title = getString(R.string.user_details_label)
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }

        viewModel.user.observe(viewLifecycleOwner, {
            if (!it.imageUrl.isNullOrBlank())
                binding?.userImg?.loadImageFromLink(it.imageUrl)
            else
                binding?.userImg?.setImageDrawable(resources.getDrawable(R.drawable.logo))
            binding?.userName?.text = it.name
            binding?.userEmail?.text = it.email
        })

        binding?.logoutBtn?.setOnClickListener {
            viewModel.signOut(requireContext())
            val direction = UserDetailsFragmentDirections.actionGlobalListMyFragment()
            view.findNavController().navigate(direction)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val direction = UserDetailsFragmentDirections.actionGlobalListMyFragment()
            view?.findNavController()?.navigate(direction)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}