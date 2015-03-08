package ir.tgbs.iranapps.inappbilling.sample.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.tgbs.iranapps.billing.helper.interfaces.SkuDetailListener;
import ir.tgbs.iranapps.billing.helper.model.Product;
import ir.tgbs.iranapps.billing.helper.util.InAppError;
import ir.tgbs.iranapps.inappbilling.sample.R;
import ir.tgbs.iranapps.inappbilling.sample.adapter.SkuAdapter;
import ir.tgbs.iranapps.inappbilling.sample.util.Util;

/**
 * activity class that shows SkuDetail list
 */
public class SkuActivity extends BaseInAppActivity {


    ListView lvSku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sku);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvSku = (ListView) findViewById(R.id.lv_sku);

        lvSku.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SkuActivity.this, BuyActivity.class);
                intent.putExtra("sku", (Product) parent.getItemAtPosition(position));
                startActivity(intent);
            }
        });
    }

    /**
     * if service binds, this method executes.
     * then gets skuDetail list.
     *
     * @see ir.tgbs.iranapps.billing.helper.interfaces.SkuDetailListener
     */
    @Override
    public void onConnectedToIABService() {
        super.onConnectedToIABService();

        final AlertDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("getting sku details");
        progressDialog.show();

        //define products ID
        ArrayList<String> skus = new ArrayList<>();
        skus.add("product_1");
        skus.add("product_2");
        skus.add("product_3");
        skus.add("product_4");

        inAppHelper.getSkuDetails(skus, new SkuDetailListener() {
            @Override
            public void onGotSkus(final ArrayList<Product> productDetails) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();

                        if (productDetails.size() > 0) {
                            SkuAdapter adapter = new SkuAdapter(SkuActivity.this, productDetails);
                            lvSku.setAdapter(adapter);
                        } else {
                            Util.showAlertDialog(SkuActivity.this, "you don't have any product got to IranApps developer panel and add your products", true);
                        }
                    }
                });
            }

            @Override
            public void onFailedGettingSkus(final InAppError errorCode) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        Util.showAlertDialog(SkuActivity.this, errorCode.getMessage(), true);
                    }
                });
            }
        });
    }

    /**
     * this method execute when service not can binds
     *
     * @param error code of occurred error
     */
    @Override
    public void onCantConnectToIABService(InAppError error) {
        super.onCantConnectToIABService(error);


        switch (error) {
            case BILLING_RESPONSE_IRANAPPS_NOT_AVAILABLE:
                Util.showAlertDialog(SkuActivity.this, error.getMessage(), true);
                break;

            default:
                Util.showAlertDialog(SkuActivity.this, error.getMessage(), true);
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
