package bd.com.aristo.edcr.modules.dcr;

/**
 * Created by monir.sobuj on 9/20/2018.
 */

public interface DCRSenderListener {

    void onError(String remarks);
    void onSuccess(String remarks);
}
