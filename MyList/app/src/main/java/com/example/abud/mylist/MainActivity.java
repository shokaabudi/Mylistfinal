package com.example.abud.mylist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class MainActivity extends Activity {
    Button manual;
    Button Auto;
    Button Saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        manual = (Button)findViewById(R.id.btn_manual);
        Auto = (Button)findViewById(R.id.btn_automatic);
        Saved = (Button)findViewById(R.id.btn_saved);
        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startManual = new Intent();
                startManual.setClass(MainActivity.this,Manual.class);
                startActivity(startManual);
            }
        });

        Saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startSaved = new Intent ();
                startSaved.setClass(MainActivity.this, Saved.class);
                startActivity(startSaved);
            }
        });

        Auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startAuto = new Intent ();
                startAuto.setClass(MainActivity.this, Auto.class);
                startActivity(startAuto);

            }
        });
    }
    public void onBackPressed()
    {  super.onBackPressed();
        finish();

    }
}
