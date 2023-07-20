package bd.com.aristo.edcr.modules.dcr.newdcr;

import bd.com.aristo.edcr.modules.dcr.newdcr.model.NewDCRModel;

/**
 * Created by monir.sobuj on 9/10/2018.
 */

public interface NewDCRListListener {
    void onSuccess(String remarks, NewDCRModel newDCRModel);
    void onFailed(String remarks);
}
