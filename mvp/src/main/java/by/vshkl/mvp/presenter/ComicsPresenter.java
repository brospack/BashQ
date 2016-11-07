package by.vshkl.mvp.presenter;

import java.util.List;

import by.vshkl.mvp.domain.FetchComicsUsecase;
import by.vshkl.mvp.model.ComicsThumbnail;
import by.vshkl.mvp.model.Errors;
import by.vshkl.mvp.view.ComicsView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ComicsPresenter implements Presenter<ComicsView> {

    private ComicsView view;
    private FetchComicsUsecase fetchComicsUsecase;
    private Disposable disposable;
    private int year;

    public ComicsPresenter(FetchComicsUsecase fetchComicsUsecase) {
        this.fetchComicsUsecase = fetchComicsUsecase;
    }

    //==================================================================================================================

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {
        view.showLoading();
        getComics();
    }

    @Override
    public void onStop() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void attachView(ComicsView view) {
        this.view = view;
    }

    //==================================================================================================================

    public void setYear(int year) {
        this.year = year;
    }

    //==================================================================================================================

    private void getComics() {
        fetchComicsUsecase.setYear(year);
        disposable = fetchComicsUsecase.execute()
                .subscribeOn(Schedulers.newThread())
                .onErrorReturn(new Function<Throwable, List<ComicsThumbnail>>() {
                    @Override
                    public List<ComicsThumbnail> apply(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        view.hideLoading();
                        view.showError(Errors.COMICS_LOADING_FAILED);
                        return null;
                    }
                })
                .subscribe(new Consumer<List<ComicsThumbnail>>() {
                    @Override
                    public void accept(List<ComicsThumbnail> comicsThumbnails) throws Exception {
                        view.hideLoading();
                        if (comicsThumbnails != null) {
                            if (comicsThumbnails.isEmpty()) {
                                view.showEmpty();
                            } else {
                                view.showComics(comicsThumbnails);
                            }
                        }
                    }
                });
    }
}
