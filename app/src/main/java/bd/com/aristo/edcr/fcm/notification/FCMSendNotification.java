package bd.com.aristo.edcr.fcm.notification;

import android.util.Log;

import java.util.Calendar;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.models.db.TokenModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.networking.RequestServices;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 14/07/2019.
 */

public class FCMSendNotification {
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    FCMPostBody fcmPostBody;
    @Inject
    FCMServices fcmServices;
    @Inject
    RequestServices requestServices;
    @Inject
    APIServices apiServices;
    @Inject
    Realm r;
    public FCMSendNotification(String marketName, String title, String monthName, String userId, int month, int year){
        App.getComponent().inject(this);
        TokenModel tokenTM = r.where(TokenModel.class).equalTo(TokenModel.COL_DESIGNATION, "TM").findFirst();
        TokenModel tokenRSM = r.where(TokenModel.class).equalTo(TokenModel.COL_DESIGNATION, "RSM").findFirst();
        if(tokenTM != null){
            //TP is uploaded by Market Name
            String detail = title + " of "+ monthName +" is uploaded by " + marketName;
            String title1 = title + " uploaded";
            String strTag = title.replace(" ", "_");
            FCMPostBody fcmPostBody = new FCMPostBody();
            fcmPostBody.setTo(tokenTM.getToken());
            fcmPostBody.setCollapse_key("type_a");
            FCMBodyData fcmBodyData = new FCMBodyData();
            fcmBodyData.setCount("1");
            fcmBodyData.setDatetime(Calendar.getInstance().getTime().toString());
            fcmBodyData.setDetail(detail);
            fcmBodyData.setTag(strTag);
            fcmBodyData.setTitle(title);
            fcmPostBody.setData(fcmBodyData);
            FCMBodyNotification fcmBodyNotification = new FCMBodyNotification();
            fcmBodyNotification.setBody(detail);
            fcmBodyNotification.setTitle(title);
            fcmPostBody.setNotification(fcmBodyNotification);
            this.fcmPostBody = fcmPostBody;
            requestServices.postNotification(apiServices, userId, title, title1, detail, year, month, userId);
            sendNotification();
        }
        if(title.equalsIgnoreCase("Change TP")) {
            if (tokenRSM != null) {
                //TP is uploaded by Market Name
                String detail = title + " of " + monthName + " is uploaded by " + marketName;
                String title1 = title + " uploaded";
                String strTag = title.replace(" ", "_");
                FCMPostBody fcmPostBody = new FCMPostBody();
                fcmPostBody.setTo(tokenRSM.getToken());
                fcmPostBody.setCollapse_key("type_a");
                FCMBodyData fcmBodyData = new FCMBodyData();
                fcmBodyData.setCount("1");
                fcmBodyData.setDatetime(Calendar.getInstance().getTime().toString());
                fcmBodyData.setDetail(detail);
                fcmBodyData.setTag(strTag);
                fcmBodyData.setTitle(title);
                fcmPostBody.setData(fcmBodyData);
                FCMBodyNotification fcmBodyNotification = new FCMBodyNotification();
                fcmBodyNotification.setBody(detail);
                fcmBodyNotification.setTitle(title);
                fcmPostBody.setNotification(fcmBodyNotification);
                //requestServices.postNotification(apiServices, userId, title, title1, detail, year, month, userId);
                sendNotificationToRsm(fcmPostBody);
            }
        }
    }


    public void sendNotification(){

        mCompositeDisposable.add(fcmServices.sendNotification(fcmPostBody) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<FCMResponse>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SEND NOTI", "onError: "+e);

                    }

                    @Override
                    public void onNext(FCMResponse fcmResponse) {
                        Log.d("SEND NOTI", "onNext: "+fcmResponse.getSuccess());
                    }
                }));
    }

    public void sendNotificationToRsm(FCMPostBody fcmPostBody1){

        mCompositeDisposable.add(fcmServices.sendNotification(fcmPostBody1) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<FCMResponse>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SEND NOTI", "onError: "+e);

                    }

                    @Override
                    public void onNext(FCMResponse fcmResponse) {
                        Log.d("SEND NOTI", "onNext: "+fcmResponse.getSuccess());
                    }
                }));
    }

}
