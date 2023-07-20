package bd.com.aristo.edcr.modules.wp.model;

import java.util.List;

/**
 * Created by monir.sobuj on 6/13/17.
 */

public class WPModelForSend {

    private String ProductCode;
    private List<WPDoctorsModelForSend> DetailList;

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public List<WPDoctorsModelForSend> getDetailList() {
        return DetailList;
    }

    public void setDetailList(List<WPDoctorsModelForSend> detailList) {
        DetailList = detailList;
    }

    @Override
    public String toString() {
        return "WPModelForSend{" +
                "ProductCode='" + ProductCode + '\'' +
                ", DetailList=" + DetailList +
                '}';
    }
}
