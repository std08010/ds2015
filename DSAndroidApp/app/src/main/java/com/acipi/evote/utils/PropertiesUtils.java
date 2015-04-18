package com.acipi.evote.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Altin Cipi on 2/7/2015.
 */
public class PropertiesUtils
{
    private static Properties urlsProps;
    private static Properties dbProps;
    private static Properties sysProps;

    public static Properties getUrlsProps(Context context)
    {
        if (urlsProps == null)
        {
            try
            {
                urlsProps = new Properties();
                AssetManager assetManager = context.getAssets();
                InputStream inputStream = assetManager.open("urls.properties");
                urlsProps.load(inputStream);
            }
            catch (IOException e)
            {
                Log.e("PropertiesUtils", e.getLocalizedMessage(), e);
            }
        }

        return urlsProps;
    }

    public static Properties getDBProps(Context context)
    {
        if (dbProps == null)
        {
            try
            {
                dbProps = new Properties();
                AssetManager assetManager = context.getAssets();
                InputStream inputStream = assetManager.open("db.properties");
                dbProps.load(inputStream);
            }
            catch (IOException e)
            {
                Log.e("PropertiesUtils", e.getLocalizedMessage(), e);
            }
        }

        return dbProps;
    }

    public static Properties getSysProps(Context context)
    {
        if (sysProps == null)
        {
            try
            {
                sysProps = new Properties();
                AssetManager assetManager = context.getAssets();
                InputStream inputStream = assetManager.open("system.properties");
                sysProps.load(inputStream);
            }
            catch (IOException e)
            {
                Log.e("PropertiesUtils", e.getLocalizedMessage(), e);
            }
        }

        return sysProps;
    }
}
