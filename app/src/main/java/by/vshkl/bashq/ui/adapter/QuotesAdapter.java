package by.vshkl.bashq.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;

import java.util.ArrayList;
import java.util.List;

import by.vshkl.bashq.R;
import by.vshkl.bashq.ui.common.RatingHelper;
import by.vshkl.mvp.model.Quote;
import by.vshkl.mvp.model.Rating;
import xyz.hanks.library.SmallBang;


import static by.vshkl.mvp.model.Quote.VoteState.VOTED_DOWN;
import static by.vshkl.mvp.model.Quote.VoteState.VOTED_OLD;
import static by.vshkl.mvp.model.Quote.VoteState.VOTED_UP;

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

    public interface OnQuoteItemLongClickListener {
        void onQuoteItemLongClicked(Quote quote, int position);
    }

    public interface OnQuoteComicLabelClickListener {
        void onQuoteComicLabelClicked(String comicLinkPart);
    }

    private static final int adStep = 10;
    private int quoteTextSize;
    private List<Quote> quotes = new ArrayList<>();
    private SmallBang bang;
    private OnVoteUpClickListener onVoteUpClickListener;
    private OnVoteDownClickListener onVoteDownClickListener;
    private OnVoteOldClickListener onVoteOldClickListener;
    private OnQuoteItemLongClickListener onQuoteItemLongClickListener;
    private OnQuoteComicLabelClickListener onQuoteComicLabelClickListener;
    private AdRequest adRequest;

    public QuotesAdapter(int quoteTextSize) {
        this.quoteTextSize = quoteTextSize;
        adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
    }

    @Override
    public QuoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quote, parent, false);
        return new QuoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final QuoteViewHolder holder, int position) {
        final Quote quote = quotes.get(position);
        final int quotePosition = position;

        if (quote == null) {
            holder.avNativeAd.loadAd(adRequest);
            holder.rvQuotesContainer.setVisibility(View.GONE);
            holder.avNativeAd.setVisibility(View.VISIBLE);
            return;
        } else {
            holder.avNativeAd.setVisibility(View.GONE);
            holder.rvQuotesContainer.setVisibility(View.VISIBLE);
        }

        holder.tvNumber.setText(quote.getId());
        holder.tvDate.setText(quote.getDate());
        holder.tvContent.setTextSize(quoteTextSize);
        holder.tvContent.setText(Html.fromHtml(quote.getContent().trim()));
        holder.tvContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onQuoteItemLongClickListener != null) {
                    onQuoteItemLongClickListener.onQuoteItemLongClicked(quote, holder.getAdapterPosition());
                }
                return true;
            }
        });

        if (quote.getRating() == null) {
            changeVoteSectionVisibility(holder, View.GONE);
        } else {
            changeVoteSectionVisibility(holder, View.VISIBLE);

            updateVoteStateImage(quote.getVoteState(), holder);

            holder.tvRating.setText(quote.getRating());
            holder.ivVoteUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bang.bang(view, 75, null);
                    if (onVoteUpClickListener != null) {
                        if (quote.getVoteCount() == 0) {
                            onVoteUpClickListener.onVoteUpClicked(quote.getId());
                        }
                        notifyDataSetChanged();
                    }
                    updateVoteState(VOTED_UP, holder, quotePosition);
                }
            });
            holder.ivVoteDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bang.bang(view, 75, null);
                    if (onVoteDownClickListener != null) {
                        if (quote.getVoteCount() == 0) {
                            onVoteDownClickListener.onVoteDownClicked(quote.getId());
                        }
                        notifyDataSetChanged();
                    }
                    updateVoteState(VOTED_DOWN, holder, quotePosition);
                }
            });
            holder.ivVoteOld.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bang.bang(view, 75, null);
                    if (onVoteOldClickListener != null) {
                        if (quote.getVoteCount() == 0) {
                            onVoteOldClickListener.onVoteOldClicked(quote.getId());
                        }
                        notifyDataSetChanged();
                    }
                    updateVoteState(VOTED_OLD, holder, quotePosition);
                }
            });
        }

        if (quote.getComicLink() != null) {
            holder.tvComicLabel.setVisibility(View.VISIBLE);
            holder.tvComicLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onQuoteComicLabelClickListener != null) {
                        onQuoteComicLabelClickListener.onQuoteComicLabelClicked(quote.getComicLink());
                    }
                }
            });
        } else {
            holder.tvComicLabel.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (this.quotes != null) ? this.quotes.size() : 0;
    }

    //==================================================================================================================

    private void updateVoteState(Quote.VoteState voteState, QuoteViewHolder holder, int position) {
        Quote quote = quotes.get(position);

        quote.setVoteState(voteState);
        updateVoteStateImage(voteState, holder);

        Rating rating = RatingHelper.updateRating(voteState, new Rating(quote.getRating(), quote.getVoteCount()));

        quote.setVoteCount(rating.getVoteCount());
        quote.setRating(rating.getRating());

        quotes.set(position, quote);
    }

    private void updateVoteStateImage(Quote.VoteState voteState, QuoteViewHolder holder) {
        switch (voteState) {
            case VOTED_UP:
                holder.ivVoteUp.setImageResource(R.drawable.ic_plus_selected);
                holder.ivVoteDown.setImageResource(R.drawable.ic_minus);
                holder.ivVoteOld.setImageResource(R.drawable.ic_crux);
                break;
            case VOTED_DOWN:
                holder.ivVoteUp.setImageResource(R.drawable.ic_plus);
                holder.ivVoteDown.setImageResource(R.drawable.ic_minus_selected);
                holder.ivVoteOld.setImageResource(R.drawable.ic_crux);
                break;
            case VOTED_OLD:
                holder.ivVoteUp.setImageResource(R.drawable.ic_plus);
                holder.ivVoteDown.setImageResource(R.drawable.ic_minus);
                holder.ivVoteOld.setImageResource(R.drawable.ic_crux_selected);
                break;
            default:
                holder.ivVoteUp.setImageResource(R.drawable.ic_plus);
                holder.ivVoteDown.setImageResource(R.drawable.ic_minus);
                holder.ivVoteOld.setImageResource(R.drawable.ic_crux);
                break;
        }
    }

    public void addQuotes(List<Quote> quotes) {
        int count = quotes.size();
        for (int i = 0; i < count; i++) {
            if (i != 0 && i % adStep == 0) {
                this.quotes.add(null);
            }
            if (!this.quotes.contains(quotes.get(i))) {
                this.quotes.add(quotes.get(i));
            }
        }
    }

    public void deleteQuote(int position) {
        quotes.remove(position);
    }

    public void clearQuotes() {
        this.quotes.clear();
    }

    public void setSmallBang(SmallBang bang) {
        this.bang = bang;
        bang.setColors(new int[]{0xFFFFC107, 0xFFFFC107});
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

    public void setOnQuoteItemLongClickListener(OnQuoteItemLongClickListener onQuoteItemLongClickListener) {
        this.onQuoteItemLongClickListener = onQuoteItemLongClickListener;
    }

    public void setOnQuoteComicLabelClickListener(OnQuoteComicLabelClickListener onQuoteComicLabelClickListener) {
        this.onQuoteComicLabelClickListener = onQuoteComicLabelClickListener;
    }

    private void changeVoteSectionVisibility(QuoteViewHolder holder, int visibility) {
        holder.ivVoteOld.setVisibility(visibility);
        holder.ivVoteUp.setVisibility(visibility);
        holder.ivVoteDown.setVisibility(visibility);
        holder.tvRating.setVisibility(visibility);
        holder.vVotesDivider.setVisibility(visibility);
    }
}
