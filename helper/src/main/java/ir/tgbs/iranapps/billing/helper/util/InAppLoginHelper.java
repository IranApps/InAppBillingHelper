package ir.tgbs.iranapps.billing.helper.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

import ir.tgbs.iranapps.billing.IranAppsIabService;
import ir.tgbs.iranapps.billing.helper.interfaces.LoginListener;

/**
 * @author Shima Zeinali
 * @author Khaled Bakhtiari
 * @since 2015-02-14
 */
public class InAppLoginHelper extends InAppRequestHelper {
    public static final int LOGIN_REQUEST_CODE = 1001;
    private IranAppsIabService inAppService;
    private LoginListener listener;

    /**
     * creates instanceOf InAppLoginHelper with given parameter<br>
     *
     * @param inAppService  the service that provides communicate to IranApps in-app billing service
     * @param listener callback listener for indicating failure and success consume process
     */
    public InAppLoginHelper(IranAppsIabService inAppService, LoginListener listener) {
        super();
        this.inAppService = inAppService;
        this.listener = listener;
    }

    @Override
    public void start(Activity activity) {
        if (inAppService == null) {
            postLoginFailed(InAppError.LOCAL_HELPER_NOT_CONNECTED_TO_SERVICE);
            return;
        }

        try {
            Bundle loginResponse = inAppService.getLoginIntent(InAppHelper.IAB_VERSION, InAppHelper.PACKAGE_NAME);

            if (loginResponse.getBoolean(InAppKeys.USER_IS_LOGIN)) {
                postLoginSucceed();
            } else {
                PendingIntent loginIntent = loginResponse.getParcelable(InAppKeys.LOGIN_INTENT);
                assert loginIntent != null;//TODO send error instead of assertion
                activity.startIntentSenderForResult(loginIntent.getIntentSender(), LOGIN_REQUEST_CODE, new Intent(), 0, 0, 0);
            }
        } catch (RemoteException | IntentSender.SendIntentException e) {
            e.printStackTrace();
            postLoginFailed(InAppError.getError(-1));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                postLoginSucceed();
            } else {
                int responseCode = data == null ? InAppKeys.BILLING_RESPONSE_RESULT_ERROR :
                        data.getIntExtra(InAppKeys.RESPONSE_CODE, InAppKeys.BILLING_RESPONSE_RESULT_ERROR);
                postLoginFailed(InAppError.getError(responseCode));
            }
        }
    }

    public void setListener(LoginListener listener) {
        this.listener = listener;
    }

    public LoginListener getListener() {
        return listener;
    }

    private void postLoginSucceed() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listener.onLoginSucceed();
            }
        });
    }

    private void postLoginFailed(final InAppError error) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listener.onLoginFailed(error);
            }
        });

    }
}
