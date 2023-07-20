package bd.com.aristo.edcr.modules.wp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by altaf.sil on 3/18/18.
 */

public class WPDetailListModel {

    @SerializedName("DetailList")
    @Expose
    private List<WPDoctorsServerModel> wpDoctorsServerModelList;

    @SerializedName("DetailSL")
    @Expose
    private int detailSL;

    @SerializedName("ProductCode")
    @Expose
    private String productCode;

    @SerializedName("ProductName")
    @Expose
    private String productName;

    @SerializedName("ItemType")
    @Expose
    private String itemType;

    @SerializedName("DayNumber")
    @Expose
    private String dayNumber;

    public List<WPDoctorsServerModel> getWpDoctorsServerModelList() {
        return wpDoctorsServerModelList;
    }

    public void setWpDoctorsServerModelList(List<WPDoctorsServerModel> wpDoctorsServerModelList) {
        this.wpDoctorsServerModelList = wpDoctorsServerModelList;
    }

    public int getDetailSL() {
        return detailSL;
    }

    public void setDetailSL(int detailSL) {
        this.detailSL = detailSL;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }


    public String getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(String dayNumber) {
        this.dayNumber = dayNumber;
    }

    @Override
    public String toString() {
        return "WPDetailListModel{" +
                "wpDoctorsServerModelList=" + wpDoctorsServerModelList +
                ", detailSL=" + detailSL +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", itemType='" + itemType + '\'' +
                ", dayNumber='" + dayNumber + '\'' +
                '}';
    }
}
