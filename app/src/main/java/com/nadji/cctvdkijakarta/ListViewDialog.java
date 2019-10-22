package com.nadji.cctvdkijakarta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListViewDialog {
    private ArrayList<HashMap<String, List<String>>> listt;
    private Context context;
    private Dialog dialog;
    private View view;
    private ListView listView;
    private Button buttonDismiss;
    private String KEY_ALAMAT = "ALAMAT";

    public ListViewDialog(Context context, ArrayList<HashMap<String, List<String>>> listt) {
        this.context = context;
        this.listt = listt;
        setupDialog();
        setupUI();
        setupListView();
        setButtonClickListener();
    }

    public void showDialog() {
        dialog.show();
    }

    private void setupDialog() {
        dialog = new Dialog(context);
        view = LayoutInflater.from(context).inflate(R.layout.listview_dialog, null);
        dialog.setContentView(view);
    }

    private void setupUI() {
        listView = view.findViewById(R.id.listView);
        buttonDismiss = view.findViewById(R.id.buttonDismiss);
    }

    private void setupListView() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.listview_dialog_item, listt.get(0).get("alamatt"));
        ArrayAdapter<String> arrayLink = new ArrayAdapter<>(context, R.layout.listview_dialog_item, listt.get(0).get("linkk"));
        listView.setAdapter(arrayAdapter);
        // Fungsi click pada listView dialog
        listView.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
            alert.setTitle("Lokasi yang di pilih")
                    .setMessage(arrayAdapter.getItem(position));
            alert.setPositiveButton("lihat video", (dialog, which) -> {
//                    Toast.makeText(context, arrayLink.getItem(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PlayStream.class);
                intent.putExtra(KEY_ALAMAT, arrayLink.getItem(position));
                context.startActivity(intent);
            });
            alert.setNegativeButton("cancel", (dialogInterface, i) -> dialogInterface.dismiss());
            alert.show();
        });
    }

    private void setButtonClickListener() {
        buttonDismiss.setOnClickListener(e -> dialog.dismiss());
    }
}
