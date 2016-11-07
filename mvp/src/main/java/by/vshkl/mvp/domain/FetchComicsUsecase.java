package by.vshkl.mvp.domain;

import java.util.List;

import by.vshkl.mvp.model.ComicsThumbnail;
import by.vshkl.repository.Repository;
import io.reactivex.Observable;

public class FetchComicsUsecase implements Usecase<List<ComicsThumbnail>> {

    private Repository repository;
    private int year;

    public FetchComicsUsecase(Repository repository) {
        this.repository = repository;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public Observable<List<ComicsThumbnail>> execute() {
        return repository.getComics(year);
    }
}
