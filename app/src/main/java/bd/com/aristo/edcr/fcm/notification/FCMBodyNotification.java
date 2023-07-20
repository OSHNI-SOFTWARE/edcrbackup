package bd.com.aristo.edcr.fcm.notification;

/**
 * Created by monir.sobuj on 14/07/2019.
 */

public class FCMBodyNotification {

    private String body;
    private String title;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "FCMBodyNotification{" +
                "body='" + body + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
