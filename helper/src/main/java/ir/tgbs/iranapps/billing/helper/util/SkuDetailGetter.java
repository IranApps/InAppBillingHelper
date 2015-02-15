package ir.tgbs.iranapps.billing.helper.util;

import android.os.Bundle;
import android.os.RemoteException;

import org.json.JSONException;

import java.util.ArrayList;

import ir.tgbs.iranapps.billing.IranAppsIabService;
import ir.tgbs.iranapps.billing.helper.interfaces.SkuDetailListener;
import ir.tgbs.iranapps.billing.helper.model.Product;

/**
 * runs getSkuDetail process on separated thread
 *
 * @author Shima Zeinali
 * @author Khaled Bakhtiari
 * @since 2015-02-14
 */
public class SkuDetailGetter extends Thread {
    SkuDetailListener listener;
    IranAppsIabService inAppService;
    Bundle skusBundle;

    /**
     * creates instanceOf SkuDetailGetter with the given parameters<br>
     * also informs list of skuNames in bundle
     *
     * @param inAppService the service that provides communicate to IranApps in-app billing service
     * @param skuNames     list of skuName
     * @param listener     callback listener for indicating failure or success getSkuDetail process
     */
    public SkuDetailGetter(IranAppsIabService inAppService, ArrayList<String> skuNames, SkuDetailListener listener) {
        this.inAppService = inAppService;
        skusBundle = new Bundle();
        //put skuNames in skuBundle
        skusBundle.putStringArrayList(InAppKeys.ITEM_ID_LIST, skuNames);
        this.listener = listener;
    }

    @Override
    public void run() {
        if (inAppService == null) {
            listener.onFailedGettingSkus(InAppError.LOCAL_HELPER_NOT_CONNECTED_TO_SERVICE);
            return;
        }

        int responseCode = InAppKeys.BILLING_RESPONSE_RESULT_ERROR;
        try {

            Bundle response = inAppService.getSkuDetails(InAppHelper.IAB_VERSION, InAppHelper.PACKAGE_NAME, InAppHelper.TYPE_INAPP, skusBundle);

            if (response == null) {
                listener.onFailedGettingSkus(InAppError.getError(responseCode));
                return;
            }

            responseCode = response.getInt(InAppKeys.RESPONSE_CODE);

            if (responseCode == InAppKeys.RESPONSE_OK) {
                ArrayList<Product> products = new ArrayList<>();
                for (String product : response.getStringArrayList(InAppKeys.DETAILS_LIST)) {
                    products.add(new Product(product));
                }
                listener.onGotSkus(products);
            } else {
                listener.onFailedGettingSkus(InAppError.getError(responseCode));
            }
        } catch (RemoteException | JSONException e) {
            e.printStackTrace();
            listener.onFailedGettingSkus(InAppError.LOCAL_EXCEPTION);
        }
    }
}
