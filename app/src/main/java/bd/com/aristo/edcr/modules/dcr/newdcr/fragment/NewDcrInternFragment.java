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
import android.widget.Spinner;

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
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModelForSend;
import bd.com.aristo.edcr.modules.dcr.newdcr.NewDCRListener;
import bd.com.aristo.edcr.modules.dcr.newdcr.NewDCRUtils;
import bd.com.aristo.edcr.modules.dcr.accompany.AccompanyEvent;
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
 * Created by monir.sobuj on 9/10/17.
 */

public class NewDcrInternFragment extends Fragment implements NewDCRListener {

    private static final String TAG = "NewDcrInternFragment";

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

    RadioButton radioButton;

    @BindView(R.id.btnGivenSample)
    AButton btnGivenSample;



    @BindView(R.id.spWardName)
    Spinner spWardName;

    @BindView(R.id.etKeyPersonName)
    AnEditText etKeyPersonName;

    @BindView(R.id.etCount)
    AnEditText etCount;

    @BindView(R.id.etRemarks)
    AnEditText etRemarks;
    @BindView(R.id.txtAccompanyInfo)
    ATextView txtAccompanyInfo;
    @BindView(R.id.rvNewDcrProduct)
    RecyclerView rvNewDcrProduct;

    public Bundle savedInstanceState;
    Context context;

    private long newDCRId;

    List<DCRModelForSend> dcrModelForSendList;

    private String accompanyId;

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

    public NewDcrInternFragment() {
    }

