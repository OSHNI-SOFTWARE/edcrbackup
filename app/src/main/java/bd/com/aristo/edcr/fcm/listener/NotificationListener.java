package bd.com.aristo.edcr.fcm.listener;

import bd.com.aristo.edcr.models.db.NotificationModel;

/**
 * Created by altaf.sil on 1/18/18.
 */

public interface NotificationListener {
    void onClickTag(NotificationModel notificationModel);
}
