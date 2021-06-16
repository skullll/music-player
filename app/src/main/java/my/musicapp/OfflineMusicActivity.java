package my.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class OfflineMusicActivity extends AppCompatActivity {

    private String[] itemsAll;
    private ListView msongList;
    MediaPlayer Mymediaplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_music);

        msongList = findViewById(R.id.List_View);

        external_storage_Permiss();
    }

    public void external_storage_Permiss()
    {
        Dexter.withContext(this)  .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)  .withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */
                displayName();
            }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token)
            {/* ... */
            token.continuePermissionRequest();}
        }).check();
    }

    public ArrayList<File> readonlyaudiofile(File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] allfiles = file.listFiles();

        for(File individualFile : allfiles)
        {
            if(individualFile.isDirectory() && !individualFile.isHidden())
            {
                arrayList.addAll(readonlyaudiofile(individualFile));
            }else
            {
                if(individualFile.getName().endsWith(".mp3") || individualFile.getName().endsWith(".aac") || individualFile.getName().endsWith(".wav")){
                    arrayList.add(individualFile);
                }
            }
        }


       return arrayList;
    }

    private void displayName()
    {
        final ArrayList<File> audioSong = readonlyaudiofile(Environment.getExternalStorageDirectory());

        itemsAll = new String[audioSong.size()];

        for (int songCounter = 0; songCounter<audioSong.size(); songCounter++)
        {
            itemsAll[songCounter]= audioSong.get(songCounter).getName();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(OfflineMusicActivity.this,android.R.layout.simple_list_item_1,itemsAll);
        msongList.setAdapter(arrayAdapter);

        msongList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String songnme = msongList.getItemAtPosition(position).toString();
                Intent intent = new Intent(OfflineMusicActivity.this, OfflinePlayerActivity.class);
                intent.putExtra("song",audioSong);
                intent.putExtra("name",songnme);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(OfflineMusicActivity.this,Music_select.class);
        startActivity(intent);
    }

}
