package by.vshkl.bashq.ui.common;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentManager;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.util.Calendar;

import by.vshkl.bashq.R;
import by.vshkl.bashq.common.Navigator;
import by.vshkl.mvp.model.Quote;
import by.vshkl.mvp.presenter.QuotesPresenter;
import by.vshkl.mvp.presenter.common.UrlBuilder;

public class DialogHelper {

    public enum DateTypes {
        YEAR,
        YEAR_MONTH,
        YEAR_MONTH_DAY
    }

    public static void showDatePickerDialog(DateTypes dateTypes, Context context, OnDateSetListener listener,
                                            FragmentManager fragmentManager, String tag) {
        Calendar calendar = Calendar.getInstance();

        switch (dateTypes) {
            case YEAR:
                calendar.set(Calendar.YEAR, 2004);
                new TimePickerDialog.Builder()
                        .setCallBack(listener)
                        .setCancelStringId(context.getString(android.R.string.cancel))
                        .setSureStringId(context.getString(android.R.string.ok))
                        .setTitleStringId(context.getString(R.string.date_picker_title_month))
                        .setType(Type.YEAR)
                        .setYearText(context.getString(R.string.date_picker_ext_year))
                        .setThemeColor(context.getResources().getColor(R.color.colorAccent))
                        .setMinMillseconds(calendar.getTimeInMillis())
                        .setMaxMillseconds(System.currentTimeMillis())
                        .setWheelItemTextSize(16)
                        .setCyclic(false)
                        .build()
                        .show(fragmentManager, tag);
                break;
            case YEAR_MONTH:
                calendar.set(Calendar.YEAR, 2004);
                calendar.set(Calendar.MONTH, 8);
                new TimePickerDialog.Builder()
                        .setCallBack(listener)
                        .setCancelStringId(context.getString(android.R.string.cancel))
                        .setSureStringId(context.getString(android.R.string.ok))
                        .setTitleStringId(context.getString(R.string.date_picker_title_month))
                        .setType(Type.YEAR_MONTH)
                        .setYearText(context.getString(R.string.date_picker_ext_year))
                        .setMonthText(context.getString(R.string.date_picker_ext_month))
                        .setThemeColor(context.getResources().getColor(R.color.colorAccent))
                        .setMinMillseconds(calendar.getTimeInMillis())
                        .setMaxMillseconds(System.currentTimeMillis())
                        .setWheelItemTextSize(16)
                        .setCyclic(false)
                        .build()
                        .show(fragmentManager, tag);
                break;
            case YEAR_MONTH_DAY:
                new TimePickerDialog.Builder()
                        .setCallBack(listener)
                        .setCancelStringId(context.getString(android.R.string.cancel))
                        .setSureStringId(context.getString(android.R.string.ok))
                        .setTitleStringId(context.getString(R.string.date_picker_title_abyss))
                        .setType(Type.YEAR_MONTH_DAY)
                        .setYearText(context.getString(R.string.date_picker_ext_year))
                        .setMonthText(context.getString(R.string.date_picker_ext_month))
                        .setDayText(context.getString(R.string.date_picker_ext_day))
                        .setThemeColor(context.getResources().getColor(R.color.colorAccent))
                        .setMinMillseconds(System.currentTimeMillis() - DateUtils.YEAR_IN_MILLIS)
                        .setMaxMillseconds(System.currentTimeMillis())
                        .setWheelItemTextSize(16)
                        .setCyclic(false)
                        .build()
                        .show(fragmentManager, tag);
                break;
        }
    }

    public static void showQuoteActionsBottomSheetDialog(final Context context, final Navigator navigator,
                                                         final QuotesPresenter presenter, final Quote quote,
                                                         final int quotePosition) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.dialog_quote_actions);
        ((TextView) bottomSheetDialog.findViewById(R.id.tv_bs_title))
                .setText(context.getString(R.string.quote_action_title, quote.getId()));

        FrameLayout bsShareLink = (FrameLayout) bottomSheetDialog.findViewById(R.id.bs_share_link);
        bsShareLink.setVisibility(quote.getLink() != null ? View.VISIBLE : View.GONE);

        FrameLayout bsAddToFavourite = (FrameLayout) bottomSheetDialog.findViewById(R.id.bs_favourite_add);
        bsAddToFavourite.setVisibility(quote.getVoteCount() != -1 ? View.VISIBLE : View.GONE);

        FrameLayout bsRemoveFromFavourite = (FrameLayout) bottomSheetDialog.findViewById(R.id.bs_favourite_remove);
        bsRemoveFromFavourite.setVisibility(quote.getVoteCount() != -1 ? View.GONE : View.VISIBLE);

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

        bottomSheetDialog.findViewById(R.id.bs_favourite_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.saveQuote(quote);
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.findViewById(R.id.bs_favourite_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.deleteQuote(quote, quotePosition);
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }
}
