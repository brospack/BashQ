package by.vshkl.mvp.domain;

import by.vshkl.mvp.model.Quote;
import by.vshkl.repository.Repository;
import io.reactivex.Observable;

public class FetchQuoteUsecase implements Usecase<Quote> {

    private Repository repository;
    private String quoteId;

    public FetchQuoteUsecase(Repository repository) {
        this.repository = repository;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    @Override
    public Observable<Quote> execute() {
        return repository.getQuote(quoteId);
    }
}
