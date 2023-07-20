package bd.com.aristo.edcr.fcm.notification;

/**
 * Created by monir.sobuj on 14/07/2019.
 */

public class FCMPostBody {

    private String to;
    private String collapse_key;
    private FCMBodyNotification notification;
    private FCMBodyData data;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCollapse_key() {
        return collapse_key;
    }

    public void setCollapse_key(String collapse_key) {
        this.collapse_key = collapse_key;
    }

    public FCMBodyNotification getNotification() {
        return notification;
    }

    public void setNotification(FCMBodyNotification notification) {
        this.notification = notification;
    }

    public FCMBodyData getData() {
        return data;
    }

    public void setData(FCMBodyData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FCMPostBody{" +
                "to='" + to + '\'' +
                ", collapse_key='" + collapse_key + '\'' +
                ", notification=" + notification +
                ", data=" + data +
                '}';
    }
}
