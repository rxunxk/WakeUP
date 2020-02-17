package com.raunak.alarmdemo4.Interfaces;

import android.widget.TextView;

import java.util.ArrayList;

public interface AlarmRecyclerViewListener {
    void onItemClick(int position);
    void onLongItemClick(int position);
    void onSwitchClicked(boolean isStart, int position);
    void onModeClicked(int position,String m);
}
