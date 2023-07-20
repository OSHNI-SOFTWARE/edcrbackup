package bd.com.aristo.edcr.modules.dcr.newdcr;

import bd.com.aristo.edcr.modules.dcr.newdcr.fragment.DCRProductsModel;

/**
 * Created by monir.sobuj on 9/10/2018.
 */

public interface ViewPagerListener {

    void updateDCRGiftProduct(DCRProductsModel dcrProductsModel);
    void updateDCRSampleProduct(DCRProductsModel dcrProductsModel);
    void updateDCRSelectedProduct(DCRProductsModel dcrProductsModel);
}
