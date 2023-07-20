package bd.com.aristo.edcr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import javax.inject.Inject;

import bd.com.aristo.edcr.databinding.ActivityLoginBinding;
import bd.com.aristo.edcr.listener.CheckMarketListener;
import bd.com.aristo.edcr.models.response.LoginResponse;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.networking.RequestServices;
import bd.com.aristo.edcr.utils.ConnectionUtils;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.SharedPrefsUtils;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

import static bd.com.aristo.edcr.utils.DateTimeUtils.FORMAT9;

public class LoginActivity extends AppCompatActivity implements CheckMarketListener {

    private String TAG = "LoginActivity";

    private LoginActivity activity = this;
    private ActivityLoginBinding binding;



    public String REMMEMBER_USER_ID ="rem_userid";
    public String REMMEMBER_PASSWORD ="rem_password";

    @Inject
    Realm r;

    @Inject
    App app;

    private CompositeDisposable mCompositeDisposable;
    public LoadingDialog loadingDialog;// = LoadingDialog.newInstance(this, "Please Wait...");

    @Inject
    APIServices apiServices;
    @Inject
    RequestServices requestServices;


    public static void start(Activity context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        App.getComponent().inject(this);
        initialize();
    }

    public void exportRealmDatabase(){
        try {
            final File file = new File(Environment.getExternalStorageDirectory().getPath().concat("/sample.realm"));
            if (file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }

            r.writeCopyTo(file);
            Toast.makeText(LoginActivity.this, "Success export realm file", Toast.LENGTH_SHORT).show();
        } catch (io.realm.internal.IOException e) {
            r.close();
            e.printStackTrace();
        }
    }

    public void initialize(){

        hideKeyboard(activity);

        mCompositeDisposable = new CompositeDisposable();

        UserModel userModel = r.where(UserModel.class).findFirst();

        if (userModel !=null){
            String token = SharedPrefsUtils.getStringPreference(activity, SharedPrefsUtils.FCM_TOKEN);
            if(!SharedPrefsUtils.getStringPreference(this, StringConstants.PREF_CHECKED_MARKET_DATE).equalsIgnoreCase(DateTimeUtils.getToday(FORMAT9))) {
                //requestServices.checkUserMarket(activity, apiServices, userModel.getLoginID(), userModel.getMarketCode(), userModel.getMPGroup(), token, this);
            } else {
                HomeActivity.start(activity, false);
                finish();
            }
        }

        //Remember me
       String rememberedUserId = SharedPrefsUtils.getStringPreference(activity,REMMEMBER_USER_ID);
       String rememberedPassword = SharedPrefsUtils.getStringPreference(activity,REMMEMBER_PASSWORD);

       if (rememberedUserId!=null && rememberedPassword!=null){
           binding.checkBoxRememberMe.setChecked(true);
           binding.editTextUserId.setText(rememberedUserId);
           binding.editTextPassword.setText(rememberedPassword);
       }else{
           binding.checkBoxRememberMe.setChecked(false);
           binding.editTextUserId.setText("");
           binding.editTextPassword.setText("");
       }
        binding.editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }

        });
        binding.editTextUserId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //if(b)
                //showTooltip(view, "Enter User ID");

            }

        });

        if (ConnectionUtils.isNetworkConnected(activity)){
            binding.connectionInfoTV.setVisibility(View.GONE);
        }else{
            binding.connectionInfoTV.setVisibility(View.VISIBLE);
        }

        String vInfo = " version: "+StringUtils.getVersionName(activity);
        String copyright = getString(R.string.oshni);
        binding.vInfoTV.setText(copyright);
        binding.txtVersion.setText(vInfo);

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = binding.editTextUserId.getText().toString();
                String password = binding.editTextPassword.getText().toString();


                if (TextUtils.isEmpty(userId)){
                    binding.editTextUserId.setError(StringConstants.USER_ID_REQUIRED_MSG);
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    binding.editTextPassword.setError(StringConstants.PASSWORD_REQUIERD_MSG);
                    return;
                }


                if (ConnectionUtils.isNetworkConnected(activity)){
                    handleLogin(userId,password);
                }else{
                    ToastUtils.shortToast(getString(R.string.check_internet));
                }
            }
        });


    }
    public void forceCrash(){
        int d = 50/0;
    }


    public void handleLogin(final String userId, final String password){
        binding.buttonLogin.setText(getString(R.string.please_wait));
        binding.buttonLogin.setEnabled(false);

        String deviceToken = SharedPrefsUtils.getStringPreference(activity,SharedPrefsUtils.FCM_TOKEN);

        MyLog.show(TAG,"HandleLogin- device token:"+deviceToken);

        loadingDialog.show();

        mCompositeDisposable.add(apiServices.login(userId,password)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<LoginResponse>() {
                    @Override
                    public void onComplete() {
                       Log.e(TAG, "OnComplete login");
                        binding.buttonLogin.setText(getString(R.string.login));
                        binding.buttonLogin.setEnabled(true);
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError login: "+e.toString());
                        //ToastUtils.shortToast(StringConstants.SERVER_ERROR_MSG);
                        ToastUtils.displayAlert(activity, e.toString());
                        //forceCrash();
                        loadingDialog.dismiss();
                        binding.buttonLogin.setText(getString(R.string.login));
                        binding.buttonLogin.setEnabled(true);
                    }

                    @Override
                    public void onNext(LoginResponse value) {
                        if (value.getStatus()) {
                            ToastUtils.shortToast(StringConstants.LOGIN_SUCCESS_MSG);
                            //forceCrash();
                            goToDashboard(userId, password, value);
                        }else{
                            ToastUtils.shortToast(StringConstants.LOGIN_FAIL_MSG);
                        }
                    }
                }));
    }

    public void goToDashboard(final String userId, final String password, final LoginResponse value){
        SharedPrefsUtils.setStringPreference(activity, StringConstants.PREF_AUTH_TOKEN, value.getToken());
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm r) {
                final UserModel userModel= new UserModel();
                app.logUser(userId, value.getUserModel().getName());

                r.insertOrUpdate(value.getUserModel());

               if (binding.checkBoxRememberMe.isChecked()){
                   SharedPrefsUtils.setStringPreference(activity,REMMEMBER_USER_ID, userId);
                   SharedPrefsUtils.setStringPreference(activity,REMMEMBER_PASSWORD, password);
               }else{
                   SharedPrefsUtils.setStringPreference(activity,REMMEMBER_USER_ID,null);
                   SharedPrefsUtils.setStringPreference(activity,REMMEMBER_PASSWORD,null);
               }
                SharedPrefsUtils.setBooleanPreference(activity, "isSyncNeed",true);

                //Go to Main activity
                HomeActivity.start(activity, true);

                finish();
            }
        });

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadingDialog = LoadingDialog.newInstance(this, "Please wait...");
        if (ConnectionUtils.isNetworkConnected(activity)){
            binding.connectionInfoTV.setVisibility(View.GONE);
        }else{
            binding.connectionInfoTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable!=null){
            mCompositeDisposable.clear();
        }
    }

    @Override
    public void onError(final String error) {

    }

    @Override
    public void onCheckMarket(final boolean isValid) {
        if(isValid){
            HomeActivity.start(activity, false);
            finish();
        } else {
            SharedPrefsUtils.setBooleanPreference(this, StringConstants.IS_SYNCED_SUCCESS, false);
            getCacheDir().delete();
            r.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.deleteAll();
                }
            });
        }
    }
}
