package by.vshkl.bashq.model

data class Quote(val id: String,
                 val date: String,
                 val rating: String,
                 val content: String,
                 val voteUp: String,
                 val voteDown: String,
                 val voteOld: String,
                 var voteCount: Int = 0
)

