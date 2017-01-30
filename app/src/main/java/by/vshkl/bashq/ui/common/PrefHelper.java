package by.vshkl.bashq.ui.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import by.vshkl.bashq.R;

public class PrefHelper {

    public static int getQuoteFontSize(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(sharedPreferences.getString(
                context.getString(R.string.pref_font_size_key),
                context.getString(R.string.pref_font_size_default)));
    }
}
