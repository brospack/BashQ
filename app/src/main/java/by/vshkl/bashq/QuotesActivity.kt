package by.vshkl.bashq

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import by.vshkl.bashq.constants.Urls
import by.vshkl.bashq.model.Quote
import by.vshkl.bashq.model.Rating
import by.vshkl.bashq.presenter.QuotesPresenter
import by.vshkl.bashq.utils.Ratings
import by.vshkl.bashq.view.QuoteActionListener
import by.vshkl.bashq.view.QuotesList
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.pnikosis.materialishprogress.ProgressWheel
import kotlinx.android.synthetic.main.item_quote.view.*
import org.jsoup.Jsoup
import java.util.*

class QuotesActivity : AppCompatActivity(), QuotesList, QuoteActionListener {

    val toolbar by lazy { find<Toolbar>(R.id.toolbar) }
    val container by lazy {find<FrameLayout>(R.id.container)}
    val swipe by lazy { find<SwipeRefreshLayout>(R.id.swipe) }
    val list by lazy { find<RecyclerView>(R.id.list) }
    val progress by lazy { find<ProgressWheel>(R.id.progress) }

    var url: String = ""
    var quotesList: MutableList<Quote> = ArrayList()

    var presenter: QuotesPresenter

    init {
        presenter = QuotesPresenter(this)
    }

    /***********************************************************************************************
     * Lifecycle methods
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes)
        setSupportActionBar(toolbar)

        toolbar.setOnClickListener { list.scrollToPosition(0) }
        toolbar.setSubtitle(R.string.drawer_item_new)

        list.layoutManager = LinearLayoutManager(this)

        url = Urls.Companion.urlNew
        presenter.loadQuotes(url, false)

        initDrawer(savedInstanceState, toolbar, presenter)

        swipe.setOnRefreshListener { presenter.loadQuotes(url, false) }

        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0 && recyclerView?.adapter?.itemCount != null) {
                    val lastItemPos = (recyclerView?.layoutManager as LinearLayoutManager)
                            .findLastCompletelyVisibleItemPosition()
                    if (lastItemPos != RecyclerView.NO_POSITION && lastItemPos
                            == recyclerView?.adapter?.itemCount?.minus(3)) {
                        presenter.loadQuotes(url, true)
                    }
                }
            }
        })
    }

    /***********************************************************************************************
     * Interfaces implementations
     */

    override fun onLoadSuccess(quotes: MutableList<Quote>, next: Boolean) {
        swipe.isRefreshing = false

        if (!next) {
            quotesList = quotes
            list.adapter = QuotesAdapter(quotesList, this)
        } else {
            quotesList.addAll(quotes)
            list.adapter.notifyDataSetChanged()
        }

        progress.visibility = View.GONE
        container.visibility = View.VISIBLE
    }

    override fun onLoadingError(errorMessage: String?) {
        swipe.isRefreshing = false
        toast(errorMessage.toString())
    }

