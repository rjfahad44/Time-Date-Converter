package com.ft.time_converter.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ft.time_converter.BuildConfig;
import com.ft.time_converter.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ImageButton backImageIconBtn = findViewById(R.id.backImageIconBtn);

        //app Version Name//
        TextView appVersionName = findViewById(R.id.versionName);
        appVersionName.setText(BuildConfig.VERSION_NAME);

        backImageIconBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}