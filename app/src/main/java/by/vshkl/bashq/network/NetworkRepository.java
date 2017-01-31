package by.vshkl.bashq.network;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import by.vshkl.bashq.database.AppDatabase;
import by.vshkl.bashq.database.mapper.QuoteEntityMapper;
import by.vshkl.bashq.database.mapper.QuoteMapper;
import by.vshkl.bashq.database.model.QuoteEntity;
import by.vshkl.mvp.model.Comic;
import by.vshkl.mvp.model.ComicsThumbnail;
import by.vshkl.mvp.model.Quote;
import by.vshkl.mvp.presenter.common.Subsection;
import by.vshkl.mvp.presenter.common.UrlBuilder;
import by.vshkl.repository.Repository;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.R.attr.action;

public class NetworkRepository implements Repository {

    private OkHttpClient client;
    private String nextUrlPart = "";

    public NetworkRepository(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public Observable<List<Quote>> getQuotes(final Subsection subsection, final boolean next, final String urlPartBest) {
        switch (subsection) {
            case FAVOURITE_QUOTES:
                return getQuotesFromDatabase();
            default:
                return getQuotesFromNetwork(subsection, next, urlPartBest);
        }
    }

    @Override
    public Observable<Boolean> saveQuote(final Quote quote) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> emitter) throws Exception {
                final QuoteEntity quoteEntity = QuoteMapper.transform(quote);

                final ProcessModelTransaction<QuoteEntity> processModelTransaction = new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<QuoteEntity>() {
                            @Override
                            public void processModel(QuoteEntity quoteEntity, DatabaseWrapper wrapper) {
                                quoteEntity.insert();
                            }
                        })
                        .add(quoteEntity)
                        .build();

                final DatabaseDefinition database = FlowManager.getDatabase(AppDatabase.class);

