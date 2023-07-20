package bd.com.aristo.edcr.modules.dcr.dcr.model;

/**
 * Created by monir.sobuj on 6/13/17.
 */

public class DCRProductModelForSave {

    private long id;
    private long dcrID;
    private String productID;
    private int quantity;
    private int type;
    private boolean isPlanned;

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
        return "DCRProductModelForSave{" +
                "id=" + id +
                ", dcrID=" + dcrID +
                ", productID='" + productID + '\'' +
                ", quantity=" + quantity +
                ", type=" + type +
                ", isPlanned=" + isPlanned +
                '}';
    }
}
