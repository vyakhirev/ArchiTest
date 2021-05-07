package com.mikhail.vyakhirev.presentation.user_details_fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.databinding.UserDetailFragmentBinding
import com.mikhail.vyakhirev.presentation.main_activity.MainActivity
import com.mikhail.vyakhirev.utils.extensions.loadImageFromLink
import com.mikhail.vyakhirev.utils.extensions.myLog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class UserDetailsFragment : Fragment() {

    private val viewModel: UserDetailViewModel by viewModels()

    private var binding: UserDetailFragmentBinding? = null
    private var job: Job? = null
    private var accountIcon: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setHasOptionsMenu(true)
//        (activity as? AppCompatActivity)?.supportActionBar?.title = "kan2222"
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
            val  mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(View.GONE)
        }

        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = getString(R.string.user_details)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        viewModel.user.observe(viewLifecycleOwner, {
            binding?.userImg?.loadImageFromLink(it.imageUrl)
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