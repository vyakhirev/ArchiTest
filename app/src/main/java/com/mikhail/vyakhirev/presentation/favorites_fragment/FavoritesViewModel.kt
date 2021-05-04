package com.mikhail.vyakhirev.presentation.favorites_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikhail.vyakhirev.data.IRepository
import com.mikhail.vyakhirev.data.model.FavoriteModel
import com.mikhail.vyakhirev.data.model.PhotoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val _favorites = MutableLiveData<List<FavoriteModel>>()
    val favorites: LiveData<List<FavoriteModel>> = _favorites

    private val _favoriteItem = MutableLiveData<PhotoItem>()
    val favoriteItem: LiveData<PhotoItem> = _favoriteItem

    suspend fun getFavorites() {
        viewModelScope.launch {
            _favorites.value = repository.getFavorites()
        }
//        if (lastResult != null) {
//            return lastResult
//        }
//        currentQueryValue = queryString
//        val newResult:List<PhotoItem> = repository.getFavorites()
//            .map { pagingData -> pagingData { UiModel.Photo(it) }.filter { it.photoItem.isFavorite} }
//            .map {
//                it.insertSeparators<UiModel.Photo, UiModel> { before, after ->
//                    null
//                }
//            }
//            .cachedIn(viewModelScope)
//        currentResult = newResult
//        return newResult
    }

    fun favoriteSwitcher(photoItem: FavoriteModel) {
//        photoItem.isFavorite = !photoItem.isFavorite
        viewModelScope.launch {
            repository.switchFavorite(photoItem.id)
            getFavorites()
        }
    }

    suspend fun getPhotoItemById(id: String) {
        _favoriteItem.value = repository.getPhotoItemByID(id)
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