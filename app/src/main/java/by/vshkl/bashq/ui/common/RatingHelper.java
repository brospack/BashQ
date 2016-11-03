package by.vshkl.bashq.ui.common;

import java.util.regex.Pattern;

import by.vshkl.mvp.model.Quote;
import by.vshkl.mvp.model.Rating;

public class RatingHelper {

    public static Rating updateRating(Quote.VoteState voteState, Rating rating) {
        String globalRating = rating.getRating();
        int globalVoteCount = rating.getVoteCount();

        if (Pattern.compile("\\d+").matcher(globalRating).find()) {
            int ratingNum = Integer.valueOf(globalRating);
            String ratingTxt = "";

            if (rating.getVoteCount() == 0) {
                if (voteState == Quote.VoteState.VOTED_UP) {
                    ratingNum++;
                } else if (voteState == Quote.VoteState.VOTED_DOWN) {
                    ratingNum--;
                }

                globalVoteCount++;
                globalRating = String.valueOf(ratingNum);
            } else {
                if (voteState == Quote.VoteState.VOTED_UP) {
                    if (rating.getVoteCount() - 1 >= RatingIndicators.LENGTH_RIGHT) {
                        ratingTxt = RatingIndicators.ratingsRight[RatingIndicators.LENGTH_RIGHT - 1];
                    } else {
                        ratingTxt = RatingIndicators.ratingsRight[rating.getVoteCount() - 1];
                    }
                } else if (voteState == Quote.VoteState.VOTED_DOWN || voteState == Quote.VoteState.VOTED_OLD) {
                    if (rating.getVoteCount() > RatingIndicators.LENGTH_LEFT) {
                        ratingTxt = RatingIndicators.ratingsLeft[RatingIndicators.LENGTH_LEFT - 1];
                    } else {
                        ratingTxt = RatingIndicators.ratingsLeft[rating.getVoteCount() - 1];
                    }
                }

                globalVoteCount++;
                globalRating = ratingTxt;
            }
        } else {
            String ratingTxt = globalRating;

            if (rating.getVoteCount() == 0) {
                if (voteState == Quote.VoteState.VOTED_UP) {
                    ratingTxt = ":-)";
                } else if (voteState == Quote.VoteState.VOTED_DOWN) {
                    ratingTxt = ":-(";
                }
                globalVoteCount++;
                globalRating = ratingTxt;
            } else if (rating.getVoteCount() > 0) {
                if (voteState == Quote.VoteState.VOTED_UP) {
                    if (rating.getVoteCount() - 1 >= RatingIndicators.LENGTH_RIGHT) {
                        ratingTxt = RatingIndicators.ratingsRight[RatingIndicators.LENGTH_RIGHT - 1];
                    } else {
                        ratingTxt = RatingIndicators.ratingsRight[rating.getVoteCount() - 1];
                    }
                } else if (voteState == Quote.VoteState.VOTED_DOWN || voteState == Quote.VoteState.VOTED_OLD) {
                    if (rating.getVoteCount() - 1 >= RatingIndicators.LENGTH_LEFT) {
                        ratingTxt = RatingIndicators.ratingsLeft[RatingIndicators.LENGTH_LEFT - 1];
                    } else {
                        ratingTxt = RatingIndicators.ratingsLeft[rating.getVoteCount() - 1];
                    }
                }

                globalVoteCount++;
                globalRating = ratingTxt;
            }
        }

        return new Rating(globalRating, globalVoteCount);
    }
}
