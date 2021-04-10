package com.mikhail.vyakhirev.presentation.favorites_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.mikhail.vyakhirev.databinding.FavoritesFragmentBinding
import com.mikhail.vyakhirev.databinding.ListFragmentBinding
import com.mikhail.vyakhirev.presentation.adapters.ListAdapter
import com.mikhail.vyakhirev.presentation.list_fragment.ListFragmentViewModel
import com.mikhail.vyakhirev.presentation.list_fragment.ListFragmentViewModelFactory
import kotlinx.coroutines.Job
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.subDI
import org.kodein.di.android.x.closestDI
import org.kodein.di.instance

class FavoritesFragment : Fragment(), DIAware {

    private val factory by instance<FavoritesViewModelFactory>()
    override val di: DI by subDI(closestDI()) {}
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var adapter: ListAdapter

    private var _binding: FavoritesFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var getFavoritesJob: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this, factory).get(FavoritesViewModel::class.java)
        _binding = FavoritesFragmentBinding.inflate(inflater, container, false)
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