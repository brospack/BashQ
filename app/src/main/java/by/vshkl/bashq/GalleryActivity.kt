package by.vshkl.bashq

import android.app.Activity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import by.vshkl.bashq.constants.Urls
import by.vshkl.bashq.presenter.GalleryPresenter

class GalleryActivity : AppCompatActivity() {

    val toolbar by lazy { find<Toolbar>(R.id.toolbar) }
    val swipe by lazy { find<SwipeRefreshLayout>(R.id.swipe) }
    val list by lazy { find<RecyclerView>(R.id.grid) }

    var presenter: GalleryPresenter

    init {
        presenter = GalleryPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        toolbar.setOnClickListener { list.scrollToPosition(0) }

        val url = Urls.Companion.urlComicsCalendar
        presenter.loadComics(url, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
          when (itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    /***********************************************************************************************
     * Inline and extension functions
     */

    inline fun <reified T : View> Activity.find(id: Int): T = findViewById(id) as T

}
