package ir.tgbs.iranapps.inappbilling.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ir.tgbs.iranapps.billing.helper.util.InAppError;
import ir.tgbs.iranapps.billing.helper.util.InAppHelper;

public class BaseInAppActivity extends AppCompatActivity implements InAppHelper.InAppHelperListener {
    protected InAppHelper inAppHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inAppHelper = new InAppHelper(this, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inAppHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (inAppHelper != null)
            inAppHelper.onActivityDestroy();
    }

    @Override
    public void onConnectedToIABService() {

    }

    @Override
    public void onCantConnectToIABService(InAppError error) {

    }

    @Override
    public void onConnectionLost() {

    }
}
