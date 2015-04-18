package com.acipi.evote;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.acipi.evote.utils.VolleySingleton;
import com.android.volley.toolbox.NetworkImageView;

public class FullSceenImageActivity extends Activity
{
    private String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image);

        if (getIntent().hasExtra("imageURL"))
        {
            this.imageURL = getIntent().getExtras().getString("imageURL");
        }

        NetworkImageView full_image = (NetworkImageView) findViewById(R.id.full_image);
        full_image.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));

        if (imageURL != null)
        {
            full_image.setImageUrl(imageURL, VolleySingleton.getInstance(this).getImageLoader());
        }

        full_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }
}
