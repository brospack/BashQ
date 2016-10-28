package by.vshkl.bashq.ui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import android.widget.ImageView
import by.vshkl.bashq.R
import by.vshkl.bashq.model.Quote
import by.vshkl.bashq.model.Rating
import by.vshkl.bashq.utils.Ratings
import by.vshkl.bashq.view.QuoteActionListener
import kotlinx.android.synthetic.main.item_quote.view.*

class QuotesViewHolder(view: View, val listener: QuoteActionListener) :
        RecyclerView.ViewHolder(view) {

    fun bindQuote(quote: Quote) {
        with(quote) {
            itemView.number.text = quote.id
            itemView.date.text = quote.date
            itemView.content.text = Html.fromHtml(quote.content).trim()
            dropTint(itemView.context, itemView.votePlus, itemView.voteMinus, itemView.voteOld, 1)

            if (quote.rating.equals("")) {
                itemView.votes.visibility = View.GONE
                itemView.votesDivider.visibility = View.GONE
            } else {
                itemView.rating.text = quote.rating

                itemView.votePlus.setOnClickListener {
                    listener.vote(voteUp, "rulez")
                    val ratingObj = Ratings.updateRating(Rating(quote.rating, quote.voteCount), 1)
                    quote.voteCount = ratingObj.voteCount
                    itemView.rating.text = ratingObj.rating
                    tintDrawables(itemView.context, itemView.votePlus, itemView.voteMinus, itemView.voteOld, 1)
                }

                itemView.voteMinus.setOnClickListener {
                    listener.vote(voteDown, "sux")
                    val ratingObj = Ratings.updateRating(Rating(quote.rating, quote.voteCount), -1)
                    quote.voteCount = ratingObj.voteCount
                    itemView.rating.text = ratingObj.rating
                    tintDrawables(itemView.context, itemView.votePlus, itemView.voteMinus, itemView.voteOld, 2)
                }

                itemView.voteOld.setOnClickListener {
                    listener.vote(voteOld, "bayan")
                    val ratingObj = Ratings.updateRating(Rating(quote.rating, quote.voteCount), 0)
                    quote.voteCount = ratingObj.voteCount
                    itemView.rating.text = ratingObj.rating
                    tintDrawables(itemView.context, itemView.votePlus, itemView.voteMinus, itemView.voteOld, 3)
                }

                itemView.setOnLongClickListener { listener.share(content, link) }
            }
        }
    }

    fun dropTint(context: Context, plus: ImageView, minus: ImageView, old: ImageView, which: Int) {
        DrawableCompat.setTint(plus.drawable, ContextCompat.getColor(context, R.color.colorSecondaryText))
        DrawableCompat.setTint(minus.drawable, ContextCompat.getColor(context, R.color.colorSecondaryText))
        DrawableCompat.setTint(old.drawable, ContextCompat.getColor(context, R.color.colorSecondaryText))
    }

    fun tintDrawables(context: Context, plus: ImageView, minus: ImageView, old: ImageView, which: Int) {
        when (which) {
            1 -> {
                DrawableCompat.setTint(plus.drawable, ContextCompat.getColor(context, R.color.colorAccent))
                DrawableCompat.setTint(minus.drawable, ContextCompat.getColor(context, R.color.colorSecondaryText))
                DrawableCompat.setTint(old.drawable, ContextCompat.getColor(context, R.color.colorSecondaryText))
            }
            2 -> {
                DrawableCompat.setTint(plus.drawable, ContextCompat.getColor(context, R.color.colorSecondaryText))
                DrawableCompat.setTint(minus.drawable, ContextCompat.getColor(context, R.color.colorAccent))
                DrawableCompat.setTint(old.drawable, ContextCompat.getColor(context, R.color.colorSecondaryText))
            }
            3 -> {
                DrawableCompat.setTint(plus.drawable, ContextCompat.getColor(context, R.color.colorSecondaryText))
                DrawableCompat.setTint(minus.drawable, ContextCompat.getColor(context, R.color.colorSecondaryText))
                DrawableCompat.setTint(old.drawable, ContextCompat.getColor(context, R.color.colorAccent))
            }
        }
    }
}
