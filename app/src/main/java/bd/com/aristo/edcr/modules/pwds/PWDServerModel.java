package bd.com.aristo.edcr.modules.pwds;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by altaf.sil on 1/4/18.
 */

public class PWDServerModel {
    @SerializedName("ProductCode")
    @Expose
    private String ProductCode;

    @SerializedName("DetailList")
    @Expose
    private List<PWDServerDoctorModel> DetailList;

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        this.ProductCode = productCode;
    }

    public List<PWDServerDoctorModel> getList() {
        return DetailList;
    }

    public void setList(List<PWDServerDoctorModel> list) {
        this.DetailList = list;
    }
}
