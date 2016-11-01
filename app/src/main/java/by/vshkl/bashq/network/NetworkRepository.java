package by.vshkl.bashq.network;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import by.vshkl.mvp.model.Comic;
import by.vshkl.mvp.model.Quote;
import by.vshkl.mvp.presenter.common.Subsection;
import by.vshkl.mvp.presenter.common.UrlBuilder;
import by.vshkl.repository.Repository;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkRepository implements Repository {

    private OkHttpClient client;
    private String nextUrlPart = "";

    public NetworkRepository(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public Observable<String> getNewestIndex() {
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                Request request = new Request.Builder().url("http://bash.im").build();

                String index = null;
                try {
                    Response response = client.newCall(request).execute();
                    Document document = Jsoup.parse(response.body().string());

                    Element pageElement = document.select(".page").first();
                    if (pageElement != null) {
                        index = pageElement.attr("max");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return Observable.just(index);
            }
        }).subscribeOn(Schedulers.newThread());
    }

    @Override
    public Observable<List<Quote>> getQuotes(final Subsection subsection, final boolean next) {
        return Observable.defer(new Callable<ObservableSource<? extends List<Quote>>>() {
            @Override
            public ObservableSource<? extends List<Quote>> call() throws Exception {
                String fullUrl = UrlBuilder.BuildMainUrl(subsection);
                if (next) {
                    fullUrl += nextUrlPart;
                }

                Request request = new Request.Builder().url(fullUrl).build();

                List<Quote> quotes = new ArrayList<>();
                try {
                    Response response = client.newCall(request).execute();
                    Document document = Jsoup.parse(response.body().string());

                    Elements quoteElements = document.select(".quote");
                    if (quoteElements != null) {
                        for (Element quoteElement : quoteElements) {
                            Quote quote = parseQuote(quoteElement);
                            if (quote.getId() != null) {
                                quotes.add(parseQuote(quoteElement));
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return Observable.just(quotes);
            }
        }).subscribeOn(Schedulers.newThread());
    }

    @Override
    public Observable<Quote> getQuote(String quoteId) {
        final Quote[] quote = new Quote[1];

        Request request = new Request.Builder()
                .url(UrlBuilder.BuildQuoteUrl(quoteId))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Element quoteElement = Jsoup.parse(response.body().toString()).select(".quote").first();
                if (quoteElement != null) {
                    quote[0] = parseQuote(quoteElement);
                }
            }
        });

        return Observable.just(quote[0]);
    }

    @Override
    public Observable<Boolean> voteQuote(Quote.VoteState requiredVoteStatus) {
        return null;
    }

    @Override
    public Observable<List<Comic>> getComics() {
        return null;
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

        // Retrieving quote's id and link for quotes from Abyss Top
        Element quoteAbyssId = quoteElement.select(".abysstop").first();
        if (quoteAbyssId != null) {
            quote.setId(quoteAbyssId.text());
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

        return quote;
    }
}
