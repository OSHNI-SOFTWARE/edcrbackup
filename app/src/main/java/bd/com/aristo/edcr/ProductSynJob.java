package bd.com.aristo.edcr;

import android.app.job.JobParameters;
import android.app.job.JobService;

import javax.inject.Inject;

import bd.com.aristo.edcr.listener.JobsListener;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.networking.RequestServices;
import bd.com.aristo.edcr.utils.ConnectionUtils;
import io.realm.Realm;

public class ProductSynJob extends JobService implements JobsListener {

    @Inject
    RequestServices requestServices;
    @Inject
    Realm r;
    @Inject
    APIServices apiServices;
    UserModel userModel;
    public boolean setUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
        return userModel != null;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        App.getComponent().inject(this);
        syncProduct(params);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    public void syncProduct(JobParameters params){
        if(ConnectionUtils.isNetworkConnected(getApplicationContext()) && setUserInfo()) {
            requestServices.syncProductJob(getApplicationContext(), apiServices, userModel.getUserId(), r, this, params);
        } else {
            jobFinished(params, true);
        }
    }

    @Override
    public void onFinish(JobParameters params) {
        jobFinished(params, true);
    }
}
