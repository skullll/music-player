package my.musicapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import my.musicapp.Model.UploadSong;

public class UploadSongsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView textviewImage;
    ProgressBar progressBar;
    Uri audiouri;
    StorageReference mStorageRef;
    StorageTask mUploadTask;
    DatabaseReference referenceSongs;
    String songCatagory;
    MediaMetadataRetriever metadataRetriever ;
    byte [] art = null;
    String title1 ,artist1,album_art1 = "",duration1;
    EditText title, Artist, duration,datas, album;
    ImageView album_art;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_songs);

        textviewImage = findViewById(R.id.textviewsongfileselect);
        progressBar = findViewById(R.id.Progressbar);
        title = findViewById(R.id.title);
        Artist = findViewById(R.id.artist);
        //duration = findViewById(R.id.duration);
        datas = findViewById(R.id.dataa);
        album_art = findViewById(R.id.imageview);

        metadataRetriever = new MediaMetadataRetriever();
        referenceSongs = FirebaseDatabase.getInstance().getReference().child("songs");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("songs");




        Spinner spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<>();

        categories.add("Love songs");
        categories.add("Sad songs");
        categories.add("Party songs");
        categories.add("Birthday songs");

        ArrayAdapter <String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);



    }


    @Override
    public void onItemSelected(AdapterView<?>  adapterView, View view, int i, long l) {
        songCatagory = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, "Selected: "+songCatagory, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void openAudioFiles(View v){
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("audio/*");
        startActivityForResult(i,101);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101 && resultCode == RESULT_OK && data.getData() != null){

            audiouri = data.getData();
            String fileName = getFileName(audiouri);
            textviewImage.setText(fileName);

//            try {
                metadataRetriever.setDataSource(this,audiouri);
//            }catch (NullPointerException ignored){
//
//            }
//            try {
//                art = metadataRetriever.getEmbeddedPicture();
//            }catch (NullPointerException ignored){
//
//            }
//
//            Bitmap bitmap= BitmapFactory.decodeByteArray(art,0,art.length);
//            album_art.setImageBitmap(bitmap);

//            album.setText("");
//            artist.setText("");
//            datas.setText("");
//            duration.setText("");
//            title.setText("");

//            artist1 = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
//            title1 = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
//            duration1 = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);


        }
    }

    private String getFileName(Uri uri){

        String result = null;
        if(uri.getScheme().equals("content")){

            Cursor cursor = getContentResolver().query(uri,null,null,null,null);
            try {

                if(cursor != null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
            finally {
                cursor.close();
            }

        }

        if(result == null){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if(cut != -1){
                result = result.substring(cut+1);
            }
        }
        return result;
    }

    public void uploadfiletofirebase(View v){

        if(textviewImage.equals("No file Selected")){
            Toast.makeText(this,"Please, select an Image !",Toast.LENGTH_SHORT).show();
        }else {
            if(mUploadTask !=null && mUploadTask.isInProgress()){

                Toast.makeText(this,"Songs upload is already in progess !",Toast.LENGTH_SHORT).show();
            }else {
                uploadFiles();
            }
        }
    }

    private void uploadFiles() {

        if (title == null) {
            Toast.makeText(this,"enter song title.... ",Toast.LENGTH_SHORT).show();

        }
        if (Artist == null) {
            Toast.makeText(this,"enter song artist.... ",Toast.LENGTH_SHORT).show();
        }

        if(audiouri != null){
            Toast.makeText(this,"uploads please wait",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
            final  StorageReference storageReference = mStorageRef.child(System.currentTimeMillis()+"."+getFileextention(audiouri));
            mUploadTask = storageReference.putFile(audiouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String songTitle = title.getText().toString();
                            String artist = Artist.getText().toString();

                            UploadSong uploadSong = new UploadSong(songTitle,songCatagory,artist,uri.toString());
                            String uploadId = referenceSongs.push().getKey();
                            referenceSongs.child(uploadId).setValue(uploadSong);
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    double progress = (100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    progressBar.setProgress((int)progress);

                    if(progress == 100.0){
                        Toast.makeText(UploadSongsActivity.this,"task completred",Toast.LENGTH_SHORT).show();

                        Intent ReqIntent = new Intent(UploadSongsActivity.this,Music_select.class);
                        startActivity(ReqIntent);
                        finish();

                    }

                }
            });
        }else {
            Toast.makeText(this,"No file selected to uploads",Toast.LENGTH_SHORT).show();
        }


    }

    private String getFileextention(Uri audiouri){

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(audiouri));
    }

    public void Uploadalbumact(View view){

        Intent intent = new Intent(UploadSongsActivity.this,UploadAlbumActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(UploadSongsActivity.this,Music_select.class);
        startActivity(intent);
    }
}



