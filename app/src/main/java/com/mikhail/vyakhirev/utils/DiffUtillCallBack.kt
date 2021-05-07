package com.mikhail.vyakhirev.utils

import androidx.recyclerview.widget.DiffUtil
import com.mikhail.vyakhirev.data.model.UiModel

class DiffUtilCallBack : DiffUtil.ItemCallback<UiModel>() {
    override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
        return (oldItem is UiModel.Photo && newItem is UiModel.Photo &&
                oldItem.photoItem.id == newItem.photoItem.id ) ||
                (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem &&
                        oldItem.description == newItem.description)
    }

    override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
        oldItem == newItem
}
