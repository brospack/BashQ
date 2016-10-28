package by.vshkl.bashq.view

import by.vshkl.bashq.model.ComicDetail

interface ComicView {

    fun onLoadSuccess(comicDetail: ComicDetail)

    fun onLoadingError(errorMessage: String?)
}
