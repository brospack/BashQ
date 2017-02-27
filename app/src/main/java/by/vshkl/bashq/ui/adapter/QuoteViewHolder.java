package by.vshkl.bashq.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.vshkl.bashq.R;
import by.vshkl.bashq.ui.component.RobotoMediumTextView;
import by.vshkl.bashq.ui.component.RobotoRegularTextView;

class QuoteViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.number) RobotoMediumTextView tvNumber;
    @BindView(R.id.date) RobotoMediumTextView tvDate;
    @BindView(R.id.content) RobotoRegularTextView tvContent;
    @BindView(R.id.comic) RobotoMediumTextView tvComicLabel;
    @BindView(R.id.rating) RobotoMediumTextView tvRating;
    @BindView(R.id.votes) RelativeLayout rlVotes;
    @BindView(R.id.votesDivider) View vVotesDivider;
    @BindView(R.id.voteUp) ImageView ivVoteUp;
    @BindView(R.id.voteDown) ImageView ivVoteDown;
    @BindView(R.id.voteOld) ImageView ivVoteOld;

    QuoteViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
