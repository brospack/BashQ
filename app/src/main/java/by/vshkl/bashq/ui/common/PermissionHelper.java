package by.vshkl.bashq.ui.common;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import by.vshkl.bashq.common.Navigator;

public class PermissionHelper {

    public static void requestPermission(
            final AppCompatActivity activity,
            final Navigator navigator,
            final String permissionName,
            final int permissionRequestCode,
            final String title,
            final String message,
            final String comicImageLink) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showPermissionRationale(activity, permissionName, permissionRequestCode, title, message);
            } else {
                requestPermission(activity, permissionName, permissionRequestCode);
            }
        } else {
            navigator.navigateToComicsDownloadImage(activity, comicImageLink);
        }
    }

    private static void showPermissionRationale(
            final AppCompatActivity activity,
            final String permissionName,
            final int permissionRequestCode,
            final String title,
            final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermission(activity, permissionName, permissionRequestCode);
                    }
                }).create();
        builder.show();
    }

    private static void requestPermission(
            final AppCompatActivity activity,
            final String permissionName,
            final int permissionRequestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permissionName}, permissionRequestCode);
    }
}
