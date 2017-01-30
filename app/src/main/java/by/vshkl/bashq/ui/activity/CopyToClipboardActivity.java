package by.vshkl.bashq.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import by.vshkl.bashq.BashqApplication;
import by.vshkl.bashq.R;

public class CopyToClipboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT) && intent.getType().equals("text/plain")) {
            String contentToCopy = intent.getStringExtra(Intent.EXTRA_TEXT);
            ClipData clipData = ClipData.newPlainText("", contentToCopy);
            clipboardManager.setPrimaryClip(clipData);

            Toast.makeText(this, R.string.message_copied_to_clipboard, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BashqApplication.getRefWatcher(this).watch(this);
    }
}
