package by.vshkl.bashq.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import by.vshkl.bashq.R;
import by.vshkl.bashq.ui.activity.MainActivity;
import by.vshkl.bashq.ui.adapter.ComicsPagerAdapter;

public class ComicsPagerFragment extends Fragment {

    @BindView(R.id.vp_container)
    ViewPager vpContainer;

    private MainActivity parentActivity;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            this.parentActivity = (MainActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comics_pager, container, false);
        unbinder = ButterKnife.bind(ComicsPagerFragment.this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializePageAdapter();
        initializeTabLayout();
        showTabLayout();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideTabLayout();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.parentActivity = null;
    }

    //==================================================================================================================

    private void initializePageAdapter() {
        vpContainer.setAdapter(new ComicsPagerAdapter(getChildFragmentManager(), getActivity()));
    }

    private void initializeTabLayout() {
        parentActivity.setTabLayoutWithViewPager(vpContainer);
    }

    private void showTabLayout() {
        parentActivity.showTabLayout();
    }

    private void hideTabLayout() {
        parentActivity.hideTabLayout();
    }
}
