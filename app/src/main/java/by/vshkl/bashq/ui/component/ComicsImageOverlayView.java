package by.vshkl.bashq.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import by.vshkl.bashq.R;

public class ComicsImageOverlayView extends RelativeLayout {

    public OnDownloadClickListener onDownloadClickListener;
    public OnShareClickListener onShareClickListener;

    public interface OnDownloadClickListener {
        void onDownloadClicked();
    }

    public interface OnShareClickListener {
        void onShareClicked();
    }

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

    public void setOnDownloadClickListener(OnDownloadClickListener onDownloadClickListener) {
        this.onDownloadClickListener = onDownloadClickListener;
    }

    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }

    private void initialize() {
        View view = inflate(getContext(), R.layout.overlay_comics, this);

        ImageView imgDownload = (ImageView) view.findViewById(R.id.img_comics_overlay_action_download);
        ImageView imgShare = (ImageView) view.findViewById(R.id.img_comics_overlay_action_share);

        imgDownload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDownloadClickListener != null) {
                    onDownloadClickListener.onDownloadClicked();
                }
            }
        });

        imgShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onShareClickListener != null) {
                    onShareClickListener.onShareClicked();
                }
            }
        });
    }
}
