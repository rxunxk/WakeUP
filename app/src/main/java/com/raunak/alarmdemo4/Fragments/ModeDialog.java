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

public class ModeDialog extends DialogFragment {
    int position =0;
    public interface  radioClickListener{
        void onPositiveClick(String[] list, int position);
        void onNegativeClick();
    }
    private radioClickListener mRadioClickListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mRadioClickListener = (radioClickListener) context;
        } catch (Exception e) {
            throw new ClassCastException(getActivity().toString()+" RadioClickListener Not implemented");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] list = getActivity().getResources().getStringArray(R.array.modes);
        builder.setTitle("Select Mode")
                .setSingleChoiceItems(list, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        position = i;
                    }
                })
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mRadioClickListener.onPositiveClick(list,position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mRadioClickListener.onNegativeClick();
                    }
                });
        return builder.create();
    }
}
