package bd.com.aristo.edcr.modules.dcr.accompany;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mikepenz.materialize.util.KeyboardUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.response.ResponseDetail;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import bd.com.aristo.edcr.utils.ui.texts.AnEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 6/5/17.
 */

public class AccompanyActivity extends AppCompatActivity {

    private final String TAG = AccompanyActivity.class.getSimpleName();

    @Inject
    APIServices apiServices;
    @Inject
    Realm r;

    @BindView(R.id.rvAccompany)
    RecyclerView rv;
    @BindView(R.id.btnAddAccompany)
    ATextView btnAddAccompany;

    @BindView(R.id.editTextMPOCode)
    AnEditText editTextMpoCode;

    private CompositeDisposable mCompositeDisposable;

    FastItemAdapter<AccompanyModel> fastAdapter;

    List<AccompanyModel> accompanyModelList = new ArrayList<>();
    LoadingDialog loadingDialog;

    public UserModel userModel;
    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }


    public AccompanyActivity() {
    }

    public static AccompanyActivity newInstance() {
        return new AccompanyActivity();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().inject(this);
        setContentView(R.layout.fragment_add_accompany);
        ButterKnife.bind(this);
        loadingDialog = LoadingDialog.newInstance(this, "Please Wait...");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getUserInfo();

        fastAdapter = new FastItemAdapter<>();
        accompanyModelList.clear();

        //setTitle("Add Accompany");

        btnAddAccompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editTextMPOCode = editTextMpoCode.getText().toString();
                if (!TextUtils.isEmpty(editTextMPOCode)){
                    if (!userModel.getLoginID().equalsIgnoreCase(editTextMPOCode)){
                        searchMpoInformation(editTextMPOCode);
                    }else{
                        ToastUtils.shortToast("Please enter other Employee ID");
                    }
                }else{
                    ToastUtils.shortToast(StringConstants.EMPLOYEE_ID_REQ);
                }

            }
        });

        refreshList();
    }


    public void searchMpoInformation(final String mpoCode){
        MyLog.show(TAG,"Got mpoCode:"+mpoCode);

        KeyboardUtil.hideKeyboard(this);

        btnAddAccompany.setClickable(false);
        btnAddAccompany.setText(getString(R.string.searching));
        loadingDialog.show();
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(apiServices.getMpoInfo(mpoCode)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<AccompanyServerModel>>() {
                    @Override
                    public void onComplete() {
                        MyLog.show(TAG,"onComplete");
                        dismissLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        btnAddAccompany.setClickable(true);
                        btnAddAccompany.setText("Add");

                        MyLog.show(TAG,"onError:"+e.toString());
                        ToastUtils.shortToast(StringConstants.SERVER_ERROR_MSG);
                        dismissLoading();
                    }

                    @Override
                    public void onNext(final ResponseDetail<AccompanyServerModel> value) {
                        btnAddAccompany.setClickable(true);
                        btnAddAccompany.setText("Add");
                        MyLog.show(TAG, "onNext: "+value);
                        if (value.getStatus()!=null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                            if (value.getDataModelList().size()>0){
                                showMpoInfo(value.getDataModelList().get(0).getMpoCode(),
                                        value.getDataModelList().get(0).getMpoName(),
                                        value.getDataModelList().get(0).getDesignation());//not available yet at the api services
                            }else{
                                showNotFoundAlert(mpoCode);
                            }
                        }else{
                            showNotFoundAlert(mpoCode);
                        }
                    }
                }));



    }

    public void showMpoInfo(final String mpoID,final String mpoName,final String mpoDesignation){

        String mpoInfo ="Name: "+mpoName + "\n"+
                         "ID: "+mpoID + "\n"+
                         "Designation:" + mpoDesignation;

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Accompany With");
        alert.setMessage(mpoInfo);
        alert.setCancelable(false);
        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                editTextMpoCode.setText("");
                addMPO(mpoID,mpoName,mpoDesignation);
               // EventBus.getDefault().post(new AccompanyEvent(mpoID));
              //  getFragmentManager().popBackStack();

            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        editTextMpoCode.setText("");
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

    public void showNotFoundAlert(final String mpoID){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Search Result!");
        alert.setMessage("Not Found! \nDo you want to add this as a new id?");
        alert.setCancelable(false);
        alert.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        editTextMpoCode.setText("");
                        dialog.dismiss();
                    }
                });
        alert.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editTextMpoCode.setText("");
                addMPO(mpoID,mpoID,"CHQ");
            }
        });
        alert.show();
    }



    public void addMPO(String mpoId,String name,String designation){

        final AccompanyModel accompanyModel = new AccompanyModel();

        accompanyModel.setId(mpoId);
        accompanyModel.setName(name);
        accompanyModel.setDesignation(designation);

        boolean isExist = false;
        if (accompanyModelList.size()>0){
            for (AccompanyModel acm:accompanyModelList){
                if (acm.getId().equalsIgnoreCase(mpoId)){
                    isExist = true;
                }else {
                    isExist = false;
                }
            }
        }
        //check duplicate here
        if (isExist){
            ToastUtils.shortToast("Already exist in this list");
        }else{
            accompanyModelList.add(accompanyModel);
        }

        refreshList();

    }



    public void refreshList(){
        fastAdapter.clear();
        fastAdapter.add(accompanyModelList);
        fastAdapter.withSelectable(true);
        fastAdapter.withItemEvent(new ClickEventHook<AccompanyModel>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof AccompanyModel.ViewHolder) {
                    return ((AccompanyModel.ViewHolder) viewHolder).ivMinus;
                }
                return null;
            }

            @Override
            public void onClick(View v, int position, FastAdapter<AccompanyModel> fastAdapter1, final AccompanyModel item) {
                accompanyModelList.remove(position);
                fastAdapter.remove(position);
                fastAdapter.notifyAdapterDataSetChanged();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(fastAdapter);
    }


    @OnClick(R.id.btnDone)
    void onClickAbutton(){
        MyLog.show(TAG,"accompanyModelList.size():"+accompanyModelList.size());

        if (accompanyModelList.size()>0){
            StringBuffer acmIds = new StringBuffer();
            String prefix = "";
            for (AccompanyModel acm:accompanyModelList){
                acmIds.append(prefix);
                prefix = ",";
                acmIds.append(acm.getId());
            }
            MyLog.show(TAG,"accompany string:"+acmIds.toString());
            EventBus.getDefault().post(new AccompanyEvent(acmIds.toString()));
            //getFragmentManager().popBackStack();
            finish();
        }else{
            ToastUtils.shortToast("You didn\'t add any accompany yet!");
        }

    }


        @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable!=null){
            mCompositeDisposable.clear();
        }
        //dismissLoading();
    }



    private void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
