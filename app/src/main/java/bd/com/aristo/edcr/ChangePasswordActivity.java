package bd.com.aristo.edcr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import bd.com.aristo.edcr.databinding.ActivityChangePasswordBinding;
import bd.com.aristo.edcr.models.response.LoginResponse;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.ConnectionUtils;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.SharedPrefsUtils;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class ChangePasswordActivity extends AppCompatActivity {

    public static final String TAG = ChangePasswordActivity.class.getSimpleName();
    public String REMMEMBER_PASSWORD ="rem_password";
    private ActivityChangePasswordBinding binding;

    private ChangePasswordActivity activity = this;

    @Inject
    APIServices apiServices;
    @Inject
    Realm r;

    private CompositeDisposable mCompositeDisposable;

    public UserModel userModel;
    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }

    public static void start(Context context){
        Intent intent = new Intent(context,ChangePasswordActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        App.getComponent().inject(this);
        mCompositeDisposable = new CompositeDisposable();
        getUserInfo();

        binding.buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = binding.editTextOldPassword.getText().toString().trim();
                String newPassword = binding.editTextNewPassword.getText().toString().trim();
                String confirmPassword = binding.editTextConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(oldPassword)){
                    binding.editTextOldPassword.setError("Old password is required!");
                    return;
                }

                if (TextUtils.isEmpty(newPassword)){
                    binding.editTextNewPassword.setError("New password is required!");
                    return;
                }

                if (TextUtils.isEmpty(confirmPassword)){
                    binding.editTextConfirmPassword.setError("Confirm password is required!");
                    return;
                }

                if (!newPassword.equals(confirmPassword)){
                    binding.editTextConfirmPassword.setError("Confirm password is not matched!");
                    return;
                }


                if (ConnectionUtils.isNetworkConnected(activity)){
                    binding.connectionInfoTV.setVisibility(View.GONE);
                    postNewPassword(oldPassword,newPassword);

                }else{
                    binding.connectionInfoTV.setVisibility(View.VISIBLE);
                }
            }
        });


    }


    public void postNewPassword(final String oldPassword,final String newPassword){
        ToastUtils.shortToast(""+oldPassword+" "+newPassword);

        String deviceToken = SharedPrefsUtils.getStringPreference(activity,SharedPrefsUtils.FCM_TOKEN);

        MyLog.show(TAG,"HandleChangePasss- device token:"+deviceToken);
        displayProgress();

        mCompositeDisposable.add(apiServices.changePassword(userModel.getLoginID(),oldPassword,newPassword, deviceToken)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<LoginResponse>() {
                    @Override
                    public void onComplete() {
                        Log.e(TAG, "OnComplete login");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError login: "+e.toString());
                        hideProgress();
                        ToastUtils.shortToast(StringConstants.SERVER_ERROR_MSG);

                    }

                    @Override
                    public void onNext(LoginResponse value) {
                        hideProgress();
                        if (value.getStatus()!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                            SharedPrefsUtils.setStringPreference(activity, REMMEMBER_PASSWORD, newPassword);
                            displayAlert("Success", " Password Changed Successfully!");

                        }else{
                            ToastUtils.shortToast(StringConstants.PASSWORD_CHANGE_FAIL_MSG);
                        }
                    }
                }));

    }


    public void displayAlert(final String title,final String msg){
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
         alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                finish();
            }
        });
        alert.show();
    }


    private ProgressDialog progressDialog;
    private void displayProgress(){
        if (progressDialog==null)
            progressDialog = new ProgressDialog(activity);

        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Password is changing.");
        progressDialog.setCancelable(false);

        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideProgress(){
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.hide();
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ConnectionUtils.isNetworkConnected(activity)){
            binding.connectionInfoTV.setVisibility(View.GONE);
        }else{
            binding.connectionInfoTV.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        hideProgress();

        if (mCompositeDisposable!=null){
            mCompositeDisposable.clear();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }



}
