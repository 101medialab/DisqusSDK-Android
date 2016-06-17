package com.hkm.disqus.application;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.hkm.ezwebview.BuildConfig;
import com.hkm.ezwebview.Util.Fx9C;
import com.hkm.ezwebview.app.BasicWebViewNormal;
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

    private class GM extends PaymentClient {
        public GM(Activity context, WebView fmWebView) {
            super(context, fmWebView);
        }
        @Override
        protected boolean interceptUrl(WebView view, String url) {
            return false;
        }
    }

    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);
    }

    public void loadLandingPage() {
        String disqusUrl = getDisqusUrl();

        Log.d(TAG, String.format("loading disqus page: %s", disqusUrl));

        try {
            Fx9C.setup_payment_gateway(
                    new GM(getActivity(), block),
                    framer,
                    block,
                    betterCircleBar,
                    getDisqusUrl(),
                    "",
                    3000
            );
        } catch (Exception e) {
            Log.e(TAG, "failed to load the disqus page", e);
        }
    }
}
