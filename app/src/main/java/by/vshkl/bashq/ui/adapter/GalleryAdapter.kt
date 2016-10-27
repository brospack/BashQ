package by.vshkl.bashq.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import by.vshkl.bashq.R
import by.vshkl.bashq.model.Comic
import by.vshkl.bashq.view.GalleryActionListener

class GalleryAdapter(val comics: List<Comic>, val listener: GalleryActionListener, val context: Context) :
        RecyclerView.Adapter<GalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_comic, parent, false)
        return GalleryViewHolder(v, listener, context)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bindComic(comics[position])
    }

    override fun getItemCount() = comics.size
}
