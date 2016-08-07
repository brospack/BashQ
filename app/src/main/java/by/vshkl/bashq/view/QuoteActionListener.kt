package by.vshkl.bashq.view

interface QuoteActionListener {

    fun share(content: String): Boolean

    fun vote(actionLink: String, action: String)
}