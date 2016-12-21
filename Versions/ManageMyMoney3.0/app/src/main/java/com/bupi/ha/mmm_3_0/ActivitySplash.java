package com.bupi.ha.mmm_3_0;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by user on 12/20/2016.
 * Hello
 */

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, ActivityMain.class);
        startActivity(intent);
        finish();
    }
}
