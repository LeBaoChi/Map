package com.itshareplus.googlemapdemo;

/**
 * Created by Lê Bảo Chi on 5/5/2017.
 */

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.gms.common.GoogleApiAvailability;

public class LegalNoticesActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legal);

        TextView legal=(TextView)findViewById(R.id.legal);

        legal.setText(GoogleApiAvailability
                .getInstance()
                .getOpenSourceSoftwareLicenseInfo(this));
    }
}