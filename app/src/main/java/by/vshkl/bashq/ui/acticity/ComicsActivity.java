package by.vshkl.bashq.ui.acticity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.vshkl.bashq.BashqApplication;
import by.vshkl.bashq.R;
import by.vshkl.bashq.common.Navigator;
import by.vshkl.bashq.injection.component.ApplicationComponent;
import by.vshkl.bashq.injection.component.ComicsComponent;
import by.vshkl.bashq.injection.component.DaggerComicsComponent;
import by.vshkl.bashq.injection.module.ActivityModule;
import by.vshkl.bashq.injection.module.ComicsModule;
import by.vshkl.bashq.injection.module.NavigationModule;
import by.vshkl.bashq.ui.adapter.ComicsAdapter;
import by.vshkl.bashq.ui.component.MarqueeToolbar;
import by.vshkl.mvp.model.ComicsThumbnail;
import by.vshkl.mvp.model.Errors;
import by.vshkl.mvp.presenter.ComicsPresenter;
import by.vshkl.mvp.view.ComicsView;

public class ComicsActivity extends AppCompatActivity implements ComicsView {

    @Inject
    ComicsPresenter comicsPresenter;
    @Inject
    Navigator navigator;

    @BindView(R.id.toolbar)
    MarqueeToolbar toolbar;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.srl_update)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rv_comics)
    RecyclerView rvComics;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;

    private ComicsComponent comicsComponent;
    private ComicsAdapter comicsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comics);
        ButterKnife.bind(ComicsActivity.this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeDaggerComponent(((BashqApplication) getApplication()).getApplicationComponent());
        initializeRecyclerView();
        initializePresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        comicsPresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        comicsPresenter.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    //==================================================================================================================

    @Override
    public void showComics(final List<ComicsThumbnail> comics) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setComics(comics);
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

    private void initializeDaggerComponent(ApplicationComponent applicationComponent) {
        comicsComponent = DaggerComicsComponent.builder()
                .comicsModule(new ComicsModule())
                .activityModule(new ActivityModule(ComicsActivity.this))
                .navigationModule(new NavigationModule())
                .applicationComponent(applicationComponent)
                .build();
        comicsComponent.inject(ComicsActivity.this);
    }

    private void initializePresenter() {
        comicsPresenter.attachView(ComicsActivity.this);
    }

    private void initializeRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvComics.setLayoutManager(gridLayoutManager);

        comicsAdapter = new ComicsAdapter();
        rvComics.setAdapter(comicsAdapter);
    }

    private void setComics(List<ComicsThumbnail> comics) {
        comicsAdapter.setComics(comics);
        comicsAdapter.notifyDataSetChanged();
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ComicsActivity.class);
    }
}
