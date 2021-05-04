package com.mikhail.vyakhirev.presentation.list_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.mikhail.vyakhirev.data.IRepository
import com.mikhail.vyakhirev.data.model.PhotoItem
import com.mikhail.vyakhirev.data.model.UiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListFragmentViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {
    private var currentQueryValue: String? = null

//    private val _photos = MutableLiveData<List<PhotoItem>>()
//    val photos: LiveData<List<PhotoItem>> = _photos
//    fun getRecentPhoto(){
//        viewModelScope.launch {
//            _photos.value=repository.getFavorites().photos.photo
//        }
//    }

//    private var currentQueryValue: String? = null

    private val _queryStat = MutableLiveData<Int>()
    val queryStat: LiveData<Int> = _queryStat

    private var currentResult: Flow<PagingData<UiModel>>? = null

    fun setQueryStat() {
        viewModelScope.launch {
            _queryStat.value = repository.getStatByQuery()
        }
    }

//    fun getFavorites(): Flow<PagingData<UiModel>> {
//        val lastResult = currentResult
//        if (lastResult != null) {
//            return lastResult
//        }
////        currentQueryValue = queryString
//        val newResult: Flow<PagingData<UiModel>> = repository.getFavorites()
//            .map { pagingData -> pagingData.map { UiModel.Photo(it) } }
//            .map {
//                it.insertSeparators<UiModel.Photo, UiModel> { before, after ->
//                    null
//                }
//            }
//            .cachedIn(viewModelScope)
//        currentResult = newResult
//        return newResult
//    }

    fun searchPhoto(query: String): Flow<PagingData<UiModel>> {
        val lastResult = currentResult
//        setQueryStat()
        if (query == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = query
        val newResult: Flow<PagingData<UiModel>> = repository.getPhotoSearchResult(query)
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

    fun favoriteSwitcher(photoItem: PhotoItem) {
        photoItem.isFavorite = !photoItem.isFavorite
        viewModelScope.launch {
            repository.switchFavorite(photoItem)
        }
    }

    fun saveQuery(query: String) {
        repository.saveQueryToPrefs(query)
        setQueryStat()
    }

    fun loadLastQuery(): String = repository.loadQueryFromPrefs()

}

//@Suppress("UNCHECKED_CAST")
//class ListFragmentViewModelFactory(
//    private val IRepository: IRepository
//) : ViewModelProvider.NewInstanceFactory() {
//
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return ListFragmentViewModel(
//            IRepository
//        ) as T
//    }

//}