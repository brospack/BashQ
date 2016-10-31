package by.vshkl.bashq.model

data class Quote(val id: String,
                 val link: String,
                 val date: String,
                 var rating: String,
                 val content: String,
                 val voteUp: String,
                 val voteDown: String,
                 val voteOld: String,
                 var voteStatus: QuoteVoteState = QuoteVoteState.NONE,
                 var voteCount: Int = 0
)

