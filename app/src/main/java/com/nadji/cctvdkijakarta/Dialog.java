package com.nadji.cctvdkijakarta;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Dialog extends DialogFragment {

    public static String KEY_ACTIVITY = "alamat";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_window, container, false);
//        getDialog().setTitle("simple Dialog");


        String txt = getArguments().getString(KEY_ACTIVITY);
        TextView tv = view.findViewById(R.id.title_fragmen);
        tv.setText(txt);

        Button dismis = view.findViewById(R.id.dismis_fragment);
        dismis.setOnClickListener(view1 -> {
            Log.e("kie", "contohe klik button fragment");
            dismiss();
        });


        return view;
    }
}
