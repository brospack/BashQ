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
import by.vshkl.bashq.ui.adapter.QuoteViewHolder.AdView;
import by.vshkl.bashq.ui.adapter.QuoteViewHolder.QuoteView;
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

    private QuoteView quoteView;

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
            AdView adView = new AdView(holder.vsAd.inflate());
            adView.avNativeAd.loadAd(adRequest);
            return;
        } else {
            quoteView = new QuoteView(holder.vsQuote.inflate());
        }

        quoteView.tvNumber.setText(quote.getId());
        quoteView.tvDate.setText(quote.getDate());
        quoteView.tvContent.setTextSize(quoteTextSize);
        quoteView.tvContent.setText(Html.fromHtml(quote.getContent().trim()));
        quoteView.tvContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onQuoteItemLongClickListener != null) {
                    onQuoteItemLongClickListener.onQuoteItemLongClicked(quote, holder.getAdapterPosition());
                }
                return true;
            }
        });

        if (quote.getRating() == null) {
            changeVoteSectionVisibility(View.GONE);
        } else {
            changeVoteSectionVisibility(View.VISIBLE);

            updateVoteStateImage(quote.getVoteState());

            quoteView.tvRating.setText(quote.getRating());
            quoteView.ivVoteUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bang.bang(view, 75, null);
                    if (onVoteUpClickListener != null) {
                        if (quote.getVoteCount() == 0) {
                            onVoteUpClickListener.onVoteUpClicked(quote.getId());
                        }
                        notifyDataSetChanged();
                    }
                    updateVoteState(VOTED_UP, quotePosition);
                }
            });
            quoteView.ivVoteDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bang.bang(view, 75, null);
                    if (onVoteDownClickListener != null) {
                        if (quote.getVoteCount() == 0) {
                            onVoteDownClickListener.onVoteDownClicked(quote.getId());
                        }
                        notifyDataSetChanged();
                    }
                    updateVoteState(VOTED_DOWN, quotePosition);
                }
            });
            quoteView.ivVoteOld.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bang.bang(view, 75, null);
                    if (onVoteOldClickListener != null) {
                        if (quote.getVoteCount() == 0) {
                            onVoteOldClickListener.onVoteOldClicked(quote.getId());
                        }
                        notifyDataSetChanged();
                    }
                    updateVoteState(VOTED_OLD, quotePosition);
                }
            });
        }

        if (quote.getComicLink() != null) {
            quoteView.tvComicLabel.setVisibility(View.VISIBLE);
            quoteView.tvComicLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onQuoteComicLabelClickListener != null) {
                        onQuoteComicLabelClickListener.onQuoteComicLabelClicked(quote.getComicLink());
                    }
                }
            });
        } else {
            quoteView.tvComicLabel.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (this.quotes != null) ? this.quotes.size() : 0;
    }

    //==================================================================================================================

    private void updateVoteState(Quote.VoteState voteState, int position) {
        Quote quote = quotes.get(position);

        quote.setVoteState(voteState);
        updateVoteStateImage(voteState);

        Rating rating = RatingHelper.updateRating(voteState, new Rating(quote.getRating(), quote.getVoteCount()));

        quote.setVoteCount(rating.getVoteCount());
        quote.setRating(rating.getRating());

        quotes.set(position, quote);
    }

    private void updateVoteStateImage(Quote.VoteState voteState) {
        switch (voteState) {
            case VOTED_UP:
                quoteView.ivVoteUp.setImageResource(R.drawable.ic_plus_selected);
                quoteView.ivVoteDown.setImageResource(R.drawable.ic_minus);
                quoteView.ivVoteOld.setImageResource(R.drawable.ic_crux);
                break;
            case VOTED_DOWN:
                quoteView.ivVoteUp.setImageResource(R.drawable.ic_plus);
                quoteView.ivVoteDown.setImageResource(R.drawable.ic_minus_selected);
                quoteView.ivVoteOld.setImageResource(R.drawable.ic_crux);
                break;
            case VOTED_OLD:
                quoteView.ivVoteUp.setImageResource(R.drawable.ic_plus);
                quoteView.ivVoteDown.setImageResource(R.drawable.ic_minus);
                quoteView.ivVoteOld.setImageResource(R.drawable.ic_crux_selected);
                break;
            default:
                quoteView.ivVoteUp.setImageResource(R.drawable.ic_plus);
                quoteView.ivVoteDown.setImageResource(R.drawable.ic_minus);
                quoteView.ivVoteOld.setImageResource(R.drawable.ic_crux);
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

    private void changeVoteSectionVisibility(int visibility) {
        quoteView.ivVoteOld.setVisibility(visibility);
        quoteView.ivVoteUp.setVisibility(visibility);
        quoteView.ivVoteDown.setVisibility(visibility);
        quoteView.tvRating.setVisibility(visibility);
        quoteView.vVotesDivider.setVisibility(visibility);
    }
}
