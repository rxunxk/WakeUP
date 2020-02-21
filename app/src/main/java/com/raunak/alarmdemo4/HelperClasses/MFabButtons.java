package com.raunak.alarmdemo4.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raunak.alarmdemo4.Activities.AddAlarm;
import com.raunak.alarmdemo4.Fragments.HomeFragment;
import com.raunak.alarmdemo4.Fragments.TimePickerFragment;
import com.raunak.alarmdemo4.MainActivity;

public class MFabButtons extends HomeFragment {
    private final int ADD_ALARM_REQUEST_CODE =2;
    private FloatingActionButton fab_button_1, fab_button_2, fab_button_3, fab_button_4;
    private OvershootInterpolator overshootInterpolator = new OvershootInterpolator();
    private int fabTranslationY = 100;
    private boolean isFabMenuOpen = false;
    private int speed = 250;
    private Context context;

    public MFabButtons(Context context, FloatingActionButton fab_button_1, FloatingActionButton fab_button_2, FloatingActionButton fab_button_3) {
        this.fab_button_1 = fab_button_1;
        this.fab_button_2 = fab_button_2;
        this.fab_button_3 = fab_button_3;
        this.context = context;

        setFabTranslationY();
        setFabClicks();
    }

    private void setFabTranslationY() {
        fab_button_2.setAlpha(0f);
        fab_button_3.setAlpha(0f);

        fab_button_2.setTranslationY(fabTranslationY);
        fab_button_3.setTranslationY(fabTranslationY);
    }

    private void setFabClicks() {
        fab_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuCheck();
            }
        });

        fab_button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuCheck();
                Intent mIntent = new Intent(context, AddAlarm.class);
                startActivityForResult(mIntent, ADD_ALARM_REQUEST_CODE);
            }
        });
    }

    private void fabMenuOpen() {
        isFabMenuOpen = !isFabMenuOpen;
        fabMainAnimation(true);
        fabOpenAnimation(fab_button_2);
        fabOpenAnimation(fab_button_3);
    }

    private void fabMenuClose() {
        isFabMenuOpen = !isFabMenuOpen;
        fabMainAnimation(false);
        fabCloseAnimation(fab_button_2);
        fabCloseAnimation(fab_button_3);
    }

    private void fabCloseAnimation(FloatingActionButton fab) {
        fab.animate().translationY(fabTranslationY).alpha(0f).setInterpolator(overshootInterpolator).setDuration(speed).start();
    }

    private void fabOpenAnimation(FloatingActionButton fab) {
        fab.animate().translationY(0f).alpha(1f).setInterpolator(overshootInterpolator).setDuration(speed).start();
    }

    private void fabMainAnimation(boolean isOpen) {
        if (isOpen) {
            fab_button_1.animate().setInterpolator(overshootInterpolator).rotation(45f).setDuration(speed).start();
        } else {
            fab_button_1.animate().setInterpolator(overshootInterpolator).rotation(0f).setDuration(speed).start();
        }
    }

    private void menuCheck() {
        if (isFabMenuOpen) {
            fabMenuClose();
        } else {
            fabMenuOpen();
        }
    }
}
