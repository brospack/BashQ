package by.vshkl.bashq.utils

import by.vshkl.bashq.constants.RatingIndicators
import by.vshkl.bashq.model.Rating

class Ratings {
    companion object {

        /**
         * voteType: -1 -> vote down, 0 -> vote bayan, 1 -> vote up
         */
        fun updateRating(rating: Rating, voteType: Int): Rating {
            var globalRating = rating.rating
            var globalVoteCount = rating.voteCount

            if (Regex("^[0-9]*").matches(globalRating)) {
                var ratingNum = globalRating.toInt()
                var ratingTxt = ""

                if (rating.voteCount == 0) {
                    if (voteType == 1) {
                        ratingNum += 1
                    } else if (voteType == -1) {
                        ratingNum -= 1

                    }

                    globalVoteCount += 1
                    globalRating = ratingNum.toString()
                } else {
                    if (voteType == 1) {
                        if (rating.voteCount - 1 >= RatingIndicators.ratingRight.size) {
                            ratingTxt = RatingIndicators.ratingRight.last()
                        } else {
                            ratingTxt = RatingIndicators.ratingRight[rating.voteCount - 1]
                        }
                    } else if (voteType == -1 || voteType == 0) {
                        if (rating.voteCount - 1 >= RatingIndicators.ratingRight.size) {
                            ratingTxt = RatingIndicators.ratingLeft.last()
                        } else {
                            ratingTxt = RatingIndicators.ratingLeft[rating.voteCount - 1]
                        }
                    }

                    globalVoteCount += 1
                    globalRating = ratingTxt
                }
            } else {
                var ratingTxt = globalRating

                if (rating.voteCount == 0) {
                    if (voteType == 1) {
                        ratingTxt = ":-)"
                    } else if (voteType == -1) {
                        ratingTxt = ":-("
                    }

                    globalVoteCount += 1
                    globalRating = ratingTxt
                } else if (rating.voteCount > 0) {
                    if (voteType == 1) {
                        if (rating.voteCount - 1 >= RatingIndicators.ratingRight.size) {
                            ratingTxt = RatingIndicators.ratingRight.last()
                        } else {
                            ratingTxt = RatingIndicators.ratingRight[rating.voteCount - 1]
                        }
                    } else if (voteType == -1 || voteType == 0) {
                        if (rating.voteCount - 1 >= RatingIndicators.ratingRight.size) {
                            ratingTxt = RatingIndicators.ratingLeft.last()
                        } else {
                            ratingTxt = RatingIndicators.ratingLeft[rating.voteCount - 1]
                        }
                    }

                    globalVoteCount += 1
                    globalRating = ratingTxt
                }
            }

            return Rating(globalRating, globalVoteCount)
        }
    }
}
