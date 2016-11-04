package by.vshkl.mvp.domain;

import by.vshkl.repository.Repository;
import io.reactivex.Observable;

public class FetchQuoteComicImageUsecase implements Usecase<String> {

    private Repository repository;
    private String comicUrlPart;

    public FetchQuoteComicImageUsecase(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<String> execute() {
        return repository.getComicImageUrl(comicUrlPart);
    }

    public void setComicUrlPart(String comicUrlPart) {
        this.comicUrlPart = comicUrlPart;
    }
}
