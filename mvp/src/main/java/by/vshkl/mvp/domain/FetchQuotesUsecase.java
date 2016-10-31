package by.vshkl.mvp.domain;

import java.util.List;

import by.vshkl.mvp.domain.common.ResponseMappingFunc;
import by.vshkl.mvp.model.Quote;
import by.vshkl.repository.Repository;
import io.reactivex.Observable;

public class FetchQuotesUsecase implements Usecase<List<Quote>> {

    private Repository repository;

    public FetchQuotesUsecase(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<Quote>> execute() {
        return repository.getQuotes().map(new ResponseMappingFunc<List<Quote>>());
    }
}
