package bd.com.aristo.edcr.fcm.notification;

import java.util.List;

/**
 * Created by monir.sobuj on 14/07/2019.
 */

public class FCMResponse {
    private String multicast_id;
    private int success;
    private int failure;
    private int canonical_ids;
    private List<FCMMessage> results;

    public String getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(String multicast_id) {
        this.multicast_id = multicast_id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getCanonical_ids() {
        return canonical_ids;
    }

    public void setCanonical_ids(int canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    public List<FCMMessage> getResults() {
        return results;
    }

    public void setResults(List<FCMMessage> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "FCMResponse{" +
                "multicast_id='" + multicast_id + '\'' +
                ", success=" + success +
                ", failure=" + failure +
                ", canonical_ids=" + canonical_ids +
                ", results=" + results +
                '}';
    }
}
