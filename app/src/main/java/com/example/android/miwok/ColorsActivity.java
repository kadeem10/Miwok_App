package com.example.android.miwok;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_list);

        //Declare and Initialize words array
        ArrayList<Word> words = new ArrayList<Word>();

        //Declare and initialize AudioManager + Listener
        final AudioManager am = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
        final AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;

        words.add(new Word("red","weṭeṭṭi",R.drawable.color_red,R.raw.color_red));
        words.add(new Word("green","chokokki",R.drawable.color_green,R.raw.color_green));
        words.add(new Word("brown","ṭakaakki",R.drawable.color_brown,R.raw.color_brown));
        words.add(new Word("gray","ṭopoppi",R.drawable.color_gray,R.raw.color_gray));
        words.add(new Word("black","kululli",R.drawable.color_black,R.raw.color_black));
        words.add(new Word("white","kelelli",R.drawable.color_white,R.raw.color_white));
        words.add(new Word("dusty yellow","ṭopiisә",R.drawable.color_dusty_yellow,R.raw.color_dusty_yellow));
        words.add(new Word("mustard yellow","chiwiiṭә",R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));

        WordAdapter itemsAdapter = new WordAdapter(this, words,R.color.category_colors);

        ListView listView = (ListView) findViewById(R.id.list);

        //Audio Focus Listener - Detects various states of Audio Focus
        audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange){
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:{
                        //Audio focus temporarily lost Pause Playback
                        mediaPlayer.pause();
                        break;
                    }

                    case AudioManager.AUDIOFOCUS_GAIN:{
                        //Audio focus gained after being lost Resume Playback
                        mediaPlayer.start();
                        break;
                    }

                    case AudioManager.AUDIOFOCUS_LOSS:{
                        //Audio focus lost permanently Stop Playback
                        if(mediaPlayer != null) {
                            mediaPlayer.release();
                        }
                        break;
                    }

                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:{
                        //Audio focus temporarily lost but may duck and play at a lower volume
                        //However since it is important in this case the user hear all audio Pause Playback
                        mediaPlayer.pause();
                        break;
                    }
                }
            }
        };


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word currentWord = (Word) parent.getItemAtPosition(position);

                mediaPlayer = MediaPlayer.create(view.getContext(),currentWord.getAudioResID());

                if(mediaPlayer == null){
                    Toast.makeText(view.getContext(),"Error Playing Audio file",Toast.LENGTH_LONG);
                }
                else{
                    if(mediaPlayer.isPlaying()){
                        //Do nothing
                    }
                    else {
                        //Request audio focus
                        int result = am.requestAudioFocus(audioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                        //Check if audio focus was granted
                        if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                            mediaPlayer.start();
                        }
                    }

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            //Release audio focus on completion of playing audio
                            am.abandonAudioFocus(audioFocusChangeListener);
                        }
                    });
                }


            }
        });

        listView.setAdapter(itemsAdapter);

    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer != null){
            mediaPlayer.release();
        }

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mediaPlayer !=null) {
            mediaPlayer.release();
        }
    }
}
