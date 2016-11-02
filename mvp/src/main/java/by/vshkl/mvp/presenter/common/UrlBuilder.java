package by.vshkl.mvp.presenter.common;

import by.vshkl.mvp.model.Quote;

public class UrlBuilder {

    private static final String BASE_URL = "http://bash.im";

    private static final String INDEX = "/index/";
    private static final String RANDOM = "/random";
    private static final String BEST = "/best";
    private static final String BEST_YEAR = "/bestyear/";
    private static final String BEST_MONTH = "/bestmonth/";
    private static final String BY_RATING = "/byrating/";
    private static final String ABYSS = "/abyss";
    private static final String ABYSS_TOP = "/abysstop";
    private static final String ABYSS_BEST = "/abyssbest/";

    public static final String QUOTE = "/quote/";

    private static final String SLASH = "/";
    private static final String DEFAULT_PARAM_BY_RATING = "1";

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

    public static String BuildVoteUrl(Quote.VoteState quoteVoteStatus, String quoteUrl) {
        StringBuilder sb = new StringBuilder(quoteUrl);

        switch (quoteVoteStatus) {
            case VOTED_UP:
                sb.append("/rulez");
                break;
            case VOTED_DOWN:
                sb.append("/sux");
                break;
            case VOTED_OLD:
                sb.append("/bayan");
                break;
        }

        return sb.toString();
    }

    public static String BuildQuoteUrl(String quoteId) {
        StringBuilder sb = new StringBuilder(BASE_URL);

        sb.append(QUOTE).append(quoteId);

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
        StringBuilder sb = new StringBuilder(BASE_URL);

//        sb.append(BEST_YEAR).append(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

        return sb.toString();
    }

    private static String buildBestMonth() {
        StringBuilder sb = new StringBuilder(BASE_URL);

//        sb.append(BEST_MONTH)
//                .append(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))
//                .append(SLASH)
//                .append(String.valueOf(Calendar.getInstance().get(Calendar.MONTH)));

        return sb.toString();
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

//        sb.append(ABYSS_BEST)
//                .append(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))
//                .append(String.valueOf(Calendar.getInstance().get(Calendar.MONTH)))
//                .append(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        sb.append(ABYSS_BEST);

        return sb.toString();
    }
}