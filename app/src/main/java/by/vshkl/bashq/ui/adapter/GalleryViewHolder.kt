package by.vshkl.bashq.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import by.vshkl.bashq.model.Comic
import by.vshkl.bashq.view.GalleryActionListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_comic.view.*

class GalleryViewHolder(view: View, val listener: GalleryActionListener, val context: Context) :
        RecyclerView.ViewHolder(view) {

    fun bindComic(comic: Comic) {
        with(comic) {
            Picasso.with(context).load(comic.thumbLink).into(itemView.image)
            itemView.image.setOnClickListener { listener.onGalleryItemClicked(comic.imageLink, itemView) }
        }
    }
}
