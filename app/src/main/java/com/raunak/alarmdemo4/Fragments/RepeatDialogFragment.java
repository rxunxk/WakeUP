package com.raunak.alarmdemo4.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.raunak.alarmdemo4.R;
import java.util.ArrayList;

public class RepeatDialogFragment extends DialogFragment {
    public interface onMultiChoiceListener{
        void onPositiveButtonClicked(String[] list, ArrayList<String> selectedDaysList);
        void onNegativeButtonClicked();
    }

    private onMultiChoiceListener mOnMultiChoiceListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnMultiChoiceListener = (onMultiChoiceListener) context;
        } catch (Exception e) {
            throw new ClassCastException(getActivity().toString()+" onMultiChoiceListener must be implemented");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final ArrayList<String> selectedDays = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[]  list = getActivity().getResources().getStringArray(R.array.days);
        builder.setTitle("Select Repeat Days")
                .setMultiChoiceItems(list,null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            selectedDays.add(list[i]);
                        }else{
                            selectedDays.remove(list[i]);
                        }
                    }
                })
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mOnMultiChoiceListener.onPositiveButtonClicked(list,selectedDays);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mOnMultiChoiceListener.onNegativeButtonClicked();
                    }
                });
        return builder.create();
    }
}
