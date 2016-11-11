package by.vshkl.bashq.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import by.vshkl.bashq.BashqApplication;
import by.vshkl.bashq.R;
import by.vshkl.bashq.injection.component.ApplicationComponent;
import by.vshkl.bashq.injection.component.DaggerQuotesComponent;
import by.vshkl.bashq.injection.component.QuotesComponent;
import by.vshkl.bashq.injection.module.QuotesModule;
import by.vshkl.bashq.ui.activity.MainActivity;
import by.vshkl.bashq.ui.adapter.EndlessScrollListener;
import by.vshkl.bashq.ui.adapter.HidingScrollListener;
import by.vshkl.bashq.ui.adapter.QuotesAdapter;
import by.vshkl.bashq.ui.adapter.QuotesAdapter.OnQuoteComicLabelClickListener;
import by.vshkl.bashq.ui.adapter.QuotesAdapter.OnQuoteItemLongClickListener;
import by.vshkl.bashq.ui.adapter.QuotesAdapter.OnVoteDownClickListener;
import by.vshkl.bashq.ui.adapter.QuotesAdapter.OnVoteOldClickListener;
import by.vshkl.bashq.ui.adapter.QuotesAdapter.OnVoteUpClickListener;
import by.vshkl.bashq.ui.common.DialogHelper;
import by.vshkl.bashq.ui.common.PermissionHelper;
import by.vshkl.bashq.ui.common.ToolbarTitleHelper;
import by.vshkl.bashq.ui.component.ComicsImageOverlayView;
import by.vshkl.bashq.ui.component.ComicsImageOverlayView.OnDownloadClickListener;
import by.vshkl.bashq.ui.component.ComicsImageOverlayView.OnFavouriteClickListener;
import by.vshkl.bashq.ui.component.ComicsImageOverlayView.OnShareClickListener;
import by.vshkl.mvp.model.Errors;
import by.vshkl.mvp.model.Quote;
import by.vshkl.mvp.presenter.QuotesPresenter;
import by.vshkl.mvp.presenter.common.Subsection;
import by.vshkl.mvp.view.QuotesView;
import xyz.hanks.library.SmallBang;

