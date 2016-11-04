package by.vshkl.bashq.common;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.Html;

import javax.inject.Inject;

import by.vshkl.bashq.injection.scope.PerActivity;

@PerActivity
public class Navigator {

    @Inject
    public Navigator() {
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

    //==================================================================================================================

    public void copyQuoteTextToClipboard(Context context, String quoteText) {
        if (context != null) {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("", Html.fromHtml(quoteText).toString());
            clipboardManager.setPrimaryClip(clipData);
        }
    }
}
