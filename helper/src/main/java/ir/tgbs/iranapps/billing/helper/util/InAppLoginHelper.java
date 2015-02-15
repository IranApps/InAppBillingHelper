package ir.tgbs.iranapps.billing.helper.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
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
    private LoginListener loginListener;
    private Activity activity;

    /**
     * creates instanceOf InAppLoginHelper with given parameter<br>
     *
     * @param activity      instance of the activity that has requested the login process
     * @param inAppService  the service that provides communicate to IranApps in-app billing service
     * @param loginListener callback listener for indicating failure and success consume process
     */
    public InAppLoginHelper(Activity activity, IranAppsIabService inAppService, LoginListener loginListener) {
        super(loginListener);
        this.activity = activity;
        this.inAppService = inAppService;
        this.loginListener = loginListener;
    }

    @Override
    public void start() {
        if (inAppService == null) {
            loginListener.onLoginFailed(InAppError.LOCAL_HELPER_NOT_CONNECTED_TO_SERVICE);
            return;
        }

        try {
            Bundle loginResponse = inAppService.getLoginIntent(InAppHelper.IAB_VERSION, InAppHelper.PACKAGE_NAME);

            if (loginResponse.getBoolean(InAppKeys.USER_IS_LOGIN)) {
                loginListener.onLoginSucceed();
            } else {
                PendingIntent loginIntent = loginResponse.getParcelable(InAppKeys.LOGIN_INTENT);
                activity.startIntentSenderForResult(loginIntent.getIntentSender(), LOGIN_REQUEST_CODE, new Intent(), 0, 0, 0);
            }
        } catch (RemoteException | IntentSender.SendIntentException e) {
            e.printStackTrace();
            loginListener.onLoginFailed(InAppError.getError(-1));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                loginListener.onLoginSucceed();
            } else {
                int responseCode = data == null ? InAppKeys.BILLING_RESPONSE_RESULT_ERROR :
                        data.getIntExtra(InAppKeys.RESPONSE_CODE, InAppKeys.BILLING_RESPONSE_RESULT_ERROR);
                loginListener.onLoginFailed(InAppError.getError(responseCode));
            }
        }
    }

}
