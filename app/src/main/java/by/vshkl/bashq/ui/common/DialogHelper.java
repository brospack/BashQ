package by.vshkl.bashq.ui.common;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.view.View;
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

    public static void showDatePickerDialog(final DateTypes dateTypes, final Context context,
                                            final OnDateSetListener listener,
                                            final FragmentManager fragmentManager, final String tag) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog.Builder builder = new TimePickerDialog.Builder()
                .setCallBack(listener)
                .setCancelStringId(context.getString(android.R.string.cancel))
                .setSureStringId(context.getString(android.R.string.ok))
                .setThemeColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setWheelItemTextSize(16)
                .setCyclic(false);

        switch (dateTypes) {
            case YEAR:
                calendar.set(Calendar.YEAR, 2004);
                builder.setType(Type.YEAR)
                        .setYearText("")
                        .setTitleStringId(context.getString(R.string.date_picker_title_year))
                        .setMinMillseconds(calendar.getTimeInMillis())
                        .setMaxMillseconds(System.currentTimeMillis())
                        .build()
                        .show(fragmentManager, tag);
                break;
            case YEAR_MONTH:
                calendar.set(Calendar.YEAR, 2004);
                calendar.set(Calendar.MONTH, 8);
                builder.setType(Type.YEAR_MONTH)
                        .setTitleStringId(context.getString(R.string.date_picker_title_month))
                        .setYearText("")
                        .setMonthText("")
                        .setMinMillseconds(calendar.getTimeInMillis())
                        .setMaxMillseconds(System.currentTimeMillis())
                        .build()
                        .show(fragmentManager, tag);
                break;
            case YEAR_MONTH_DAY:
                builder.setType(Type.YEAR_MONTH_DAY)
                        .setTitleStringId(context.getString(R.string.date_picker_title_abyss))
                        .setYearText("")
                        .setMonthText("")
                        .setDayText("")
                        .setMinMillseconds(System.currentTimeMillis() - DateUtils.YEAR_IN_MILLIS)
                        .setMaxMillseconds(System.currentTimeMillis())
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

        TextView tvTitle = (TextView) bottomSheetDialog.findViewById(R.id.tv_bs_title);
        View ivLink = bottomSheetDialog.findViewById(R.id.iv_bs_link);
        View tvLink = bottomSheetDialog.findViewById(R.id.tv_bs_link);
        View tvText = bottomSheetDialog.findViewById(R.id.tv_bs_text);
        View ivFavAdd = bottomSheetDialog.findViewById(R.id.iv_bs_favourite_add);
        TextView tvFavAdd = (TextView) bottomSheetDialog.findViewById(R.id.tv_bs_favourite_add);

        if (tvTitle != null) {
            tvTitle.setText(context.getString(R.string.quote_action_title, quote.getId()));
        }

        if (ivLink != null && tvLink != null) {
            ivLink.setVisibility(quote.getLink() != null ? View.VISIBLE : View.GONE);
            tvLink.setVisibility(quote.getLink() != null ? View.VISIBLE : View.GONE);
            tvLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigator.navigateToQuoteShareLinkChooser(context, UrlBuilder.BuildQuoteUrl(quote.getId()));
                    bottomSheetDialog.dismiss();
                }
            });
        }

        if (tvText != null) {
            tvText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigator.navigateToQuoteShareTextChooser(context, quote.getContent());
                    bottomSheetDialog.dismiss();
                }
            });
        }

        if (ivFavAdd != null && tvFavAdd != null) {
            tvFavAdd.setText(quote.getVoteCount() == -1
                    ? context.getString(R.string.quote_action_remove_from_favourite)
                    : context.getString(R.string.quote_action_add_to_favourite));
            tvFavAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (quote.getVoteCount()) {
                        case -1:
                            presenter.deleteQuote(quote, quotePosition);
                            break;
                        default:
                            presenter.saveQuote(quote);
                            break;
                    }
                    bottomSheetDialog.dismiss();
                }
            });
        }

        bottomSheetDialog.show();
    }
}
