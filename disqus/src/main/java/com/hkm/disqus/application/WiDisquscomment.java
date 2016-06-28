package com.hkm.disqus.application;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.hkm.disqus.BuildConfig;
import com.hkm.ezwebview.Util.CacheMode;
import com.hkm.ezwebview.Util.Fx9C;
import com.hkm.ezwebview.app.BasicWebViewNormal;
import com.hkm.ezwebview.webviewclients.ChromeLoader;
import com.hkm.ezwebview.webviewclients.PaymentClient;

/**
 * Created by hesk on 10/11/15.
 */
public class WiDisquscomment extends BasicWebViewNormal {
    private static final String
            BASE_URL_KEY = "baseUrl",
            URL_KEY = "url",
            IDENTIFER_KEY = "identifier",
            SHORTNAME_KEY = "shortname";
    private static final String TAG = WiDisquscomment.class.getSimpleName();

    public static WiDisquscomment newInstance(
            Bundle b) {
        final WiDisquscomment wi = new WiDisquscomment();
        wi.setArguments(b);
        return wi;
    }

    public static Bundle createBundle(
            final String baseUrl,
            final String id,
            final String url,
            final String shortname) {
        Bundle b = new Bundle();
        b.putString(BASE_URL_KEY, baseUrl);
        b.putString(URL_KEY, url);
        b.putString(IDENTIFER_KEY, id);
        b.putString(SHORTNAME_KEY, shortname);
        return b;
    }

    public static WiDisquscomment newInstance(
            final String baseUrl,
            final String id,
            final String url,
            final String shortname) {
        final WiDisquscomment wi = new WiDisquscomment();
        wi.setArguments(createBundle(baseUrl, id, url, shortname));
        return wi;
    }

    protected String getDisqusUrl() {
        Uri.Builder bu = Uri.parse(getArguments().getString(BASE_URL_KEY)).buildUpon();
        bu.appendQueryParameter(URL_KEY, getArguments().getString(URL_KEY));
        bu.appendQueryParameter(IDENTIFER_KEY, getArguments().getString(IDENTIFER_KEY));
        bu.appendQueryParameter(SHORTNAME_KEY, getArguments().getString(SHORTNAME_KEY));
        return bu.build().toString();
    }

    private class DisqusClient extends PaymentClient {
        public DisqusClient(Activity context, WebView fmWebView) {
            super(context, fmWebView);
        }

        private boolean isLoginSuccessUrl(String url) {
            url = url.trim();
            return url.startsWith("http://disqus.com/next/login-success/")||
                    url.startsWith("https://disqus.com/next/login-success/") ||
                    url.matches("^https?:\\/\\/disqus\\.com\\/_ax\\/(twitter|google|facebook)\\/complete.*");
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.e(TAG, String.format("received error on loading disqus page; errorCode=%d; failingUrl=%s; description=%s", errorCode, failingUrl, description));
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (BuildConfig.DEBUG) {
                Log.d(TAG, String.format("onPageFinished; url=%s", url));
            }

            if (isLoginSuccessUrl(url)) {
                view.loadUrl(getDisqusUrl());
            }
        }

        @Override
        protected boolean interceptUrl(WebView view, String url) {
            return false;
        }
    }

    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);
        loadLandingPage();
    }

    public void loadLandingPage() {
        String disqusUrl = getDisqusUrl();

        Log.d(TAG, String.format("loading disqus page: %s", disqusUrl));

        try {
            Fx9C
                    .with(getActivity())
                    .setAllowHTTPSMixedContent(true)
                    .setAnimationDuration(3000)
                    .setCacheMode(CacheMode.LOAD_NO_CACHE)
                    .setOnCloseWindowCallback(new ChromeLoader.OnCloseWindowCallback() {
                        /* that happens after successful sign-on */
                        @Override
                        public void onCloseWindow(WebView webView) {
                            block.loadUrl(getDisqusUrl());
                        }
                    })
                    .setJavaScriptEnabled(true)
                    .setWebViewHolder(framer)
                    .setWebView(block)
                    .setWebViewClient(new DisqusClient(getActivity(), block))
                    .setProgressBar(betterCircleBar)
                    .loadUrl(getDisqusUrl());
        } catch (Exception e) {
            Log.e(TAG, "failed to load the disqus page", e);
        }
    }

    public void onBackPressed() {
        if (block.canGoBack()) {
            block.goBack();
        }
    }

    public void openInChrome() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(block.getUrl()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        startActivity(intent);
    }
}
