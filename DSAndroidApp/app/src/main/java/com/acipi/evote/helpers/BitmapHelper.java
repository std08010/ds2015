package com.acipi.evote.helpers;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;

/**
 * Created by Altin Cipi on 3/7/2015.
 */
public class BitmapHelper
{
    public static Bitmap decodeSampledBitmapFromResource(String strPath, int reqWidth, int reqHeight)
    {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(strPath, options);

        // Calculate inSampleSize
        if(options.outWidth > options.outHeight)
        {
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);//if landscape
        }
        else
        {
            options.inSampleSize = calculateInSampleSize(options, reqHeight, reqWidth);//if portrait
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(strPath, options);
    }

    public static Bitmap decodeSampledBitmapFromResourceV2(String strPath, int reqWidth, int reqHeight)
    {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(strPath, options);

        if(options.outWidth > options.outHeight)
        {
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            return Bitmap.createScaledBitmap(BitmapFactory.decodeFile(strPath, options), reqWidth, reqHeight, true);//if landscape
        }
        else
        {
            options.inSampleSize = calculateInSampleSize(options, reqHeight, reqWidth);
            options.inJustDecodeBounds = false;
            return Bitmap.createScaledBitmap(BitmapFactory.decodeFile(strPath, options), reqHeight, reqWidth, true);//if portrait
        }
    }

    public static Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fileDescriptor, int reqWidth, int reqHeight)
    {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        // Calculate inSampleSize
        if(options.outWidth > options.outHeight)
        {
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);//if landscape
        }
        else
        {
            options.inSampleSize = calculateInSampleSize(options, reqHeight, reqWidth);//if portrait
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }

    public static Bitmap decodeSampledBitmapFromFileDescriptorV2(FileDescriptor fileDescriptor, int reqWidth, int reqHeight)
    {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        if(options.outWidth > options.outHeight)
        {
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            return Bitmap.createScaledBitmap(BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options), reqWidth, reqHeight, true);//if landscape
        }
        else
        {
            options.inSampleSize = calculateInSampleSize(options, reqHeight, reqWidth);
            options.inJustDecodeBounds = false;
            return Bitmap.createScaledBitmap(BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options), reqHeight, reqWidth, true);//if portrait
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth)
        {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth)
            {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static String getImagePath(Uri uri, ContentResolver contentResolver)
    {
        if (uri == null)
        {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        if (cursor != null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
    }

    public static String getImagePathForKitKat(Uri uri, ContentResolver contentResolver)
    {
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = contentResolver.query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public static Bitmap fixOrientation(String path, Bitmap bimap)
    {
        try
        {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();
            if (orientation == 6)
            {
                matrix.postRotate(90);
            }
            else if (orientation == 3)
            {
                matrix.postRotate(180);
            }
            else if (orientation == 8)
            {
                matrix.postRotate(270);
            }
            return Bitmap.createBitmap(bimap, 0, 0, bimap.getWidth(), bimap.getHeight(), matrix, true); // rotating bitmap
        }
        catch (Exception e)
        {
            Log.e("BitmapHelper", e.getLocalizedMessage(), e);
        }

        return null;
    }

    public static String encodeBitmap(Bitmap bitmap)
    {
        int quality = 100;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        System.gc();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] imageBytes = baos.toByteArray();

        while(imageBytes.length > (500 * 1024) && quality > 20)
        {
            quality = quality - 10;

            baos.reset();
            imageBytes = null;
            System.gc();

            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            imageBytes = baos.toByteArray();
        }

        try
        {
            bitmap.recycle();
            baos.close();
            baos = null;
            System.gc();
        }
        catch (Exception e)
        {
            Log.e("BitmapHelper", e.getLocalizedMessage(), e);
        }

        Log.i("BitmapHelper", "Image size after compression: " + imageBytes.length + " bytes");

        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
