package by.vshkl.bashq.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import by.vshkl.bashq.R;

public class ComicsImageOverlayView extends RelativeLayout {

    private ImageView imgDownload;
    private ImageView imgFavourite;
    private ImageView imgShare;

    public ComicsImageOverlayView(Context context) {
        super(context);
        initialize();
    }

    public ComicsImageOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ComicsImageOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public ComicsImageOverlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }


    private void initialize() {
        View view = inflate(getContext(), R.layout.overlay_comics, this);

        imgDownload = (ImageView) view.findViewById(R.id.img_comics_overlay_action_download);
        imgFavourite = (ImageView) view.findViewById(R.id.img_comics_overlay_action_favourite);
        imgShare = (ImageView) view.findViewById(R.id.img_comics_overlay_action_share);

        imgDownload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        imgFavourite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        imgShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
