package by.vshkl.bashq

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import by.vshkl.bashq.constants.Urls
import by.vshkl.bashq.model.Comic
import by.vshkl.bashq.presenter.GalleryPresenter
import by.vshkl.bashq.view.Gallery
import by.vshkl.bashq.view.GalleryActionListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_comic.view.*
import java.util.*

class GalleryActivity : AppCompatActivity(), Gallery, GalleryActionListener {

    val toolbar by lazy { find<Toolbar>(R.id.toolbar) }
    val swipe by lazy { find<SwipeRefreshLayout>(R.id.swipe) }
    val grid by lazy { find<RecyclerView>(R.id.grid) }

    var presenter: GalleryPresenter
    var comicsList: MutableList<Comic> = ArrayList()

    init {
        presenter = GalleryPresenter(this)
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

        val url = Urls.Companion.urlComicsCalendar
        presenter.loadComics(url, false)

        swipe.setOnRefreshListener { presenter.loadComics(url, false) }
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
            grid.adapter = GalleryActivity.ComicsAdapter(comicsList, this, this)
        } else {
            comicsList.addAll(comics)
            grid.adapter.notifyDataSetChanged()
        }
    }

    override fun onLoadingError(errorMessage: String?) {
        toast(errorMessage.toString())
    }

    override fun onGalleryItemClicked() {

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

    class ComicsAdapter(val comics: List<Comic>, val listener: GalleryActionListener, val context: Context) :
            RecyclerView.Adapter<GalleryActivity.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryActivity.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_comic, parent, false)
            return GalleryActivity.ViewHolder(v, listener, context)
        }

        override fun onBindViewHolder(holder: GalleryActivity.ViewHolder, position: Int) {
            holder.bindComic(comics[position])
        }

        override fun getItemCount() = comics.size
    }

    /***********************************************************************************************
     * RecyclerView view holder
     */

    class ViewHolder(view: View, val listener: GalleryActionListener, val context: Context) :
            RecyclerView.ViewHolder(view) {

        fun bindComic(comic: Comic) {
            with(comic) {
                Picasso.with(context).load(comic.thumbLink).into(itemView.image)
            }
        }
    }
}
