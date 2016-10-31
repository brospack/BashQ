package by.vshkl.bashq.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import by.vshkl.bashq.R;
import by.vshkl.mvp.model.Quote;

public class QuotesAdapter extends RecyclerView.Adapter<QuoteViewHolder> {

    private List<Quote> quotes = new ArrayList<>();

    @Override
    public QuoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quote, parent, false);
        return new QuoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(QuoteViewHolder holder, int position) {
        final Quote quote = quotes.get(position);

        holder.tvNumber.setText(quote.getId());
        holder.tvDate.setText(quote.getDate());
        holder.tvContent.setText(quote.getContent());
        holder.tvRating.setText(quote.getRating());
    }

    @Override
    public int getItemCount() {
        return (this.quotes != null) ? this.quotes.size() : 0;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }
}
