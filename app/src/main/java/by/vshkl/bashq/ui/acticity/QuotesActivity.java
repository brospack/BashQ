package by.vshkl.bashq.ui.acticity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import by.vshkl.bashq.BashqApplication;
import by.vshkl.bashq.R;
import by.vshkl.bashq.common.Navigator;
import by.vshkl.bashq.injection.component.ApplicationComponent;
import by.vshkl.bashq.injection.component.DaggerQuotesComponent;
import by.vshkl.bashq.injection.component.QuotesComponent;
import by.vshkl.bashq.injection.module.ActivityModule;
import by.vshkl.bashq.injection.module.NavigationModule;
import by.vshkl.bashq.injection.module.QuotesModule;
import by.vshkl.bashq.ui.adapter.EndlessScrollListener;
import by.vshkl.bashq.ui.adapter.HidingScrollListener;
import by.vshkl.bashq.ui.adapter.QuotesAdapter;
import by.vshkl.bashq.ui.common.DialogHelper;
import by.vshkl.bashq.ui.common.DrawerHelper;
import by.vshkl.mvp.model.Errors;
import by.vshkl.mvp.model.Quote;
import by.vshkl.mvp.presenter.QuotesPresenter;
import by.vshkl.mvp.presenter.common.Subsection;
import by.vshkl.mvp.view.QuotesView;

public class QuotesActivity extends AppCompatActivity implements QuotesView, Drawer.OnDrawerItemClickListener, DatePickerDialog.OnDateSetListener {

