package by.vshkl.bashq.constants

import by.vshkl.bashq.model.Rating

class Ratings {
    companion object {

        val ratingLeft = arrayOf(
                "(\uff65_\uff65 )", "(\u00ac_\u00ac )", "(\u0ca0_\u0ca0 )",
                "(\u0ca0\uff3f\u0ca0 )", "(\u0ca0\u76ca\u0ca0 )", "(>\uff3f< )", "(=\uff3f= )")
        val ratingRight = arrayOf(
                "( \uff65_\uff65)", "( \u00ac_\u00ac)", "( \u0ca0_\u0ca0)",
                "( \u0ca0\uff3f\u0ca0)", "( \u0ca0\u76ca\u0ca0)", "( >\uff3f<)", "( =\uff3f=)")

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
                        if (rating.voteCount - 1 >= ratingRight.size) {
                            ratingTxt = ratingRight.last()
                        } else {
                            ratingTxt = ratingRight[rating.voteCount - 1]
                        }
                    } else if (voteType == -1 || voteType == 0) {
                        if (rating.voteCount - 1 >= ratingRight.size) {
                            ratingTxt = ratingLeft.last()
                        } else {
                            ratingTxt = ratingLeft[rating.voteCount - 1]
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
                        if (rating.voteCount - 1 >= ratingRight.size) {
                            ratingTxt = ratingRight.last()
                        } else {
                            ratingTxt = ratingRight[rating.voteCount - 1]
                        }
                    } else if (voteType == -1 || voteType == 0) {
                        if (rating.voteCount - 1 >= ratingRight.size) {
                            ratingTxt = ratingLeft.last()
                        } else {
                            ratingTxt = ratingLeft[rating.voteCount - 1]
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
