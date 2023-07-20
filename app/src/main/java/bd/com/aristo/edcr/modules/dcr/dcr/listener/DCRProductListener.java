package bd.com.aristo.edcr.modules.dcr.dcr.listener;

import java.util.List;

import bd.com.aristo.edcr.modules.dcr.dcr.model.IDCRProductsModel;

/**
 * Created by monir.sobuj on 9/5/2018.
 */

public interface DCRProductListener {
    List<IDCRProductsModel> getDCRRefreshList(int type);
}
