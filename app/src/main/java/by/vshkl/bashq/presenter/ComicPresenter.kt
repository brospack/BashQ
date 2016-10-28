package by.vshkl.bashq.presenter

import by.vshkl.bashq.R
import by.vshkl.bashq.model.ComicDetail
import by.vshkl.bashq.ui.activity.ComicActivity
import okhttp3.*
import org.jsoup.Jsoup
import java.io.IOException

class ComicPresenter(val activity: ComicActivity) {

    val client: OkHttpClient

    init {
        client = OkHttpClient()
    }

    fun loadComic(url: String) {
        val request: Request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity.runOnUiThread { activity.onLoadingError(activity.getString(R.string.message_error_connection)) }
            }

            override fun onResponse(call: Call, response: Response) {
                val document = Jsoup.parse(response.body().string())

                val comicElement = document.select("div#the_strip").first()
                val boilerElement = document.select("div#boiler").first()

                val comicDetail = ComicDetail(
                        imageLink = comicElement.child(0).attr("src"),
                        creator = boilerElement.child(1).text(),
                        quoteLink = "http://bash.im" + boilerElement.child(1).child(1).attr("href")
                )

                loadQuoteContent(comicDetail)
            }

        })
    }

    fun loadQuoteContent(comicDetail: ComicDetail) {
        val request: Request = Request.Builder().url(comicDetail.quoteLink).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity.runOnUiThread { activity.onLoadingError(activity.getString(R.string.message_error_connection)) }
            }

            override fun onResponse(call: Call, response: Response) {
                val document = Jsoup.parse(response.body().string())

                comicDetail.quoteContent = document.select(".text").first().html()

                activity.runOnUiThread { activity.onLoadSuccess(comicDetail) }
            }

        })
    }

}
