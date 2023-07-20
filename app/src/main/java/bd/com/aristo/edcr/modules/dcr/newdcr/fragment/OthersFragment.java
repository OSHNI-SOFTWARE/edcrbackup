package bd.com.aristo.edcr.modules.dcr.newdcr.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.dcr.DCRSendModel;
import bd.com.aristo.edcr.modules.dcr.accompany.AccompanyEvent;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModelForSend;
import bd.com.aristo.edcr.modules.dcr.newdcr.NewDCRListener;
import bd.com.aristo.edcr.modules.dcr.newdcr.NewDCRUtils;
import bd.com.aristo.edcr.modules.dcr.newdcr.model.NewDCRModel;
import bd.com.aristo.edcr.modules.dcr.newdcr.model.NewDCRProductModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.networking.RequestServices;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.buttons.AButton;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import bd.com.aristo.edcr.utils.ui.texts.AnEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by Tariqul.Islam on 6/5/17.
 */

public class OthersFragment extends Fragment implements NewDCRListener {

    private static final String TAG = "NewDoctorFragment";

    List<DCRModelForSend> dcrModelForSendList;
    //SyncUtils syncUtils;

    @Inject
    APIServices apiServices;
    @Inject
    Realm r;
    @Inject
    RequestServices requestServices;

    @BindView(R.id.rgMorEve)
    RadioGroup rgMorEve;
    @BindView(R.id.morning)
    RadioButton morning;
    @BindView(R.id.evening)
    RadioButton evening;
    @BindView(R.id.etName)
    AnEditText etName;
    @BindView(R.id.etDegree)
    AnEditText etDegree;
    @BindView(R.id.etAddress)
    AnEditText etAddress;
    @BindView(R.id.etRemarks)
    AnEditText etRemarks;
    @BindView(R.id.txtAccompanyInfo)
    ATextView txtAccompanyInfo;

    @BindView(R.id.btnGivenSample)
    AButton btnGivenSample;
    @BindView(R.id.rvNewDcrProduct)
    RecyclerView rvNewDcrProduct;

    RadioButton radioButton;

    private long newDCRId;

    private String accompanyId;
    Context context;

