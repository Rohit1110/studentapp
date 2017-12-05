package com.resoneuronance.shahucetcell;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class AnalysisShow extends AppCompatActivity {

    private String url;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_show);
        webView=(WebView)findViewById(R.id.analysiswebview);
        Bundle bundle = getIntent().getExtras();//Extract the data…
        if (bundle != null) {
            url = bundle.getString("examid");
            webView.loadUrl(url);

        }
    }
}
