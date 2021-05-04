package com.mikhail.vyakhirev.presentation.settings_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mikhail.vyakhirev.databinding.SettingsFragmentBinding
import com.mikhail.vyakhirev.presentation.adapters.ListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    //    private val factory by instance<SettingsViewModelFactory>()
//    override val di: DI by subDI(closestDI()) {}
    private lateinit var viewModel: SettingsViewModel
    private lateinit var adapter: ListAdapter

    private var _binding: SettingsFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    private var getFavoritesJob: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        viewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}