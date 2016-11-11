package by.vshkl.bashq.ui.common;

import android.content.Context;

import by.vshkl.bashq.R;
import by.vshkl.mvp.presenter.common.Subsection;

public class ToolbarTitleHelper {

    public static String GetToolbarTitle(Context context, Subsection subsection, String parameter) {
        switch (subsection) {
            case INDEX:
                return context.getString(R.string.nd_new);
            case RANDOM:
                return context.getString(R.string.nd_random);
            case BEST:
                return context.getString(R.string.nd_best_day_week);
            case BEST_YEAR:
                if (parameter == null) {
                    return context.getString(R.string.nd_best_day_week);
                } else {
                    return context.getString(R.string.nd_best_year, parameter);
                }
            case BEST_MONTH:
                if (parameter == null) {
                    return context.getString(R.string.nd_best_day_week);
                } else {
                    return context.getString(R.string.nd_best_month, parameter);
                }
            case BY_RATING:
                return context.getString(R.string.nd_rating);
            case ABYSS:
                return context.getString(R.string.nd_abyss);
            case ABYSS_TOP:
                return context.getString(R.string.nd_abyss_top);
            case ABYSS_BEST:
                if (parameter == null) {
                    return context.getString(R.string.nd_abyss_best);
                } else {
                    return context.getString(R.string.nd_abyss_best_param, parameter);
                }
            case COMICS:
                return context.getString(R.string.nd_comics);
            case FAVOURITE_QUOTES:
                return context.getString(R.string.nd_fav_quotes);
            case FAVOURITE_COMICS:
                return context.getString(R.string.nd_fav_comics);
            case SETTINGS:
                return context.getString(R.string.nd_settings);
            default:
                return context.getString(R.string.app_name);
        }
    }
}
