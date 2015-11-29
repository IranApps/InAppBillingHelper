package ir.tgbs.iranapps.inappbilling.sample.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.tgbs.iranapps.billing.helper.interfaces.ConsumeListener;
import ir.tgbs.iranapps.billing.helper.interfaces.LoginListener;
import ir.tgbs.iranapps.billing.helper.interfaces.PurchasesListener;
import ir.tgbs.iranapps.billing.helper.model.PurchaseData;
import ir.tgbs.iranapps.billing.helper.util.ConsumeHelper;
import ir.tgbs.iranapps.billing.helper.util.GetPurchasesHelper;
import ir.tgbs.iranapps.billing.helper.util.InAppError;
import ir.tgbs.iranapps.billing.helper.util.InAppKeys;
import ir.tgbs.iranapps.billing.helper.util.InAppLoginHelper;
import ir.tgbs.iranapps.inappbilling.sample.R;
import ir.tgbs.iranapps.inappbilling.sample.adapter.PurchasesAdapter;
import ir.tgbs.iranapps.inappbilling.sample.util.Util;

/**
 * activity class that shows purchaseList in listView
 */
public class PurchasesActivity extends BaseInAppActivity {
    private static final String KEY_PURCHASED_DATA = "PurchasedData";

    private ListView lvPurchase;
    @Nullable
    private AlertDialog progressDialog;
    @Nullable
    private ArrayList<PurchaseData> purchaseDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        lvPurchase = (ListView) findViewById(R.id.lv_purchase);

        lvPurchase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PurchaseData purchaseData = (PurchaseData) parent.getItemAtPosition(position);
                String purchaseToken = purchaseData.purchaseItem.purchaseToken;
                ConsumeHelper consumeHelper = inAppHelper.consumeProduct(purchaseToken, new ConsumeListenerImpl(purchaseToken));
                holderFragment.setConsumeHelper(consumeHelper);
            }
        });

        if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_PURCHASED_DATA)) {
            progressDialog = ProgressDialog.show(this, null, "getting purchased items details");
        } else {
            //noinspection unchecked
            purchaseDatas = (ArrayList<PurchaseData>) savedInstanceState.getSerializable(KEY_PURCHASED_DATA);
            updateAdapter(purchaseDatas);
        }

        GetPurchasesHelper purchasesHelper = holderFragment.getPurchasesHelper();
        if (purchasesHelper != null)
            purchasesHelper.setListener(new PurchasesListenerImpl());

        InAppLoginHelper loginHelper = holderFragment.getLoginHelper();
        if (loginHelper != null)
            loginHelper.setListener(new LoginListenerImpl());

        ConsumeHelper consumeHelper = holderFragment.getConsumeHelper();
        if (consumeHelper != null)
            consumeHelper.setListener(new ConsumeListenerImpl(consumeHelper.getPurchaseToken()));
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
        GetPurchasesHelper purchasesHelper = inAppHelper.getPurchases(new PurchasesListenerImpl());
        holderFragment.setPurchasesHelper(purchasesHelper);
    }

    @Override
    public void onCantConnectToIABService(InAppError error) {
        super.onCantConnectToIABService(error);

        switch (error) {
            case BILLING_RESPONSE_IRANAPPS_NOT_AVAILABLE:
                Util.showAlertDialog(PurchasesActivity.this, error.getMessage(), true);
                break;

            default:
                Util.showAlertDialog(PurchasesActivity.this, error.getMessage(), true);
                break;
        }
    }

    private void updateAdapter(ArrayList<PurchaseData> purchases) {
        if (purchases.size() > 0) {
            PurchasesAdapter adapter = new PurchasesAdapter(PurchasesActivity.this, purchases);
            lvPurchase.setAdapter(adapter);
        } else {
            Util.showAlertDialog(PurchasesActivity.this, "you have not purchased anything yet.", true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (purchaseDatas != null) {
            outState.putSerializable(KEY_PURCHASED_DATA, purchaseDatas);
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

    private class PurchasesListenerImpl implements PurchasesListener {
        @Override
        public void onGotPurchases(ArrayList<PurchaseData> purchases, String continuationToken) {
            if (progressDialog != null)
                progressDialog.dismiss();

            purchaseDatas = purchases;
            updateAdapter(purchases);
        }

        @Override
        public void onFailedGettingPurchases(InAppError error) {
            if (progressDialog != null)
                progressDialog.dismiss();

            if (error.getErrorCode() == InAppKeys.BILLING_RESPONSE_USER_NOT_LOGIN) {
                InAppLoginHelper loginHelper = inAppHelper.loginUser(PurchasesActivity.this, new LoginListenerImpl());
                holderFragment.setLoginHelper(loginHelper);
            } else {
                Util.showAlertDialog(PurchasesActivity.this, error.getMessage(), true);
            }
        }
    }

    private class LoginListenerImpl implements LoginListener {

        @Override
        public void onLoginSucceed() {
            //after than logging should getPurchase process done again
            onConnectedToIABService();
        }

        @Override
        public void onLoginFailed(InAppError error) {
            Util.showAlertDialog(PurchasesActivity.this, error.getMessage(), true);
        }
    }

    private class ConsumeListenerImpl implements ConsumeListener {
        private String purchaseToken;

        public ConsumeListenerImpl(String purchaseToken) {
            this.purchaseToken = purchaseToken;
        }

        @Override
        public void onConsumeSucceed() {
            //TODO the item has been consumed, call getPurchases again to update the list
            Util.showAlertDialog(PurchasesActivity.this, String.format("purchaseToken: %s consumed", purchaseToken), false);
        }

        @Override
        public void onItemNotOwned() {
            Util.showAlertDialog(PurchasesActivity.this, String.format("purchaseToken: %s is not owned by user", purchaseToken), false);
        }

        @Override
        public void onConsumeFailed(InAppError error) {
            Util.showAlertDialog(PurchasesActivity.this, error.getMessage(), false);
        }
    }

}
