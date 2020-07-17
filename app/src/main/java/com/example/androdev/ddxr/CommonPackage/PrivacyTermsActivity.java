package com.example.androdev.ddxr.CommonPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.androdev.ddxr.R;

public class PrivacyTermsActivity extends AppCompatActivity {

    String type;
    NestedScrollView privacy;
    NestedScrollView terms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_privacy);

        privacy=findViewById(R.id.privacy_layout);
        terms=findViewById(R.id.terms_layout);


        Intent i = getIntent();
        type = i.getStringExtra("Type");

        if(type.equals("Privacy")){
            privacy.setVisibility(View.VISIBLE);
            terms.setVisibility(View.GONE);
        }

        if(type.equals("Terms")){
            terms.setVisibility(View.VISIBLE);
            privacy.setVisibility(View.GONE);
        }



    }


}

