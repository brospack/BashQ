package by.vshkl.bashq.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import by.vshkl.bashq.R;
import by.vshkl.bashq.common.Navigator;
import by.vshkl.bashq.ui.acticity.QuotesActivity;
import by.vshkl.mvp.model.Quote;
import by.vshkl.mvp.presenter.common.Subsection;
import by.vshkl.mvp.presenter.common.UrlBuilder;

public class DialogHelper {

    public static void showDatePickerDialog(QuotesActivity activity, Subsection currentSubsection) {
        Calendar calendarMaxDate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                activity,
                calendarMaxDate.get(Calendar.YEAR),
                calendarMaxDate.get(Calendar.MONTH),
                calendarMaxDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setMaxDate(calendarMaxDate);

        Calendar calendarMinDate = Calendar.getInstance();
        if (currentSubsection == Subsection.ABYSS_BEST) {
            calendarMinDate.set(Calendar.YEAR, calendarMaxDate.get(Calendar.YEAR) - 1);
        } else {
            calendarMinDate.set(Calendar.YEAR, 2004);
            calendarMinDate.set(Calendar.MONTH, 8);
            calendarMinDate.set(Calendar.DAY_OF_MONTH, 1);
        }
        datePickerDialog.setMinDate(calendarMinDate);

        datePickerDialog.showYearPickerFirst(true);

        datePickerDialog.show(activity.getFragmentManager(), "Pick a date");
    }

    public static void showQuoteActionsBottomSheetDialog(final Context context, final Navigator navigator,
                                                         final Quote quote) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.dialog_quote_actions);
        ((TextView) bottomSheetDialog.findViewById(R.id.tv_bs_title))
                .setText(context.getString(R.string.quote_action_title, quote.getId()));

        FrameLayout bsShareLink = (FrameLayout) bottomSheetDialog.findViewById(R.id.bs_share_link);
        bsShareLink.setVisibility(quote.getLink() != null ? View.VISIBLE : View.GONE);
        bottomSheetDialog.findViewById(R.id.bs_share_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigator.navigateToQuoteShareLinkChooser(context, UrlBuilder.BuildQuoteUrl(quote.getId()));
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.findViewById(R.id.bs_share_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigator.navigateToQuoteShareTextChooser(context, quote.getContent());
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.findViewById(R.id.bs_favourite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bottomSheetDialog.show();
    }

    public static void showQuoteComucImageDialog(final QuotesActivity activity, final String imageUrl) {
        Target loadTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                final Dialog imageDialog = new Dialog(activity);
                imageDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                imageDialog.setContentView(activity.getLayoutInflater().inflate(R.layout.dialog_image_view, null));
                ((ImageView) imageDialog.findViewById(R.id.dialog_image)).setImageBitmap(bitmap);
                imageDialog.show();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(activity).load(imageUrl).into(loadTarget);
    }
}
