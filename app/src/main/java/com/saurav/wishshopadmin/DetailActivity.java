package com.saurav.wishshopadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {
    TextView dataDescription;
    TextView dataName;
    TextView dataPrice;
    ImageView dataImage;
    String key="";
    String imageUrl="";
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dataDescription=findViewById(R.id.txtDescription);
        dataImage=findViewById(R.id.ivImage2);
        dataName=findViewById(R.id.txtName);
        dataPrice=findViewById(R.id.txtPrice);

        Bundle mBundle= getIntent().getExtras();
        if (mBundle!=null)
        {
            dataDescription.setText(mBundle.getString("Description"));
            dataName.setText(mBundle.getString("Name"));
            dataPrice.setText(mBundle.getString("Price"));
            key=mBundle.getString("keyValue");
            imageUrl=mBundle.getString("Image");
            //   dataImage.setImageResource(mBundle.getInt("Image"));

            Glide.with(this)
                    .load(mBundle.getString("Image"))
                    .into(dataImage);
        }

    }



    public void btnDeleteReciepe1(View view) {
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Item_Name").child(key).removeValue();
        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
        StorageReference storageReference=firebaseStorage.getReferenceFromUrl(imageUrl);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child(key).removeValue();
                Toast.makeText(DetailActivity.this, "Recipie Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity2.class));
                finish();
            }
        });



    }

    public void btnUpdate(View view) {
        startActivity(new Intent(getApplicationContext(),UpdateActivity.class)
        .putExtra("NameKey",dataName.getText().toString())
                .putExtra("DescriptionKey",dataDescription.getText().toString())
                .putExtra("price",dataPrice.getText().toString())
                .putExtra("oldImageUrl",imageUrl)
                .putExtra("Oldkey",key)
        );

    }

}