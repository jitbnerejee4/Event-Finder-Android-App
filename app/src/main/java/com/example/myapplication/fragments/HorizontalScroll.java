package com.example.myapplication.fragments;

import android.widget.TextView;


public class HorizontalScroll {
    public HorizontalScroll(TextView textView){
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setSelected(true);
            }
        }, 500);
    }
}
