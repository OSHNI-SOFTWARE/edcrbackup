package bd.com.aristo.edcr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.listener.ClearAllDataListener;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.models.response.VersionResponse;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.networking.RequestServices;
import bd.com.aristo.edcr.networking.ResponseListener;
import bd.com.aristo.edcr.utils.ConnectionUtils;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.SharedPrefsUtils;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.buttons.AButton;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class SettingsActivity extends AppCompatActivity {

    public static final String TAG = SettingsActivity.class.getSimpleName();

    @BindView(R.id.userNameTV)
    TextView userNameTV;
    @BindView(R.id.vInfoTV)
    ATextView vInfoTV;
    @BindView(R.id.connectionInfoTV)
    TextView connectionInfoTV;
    @BindView(R.id.marketTV)
    TextView marketTV;
    @BindView(R.id.depotTV)
    TextView depotTV;
    @BindView(R.id.changePasswordTV)
    TextView changePasswordTV;
    @BindView(R.id.btnMasterCurrent)
    AButton btnMasterCurrent;
    @BindView(R.id.btnTranCurrent)
    AButton btnTranCurrent;
    @BindView(R.id.btnTranPrev)
    AButton btnTranPrev;
    @BindView(R.id.btnTranNext)
    AButton btnTranNext;
    @BindView(R.id.cardViewSync)
    CardView cardViewSync;

    @BindView(R.id.txtVersionStatus)
    TextView txtVersionStatus;

    static ClearAllDataListener clearAllDataListener1;


    private SettingsActivity activity = this;
    @Inject
    APIServices apiServices;
    @Inject
    Realm r;
    @Inject
    RequestServices requestServices;

    public UserModel userModel;
    DateModel currentDateModel, dateModel, nextDateModel, prevDateModel;
    int month, year;

    public void getUserInfo() {
        userModel = r.where(UserModel.class).findFirst();
        dateModel = DCRUtils.getToday();
        String userName = userModel.getName() + " [" + userModel.getEmployeeNumber() + "]";
        String userMarket = userModel.getProductGroup() + " [" + userModel.getType() + "]";
        String userDepot = "Depot: " + userModel.getDepotId();
        userNameTV.setText(userName);
        marketTV.setText(userMarket);
        depotTV.setText(userDepot);
        String vInfo = " V: " + StringUtils.getVersionName(activity);
        vInfoTV.setText(vInfo);
    }

    public static void start(Context context, ClearAllDataListener clearAllDataListener) {
        clearAllDataListener1 = clearAllDataListener;
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        App.getComponent().inject(this);
        ButterKnife.bind(this);
        getUserInfo();

        month = dateModel.getMonth();
        year = dateModel.getYear();
        setDateModel();
        if (ConnectionUtils.isNetworkConnected(activity)) {
            requestServices.getAppVersion(apiServices, new ResponseListener<VersionResponse>() {
                @Override
                public void onSuccess(List<VersionResponse> valueList) {
                }

                @Override
                public void onSuccess(VersionResponse value) {
                    if(value.getVersionName().equals(StringUtils.getVersionName(activity))){
                        txtVersionStatus.setText("Your app is updated.");
                        txtVersionStatus.setEnabled(false);
                        txtVersionStatus.setTextColor(getResources().getColor(R.color.md_green_700));
                    } else {
                        String version = "Click here to download latest version";
                        txtVersionStatus.setTextColor(getResources().getColor(R.color.md_red_700));
                        txtVersionStatus.setText(version);
                        txtVersionStatus.setEnabled(true);
                    }
                }

                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailed() {
                    txtVersionStatus.setVisibility(View.GONE);
                }
            });
            connectionInfoTV.setVisibility(View.GONE);
            cardViewSync.setVisibility(View.VISIBLE);
        } else {
            connectionInfoTV.setVisibility(View.VISIBLE);
            cardViewSync.setVisibility(View.GONE);
        }


    }

    @OnClick(R.id.changePasswordTV)
    void onClickPasswordChange() {
        ChangePasswordActivity.start(activity);
    }


    @OnClick(R.id.btnMasterCurrent)
    void onClickUpdateMasterForCurrent() {
        if (ConnectionUtils.isNetworkConnected(activity)) {
            //requestServices.syncMaster(this, apiServices, userModel.getMarketCode(), userModel.getUserId(), r);
        } else {
            ToastUtils.shortToast(getString(R.string.check_internet));
        }

    }

    @OnClick(R.id.btnTranCurrent)
    void onClickUpdateTranForCurrent() {
        if (ConnectionUtils.isNetworkConnected(activity)) {
            //requestServices.syncTransaction(this, apiServices, dateModel, userModel.getUserId(), r);
        } else {
            ToastUtils.shortToast(getString(R.string.check_internet));
        }

    }

    @OnClick(R.id.btnTranNext)
    void onClickUpdateTranForNext() {
        if (ConnectionUtils.isNetworkConnected(activity)) {
            //requestServices.syncTransaction(this, apiServices, nextDateModel, userModel.getUserId(), r);
        } else {
            ToastUtils.shortToast(getString(R.string.check_internet));
        }

    }

    @OnClick(R.id.btnTranPrev)
    void onClickUpdateTranForPrev() {
        if (ConnectionUtils.isNetworkConnected(activity)) {
            //requestServices.syncTransaction(this, apiServices, prevDateModel, userModel.getUserId(), r);
        } else {
            ToastUtils.shortToast(getString(R.string.check_internet));
        }

    }

    @OnClick(R.id.btnDeleteAll)
    void onClickDeleteAll() {
        displayConfirmationPopup();

    }

    @OnClick(R.id.txtVersionStatus)
    void onClickUpdateVersion(){
        if (ConnectionUtils.isNetworkConnected(activity)) {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://www.squarepharma.com.bd/edcr"));
            if (viewIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(viewIntent);
            } else {
                ToastUtils.longToast("Failed to load!!");
            }
        } else {
            ToastUtils.shortToast(getString(R.string.check_internet));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ConnectionUtils.isNetworkConnected(activity)) {
            connectionInfoTV.setVisibility(View.GONE);
            cardViewSync.setVisibility(View.VISIBLE);
        } else {
            connectionInfoTV.setVisibility(View.VISIBLE);
            cardViewSync.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }

    public void setDateModel() {
        //dateModel = new DateModel(1,month,  year, 1, 1);
        currentDateModel = new DateModel(1,month,  year, 1, 1);
        prevDateModel = new DateModel(1,month,  year, 1, 1);
        nextDateModel = new DateModel(1,month,  year, 1, 1);

        if(month == 12){
            nextDateModel.setMonth(1);
            nextDateModel.setYear(year + 1);
            prevDateModel.setMonth(11);
        } else if(dateModel.getMonth() == 1){
            prevDateModel.setMonth(12);
            prevDateModel.setYear(year - 1);
            nextDateModel.setMonth(2);
        } else {
            prevDateModel.setMonth(month - 1);
            nextDateModel.setMonth(month + 1);
        }

        btnTranCurrent.setText(DateTimeUtils.getFullMonthForInt( currentDateModel.getMonth()));
        btnTranPrev.setText(DateTimeUtils.getFullMonthForInt( prevDateModel.getMonth()));
        btnTranNext.setText(DateTimeUtils.getFullMonthForInt( nextDateModel.getMonth()));
    }

    public void displayConfirmationPopup(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(StringConstants.DELETE_ALL_ALERT);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                deleteAll();

            }
        });

        alert.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

    public void deleteAll(){
        SharedPrefsUtils.setBooleanPreference(this, StringConstants.IS_SYNCED_SUCCESS, false);
        getCacheDir().delete();
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
                finish();
                clearAllDataListener1.onClearSuccess();
            }
        });
    }
}
