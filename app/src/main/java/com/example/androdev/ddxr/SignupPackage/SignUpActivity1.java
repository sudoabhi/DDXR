package com.example.androdev.ddxr.SignupPackage;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.androdev.ddxr.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SignUpActivity1 extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference dbRef;
    FirebaseStorage storage;
    StorageReference storage_reference;
    Bitmap original;
    Uri profile_image_url;
    String downloadUriS;



    FrameLayout content;
    ProgressBar progressBar;
    ProgressBar LayoutprogressBar;

    Button next_viewpager;


    ImageView profile_image;
    FirebaseUser user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment1);

        progressBar=(ProgressBar) findViewById(R.id.top_progress_bar);
        LayoutprogressBar=(ProgressBar) findViewById(R.id.progressBar);
        LayoutprogressBar.setVisibility(View.INVISIBLE);
        content=findViewById(R.id.content);

        user= FirebaseAuth.getInstance().getCurrentUser();
        database=FirebaseDatabase.getInstance();
        dbRef=database.getReference();
        storage=FirebaseStorage.getInstance();
        storage_reference = storage.getReference();
        dbRef.child(user.getUid()).child("Registered").setValue("False");
        next_viewpager=(Button)findViewById(R.id.next_viewpager);
        next_viewpager.setVisibility(View.INVISIBLE);

        next_viewpager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(downloadUriS)){
                    next_viewpager.setClickable(true);
                    LayoutprogressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignUpActivity1.this, "Please upload a profile photo", Toast.LENGTH_SHORT).show();
                }
                else {
                    next_viewpager.setClickable(false);
                    LayoutprogressBar.setVisibility(View.VISIBLE);
                    content.setForeground(new ColorDrawable(Color.parseColor("#60000000")));
                    sendData();

                }

            }
        });




        profile_image = (ImageView) findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                upload_image();

            }
        });


    }


    public void upload_image() {

        Intent intent=new Intent(SignUpActivity1.this, PhotoSelector.class);
        intent.putExtra("Activity","SignUpActivity");
        startActivityForResult(intent,1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String resultUriS = data.getStringExtra("SignUpProfileStringUri");
                Uri resultUri=Uri.parse(resultUriS);

                if(resultUriS!=null){
                    profile_image.setImageURI(resultUri);

                    new SignUpActivity1.CompressionAsyncTask(resultUri,resultUriS).execute();
                }
            }
        }
    }





    /*public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        if (ContextCompat.checkSelfPermission(SignUpActivity1.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {



            if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity1.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Toast.makeText(SignUpActivity1.this, "Please allow us storage permissions.", Toast.LENGTH_SHORT).show();

            } else {

                ActivityCompat.requestPermissions(SignUpActivity1.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
        else{
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }
        return null;


    }*/

    public void uploadimageinbytes(Bitmap bitmap,int quality,Uri uri){
        final StorageReference mountainsRef = storage_reference.child(user.getUid()).child("profile_images");
        // final StorageReference mountainsRef = storage_reference.child(user.getUid()).child("profile_images/"+uri.getLastPathSegment());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(SignUpActivity1.this,"Failed",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SignUpActivity1.this,"Successful",Toast.LENGTH_SHORT).show();


            }
        });


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }


                return mountainsRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    downloadUriS=downloadUri.toString();
                    profile_image_url=downloadUri;
                    next_viewpager.setVisibility(View.VISIBLE);
                    //dbRef.child(user.getUid()).child("ImageUrl").setValue(downloadUriS);
                    //Toast.makeText(getActivity(),""+downloadUriS,Toast.LENGTH_SHORT).show();

                } else {

                }
            }
        });


    }


    private class CompressionAsyncTask extends AsyncTask<Uri,Integer,Void> {

        Uri selectedImage;
        String selectedImageS;

        public CompressionAsyncTask(Uri selectedImage,String selectedImageS){

            this.selectedImage=selectedImage;
            this.selectedImageS=selectedImageS;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }




        @Override
        protected Void doInBackground(Uri... params) {
            try{
                original = MediaStore.Images.Media.getBitmap(SignUpActivity1.this.getContentResolver(),selectedImage);
            }
            catch(IOException e)
            {
            }
            uploadimageinbytes(original,40,selectedImage);


            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            /*Picasso.get()
                    .load(Uri.parse(selectedImageS)) // mCategory.icon is a string
                    .resize(800, 800)
                    .centerCrop()
                    .noFade()
                    .error(R.drawable.addphoto) // default image to load
                    .into(profile_image);*/

            }
    }

    private void sendData()
    {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        String UserEmail=user.getEmail();


        dbRef.child(user.getUid()).child("JoiningDate").setValue(formattedDate);
        dbRef.child(user.getUid()).child("UserEmail").setValue(UserEmail);
        dbRef.child(user.getUid()).child("ImageUrl").setValue(downloadUriS)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        next_viewpager.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        startActivity(new Intent(SignUpActivity1.this,SignUpActivity2.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        next_viewpager.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        Toast.makeText(SignUpActivity1.this, "An error occurred, with error message:  "+e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

    }







}
