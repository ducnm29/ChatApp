package com.minhduc.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText edtUserName,edtPassword,edtConfirmPass,edtEmail;
    Button btnCreate;
    TextView txtCancel;
    FirebaseAuth auth;
    DatabaseReference myref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        anhXa();
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,StartActivity.class));
                finish();
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String userName = edtUserName.getText().toString();
                String password = edtPassword.getText().toString();
                String confirmPass = edtConfirmPass.getText().toString();
                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(userName)||TextUtils.isEmpty(password)||TextUtils.isEmpty(confirmPass)){
                    Toast.makeText(RegisterActivity.this,"All fields are required!",Toast.LENGTH_SHORT).show();
                }else if(!password.equals(confirmPass)){
                    Toast.makeText(RegisterActivity.this,"Password not matched!",Toast.LENGTH_SHORT).show();
                }else{
                    register(email,userName,password);
                }
            }
        });
    }
    private void register(String email,String userName,String password){
        auth.createUserWithEmailAndPassword(email,password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser dtUser = auth.getCurrentUser();
                    assert dtUser != null;
                    String userID = dtUser.getUid();
                    myref = FirebaseDatabase.getInstance().getReference("Users").child(userID);
                    HashMap<String , String> hashMap = new HashMap<>();
                    hashMap.put("id",userID);
                    hashMap.put("username",userName);
                    hashMap.put("imageurl","default");
                    hashMap.put("password",password);
                    hashMap.put("email",email);
                    hashMap.put("status","offline");
                    myref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Done!",Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(RegisterActivity.this,StartActivity.class));
                                finish();
                            }else {
                                Toast.makeText(RegisterActivity.this,"Try again!1",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this,"Try again!2",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void anhXa(){
        auth = FirebaseAuth.getInstance();
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        edtUserName = (EditText)findViewById(R.id.edtUserName);
        btnCreate    = (Button)findViewById(R.id.btn_create);
        edtConfirmPass = (EditText)findViewById(R.id.edtPasswordConfirm);
        txtCancel = (TextView)findViewById(R.id.txtCancel);
    }
}