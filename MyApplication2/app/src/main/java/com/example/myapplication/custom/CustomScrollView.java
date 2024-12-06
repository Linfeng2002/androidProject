package com.example.myapplication.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {

    private float mLastY;
    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

     @Override
     public boolean onInterceptTouchEvent(MotionEvent ev) {
         super.onInterceptTouchEvent(ev);
         boolean intercept = false;
         switch (ev.getAction()){
             case MotionEvent.ACTION_DOWN:
                 break;
             case MotionEvent.ACTION_MOVE:
                 intercept=true;
                 break;
         }
         return intercept;
    }

 }
