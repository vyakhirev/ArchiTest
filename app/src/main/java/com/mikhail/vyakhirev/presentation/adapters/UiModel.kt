package com.mikhail.vyakhirev.presentation.adapters

import com.mikhail.vyakhirev.data.model.PhotoItem

sealed class UiModel {
    data class Photo(val photoItem: PhotoItem) : UiModel()
    data class SeparatorItem(val description: String) : UiModel()
}