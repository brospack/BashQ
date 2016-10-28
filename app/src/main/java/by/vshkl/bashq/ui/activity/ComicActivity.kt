package by.vshkl.bashq.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import by.vshkl.bashq.R
import by.vshkl.bashq.model.ComicDetail
import by.vshkl.bashq.presenter.ComicPresenter
import by.vshkl.bashq.utils.PicassoDecoder
import by.vshkl.bashq.utils.PicassoRegionDecoder
import by.vshkl.bashq.view.ComicView
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.pnikosis.materialishprogress.ProgressWheel
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient

class ComicActivity : AppCompatActivity(), ComicView {

    val toolbar by lazy { find<Toolbar>(R.id.toolbar) }
    val comic by lazy { find<SubsamplingScaleImageView>(R.id.comic) }
    val progress by lazy { find<ProgressWheel>(R.id.progress) }
    val swipe by lazy { find<SwipeRefreshLayout>(R.id.swipe) }
    val bottomSheet by lazy { find<ScrollView>(R.id.bottom_sheet) }
    val quoteContent by lazy { find<TextView>(R.id.quote) }

    val bottomSheetBehaviour: BottomSheetBehavior<ScrollView>? by lazy { BottomSheetBehavior.from(bottomSheet) }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, ComicActivity::class.java)
        }
    }

    var presenter: ComicPresenter

    init {
        presenter = ComicPresenter(this)
    }

    /***********************************************************************************************
     * Lifecycle methods
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val comicUrl = intent.getStringExtra(GalleryActivity.EXTRA_COMIC_URL)
        presenter.loadComic(comicUrl)

        swipe.isEnabled = false
        swipe.setOnRefreshListener {
            presenter.loadComic(comicUrl)
        }

        comic.setOnClickListener({
            bottomSheetBehaviour?.state = BottomSheetBehavior.STATE_COLLAPSED
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

    override fun onLoadSuccess(comicDetail: ComicDetail) {
        comic.visibility = View.VISIBLE

        val creator = comicDetail.creator
        val index = creator.indexOf("по")
        toolbar.title = creator.substring(0, index)
        toolbar.subtitle = creator.substring(index)

        comic.setBitmapDecoderFactory { PicassoDecoder(comicDetail.imageLink, Picasso.with(this)) }
        comic.setRegionDecoderFactory { PicassoRegionDecoder(OkHttpClient()) }
        comic.setOnImageEventListener(object : SubsamplingScaleImageView.DefaultOnImageEventListener() {
            override fun onReady() {
                progress.visibility = View.GONE
            }

            override fun onImageLoaded() {
            }
        })
        comic.setImage(ImageSource.uri(comicDetail.imageLink))

        quoteContent.text = Html.fromHtml(comicDetail.quoteContent)
    }

    override fun onLoadingError(errorMessage: String?) {
        progress.visibility = View.GONE
        comic.visibility = View.VISIBLE

        swipe.isEnabled = true

        toast(errorMessage.toString())
    }

    /***********************************************************************************************
     * Inline and extension functions
     */

    inline fun <reified T : View> Activity.find(id: Int): T = findViewById(id) as T

    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }
}