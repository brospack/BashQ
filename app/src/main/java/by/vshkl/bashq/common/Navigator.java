package by.vshkl.bashq.common;

import android.content.Context;
import android.content.Intent;
import android.text.Html;

import javax.inject.Inject;

import by.vshkl.bashq.injection.scope.PerActivity;
import by.vshkl.bashq.ui.acticity.ComicsActivity;

@PerActivity
public class Navigator {

    @Inject
    public Navigator() {
    }

    public void navigateToComicsActivity(Context context) {
        if (context != null) {
            context.startActivity(ComicsActivity.getCallingIntent(context));
        }
    }

    public void navigateToQuoteShareLinkChooser(Context context, String quoteLink) {
        if (context != null) {
            Intent shareLinkIntent = new Intent();
            shareLinkIntent.setAction(Intent.ACTION_SEND);
            shareLinkIntent.setType("text/plain");
            shareLinkIntent.putExtra(Intent.EXTRA_TEXT, quoteLink);
            context.startActivity(Intent.createChooser(shareLinkIntent, "Share quote link with..."));
        }
    }

    public void navigateToQuoteShareTextChooser(Context context, String quoteText) {
        if (context != null) {
            Intent shareTextIntent = new Intent();
            shareTextIntent.setAction(Intent.ACTION_SEND);
            shareTextIntent.setType("text/plain");
            shareTextIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(quoteText).toString());
            context.startActivity(Intent.createChooser(shareTextIntent, "Share quote text with..."));
        }
    }

    public void navigateToComicsShareImageChooser(Context context, String comicsImageLink) {
        if (context != null) {
            Intent shareTextIntent = new Intent();
            shareTextIntent.setAction(Intent.ACTION_SEND);
            shareTextIntent.setType("text/plain");
            shareTextIntent.putExtra(Intent.EXTRA_TEXT, comicsImageLink);
            context.startActivity(Intent.createChooser(shareTextIntent, "Share comics with..."));
        }
    }
}
