package by.vshkl.bashq.common

import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import by.vshkl.bashq.R
import by.vshkl.bashq.ui.activity.ComicViewActivity
import by.vshkl.bashq.ui.activity.GalleryActivity

class Navigator {

    companion object {
        fun navigateToGalleryActivity(context: Context) {
            val intent = GalleryActivity.getCallingIntent(context)

            context.startActivity(intent)
        }

        fun navigateToComicViewActivity(context: Context, comicUrl: String, view: View) {
            val intent = ComicViewActivity.getCallingIntent(context)
            intent.putExtra(GalleryActivity.EXTRA_COMIC_URL, comicUrl)

            val sharedView = view
            val transitionName = context.getString(R.string.comic_view_transition)
            val transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as AppCompatActivity, sharedView, transitionName)

            context.startActivity(intent, transitionActivityOptions.toBundle())
        }

        fun navigateToShareChooser(context: Context, quoteText: String) {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, quoteText)
            intent.type = "text/plain"

            context.startActivity(intent)
        }
    }
}
