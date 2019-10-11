package com.nadji.cctvdkijakarta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ListViewDialog {
    private List<String> list;
    private Context context;
    private Dialog dialog;
    private View view;
    private ListView listView;
    private Button buttonDismiss;

    public ListViewDialog(Context context, List<String> list) {
        this.context = context;
        this.list = list;
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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.listview_dialog_item, list);
        listView.setAdapter(arrayAdapter);
        // Fungsi click pada listView dialog
        listView.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);

            alert.setTitle("Lokasi yang di pilih")
                    .setMessage(arrayAdapter.getItem(position));
            alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    Toast.makeText(context, arrayAdapter.getItem(position), Toast.LENGTH_SHORT).show();

//                    String item = MainActivity.LINK ;
//                    Bundle data = new Bundle();
//                    data.putString(WebViewPlay.KEY_ACTIVITY, item);
//                Dialog dialog = new Dialog();
//                    WebViewPlay webViewPlay = new WebViewPlay();
////                    webViewPlay.setArguments(data);
//                    ((FragmentActivity)context)
//                            .getSupportFragmentManager()
//                            .beginTransaction()
//                            .addToBackStack(null)
//                            .replace(R.id.frame_fragment, webViewPlay)
//                            .commit();

                }
            });
            alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alert.show();
        });
    }

    private void setButtonClickListener() {
        buttonDismiss.setOnClickListener(e -> dialog.dismiss());
    }
}
