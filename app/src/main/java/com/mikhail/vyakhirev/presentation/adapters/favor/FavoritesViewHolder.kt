package com.mikhail.vyakhirev.presentation.adapters.favor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.data.model.FavoriteModel
import com.mikhail.vyakhirev.databinding.ListRowBinding

class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var binding: ListRowBinding? = null

    @SuppressLint("ResourceAsColor")
    fun bind(item: FavoriteModel) {
        binding = ListRowBinding.bind(itemView)
        binding?.photoIV?.loadImageFromLink(item.imageUrl)

        binding?.titleTV?.setTextColor(R.color.colorPrimary)
        binding?.titleTV?.text = item.title

        binding?.favorStar?.setImageResource(
            if (item.isFavorite)
                R.drawable.ic_star_on else R.drawable.ic_star_off
        )

    }

    private fun ImageView.loadImageFromLink(link: String?) {
        if (!link.isNullOrEmpty()) {
            Glide.with(context.applicationContext)
                .load(link)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(this)
        }
    }

    companion object {
        fun create(parent: ViewGroup): FavoritesViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_row, parent, false)
            return FavoritesViewHolder(view)
        }
    }

}