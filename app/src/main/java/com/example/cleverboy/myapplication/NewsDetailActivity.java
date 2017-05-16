package com.example.cleverboy.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * Created by clever boy on 2017/5/11.
 */

public class NewsDetailActivity extends Activity implements View.OnClickListener{

    private LinearLayout linearLayout;
    private ImageButton btnBack;
    private ImageButton btnMenu;
    private WebView webView;
    private ProgressBar progressBar;
    private String mUrl;
    private ImageButton btnTextSize;
    private ImageButton btnShare;
    private int mChooseItem;
    private int mSelectedItem = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);

        btnMenu = (ImageButton) findViewById(R.id.btn_menu);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnTextSize = (ImageButton) findViewById(R.id.btn_textsize);
        btnShare = (ImageButton) findViewById(R.id.btn_share);
        linearLayout = (LinearLayout) findViewById(R.id.ll_container);
        webView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading);

        btnMenu.setVisibility(View.GONE);
        btnBack.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);
        btnTextSize.setOnClickListener(this);
        btnShare.setOnClickListener(this);

        mUrl = getIntent().getStringExtra("url");
        System.out.println(mUrl);
        webView.loadUrl(mUrl);
        WebSettings settings = webView.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
//        settings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }
            @JavascriptInterface
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("sdfsdf");
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_textsize:
                showChooseDialog();
                break;
            case R.id.btn_share:
                break;

        }
    }
    private void showChooseDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        String[] items = new String[]{"超大号字体","大号字体","正常字体","小号字体","超小号字体"};
        builder.setSingleChoiceItems(items,mSelectedItem,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mChooseItem = which;
            }
        });
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){


            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings = webView.getSettings();
                switch (mChooseItem){
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }
                mSelectedItem = mChooseItem;
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }
}
