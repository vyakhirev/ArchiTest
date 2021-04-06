package com.mikhail.vyakhirev.presentation.detail_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mikhail.vyakhirev.data.IRepository

class DetailViewModel(private val IRepository: IRepository) : ViewModel()  {
}
@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(
    private val IRepository: IRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(
            IRepository
        ) as T
    }

}