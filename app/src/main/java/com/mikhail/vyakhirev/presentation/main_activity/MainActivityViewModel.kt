package com.mikhail.vyakhirev.presentation.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mikhail.vyakhirev.data.Repository

class MainActivityViewModel(private val repository: Repository) : ViewModel()  {
    fun kan(){
        repository.toString()
    }
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