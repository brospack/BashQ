package by.vshkl.bashq.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import by.vshkl.bashq.R;
import by.vshkl.mvp.model.Quote;
import xyz.hanks.library.SmallBang;

public class QuotesAdapter extends RecyclerView.Adapter<QuoteViewHolder> {

    public interface OnVoteUpClickListener {
        void onVoteUpClicked(String quoteId);
    }

    public interface OnVoteDownClickListener {
        void onVoteDownClicked(String quoteId);
    }

    public interface OnVoteOldClickListener {
        void onVoteOldClicked(String quoteId);
    }

    private List<Quote> quotes = new ArrayList<>();
    private SmallBang bang;
    private OnVoteUpClickListener onVoteUpClickListener;
    private OnVoteDownClickListener onVoteDownClickListener;
    private OnVoteOldClickListener onVoteOldClickListener;

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
        holder.tvContent.setText(Html.fromHtml(quote.getContent().trim()));

        if (quote.getRating() == null) {
            holder.rlVotes.setVisibility(View.GONE);
            holder.vVotesDivider.setVisibility(View.GONE);
        } else {
            holder.rlVotes.setVisibility(View.VISIBLE);
            holder.vVotesDivider.setVisibility(View.VISIBLE);
            holder.tvRating.setText(quote.getRating());
            holder.ivVoteUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bang.bang(view, 75, null);
                    if (onVoteUpClickListener != null) {
                        onVoteUpClickListener.onVoteUpClicked(quote.getId());
                    }
                }
            });
            holder.ivVoteDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bang.bang(view, 75, null);
                    if (onVoteDownClickListener != null) {
                        onVoteDownClickListener.onVoteDownClicked(quote.getId());
                    }
                }
            });
            holder.ivVoteOld.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bang.bang(view, 75, null);
                    if (onVoteOldClickListener != null) {
                        onVoteOldClickListener.onVoteOldClicked(quote.getId());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (this.quotes != null) ? this.quotes.size() : 0;
    }

    public void addQuotes(List<Quote> quotes) {
        this.quotes.addAll(quotes);
    }

    public void clearQuotes() {
        this.quotes.clear();
    }

    public void setSmallBang(SmallBang bang) {
        this.bang = bang;
        bang.setColors(new int[]{0xFF757575, 0xFFFFC107});
        bang.setDotNumber(25);
    }

    public void setOnVoteUpClickListener(OnVoteUpClickListener onVoteUpClickListener) {
        this.onVoteUpClickListener = onVoteUpClickListener;
    }

    public void setOnVoteDownClickListener(OnVoteDownClickListener onVoteDownClickListener) {
        this.onVoteDownClickListener = onVoteDownClickListener;
    }

    public void setOnVoteOldClickListener(OnVoteOldClickListener onVoteOldClickListener) {
        this.onVoteOldClickListener = onVoteOldClickListener;
    }
}
