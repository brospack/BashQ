package by.vshkl.bashq.ui.acticity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.Calendar;
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
import by.vshkl.bashq.ui.common.PermissionHelper;
import by.vshkl.bashq.ui.component.ComicsImageOverlayView;
import by.vshkl.bashq.ui.component.MarqueeToolbar;
import by.vshkl.mvp.model.ComicsThumbnail;
import by.vshkl.mvp.model.Errors;
import by.vshkl.mvp.presenter.ComicsPresenter;
import by.vshkl.mvp.view.ComicsView;

public class ComicsActivity extends AppCompatActivity implements ComicsView, Spinner.OnItemSelectedListener {

    @Inject
    ComicsPresenter comicsPresenter;
    @Inject
    Navigator navigator;

    @BindView(R.id.toolbar)
    MarqueeToolbar toolbar;
    @BindView(R.id.sp_years)
    Spinner spYears;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.srl_update)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rv_comics)
    RecyclerView rvComics;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 42;

    private List<Integer> years = new ArrayList<>();
    private ComicsComponent comicsComponent;
    private ComicsAdapter comicsAdapter;
    private String comicImageLink;
    private ComicsAdapter.OnComicItemClickListener onComicItemClickListener;
    private ComicsImageOverlayView.OnDownloadClickListener onDownloadClickListener;
    private ComicsImageOverlayView.OnFavouriteClickListener onFavouriteClickListener;
    private ComicsImageOverlayView.OnShareClickListener onShareClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comics);
        ButterKnife.bind(ComicsActivity.this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initializeDaggerComponent(((BashqApplication) getApplication()).getApplicationComponent());
        initializePresenter();
        initializeRecyclerView();
        initializeSpinner();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    navigator.navigateToComicsDownloadImage(ComicsActivity.this, comicImageLink);
                }
        }
    }

    //==================================================================================================================

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        comicsPresenter.setYear(years.get(i));
        comicsPresenter.getComics();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rvComics.setLayoutManager(gridLayoutManager);

        onComicItemClickListener = new ComicsAdapter.OnComicItemClickListener() {
            @Override
            public void onComicItemClicked(int position, String comicsLink, final String comicImageLink) {
                ComicsImageOverlayView overlay = new ComicsImageOverlayView(ComicsActivity.this);
                ComicsActivity.this.comicImageLink = comicImageLink;

                onDownloadClickListener = new ComicsImageOverlayView.OnDownloadClickListener() {
                    @Override
                    public void onDownloadClicked() {
                        PermissionHelper.requestPermission(
                                ComicsActivity.this,
                                navigator,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                REQUEST_WRITE_EXTERNAL_STORAGE,
                                getString(R.string.permission_write_external_storage_title),
                                getString(R.string.permission_write_external_storage_rationale),
                                comicImageLink);
                    }
                };

                onFavouriteClickListener = new ComicsImageOverlayView.OnFavouriteClickListener() {
                    @Override
                    public void onFavouriteClicked() {
                        Toast.makeText(ComicsActivity.this, "Not implemented yet", Toast.LENGTH_SHORT).show();
                    }
                };

                onShareClickListener = new ComicsImageOverlayView.OnShareClickListener() {
                    @Override
                    public void onShareClicked() {
                        navigator.navigateToComicsShareImageChooser(ComicsActivity.this, comicImageLink);
                    }
                };

                overlay.setOnDownloadClickListener(onDownloadClickListener);
                overlay.setOnFavouriteClickListener(onFavouriteClickListener);
                overlay.setOnShareClickListener(onShareClickListener);

                new ImageViewer.Builder(ComicsActivity.this, comicsAdapter.getComicsImageUrls())
                        .setStartPosition(position)
                        .setOverlayView(overlay)
                        .show();
            }
        };

        comicsAdapter = new ComicsAdapter();
        comicsAdapter.setOnComicItemClickListener(onComicItemClickListener);
        rvComics.setAdapter(comicsAdapter);
    }

    private void initializeSpinner() {
        int firstYear = 2007;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = currentYear; i >= firstYear; i--) {
            years.add(i);
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(R.layout.item_spinner);
        spYears.setAdapter(adapter);
        spYears.setOnItemSelectedListener(ComicsActivity.this);
    }

    private void setComics(List<ComicsThumbnail> comics) {
        comicsAdapter.setComics(comics);
        comicsAdapter.notifyDataSetChanged();
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ComicsActivity.class);
    }
}
