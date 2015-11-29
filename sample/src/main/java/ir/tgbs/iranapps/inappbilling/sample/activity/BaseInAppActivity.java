package ir.tgbs.iranapps.inappbilling.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import ir.tgbs.iranapps.billing.helper.util.BuyProductHelper;
import ir.tgbs.iranapps.billing.helper.util.ConsumeHelper;
import ir.tgbs.iranapps.billing.helper.util.GetPurchasesHelper;
import ir.tgbs.iranapps.billing.helper.util.InAppError;
import ir.tgbs.iranapps.billing.helper.util.InAppHelper;
import ir.tgbs.iranapps.billing.helper.util.InAppLoginHelper;
import ir.tgbs.iranapps.billing.helper.util.SkuDetailGetter;

public class BaseInAppActivity extends AppCompatActivity implements InAppHelper.InAppHelperListener {
    protected InAppHelper inAppHelper;
    protected InAppHolderFragment holderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        holderFragment = (InAppHolderFragment) fm.findFragmentByTag("InAppHolderFragmentTag");
        if (holderFragment == null) {
            inAppHelper = new InAppHelper(this, this);
            holderFragment = new InAppHolderFragment();
            fm.beginTransaction().add(holderFragment, "InAppHolderFragmentTag").commit();
            holderFragment.setHelper(inAppHelper);
        } else {
            inAppHelper = holderFragment.getHelper();
        }
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
            inAppHelper.onActivityDestroy(this);
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

    public static class InAppHolderFragment extends Fragment {
        private InAppHelper helper;
        private SkuDetailGetter skuDetailGetter;
        private BuyProductHelper buyProductHelper;
        private GetPurchasesHelper purchasesHelper;
        private InAppLoginHelper loginHelper;
        private ConsumeHelper consumeHelper;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        public BuyProductHelper getBuyProductHelper() {
            return buyProductHelper;
        }

        public void setBuyProductHelper(BuyProductHelper buyProductHelper) {
            this.buyProductHelper = buyProductHelper;
        }

        @Nullable
        public SkuDetailGetter getSkuDetailGetter() {
            return skuDetailGetter;
        }

        public void setSkuDetailGetter(SkuDetailGetter skuDetailGetter) {
            this.skuDetailGetter = skuDetailGetter;
        }

        public InAppHelper getHelper() {
            return helper;
        }

        public void setHelper(InAppHelper helper) {
            this.helper = helper;
        }

        public GetPurchasesHelper getPurchasesHelper() {
            return purchasesHelper;
        }

        public void setPurchasesHelper(GetPurchasesHelper purchasesHelper) {
            this.purchasesHelper = purchasesHelper;
        }

        public InAppLoginHelper getLoginHelper() {
            return loginHelper;
        }

        public void setLoginHelper(InAppLoginHelper loginHelper) {
            this.loginHelper = loginHelper;
        }

        public ConsumeHelper getConsumeHelper() {
            return consumeHelper;
        }

        public void setConsumeHelper(ConsumeHelper consumeHelper) {
            this.consumeHelper = consumeHelper;
        }
    }
}
