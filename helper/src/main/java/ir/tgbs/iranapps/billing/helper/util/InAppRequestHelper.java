package ir.tgbs.iranapps.billing.helper.util;

import android.app.Activity;
import android.content.Intent;

/**
 * abstract request helper for in-app billing requests that start an activity in IranApps
 * and receive the response in {@link android.app.Activity#onActivityResult(int, int, android.content.Intent)}
 *
 * @author Shima Zeinali
 * @author Khaled Bakhtiari
 * @since 2015-02-14
 */
public abstract class InAppRequestHelper {

    public InAppRequestHelper() {
    }

    /**
     * starts the process of this helper.
     *
     * @param activity instance of the activity that has requested this operation
     */
    public abstract void start(Activity activity);

    /**
     * this method should be called inside
     * {@link android.app.Activity#onActivityResult(int, int, android.content.Intent)}
     * or if its in a fragment
     * {@link android.app.Fragment#onActivityResult(int, int, android.content.Intent)} <p>
     * handles the response of the request.
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
}
