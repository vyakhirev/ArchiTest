package com.mikhail.vyakhirev.presentation.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.databinding.RegistrationFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private val viewModel: RegistrationViewModel by viewModels()
    private var binding: RegistrationFragmentBinding? = null
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val registrationFragmentBinding =
            RegistrationFragmentBinding.inflate(inflater, container, false)
        binding = registrationFragmentBinding

        setHasOptionsMenu(true)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = getString(R.string.registration_fragment_label)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.loginBtn?.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        binding?.cancelBTN?.setOnClickListener {
            findNavController().navigate(R.id.listMyFragment)
        }

        binding?.signBtn?.setOnClickListener {
            viewModel.userRegistration(
                binding?.userEditText?.text.toString(),
                binding?.emailEditText?.text.toString(),
                binding?.passwordEditText?.text.toString()
            )
        }

        viewModel.isRegistered.observe(viewLifecycleOwner, {
            if (it.peekContent())
                findNavController().navigate(R.id.loginFragment)
        })

    }
}