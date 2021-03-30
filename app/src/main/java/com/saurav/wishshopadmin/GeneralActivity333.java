package com.saurav.wishshopadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class GeneralActivity333 extends AppCompatActivity {
    ImageView itemImage;
    Uri uri;
    EditText itemName,itemDescription,itemPrice;
    String imageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general333);

        itemImage=findViewById(R.id.itemImage);
        itemName= findViewById(R.id.itemName);
        itemDescription= findViewById(R.id.itemDescription);
        itemPrice= findViewById(R.id.itemPrice);

    }

    public void btnSelectImage(View view) {
        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            uri = data.getData();
            itemImage.setImageURI(uri);
        }
        else {
            Toast.makeText(this, "please Select an Image", Toast.LENGTH_SHORT).show();
        }
    }
    public void uploadImage(){
        StorageReference storageReference= FirebaseStorage.getInstance()
                .getReference().child("GeneralImage").child(uri.getLastPathSegment());

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Item Uploading");
        progressDialog.show();


        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage=uriTask.getResult();
                imageUrl=urlImage.toString();
                uploadItemData();
                progressDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(GeneralActivity333.this,e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void btnUpload(View view) {
        uploadImage();
    }
    public void uploadItemData(){

        Main2Modal main2Modal = new Main2Modal(itemName.getText().toString(),
                itemDescription.getText().toString(),
                itemPrice.getText().toString(),imageUrl);
        String myCurrentDateTime= DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());
        FirebaseDatabase.getInstance().getReference("General_Name")
                .child(myCurrentDateTime).setValue(main2Modal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(GeneralActivity333.this, "Item Uploaded", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GeneralActivity333.this,e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}