package by.vshkl.mvp.presenter.common;

import java.util.Calendar;

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
    private static final String SLASH = "/";
    private static final String DEFAULT_PARAM_BY_RATING = "1";

    public static String BuildUrl(Subsection subsection, String currentIndex) {
        String resultingUrl = null;

        switch (subsection) {
            case INDEX:
                resultingUrl = buildIndex(currentIndex);
            case RANDOM:
                resultingUrl = buildRandom();
            case BEST:
                resultingUrl = buildBest();
            case BEST_YEAR:
                resultingUrl = buildBestYear();
            case BEST_MONTH:
                resultingUrl = buildBestMonth();
            case BY_RATING:
                resultingUrl = buildByRating();
            case ABYSS:
                resultingUrl = buildAbyss();
            case ABYSS_TOP:
                resultingUrl = buildAbyssTop();
            case ABYSS_BEST:
                resultingUrl = buildAbyssBest();
        }

        return resultingUrl;
    }

    //==================================================================================================================

    private static String buildIndex(String currentIndex) {
        StringBuilder sb = new StringBuilder(BASE_URL);

        sb.append(INDEX).append(currentIndex);

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

        sb.append(BEST_YEAR).append(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

        return sb.toString();
    }

    private static String buildBestMonth() {
        StringBuilder sb = new StringBuilder(BASE_URL);

        sb.append(BEST_MONTH)
                .append(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))
                .append(SLASH)
                .append(String.valueOf(Calendar.getInstance().get(Calendar.MONTH)));

        return sb.toString();
    }

    private static String buildByRating() {
        StringBuilder sb = new StringBuilder(BASE_URL);

        sb.append(BY_RATING).append(DEFAULT_PARAM_BY_RATING);

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

        sb.append(ABYSS_BEST)
                .append(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))
                .append(String.valueOf(Calendar.getInstance().get(Calendar.MONTH)))
                .append(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));

        return sb.toString();
    }
}
