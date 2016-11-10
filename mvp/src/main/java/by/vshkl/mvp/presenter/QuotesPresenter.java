package by.vshkl.mvp.presenter;

import java.util.List;

import by.vshkl.mvp.domain.FetchQuoteComicImageUsecase;
import by.vshkl.mvp.domain.FetchQuotesUsecase;
import by.vshkl.mvp.domain.SaveQuoteUsecase;
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
    private FetchQuoteComicImageUsecase fetchQuoteComicImageUsecase;
    private SaveQuoteUsecase saveQuoteUsecase;
    private Disposable disposable;
    private Subsection subsection;
    private String urlPartBest;
    private String voteQuoteId;
    private String comicUrlPart;
    private Quote.VoteState requiredVoteState;

    public QuotesPresenter(FetchQuotesUsecase fetchQuotesUsecase,
                           VoteQuoteUsecase voteQuoteUsecase,
                           FetchQuoteComicImageUsecase fetchQuoteComicImageUsecase,
                           SaveQuoteUsecase saveQuoteUsecase) {
        this.fetchQuotesUsecase = fetchQuotesUsecase;
        this.voteQuoteUsecase = voteQuoteUsecase;
        this.fetchQuoteComicImageUsecase = fetchQuoteComicImageUsecase;
        this.saveQuoteUsecase = saveQuoteUsecase;
    }

    //==================================================================================================================

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {
        view.showLoading();
        getQuotes(false);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void attachView(QuotesView view) {
        this.view = view;
    }

    //==================================================================================================================

    public void setSubsection(Subsection subsection) {
        this.subsection = subsection;
    }

    public void setUrlPartBest(String urlPartBest) {
        this.urlPartBest = urlPartBest;
    }

    public void setVoteQuoteId(String voteQuoteId) {
        this.voteQuoteId = voteQuoteId;
    }

    public void setRequiredVoteState(Quote.VoteState requiredVoteState) {
        this.requiredVoteState = requiredVoteState;
    }

    public void setComicUrlPart(String comicUrlPart) {
        this.comicUrlPart = comicUrlPart;
    }

    //==================================================================================================================

    public void getQuotes(boolean next) {
        fetchQuotesUsecase.setSubsection(subsection);
        fetchQuotesUsecase.setNext(next);
        fetchQuotesUsecase.setUrlPartBest(urlPartBest);
        disposable = fetchQuotesUsecase.execute()
                .subscribeOn(Schedulers.newThread())
                .onErrorReturn(new Function<Throwable, List<Quote>>() {
                    @Override
                    public List<Quote> apply(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        view.hideLoading();
                        view.showError(Errors.QUOTES_LOAD_FAILED);
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

    public void voteQuote() {
        voteQuoteUsecase.setQuoteId(voteQuoteId);
        voteQuoteUsecase.setRequiredQuoteVoteState(requiredVoteState);
        disposable = voteQuoteUsecase.execute()
                .subscribeOn(Schedulers.newThread())
                .onErrorReturn(new Function<Throwable, Boolean>() {
                    @Override
                    public Boolean apply(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        view.showError(Errors.QUOTES_VOTE_FAILED);
                        return null;
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            view.showMessage("Vote passed!");
                        } else {
                            view.showMessage("Vote failed!");
                        }
                    }
                });
    }

    public void getQuoteComicImage() {
        fetchQuoteComicImageUsecase.setComicUrlPart(comicUrlPart);
        disposable = fetchQuoteComicImageUsecase.execute()
                .subscribeOn(Schedulers.newThread())
                .onErrorReturn(new Function<Throwable, String>() {
                    @Override
                    public String apply(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        view.showError(Errors.QUOTE_COMIC_IMAGE_LOAD_FAILED);
                        return null;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (s != null) {
                            view.showQuoteComicImageDialog(s);
                        }
                    }
                });
    }

    public void saveQuote(Quote quote) {
        saveQuoteUsecase.setQuote(quote);
        disposable = saveQuoteUsecase.execute()
                .subscribeOn(Schedulers.newThread())
                .onErrorReturn(new Function<Throwable, Boolean>() {
                    @Override
                    public Boolean apply(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        view.showError(Errors.QUOTES_FAVOURITE_ADD_FAILED);
                        return null;
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            view.showMessage("Quote added to favourites");
                        } else {
                            view.showError(Errors.QUOTES_FAVOURITE_ADD_FAILED);
                        }
                    }
                });
    }
}
