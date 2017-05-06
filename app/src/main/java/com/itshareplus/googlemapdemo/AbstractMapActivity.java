package com.itshareplus.googlemapdemo;

/**
 * Created by Lê Bảo Chi on 5/5/2017.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import static com.itshareplus.googlemapdemo.R.layout.activity_maps;

public class AbstractMapActivity extends Activity {
    static final String TAG_ERROR_DIALOG_FRAGMENT="errorDialog";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(activity_maps, menu);

        return(super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.legal) {
            startActivity(new Intent(this, LegalNoticesActivity.class));

            return(true);
        }

        return super.onOptionsItemSelected(item);
    }

    protected boolean readyToGo() {
        GoogleApiAvailability checker=
                GoogleApiAvailability.getInstance();

        int status=checker.isGooglePlayServicesAvailable(this);

        if (status == ConnectionResult.SUCCESS) {
            if (getVersionFromPackageManager(this)>=2) {
                return(true);
            }
            else {
                Toast.makeText(this, R.string.no_maps, Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else if (checker.isUserResolvableError(status)) {
            ErrorDialogFragment.newInstance(status)
                    .show(getFragmentManager(),
                            TAG_ERROR_DIALOG_FRAGMENT);
        }
        else {
            Toast.makeText(this, R.string.no_maps, Toast.LENGTH_LONG).show();
            finish();
        }

        return(false);
    }

    public static class ErrorDialogFragment extends DialogFragment {
        static final String ARG_ERROR_CODE="errorCode";

        static ErrorDialogFragment newInstance(int errorCode) {
            Bundle args=new Bundle();
            ErrorDialogFragment result=new ErrorDialogFragment();

            args.putInt(ARG_ERROR_CODE, errorCode);
            result.setArguments(args);

            return(result);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle args=getArguments();
            GoogleApiAvailability checker=
                    GoogleApiAvailability.getInstance();

            return(checker.getErrorDialog(getActivity(),
                    args.getInt(ARG_ERROR_CODE), 0));
        }

        @Override
        public void onDismiss(DialogInterface dlg) {
            if (getActivity()!=null) {
                getActivity().finish();
            }
        }
    }



    private static int getVersionFromPackageManager(Context context) {
        PackageManager packageManager=context.getPackageManager();
        FeatureInfo[] featureInfos=
                packageManager.getSystemAvailableFeatures();
        if (featureInfos != null && featureInfos.length > 0) {
            for (FeatureInfo featureInfo : featureInfos) {

                if (featureInfo.name == null) {
                    if (featureInfo.reqGlEsVersion != FeatureInfo.GL_ES_VERSION_UNDEFINED) {
                        return getMajorVersion(featureInfo.reqGlEsVersion);
                    }
                    else {
                        return 1;
                    }
                }
            }
        }
        return 1;
    }

    /** @see FeatureInfo#getGlEsVersion() */
    private static int getMajorVersion(int glEsVersion) {
        return((glEsVersion & 0xffff0000) >> 16);
    }
}