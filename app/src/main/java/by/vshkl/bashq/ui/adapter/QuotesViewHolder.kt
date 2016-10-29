package by.vshkl.bashq.ui.adapter

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import by.vshkl.bashq.R
import by.vshkl.bashq.model.Quote
import by.vshkl.bashq.model.Rating
import by.vshkl.bashq.utils.Ratings
import by.vshkl.bashq.view.QuoteActionListener
import kotlinx.android.synthetic.main.item_quote.view.*
import xyz.hanks.library.SmallBang

class QuotesViewHolder(view: View, val listener: QuoteActionListener) :
        RecyclerView.ViewHolder(view) {

    fun bindQuote(activity: AppCompatActivity, quote: Quote) {
        val smallBang: SmallBang = SmallBang.attach2Window(activity)
        smallBang.setColors(intArrayOf(R.color.colorAccent, R.color.colorAccent, R.color.colorAccent))
        smallBang.setDotNumber(25)

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
                    smallBang.bang(itemView.votePlus)

                    listener.vote(voteUp, "rulez")
                    val ratingObj = Ratings.updateRating(Rating(quote.rating, quote.voteCount), 1)
                    quote.voteCount = ratingObj.voteCount
                    itemView.rating.text = ratingObj.rating
                }

                itemView.voteMinus.setOnClickListener {
                    smallBang.bang(itemView.voteMinus)

                    listener.vote(voteDown, "sux")
                    val ratingObj = Ratings.updateRating(Rating(quote.rating, quote.voteCount), -1)
                    quote.voteCount = ratingObj.voteCount
                    itemView.rating.text = ratingObj.rating
                }

                itemView.voteOld.setOnClickListener {
                    smallBang.bang(itemView.voteOld)

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
