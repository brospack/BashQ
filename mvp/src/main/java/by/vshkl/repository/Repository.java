package by.vshkl.repository;

import java.util.List;

import by.vshkl.mvp.model.Comic;
import by.vshkl.mvp.model.ComicsThumbnail;
import by.vshkl.mvp.model.Quote;
import by.vshkl.mvp.presenter.common.Subsection;
import io.reactivex.Observable;

public interface Repository {

    Observable<List<Quote>> getQuotes(Subsection subsection, boolean next, String urlPartBest);

    Observable<Boolean> saveQuote(Quote quote);

    Observable<Boolean> voteQuote(String quoteId, Quote.VoteState requiredVoteStatus);

    Observable<String> getComicImageUrl(String comicUrlPart);

    Observable<List<ComicsThumbnail>> getComics(int year);

    Observable<Comic> getComic(String comicId);
}
