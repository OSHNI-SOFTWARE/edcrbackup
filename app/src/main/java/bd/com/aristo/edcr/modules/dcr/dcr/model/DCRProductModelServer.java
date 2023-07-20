package bd.com.aristo.edcr.modules.dcr.dcr.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by monir.sobuj on 6/13/17.
 */

public class DCRProductModelServer {

    @SerializedName("ProductCode")
    private String productID;
    @SerializedName("Quantity")
    private int quantity;
    @SerializedName("ItemType")
    private String type; // 0 for Selective, 1 for Sample, 2 for Gift

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DCRProductModel{" +
                ", productID='" + productID + '\'' +
                ", quantity=" + quantity +
                ", type=" + type +
                '}';
    }
}
