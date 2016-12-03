package com.example.android.miwok;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.R.attr.onClick;
import static android.R.attr.resource;

/**
 * Created by Reshaud Ally on 11/5/2016.
 */

public class WordAdapter extends ArrayAdapter<Word> {
    private int mColor;
    private int mAudio;

    public WordAdapter(Context context,ArrayList<Word> words,int color){
        super(context,0,words);
        mColor = color;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Check if the exisiting view is being used else inflate(inflate is to create the view)
        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout,parent,false);
        }

        //get the current word object in the array list
        Word currentWord = getItem(position);

        //Find the miwok text view

        TextView miwokText = (TextView) listItemView.findViewById(R.id.miwok_word);

        //Find the eng text view
        TextView engText = (TextView) listItemView.findViewById(R.id.english_word);

        //Find the image view
        ImageView imgView = (ImageView) listItemView.findViewById(R.id.icon);

        //get the miwok word at the current position
        //set the text of the TextView
        miwokText.setText(currentWord.getMiwokWord());

        //get the english word at the current position
        //set the text of the TextView
        engText.setText(currentWord.getEngWord());

        if (currentWord.hasImage()){
            //get the image at the current position
            //set the image of the ImageView
            imgView.setImageResource(currentWord.getResourceID());
        }

        else{
            imgView.setVisibility(View.GONE);
        }

        //find view to set background for
        View textContainer = (View) listItemView.findViewById(R.id.text_container);

        //set color
        textContainer.setBackgroundColor(ContextCompat.getColor(getContext(),mColor));



        //Return the view containing the two text view
        //to be shown in the listview
        return listItemView;
    }
}
