package com.example.saurabh.codingexercise;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by Saurabh on 8/14/2016.
 * Class displays details of an entry selected by user
 */

public class DescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.SELECTED_ENTRY)) {
            Entry entry = intent.getParcelableExtra(Constants.SELECTED_ENTRY);
            if (entry != null) {
                TextView descriptionView = (TextView) findViewById(R.id.desc_textView);
                descriptionView.setText(entry.getDescription());
                setTitle(entry.getTitle());
                NetworkImageView imageView = (NetworkImageView) findViewById(R.id.networkImageView);
                ImageLoader mImageLoader = VolleySingleton.getInstance(this).getImageLoader();
                imageView.setImageUrl(entry.getImageUrl(), mImageLoader);
            }
        }
    }
}
