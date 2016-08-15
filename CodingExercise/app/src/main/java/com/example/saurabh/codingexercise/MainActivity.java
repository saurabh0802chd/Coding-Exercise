package com.example.saurabh.codingexercise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Saurabh on 8/14/2016.
 * Class is a main screen displayed to the user
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
