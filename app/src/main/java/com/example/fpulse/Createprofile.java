package com.example.fpulse;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class Createprofile extends AppCompatActivity {
    public EditText name;
    EditText serial;
    Button create;
    private Uri filepath;
    public static final int PICK_IMAGE_REQUSET = 71;
    private Home homeclass = new Home();
    ImageView upload;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createprofile);
        database = FirebaseDatabase.getInstance();
        //
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //
       @SuppressLint("WrongViewCast") final LinearLayout gallery = findViewById(R.id.ListView);
        final LayoutInflater inflater = LayoutInflater.from(this);
        upload = (ImageView) findViewById(R.id.uploadimg);
        create = (Button) findViewById(R.id.createbtn);
        name = (EditText) findViewById(R.id.name_edit);
        serial = (EditText) findViewById(R.id.serial_edit);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                View view = inflater.inflate(R.layout.item, gallery, false);
//                TextView textView = view.findViewById(R.id.nametext);
//                textView.setText(name.getText().toString());

                //ImageView img = view.findViewById(R.id.imgprof);
                //img.setImageResource(R.drawable.test);
                //gallery.addView(view);


                uploadimage();
                finish();
            }
        });


    }

    private void uploadimage() {

        if (filepath != null) {
            Toast.makeText(Createprofile.this, "Uploaded", Toast.LENGTH_LONG).show();


            final StorageReference ref = storageReference.child("image/" + name.getText().toString());
            ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("mytag","Upload success");
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for "YourFolderName/YourFile.pdf"
                            // Add it to your database
                            Log.d("mytag",uri.toString());
                            DatabaseReference myRef = database.getReference("Users");
                            myRef.child(name.getText().toString()).child("serial").setValue(serial.getText().toString());
                            myRef.child(name.getText().toString()).child("picUrl").setValue(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }

            });

        }
    }



    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUSET);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode,data);
        if (requestCode == PICK_IMAGE_REQUSET && resultCode == RESULT_OK && data != null && data.getData() != null) {
                filepath = data.getData();
                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                    upload.setImageBitmap(bitmap);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
        }
    }

}

