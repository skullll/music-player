package my.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AudioPermission extends AppCompatActivity {

    private int Micro_Phone_Permission = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_permission);

        final TextView ReqPermiss = findViewById(R.id.Req_Permission);

        if(ContextCompat.checkSelfPermission(AudioPermission.this,
                Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED) {

            Intent homeIntent = new Intent(AudioPermission.this, Music_select.class);
            startActivity(homeIntent);
            finish();
        }

        ReqPermiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(AudioPermission.this,
                        Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(AudioPermission.this,"this permission is already granted...",Toast.LENGTH_SHORT).show();
                }else {
                    ReqPermission();
                }
            }
        });

    }

    private void ReqPermission() {

        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECORD_AUDIO)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("this Permission Needed for record audio")
                    .setPositiveButton("ok,", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AudioPermission.this,new String[]{Manifest.permission.RECORD_AUDIO},Micro_Phone_Permission);
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},Micro_Phone_Permission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Micro_Phone_Permission){
            if(grantResults.length> 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();

                if(ContextCompat.checkSelfPermission(AudioPermission.this,
                        Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED) {

                    Intent homeIntent = new Intent(AudioPermission.this, Music_select.class);
                    startActivity(homeIntent);
                    finish();
                }

            }else {

                Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
            }
        }

    }

}
