package com.example.coachroebuck.userexperienceexerciseandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final InfoView infoView = (InfoView)findViewById(R.id.infoView);
        infoView.bind(3, 3);
//        infoView.invalidate();
    }
}
