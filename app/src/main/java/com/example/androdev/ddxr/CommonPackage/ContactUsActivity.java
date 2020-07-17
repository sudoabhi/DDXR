package com.example.androdev.ddxr.CommonPackage;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.androdev.ddxr.R;

public class ContactUsActivity extends AppCompatActivity {


    ImageView back;
    LinearLayout abhishek;
    LinearLayout ashutosh;
    Button fb1;
    Button insta1;
    Button fb2;
    Button insta2;
    LinearLayout abhi_lay;
    LinearLayout ashu_lay;

    public static String FACEBOOK_URL = "https://www.facebook.com/sudo.abHi";
    public static String FACEBOOK_PAGE_ID = "sudo.abHi";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        back=findViewById(R.id.back);
        abhishek=findViewById(R.id.abhishek);
        ashutosh=findViewById(R.id.ashutosh);
        fb1=findViewById(R.id.fb);
        fb2=findViewById(R.id.fb2);
        insta1=findViewById(R.id.insta);
        insta2=findViewById(R.id.insta2);
        abhi_lay=findViewById(R.id.abhi_lay);
        ashu_lay=findViewById(R.id.ashu_lay);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url;
                PackageManager packageManager = getApplicationContext().getPackageManager();
                try {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850) { //newer versions of fb app
                        url="fb://facewebmodal/f?href=" + "https://www.facebook.com/sudo.abHi";
                    } else { //older versions of fb app
                        url="fb://page/" + "sudo.abHi";
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    url="https://www.facebook.com/sudo.abHi"; //normal web url
                }

                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                facebookIntent.setData(Uri.parse(url));
                startActivity(facebookIntent);
            }
        });
        fb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url;
                PackageManager packageManager = getApplicationContext().getPackageManager();
                try {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850) { //newer versions of fb app
                        url="fb://facewebmodal/f?href=" + "https://www.facebook.com/Ashu99999";
                    } else { //older versions of fb app
                        url="fb://page/" + "Ashu99999";
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    url="https://www.facebook.com/Ashu99999"; //normal web url
                }

                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                facebookIntent.setData(Uri.parse(url));
                startActivity(facebookIntent);
            }
        });
        insta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/sudo_abhi");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/sudo_abhi")));
                }
            }
        });
        insta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/ashutosh.m7");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/ashutosh.m7")));
                }
            }
        });





        abhishek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Uri uri = Uri.parse("https://www.facebook.com/sudo.abHi");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);*/
                if(abhi_lay.getVisibility()==View.VISIBLE){
                    abhi_lay.setVisibility(View.GONE);
                }else {
                    abhi_lay.setVisibility(View.VISIBLE);
                }


            }
        });


        ashutosh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Uri uri = Uri.parse("https://www.facebook.com/Ashu99999");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);*/

                if(ashu_lay.getVisibility()==View.VISIBLE){
                    ashu_lay.setVisibility(View.GONE);
                }else {
                    ashu_lay.setVisibility(View.VISIBLE);
                }



            }
        });









    }


}