    @Inject
    QuotesPresenter quotesPresenter;
    @Inject
    Navigator navigator;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.srl_update)
    SwipeRefreshLayout srlUpdate;
    @BindView(R.id.rv_quotes)
    RecyclerView rvQuotes;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.fab_calendar_multiple)
    FloatingActionMenu fabCalendarMenu;
    @BindView(R.id.fab_calendar_single)
    FloatingActionButton fabCalendar;

    private QuotesComponent quotesComponent;
    private QuotesAdapter quotesAdapter;
    private EndlessScrollListener scrollListener;
    private Subsection currentSubsection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);
        ButterKnife.bind(QuotesActivity.this);

        setSupportActionBar(toolbar);

        DrawerHelper.initializeDrawer(QuotesActivity.this, toolbar, savedInstanceState, QuotesActivity.this);
        initializeDaggerComponent(((BashqApplication) getApplication()).getApplicationComponent());
        initializePresenter();
        initializeRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        quotesPresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        quotesPresenter.onStop();
    }

    //==================================================================================================================

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        switch ((int) drawerItem.getIdentifier()) {
            case 1:
                handleSectionClicked(Subsection.INDEX, false);
                break;
            case 2:
                handleSectionClicked(Subsection.RANDOM, false);
                break;
            case 3:
                handleSectionClicked(Subsection.BEST, false);
                break;
            case 4:
                handleSectionClicked(Subsection.BY_RATING, false);
                break;
            case 5:
                handleSectionClicked(Subsection.ABYSS, false);
                break;
            case 6:
                handleSectionClicked(Subsection.ABYSS_TOP, false);
                break;
            case 7:
                handleSectionClicked(Subsection.ABYSS_BEST, false);
                break;
            case 8:
            case 9:
            case 10:
            case 11:
        }
        return false;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        switch (currentSubsection) {
            case BEST_MONTH:
                quotesPresenter.setUrlPartBest("/bestmonth/" + year + "/" + monthOfYear);
                quotesAdapter.clearQuotes();
                quotesPresenter.getQuotes(false);
                break;
            case BEST_YEAR:
                quotesPresenter.setUrlPartBest("/bestyear/" + year);
                quotesAdapter.clearQuotes();
                quotesPresenter.getQuotes(false);
                break;
            case ABYSS_BEST:
                Locale locale = Locale.getDefault();
                quotesPresenter.setUrlPartBest(String.valueOf(year)
                        + String.format(locale, "%02d", monthOfYear) + String.format(locale, "%02d", dayOfMonth));
                quotesAdapter.clearQuotes();
                quotesPresenter.getQuotes(false);
                break;
        }
    }

    //==================================================================================================================

    @OnClick(R.id.toolbar)
    void onToolbarClicked() {
        rvQuotes.scrollToPosition(0);
    }

    @OnClick(R.id.fab_calendar_multiple_month)
    void onFabCalendarMultipleMonthClicked() {
        currentSubsection = Subsection.BEST_MONTH;
        quotesPresenter.setSubsection(currentSubsection);
        showDatePickerDialog();
    }

    @OnClick(R.id.fab_calendar_multiple_year)
    void onFabCalendarMultipleYearClicked() {
        currentSubsection = Subsection.BEST_YEAR;
        quotesPresenter.setSubsection(currentSubsection);
        showDatePickerDialog();
    }

    @OnClick(R.id.fab_calendar_single)
    void onFabCalendarSingleClicked() {
        showDatePickerDialog();
    }

    //==================================================================================================================

    @Override
    public void showQuotes(final List<Quote> quotes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addQuotes(quotes);
            }
        });
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                flContainer.setVisibility(View.GONE);
                pbProgress.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pbProgress.setVisibility(View.GONE);
                flContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void showError(Errors errorType) {

    }

    @Override
    public void showMessage(String message) {

    }

    //==================================================================================================================

    private void initializeDaggerComponent(ApplicationComponent applicationComponent) {
        quotesComponent = DaggerQuotesComponent.builder()
                .quotesModule(new QuotesModule())
                .activityModule(new ActivityModule(QuotesActivity.this))
                .navigationModule(new NavigationModule())
                .applicationComponent(applicationComponent)
                .build();
        quotesComponent.inject(QuotesActivity.this);
    }

    private void initializePresenter() {
        quotesPresenter.attachView(QuotesActivity.this);
        currentSubsection = Subsection.INDEX;
        quotesPresenter.setSubsection(currentSubsection);
        toggleFloatingActionButton();
    }

    private void initializeRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvQuotes.setLayoutManager(linearLayoutManager);

        quotesAdapter = new QuotesAdapter();
        rvQuotes.setAdapter(quotesAdapter);

        rvQuotes.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                if (isScopeBest()) {
                    fabCalendarMenu.hideMenuButton(true);
                } else if (isScopeBestAbyss()) {
                    fabCalendar.hide(true);
                }
            }

            @Override
            public void onShow() {
                if (isScopeBest()) {
                    fabCalendarMenu.showMenuButton(true);
                } else if (isScopeBestAbyss()) {
                    fabCalendar.show(true);
                }
            }
        });

        scrollListener = new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isScopeCanLoadMore()) {
                    quotesPresenter.getQuotes(true);
                } else {
                    // TODO: show message that end of list reached and instruction about what to do with dat problem
                }
            }
        };
        rvQuotes.addOnScrollListener(scrollListener);
    }

    private void toggleFloatingActionButton() {
        if (isScopeBest()) {
            fabCalendar.setVisibility(View.GONE);
            fabCalendarMenu.setVisibility(View.VISIBLE);
        } else if (isScopeBestAbyss()) {
            fabCalendar.setVisibility(View.VISIBLE);
            fabCalendarMenu.setVisibility(View.GONE);
        } else {
            fabCalendar.setVisibility(View.GONE);
            fabCalendarMenu.setVisibility(View.GONE);
        }
    }

    private void showDatePickerDialog() {
        DialogHelper.showDatePickerDialog(QuotesActivity.this, currentSubsection);
    }

    private void handleSectionClicked(Subsection subsection, boolean next) {
        currentSubsection = subsection;
        toggleFloatingActionButton();
        quotesPresenter.setSubsection(currentSubsection);
        quotesAdapter.clearQuotes();
        scrollListener.resetState();
        quotesPresenter.getQuotes(next);
        //TODO: add colour tint to images
    }

    private void addQuotes(List<Quote> quotes) {
        quotesAdapter.addQuotes(quotes);
        quotesAdapter.notifyDataSetChanged();
    }

    //==================================================================================================================

    private boolean isScopeBest() {
        return currentSubsection == Subsection.BEST || currentSubsection == Subsection.BEST_YEAR
                || currentSubsection == Subsection.BEST_MONTH;
    }

    private boolean isScopeBestAbyss() {
        return currentSubsection == Subsection.ABYSS_BEST;
    }

    private boolean isScopeCanLoadMore() {
        return currentSubsection == Subsection.INDEX || currentSubsection == Subsection.RANDOM
                || currentSubsection == Subsection.BY_RATING || currentSubsection == Subsection.ABYSS
                || currentSubsection == Subsection.ABYSS_BEST;
    }
}
