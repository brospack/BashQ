package by.vshkl.bashq.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import by.vshkl.bashq.ui.fragment.ComicsFragment;

public class ComicsPagerAdapter extends FragmentPagerAdapter {

    private List<Integer> years = new ArrayList<>();

    public ComicsPagerAdapter(FragmentManager fm) {
        super(fm);
        initializeYearsList();
    }

    @Override
    public Fragment getItem(int position) {
        return ComicsFragment.newInstance(years.get(position));
    }

    @Override
    public int getCount() {
        return years != null ? years.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(years.get(position));
    }

    //==================================================================================================================

    private void initializeYearsList() {
        int firstYear = 2007;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = currentYear; i >= firstYear; i--) {
            years.add(i);
        }
    }
}
