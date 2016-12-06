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
public class NumbersFragment extends Fragment {

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


    public NumbersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //store inflated view in rootView
        View rootView = inflater.inflate(R.layout.words_list, container, false);

        //Declare and Initialize arraylist called words this takes in word objects
        ArrayList<Word> words = new ArrayList<Word>();

        //Initialize AudioManager
        am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        //Add Word objects to the arraylist words
        words.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "wo'e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "na'aacha", R.drawable.number_ten, R.raw.number_ten));

        //Declare and initialize a WordAdapter //final is just a fancy term for static
        final WordAdapter itemsAdapter = new WordAdapter(getActivity(), words, R.color.category_numbers);

        //Declare and initialize a ListView.
        //This list view is set to the list in the layout called words_list
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

    @Override
    public void onStop() {
        super.onStop();

        //Release media player since we are not using it
        releaseMediaPlayer();
    }
}
