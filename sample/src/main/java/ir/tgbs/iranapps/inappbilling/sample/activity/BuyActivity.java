package ir.tgbs.iranapps.inappbilling.sample.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ir.tgbs.iranapps.billing.helper.interfaces.BuyProductListener;
import ir.tgbs.iranapps.billing.helper.model.Product;
import ir.tgbs.iranapps.billing.helper.model.PurchaseItem;
import ir.tgbs.iranapps.billing.helper.util.BuyProductHelper;
import ir.tgbs.iranapps.billing.helper.util.InAppError;
import ir.tgbs.iranapps.inappbilling.sample.R;
import ir.tgbs.iranapps.inappbilling.sample.util.Util;

/**
 * activity class that shows buy page
 */
public class BuyActivity extends BaseInAppActivity {
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        product = (Product) getIntent().getSerializableExtra("sku");

        if (product == null)
            throw new RuntimeException("product object is not provided in activity's intent");

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_price = (TextView) findViewById(R.id.tv_price);
        Button bBuy = (Button) findViewById(R.id.b_buy);

        tv_price.setText(String.format("%s %s", product.price, getString(R.string.toman)));
        tv_title.setText(product.title);

        bBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyProduct();
            }
        });

        BuyProductHelper buyProductHelper = holderFragment.getBuyProductHelper();
        if (buyProductHelper != null) {
            buyProductHelper.setListener(new BuyListener());
        }
    }

    private void buyProduct() {
        BuyProductHelper buyProductHelper = inAppHelper.buyProduct(BuyActivity.this, product.productId, null, false, new BuyListener());
        holderFragment.setBuyProductHelper(buyProductHelper);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class BuyListener implements BuyProductListener {

        @Override
        public void onBuyProductSucceed(PurchaseItem purchaseItem) {
            Util.showAlertDialog(BuyActivity.this, purchaseItem.productId + " was purchased", true);
        }

        @Override
        public void onBuyProductFailed(InAppError error) {
            if (error != InAppError.BILLING_RESPONSE_RESULT_USER_CANCELED)
                Util.showAlertDialog(BuyActivity.this, error.getMessage(), false);
        }
    }
}