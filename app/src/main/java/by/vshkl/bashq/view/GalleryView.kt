package by.vshkl.bashq.view

import by.vshkl.bashq.model.Comic

interface GalleryView {

    fun onLoadSuccess(comics: MutableList<Comic>, next: Boolean)

    fun onLoadingError(errorMessage: String?)
}