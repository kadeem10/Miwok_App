package com.example.android.miwok;

import android.content.Context;

/**
 * Created by Reshaud Ally on 11/5/2016.
 */

//Class that contains both miwork and english translations
public class Word {
    private String miwokWord;
    private String engWord;
    private static final int  NO_IMAGE = -1;
    private int resID = NO_IMAGE;
    private int mAudioRes;

    //Methods
    //Constructor
    public Word(String eng, String miwok,int audioResource){
        miwokWord = miwok;
        engWord = eng;
        mAudioRes = audioResource;
    }

    public Word(String eng, String miwok, int resourceID, int audioResource){
        miwokWord = miwok;
        engWord = eng;
        resID = resourceID;
        mAudioRes = audioResource;
    }

    //Accessors
    public String getEngWord(){
        return engWord;
    }

    public String getMiwokWord(){
        return miwokWord;
    }

    public int getResourceID(){
        return resID;
    }

    public int getAudioResID(){
        return mAudioRes;
    }

    public boolean hasImage(){
        if(resID == -1){
            return  false;
        }

        else
        {
            return true;
        }
    }
}
