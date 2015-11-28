package ir.tgbs.iranapps.billing.helper.interfaces;


import ir.tgbs.iranapps.billing.helper.model.PurchaseItem;
import ir.tgbs.iranapps.billing.helper.util.InAppError;

/**
 * callback listener for buying process which indicates the failure or success of the process
 *
 * @author Shima Zeinali
 * @author Khaled Bakhtiari
 * @since 2015-02-14
 */
public interface BuyProductListener extends BaseInAppListener {

    /**
     * this method is called when the buying process ic successfully completed.
     *
     * @param purchaseItem data of the bought product
     */
    void onBuyProductSucceed(PurchaseItem purchaseItem);

    /**
     * this method is called whenever the process is failed.
     *
     * @param error the occurred error
     */
    void onBuyProductFailed(InAppError error);
}
