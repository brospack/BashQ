package by.vshkl.bashq.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.vshkl.bashq.BashqApplication;
import by.vshkl.bashq.R;
import by.vshkl.bashq.common.Navigator;
import by.vshkl.bashq.injection.component.ApplicationComponent;
import by.vshkl.bashq.injection.component.DaggerMainActivityComponent;
import by.vshkl.bashq.injection.component.MainActivityComponent;
import by.vshkl.bashq.injection.module.ActivityModule;
import by.vshkl.bashq.injection.module.NavigationModule;
import by.vshkl.bashq.ui.common.DrawerHelper;
import by.vshkl.bashq.ui.component.MarqueeToolbar;
import by.vshkl.bashq.ui.fragment.ComicsPagerFragment;
import by.vshkl.bashq.ui.fragment.QuotesFragment;
import by.vshkl.mvp.presenter.common.Subsection;

public class MainActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {

    private static final String CURRENT_SUBSECTION = "by.vshkl.bashq.ui.activity.MainActivity.CURRENT_SUBSECTION";
    private static final String FRAGMENT_TAG = "by.vshkl.bashq.ui.activity.MainActivity.FRAGMENT_TAG";

    @Inject
    Navigator navigator;

    @BindView(R.id.toolbar)
    MarqueeToolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    private MainActivityComponent mainActivityComponent;
    private Subsection currentSubsection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);

        setSupportActionBar(toolbar);

        DrawerHelper.initializeDrawer(MainActivity.this, toolbar, savedInstanceState, MainActivity.this);
        initializeDaggerComponent(((BashqApplication) getApplication()).getApplicationComponent());

        if (savedInstanceState != null) {
            currentSubsection = (Subsection) savedInstanceState.getSerializable(CURRENT_SUBSECTION);
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
            if (fragment == null || fragment instanceof QuotesFragment) {
                handleDrawerSectionClick(currentSubsection);
            }
        } else {
            handleDrawerSectionClick(Subsection.INDEX);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BashqApplication.getRefWatcher(this).watch(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_SUBSECTION, currentSubsection);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        switch ((int) drawerItem.getIdentifier()) {
            case 1:
                handleDrawerSectionClick(Subsection.INDEX);
                break;
            case 2:
                handleDrawerSectionClick(Subsection.RANDOM);
                break;
            case 3:
                handleDrawerSectionClick(Subsection.BEST);
                break;
            case 4:
                handleDrawerSectionClick(Subsection.BY_RATING);
                break;
            case 5:
                handleDrawerSectionClick(Subsection.ABYSS);
                break;
            case 6:
                handleDrawerSectionClick(Subsection.ABYSS_TOP);
                break;
            case 7:
                handleDrawerSectionClick(Subsection.ABYSS_BEST);
                break;
            case 8:
                handleDrawerSectionClick(Subsection.COMICS);
                break;
            case 9:
                handleDrawerSectionClick(Subsection.FAVOURITE_QUOTES);
                break;
            case 10:
                Toast.makeText(this, "¯\\_(ツ)_/¯", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    //==================================================================================================================

    public Subsection getCurrentSubsection() {
        return currentSubsection;
    }

    public void setCurrentSubsection(Subsection currentSubsection) {
        this.currentSubsection = currentSubsection;
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public void setToolbarTitle(String toolbarTitle) {
        toolbar.setTitle(toolbarTitle);
    }

    public void setTabLayoutWithViewPager(ViewPager viewPager) {
        tabLayout.setupWithViewPager(viewPager);
    }

    public void showTabLayout() {
        tabLayout.setVisibility(View.VISIBLE);
    }

    public void hideTabLayout() {
        tabLayout.setVisibility(View.GONE);
    }

    //==================================================================================================================

    private void initializeDaggerComponent(ApplicationComponent applicationComponent) {
        mainActivityComponent = DaggerMainActivityComponent.builder()
                .activityModule(new ActivityModule(MainActivity.this))
                .navigationModule(new NavigationModule())
                .applicationComponent(applicationComponent)
                .build();
        mainActivityComponent.inject(MainActivity.this);
    }

    private void handleDrawerSectionClick(Subsection subsection) {
        currentSubsection = subsection;
        dispatchFragments();
    }

    private void dispatchFragments() {
        if (currentSubsection != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (currentSubsection) {
                case COMICS:
                    fragmentTransaction.replace(R.id.fragment_placeholder, new ComicsPagerFragment(), FRAGMENT_TAG);
                    break;
                case FAVOURITE_QUOTES:
                    fragmentTransaction.replace(R.id.fragment_placeholder, new QuotesFragment(), FRAGMENT_TAG);
                    break;
                case SETTINGS:
                    break;
                default:
                    fragmentTransaction.replace(R.id.fragment_placeholder, new QuotesFragment(), FRAGMENT_TAG);
                    break;
            }
            fragmentTransaction.commit();
        }
    }
}
