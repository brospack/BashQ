package by.vshkl.bashq.ui.common;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import by.vshkl.bashq.ui.acticity.QuotesActivity;
import by.vshkl.mvp.presenter.common.Subsection;

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
}