    public UserModel userModel;
    String selectedDoctorId;
    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }

    public Bundle savedInstanceState;
    List<DCRProductsModel> dcrGifts  = new ArrayList<>();
    List<DCRProductsModel> dcrSample = new ArrayList<>();
    List<DCRProductsModel> dcrPromoted = new ArrayList<>();
    List<NewDCRProductsModel> dcrProducts = new ArrayList<>();
    public boolean isSynced = false;

    public OthersFragment() {
    }

    public static OthersFragment newInstance() {
        return new OthersFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        this.savedInstanceState                         = savedInstanceState;
        View rootView                                   = inflater.inflate(R.layout.new_dcr_others_view_pager, container, false);
        ButterKnife.bind(this, rootView);
        getUserInfo();
        ((Activity) context).setTitle("Other DCR");

        setHasOptionsMenu(true);
        dcrGifts = NewDCRUtils.getNewDcrGiftList(false, r);
        dcrSample = NewDCRUtils.getNewDcrSampleList(r, false);

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        return rootView;
    }

    public void setAdapter(){
        FastItemAdapter<NewDCRProductsModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(dcrProducts);
        fastAdapter.withSelectable(false);
        //fill the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rvNewDcrProduct.setLayoutManager(layoutManager);
        rvNewDcrProduct.setAdapter(fastAdapter);
    }


    @OnClick(R.id.btnGivenSample)
    public void givenSample(){
        startActivity(NewDcrSampleViewPagerActivity.start(context, dcrSample, dcrGifts, dcrPromoted));
    }

    @OnClick(R.id.llDCR)
    void goDCR(){

    }

    @OnClick(R.id.llSave)
    void save(){

        int selectedId = rgMorEve.getCheckedRadioButtonId();
        radioButton = (RadioButton) ((Activity) context).findViewById(selectedId);

        if(isValidate()) {
            r.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm r) {
                    NewDCRModel newDCRModel = new NewDCRModel();
                    newDCRId = NewDCRUtils.getId(r);
                    newDCRModel.setId(newDCRId);
                    newDCRModel.setDate(DateTimeUtils.getToday("dd-MM-yyyy"));
                    newDCRModel.setDoctorName(StringUtils.getAndFormAmp(etName.getText().toString()));
                    newDCRModel.setDoctorID("other");
                    newDCRModel.setWard(StringUtils.getAndFormAmp(etDegree.getText().toString()));
                    newDCRModel.setContact(StringUtils.getAndFormAmp(etAddress.getText().toString()));
                    newDCRModel.setShift(morning.isChecked()?StringConstants.MORNING:StringConstants.EVENING);
                    newDCRModel.setRemarks(StringUtils.getAndFormAmp(etRemarks.getText().toString()));
                    newDCRModel.setAccompanyId(accompanyId);
                    newDCRModel.setSynced(isSynced);
                    newDCRModel.setOption(3);
                    long productId = NewDCRUtils.getNewDCRProductModelId(r);
                    List<NewDCRProductModel> newDCRProductModels = new ArrayList<>();
                    for (DCRProductsModel newProductModel:dcrSample){
                        if(newProductModel.getCount() > 0) {
                            NewDCRProductModel newDCRProductModel = new NewDCRProductModel();
                            newDCRProductModel.setNewDcrId(newDCRId);
                            newDCRProductModel.setId(productId);
                            newDCRProductModel.setProductID(newProductModel.getCode());
                            newDCRProductModel.setProductName(newProductModel.getName());
                            newDCRProductModel.setCount(newProductModel.getCount());
                            newDCRProductModel.setFlag(newProductModel.getItemType());
                            newDCRProductModels.add(newDCRProductModel);
                            productId++;
                            //if(isSynced) DCRUtils.setBalanceAfterDCRSent(r, newProductModel.getCode(), newProductModel.getCount());
                        }
                    }
                    for (DCRProductsModel newProductModel:dcrGifts){
                        if(newProductModel.getCount() > 0) {
                            NewDCRProductModel newDCRProductModel = new NewDCRProductModel();
                            newDCRProductModel.setNewDcrId(newDCRId);
                            newDCRProductModel.setId(productId);
                            newDCRProductModel.setProductID(newProductModel.getCode());
                            newDCRProductModel.setProductName(newProductModel.getName());
                            newDCRProductModel.setCount(newProductModel.getCount());
                            newDCRProductModel.setFlag(newProductModel.getItemType());
                            newDCRProductModels.add(newDCRProductModel);
                            productId++;
                            //if(isSynced) DCRUtils.setBalanceAfterDCRSent(r, newProductModel.getCode(), newProductModel.getCount());
                        }
                    }
                    for (DCRProductsModel newProductModel:dcrPromoted){
                        if(newProductModel.getCount() > 0) {
                            NewDCRProductModel newDCRProductModel = new NewDCRProductModel();
                            newDCRProductModel.setNewDcrId(newDCRId);
                            newDCRProductModel.setId(productId);
                            newDCRProductModel.setProductID(newProductModel.getCode());
                            newDCRProductModel.setProductName(newProductModel.getName());
                            newDCRProductModel.setCount(newProductModel.getCount());
                            newDCRProductModel.setFlag(newProductModel.getItemType());
                            newDCRProductModels.add(newDCRProductModel);
                            productId++;
                            //if(isSynced) DCRUtils.setBalanceAfterDCRSent(r, newProductModel.getCode(), newProductModel.getCount());
                        }
                    }
                    //Parent data
                    r.insertOrUpdate(newDCRProductModels);
                    r.insertOrUpdate(newDCRModel);
                    ((Activity) context).onBackPressed();
                }
            });

        } else {
            ToastUtils.shortToast("Select sample given and fill the form correctly!");
        }
    }


    @OnClick(R.id.llUpload)
    void upload(){
        int selectedId = rgMorEve.getCheckedRadioButtonId();
        radioButton = (RadioButton) ((Activity) context).findViewById(selectedId);

        //Validate the filed
        if(!isValidate()){
            ToastUtils.shortToast("Select sample given and fill the form correctly!");
            return;
        }

        displayNewDCRUploadAlert();

    }

    public void displayNewDCRUploadAlert(){
        String accompanyIDS = accompanyId;
        String msg = "";
        if(TextUtils.isEmpty(accompanyIDS)){
            msg = "Your are trying to upload New DCR "+" <span style=\"color:#F06423;\">"+"without Accompany!</span>"+" Would you like to continue?"; //#F06423
        } else {
            msg = "Your are trying to upload New DCR" +" <span style=\"color:#01991f;\">"+"Accompany with: "+accompanyIDS+"!"+"</span>"+" Would you like to continue?"; //#01991f
        }
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(Html.fromHtml(msg));
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                newDCRUpload();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

 public void  newDCRUpload(){
     //Now get data from database
     isSynced = false;
     dcrModelForSendList = getNewDcrForSend();
     DCRSendModel dcrSendModel = new DCRSendModel();
     dcrSendModel.setUserId(userModel.getUserId());
     dcrSendModel.setAccompanyIds(accompanyId);
     dcrSendModel.setCreateDate(DateTimeUtils.getToday(DateTimeUtils.FORMAT9));
     dcrSendModel.setShift(morning.isChecked()?StringConstants.MORNING:StringConstants.EVENING);
     dcrSendModel.setDoctorId("other");
     dcrSendModel.setRemarks(StringUtils.getAndFormAmp(etRemarks.getText().toString()));
     dcrSendModel.setTeamLeader("");
     dcrSendModel.setTeamVolume("0");
     dcrSendModel.setDcrSubType(3);
     dcrSendModel.setWard(StringUtils.getAndFormAmp(etDegree.getText().toString()));
     dcrSendModel.setContactNo(StringUtils.getAndFormAmp(etAddress.getText().toString()));
     dcrSendModel.setSampleList(dcrModelForSendList);
     requestServices.uploadNewDcr(context, dcrSendModel, apiServices, r, this);
     //DCRSender.newDCRUpload(this, dcrSendModel,  ((Activity) context), apiServices, dcrModelForSendList);
 }

 public boolean isValidate(){
        if(!TextUtils.isEmpty(etName.getText().toString()) && !TextUtils.isEmpty(etAddress.getText().toString())
                && !TextUtils.isEmpty(etRemarks.getText().toString()) && getSampleCount() > 0){
            return true;
     }

        return false;
 }

    public List<DCRModelForSend> getNewDcrForSend(){
        List<DCRModelForSend> dcrModelForSendList = new ArrayList<>();
        for (DCRProductsModel dcrProductsModel:dcrSample){
            if(dcrProductsModel.getCount() > 0){
                DCRModelForSend newDCRModelForSend = new DCRModelForSend();
                newDCRModelForSend.setProductCode(dcrProductsModel.getCode());
                newDCRModelForSend.setQuantity(String.valueOf(dcrProductsModel.getCount()));
                dcrModelForSendList.add(newDCRModelForSend);
            }
        }
        for (DCRProductsModel dcrProductsModel:dcrGifts){
            if(dcrProductsModel.getCount() > 0){
                DCRModelForSend newDCRModelForSend = new DCRModelForSend();
                newDCRModelForSend.setProductCode(dcrProductsModel.getCode());
                newDCRModelForSend.setQuantity(String.valueOf(dcrProductsModel.getCount()));
                dcrModelForSendList.add(newDCRModelForSend);
            }
        }
        for (DCRProductsModel dcrProductsModel:dcrPromoted){
            if(dcrProductsModel.getCount() > 0){
                DCRModelForSend newDCRModelForSend = new DCRModelForSend();
                newDCRModelForSend.setProductCode(dcrProductsModel.getCode());
                newDCRModelForSend.setQuantity(String.valueOf(dcrProductsModel.getCount()));
                dcrModelForSendList.add(newDCRModelForSend);
            }
        }
        return dcrModelForSendList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_newdcr, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccompanyEvent(AccompanyEvent event) {
       if (event.getAccompanyID()!=null && !TextUtils.isEmpty(event.getAccompanyID())){
           accompanyId = event.getAccompanyID();
           txtAccompanyInfo.setText("Accompany with: "+accompanyId);
       }
    }


    @Override
    public void onResume() {
        super.onResume();
        String txt = "Sample given("+getSampleCount()+")";
        btnGivenSample.setText(txt);
        setAdapter();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }




    }

    public int getSampleCount(){
        dcrProducts.clear();
        int count = 0;
        for (DCRProductsModel dcrProductsModel:dcrSample){
            if(dcrProductsModel.getCount() > 0){
                NewDCRProductsModel newDCRProductsModel = new NewDCRProductsModel();
                newDCRProductsModel.setName(dcrProductsModel.getName());
                newDCRProductsModel.setBalance(dcrProductsModel.getBalance());
                newDCRProductsModel.setCode(dcrProductsModel.getCode());
                newDCRProductsModel.setCount(dcrProductsModel.getCount());
                newDCRProductsModel.setGeneric(dcrProductsModel.getGeneric());
                newDCRProductsModel.setItemType(dcrProductsModel.getItemType());
                newDCRProductsModel.setPackSize(dcrProductsModel.getPackSize());
                newDCRProductsModel.setPlanned(dcrProductsModel.getPlanned());
                newDCRProductsModel.setQuantity(dcrProductsModel.getQuantity());
                newDCRProductsModel.setStatus(dcrProductsModel.getStatus());
                dcrProducts.add(newDCRProductsModel);
                count++;
            }
        }
        for (DCRProductsModel dcrProductsModel:dcrGifts){
            if(dcrProductsModel.getCount() > 0){
                NewDCRProductsModel newDCRProductsModel = new NewDCRProductsModel();
                newDCRProductsModel.setName(dcrProductsModel.getName());
                newDCRProductsModel.setBalance(dcrProductsModel.getBalance());
                newDCRProductsModel.setCode(dcrProductsModel.getCode());
                newDCRProductsModel.setCount(dcrProductsModel.getCount());
                newDCRProductsModel.setGeneric(dcrProductsModel.getGeneric());
                newDCRProductsModel.setItemType(dcrProductsModel.getItemType());
                newDCRProductsModel.setPackSize(dcrProductsModel.getPackSize());
                newDCRProductsModel.setPlanned(dcrProductsModel.getPlanned());
                newDCRProductsModel.setQuantity(dcrProductsModel.getQuantity());
                newDCRProductsModel.setStatus(dcrProductsModel.getStatus());
                dcrProducts.add(newDCRProductsModel);
                count++;
            }
        }

        for (DCRProductsModel dcrProductsModel:dcrPromoted){
            if(dcrProductsModel.getCount() > 0){
                NewDCRProductsModel newDCRProductsModel = new NewDCRProductsModel();
                newDCRProductsModel.setName(dcrProductsModel.getName());
                newDCRProductsModel.setBalance(dcrProductsModel.getBalance());
                newDCRProductsModel.setCode(dcrProductsModel.getCode());
                newDCRProductsModel.setCount(dcrProductsModel.getCount());
                newDCRProductsModel.setGeneric(dcrProductsModel.getGeneric());
                newDCRProductsModel.setItemType(dcrProductsModel.getItemType());
                newDCRProductsModel.setPackSize(dcrProductsModel.getPackSize());
                newDCRProductsModel.setPlanned(dcrProductsModel.getPlanned());
                newDCRProductsModel.setQuantity(dcrProductsModel.getQuantity());
                newDCRProductsModel.setStatus(dcrProductsModel.getStatus());
                dcrProducts.add(newDCRProductsModel);
                count++;
            }
        }

        return count;
    }

    @Override
    public void onFailed() {
        isSynced = false;
        ToastUtils.shortToast(StringConstants.SERVER_ERROR_MSG);
        save();
    }

    @Override
    public void onSuccess() {
        isSynced = true;
        requestServices.getProductAfterDCR(context, apiServices, userModel.getUserId(), r);
        ToastUtils.shortToast(StringConstants.UPLOAD_SUCCESS_MSG);
        save();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*if(syncUtils.mCompositeDisposable != null){
            syncUtils.mCompositeDisposable.clear();
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
