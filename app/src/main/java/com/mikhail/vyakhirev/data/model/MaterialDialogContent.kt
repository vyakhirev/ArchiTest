package com.mikhail.vyakhirev.data.model

import androidx.annotation.StringRes

data class MaterialDialogContent(
    @StringRes val positiveText: Int,
    @StringRes val content: Int? = null,
    @StringRes val title: Int,
    @StringRes val negativeText: Int? = null,
    val contentText: String? = null
)