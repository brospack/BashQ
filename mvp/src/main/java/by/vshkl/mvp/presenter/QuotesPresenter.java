package by.vshkl.mvp.presenter;

import java.util.List;

import by.vshkl.mvp.model.Quote;
import by.vshkl.mvp.view.QuotesView;
import io.reactivex.disposables.Disposable;

public class QuotesPresenter implements Presenter<QuotesView> {

    private QuotesView view;
    private Disposable disposable;
    private List<Quote> quotes;

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void attachView(QuotesView view) {

    }
}
