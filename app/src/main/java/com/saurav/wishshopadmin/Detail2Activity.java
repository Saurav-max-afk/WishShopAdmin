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

public class Detail2Activity extends AppCompatActivity {
    TextView dataDescription;
    ImageView dataImage;
    String key="";
    String imageUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);

        dataDescription=findViewById(R.id.txtDescription);
        dataImage=findViewById(R.id.ivImage2);

        Bundle mBundle= getIntent().getExtras();
        if (mBundle!=null)
        {
            dataDescription.setText(mBundle.getString("Description"));
            key=mBundle.getString("keyValue");
            imageUrl=mBundle.getString("Image");
         //   dataImage.setImageResource(mBundle.getInt("Image"));

            Glide.with(this)
                    .load(mBundle.getString("Image"))
                    .into(dataImage);
        }

    }

    public void btnDeleteReciepe(View view) {
        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Item_Name");
        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
        StorageReference storageReference=firebaseStorage.getReferenceFromUrl(imageUrl);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child(key).removeValue();
                Toast.makeText(Detail2Activity.this, "Recipie Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity2.class));
                finish();
            }
        });

    }
}