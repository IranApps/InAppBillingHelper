package ir.tgbs.iranapps.inappbilling.sample.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import ir.tgbs.iranapps.billing.helper.util.InAppHelper;

public class Util {
    public static void showAlertDialog(final Activity activity, String message, final boolean finishActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (finishActivity)
                    activity.finish();
            }
        });
        builder.setMessage(message);

        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();

        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (finishActivity)
                    activity.finish();
            }
        });
    }

    public static void showInAppBillingNotSupported(Activity activity, boolean finishActivity) {
        showAlertDialog(activity, "InAppBilling version: " + InAppHelper.IAB_VERSION + "is not supported.", finishActivity);
    }
}
