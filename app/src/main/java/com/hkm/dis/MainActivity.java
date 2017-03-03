package com.hkm.dis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hkm.disqus.application.WiDisquscomment;


/**
 * Created by hesk on 9/11/15.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    /* change these values as you see fit */
    private static final String DISQUS_URL = "http://www.androidauthority.com/wearables-market-grew-16-9-q4-2016-can-believe-754567/";
    private static final String DISQUS_ID = "754567";
    private static final String DISQUS_SHORTNAME = "androidauthority";

    private WiDisquscomment disqusFragment;

    protected void statFragmentLogin() {

        disqusFragment = WiDisquscomment.newInstance(WiDisquscomment.createBundle(
                DISQUS_URL,
                DISQUS_ID,
                DISQUS_URL,
                DISQUS_SHORTNAME
        ));
        getFragmentManager()
                .beginTransaction().
                add(R.id.fragment_id_a, disqusFragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutlogin);
        Bundle extras = new Bundle();
        statFragmentLogin();
    }

    @Override
    public void onBackPressed() {
        disqusFragment.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.web_integration_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.openInChromeButton:
                Log.d(TAG, "open page in chrome");
                disqusFragment.openInChrome();
                return true;
            default:
                Log.d(TAG, String.format("unhandled menu item; id=%d", item.getItemId()));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
