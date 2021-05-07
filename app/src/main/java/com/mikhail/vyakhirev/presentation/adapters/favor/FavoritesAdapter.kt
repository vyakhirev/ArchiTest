package com.mikhail.vyakhirev.presentation.adapters.favor

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikhail.vyakhirev.data.model.FavoriteModel
import com.mikhail.vyakhirev.data.model.PhotoItem
import com.mikhail.vyakhirev.presentation.adapters.ListViewHolder

class FavoritesAdapter(
//    private val context: Context,
    private var photos: List<FavoriteModel>,
    val photoClickListener: ((favPhoto: FavoriteModel?) -> Unit)?,
    val favorStarClickListener: ((favorStar: FavoriteModel?) -> Unit)?,
    val posListener: ((pos: Int) -> Unit)?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FavoritesViewHolder.create(parent)
    }

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val uiModel = getItem(position)
        if (holder is FavoritesViewHolder) {
            holder.bind(photos[position])

//        (holder as FavoritesViewHolder).bind(photos[position])
            holder.binding?.favorStar?.setOnClickListener {
                favorStarClickListener?.invoke(photos[position])
                posListener?.invoke(position)
            }
            holder.binding?.photoIV?.setOnClickListener {
                photoClickListener?.invoke(photos[position])
            }
        }

    }

    fun addItems(items: MutableList<PhotoItem?>) {
        items.addAll(items)
    }

    fun update(data: List<FavoriteModel>) {
//        val movieDiffUtilCallback = DiffCallback(photos, data)
//        val diffResult = DiffUtil.calculateDiff(movieDiffUtilCallback)
        photos = data
        notifyDataSetChanged()
//        diffResult.dispatchUpdatesTo(this)
    }
}