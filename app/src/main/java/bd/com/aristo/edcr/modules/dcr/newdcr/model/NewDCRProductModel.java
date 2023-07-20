package bd.com.aristo.edcr.modules.dcr.newdcr.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Tariqul.Islam on 6/13/17.
 */

public class NewDCRProductModel extends RealmObject {

    @PrimaryKey
    private long id;
    private long newDcrId;
    private String productID;
    private String productName;
    private int count;
    private int flag; //0 for product, 1 for Sample, 2 for Gift

    @Ignore
    public static String COL_ID = "id",
            COL_NEW_DCR_ID = "newDcrId",
            COL_PRODUCT_ID = "productID",
            COL_COUNT = "count",
            COL_PRODUCT_NAME = "productName",
            COL_FLAG = "flag";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNewDcrId() {
        return newDcrId;
    }

    public void setNewDcrId(long newDcrId) {
        this.newDcrId = newDcrId;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "NewDCRProductModel{" +
                "id=" + id +
                ", newDcrId=" + newDcrId +
                ", productID='" + productID + '\'' +
                ", productName='" + productName + '\'' +
                ", count=" + count +
                ", flag=" + flag +
                '}';
    }
}
