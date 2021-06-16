package my.musicapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import my.musicapp.Model.Constraints;
import my.musicapp.Model.Upload;

public class UploadAlbumActivity extends AppCompatActivity implements AdapterView.OnClickListener{

    private Button buttonchoose;
    private Button buttonupload;
    private EditText editTextname;
    private ImageView imageView;
    String songCategory;
    private static final int Pick_Img_req = 2;

    private Uri fillpath;
    StorageReference storageReference;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_album);

        buttonchoose = findViewById(R.id.choosebutton);
        buttonupload = findViewById(R.id.buttonupload);
        editTextname = findViewById(R.id.edit_text);
        imageView = findViewById(R.id.imageview2);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constraints.DATABASE_PATH_UPLOADS);

        Spinner spinner = findViewById(R.id.spinner2);

        buttonchoose.setOnClickListener(this);
        buttonupload.setOnClickListener(this);

        List<String> categories = new ArrayList<>();

        categories.add("Love songs");
        categories.add("Sad songs");
        categories.add("Party songs");
        categories.add("Birthday songs");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                songCategory = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(UploadAlbumActivity.this,"Selcted:"+songCategory,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        if(v == buttonchoose){
            showFilechoose();
        }
        else if(v == buttonupload){
            uploadFiles();
        }
    }

    private void uploadFiles() {

        if(fillpath!= null){
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final  StorageReference sRef = storageReference.child(Constraints.STORAGR_PATH_UPLOADS + System.currentTimeMillis()+"." + getfileextention(fillpath) );

            sRef.putFile(fillpath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Upload upload = new Upload(editTextname.getText().toString().trim(),url,songCategory);
                            String uploadId = mDatabase.push().getKey();
                            mDatabase.child(uploadId).setValue(upload);
                            progressDialog.dismiss();
                            Toast.makeText(UploadAlbumActivity.this,"file uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();

                    Toast.makeText(UploadAlbumActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 + snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded" + ((int)progress)+"%....");
                }
            });
        }else{

            Toast.makeText(UploadAlbumActivity.this,"Select album image for songs", Toast.LENGTH_SHORT).show();
        }
    }

    private void showFilechoose() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Pick_Img_req && resultCode == RESULT_OK && data != null && data.getData() != null){
            fillpath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),fillpath);
//                    imageView.setImageURI(fillpath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  String getfileextention(Uri uri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getMimeTypeFromExtension(cr.getType(uri));
    }
}
