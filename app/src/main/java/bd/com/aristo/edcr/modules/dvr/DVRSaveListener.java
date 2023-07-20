package bd.com.aristo.edcr.modules.dvr;

import java.io.Serializable;

/**
 * Created by monir.sobuj on 8/26/2018.
 */

public interface DVRSaveListener extends Serializable {
    void onSave(int pos, String docId);
}
