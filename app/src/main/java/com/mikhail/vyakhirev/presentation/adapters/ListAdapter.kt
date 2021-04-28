package com.mikhail.vyakhirev.presentation.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.data.model.PhotoItem
import com.mikhail.vyakhirev.data.model.UiModel
import com.mikhail.vyakhirev.utils.DiffUtilCallBack

class ListAdapter(
    val favorStarClickListener: ((photo: PhotoItem) -> Unit)?,
    val photoClickListener: ((photo: PhotoItem) -> Unit)?,
    val posListener:((pos:Int) -> Unit)?
) : PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.list_row) {
            ListViewHolder.create(parent)
        } else {
            SeparatorViewHolder.create(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.Photo -> R.layout.list_row
            is UiModel.SeparatorItem -> R.layout.separator_view_item
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        uiModel.let {
            when (uiModel) {
                is UiModel.Photo -> {
                    (holder as ListViewHolder).bind(uiModel.photoItem)
                    holder.binding?.favorStar?.setOnClickListener {
                        favorStarClickListener?.invoke(uiModel.photoItem)
                        posListener?.invoke(position)
                    }
                    holder.binding?.photoIV?.setOnClickListener {
                        photoClickListener?.invoke(uiModel.photoItem)
                    }
                }
                is UiModel.SeparatorItem -> (holder as SeparatorViewHolder).bind(uiModel.description)
                else -> throw UnsupportedOperationException("Unknown view")
            }
        }
    }
}