package ir.tgbs.iranapps.billing.helper.util;

import android.util.Log;

/**
 * @author Shima Zeinali
 * @author Khaled Bakhtiari
 * @since 2015-02-14
 */
public enum InAppError {
    /**
     * occurs when user cancels the process <br> errorCode is 1
     */
    BILLING_RESPONSE_RESULT_USER_CANCELED("The process was canceled by the user", InAppKeys.BILLING_RESPONSE_RESULT_USER_CANCELED),

    /**
     * occurs when request is not supported by this version of the API <br> errorCode is 3
     */
    BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE("Your request is not supported by this version of the API", InAppKeys.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE),

    /**
     * occurs when the requested product is not registered in the system <br> errorCode is 4
     */
    BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE("The requested product is not registered in the system", InAppKeys.BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE),

    /**
     * developer errors can occur in the following circumstances :<br>
     * Sending invalid parameters to methods <br>
     * Application not registered in Iranapps <br>
     * Not including permissions in the Manifest file <br> errorCode is 5
     */
    BILLING_RESPONSE_RESULT_DEVELOPER_ERROR("Developer errors can occur in the following circumstances :\n" +
            "\n" +
            "Sending invalid parameters to methods\n" +
            "Application not registered in Iranapps\n" +
            "Not including permissions in the Manifest file", InAppKeys.BILLING_RESPONSE_RESULT_DEVELOPER_ERROR),

    /**
     * other error that happen in IranApps in-app billing service <br> errorCode is 6
     */
    BILLING_RESPONSE_RESULT_ERROR("operation failed.", InAppKeys.BILLING_RESPONSE_RESULT_ERROR),

    /**
     * occurs when the product has already benn purchased(to buy it again you must consume it first). <br> errorCode is 7
     */
    BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED("occurs when the product has already benn purchased(to buy it again you must consume it first).", InAppKeys.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED),

    /**
     * User is not the owner of the product so it cannot be consumed. <br> errorCode is 8
     * occurs when the product owner is not able to Consume it is not so <br> errorCode is 8
     */
    BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED("User is not the owner of the product so it cannot be consumed.", InAppKeys.BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED),

    /**
     * occurs when the user is not logged in to IranApps. <br> errorCode is 9
     */
    BILLING_RESPONSE_USER_NOT_LOGIN("User has not logged in to Iranapps.", InAppKeys.BILLING_RESPONSE_USER_NOT_LOGIN),

    /**
     * occurs when IranApps app is not installed on the device <br> errorCode is 10
     */
    BILLING_RESPONSE_IRANAPPS_NOT_AVAILABLE("IranApps app is not installed", InAppKeys.BILLING_RESPONSE_IRANAPPS_NOT_AVAILABLE),

    /**
     * occurs when helper can't connect to IranApps billing service <br> errorCode is 11
     */
    LOCAL_CANT_CONNECT_TO_IAB_SERVICE("Helper can't connect to the in-app billing service", InAppKeys.LOCAL_CANT_CONNECT_TO_IAB_SERVICE),

    /**
     * occurs when helper can't connect to IranApps billing service <br> errorCode is 12
     */
    LOCAL_HELPER_NOT_CONNECTED_TO_SERVICE("helper isn't connected to the service", InAppKeys.LOCAL_HELPER_NOT_CONNECTED_TO_SERVICE),

    /**
     * any error or exception that happens inside the helper <br> errorCode is 13
     */
    LOCAL_EXCEPTION("any error or exception that happens inside the helper", InAppKeys.LOCAL_EXCEPTION),

    /**
     * if an in-app billing service provider throws a error that is not defined you will get this error.
     */
    UNKNOWN_ERROR("no such error is defined", -1);

    private String message;
    private int code;

    private InAppError(String text, int code) {
        this.message = text;
        this.code = code;
    }

    /**
     * finds the {@link InAppError} by the given error code <br>
     * if error code doesn't exist throws a {@link RuntimeException}.<p>
     * you can use the message of the error to understand the error by {@link InAppError#getMessage()}
     *
     * @param errorCode error code of the occurred error (if doesn't exist throws an exception)
     * @return {@link InAppError} representing the given error code.
     */
    public static InAppError getError(int errorCode) {
        switch (errorCode) {
            case 1:
                return BILLING_RESPONSE_RESULT_USER_CANCELED;

            case 3:
                return BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE;

            case 4:
                return BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE;

            case 5:
                return BILLING_RESPONSE_RESULT_DEVELOPER_ERROR;

            case 6:
                return BILLING_RESPONSE_RESULT_ERROR;

            case 7:
                return BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED;

            case 8:
                return BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED;

            case 9:
                return BILLING_RESPONSE_USER_NOT_LOGIN;

            case 10:
                return BILLING_RESPONSE_IRANAPPS_NOT_AVAILABLE;

            default:
                Log.e("SuperIabHelper", "an undefined error was caught from in-app billing provider service, errorCode: '" + errorCode + "'");
                UNKNOWN_ERROR.code = errorCode;
                return UNKNOWN_ERROR;
        }
    }

    /**
     * @return the message of this error
     */
    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return code;
    }
}

