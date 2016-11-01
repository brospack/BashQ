package by.vshkl.bashq.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.vshkl.bashq.R;

class QuoteViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.number)
    TextView tvNumber;
    @BindView(R.id.date)
    TextView tvDate;
    @BindView(R.id.content)
    TextView tvContent;
    @BindView(R.id.rating)
    TextView tvRating;
    @BindView(R.id.votes)
    RelativeLayout rlVotes;
    @BindView(R.id.votesDivider)
    View vVotesDivider;

    QuoteViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
