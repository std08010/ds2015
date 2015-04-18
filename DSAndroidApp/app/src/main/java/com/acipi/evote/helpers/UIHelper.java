package com.acipi.evote.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Altin Cipi on 3/3/2015.
 */
public class UIHelper
{
    public static ProgressDialog pDialog;

    public static void showProgressDialog(Context mContext, String processMessage)
    {
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(processMessage);
        pDialog.setProgressDrawable(mContext.getWallpaper());
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public static void hideProgressDialog()
    {
        pDialog.dismiss();
    }

    public static void displayMessage(Context mContext, String Message)
    {
        Toast.makeText(mContext, Message, Toast.LENGTH_SHORT).show();
    }
}
