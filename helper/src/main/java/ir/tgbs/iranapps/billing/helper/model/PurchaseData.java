package ir.tgbs.iranapps.billing.helper.model;

import java.io.Serializable;

/**
 * @author Shima Zeinali
 * @author Khaled Bakhtiari
 * @since 2015-02-14
 */
public class PurchaseData implements Serializable {

    /**
     * sku of this purchase
     */
    public String sku;

    /**
     * information of this purchase
     */
    public PurchaseItem purchaseItem;

    /**
     * signature of this purchase
     */
    public String signature;

    /**
     * creates an instance of PurchaseData with the given parameters.<br>
     */
    public PurchaseData(String sku, String data, String signature) {
        this.sku = sku;
        this.purchaseItem = PurchaseItem.newInstanceNoException(data);
        this.signature = signature;
    }
}
