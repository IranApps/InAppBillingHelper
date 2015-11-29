package ir.tgbs.iranapps.billing.helper.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
     * @param continuationToken to be set as null for the first call, if the number of owned skus are too many,
     *                          a continuationToken is returned in the response bundle. This method can
     *                          be called again with the continuation token to get the next set of owned skus.
     */
    public GetPurchasesHelper(IranAppsIabService inAppService, PurchasesListener listener, String continuationToken) {
        this.inAppService = inAppService;
        this.listener = listener;
        this.continuationToken = continuationToken;
    }

    @Override
    public void run() {
        if (inAppService == null) {
            postFailedGettingPurchases(InAppError.LOCAL_HELPER_NOT_CONNECTED_TO_SERVICE);
            return;
        }

        int responseCode = InAppKeys.BILLING_RESPONSE_RESULT_ERROR;
        try {
            Bundle response = inAppService.getPurchases(InAppHelper.IAB_VERSION, InAppHelper.PACKAGE_NAME, InAppHelper.TYPE_INAPP, continuationToken);

            if (response == null) {
                postFailedGettingPurchases(InAppError.getError(responseCode));
                return;
            }

            responseCode = response.getInt(InAppKeys.RESPONSE_CODE);

            if (responseCode == InAppKeys.RESPONSE_OK) {
                ArrayList<PurchaseData> purchases = new ArrayList<>();
                ArrayList<String> itemList = response.getStringArrayList(InAppKeys.INAPP_PURCHASE_ITEM_LIST);
                ArrayList<String> dataList = response.getStringArrayList(InAppKeys.INAPP_PURCHASE_DATA_LIST);
                ArrayList<String> signatureList = response.getStringArrayList(InAppKeys.INAPP_DATA_SIGNATURE_LIST);

                if (itemList != null && dataList != null && signatureList != null) {
                    for (int i = 0; i < itemList.size(); i++) {
                        purchases.add(new PurchaseData(itemList.get(i), dataList.get(i), signatureList.get(i)));
                    }
                }

                postGotPurchases(purchases, response.getString(InAppKeys.INAPP_CONTINUATION_TOKEN));
            } else {
                postFailedGettingPurchases(InAppError.getError(responseCode));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            postFailedGettingPurchases(InAppError.LOCAL_EXCEPTION);
        }
    }

    public void setListener(PurchasesListener listener) {
        this.listener = listener;
    }

    public PurchasesListener getListener() {
        return listener;
    }

    public String getContinuationToken() {
        return continuationToken;
    }

    private void postGotPurchases(final ArrayList<PurchaseData> purchases, final String continuationToken) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listener.onGotPurchases(purchases, continuationToken);
            }
        });
    }

    private void postFailedGettingPurchases(final InAppError error) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listener.onFailedGettingPurchases(error);
            }
        });
    }
}
