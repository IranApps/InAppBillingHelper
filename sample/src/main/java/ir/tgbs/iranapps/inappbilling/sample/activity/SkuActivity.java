package ir.tgbs.iranapps.inappbilling.sample.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.tgbs.iranapps.billing.helper.interfaces.SkuDetailListener;
import ir.tgbs.iranapps.billing.helper.model.Product;
import ir.tgbs.iranapps.billing.helper.util.InAppError;
import ir.tgbs.iranapps.billing.helper.util.SkuDetailGetter;
import ir.tgbs.iranapps.inappbilling.sample.R;
import ir.tgbs.iranapps.inappbilling.sample.adapter.SkuAdapter;
import ir.tgbs.iranapps.inappbilling.sample.util.Util;

/**
 * activity class that shows SkuDetail list
 */
public class SkuActivity extends BaseInAppActivity implements SkuDetailListener {
    private static final String KEY_PRODUCTS_DATA = "ProductsData";

    @Nullable
    private ArrayList<Product> products;
    private ListView lvSku;
    @Nullable
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sku);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        lvSku = (ListView) findViewById(R.id.lv_sku);

        lvSku.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SkuActivity.this, BuyActivity.class);
                intent.putExtra("sku", (Product) parent.getItemAtPosition(position));
                startActivity(intent);
                intent = new Intent(SkuActivity.this, BuyActivity.class);
                intent.putExtra("sku", (Product) parent.getItemAtPosition(position));
                startActivity(intent);
            }
        });

        if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_PRODUCTS_DATA)) {
            progressDialog = ProgressDialog.show(this, null, "getting sku details");
        } else {
            //noinspection unchecked
            products = (ArrayList<Product>) savedInstanceState.getSerializable(KEY_PRODUCTS_DATA);
            updateAdapter(products);
        }

        SkuDetailGetter skuDetailGetter = holderFragment.getSkuDetailGetter();
        if (skuDetailGetter != null) {
            skuDetailGetter.setListener(this);
        }
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

        //define products ID
        ArrayList<String> skus = new ArrayList<>();
        skus.add("product_1");
        skus.add("product_2");
        skus.add("product_3");
        skus.add("product_4");

        SkuDetailGetter skuDetailGetter = inAppHelper.getSkuDetails(skus, this);
        holderFragment.setSkuDetailGetter(skuDetailGetter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (products != null) {
            outState.putSerializable(KEY_PRODUCTS_DATA, products);
        }
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

    @Override
    public void onGotSkus(ArrayList<Product> productDetails) {
        if (progressDialog != null)
            progressDialog.dismiss();

        products = productDetails;

        updateAdapter(products);
    }

    private void updateAdapter(ArrayList<Product> products) {
        if (products.size() > 0) {
            SkuAdapter adapter = new SkuAdapter(SkuActivity.this, products);
            lvSku.setAdapter(adapter);
        } else {
            Util.showAlertDialog(SkuActivity.this, "you don't have any product go to IranApps developer panel and add your products", true);
        }
    }

    @Override
    public void onFailedGettingSkus(InAppError error) {
        if (progressDialog != null)
            progressDialog.dismiss();
        Util.showAlertDialog(SkuActivity.this, error.getMessage(), true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}
