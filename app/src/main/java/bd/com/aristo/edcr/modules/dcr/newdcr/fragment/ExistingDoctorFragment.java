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
import bd.com.aristo.edcr.models.Doctors;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.dcr.DCRSendModel;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModelForSend;
import bd.com.aristo.edcr.modules.dcr.newdcr.NewDCRListener;
import bd.com.aristo.edcr.modules.dcr.newdcr.existdoctors.ExistDoctorsListActivity;
import bd.com.aristo.edcr.modules.dcr.newdcr.NewDCRUtils;
import bd.com.aristo.edcr.modules.dcr.accompany.AccompanyEvent;
import bd.com.aristo.edcr.modules.dcr.newdcr.model.NewDCRModel;
import bd.com.aristo.edcr.modules.dcr.newdcr.model.NewDCRProductModel;
import bd.com.aristo.edcr.modules.dvr.DVRUtils;
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


public class ExistingDoctorFragment extends Fragment implements NewDCRListener {

    private static final String TAG = "ExistingDoctorFragment";

    @Inject
    APIServices apiServices;
    @Inject
    Realm r;
    @BindView(R.id.rgMorEve)
    RadioGroup rgMorEve;
    @BindView(R.id.morning)
    RadioButton morning;
    @BindView(R.id.evening)
    RadioButton evening;
    @BindView(R.id.btnGivenSample)
    AButton btnGivenSample;
    @BindView(R.id.rvNewDcrProduct)
    RecyclerView rvNewDcrProduct;


    @BindView(R.id.tvDoctorName)
    ATextView tvDoctorName;

    @BindView(R.id.etRemarks)
    AnEditText etRemarks;

    @BindView(R.id.txtAccompanyInfo)
    ATextView txtAccompanyInfo;

    Context context;

    public Doctors doctors;

    RadioButton radioButton;

    private long newDCRId;

    List<DCRModelForSend> dcrModelForSendList;

    private String accompanyId = "";

