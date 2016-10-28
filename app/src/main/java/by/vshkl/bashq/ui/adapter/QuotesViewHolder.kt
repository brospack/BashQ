package by.vshkl.bashq.ui.adapter

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
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
                }

                itemView.voteMinus.setOnClickListener {
                    listener.vote(voteDown, "sux")
                    val ratingObj = Ratings.updateRating(Rating(quote.rating, quote.voteCount), -1)
                    quote.voteCount = ratingObj.voteCount
                    itemView.rating.text = ratingObj.rating
                }

                itemView.voteOld.setOnClickListener {
                    listener.vote(voteOld, "bayan")
                    val ratingObj = Ratings.updateRating(Rating(quote.rating, quote.voteCount), 0)
                    quote.voteCount = ratingObj.voteCount
                    itemView.rating.text = ratingObj.rating
                }

                itemView.setOnLongClickListener { listener.share(content, link) }
            }
        }
    }
}
