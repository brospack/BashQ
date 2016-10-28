package by.vshkl.bashq.model

data class ComicDetail(
        val imageLink: String,
        val creator: String,
        val quoteLink: String,
        var quoteContent: String = ""
)
