package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtil {
    private Context context ;

    public PermissionUtil(Context context) {
        this.context = context;
    }

    public void ask_permission (final String  permission_type,final int MY_PERMISSIONS_REQUEST_CODE,String message_title,String message_text) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission_type)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle(message_title);
            alertBuilder.setMessage(message_text);
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{permission_type},
                            MY_PERMISSIONS_REQUEST_CODE);
                }
            });

            AlertDialog alert = alertBuilder.create( );
            alert.show();
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{permission_type},
                    MY_PERMISSIONS_REQUEST_CODE);
        }
    }

    public Boolean has_permission (String permission_type){
        return (ContextCompat.checkSelfPermission(context, permission_type) == PackageManager.PERMISSION_GRANTED);
    }
}
