package com.example.androdev.ddxr.CommonPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.androdev.ddxr.R;

public class AboutActivity extends AppCompatActivity {


    Button button;
    LinearLayout layout1;
    LinearLayout layout2;
    TextView tv1;
    TextView tv2;
    Toolbar toolbar;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        layout1=findViewById(R.id.layout1);
        layout2=findViewById(R.id.layout2);
        tv1=findViewById(R.id.tnc);
        tv2=findViewById(R.id.privacy);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(AboutActivity.this, PrivacyTermsActivity.class);
                i.putExtra("Type","Terms");
                startActivity(i);
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AboutActivity.this, PrivacyTermsActivity.class);
                i.putExtra("Type","Privacy");
                startActivity(i);
            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(AboutActivity.this, PrivacyTermsActivity.class);
                i.putExtra("Type","Terms");
                startActivity(i);
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(AboutActivity.this, PrivacyTermsActivity.class);
                i.putExtra("Type","Privacy");
                startActivity(i);
            }
        });








    }


}

