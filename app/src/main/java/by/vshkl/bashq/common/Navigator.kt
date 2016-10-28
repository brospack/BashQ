package by.vshkl.bashq.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import by.vshkl.bashq.R
import by.vshkl.bashq.ui.activity.ComicActivity
import by.vshkl.bashq.ui.activity.GalleryActivity

class Navigator {

    companion object {
        fun navigateToGalleryActivity(context: Context) {
            val intent = GalleryActivity.getCallingIntent(context)

            context.startActivity(intent)
        }

        fun navigateToComicViewActivity(context: Context, comicUrl: String, view: View) {
            val intent = ComicActivity.getCallingIntent(context)
            intent.putExtra(GalleryActivity.EXTRA_COMIC_URL, comicUrl)

            val sharedView = view
            val transitionName = context.getString(R.string.comic_view_transition)
            val transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as AppCompatActivity, sharedView, transitionName)

            context.startActivity(intent, transitionActivityOptions.toBundle())
        }

        fun navigateToShareQuoteChooser(context: Context, quoteText: String) {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, quoteText)
            intent.type = "text/plain"

            context.startActivity(Intent.createChooser(intent, "Share quote..."))
        }

        fun navigateToShareComicChooser(context: Context, quoteText: String, imageUri: Uri) {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, quoteText)
            intent.putExtra(Intent.EXTRA_STREAM, imageUri)
            intent.type = "image/*"
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            context.startActivity(Intent.createChooser(intent, "Share comic..."))
        }
    }
}
