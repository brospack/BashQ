package by.vshkl.bashq.presenter

import by.vshkl.bashq.R
import by.vshkl.bashq.model.Comic
import by.vshkl.bashq.ui.activity.GalleryActivity
import okhttp3.*
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*

class GalleryPresenter(val activity: GalleryActivity) {

    val firstYear: Int = 2007
    val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)
    val client: OkHttpClient

    init {
        client = OkHttpClient()
    }

    fun loadComics(url: String, next: Boolean) {
        var fullUrl: String = url
        if (next && currentYear >= firstYear) {
            fullUrl = url + "/" + currentYear.minus(1).toString()
        }

        val request: Request = Request.Builder().url(fullUrl).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity.runOnUiThread { activity.onLoadingError(activity.getString(R.string.message_error_connection)) }
            }

            override fun onResponse(call: Call, response: Response) {
                val comics: MutableList<Comic> = ArrayList()
                val document = Jsoup.parse(response.body().string())

                val comicsElements = document.select("div#calendar a[href]")

                for (e in comicsElements) {
                    val comic = Comic(
                            thumbLink = e.child(0).attr("src"),
                            comicLink = "http://bash.im".plus(e.attr("href"))
                    )
                    comics.add(comic)
                    comics.reverse()
                }

                activity.runOnUiThread { activity.onLoadSuccess(comics, next) }
            }
        })
    }
}