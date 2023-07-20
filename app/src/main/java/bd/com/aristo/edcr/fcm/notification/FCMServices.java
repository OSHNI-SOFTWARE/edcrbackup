package bd.com.aristo.edcr.fcm.notification;



import bd.com.aristo.edcr.utils.constants.StringConstants;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by monir.sobuj on 5/25/17.
 */

public interface FCMServices {

    String TAG = FCMServices.class.getSimpleName();



    @Headers({"Authorization: key=" + StringConstants.SERVER_KEY, "Content-Type:application/json"})
    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMPostBody body);
}
