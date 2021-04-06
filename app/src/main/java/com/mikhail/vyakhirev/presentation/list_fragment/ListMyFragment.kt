package com.mikhail.vyakhirev.presentation.list_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.mikhail.vyakhirev.databinding.ListFragmentBinding
import com.mikhail.vyakhirev.presentation.adapters.ListAdapter
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.subDI
import org.kodein.di.android.x.closestDI
import org.kodein.di.instance

class ListMyFragment : Fragment(), DIAware {

    private val factory by instance<ListFragmentViewModelFactory>()
    override val di: DI by subDI(closestDI()) {}
    private lateinit var fragmentViewModel: ListFragmentViewModel
    private lateinit var listAdapter: ListAdapter

    private var _binding: ListFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentViewModel = ViewModelProvider(this, factory).get(ListFragmentViewModel::class.java)
        _binding = ListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = ListAdapter(mutableListOf())
        binding.listPhotoRv.layoutManager = GridLayoutManager(context, 2)
        binding.listPhotoRv.adapter = listAdapter

        fragmentViewModel.getRecentPhoto()

        fragmentViewModel.photos.observe(viewLifecycleOwner, {
            listAdapter.updateItems(it)
        })
    }

}