package bd.com.aristo.edcr.modules.gwds;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by altaf.sil on 1/4/18.
 */

public class GWDServerModel {
    @SerializedName("ProductCode")
    @Expose
    private String ProductCode;

    @SerializedName("DetailList")
    @Expose
    private List<GWDServerDoctorModel> DetailList;

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        this.ProductCode = productCode;
    }

    public List<GWDServerDoctorModel> getList() {
        return DetailList;
    }

    public void setList(List<GWDServerDoctorModel> list) {
        this.DetailList = list;
    }
}
