package bd.com.aristo.edcr.listener;

/**
 * Created by monir.sobuj on 10/1/2018.
 */

public interface CheckMarketListener {
    void onCheckMarket(boolean isValid);
    void onError(String error);
}
