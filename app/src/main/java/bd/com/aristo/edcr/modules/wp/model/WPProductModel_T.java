package bd.com.aristo.edcr.modules.wp.model;

/**
 * Created by monir.sobuj on 6/13/17.
 */

public class WPProductModel_T {

    private String productID;
    private String name;
    private int quantity;
    private int type; // 1 for Selective, 2 for Sample, 3 for Gift


    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "WPProductModel_T{" +
                ", productID='" + productID + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", type=" + type +
                '}';
    }
}
