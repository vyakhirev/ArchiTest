package com.mikhail.vyakhirev.presentation.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.data.model.PhotoItem
import com.mikhail.vyakhirev.databinding.ListRowBinding
import com.mikhail.vyakhirev.presentation.list_fragment.ListMyFragmentDirections


class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var binding: ListRowBinding? = null

    fun bind(item: PhotoItem) {
        binding = ListRowBinding.bind(itemView)

        binding?.photoIV?.loadImageFromLink(item.getFlickrImageLink())

        binding?.photoIV?.setOnClickListener {
            navigateToDetail(it,item)
        }

        binding?.titleTV?.setTextColor(Color.parseColor("#0972C5"))
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
        fun create(parent: ViewGroup): ListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_row, parent, false)
            return ListViewHolder(view)
        }
    }

    private fun navigateToDetail(view: View,photoItem: PhotoItem) {
        val direction = ListMyFragmentDirections.actionListMyFragmentToDetailFragment(photoItem)
        view.findNavController().navigate(direction)
    }
}