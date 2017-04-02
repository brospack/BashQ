package by.vshkl.bashq.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.NativeExpressAdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.vshkl.bashq.R;
import by.vshkl.bashq.ui.view.RobotoMediumTextView;
import by.vshkl.bashq.ui.view.RobotoRegularTextView;

class QuoteViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.vs_quote) ViewStub vsQuote;
    @BindView(R.id.vs_ad) ViewStub vsAd;

    QuoteViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public static class AdView {
        @BindView(R.id.av_nativeAd) NativeExpressAdView avNativeAd;

        public AdView(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public static class QuoteView {
        @BindView(R.id.number) RobotoMediumTextView tvNumber;
        @BindView(R.id.date) RobotoMediumTextView tvDate;
        @BindView(R.id.content) RobotoRegularTextView tvContent;
        @BindView(R.id.comic) RobotoMediumTextView tvComicLabel;
        @BindView(R.id.rating) RobotoMediumTextView tvRating;
        @BindView(R.id.votesDivider) View vVotesDivider;
        @BindView(R.id.voteUp) ImageView ivVoteUp;
        @BindView(R.id.voteDown) ImageView ivVoteDown;
        @BindView(R.id.voteOld) ImageView ivVoteOld;

        public QuoteView(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
