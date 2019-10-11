package com.nadji.cctvdkijakarta;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;


public class PlayStream extends AppCompatActivity {

    private String lokasi;
    private String KEY_ALAMAT = "ALAMAT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_stream);
        WebView webView = findViewById(R.id.playstream);

        Bundle extra = getIntent().getExtras();
        lokasi = extra.getString(KEY_ALAMAT);

        webView.setWebChromeClient(new WebChromeClientCustomClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSettings.setJavaScriptEnabled(true);
        webView.setFocusable(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(lokasi);
    }

    private class WebChromeClientCustomClient extends WebChromeClient {
        @Override
        public Bitmap getDefaultVideoPoster() {
            return Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
        }
    }
}
