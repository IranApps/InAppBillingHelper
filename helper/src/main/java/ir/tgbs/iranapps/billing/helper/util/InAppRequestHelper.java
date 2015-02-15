package ir.tgbs.iranapps.billing.helper.util;

import android.content.Intent;

import ir.tgbs.iranapps.billing.helper.interfaces.BaseInAppListener;

/**
 * abstract request helper for in-app billing requests that start an activity in IranApps
 * and receive the response in {@link android.app.Activity#onActivityResult(int, int, android.content.Intent)}
 *
 * @author Shima Zeinali
 * @author Khaled Bakhtiari
 * @since 2015-02-14
 */
public abstract class InAppRequestHelper {

    /**
     * callback listener of this request <br>
     * other requests pass an extend of {@link ir.tgbs.iranapps.billing.helper.interfaces.BaseInAppListener}
     */
    private BaseInAppListener baseInAppListener;

    /**
     * @param baseInAppListener callback listener of the request
     */
    public InAppRequestHelper(BaseInAppListener baseInAppListener) {
        this.baseInAppListener = baseInAppListener;
    }

    /**
     * starts the process of this helper.
     */
    public abstract void start();

    /**
     * this method should be called inside
     * {@link android.app.Activity#onActivityResult(int, int, android.content.Intent)}
     * or if its in a fragment
     * {@link android.app.Fragment#onActivityResult(int, int, android.content.Intent)} <p>
     * handles the response of the request and returns the response on {@link #baseInAppListener}
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     */
    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    public BaseInAppListener getListener() {
        return baseInAppListener;
    }
}
