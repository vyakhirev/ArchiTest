package com.mikhail.vyakhirev.data.model

sealed class UiModel {
    data class Photo(val photoItem: PhotoItem) : UiModel()
    data class SeparatorItem(val description: String) : UiModel()
}