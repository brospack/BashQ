package by.vshkl.bashq.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import by.vshkl.bashq.R
import by.vshkl.bashq.common.Navigator
import by.vshkl.bashq.constants.Urls
import by.vshkl.bashq.model.Comic
import by.vshkl.bashq.presenter.GalleryPresenter
import by.vshkl.bashq.ui.adapter.GalleryAdapter
import by.vshkl.bashq.view.GalleryActionListener
import by.vshkl.bashq.view.GalleryView
import com.pnikosis.materialishprogress.ProgressWheel
import java.util.*

class GalleryActivity : AppCompatActivity(), GalleryView, GalleryActionListener {

    val toolbar by lazy { find<Toolbar>(R.id.toolbar) }
    val container by lazy { find<FrameLayout>(R.id.container) }
    val swipe by lazy { find<SwipeRefreshLayout>(R.id.swipe) }
    val grid by lazy { find<RecyclerView>(R.id.grid) }
    val progress by lazy { find<ProgressWheel>(R.id.progress) }

    var presenter: GalleryPresenter
    var comicsList: MutableList<Comic> = ArrayList()

    init {
        presenter = GalleryPresenter(this)
    }

    companion object {
        val EXTRA_COMIC_URL = "EXTRA_COMIC_URL"

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, GalleryActivity::class.java)
        }
    }

    /***********************************************************************************************
     * Lifecycle methods
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setOnClickListener { grid.scrollToPosition(0) }
        toolbar.setSubtitle(R.string.drawer_item_gallery)

        grid.layoutManager = GridLayoutManager(this, 3)

        val url = Urls.urlComicsCalendar
        presenter.loadComics(url, false)

        swipe.setOnRefreshListener { presenter.loadComics(url, false) }

        grid.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0 && recyclerView?.adapter?.itemCount != null) {
                    val lastItemPos = (recyclerView?.layoutManager as LinearLayoutManager)
                            .findLastCompletelyVisibleItemPosition()
                    if (lastItemPos != RecyclerView.NO_POSITION && lastItemPos
                            == recyclerView?.adapter?.itemCount?.minus(1)) {
                        swipe.isRefreshing = true
                        presenter.loadComics(url, true)
                    }
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    /***********************************************************************************************
     * Interfaces implementations
     */

    override fun onLoadSuccess(comics: MutableList<Comic>, next: Boolean) {
        swipe.isRefreshing = false

        if (!next) {
            comicsList = comics
            grid.adapter = GalleryAdapter(comicsList, this, this)
        } else {
            comicsList.addAll(comics)
            grid.adapter.notifyDataSetChanged()
        }

        progress.visibility = View.GONE
        container.visibility = View.VISIBLE
    }

    override fun onLoadingError(errorMessage: String?) {
        toast(errorMessage.toString())
    }

    override fun onGalleryItemClicked(comicUrl: String, view: View) {
        Navigator.navigateToComicViewActivity(this@GalleryActivity, comicUrl, view)
    }

    /***********************************************************************************************
     * Inline and extension functions
     */

    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    inline fun <reified T : View> Activity.find(id: Int): T = findViewById(id) as T
}