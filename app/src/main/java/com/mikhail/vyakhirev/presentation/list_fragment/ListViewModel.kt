package com.mikhail.vyakhirev.presentation.list_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mikhail.vyakhirev.data.Repository

class ListViewModel(private val repository: Repository) : ViewModel()  {
}
@Suppress("UNCHECKED_CAST")
class ListViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListViewModel(
            repository
        ) as T
    }

}