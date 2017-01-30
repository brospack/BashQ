package by.vshkl.bashq.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import by.vshkl.bashq.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
