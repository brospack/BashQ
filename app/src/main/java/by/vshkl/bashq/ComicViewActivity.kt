package by.vshkl.bashq

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import by.vshkl.bashq.utils.PicassoDecoder
import by.vshkl.bashq.utils.PicassoRegionDecoder
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient

class ComicViewActivity : AppCompatActivity() {

    val toolbar by lazy { find<Toolbar>(R.id.toolbar) }
    val comic by lazy { find<SubsamplingScaleImageView>(R.id.comic) }

    /***********************************************************************************************
     * Lifecycle methods
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val comicUrl = intent.getStringExtra(GalleryActivity.Companion.EXTRA_COMIC_URL)

        comic.setBitmapDecoderFactory { PicassoDecoder(comicUrl, Picasso.with(this)) }
        comic.setRegionDecoderFactory { PicassoRegionDecoder(OkHttpClient()) }
        comic.setImage(ImageSource.uri(comicUrl))
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