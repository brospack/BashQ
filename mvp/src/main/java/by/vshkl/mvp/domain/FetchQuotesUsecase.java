package by.vshkl.mvp.domain;

import java.util.List;

import by.vshkl.mvp.model.Quote;
import by.vshkl.repository.Repository;
import io.reactivex.Observable;

public class FetchQuotesUsecase implements Usecase<List<Quote>> {

    private Repository repository;
    private String fullUrl;

    public FetchQuotesUsecase(Repository repository) {
        this.repository = repository;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    @Override
    public Observable<List<Quote>> execute() {
        return repository.getQuotes(fullUrl);
    }
}
