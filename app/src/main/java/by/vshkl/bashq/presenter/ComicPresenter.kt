package by.vshkl.bashq.presenter

import by.vshkl.bashq.ui.activity.ComicActivity
import okhttp3.OkHttpClient

class ComicPresenter(val activity: ComicActivity) {

    val client: OkHttpClient

    init {
        client = OkHttpClient()
    }

    fun loadComic(url: String) {

    }

}
