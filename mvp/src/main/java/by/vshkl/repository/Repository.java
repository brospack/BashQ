package by.vshkl.repository;

import java.util.List;

import by.vshkl.mvp.model.Comic;
import by.vshkl.mvp.model.Quote;
import by.vshkl.mvp.model.ResponseWrapper;
import io.reactivex.Observable;

public interface Repository {

    Observable<String> getNewestIndex();

    Observable<ResponseWrapper<List<Quote>>> getQuotes(String fullUrl);

    Observable<Quote> getQuote(String quoteId);

    Observable<Boolean> voteQuote(Quote.VoteState requiredVoteStatus);

    Observable<ResponseWrapper<List<Comic>>> getComics();

    Observable<Comic> getComic(String comicId);
}
