package by.vshkl.bashq.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import by.vshkl.bashq.R
import by.vshkl.bashq.model.ComicDetail
import by.vshkl.bashq.presenter.ComicPresenter
import by.vshkl.bashq.utils.PicassoDecoder
import by.vshkl.bashq.utils.PicassoRegionDecoder
import by.vshkl.bashq.view.Comic
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient

class ComicActivity : AppCompatActivity(), Comic {

    val toolbar by lazy { find<Toolbar>(R.id.toolbar) }
    val comic by lazy { find<SubsamplingScaleImageView>(R.id.comic) }
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
        comic.setBitmapDecoderFactory { PicassoDecoder(comicDetail.imageLink, Picasso.with(this)) }
        comic.setRegionDecoderFactory { PicassoRegionDecoder(OkHttpClient()) }
        comic.setImage(ImageSource.uri(comicDetail.imageLink))

        quoteContent.text = Html.fromHtml(comicDetail.quoteContent)

        toolbar.subtitle = comicDetail.creator
    }

    override fun onLoadingError(errorMessage: String?) {
    }

    /***********************************************************************************************
     * Inline and extension functions
     */

    inline fun <reified T : View> Activity.find(id: Int): T = findViewById(id) as T
}