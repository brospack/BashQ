package by.vshkl.repository;

import java.util.List;

import by.vshkl.mvp.model.Comic;
import by.vshkl.mvp.model.Quote;
import by.vshkl.mvp.presenter.common.Subsection;
import io.reactivex.Observable;

public interface Repository {

    Observable<List<Quote>> getQuotes(Subsection subsection, boolean next, String urlPartBest);

    Observable<Quote> getQuote(String quoteId);

    Observable<Boolean> voteQuote(Quote.VoteState requiredVoteStatus);

    Observable<List<Comic>> getComics();

    Observable<Comic> getComic(String comicId);
}