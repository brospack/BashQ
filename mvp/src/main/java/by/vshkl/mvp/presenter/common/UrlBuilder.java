package by.vshkl.mvp.presenter.common;

import by.vshkl.mvp.model.Quote;

public class UrlBuilder {

    private static final String BASE_URL = "http://bash.im";
    private static final String INDEX = "/index/";
    private static final String RANDOM = "/random";
    private static final String BEST = "/best";
    private static final String BY_RATING = "/byrating/";
    private static final String ABYSS = "/abyss";
    private static final String ABYSS_TOP = "/abysstop";
    private static final String ABYSS_BEST = "/abyssbest/";
    private static final String QUOTE = "/quote/";
    private static final String COMICS_CALENDAR = "/comics-calendar/";
    private static final String VOTE_UP = "/rulez";
    private static final String VOTE_DOWN = "/sux";
    private static final String VOTE_OLD = "/bayan";

    public static String BuildMainUrl(Subsection subsection) {
        String resultingUrl = null;
        switch (subsection) {
            case INDEX:
                resultingUrl = buildIndex();
                break;
            case RANDOM:
                resultingUrl = buildRandom();
                break;
            case BEST:
                resultingUrl = buildBest();
                break;
            case BEST_YEAR:
                resultingUrl = buildBestYear();
                break;
            case BEST_MONTH:
                resultingUrl = buildBestMonth();
                break;
            case BY_RATING:
                resultingUrl = buildByRating();
                break;
            case ABYSS:
                resultingUrl = buildAbyss();
                break;
            case ABYSS_TOP:
                resultingUrl = buildAbyssTop();
                break;
            case ABYSS_BEST:
                resultingUrl = buildAbyssBest();
                break;
        }
        return resultingUrl;
    }

    public static String BuildVoteUrl(Quote.VoteState quoteVoteStatus, String quoteId) {
        StringBuilder sb = new StringBuilder(BASE_URL);
        sb.append(QUOTE).append(quoteId.substring(1));
        switch (quoteVoteStatus) {
            case VOTED_UP:
                sb.append(VOTE_UP);
                break;
            case VOTED_DOWN:
                sb.append(VOTE_DOWN);
                break;
            case VOTED_OLD:
                sb.append(VOTE_OLD);
                break;
        }
        return sb.toString();
    }

    public static String BuildQuoteUrl(String quoteId) {
        StringBuilder sb = new StringBuilder(BASE_URL);
        sb.append(QUOTE).append(quoteId.substring(1));
        return sb.toString();
    }

    public static String BuildComicsCalendarUrl(int year) {
        StringBuilder sb = new StringBuilder(BASE_URL);
        sb.append(COMICS_CALENDAR).append(year);
        return sb.toString();
    }

    public static String BuildComicUrl(String comicLinkPart) {
        StringBuilder sb = new StringBuilder(BASE_URL);
        sb.append(comicLinkPart);
        return sb.toString();
    }

    //==================================================================================================================

    private static String buildIndex() {
        StringBuilder sb = new StringBuilder(BASE_URL);
        sb.append(INDEX);
        return sb.toString();
    }

    private static String buildRandom() {
        StringBuilder sb = new StringBuilder(BASE_URL);
        sb.append(RANDOM);
        return sb.toString();
    }

    private static String buildBest() {
        StringBuilder sb = new StringBuilder(BASE_URL);
        sb.append(BEST);
        return sb.toString();
    }

    private static String buildBestYear() {
        return BASE_URL;
    }

    private static String buildBestMonth() {
        return BASE_URL;
    }

    private static String buildByRating() {
        StringBuilder sb = new StringBuilder(BASE_URL);
        sb.append(BY_RATING);
        return sb.toString();
    }

    private static String buildAbyss() {
        StringBuilder sb = new StringBuilder(BASE_URL);
        sb.append(ABYSS);
        return sb.toString();
    }

    private static String buildAbyssTop() {
        StringBuilder sb = new StringBuilder(BASE_URL);
        sb.append(ABYSS_TOP);
        return sb.toString();
    }

    private static String buildAbyssBest() {
        StringBuilder sb = new StringBuilder(BASE_URL);
        sb.append(ABYSS_BEST);
        return sb.toString();
    }
}
