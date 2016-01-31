package com.konifar.confsched.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.konifar.confsched.R;
import com.konifar.confsched.databinding.ActivityWebViewBinding;

public class WebViewActivity extends AppCompatActivity {

    private static final String EXTRA_URL = "url";
    private static final String EXTRA_TITLE = "title";

    private ActivityWebViewBinding binding;

    public static void start(Context context, @NonNull String url, @NonNull String title) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(title)) {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra(EXTRA_URL, url);
            intent.putExtra(EXTRA_TITLE, title);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String title = getIntent().getStringExtra(EXTRA_TITLE);
        final String url = getIntent().getStringExtra(EXTRA_URL);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);

        initToolbar(title);
        initWebView(url);
    }

    private void initWebView(@NonNull String url) {
        binding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        binding.webview.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar(@NonNull String title) {
        setSupportActionBar(binding.toolbar);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setDisplayShowTitleEnabled(false);
            bar.setHomeButtonEnabled(true);
        }
        binding.toolbar.setTitle(title);
    }

}
