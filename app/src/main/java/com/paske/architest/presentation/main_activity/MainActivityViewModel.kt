package com.paske.architest.presentation.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.paske.architest.data.Repository

class MainActivityViewModel(private val repository: Repository) : ViewModel()  {
}
@Suppress("UNCHECKED_CAST")
class MainActivityViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(
            repository
        ) as T
    }

}