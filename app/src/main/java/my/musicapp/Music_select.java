package my.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Music_select extends AppCompatActivity {

    Button onlinebut, offlinebut, uploadsongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_select);

        onlinebut = findViewById(R.id.onlinemusic);
        offlinebut = findViewById(R.id.offlinemusic);
        uploadsongs = findViewById(R.id.uploadmusic);


        offlinebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ReqIntent = new Intent(Music_select.this, OfflineMusicActivity.class);
                startActivity(ReqIntent);
                finish();
            }
        });

        onlinebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ReqIntent = new Intent(Music_select.this, OnlineMusicAlbum.class);
                startActivity(ReqIntent);
                finish();
            }
        });

        uploadsongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ReqIntent = new Intent(Music_select.this,UploadSongsActivity.class);
                startActivity(ReqIntent);
                finish();
            }
        });
    }

    int  counter = 0;
    @Override
    public void onBackPressed() {
        counter ++;
        if (counter==1){
            Toast.makeText(Music_select.this,"Tap again to exit",Toast.LENGTH_SHORT).show();
        }
        if (counter==2)
        super.onBackPressed();

    }
}
