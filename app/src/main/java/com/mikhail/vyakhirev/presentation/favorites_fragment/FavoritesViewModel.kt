package com.mikhail.vyakhirev.presentation.favorites_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mikhail.vyakhirev.data.IRepository

class FavoritesViewModel(private val IRepository: IRepository) : ViewModel()  {
}
@Suppress("UNCHECKED_CAST")
class FavoritesViewModelFactory(
    private val IRepository: IRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoritesViewModel(
            IRepository
        ) as T
    }

}