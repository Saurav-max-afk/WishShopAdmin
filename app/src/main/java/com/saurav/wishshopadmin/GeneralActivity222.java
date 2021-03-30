package com.saurav.wishshopadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GeneralActivity222 extends AppCompatActivity {
    RecyclerView mRecyclerView;
    List<Main2Modal> main2ModalList;
    Main2Modal main2Modal;

    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    ProgressDialog progressDialog;
    EditText txt_Search3;
    MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        mRecyclerView = findViewById(R.id.recyclerView);
        txt_Search3=findViewById(R.id.txt_SearchText2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(GeneralActivity222.this,1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading Items...");

        main2ModalList = new ArrayList<>();



         myAdapter = new MyAdapter(GeneralActivity222.this,main2ModalList);
        mRecyclerView.setAdapter(myAdapter);

        databaseReference= FirebaseDatabase.getInstance().getReference("General_Name");
        progressDialog.show();
        eventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                main2ModalList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    main2Modal=itemSnapshot.getValue(Main2Modal.class);
                    main2ModalList.add(main2Modal);
                }
                myAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });

        txt_Search3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });

    }

    private void filter(String text) {
        ArrayList<Main2Modal> filterList = new ArrayList<>();
        for (Main2Modal item: main2ModalList){

            if (item.getItemName().toLowerCase().contains(text.toLowerCase())){

                filterList.add(item);
            }

        }
        myAdapter.filteredList(filterList);
    }

    public void floating_upload(View view) {
        startActivity(new Intent(getApplicationContext(),GeneralActivity333.class));
    }
}