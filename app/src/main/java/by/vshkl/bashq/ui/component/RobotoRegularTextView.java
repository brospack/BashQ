package by.vshkl.bashq.ui.component;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class RobotoRegularTextView extends TextView {

    public RobotoRegularTextView(Context context) {
        super(context);
        setupTypeface(context);
    }

    public RobotoRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupTypeface(context);
    }

    public RobotoRegularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupTypeface(context);
    }

    public RobotoRegularTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupTypeface(context);
    }

    private void setupTypeface(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
    }
}
