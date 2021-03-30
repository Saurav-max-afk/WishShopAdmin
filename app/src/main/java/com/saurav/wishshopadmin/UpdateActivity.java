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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {
    ImageView itemImage;
     Uri uri;
    EditText itemName1,itemDescription1,itemPrice;
    String imageUrl;
    String key,oldImageUrl;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String itemName,itemDescription,dataPrice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        itemImage=findViewById(R.id.itemImage);
        itemName1=  findViewById(R.id.itemName);
        itemDescription1= findViewById(R.id.itemDescription);
        itemPrice= findViewById(R.id.itemPrice);

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            Glide.with(UpdateActivity.this)
                    .load(bundle.getString("oldImageUrl"))
                    .into(itemImage);
            itemName1.setText(bundle.getString("NameKey"));
            itemDescription1.setText(bundle.getString("DescriptionKey"));
            itemPrice.setText(bundle.getString("price"));
            key=bundle.getString("Oldkey");
            oldImageUrl=bundle.getString("oldImageUrl");

        }
        databaseReference= FirebaseDatabase.getInstance().getReference("Item_Name").child(key);


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

    public void btnUpdate(View view)
    {
         itemName = itemName1.getText().toString().trim();
         itemDescription=itemDescription1.getText().toString().trim();
         dataPrice=itemPrice.getText().toString();

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Item Uploading");
        progressDialog.show();

        storageReference= FirebaseStorage.getInstance()
                .getReference().child("ItemImage").child(uri.getLastPathSegment());

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
                Toast.makeText(UpdateActivity.this,e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void uploadItemData(){
        Main2Modal main2Modal = new Main2Modal(itemName,itemDescription,dataPrice,imageUrl);

databaseReference.setValue(main2Modal).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        StorageReference storageReferencenew=FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
        storageReferencenew.delete();
        Toast.makeText(UpdateActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
    }
});
    }
}