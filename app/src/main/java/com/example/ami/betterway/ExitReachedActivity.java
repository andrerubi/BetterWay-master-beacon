package com.example.ami.betterway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ExitReachedActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_reached);

    }

    @Override
    public void onBackPressed() {
        /*NOTHING TO DO: block back button*/

    }
}

