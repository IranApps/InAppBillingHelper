package ir.tgbs.iranapps.billing.helper.util;

import android.os.Bundle;
import android.os.RemoteException;

import java.util.ArrayList;

import ir.tgbs.iranapps.billing.IranAppsIabService;
import ir.tgbs.iranapps.billing.helper.interfaces.PurchasesListener;
import ir.tgbs.iranapps.billing.helper.model.PurchaseData;

/**
 * to do purchase product process on separated thread
 *
 * @author Shima Zeinali
 * @author Khaled Bakhtiari
 * @since 2015-02-14
 */
public class GetPurchasesHelper extends Thread {

    /**
     * callback listener for response of purchased items
     */
    PurchasesListener listener;

    /**
     * connection to IranApps billing service
     */
    IranAppsIabService inAppService;

    /**
     * the INAPP_CONTINUABLE_TOKEN returned in {@link ir.tgbs.iranapps.billing.helper.interfaces.PurchasesListener#onGotPurchases(java.util.ArrayList, String)}
     * witch can be used to get the next part of purchased items
     */
    String continuationToken;

    /**
     * creates instanceOf purchaseGetter with the given parameters<br>
     *
     * @param inAppService      the service that provides communicate to IranApps in-app billing service
     * @param listener          callback listener for indicating failure and success purchase process
     * @param continuationToken token for verify purchase
     */
    public GetPurchasesHelper(IranAppsIabService inAppService, PurchasesListener listener, String continuationToken) {
        this.inAppService = inAppService;
        this.listener = listener;
        this.continuationToken = continuationToken;
    }

    @Override
    public void run() {
        if (inAppService == null) {
            listener.onFailedGettingPurchases(InAppError.LOCAL_HELPER_NOT_CONNECTED_TO_SERVICE);
            return;
        }

        int responseCode = InAppKeys.BILLING_RESPONSE_RESULT_ERROR;
        try {
            Bundle response = inAppService.getPurchases(InAppHelper.IAB_VERSION, InAppHelper.PACKAGE_NAME, InAppHelper.TYPE_INAPP, continuationToken);

            if (response == null) {
                listener.onFailedGettingPurchases(InAppError.getError(responseCode));
                return;
            }

            responseCode = response.getInt(InAppKeys.RESPONSE_CODE);

            if (responseCode == InAppKeys.RESPONSE_OK) {
                ArrayList<PurchaseData> purchases = new ArrayList<>();
                ArrayList<String> itemList = response.getStringArrayList(InAppKeys.INAPP_PURCHASE_ITEM_LIST);
                ArrayList<String> dataList = response.getStringArrayList(InAppKeys.INAPP_PURCHASE_DATA_LIST);
                ArrayList<String> signatureList = response.getStringArrayList(InAppKeys.INAPP_DATA_SIGNATURE_LIST);

                for (int i = 0; i < itemList.size(); i++) {
                    purchases.add(new PurchaseData(itemList.get(i), dataList.get(i), signatureList.get(i)));
                }

                listener.onGotPurchases(purchases, response.getString(InAppKeys.INAPP_CONTINUATION_TOKEN));
            } else {
                listener.onFailedGettingPurchases(InAppError.getError(responseCode));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            listener.onFailedGettingPurchases(InAppError.LOCAL_EXCEPTION);
        }
    }
}
