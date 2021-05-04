package com.mikhail.vyakhirev.presentation.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mikhail.vyakhirev.data.IRepository

class MainActivityViewModel(private val repository: IRepository) : ViewModel()  {
    fun kan(){
        repository.toString()
    }
    fun searchMovies(query:String){

    }
}
@Suppress("UNCHECKED_CAST")
class MainActivityViewModelFactory(
    private val IRepository: IRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(
            IRepository
        ) as T
    }

}