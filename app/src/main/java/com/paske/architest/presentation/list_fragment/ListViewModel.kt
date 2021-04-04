package com.paske.architest.presentation.list_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.paske.architest.data.Repository

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