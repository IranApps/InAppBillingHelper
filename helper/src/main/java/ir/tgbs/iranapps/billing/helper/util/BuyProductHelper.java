package ir.tgbs.iranapps.billing.helper.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.RemoteException;

import ir.tgbs.iranapps.billing.IranAppsIabService;
import ir.tgbs.iranapps.billing.helper.interfaces.BuyProductListener;
import ir.tgbs.iranapps.billing.helper.interfaces.ConsumeListener;
import ir.tgbs.iranapps.billing.helper.model.PurchaseItem;

/**
 * helper class for buying a product
 *
 * @author Shima Zeinali
 * @author Khaled Bakhtiari
 * @since 2015-02-14
 */
public class BuyProductHelper extends InAppRequestHelper {
    /**
     * activity request code for making the purchase intent.
     */
    public static final int BUY_REQUEST_CODE = 1000;

    /**
     * product purchase callback listener
     */
    BuyProductListener listener;

    /**
     * instance of the activity that has requested the purchase process
     */
    Activity activity;

    /**
     * id(sku) of the requested product to be purchased
     */
    String productId;

    /**
     * whether this purchase is consumable or not
     */
    boolean consumable;

    /**
     * optional string that can later be used to check the validity of the purchase data
     */
    String developerPayload;

    /**
     * connection to IranApps billing service
     */
    private IranAppsIabService inAppService;

    /**
     * @param activity         instance of the activity that has requested the purchase process
     * @param inAppService     the service that provides communicate to IranApps in-app billing service
     * @param productId        id(sku) of the requested product to be purchased
     * @param consumable       whether this purchase can be consumed or not
     * @param developerPayload optional parameter that can later be used to check the validity of the purchase data
     * @param listener         purchase callback listener
     */
    public BuyProductHelper(Activity activity, IranAppsIabService inAppService, String productId,
                            boolean consumable, String developerPayload, BuyProductListener listener) {
        super(listener);
        this.activity = activity;
        this.inAppService = inAppService;
        this.listener = listener;
        this.productId = productId;
        this.consumable = consumable;
        this.developerPayload = developerPayload;
    }

    @Override
    public void start() {
        if (inAppService == null) {
            listener.onBuyProductFailed(InAppError.LOCAL_HELPER_NOT_CONNECTED_TO_SERVICE);
            return;
        }

        try {
            Bundle buyResponse = inAppService.getBuyIntent(InAppHelper.IAB_VERSION, InAppHelper.PACKAGE_NAME, productId, InAppHelper.TYPE_INAPP, developerPayload);

            int responseCode = buyResponse.getInt(InAppKeys.RESPONSE_CODE);

            if (responseCode == InAppKeys.RESPONSE_OK) {
                PendingIntent buyIntent = buyResponse.getParcelable(InAppKeys.BUY_INTENT);
                activity.startIntentSenderForResult(buyIntent.getIntentSender(), BUY_REQUEST_CODE, new Intent(), 0, 0, 0);
            } else {
                listener.onBuyProductFailed(InAppError.getError(responseCode));
            }
        } catch (RemoteException | IntentSender.SendIntentException e) {
            e.printStackTrace();
            listener.onBuyProductFailed(InAppError.LOCAL_EXCEPTION);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BUY_REQUEST_CODE) {
            int responseCode = data == null ? InAppKeys.BILLING_RESPONSE_RESULT_ERROR :
                    data.getIntExtra(InAppKeys.RESPONSE_CODE, InAppKeys.BILLING_RESPONSE_RESULT_ERROR);

            if (responseCode != InAppKeys.RESPONSE_OK) {
                listener.onBuyProductFailed(InAppError.getError(responseCode));
                return;
            }

            final PurchaseItem purchaseItem = PurchaseItem.newInstanceNoException(data.getStringExtra(InAppKeys.INAPP_PURCHASE_DATA));

            if (consumable) {
                new ConsumeHelper(inAppService, purchaseItem.purchaseToken, new ConsumeListener() {
                    @Override
                    public void onConsumeSucceed() {
                        listener.onBuyProductSucceed(purchaseItem);
                    }

                    @Override
                    public void onItemNotOwned() {
                        //this can't happen here because we just bought the product
                        throw new RuntimeException("onItemNotOwned can't happen right after buying the product");
                    }

                    @Override
                    public void onConsumeFailed(InAppError errorCode) {
                        listener.onBuyProductFailed(errorCode);
                    }
                }).start();
            } else {
                listener.onBuyProductSucceed(purchaseItem);
            }
        }
    }
}