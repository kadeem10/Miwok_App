package com.example.android.miwok;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ColorsActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        //Replace container in activity_categories with fragment NumbersFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ColorsFragment()).commit();
    }
}
