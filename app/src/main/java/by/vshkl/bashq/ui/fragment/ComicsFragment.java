package by.vshkl.bashq.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import by.vshkl.bashq.BashqApplication;
import by.vshkl.bashq.R;
import by.vshkl.bashq.injection.component.ApplicationComponent;
import by.vshkl.bashq.injection.component.ComicsComponent;
import by.vshkl.bashq.injection.component.DaggerComicsComponent;
import by.vshkl.bashq.injection.module.ComicsModule;
import by.vshkl.bashq.ui.activity.MainActivity;
import by.vshkl.bashq.ui.adapter.ComicsAdapter;
import by.vshkl.bashq.ui.adapter.ComicsAdapter.OnComicItemClickListener;
import by.vshkl.bashq.ui.common.NetworkStateHelper;
import by.vshkl.bashq.ui.common.PermissionHelper;
import by.vshkl.bashq.ui.component.ComicsImageOverlayView;
import by.vshkl.bashq.ui.component.ComicsImageOverlayView.OnDownloadClickListener;
import by.vshkl.bashq.ui.component.ComicsImageOverlayView.OnShareClickListener;
import by.vshkl.mvp.model.ComicsThumbnail;
import by.vshkl.mvp.model.Errors;
import by.vshkl.mvp.presenter.ComicsPresenter;
import by.vshkl.mvp.view.ComicsView;

public class ComicsFragment extends Fragment implements ComicsView, OnComicItemClickListener, OnRefreshListener,
        OnDownloadClickListener, OnShareClickListener {

    @Inject ComicsPresenter comicsPresenter;

    @BindView(R.id.fl_container) FrameLayout flContainer;
    @BindView(R.id.srl_update) SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rv_comics) RecyclerView rvComics;
    @BindView(R.id.pb_progress) ProgressBar pbProgress;

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 42;
    private static final String KEY_YEAR = "year";

    private MainActivity parentActivity;
    private ComicsComponent comicsComponent;
    private ComicsAdapter comicsAdapter;
    private String comicImageLink;
    private Unbinder unbinder;

    public static ComicsFragment newInstance(int year) {
        Bundle args = new Bundle();
        args.putInt(KEY_YEAR, year);

        ComicsFragment fragment = new ComicsFragment();
        fragment.setArguments(args);

        return fragment;
    }

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
        View view = inflater.inflate(R.layout.fragment_comics, container, false);
        unbinder = ButterKnife.bind(ComicsFragment.this, view);

        int year = getArguments().getInt(KEY_YEAR);
        if (NetworkStateHelper.isConnected(getContext())) {
            getComicsForYear(year);
        } else {
            handleNoConnection();
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeSwipeRefreshLayout();
        initializeRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (NetworkStateHelper.isConnected(getContext())) {
            comicsPresenter.onStart();
        } else {
            handleNoConnection();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        comicsPresenter.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        BashqApplication.getRefWatcher(getActivity()).watch(this);
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

    @Override
    public void showComics(final List<ComicsThumbnail> comics) {
        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setComics(comics);
                parentActivity.setToolbarTitle(getString(R.string.comics_title));
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
    public void showMessage(String message) {
        Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show();
    }

    //==================================================================================================================

    @Override
    public void onRefresh() {
        handleUpdate();
    }

    @Override
    public void onComicItemClicked(int position, String comicsLink, String comicImageLink) {
        ComicsFragment.this.comicImageLink = comicImageLink;
        ComicsImageOverlayView overlay = new ComicsImageOverlayView(getContext());
        overlay.setOnDownloadClickListener(ComicsFragment.this);
        overlay.setOnShareClickListener(ComicsFragment.this);

        new ImageViewer.Builder(getContext(), comicsAdapter.getComicsImageUrls())
                .setStartPosition(position)
                .setOverlayView(overlay)
                .show();
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
    public void onShareClicked() {
        parentActivity.getNavigator().navigateToComicsShareImageChooser(getContext(), comicImageLink);
    }

    //==================================================================================================================

    private void initializeDaggerComponent(ApplicationComponent applicationComponent) {
        comicsComponent = DaggerComicsComponent.builder()
                .comicsModule(new ComicsModule())
                .applicationComponent(applicationComponent)
                .build();
        comicsComponent.inject(ComicsFragment.this);
    }

    private void initializePresenter() {
        comicsPresenter.attachView(ComicsFragment.this);
    }

    private void initializeRecyclerView() {
        GridLayoutManager gridLayoutManager = null;
        switch (parentActivity.getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                gridLayoutManager = new GridLayoutManager(getContext(), 4);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                gridLayoutManager = new GridLayoutManager(getContext(), 6);
                break;
        }

        rvComics.setLayoutManager(gridLayoutManager);
        comicsAdapter = new ComicsAdapter();
        comicsAdapter.setOnComicItemClickListener(ComicsFragment.this);
        rvComics.setAdapter(comicsAdapter);
    }

    private void initializeSwipeRefreshLayout() {
        srlRefresh.setOnRefreshListener(ComicsFragment.this);
        srlRefresh.setColorSchemeResources(R.color.colorAccent);
    }

    private void handleUpdate() {
        if (NetworkStateHelper.isConnected(getContext())) {
            comicsAdapter.clearComics();
            comicsPresenter.getComics();
        } else {
            handleNoConnection();
        }
    }

    private void setComics(List<ComicsThumbnail> comics) {
        comicsAdapter.setComics(comics);
        comicsAdapter.notifyDataSetChanged();
    }

    private void getComicsForYear(int year) {
        comicsPresenter.setYear(year);
        comicsPresenter.getComics();
    }

    private void handleNoConnection() {
        showMessage(getString(R.string.message_network_connection_required));
        hideLoading();
        showEmpty();
    }
}
