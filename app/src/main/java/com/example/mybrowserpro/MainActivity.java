package com.example.mybrowserpro;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnBookMarkTv;
    EditText urlTv;
    ImageButton btnMic,btnClear,btnSearch,btnBookMark,btnMore;
    ProgressBar progressBar;
    ImageButton btnHome,btnStop,btnRefresh,btnForward,btnBack;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webViewId);
        btnBookMarkTv = findViewById(R.id.btnBookMarkTvTabId);
        urlTv = findViewById(R.id.editTextUrlId);
        btnMic = findViewById(R.id.btnMicId);
        btnClear = findViewById(R.id.btnClearId);
        btnSearch = findViewById(R.id.btnSearchId);
        btnBookMark = findViewById(R.id.btnBookMarkId);
        btnMore = findViewById(R.id.btnMoreId);
        btnHome = findViewById(R.id.homeId);
        btnStop = findViewById(R.id.stopId);
        btnRefresh = findViewById(R.id.refreshId);
        btnForward = findViewById(R.id.forwardId);
        btnBack = findViewById(R.id.backId);
        progressBar = findViewById(R.id.progressBarId);

        progressBar.setMax(100);

        btnSearch.setOnClickListener(this::onClick);
        btnClear.setOnClickListener(this::onClick);

    }

    @SuppressLint("SetJavaScriptEnabled")
    public void webViewLoad(String link) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowFileAccess(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if(newProgress == 100){
                   progressBar.setVisibility(View.INVISIBLE);
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, newProgress);

            }
        });
        webView.loadUrl(link);

    }

    public String getLink(EditText urlTv){
        if(!urlTv.getText().toString().isEmpty()){
            String link = urlTv.getText().toString();
            boolean status = link.contains("www.") || link.contains(".com") || link.contains(".");
            if(link.contains("https://")){
                if(status){
                    return link;
                }else{
                    return "https://www.google.com/search?q="+link;
                }
            }else {
                if(status) {
                    return "https://"+link;
                }else{
                    return "https://www.google.com/search?q="+link;
                }
            }
        }

        return null;
    }

    @Override
    public void onClick(View v) {
        if(v == btnSearch){
            webViewLoad(getLink(urlTv));
            Toast.makeText(getApplicationContext(),"working",Toast.LENGTH_LONG).show();
        }
        if(v == btnClear){
            urlTv.getText().clear();
        }
    }
}