public class QuotesFragment extends Fragment implements QuotesView, OnQuoteItemLongClickListener,
        OnVoteUpClickListener, OnVoteDownClickListener, OnVoteOldClickListener, OnQuoteComicLabelClickListener,
        OnDownloadClickListener, OnFavouriteClickListener, OnShareClickListener, OnRefreshListener, OnDateSetListener {

    @Inject
    QuotesPresenter quotesPresenter;

    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.srl_update)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rv_quotes)
    RecyclerView rvQuotes;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.fab_calendar_multiple)
    FloatingActionMenu fabCalendarMenu;
    @BindView(R.id.fab_calendar_single)
    FloatingActionButton fabCalendar;

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 42;
    private static final String TAG_CALENDAR_PICKER = "CALENDAR_PICKER";

    private MainActivity parentActivity;
    private QuotesComponent quotesComponent;
    private QuotesAdapter quotesAdapter;
    private EndlessScrollListener scrollListener;
    private String datePart = null;
    private String comicImageLink;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            this.parentActivity = (MainActivity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDaggerComponent(((BashqApplication) parentActivity.getApplication()).getApplicationComponent());
        initializePresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quotes, container, false);
        unbinder = ButterKnife.bind(QuotesFragment.this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initializeSwipeRefreshLayout();
        initializeRecyclerView();
        initializeFloatingActionButtons();
    }

    @Override
    public void onStart() {
        super.onStart();
        quotesPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        quotesPresenter.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.parentActivity = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    parentActivity.getNavigator().navigateToComicsDownloadImage(getContext(), comicImageLink);
                }
        }
    }

    //==================================================================================================================

    @OnClick(R.id.fab_calendar_multiple_month)
    void onFabCalendarMultipleMonthClicked() {
        parentActivity.setCurrentSubsection(Subsection.BEST_MONTH);
        quotesPresenter.setSubsection(parentActivity.getCurrentSubsection());
        DialogHelper.showDatePickerDialog(
                DialogHelper.DateTypes.YEAR_MONTH, getContext(), this, getFragmentManager(), TAG_CALENDAR_PICKER);
        fabCalendarMenu.close(true);
    }

    @OnClick(R.id.fab_calendar_multiple_year)
    void onFabCalendarMultipleYearClicked() {
        parentActivity.setCurrentSubsection(Subsection.BEST_YEAR);
        quotesPresenter.setSubsection(parentActivity.getCurrentSubsection());
        DialogHelper.showDatePickerDialog(
                DialogHelper.DateTypes.YEAR, getContext(), this, getFragmentManager(), TAG_CALENDAR_PICKER);
        fabCalendarMenu.close(true);
    }

    @OnClick(R.id.fab_calendar_single)
    void onFabCalendarSingleClicked() {
        DialogHelper.showDatePickerDialog(
                DialogHelper.DateTypes.YEAR_MONTH_DAY, getContext(), this, getFragmentManager(), TAG_CALENDAR_PICKER);
    }

    //==================================================================================================================

    @Override
    public void showQuotes(final List<Quote> quotes) {
        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addQuotes(quotes);
                parentActivity.setToolbarTitle(ToolbarTitleHelper.GetToolbarTitle(
                        getContext(), parentActivity.getCurrentSubsection(), datePart));
            }
        });
    }

    @Override
    public void showQuoteComicImageDialog(final String imageUrl) {
        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                QuotesFragment.this.comicImageLink = imageUrl;

                ComicsImageOverlayView overlay = new ComicsImageOverlayView(getContext());
                overlay.setOnDownloadClickListener(QuotesFragment.this);
                overlay.setOnFavouriteClickListener(QuotesFragment.this);
                overlay.setOnShareClickListener(QuotesFragment.this);

                new ImageViewer.Builder(getContext(), new String[]{imageUrl})
                        .setOverlayView(overlay)
                        .show();
            }
        });
    }

    @Override
    public void showMessage(final String message) {
        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showLoading() {
        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                flContainer.setVisibility(View.GONE);
                pbProgress.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideLoading() {
        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pbProgress.setVisibility(View.GONE);
                flContainer.setVisibility(View.VISIBLE);
                srlRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void showError(Errors errorType) {
        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void notifyDataSetChanged(int position) {
        quotesAdapter.deleteQuote(position);
        quotesAdapter.notifyItemRemoved(position);
    }

    //==================================================================================================================

    @Override
    public void onRefresh() {
        handleUpdate();
    }

    @Override
    public void onQuoteItemLongClicked(Quote quote, int position) {
        DialogHelper.showQuoteActionsBottomSheetDialog(
                getContext(), parentActivity.getNavigator(), quotesPresenter, quote, position);
    }

    @Override
    public void onVoteDownClicked(String quoteId) {
        quotesPresenter.setVoteQuoteId(quoteId);
        quotesPresenter.setRequiredVoteState(Quote.VoteState.VOTED_DOWN);
        quotesPresenter.voteQuote();
    }

    @Override
    public void onVoteOldClicked(String quoteId) {
        quotesPresenter.setVoteQuoteId(quoteId);
        quotesPresenter.setRequiredVoteState(Quote.VoteState.VOTED_OLD);
        quotesPresenter.voteQuote();
    }

    @Override
    public void onVoteUpClicked(String quoteId) {
        quotesPresenter.setVoteQuoteId(quoteId);
        quotesPresenter.setRequiredVoteState(Quote.VoteState.VOTED_UP);
        quotesPresenter.voteQuote();
    }

    @Override
    public void onQuoteComicLabelClicked(String comicLinkPart) {
        quotesPresenter.setComicUrlPart(comicLinkPart);
        quotesPresenter.getQuoteComicImage();
    }

    @Override
    public void onDownloadClicked() {
        PermissionHelper.requestPermission(
                parentActivity,
                parentActivity.getNavigator(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                REQUEST_WRITE_EXTERNAL_STORAGE,
                getString(R.string.permission_write_external_storage_title),
                getString(R.string.permission_write_external_storage_rationale),
                comicImageLink);
    }

    @Override
    public void onFavouriteClicked() {
        Toast.makeText(getContext(), "Not implemented yet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShareClicked() {
        parentActivity.getNavigator().navigateToComicsShareImageChooser(getContext(), comicImageLink);
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millseconds);
        Locale locale = Locale.getDefault();

        switch (parentActivity.getCurrentSubsection()) {
            case BEST_MONTH:
                datePart = (new StringBuilder()
                        .append(String.format(locale, "%02d", (calendar.get(Calendar.MONTH) + 1)))
                        .append(".")
                        .append(calendar.get(Calendar.YEAR)))
                        .toString();
                quotesAdapter.clearQuotes();
                quotesPresenter.setUrlPartBest(new StringBuilder("/bestmonth/")
                        .append(calendar.get(Calendar.YEAR))
                        .append("/")
                        .append(calendar.get(Calendar.MONTH) + 1)
                        .toString());
                quotesPresenter.getQuotes(false);
                break;
            case BEST_YEAR:
                datePart = String.valueOf(calendar.get(Calendar.YEAR));
                quotesAdapter.clearQuotes();
                quotesPresenter.setUrlPartBest(new StringBuilder("/bestyear/")
                        .append(calendar.get(Calendar.YEAR))
                        .toString());
                quotesPresenter.getQuotes(false);
                break;
            case ABYSS_BEST:
                datePart = (new StringBuilder()
                        .append(String.format(locale, "%02d", calendar.get(Calendar.DAY_OF_MONTH)))
                        .append(".")
                        .append(String.format(locale, "%02d", (calendar.get(Calendar.MONTH) + 1)))
                        .append(".")
                        .append(calendar.get(Calendar.YEAR)))
                        .toString();
                quotesAdapter.clearQuotes();
                quotesPresenter.setUrlPartBest(new StringBuilder(String.valueOf(calendar.get(Calendar.YEAR)))
                        .append(String.format(locale, "%02d", (calendar.get(Calendar.MONTH) + 1)))
                        .append(String.format(locale, "%02d", calendar.get(Calendar.DAY_OF_MONTH)))
                        .toString());
                quotesPresenter.getQuotes(false);
                break;
        }
    }

    //==================================================================================================================

    private void initializeDaggerComponent(ApplicationComponent applicationComponent) {
        quotesComponent = DaggerQuotesComponent.builder()
                .quotesModule(new QuotesModule())
                .applicationComponent(applicationComponent)
                .build();
        quotesComponent.inject(QuotesFragment.this);
    }

    private void initializePresenter() {
        quotesPresenter.attachView(QuotesFragment.this);
        quotesPresenter.setSubsection(parentActivity.getCurrentSubsection());
    }

    private void initializeRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvQuotes.setLayoutManager(linearLayoutManager);

        quotesAdapter = new QuotesAdapter();
        quotesAdapter.setSmallBang(SmallBang.attach2Window(parentActivity));
        rvQuotes.setAdapter(quotesAdapter);

        rvQuotes.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideUiElements();
            }

            @Override
            public void onShow() {
                showUiElements();
            }
        });

        scrollListener = new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isScopeCanLoadMore()) {
                    quotesPresenter.getQuotes(true);
                }
            }
        };
        rvQuotes.addOnScrollListener(scrollListener);

        quotesAdapter.setOnVoteUpClickListener(QuotesFragment.this);
        quotesAdapter.setOnVoteDownClickListener(QuotesFragment.this);
        quotesAdapter.setOnVoteOldClickListener(QuotesFragment.this);
        quotesAdapter.setOnQuoteItemLongClickListener(QuotesFragment.this);
        quotesAdapter.setOnQuoteComicLabelClickListener(QuotesFragment.this);
    }

    private void initializeSwipeRefreshLayout() {
        srlRefresh.setOnRefreshListener(QuotesFragment.this);
        srlRefresh.setColorSchemeResources(R.color.colorAccent);
    }

    private void initializeFloatingActionButtons() {
        fabCalendar.setColorNormalResId(R.color.colorButtonNormal);
        fabCalendar.setColorPressedResId(R.color.colorButtonPressed);
        fabCalendarMenu.setMenuButtonColorNormalResId(R.color.colorButtonNormal);
        fabCalendarMenu.setMenuButtonColorPressedResId(R.color.colorButtonPressed);
        toggleFloatingActionButton();
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

    private void handleUpdate() {
        datePart = null;
        quotesAdapter.clearQuotes();
        scrollListener.resetState();
        quotesPresenter.setUrlPartBest(null);
        quotesPresenter.getQuotes(false);
    }

    private void hideUiElements() {
        if (isScopeBest()) {
            fabCalendarMenu.hideMenuButton(true);
        } else if (isScopeBestAbyss()) {
            fabCalendar.hide(true);
        }
    }

    private void showUiElements() {
        if (isScopeBest()) {
            fabCalendarMenu.showMenuButton(true);
        } else if (isScopeBestAbyss()) {
            fabCalendar.show(true);
        }
    }

    private void addQuotes(List<Quote> quotes) {
        quotesAdapter.addQuotes(quotes);
        quotesAdapter.notifyDataSetChanged();
    }

    //==================================================================================================================

    private boolean isScopeBest() {
        Subsection currentSubsection = parentActivity.getCurrentSubsection();
        return currentSubsection == Subsection.BEST || currentSubsection == Subsection.BEST_YEAR
                || currentSubsection == Subsection.BEST_MONTH;
    }

    private boolean isScopeBestAbyss() {
        Subsection currentSubsection = parentActivity.getCurrentSubsection();
        return currentSubsection == Subsection.ABYSS_BEST;
    }

    private boolean isScopeCanLoadMore() {
        Subsection currentSubsection = parentActivity.getCurrentSubsection();
        return currentSubsection == Subsection.INDEX || currentSubsection == Subsection.RANDOM
                || currentSubsection == Subsection.BY_RATING || currentSubsection == Subsection.ABYSS
                || currentSubsection == Subsection.ABYSS_BEST;
    }
}
