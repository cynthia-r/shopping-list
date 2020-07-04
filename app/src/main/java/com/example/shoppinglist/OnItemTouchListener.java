package com.example.shoppinglist;


import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.core.view.MotionEventCompat;

// TODO see if I keep this
public class OnItemTouchListener implements View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        TextView textView = view.findViewById(R.id.item);

        int motionAction = MotionEventCompat.getActionMasked(motionEvent);

        if (motionAction == MotionEvent.ACTION_DOWN) {
            textView.setAlpha(0.9f);
            view.setBackgroundColor(Color.parseColor("#f5f5f5"));
        }

        if (motionAction == MotionEvent.ACTION_UP || motionAction == MotionEvent.ACTION_CANCEL) {
            textView.setAlpha(1.0f);
            view.setBackgroundColor(0);
        }

        return false;
    }
}
