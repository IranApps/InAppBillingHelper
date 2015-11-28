package ir.tgbs.iranapps.billing.helper.interfaces;

import java.util.ArrayList;

import ir.tgbs.iranapps.billing.helper.model.PurchaseData;
import ir.tgbs.iranapps.billing.helper.util.InAppError;


/**
 * callback listener for purchase process which indicates the failure or success of the process
 *
 * @author Shima Zeinali
 * @author Khaled Bakhtiari
 * @since 2015-02-14
 */
public interface PurchasesListener extends BaseInAppListener {

    /**
     * this method is called when the purchase process ic successfully completed.
     *
     * @param purchases         data of the purchased products
     * @param continuationToken In case the number of purchased items is more than 100,
     *                          this key will hold a value that you could use to receive the rest of purchase data
     */
    void onGotPurchases(ArrayList<PurchaseData> purchases, String continuationToken);

    /**
     * this method is called whenever the process is failed.
     *
     * @param error the occurred error
     */
    void onFailedGettingPurchases(InAppError error);

}
