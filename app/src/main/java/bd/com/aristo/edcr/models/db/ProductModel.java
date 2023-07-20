package bd.com.aristo.edcr.models.db;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by monir.sobuj on 5/24/17.
 */

public class ProductModel extends RealmObject {

    @SerializedName("ProductCode")
    private String code;

    @SerializedName("ProductName")
    private String name;

    @SerializedName("PackSize")
    private String packSize;

    @SerializedName("GenericName")
    private String generic;

    @SerializedName("TotalQty")
    private int quantity;

    @SerializedName("BalanceQty")
    private int balance;

    @SerializedName("ExecuteQty")
    private int execute;

    @SerializedName("MonthNumber")
    private int month;

    @SerializedName("Year")
    private int year;

    @SerializedName("ItemFor")
    private String itemFor;

    @SerializedName("ItemType")
    private String itemType;

    @Ignore
    public static final String COL_ITEM_TYPE = "itemType", COL_CODE="code", COL_MONTH = "month", COL_YEAR = "year", COL_ITEM_FOR = "itemFor";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code   = code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getGeneric() {
        return generic;
    }

    public void setGeneric(String generic) {
        this.generic = generic;
    }

    public String getItemFor() {
        return itemFor;
    }

    public void setItemFor(String itemFor) {
        this.itemFor = itemFor;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getExecute() {
        return execute;
    }

    public void setExecute(int execute) {
        this.execute = execute;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return "ProductModel{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", packSize='" + packSize + '\'' +
                ", generic='" + generic + '\'' +
                ", quantity=" + quantity +
                ", balance=" + balance +
                ", itemFor='" + itemFor + '\'' +
                '}';
    }
}