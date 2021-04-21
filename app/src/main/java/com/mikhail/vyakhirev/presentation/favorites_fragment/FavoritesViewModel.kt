package com.mikhail.vyakhirev.presentation.favorites_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.mikhail.vyakhirev.data.IRepository
import com.mikhail.vyakhirev.data.model.PhotoItem
import com.mikhail.vyakhirev.presentation.adapters.UiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {
    private var currentResult: Flow<PagingData<UiModel>>? = null

    fun getFavorites(): Flow<PagingData<UiModel>> {
        val lastResult = currentResult
        if (lastResult != null) {
            return lastResult
        }
//        currentQueryValue = queryString
        val newResult: Flow<PagingData<UiModel>> = repository.getFavorites()
            .map { pagingData -> pagingData.map { UiModel.Photo(it) }.filter { it.photoItem.isFavorite} }
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
    }

}

//@Suppress("UNCHECKED_CAST")
//class FavoritesViewModelFactory(
//    private val IRepository: IRepository
//) : ViewModelProvider.NewInstanceFactory() {
//
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return FavoritesViewModel(
//            IRepository
//        ) as T
//    }
//
//}