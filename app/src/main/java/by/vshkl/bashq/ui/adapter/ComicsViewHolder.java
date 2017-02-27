package by.vshkl.bashq.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.vshkl.bashq.R;

class ComicsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.img_comics_thumbnail) SimpleDraweeView imgThumbnail;

    ComicsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
