package by.vshkl.bashq.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import by.vshkl.bashq.R;
import by.vshkl.bashq.common.Navigator;

public class ComicsImageOverlayView extends RelativeLayout {

    private Navigator navigator;
    private String comicsLink;
    private String comicImageLink;

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

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    public void setComicsLink(String comicsLink) {
        this.comicsLink = comicsLink;
    }

    public void setComicImageLink(String comicImageLink) {
        this.comicImageLink = comicImageLink;
    }

    private void initialize() {
        View view = inflate(getContext(), R.layout.overlay_comics, this);

        ImageView imgDownload = (ImageView) view.findViewById(R.id.img_comics_overlay_action_download);
        ImageView imgFavourite = (ImageView) view.findViewById(R.id.img_comics_overlay_action_favourite);
        ImageView imgShare = (ImageView) view.findViewById(R.id.img_comics_overlay_action_share);

        imgDownload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                navigator.navigateToComicsDownloadImage(getContext(), comicImageLink);
            }
        });

        imgFavourite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });

        imgShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                navigator.navigateToComicsShareImageChooser(getContext(), comicImageLink);
            }
        });
    }
}
