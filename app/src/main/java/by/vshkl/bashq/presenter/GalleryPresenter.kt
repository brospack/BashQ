package by.vshkl.bashq.presenter

import by.vshkl.bashq.GalleryActivity
import by.vshkl.bashq.model.Comic
import okhttp3.*
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*

class GalleryPresenter(val activity: GalleryActivity) {

    val firstYear: Int = 2007
    val client: OkHttpClient

    init {
        client = OkHttpClient()
    }

    fun loadComics(url: String, next: Boolean) {
        val fullUrl: String = url
        if (next) {

        }

        val request: Request = Request.Builder().url(fullUrl).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val comics: MutableList<Comic> = ArrayList()
                val document = Jsoup.parse(response.body().string())

                val comicsElements = document.select("div#calendar a[href]")
                for (e in comicsElements) {
                    val comic = Comic(
                            thumbLink = e.child(0).attr("src"),
                            imageLink = e.child(0).attr("src").replace("ts/", "")
                    )
                    comics.add(comic)
                }
            }

        })
    }
}
