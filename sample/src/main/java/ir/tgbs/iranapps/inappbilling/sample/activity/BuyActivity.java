package ir.tgbs.iranapps.inappbilling.sample.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ir.tgbs.iranapps.billing.helper.interfaces.BuyProductListener;
import ir.tgbs.iranapps.billing.helper.model.Product;
import ir.tgbs.iranapps.billing.helper.model.PurchaseItem;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        product = (Product) getIntent().getSerializableExtra("sku");

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_price = (TextView) findViewById(R.id.tv_price);
        Button bBuy = (Button) findViewById(R.id.b_buy);

        tv_price.setText(product.price + " " + getString(R.string.toman));
        tv_title.setText(product.title);

        bBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inAppHelper.buyProduct(product.productId, null, false, new BuyProductListener() {
                    @Override
                    public void onBuyProductSucceed(PurchaseItem purchaseItem) {
                        Util.showAlertDialog(BuyActivity.this, purchaseItem.productId + " was purchased", true);
                    }

                    @Override
                    public void onBuyProductFailed(InAppError error) {
                        if (error != InAppError.BILLING_RESPONSE_RESULT_USER_CANCELED)
                            Util.showAlertDialog(BuyActivity.this, error.getMessage(), false);
                    }
                });
            }
        });
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