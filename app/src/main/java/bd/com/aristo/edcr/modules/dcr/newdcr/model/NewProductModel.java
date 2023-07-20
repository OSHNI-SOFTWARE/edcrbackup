package bd.com.aristo.edcr.modules.dcr.newdcr.model;

/**
 * Created by monir.sobuj on 2/14/2018.
 */

public class NewProductModel {

    private String productID;
    private String productName;
    private int count;
    private int flag; //2 for Sample, 3 for Gift

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
        return "NewProductModel{" +
                "productID='" + productID + '\'' +
                ", productName='" + productName + '\'' +
                ", count=" + count +
                ", flag=" + flag +
                '}';
    }
}
