package ir.tgbs.iranapps.inappbilling.sample.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.tgbs.iranapps.billing.helper.interfaces.ConsumeListener;
import ir.tgbs.iranapps.billing.helper.interfaces.LoginListener;
import ir.tgbs.iranapps.billing.helper.interfaces.PurchasesListener;
import ir.tgbs.iranapps.billing.helper.model.PurchaseData;
import ir.tgbs.iranapps.billing.helper.util.InAppError;
import ir.tgbs.iranapps.billing.helper.util.InAppKeys;
import ir.tgbs.iranapps.inappbilling.sample.R;
import ir.tgbs.iranapps.inappbilling.sample.adapter.PurchaseAdapter;
import ir.tgbs.iranapps.inappbilling.sample.util.Util;

/**
 * activity class that shows purchaseList in listView
 */
public class PurchaseActivity extends BaseInAppActivity {

    ListView lvPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvPurchase = (ListView) findViewById(R.id.lv_purchase);

        lvPurchase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final PurchaseData purchaseData = (PurchaseData) parent.getItemAtPosition(position);
                inAppHelper.consumeProduct(purchaseData.purchaseItem.purchaseToken, new ConsumeListener() {
                    @Override
                    public void onConsumeSucceed() {
                        Util.showAlertDialogBackground(PurchaseActivity.this, purchaseData.sku + " consumed", false);
                    }

                    @Override
                    public void onItemNotOwned() {
                        Util.showAlertDialogBackground(PurchaseActivity.this, purchaseData.sku + " is not owned by user", false);
                    }

                    @Override
                    public void onConsumeFailed(InAppError error) {
                        Util.showAlertDialogBackground(PurchaseActivity.this, error.getMessage(), false);
                    }
                });
            }
        });
    }

    /**
     * if service binds, this method executes.
     * then gets purchaseDetail list.
     *
     * @see ir.tgbs.iranapps.billing.helper.interfaces.PurchasesListener
     */
    @Override
    public void onConnectedToIABService() {
        super.onConnectedToIABService();
        //call {@link #getPurchases} thread

        final AlertDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("getting purchase details");
        progressDialog.show();

        inAppHelper.getPurchases(new PurchasesListener() {

            @Override
            public void onGotPurchases(final ArrayList<PurchaseData> purchases, String continuationToken) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if (purchases.size() != 0) {
                            PurchaseAdapter adapter = new PurchaseAdapter(PurchaseActivity.this, purchases);
                            lvPurchase.setAdapter(adapter);
                        } else {
                            Util.showAlertDialog(PurchaseActivity.this, "you have not purchased anything yet.", true);
                        }
                    }
                });
            }

            @Override
            public void onFailedGettingPurchases(final InAppError errorCode) {
                progressDialog.dismiss();
                if (errorCode.getErrorCode() == InAppKeys.BILLING_RESPONSE_USER_NOT_LOGIN) {
                    inAppHelper.loginUser(new LoginListener() {
                        @Override
                        public void onLoginSucceed() {
                            //after than logging should getPurchase process done again
                            onConnectedToIABService();
                        }

                        @Override
                        public void onLoginFailed(InAppError errorCode) {
                            Util.showAlertDialogBackground(PurchaseActivity.this, errorCode.getMessage(), true);
                        }
                    });
                } else {
                    Util.showAlertDialogBackground(PurchaseActivity.this, errorCode.getMessage(), true);
                }
            }
        });
    }

    @Override
    public void onCantConnectToIABService(InAppError error) {
        super.onCantConnectToIABService(error);

        switch (error) {
            case BILLING_RESPONSE_IRANAPPS_NOT_AVAILABLE:
                Util.showAlertDialog(PurchaseActivity.this, error.getMessage(), true);
                break;

            default:
                Util.showAlertDialog(PurchaseActivity.this, error.getMessage(), true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
