package by.vshkl.mvp.domain;

import java.util.List;

import by.vshkl.mvp.domain.common.ResponseMappingFunc;
import by.vshkl.mvp.model.Comic;
import by.vshkl.repository.Repository;
import io.reactivex.Observable;

public class FetchComicsUsecase implements Usecase<List<Comic>> {

    private Repository repository;

    public FetchComicsUsecase(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<Comic>> execute() {
        return repository.getComics().map(new ResponseMappingFunc<List<Comic>>());
    }
}
