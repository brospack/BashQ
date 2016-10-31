package by.vshkl.mvp.domain;

import by.vshkl.mvp.model.Comic;
import by.vshkl.repository.Repository;
import io.reactivex.Observable;

public class FetchComicUsecase implements Usecase<Comic> {

    private Repository repository;
    private String comicId;

    public FetchComicUsecase(Repository repository) {
        this.repository = repository;
    }

    public void setComicId(String comicId) {
        this.comicId = comicId;
    }

    @Override
    public Observable<Comic> execute() {
        return repository.getComic(comicId);
    }
}
