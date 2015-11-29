package ir.tgbs.iranapps.billing.helper.interfaces;


import ir.tgbs.iranapps.billing.helper.util.InAppError;

/**
 * callback listener for consume process which indicates the failure or success of the process
 *
 * @author Shima Zeinali
 * @author Khaled Bakhtiari
 * @since 2015-02-14
 */
public interface ConsumeListener extends BaseInAppListener {

    /**
     * this method is called when the consume process ic successfully completed.
     */
    void onConsumeSucceed();

    /**
     * this method is called when the requested product is not owned by user
     */
    void onItemNotOwned();

    /**
     * this method is called whenever the process is failed.
     *
     * @param error the occurred error
     */
    void onConsumeFailed(InAppError error);
}