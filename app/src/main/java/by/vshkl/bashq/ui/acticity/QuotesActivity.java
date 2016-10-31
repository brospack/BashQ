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

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.vshkl.bashq.BashqApplication;
import by.vshkl.bashq.R;
import by.vshkl.bashq.common.Navigator;
import by.vshkl.bashq.injection.component.ApplicationComponent;
import by.vshkl.bashq.injection.component.DaggerQuotesComponent;
import by.vshkl.bashq.injection.component.QuotesComponent;
import by.vshkl.bashq.injection.module.ActivityModule;
import by.vshkl.bashq.injection.module.NavigationModule;
import by.vshkl.bashq.injection.module.QuotesModule;
import by.vshkl.bashq.ui.adapter.QuotesAdapter;
import by.vshkl.mvp.model.Errors;
import by.vshkl.mvp.model.Quote;
import by.vshkl.mvp.presenter.QuotesPresenter;
import by.vshkl.mvp.presenter.common.Subsection;
import by.vshkl.mvp.view.QuotesView;

public class QuotesActivity extends AppCompatActivity implements QuotesView {

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
    @BindView(R.id.progress)
    ProgressBar pbProgress;

    private QuotesComponent quotesComponent;
    private QuotesAdapter quotesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);
        ButterKnife.bind(QuotesActivity.this);

        setSupportActionBar(toolbar);

        initializeNavigationDrawer(QuotesActivity.this, toolbar, savedInstanceState);
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

    //==================================================================================================================

    private void initializeNavigationDrawer(AppCompatActivity activity, Toolbar toolbar, Bundle savedInstanceState) {
        new DrawerBuilder().withActivity(activity)
                .withToolbar(toolbar)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.nd_new).withIcon(R.drawable.ic_new).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.nd_random).withIcon(R.drawable.ic_random).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.nd_best).withIcon(R.drawable.ic_best).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.nd_rating).withIcon(R.drawable.ic_rating).withIdentifier(4),
                        new PrimaryDrawerItem().withName(R.string.nd_abyss).withIcon(R.drawable.ic_abyss).withIdentifier(5),
                        new PrimaryDrawerItem().withName(R.string.nd_abyss_top).withIcon(R.drawable.ic_abyss_top).withIdentifier(6),
                        new PrimaryDrawerItem().withName(R.string.nd_abyss_best).withIcon(R.drawable.ic_abyss_best).withIdentifier(7),
                        new PrimaryDrawerItem().withName(R.string.nd_comics).withIcon(R.drawable.ic_comics).withIdentifier(8),
                        new SectionDrawerItem().withName(R.string.nd_fav),
                        new PrimaryDrawerItem().withName(R.string.nd_fav_quotes).withIcon(R.drawable.ic_favourite).withIdentifier(9),
                        new PrimaryDrawerItem().withName(R.string.nd_fav_comics).withIcon(R.drawable.ic_favourite).withIdentifier(10),
                        new SectionDrawerItem().withName(R.string.nd_other),
                        new PrimaryDrawerItem().withName(R.string.nd_settings).withIcon(R.drawable.ic_settings).withIdentifier(11)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case 1:
                                quotesPresenter.setSubsection(Subsection.INDEX);
                                quotesPresenter.getQuotes();
                                break;
                            case 2:
                                quotesPresenter.setSubsection(Subsection.RANDOM);
                                quotesPresenter.getQuotes();
                                break;
                            case 3:
                                quotesPresenter.setSubsection(Subsection.BEST);
                                quotesPresenter.getQuotes();
                                break;
                            case 4:
                                quotesPresenter.setSubsection(Subsection.BY_RATING);
                                quotesPresenter.getQuotes();
                                break;
                            case 5:
                                quotesPresenter.setSubsection(Subsection.ABYSS);
                                quotesPresenter.getQuotes();
                                break;
                            case 6:
                                quotesPresenter.setSubsection(Subsection.ABYSS_TOP);
                                quotesPresenter.getQuotes();
                                break;
                            case 7:
                                quotesPresenter.setSubsection(Subsection.ABYSS_BEST);
                                quotesPresenter.getQuotes();
                                break;
                            case 8:
                            case 9:
                            case 10:
                            case 11:
                        }
                        return false;
                    }
                })
                .build();
    }

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
        quotesPresenter.setSubsection(Subsection.INDEX);
    }

    private void initializeRecyclerView() {
        quotesAdapter = new QuotesAdapter();
        rvQuotes.setLayoutManager(new LinearLayoutManager(this));
        rvQuotes.setAdapter(quotesAdapter);
    }

    private void addQuotes(List<Quote> quotes) {
        quotesAdapter.setQuotes(quotes);
        quotesAdapter.notifyDataSetChanged();
    }
}
