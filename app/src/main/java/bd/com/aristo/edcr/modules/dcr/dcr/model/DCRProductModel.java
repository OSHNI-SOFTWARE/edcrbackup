package bd.com.aristo.edcr.modules.dcr.dcr.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by monir.sobuj on 6/13/17.
 */

public class DCRProductModel extends RealmObject {

    @PrimaryKey
    private long id;
    private long dcrID;
    private String productID;
    private int quantity;
    private int type; // 0 for Selective, 1 for Sample, 2 for Gift
    private boolean isPlanned;

    @Ignore
    public static String COL_ID = "id", COL_PRODUCT_ID = "productID", COL_DCR_ID = "dcrID", COL_QUANTITY = "quantity",
            COL_TYPE = "type", COL_IS_PLANNED = "getPlannedCount";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDcrID() {
        return dcrID;
    }

    public void setDcrID(long dcrID) {
        this.dcrID = dcrID;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isPlanned() {
        return isPlanned;
    }

    public void setPlanned(boolean planned) {
        isPlanned = planned;
    }

    @Override
    public String toString() {
        return "DCRProductModel{" +
                "id=" + id +
                ", dcrID=" + dcrID +
                ", productID='" + productID + '\'' +
                ", quantity=" + quantity +
                ", type=" + type +
                ", getPlannedCount=" + isPlanned +
                '}';
    }
}
