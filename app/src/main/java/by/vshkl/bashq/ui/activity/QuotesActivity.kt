package by.vshkl.bashq.ui.activity

import android.app.Activity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Html
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import by.vshkl.bashq.R
import by.vshkl.bashq.common.Navigator
import by.vshkl.bashq.constants.Urls
import by.vshkl.bashq.model.Quote
import by.vshkl.bashq.presenter.QuotesPresenter
import by.vshkl.bashq.ui.adapter.QuotesAdapter
import by.vshkl.bashq.view.QuoteActionListener
import by.vshkl.bashq.view.QuotesView
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.pnikosis.materialishprogress.ProgressWheel
import java.util.*

class QuotesActivity : AppCompatActivity(), QuotesView, QuoteActionListener {

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

        url = Urls.urlNew
        presenter.loadQuotes(url, false)

        initializeDrawer(savedInstanceState, toolbar, presenter)

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

    override fun share(content: String, link: String): Boolean {
        Navigator.navigateToShareQuoteChooser(this@QuotesActivity, Html.fromHtml(content).toString() + '\n' + '\n' + link)
        return true
    }

    override fun vote(actionLink: String, action: String) {
        presenter.vote(actionLink, action)
    }

    /***********************************************************************************************
     * Various additional methods
     */

    private fun initializeDrawer(savedInstanceState: Bundle?, toolbar: Toolbar, presenter: QuotesPresenter) {
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
                            url = Urls.urlNew
                            toolbar.setSubtitle(R.string.drawer_item_new)
                        }
                        2 -> {
                            url = Urls.urlRandom
                            toolbar.setSubtitle(R.string.drawer_item_random)
                        }
                        3 -> {
                            url = Urls.urlBest
                            toolbar.setSubtitle(R.string.drawer_item_best)
                        }
                        4 -> {
                            url = Urls.urlByRating
                            toolbar.setSubtitle(R.string.drawer_item_best)
                        }
                        5 -> {
                            url = Urls.urlAbyss
                            toolbar.setSubtitle(R.string.drawer_item_abyss)
                        }
                        6 -> {
                            url = Urls.urlAbyssTop
                            toolbar.setSubtitle(R.string.drawer_item_abyss_top)
                        }
                        7 -> {
                            url = Urls.urlAbyssBest
                            toolbar.setSubtitle(R.string.drawer_item_abyss_best)
                        }
                        8 -> {
                            Navigator.navigateToGalleryActivity(this@QuotesActivity)
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
}
