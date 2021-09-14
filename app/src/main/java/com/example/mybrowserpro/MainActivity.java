package com.example.mybrowserpro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnBookMarkTv;
    EditText urlTv;
    ImageButton btnMic, btnClear, btnSearch, btnBookMark, btnMore;
    ProgressBar progressBar;
    ImageButton btnHome, btnStop, btnRefresh, btnForward, btnBack;
    WebView webView;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private int tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webViewId);
        webViewLoad("https://www.google.com/");
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
        btnRefresh.setOnClickListener(this::onClick);
        btnMore.setOnClickListener(this::onClick);
        btnBack.setOnClickListener(this::onClick);
        btnForward.setOnClickListener(this::onClick);
        btnStop.setOnClickListener(this::onClick);
        btnHome.setOnClickListener(this::onClick);
        btnMic.setOnClickListener(this::onClick);

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

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (!url.equals("https://www.google.com/")) {
                    urlTv.setText(url);
                } else if (url.equals("https://www.google.com/")) {
                    urlTv.getText().clear();
                }
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

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, newProgress);

            }
        });
        webView.loadUrl(link);


    }

    public void webViewDwnFunc() {
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager.Request request =
                        new DownloadManager.Request(Uri.parse(url));
                request.setMimeType(mimetype);
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookic", cookies);

                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Download File.......");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                // request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url,contentDisposition,mimetype));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG)
                        .show();

            }
        });
    }

    public String getLink(EditText urlTv) {
        if (!urlTv.getText().toString().isEmpty()) {
            String link = urlTv.getText().toString();
            boolean status = link.contains("www.") || link.contains(".com") || link.contains(".");
            if (link.contains("https://")) {
                if (status) {
                    return link;
                } else {
                    return "https://www.google.com/search?q=" + link;
                }
            } else {
                if (status) {
                    return "https://" + link;
                } else {
                    return "https://www.google.com/search?q=" + link;
                }
            }
        }

        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == btnSearch) {
            webViewLoad(getLink(urlTv));
            Toast.makeText(getApplicationContext(), "working", Toast.LENGTH_LONG).show();
        }

        if (v == btnClear) {
            urlTv.getText().clear();
        }

        if (v == btnRefresh) {
            webView.reload();
        }

        if (v == btnBack) {
            if (webView.canGoBack()) {
                webView.goBack();
            }
        }

        if (v == btnForward) {
            if (webView.canGoForward()) {
                webView.goForward();
            }
        }

        if (v == btnStop) {
            webView.stopLoading();
        }

        if (v == btnHome) {
            webViewLoad("https://www.google.com/");
        }

        if (v == btnMic) {
            speak();
        }


        if (v == btnMore) {
            BottomSheetDialog bottomSheetDialog =
                    new BottomSheetDialog(MainActivity.this,
                            R.style.BottomSheetDialogTheme);
            View view = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.reorder_dialog_layout, findViewById(R.id.dialog_container));

            view.findViewById(R.id.moreMenuCancelId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                }
            });
            view.findViewById(R.id.refId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webView.reload();
                }
            });

            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:{
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    urlTv.setText(result.get(0));
                    String urls = urlTv.getText().toString();
                }
            }
        }

    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
           Log.e("#####",e.getMessage());
        }

/*        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch(ActivityNotFoundException e) {
            String appPackageName = "com.google.android.googlequicksearchbox";
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }*/
    }
}