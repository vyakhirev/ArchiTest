package com.mikhail.vyakhirev.presentation.list_fragment

import androidx.lifecycle.*
import com.mikhail.vyakhirev.data.IRepository
import com.mikhail.vyakhirev.data.model.PhotoItem
import kotlinx.coroutines.launch

class ListFragmentViewModel(private val repository: IRepository) : ViewModel()  {

    private val _photos = MutableLiveData<List<PhotoItem>>()
    val photos: LiveData<List<PhotoItem>> = _photos


    fun getRecentPhoto(){
        viewModelScope.launch {
             _photos.value=repository.getFavorites().photos.photo
        }
    }

}
@Suppress("UNCHECKED_CAST")
class ListFragmentViewModelFactory(
    private val IRepository: IRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListFragmentViewModel(
            IRepository
        ) as T
    }

}