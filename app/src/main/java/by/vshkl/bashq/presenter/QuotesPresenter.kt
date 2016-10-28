package by.vshkl.bashq.presenter

import by.vshkl.bashq.R
import by.vshkl.bashq.model.Quote
import by.vshkl.bashq.ui.activity.QuotesActivity
import okhttp3.*
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*
import java.util.regex.Pattern

class QuotesPresenter(var activity: QuotesActivity) {

    var baseUrl = ""
    var loadMoreLinkPart = ""
    val client: OkHttpClient

    init {
        client = OkHttpClient()
    }

    fun loadQuotes(url: String, next: Boolean) {
        baseUrl = url
        var fullUrl = url
        if (next) {
            fullUrl = url + loadMoreLinkPart
        }

        val request: Request = Request.Builder().url(fullUrl).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity.runOnUiThread { activity.onLoadingError(activity.getString(R.string.message_error_connection)) }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val quotes: MutableList<Quote> = ArrayList()
                val document = Jsoup.parse(response.body().string())

                val quoteElements = document.select(".quote")
                for (element in quoteElements) {
                    if (element.select(".actions").first() != null) {

                        val id: String
                        if (element.select(".id").first() != null) {
                            id = element.select(".id").first().text()
                        } else {
                            id = element.select(".abysstop").first().text()
                        }

                        val date: String
                        if (element.select(".date").first() != null) {
                            date = element.select(".date").first().text()
                        } else {
                            date = element.select(".abysstop-date").text()
                        }

                        val rating: String
                        if (element.select(".rating").first() != null) {
                            rating = element.select(".rating").first().text()
                        } else {
                            rating = ""
                        }

                        val actions = element.select(".up").first()
                        val voteUpLink: String
                        val voteDownLink: String
                        val voteOldLink: String
                        if (actions != null) {
                            voteUpLink = baseUrl + element.select(".up").first().attr("href")
                            voteDownLink = baseUrl + element.select(".down").first().attr("href")
                            voteOldLink = baseUrl + element.select(".old").first().attr("href")
                        } else {
                            voteUpLink = ""
                            voteDownLink = ""
                            voteOldLink = ""
                        }

                        val link = element.select(".id").first()
                        val quoteLink: String
                        if (link != null) {
                            quoteLink = baseUrl + link.attr("href")
                        } else {
                            quoteLink = ""
                        }

                        val quote = Quote(
                                id = id,
                                link = quoteLink,
                                date = date,
                                rating = rating,
                                content = element.select(".text").first().html(),
                                voteUp = voteUpLink,
                                voteDown = voteDownLink,
                                voteOld = voteOldLink
                        )
                        quotes.add(quote)
                    }
                }

                val nextPage = document.select(".pager").first()
                val nextRandom = document.select(".quote.more").first()
                if (nextPage != null) {
                    val link = nextPage.select("a[href]").first()?.attr("href")
                    if (link != null) {
                        loadMoreLinkPart = link
                    }
                } else if (nextRandom != null) {
                    val link = nextRandom.select("a[href]").first()?.attr("href")
                    if (link != null) {
                        loadMoreLinkPart = link
                    }
                }

                activity.runOnUiThread { activity.onLoadSuccess(quotes, next) }
            }
        })
    }

    fun vote(voteUrl: String, action: String) {
        val pattern = Pattern.compile("[0-9]+")
        val matcher = pattern.matcher(voteUrl)
        while (matcher.find()) {
            val request: Request = Request.Builder()
                    .url(voteUrl)
                    .post(RequestBody.create(MediaType.parse(
                            "application/x-www-form-urlencoded; charset=UTF-8"),
                            "quote=" + matcher.group() + "&act=" + action))
                    .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                }
            })
        }
    }
}
