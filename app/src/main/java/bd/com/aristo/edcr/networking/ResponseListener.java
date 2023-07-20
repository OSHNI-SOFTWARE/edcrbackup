package bd.com.aristo.edcr.networking;

import java.util.List;

/**
 * Created by monir.sobuj on 9/26/2018.
 */

public interface ResponseListener<T> {
    void onSuccess(List<T> valueList);
    void onSuccess(T value);
    void onSuccess();
    void onFailed();
}
