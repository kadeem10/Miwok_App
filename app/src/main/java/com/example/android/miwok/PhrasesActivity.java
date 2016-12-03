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

public class PhrasesActivity extends AppCompatActivity {

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

        words.add(new Word("Where are you going?","minto wuksus",R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?","tinnә oyaase'nә",R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...","oyaaset...",R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?","michәksәs?",R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.","kuchi achit",R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?","әәnәs'aa?",R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.","hәә’ әәnәm",R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.","әәnәm",R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.","yoowutis",R.raw.phrase_lets_go));
        words.add(new Word("Come here.","әnni'nem",R.raw.phrase_come_here));

        WordAdapter itemsAdapter = new WordAdapter(this, words,R.color.category_phrases);

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
                else {
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
        if(mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
