package by.vshkl.bashq.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import by.vshkl.bashq.R
import by.vshkl.bashq.model.Quote
import by.vshkl.bashq.view.QuoteActionListener

class QuotesAdapter(val quotes: List<Quote>, val listener: QuoteActionListener) :
        RecyclerView.Adapter<QuotesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_quote, parent, false)
        return QuotesViewHolder(v, listener)
    }

    override fun onBindViewHolder(holder: QuotesViewHolder, position: Int) {
        holder.bindQuote(quotes[position])
    }

    override fun getItemCount() = quotes.size
}