                database.beginTransactionAsync(processModelTransaction)
                        .success(new Transaction.Success() {
                            @Override
                            public void onSuccess(Transaction transaction) {
                                emitter.onNext(true);
                            }
                        })
                        .error(new Transaction.Error() {
                            @Override
                            public void onError(Transaction transaction, Throwable error) {
                                emitter.onNext(false);
                            }
                        })
                        .build()
                        .execute();
            }
        });
    }

    @Override
    public Observable<Boolean> deleteQuote(final Quote quote) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> emitter) throws Exception {
                final QuoteEntity quoteEntity = QuoteMapper.transform(quote);

                final ProcessModelTransaction<QuoteEntity> processModelTransaction = new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<QuoteEntity>() {
                            @Override
                            public void processModel(QuoteEntity quoteEntity, DatabaseWrapper wrapper) {
                                quoteEntity.delete();
                            }
                        })
                        .add(quoteEntity)
                        .build();

                final DatabaseDefinition database = FlowManager.getDatabase(AppDatabase.class);

                database.beginTransactionAsync(processModelTransaction)
                        .success(new Transaction.Success() {
                            @Override
                            public void onSuccess(Transaction transaction) {
                                emitter.onNext(true);
                            }
                        })
                        .error(new Transaction.Error() {
                            @Override
                            public void onError(Transaction transaction, Throwable error) {
                                emitter.onNext(false);
                            }
                        })
                        .build()
                        .execute();
            }
        });
    }

    @Override
    public Observable<Boolean> voteQuote(final String quoteId, final Quote.VoteState requiredVoteStatus) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                String voteUrl = UrlBuilder.BuildVoteUrl(requiredVoteStatus, quoteId);

                final Request request = new Request.Builder()
                        .url(voteUrl)
                        .post(RequestBody.create(MediaType.parse(
                                "application/x-www-form-urlencoded; charset=UTF-8"),
                                "quote=" + quoteId + "&act=" + action))
                        .build();

                try {
                    try (Response response = client.newCall(request).execute()) {
                        emitter.onNext(response.isSuccessful());
                    }
                } catch (IOException e) {
                    emitter.onNext(false);
                }
            }
        });
    }

    @Override
    public Observable<String> getComicImageUrl(final String comicUrlPart) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String comicUrl = UrlBuilder.BuildComicUrl(comicUrlPart);

                final Request request = new Request.Builder()
                        .url(comicUrl)
                        .build();

                try {
                    try (Response response = client.newCall(request).execute()) {
                        Document document = Jsoup.parse(response.body().string());

                        Element comicElement = document.select("img#cm_strip").first();
                        if (comicElement != null) {
                            emitter.onNext(comicElement.attr("src"));
                        } else {
                            emitter.onNext(null);
                        }
                    }
                } catch (IOException e) {
                    emitter.onNext("");
                }
            }
        });
    }

    @Override
    public Observable<List<ComicsThumbnail>> getComics(final int year) {
        return Observable.create(new ObservableOnSubscribe<List<ComicsThumbnail>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ComicsThumbnail>> emitter) throws Exception {
                String comicsCalendarUrl = UrlBuilder.BuildComicsCalendarUrl(year);

                final Request request = new Request.Builder()
                        .url(comicsCalendarUrl)
                        .build();

                try {
                    try (Response response = client.newCall(request).execute()) {
                        Document document = Jsoup.parse(response.body().string());
                        emitter.onNext(parseComics(document));
                    }
                } catch (IOException e) {
                    emitter.onNext(Collections.<ComicsThumbnail>emptyList());
                }
            }
        });
    }

    @Override
    public Observable<Comic> getComic(String comicId) {
        return null;
    }

    //==================================================================================================================

    private Quote parseQuote(Element quoteElement) {
        Quote quote = new Quote();

        // Retrieving quote's id and link for ordinal quotes
        Element quoteId = quoteElement.select("a.id").first();
        if (quoteId != null) {
            quote.setId(quoteId.text());
            quote.setLink(quoteElement.attr("href"));
        }

        // Retrieving quote's id and link for quotes from Abyss
        Element quoteAbyssId = quoteElement.select(".id").first();
        if (quoteAbyssId != null) {
            quote.setId(quoteAbyssId.text());
        }

        // Retrieving quote's id and link for quotes from Abyss Top
        Element quoteAbyssTopId = quoteElement.select(".abysstop").first();
        if (quoteAbyssTopId != null) {
            quote.setId(quoteAbyssTopId.text());
        }

        // Retrieving quote's date for ordinal quotes
        Element quoteDate = quoteElement.select(".date").first();
        if (quoteDate != null) {
            quote.setDate(quoteDate.text());
        }

        // Retrieving quote's date for quotes from Abyss Top
        Element quoteDateAbyss = quoteElement.select(".abysstop-date").first();
        if (quoteDateAbyss != null) {
            quote.setDate(quoteDateAbyss.text());
        }

        // Retrieving quote's content text
        Element quoteContent = quoteElement.select(".text").first();
        if (quoteContent != null) {
            quote.setContent(quoteContent.html());
        }

        // Retrieving quote's rating text
        Element quoteRating = quoteElement.select(".rating").first();
        if (quoteRating != null) {
            quote.setRating(quoteRating.text());
        }

        // Retrieving quote's comic link if there is one
        Element quoteComic = quoteElement.select(".comics").first();
        if (quoteComic != null) {
            quote.setComicLink(quoteComic.attr("href"));
        }

        return quote;
    }

    private List<ComicsThumbnail> parseComics(Document document) {
        List<ComicsThumbnail> comicsList = new ArrayList<>();

        Elements comicsCalendar = document.select("div#calendar a[href]");
        if (comicsCalendar != null) {
            for (Element comicsElement : comicsCalendar) {
                if (comicsElement != null) {
                    ComicsThumbnail comics = new ComicsThumbnail();
                    comics.setThumbLink(comicsElement.child(0).attr("src"));
                    comics.setImageLink(comics.getThumbLink().replace("ts/", ""));
                    comics.setComicsLink(comicsElement.attr("href"));
                    comicsList.add(comics);
                }
            }
        }

        return comicsList;
    }

    private Observable<List<Quote>> getQuotesFromDatabase() {
        return Observable.create(new ObservableOnSubscribe<List<Quote>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Quote>> emitter) throws Exception {
                SQLite.select()
                        .from(QuoteEntity.class)
                        .async()
                        .queryResultCallback(new QueryTransaction.QueryResultCallback<QuoteEntity>() {
                            @Override
                            public void onQueryResult(QueryTransaction<QuoteEntity> transaction,
                                                      @NonNull CursorResult<QuoteEntity> tResult) {
                                emitter.onNext(QuoteEntityMapper.transform(tResult.toList()));
                            }
                        })
                        .execute();
            }
        });
    }

    private Observable<List<Quote>> getQuotesFromNetwork(final Subsection subsection, final boolean next,
                                                         final String urlPartBest) {
        return Observable.create(new ObservableOnSubscribe<List<Quote>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Quote>> emitter) throws Exception {
                String fullUrl = UrlBuilder.BuildMainUrl(subsection);
                if (next) {
                    fullUrl += nextUrlPart;
                }

                if (urlPartBest != null) {
                    if (subsection == Subsection.BEST_MONTH || subsection == Subsection.BEST_YEAR
                            || subsection == Subsection.ABYSS_BEST) {
                        fullUrl = UrlBuilder.BuildMainUrl(subsection);
                        fullUrl += urlPartBest;
                    }
                }

                final Request request = new Request.Builder().url(fullUrl).build();

                List<Quote> quotes = null;

                try {
                    Response response = client.newCall(request).execute();
                    try {
                        ResponseBody responseBody = response.body();
                        if (responseBody == null) {
                            emitter.onNext(Collections.<Quote>emptyList());
                            return;
                        }
                        Document document = Jsoup.parse(responseBody.string());

                        Elements quoteElements = document.select(".quote");
                        if (quoteElements != null) {
                            quotes = new ArrayList<>();
                            for (Element quoteElement : quoteElements) {
                                Quote quote = parseQuote(quoteElement);
                                if (quote.getId() != null) {
                                    quotes.add(parseQuote(quoteElement));
                                }
                            }
                        }

                        Element nextPageElement = document.select(".arr").last();
                        if (nextPageElement != null) {
                            String nextLink = nextPageElement.parent().select("a").attr("href");
                            nextUrlPart = nextLink.substring(nextLink.lastIndexOf("/") + 1);
                        }

                        Element nextRandomPageElement = document.select(".quote.more").first();
                        if (nextRandomPageElement != null) {
                            String nextLink = nextRandomPageElement.select("a").attr("href");
                            nextUrlPart = nextLink.substring(nextLink.indexOf("?"));
                        }

                        emitter.onNext(quotes != null ? new ArrayList<>(quotes) : Collections.<Quote>emptyList());
                    } finally {
                        response.close();
                    }
                } catch (IOException e) {
                    emitter.onNext(Collections.<Quote>emptyList());
                }
            }
        });
    }
}
