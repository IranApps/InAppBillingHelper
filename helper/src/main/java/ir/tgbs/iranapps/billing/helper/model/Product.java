package ir.tgbs.iranapps.billing.helper.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * @author Shima Zeinali
 * @author Khaled Bakhtiari
 * @since 2015-02-14
 */
public class Product implements Serializable {
    /**
     * description of sku
     */
    public String description;

    /**
     * price of sku
     */
    public String price;

    /**
     * title of sku
     */
    public String title;

    /**
     * product'id of sku
     */
    public String productId;

    /**
     * creates an instance of Sku with the given parameters.<br>
     * gets string Product data and convert to json object
     *
     * @param jsonProduct sku string
     */
    public Product(String jsonProduct) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonProduct);
        productId = jsonObject.getString("productId");
        price = jsonObject.getString("price");
        title = jsonObject.getString("title");
        description = jsonObject.getString("description");
    }
}