    public UserModel userModel;
    String selectedDoctorId;
    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }
    List<DCRProductsModel> dcrGifts  = new ArrayList<>();
    List<DCRProductsModel> dcrSample = new ArrayList<>();
    List<DCRProductsModel> dcrPromoted = new ArrayList<>();
    List<DCRProductsModel> dcrProducts = new ArrayList<>();
    public boolean isSynced = false;
    //SyncUtils syncUtils;
    @Inject
    RequestServices requestServices;


    public ExistingDoctorFragment() {
    }


    public static ExistingDoctorFragment newInstance() {
        return new ExistingDoctorFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.new_dcr_existing_doctor_view_pager, container, false);
        ButterKnife.bind(this, rootView);
        getUserInfo();
        setHasOptionsMenu(true);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        dcrGifts = NewDCRUtils.getNewDcrGiftList(false, r);
        dcrSample = NewDCRUtils.getNewDcrSampleList(r, false);

        ((Activity) context).setTitle("Existing Doctor");

        return rootView;
    }

    public void setAdapter(){
        FastItemAdapter<DCRProductsModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(dcrProducts);
        fastAdapter.withSelectable(false);
        //fill the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rvNewDcrProduct.setLayoutManager(layoutManager);
        rvNewDcrProduct.setAdapter(fastAdapter);
    }

    @OnClick(R.id.btnGivenSample)
    void setBtnGivenSample(){
        startActivity(NewDcrSampleViewPagerActivity.start(context, dcrSample, dcrGifts, dcrPromoted));
    }

    @OnClick(R.id.tvDoctorName)
    void onClickTvDoctor(){
        ExistDoctorsListActivity.start(((Activity) context));
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
                    newDCRModel.setDoctorName(doctors.getName());
                    newDCRModel.setDoctorID(doctors.getDId());
                    newDCRModel.setWard(doctors.getDegree());
                    newDCRModel.setContact(doctors.getAddr());
                    newDCRModel.setShift(morning.isChecked()?StringConstants.MORNING:StringConstants.EVENING);
                    newDCRModel.setRemarks(StringUtils.getAndFormAmp(etRemarks.getText().toString()));
                    newDCRModel.setAccompanyId(accompanyId);
                    newDCRModel.setSynced(isSynced);
                    newDCRModel.setOption(1);
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

    public boolean isValidate(){
        if (!TextUtils.isEmpty(tvDoctorName.getText().toString())
                && getSampleCount() > 0){
            return true;
        }

        return false;
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

    public void newDCRUpload(){
        //Now get data from database
        isSynced = false;
        dcrModelForSendList = getNewDcrForSend(r);
        DCRSendModel dcrSendModel = new DCRSendModel();
        dcrSendModel.setUserId(userModel.getUserId());
        dcrSendModel.setAccompanyIds(accompanyId);
        dcrSendModel.setCreateDate(DateTimeUtils.getToday(DateTimeUtils.FORMAT9));
        dcrSendModel.setShift(morning.isChecked()?StringConstants.MORNING:StringConstants.EVENING);
        dcrSendModel.setDoctorId(doctors.getDId());
        dcrSendModel.setRemarks(StringUtils.getAndFormAmp(etRemarks.getText().toString()));
        dcrSendModel.setTeamLeader("");
        dcrSendModel.setTeamVolume("0");
        dcrSendModel.setWard(doctors.getDegree());
        dcrSendModel.setContactNo(doctors.getAddr());
        dcrSendModel.setSampleList(dcrModelForSendList);
        dcrSendModel.setDcrSubType(1);
        requestServices.uploadNewDcr(context, dcrSendModel, apiServices, r, this);
        //DCRSender.newDCRUpload(this, dcrSendModel,  ((Activity) context), apiServices, dcrModelForSendList);
    }

    public List<DCRModelForSend> getNewDcrForSend(Realm r){
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDoctorSelectionEvent(Doctors doctors) {
        if(checkedNewDCR(doctors)) {
            this.doctors = doctors;
            if (this.doctors != null) {
                tvDoctorName.setText(this.doctors.getName());
            }
        } else {

        }
    }

    public void displayAlert(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
       // alert.setTitle("Alert");
        alert.setMessage("Please select doctor first then try again!");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();

            }
        });
        alert.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(doctors != null){
            tvDoctorName.setText(this.doctors.getName());
        }
        String txt = "Add sample("+getSampleCount()+")";
        btnGivenSample.setText(txt);
        setAdapter();
    }

    public int getSampleCount(){
        dcrProducts.clear();
        int count = 0;
        for (DCRProductsModel dcrProductsModel:dcrSample){
            if(dcrProductsModel.getCount() > 0){
                dcrProducts.add(dcrProductsModel);
                count++;
            }
        }
        for (DCRProductsModel dcrProductsModel:dcrGifts){
            if(dcrProductsModel.getCount() > 0){
                dcrProducts.add(dcrProductsModel);
                count++;
            }
        }
        for (DCRProductsModel dcrProductsModel:dcrPromoted){
            if(dcrProductsModel.getCount() > 0){
                dcrProducts.add(dcrProductsModel);
                count++;
            }
        }

        return count;
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

    @Override
    public void onSuccess() {
        isSynced = true;
        requestServices.getProductAfterDCR(context, apiServices, userModel.getUserId(), r);
        ToastUtils.shortToast(StringConstants.UPLOAD_SUCCESS_MSG);
        save();
    }

    @Override
    public void onFailed() {
        isSynced = false;
        ToastUtils.shortToast(StringConstants.SERVER_ERROR_MSG);
        save();
    }

    public boolean checkedNewDCR(Doctors doctors){
        DateModel dateModel = DCRUtils.getToday();
        boolean hasInDOT = DVRUtils.hasDoctorInDVR(r, doctors.getDId(), dateModel.getDay(), dateModel.getMonth(), dateModel.getYear(), morning.isChecked());
        if (hasInDOT){
            String shift = morning.isChecked()?StringConstants.CAPITAL_MORNING:StringConstants.CAPITAL_EVENING;
            ToastUtils.longToast("Doctor is in "+ shift +" DVR!!");
            return false;
        }
        NewDCRModel newDCRModel = r.where(NewDCRModel.class)
                .equalTo(NewDCRModel.COL_DOCTOR_ID, doctors.getDId())
                .equalTo(NewDCRModel.COL_DATE, DateTimeUtils.getToday("dd-MM-yyyy"))
                .equalTo(NewDCRModel.COL_SHIFT, morning.isChecked()?StringConstants.MORNING:StringConstants.EVENING)
                .findFirst();

        if(newDCRModel != null)  {
            ToastUtils.longToast("Already DCR has been Sent against this doctor!");
            return false;
        }
        return true;
    }
}
