package my.musicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class OfflinePlayerActivity extends AppCompatActivity {

    private RelativeLayout parentRelativeLayout;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecogIntent;
    private  String keeper ="";

    private ImageView playPausebtn , prevbtn, nextbtn;
    private TextView songNametxt;

    private ImageView imageView;
    private RelativeLayout lowerRelativelayout;
    private Button Voicebtn;
    private  String Mode = "OFF";

    private MediaPlayer MymediaPlayer;
    private int position;
    private ArrayList<File> mySongs;
    private  String mSongname ;

    SeekBar Positionbar;
    Thread updatebar;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_player);

        checkVoicecmdPermission();

        playPausebtn = findViewById(R.id.playpause_Button);
        prevbtn = findViewById(R.id.prev_Button);
        nextbtn = findViewById(R.id.next_Button);
        songNametxt = findViewById(R.id.Song_name);
        imageView = findViewById(R.id.logo);
        lowerRelativelayout = findViewById(R.id.lower);
        Voicebtn = findViewById(R.id.VoiceEnable_btn);
        Positionbar = findViewById(R.id.seekbar);
        parentRelativeLayout = findViewById(R.id.parentaaRelatedLayout);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(OfflinePlayerActivity.this);
        speechRecogIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecogIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecogIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        updatebar = new Thread(){

            @Override
            public void run() {
                super.run();

                int totaldur = MymediaPlayer.getDuration();
                int cureentpos =0 ;

                while (cureentpos<totaldur){

                    try{
                        sleep(1000);
                        cureentpos = MymediaPlayer.getCurrentPosition();
                        Positionbar.setProgress(cureentpos);
                    }catch(InterruptedException e){
                        e.printStackTrace() ;
                    }
                }
            }
        };



        ValidateRecievValuetoplay();
        imageView.setBackgroundResource(R.drawable.logo);


        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

                ArrayList<String> matchFound = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if(matchFound != null)
                {
                    if(Mode.equals("ON")){

                        keeper = matchFound.get(0);

                        if(keeper.equals("pause the song") || keeper.equals("pause") || keeper.equals("stop the song") || keeper.equals("stop")){

                            platpausesong();
                            Toast.makeText(OfflinePlayerActivity.this,"Command:"+keeper,Toast.LENGTH_SHORT).show();
                        }else if(keeper.equals("play the song") || keeper.equals("play") || keeper.equals("start the song") || keeper.equals("start"))
                        {
                            platpausesong();
                            Toast.makeText(OfflinePlayerActivity.this, "Command:" + keeper, Toast.LENGTH_SHORT).show();
                        }else if(keeper.equals("play next song") || keeper.equals("play next"))
                        {
                            playnextsong();
                            Toast.makeText(OfflinePlayerActivity.this, "Command:" + keeper, Toast.LENGTH_SHORT).show();
                        }else if(keeper.equals("play previous song") || keeper.equals("play previous"))
                        {
                            playprevsong();
                            Toast.makeText(OfflinePlayerActivity.this, "Command:" + keeper, Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(OfflinePlayerActivity.this,"Result = "+keeper,Toast.LENGTH_SHORT).show();

                    }
                }


            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });



            parentRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            speechRecognizer.startListening(speechRecogIntent);
                            keeper = "";
                            break;

                        case MotionEvent.ACTION_UP:
                            speechRecognizer.stopListening();
                            break;
                    }
                    return false;
                }

            });



        Voicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Mode.equals("ON")){
                    Mode = "OFF";
                    Voicebtn.setText("Voice mode - OFF");
                    lowerRelativelayout.setVisibility(View.VISIBLE);
                }else {
                    Mode = "ON";
                    Voicebtn.setText("Voice mode - ON");
                    lowerRelativelayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        playPausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                platpausesong();
            }
        });

        prevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MymediaPlayer.getCurrentPosition()>0)
                {
                    playprevsong();
                }
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MymediaPlayer.getCurrentPosition()>0)
                {
                    playnextsong();
                }
            }
        });
    }

    private void  ValidateRecievValuetoplay()
    {
        if(MymediaPlayer != null )
        {
            MymediaPlayer.stop();
            MymediaPlayer.release();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
//        try {
            mySongs = (ArrayList) bundle.getStringArrayList("song");
            mSongname = mySongs.get(position).getName();
//        }catch (NullPointerException e){
//            e.fillInStackTrace();
//        }


        String songname = intent.getStringExtra("name");

        songNametxt.setText(songname);
        songNametxt.setSelected(true);


//        try {
            position = bundle.getInt("position",0);
            Uri uri = Uri.parse(mySongs.get(position).toString());

            MymediaPlayer = MediaPlayer.create(OfflinePlayerActivity.this,uri);
            MymediaPlayer.start();
            Positionbar.setMax(MymediaPlayer.getDuration());
            updatebar.start();
//        }catch (NullPointerException e){
//            e.fillInStackTrace();
//        }


        Positionbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        Positionbar.getThumb().setColorFilter(getResources().getColor(R.color.white),PorterDuff.Mode.SRC_IN);


        Positionbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                MymediaPlayer.seekTo(Positionbar.getProgress());
            }
        });
    }




    private  void  checkVoicecmdPermission(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(!(ContextCompat.checkSelfPermission(OfflinePlayerActivity.this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED)){
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package: "+getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }

    private void platpausesong()
    {
        imageView.setBackgroundResource(R.drawable.four);
        if(MymediaPlayer.isPlaying())
        {
            playPausebtn.setImageResource(R.drawable.plw);
            MymediaPlayer.pause();
        }else {

            playPausebtn.setImageResource(R.drawable.pew);
            MymediaPlayer.start();

            imageView.setBackgroundResource(R.drawable.five);
        }
    }

    private void playnextsong()
    {
        MymediaPlayer.stop();
        MymediaPlayer.pause();
        MymediaPlayer.release();

        position = ((position+1)%mySongs.size());

        Uri uri = Uri.parse(mySongs.get(position).toString());

        MymediaPlayer = MediaPlayer.create(OfflinePlayerActivity.this,uri);

        mSongname = mySongs.get(position).toString();
        songNametxt.setText(mSongname);
        MymediaPlayer.start();

        imageView.setBackgroundResource(R.drawable.three);

        if(MymediaPlayer.isPlaying())
        {
            playPausebtn.setImageResource(R.drawable.pew);
        }else {

            playPausebtn.setImageResource(R.drawable.plw);

            imageView.setBackgroundResource(R.drawable.five);
        }
    }

    private void playprevsong()
    {
        MymediaPlayer.stop();
        MymediaPlayer.pause();
        MymediaPlayer.release();

        position = ((position-1)<0 ? (mySongs.size()-1): (position-1));

        Uri uri = Uri.parse(mySongs.get(position).toString());

        MymediaPlayer = MediaPlayer.create(OfflinePlayerActivity.this,uri);

        mSongname = mySongs.get(position).toString();
        songNametxt.setText(mSongname);
        MymediaPlayer.start();

        imageView.setBackgroundResource(R.drawable.two);

        if(MymediaPlayer.isPlaying())
        {
            playPausebtn.setImageResource(R.drawable.pew);
        }else {

            playPausebtn.setImageResource(R.drawable.plw);

            imageView.setBackgroundResource(R.drawable.five);
        }

    }

}
