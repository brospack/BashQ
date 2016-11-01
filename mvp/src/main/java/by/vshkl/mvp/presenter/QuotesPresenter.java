package by.vshkl.mvp.presenter;

import java.util.List;

import by.vshkl.mvp.domain.FetchQuotesUsecase;
import by.vshkl.mvp.domain.VoteQuoteUsecase;
import by.vshkl.mvp.model.Errors;
import by.vshkl.mvp.model.Quote;
import by.vshkl.mvp.presenter.common.Subsection;
import by.vshkl.mvp.view.QuotesView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class QuotesPresenter implements Presenter<QuotesView> {

    private QuotesView view;
    private FetchQuotesUsecase fetchQuotesUsecase;
    private VoteQuoteUsecase voteQuoteUsecase;
    private Disposable disposable;
    private List<Quote> quotes;
    private Subsection subsection;

    public QuotesPresenter(FetchQuotesUsecase fetchQuotesUsecase, VoteQuoteUsecase voteQuoteUsecase) {
        this.fetchQuotesUsecase = fetchQuotesUsecase;
        this.voteQuoteUsecase = voteQuoteUsecase;
    }

    //==================================================================================================================

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {
        getQuotes(false);
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
    public void attachView(QuotesView view) {
        this.view = view;
    }

    //==================================================================================================================

    public void setSubsection(Subsection subsection) {
        this.subsection = subsection;
    }

    //==================================================================================================================

    public void getQuotes(boolean next) {
        view.showLoading();

        fetchQuotesUsecase.setSubsection(subsection);
        fetchQuotesUsecase.setNext(next);
        disposable = fetchQuotesUsecase.execute()
                .subscribeOn(Schedulers.newThread())
                .onErrorReturn(new Function<Throwable, List<Quote>>() {
                    @Override
                    public List<Quote> apply(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        view.hideLoading();
                        view.showError(Errors.QUOTES_LOADING_FAILED);
                        return null;
                    }
                })
                .subscribe(new Consumer<List<Quote>>() {
                    @Override
                    public void accept(List<Quote> quotes) throws Exception {
                        if (quotes != null) {
                            view.hideLoading();
                            view.showQuotes(quotes);
                        } else {
                            view.hideLoading();
                            view.showEmpty();
                        }
                    }
                });
    }

    private void voteQuote(String quoteId) {

    }
}
