package by.vshkl.mvp.domain;

import java.util.List;

import by.vshkl.mvp.model.Quote;
import by.vshkl.mvp.presenter.common.Subsection;
import by.vshkl.repository.Repository;
import io.reactivex.Observable;

public class FetchQuotesUsecase implements Usecase<List<Quote>> {

    private Repository repository;
    private Subsection subsection;
    private String urlPartBest;
    private boolean next;

    public FetchQuotesUsecase(Repository repository) {
        this.repository = repository;
    }

    public void setSubsection(Subsection subsection) {
        this.subsection = subsection;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public void setUrlPartBest(String urlPartBest) {
        this.urlPartBest = urlPartBest;
    }

    @Override
    public Observable<List<Quote>> execute() {
        return repository.getQuotes(subsection, next, urlPartBest);
    }
}
