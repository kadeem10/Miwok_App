package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhrasesFragment extends Fragment {

    //Handles playback of sounds
    private MediaPlayer mediaPlayer;

    //Handles audio focus when playing a sound file
    private AudioManager am;

    //Audio Focus Listener
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            //On completion of audio file
            //release media player
            releaseMediaPlayer();
        }
    };


    public PhrasesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.words_list, container, false);

        //Declare and Initialize words array
        ArrayList<Word> words = new ArrayList<Word>();

        //Initialize AudioManager
        am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        words.add(new Word("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...", "oyaaset...", R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.", "әәnәm", R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.", "yoowutis", R.raw.phrase_lets_go));
        words.add(new Word("Come here.", "әnni'nem", R.raw.phrase_come_here));

        final WordAdapter itemsAdapter = new WordAdapter(getActivity(), words, R.color.category_phrases);

        final ListView listView = (ListView) rootView.findViewById(R.id.list);

        //Audio Focus Listener - Detects various states of Audio Focus
        audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: {
                        //Audio focus temporarily lost Pause Playback
                        mediaPlayer.pause();
                        break;
                    }

                    case AudioManager.AUDIOFOCUS_GAIN: {
                        //Audio focus regained after being lost Resume Playback
                        mediaPlayer.start();
                        break;
                    }

                    case AudioManager.AUDIOFOCUS_LOSS: {
                        //Audio focus lost permanently Stop Playback
                        releaseMediaPlayer();
                        break;
                    }

                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK: {
                        //App is allowed to play sound but at a lower volume
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
                // Release the media player if it currently exists because we are about to
                // play a different sound file
                releaseMediaPlayer();

                //Get the word object the user clicked on
                Word currentWord = (Word) parent.getItemAtPosition(position);

                //Request audio focus
                int result = am.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                //Check if audio focus was granted
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //AudioFocus was granted, Create mediaPlayer
                    mediaPlayer = MediaPlayer.create(getActivity(), currentWord.getAudioResID());

                    //Start playing audio file
                    mediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

        //Set the adapter for the listview
        listView.setAdapter(itemsAdapter);

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();

        //release media player resources
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        //If mediaplayer is not null then release it
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        // Set the media player back to null. For our code, we've decided that
        // setting the media player to null is an easy way to tell that the media player
        // is not configured to play an audio file at the moment.
        mediaPlayer = null;

        // Regardless of whether or not we were granted audio focus, abandon it. This also
        // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
        am.abandonAudioFocus(audioFocusChangeListener);
    }
}
