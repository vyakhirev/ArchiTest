package com.paske.architest.presentation.detail_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.paske.architest.data.Repository

class DetailViewModel(private val repository: Repository) : ViewModel()  {
}
@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(
            repository
        ) as T
    }

}