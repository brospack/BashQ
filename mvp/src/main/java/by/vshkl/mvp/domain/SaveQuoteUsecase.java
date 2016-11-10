package by.vshkl.mvp.domain;

import by.vshkl.mvp.model.Quote;
import by.vshkl.repository.Repository;
import io.reactivex.Observable;

public class SaveQuoteUsecase implements Usecase<Boolean> {

    private Repository repository;
    private Quote quote;

    public SaveQuoteUsecase(Repository repository) {
        this.repository = repository;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    @Override
    public Observable<Boolean> execute() {
        return repository.saveQuote(quote);
    }
}
