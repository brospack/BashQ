package by.vshkl.bashq.view

import by.vshkl.bashq.model.Quote

interface QuotesView {

    fun onLoadSuccess(quotes: MutableList<Quote>, next: Boolean)

    fun onLoadingError(errorMessage: String?)
}