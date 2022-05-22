package com.minhduc.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class AddNewfeedActivity extends AppCompatActivity {
    private ImageView imgProfile,imgIconback,imgIconAddImage,imgImageNewFeed;
    private Button btnAddNewFeed;
    private TextView txtUserName;
    private EditText edtCaption;
    private final int PICK_IMAGE=11;
    private Uri filePath;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_newfeed);
        initView();
        //set information
        readInfo();
        //pick image from gallery
        imgIconAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        //Click button Đăng
        btnAddNewFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtCaption.getText().toString().trim().length()==0){
                    Toast.makeText(getApplicationContext(),"Thêm caption đi bạn :v",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    uploadImage();
                }
            }
        });

        //click icon back

        imgIconback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void readInfo() {
        txtUserName.setText(MainActivity.user.getUsername());
    }


    private void uploadImage() {
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference = firebaseDatabase.getReference();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đang đăng bài...");
        progressDialog.show(); // create a progressdialog showing when put data to database

        //upload newfeed to db

        DatabaseReference newFeedRef = databaseReference.child("Newfeed");
        String imgName = newFeedRef.push().getKey();
        String caption = edtCaption.getText().toString().trim();
        String linkImg="";
        if(filePath!=null){
            linkImg = imgName;
        }
        String time =new SimpleDateFormat("hh:mm:ss-dd/MM/yyyy").format(new Date());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("user",MainActivity.user);
        hashMap.put("caption",caption);
        hashMap.put("imgLink",linkImg);
        hashMap.put("time",time);
        newFeedRef.child(imgName).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(filePath==null){
                    finish();
                }
            }
        });

        //upload image
        if(filePath != null){   //if an image was picked
            StorageReference imgRef = storageReference.child("images/newfeed/"+imgName);
            imgRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.w("upload_img_result","Success");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("upload_img_result","Fail");
                    progressDialog.setMessage("Update fail!");
                    progressDialog.dismiss();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Đăng bài thành công!",Toast.LENGTH_LONG).show();
                    filePath = null;
                    finish();
                }
            });
        }
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Chọn hình ảnh"),PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE&& resultCode == RESULT_OK&& data!=null){
            //get the uri of the image
            filePath = data.getData();
        }
        try {
            //convert to bitmap
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
            imgImageNewFeed.setImageBitmap(bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initView() {
        imgProfile = (ImageView) findViewById(R.id.profile_image_add_newfeed);
        imgIconback = (ImageView)findViewById(R.id.img_back_add_newfeed);
        imgIconAddImage = (ImageView)findViewById(R.id.icon_add_image_add_newfeed);
        imgImageNewFeed = (ImageView)findViewById(R.id.img_add_image);
        btnAddNewFeed = (Button) findViewById(R.id.btn_add_newfeed);
        txtUserName = (TextView) findViewById(R.id.txt_username_add_newfeed);
        edtCaption = (EditText) findViewById(R.id.edt_caption_add_newfeed);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }
    private void setStatus(String status) {               // set status for current user
        DatabaseReference sttRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        sttRef.child("status").setValue(status);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setStatus("offline");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatus("online");
    }
}