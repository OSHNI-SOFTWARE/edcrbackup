package bd.com.aristo.edcr.modules.dvr;

import java.util.List;

import bd.com.aristo.edcr.modules.dvr.model.IDoctorsModel;

/**
 * Created by monir.sobuj on 8/26/2018.
 */

public interface DVRSelectionListener {
    void onSelect(List<IDoctorsModel> iDoctorsModels);
}
