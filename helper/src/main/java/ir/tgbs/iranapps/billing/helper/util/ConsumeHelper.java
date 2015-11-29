package ir.tgbs.iranapps.billing.helper.util;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

import ir.tgbs.iranapps.billing.IranAppsIabService;
import ir.tgbs.iranapps.billing.helper.interfaces.ConsumeListener;

/**
 * helper class for consuming a owned product. <p>
 * gives back the response on the given listener {@link ConsumeListener}
 *
 * @author Shima Zeinali
 * @author Khaled Bakhtiari
 * @since 2015-02-14
 */
public class ConsumeHelper extends Thread {
    /**
     * callback listener
     */
    private ConsumeListener listener;

    /**
     * connection to IranApps billing service
     */
    private IranAppsIabService inAppService;

    /**
     * token of the bought product to be consumed
     */
    private String purchaseToken;

    /**
     * @param inAppService  the service that provides communicate to IranApps billing service
     * @param purchaseToken purchase token of the bought product
     * @param listener      callback listener for indicating success or failure of consuming process
     */
    public ConsumeHelper(IranAppsIabService inAppService, String purchaseToken, ConsumeListener listener) {
        this.listener = listener;
        this.inAppService = inAppService;
        this.purchaseToken = purchaseToken;
    }

    /**
     * consume thread is started. <br>
     * calls IranAppsIabService.consumePurchase(int, String, String) to consume the product
     */
    @Override
    public void run() {
        if (inAppService == null) {
            postConsumeFailed(InAppError.LOCAL_HELPER_NOT_CONNECTED_TO_SERVICE);
            return;
        }

        try {
            int responseCode = inAppService.consumePurchase(InAppHelper.IAB_VERSION, InAppHelper.PACKAGE_NAME, purchaseToken);
            switch (responseCode) {
                case InAppKeys.RESPONSE_OK:
                    postConsumeSucceed();
                    break;

                case InAppKeys.BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED:
                    postItemNotOwned();
                    break;

                default:
                    postConsumeFailed(InAppError.getError(responseCode));
                    break;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            postConsumeFailed(InAppError.LOCAL_EXCEPTION);
        }
    }

    public void setListener(ConsumeListener listener) {
        this.listener = listener;
    }

    public ConsumeListener getListener() {
        return listener;
    }

    public String getPurchaseToken() {
        return purchaseToken;
    }

    private void postConsumeSucceed() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listener.onConsumeSucceed();
            }
        });
    }

    private void postItemNotOwned() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listener.onItemNotOwned();
            }
        });
    }

    private void postConsumeFailed(final InAppError error) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listener.onConsumeFailed(error);
            }
        });
    }
}