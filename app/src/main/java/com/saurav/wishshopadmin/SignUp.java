package com.saurav.wishshopadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private EditText txtEmail,txtPassword,txtConfirmPassword;
    private Button btn_register;
    ProgressBar progressBar;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtEmail=findViewById(R.id.txtEmail);
        txtPassword=findViewById(R.id.txtPassword);
        txtConfirmPassword=findViewById(R.id.confirm_Password);
        btn_register=findViewById(R.id.btn_Register);
        progressBar=findViewById(R.id.progressBar);
        auth=FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("SignUp Form");


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=txtEmail.getText().toString().trim();
                String password=txtPassword.getText().toString().trim();
                String confirmPassword=txtConfirmPassword.getText().toString().trim();


                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(SignUp.this, "Please Fill All The Fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (password.equals(confirmPassword)) {
                        progressBar.setVisibility(View.VISIBLE);

                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                           startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        } else {
                                            Toast.makeText(SignUp.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else {
                        Toast.makeText(SignUp.this, "Plzz Verify Password", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
}