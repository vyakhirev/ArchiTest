package com.mikhail.vyakhirev.presentation.list_fragment

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.mikhail.vyakhirev.data.IRepository
import com.mikhail.vyakhirev.data.model.PhotoItem
import com.mikhail.vyakhirev.presentation.adapters.UiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ListFragmentViewModel(private val repository: IRepository) : ViewModel()  {

//    private val _photos = MutableLiveData<List<PhotoItem>>()
//    val photos: LiveData<List<PhotoItem>> = _photos
//    fun getRecentPhoto(){
//        viewModelScope.launch {
//            _photos.value=repository.getFavorites().photos.photo
//        }
//    }


//    private var currentQueryValue: String? = null

    private var currentResult: Flow<PagingData<UiModel>>? = null

    fun getFavorites(): Flow<PagingData<UiModel>> {
        val lastResult = currentResult
        if (lastResult != null) {
            return lastResult
        }
//        currentQueryValue = queryString
        val newResult: Flow<PagingData<UiModel>> = repository.getFavorites()
            .map { pagingData -> pagingData.map { UiModel.Photo(it) } }
            .map {
                it.insertSeparators<UiModel.Photo, UiModel> { before, after ->
                        null
                }
            }
            .cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
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