    override fun share(content: String): Boolean {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, Jsoup.parse(content).text())
        intent.type = "text/plain"
        startActivity(intent)
        return true
    }

    override fun vote(actionLink: String, action: String) {
        presenter.vote(actionLink, action)
    }

    /***********************************************************************************************
     * Various additional methods
     */

    private fun initDrawer(savedInstanceState: Bundle?, toolbar: Toolbar, presenter: QuotesPresenter) {
        DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSavedInstance(savedInstanceState)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        PrimaryDrawerItem()
                                .withName(R.string.drawer_item_new)
                                .withIcon(R.drawable.ic_new)
                                .withIdentifier(1),
                        PrimaryDrawerItem()
                                .withName(R.string.drawer_item_random)
                                .withIcon(R.drawable.ic_random)
                                .withIdentifier(2),
                        PrimaryDrawerItem()
                                .withName(R.string.drawer_item_best)
                                .withIcon(R.drawable.ic_best)
                                .withIdentifier(3),
                        PrimaryDrawerItem()
                                .withName(R.string.drawer_item_rating)
                                .withIcon(R.drawable.ic_rating)
                                .withIdentifier(4),
                        PrimaryDrawerItem()
                                .withName(R.string.drawer_item_abyss)
                                .withIcon(R.drawable.ic_abyss)
                                .withIdentifier(5),
                        PrimaryDrawerItem()
                                .withName(R.string.drawer_item_abyss_top)
                                .withIcon(R.drawable.ic_abyss_top)
                                .withIdentifier(6),
                        PrimaryDrawerItem()
                                .withName(R.string.drawer_item_abyss_best)
                                .withIcon(R.drawable.ic_abyss_best)
                                .withIdentifier(7),
                        PrimaryDrawerItem()
                                .withName(R.string.drawer_item_gallery)
                                .withIcon(R.drawable.ic_gallery)
                                .withIdentifier(8)
                )
                .withOnDrawerItemClickListener { view, position, drawerItem ->
                    val identifier = drawerItem?.identifier?.toInt()
                    when (identifier) {
                        1 -> {
                            url = Urls.Companion.urlNew
                            toolbar.setSubtitle(R.string.drawer_item_new)
                        }
                        2 -> {
                            url = Urls.Companion.urlRandom
                            toolbar.setSubtitle(R.string.drawer_item_random)
                        }
                        3 -> {
                            url = Urls.Companion.urlBest
                            toolbar.setSubtitle(R.string.drawer_item_best)
                        }
                        4 -> {
                            url = Urls.Companion.urlByRating
                            toolbar.setSubtitle(R.string.drawer_item_best)
                        }
                        5 -> {
                            url = Urls.Companion.urlAbyss
                            toolbar.setSubtitle(R.string.drawer_item_abyss)
                        }
                        6 -> {
                            url = Urls.Companion.urlAbyssTop
                            toolbar.setSubtitle(R.string.drawer_item_abyss_top)
                        }
                        7 -> {
                            url = Urls.Companion.urlAbyssBest
                            toolbar.setSubtitle(R.string.drawer_item_abyss_best)
                        }
                        8 -> {
                            val preferenceIntent = Intent(this@QuotesActivity,
                                    GalleryActivity::class.java)
                            startActivity(preferenceIntent)
                        }
                    }
                    presenter.loadQuotes(url, false)
                    false
                }
                .build()
    }

    /***********************************************************************************************
     * Inline and extension functions
     */

    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    inline fun <reified T : View> Activity.find(id: Int): T = findViewById(id) as T

    /***********************************************************************************************
     * RecyclerView adapter
     */

    class QuotesAdapter(val quotes: List<Quote>, val listener: QuoteActionListener) :
            RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_quote, parent, false)
            return ViewHolder(v, listener)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindQuote(quotes[position])
        }

        override fun getItemCount() = quotes.size
    }

    /***********************************************************************************************
     * RecyclerView view holder
     */

    class ViewHolder(view: View, val listener: QuoteActionListener) :
            RecyclerView.ViewHolder(view) {

        fun bindQuote(quote: Quote) {
            with(quote) {
                itemView.number.text = quote.id
                itemView.date.text = quote.date
                itemView.content.text = Html.fromHtml(quote.content)
                if (quote.rating.equals("")) {
                    itemView.votes.visibility = View.GONE
                    itemView.votesDivider.visibility = View.GONE
                } else {
                    itemView.rating.text = quote.rating
                    itemView.votePlus.setOnClickListener {
                        listener.vote(voteUp, "rulez")
                        val ratingObj = Ratings.Companion.updateRating(Rating(quote.rating, quote.voteCount), 1)
                        quote.voteCount = ratingObj.voteCount
                        itemView.rating.text = ratingObj.rating
                    }
                    itemView.voteMinus.setOnClickListener {
                        listener.vote(voteDown, "sux")
                        val ratingObj = Ratings.Companion.updateRating(Rating(quote.rating, quote.voteCount), -1)
                        quote.voteCount = ratingObj.voteCount
                        itemView.rating.text = ratingObj.rating
                    }
                    itemView.voteOld.setOnClickListener {
                        listener.vote(voteOld, "bayan")
                        val ratingObj = Ratings.Companion.updateRating(Rating(quote.rating, quote.voteCount), 0)
                        quote.voteCount = ratingObj.voteCount
                        itemView.rating.text = ratingObj.rating
                    }
                    itemView.setOnLongClickListener { listener.share(content) }
                }
            }
        }
    }
}
