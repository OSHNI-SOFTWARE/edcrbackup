package bd.com.aristo.edcr.fcm.notification;

/**
 * Created by monir.sobuj on 14/07/2019.
 */

public class FCMMessage {

    private String message_id;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    @Override
    public String toString() {
        return "FCMMessage{" +
                "message_id='" + message_id + '\'' +
                '}';
    }
}