    public static NewDcrInternFragment newInstance() {
        return new NewDcrInternFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        this.savedInstanceState                         = savedInstanceState;
        View rootView                                   = inflater.inflate(R.layout.new_dcr_intern_view_pager, container, false);
        ButterKnife.bind(this, rootView);


        getUserInfo();

        setHasOptionsMenu(true);

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        dcrGifts = NewDCRUtils.getNewDcrGiftList(true, r);
        dcrSample = NewDCRUtils.getNewDcrSampleList(r, true);
        ((Activity) context).setTitle("Intern Doctor");

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
    public void setBtnGivenSample(){
        startActivity(NewDcrSampleViewPagerActivity.start(context, dcrSample, dcrGifts, dcrPromoted));
    }


    @OnClick(R.id.llSave)
    public void save(){

        final String strWordName = spWardName.getSelectedItem().toString();
        String doctorID = "I-"+strWordName+"-"+userModel.getMPGroup();
        final String strKeyPersonName = StringUtils.getAndFormAmp(etKeyPersonName.getText().toString());
        final String strRemarks = StringUtils.getAndFormAmp(etRemarks.getText().toString());
        final String noOfIntern = etCount.getText().toString();

        int selectedId = rgMorEve.getCheckedRadioButtonId();
        radioButton = (RadioButton) ((Activity) context).findViewById(selectedId);

        final String shiftName = radioButton.getText().toString();

        if(isValidate()){

            r.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm r) {
                    NewDCRModel newDCRModel = new NewDCRModel();
                    newDCRId = NewDCRUtils.getId(r);
                    newDCRModel.setId(newDCRId);
                    newDCRModel.setDate(DateTimeUtils.getToday("dd-MM-yyyy"));
                    newDCRModel.setDoctorName(strKeyPersonName);
                    newDCRModel.setDoctorID(doctorID);
                    newDCRModel.setWard(strWordName);
                    newDCRModel.setContact("");
                    newDCRModel.setShift(morning.isChecked()?StringConstants.MORNING:StringConstants.EVENING);
                    newDCRModel.setRemarks(strRemarks);
                    newDCRModel.setAccompanyId(accompanyId);
                    newDCRModel.setSynced(isSynced);
                    newDCRModel.setOption(2);
                    newDCRModel.setNoOfIntern(noOfIntern);
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
        }else{
            ToastUtils.shortToast("Please select sample and fill the form correctly!");
        }
    }

    @OnClick(R.id.llUpload)
    void upload(){
        //Validate the filed
        if(isCheckedDCR()) {
            if (!isValidate()) {
                ToastUtils.shortToast("Select sample given and fill the form correctly!");
                return;
            }
            displayNewDCRUploadAlert();

        } else {
            ToastUtils.longToast("Already DCR has been Sent against this type of Intern!");
        }
    }


    public boolean isValidate(){
        String strWordName = StringUtils.getAndFormAmp(spWardName.getSelectedItem().toString());
        String strKeyPersonName = StringUtils.getAndFormAmp(etKeyPersonName.getText().toString());
        String strRemarks = StringUtils.getAndFormAmp(etRemarks.getText().toString());
        String strNoOfIntern = etCount.getText().toString();

        if (!TextUtils.isEmpty(strWordName)
                && !TextUtils.isEmpty(strKeyPersonName)
                && !TextUtils.isEmpty(strNoOfIntern)
                && !TextUtils.isEmpty(strRemarks)
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
        String strWordName = spWardName.getSelectedItem().toString();
        String doctorID = "I-"+strWordName+"-"+userModel.getMPGroup();
        String strKeyPersonName = StringUtils.getAndFormAmp(etKeyPersonName.getText().toString());
        String strRemarks = StringUtils.getAndFormAmp(etRemarks.getText().toString());
        String noOfIntern = etCount.getText().toString();
        final String strNoOfIntern = noOfIntern.equalsIgnoreCase("6+")?"07":noOfIntern;

        int selectedId = rgMorEve.getCheckedRadioButtonId();
        radioButton = (RadioButton) ((Activity) context).findViewById(selectedId);

        String shiftName = radioButton.getText().toString();

        //Now get data from database

        dcrModelForSendList = getNewDcrForSend();

        DCRSendModel dcrSendModel = new DCRSendModel();
        dcrSendModel.setUserId(userModel.getUserId());
        dcrSendModel.setAccompanyIds(accompanyId);
        dcrSendModel.setCreateDate(DateTimeUtils.getToday(DateTimeUtils.FORMAT9));
        dcrSendModel.setShift(morning.isChecked()?StringConstants.MORNING:StringConstants.EVENING);
        dcrSendModel.setDoctorId(doctorID);
        dcrSendModel.setRemarks(strRemarks);
        dcrSendModel.setTeamLeader(strKeyPersonName);
        dcrSendModel.setTeamVolume(strNoOfIntern);
        dcrSendModel.setWard(strWordName);
        dcrSendModel.setContactNo("");
        dcrSendModel.setDcrSubType(2);
        dcrSendModel.setSampleList(dcrModelForSendList);
        requestServices.uploadNewDcr(context, dcrSendModel, apiServices, r, this);
        //DCRSender.newDCRUpload(this, dcrSendModel,  ((Activity) context), apiServices, dcrModelForSendList);
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
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String txt = "Sample given("+getSampleCount()+")";
        btnGivenSample.setText(txt);
        setAdapter();
    }

    public void displayAlert(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        // alert.setTitle("Alert");
        alert.setMessage("Please Enter intern key person name first then try again!");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();

            }
        });
        alert.show();
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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public boolean isCheckedDCR(){
        String strWordName = spWardName.getSelectedItem().toString();
        String doctorID = "I-"+strWordName+"-"+userModel.getMPGroup();
        NewDCRModel newDCRModel = r.where(NewDCRModel.class)
                .equalTo(NewDCRModel.COL_DOCTOR_ID, doctorID)
                .equalTo(NewDCRModel.COL_DATE, DateTimeUtils.getToday(DateTimeUtils.FORMAT9))
                .equalTo(NewDCRModel.COL_SHIFT, morning.isChecked()?StringConstants.MORNING:StringConstants.EVENING)
                .findFirst();

        return newDCRModel==null;
    }
}
