package by.vshkl.bashq.ui.acticity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(ComicsActivity.this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeDaggerComponent(((BashqApplication) getApplication()).getApplicationComponent());
    }

    //==================================================================================================================

    @Override
    public void showComics(List<ComicsThumbnail> comics) {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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
}
