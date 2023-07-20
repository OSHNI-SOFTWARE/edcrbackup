package bd.com.aristo.edcr.modules.dcr.dcr.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.ProductModel;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.dcr.DCRSendModel;
import bd.com.aristo.edcr.modules.dcr.DCRSenderListener;
import bd.com.aristo.edcr.modules.dcr.accompany.AccompanyEvent;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.modules.dcr.dcr.listener.DCRProductListener;
import bd.com.aristo.edcr.modules.dcr.dcr.listener.UploadSaveListener;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModelForSend;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRProductModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRProductModelForSave;
import bd.com.aristo.edcr.modules.dcr.dcr.model.IDCRProductsModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.SampleProductsModel;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.modules.dvr.model.DVRDoctorRealm;
import bd.com.aristo.edcr.modules.wp.WPUtils;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.networking.RequestServices;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.TooltipWindow;
import bd.com.aristo.edcr.utils.ui.collections.AViewPager;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

import static bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils.getToday;
import static bd.com.aristo.edcr.utils.DateTimeUtils.FORMAT9;

/**
 * Created by monir.sobuj on 6/5/17.
 */

public class InternDCRViewPager extends Fragment implements UploadSaveListener, DCRSenderListener {

    private static final String TAG = "InternDCRViewPager";

    public TooltipWindow tooltipWindow;
    //public static Activity activity;
    boolean isAbsent = false, isUploaded = false;
    String cause = "";
    List<DCRModelForSend> dcrModelForSendList = new ArrayList<>();
    DialogFragment dialogFragment;// = new DCRUploadDialog();
    //SyncUtils syncUtils;


    private SectionsPagerAdapter mSectionsPagerAdapter;
    @Inject
    APIServices apiServices;
    @Inject
    Realm r;
    @Inject
    RequestServices requestServices;

    @BindView(R.id.dcr_view_pager)
    AViewPager aViewpager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.mycoordinate)
    CoordinatorLayout mycoordinate;
    @BindView(R.id.llAbsent)
    LinearLayout llAbsent;
    @BindView(R.id.llSave)
    LinearLayout llSave;
    @BindView(R.id.llDCR)
    LinearLayout llDCR;
    @BindView(R.id.txtAccompanyInfo)
    ATextView txtAccompanyInfo;
    Context context;




    public UserModel userModel;
    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }
    private String accompanyId = "";
    DCRModel dcrModel;// = new DCRModel();
    List<DCRProductModelForSave> dcrProductModelForSaveList = new ArrayList<>();
    private int[] tabIcons = {
            //R.drawable.ic_mini_star,
            R.drawable.ic_mini_product,
            R.drawable.ic_mini_gift_color
    };


    public InternDCRViewPager() {

    }

    public static InternDCRViewPager newInstance() {
        return new InternDCRViewPager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView = inflater.inflate(R.layout.fragment_dcr_view_pager, container, false);
        ButterKnife.bind(this, rootView);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        //activity = ((Activity) context);
        setHasOptionsMenu(true);
        tooltipWindow = new TooltipWindow(getContext(), R.layout.pop_up_absent);
        getUserInfo();
        setupTabs();

        dcrModel = DCRUtils.getDCRModel(r, DateTimeUtils.getToday(FORMAT9), DCRUtils.getToday().getMonth(), DCRUtils.getToday().getYear());
        DCRUtils.DCR_IS_INTERN = true;
        if(dcrModel!=null){
            setUPBottomButton();
            if(!dcrModel.isNew()){
                if(!TextUtils.isEmpty(dcrModel.getAccompanyID())) {
                    accompanyId = dcrModel.getAccompanyID();
                    txtAccompanyInfo.setText("Accompany with: "+accompanyId);
                }
                List<DCRProductModel> dcrProductModels = r.where(DCRProductModel.class)
                        .equalTo(DCRProductModel.COL_DCR_ID, dcrModel.getId())
                        .findAll();
                if(dcrProductModels != null && dcrProductModels.size() > 0){
                    for(DCRProductModel dcrProductModel:dcrProductModels){
                        DCRProductModelForSave saveDCRProductModel = new DCRProductModelForSave();
                        saveDCRProductModel.setId(dcrProductModel.getId());
                        saveDCRProductModel.setQuantity(dcrModel.isSent()?dcrProductModel.getQuantity():DCRUtils.setSaveBalance(r, dcrProductModel));
                        saveDCRProductModel.setDcrID(dcrProductModel.getDcrID());
                        saveDCRProductModel.setProductID(dcrProductModel.getProductID());
                        saveDCRProductModel.setType(dcrProductModel.getType());
                        saveDCRProductModel.setPlanned(dcrProductModel.isPlanned());
                        onAddProduct(saveDCRProductModel);
                        //EventBus.getDefault().post(saveDCRProductModel);
                    }
                }
            } else {
                //DCRUtils.setDCRProductList(r);
                DCRUtils.setDCRSampleList(r);
                DCRUtils.setDCRGiftList(r);
            }
        }
        return rootView;
    }

    private void setupTabs() {

        mSectionsPagerAdapter = new InternDCRViewPager.SectionsPagerAdapter(getChildFragmentManager());

        aViewpager.setAdapter(mSectionsPagerAdapter);
        aViewpager.setOffscreenPageLimit(2);
        aViewpager.setCurrentItem(0);
        aViewpager.setPagingEnabled(true);
        tabs.setupWithViewPager(aViewpager);
        tabs.getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabIcons[1]);
        //tabs.getTabAt(2).setIcon(tabIcons[2]);

    }

    public void setUPBottomButton(){
        if(dcrModel.isSent()){
            llAbsent.setVisibility(View.GONE);
            llSave.setVisibility(View.GONE);
            //llUpload.setVisibility(View.GONE);
        }
    }


    @Override
    public void onError(String remarks) {
        ToastUtils.shortToast(StringConstants.UPLOAD_FAIL_MSG);
        isUploaded = false;
        onSave(remarks);
    }

    @Override
    public void onSuccess(String remarks) {
        ToastUtils.shortToast(StringConstants.UPLOAD_SUCCESS_MSG);
        isUploaded = true;
        requestServices.getProductAfterDCR(context,apiServices, userModel.getUserId(), r);
        //syncUtils = new SyncUtils((Activity) context, 2, DCRUtils.getToday());
        onSave(remarks);
    }



    private class SectionsPagerAdapter extends FragmentStatePagerAdapter implements DCRProductListener {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new InternSampleFragment(this);
                case 1:
                    return new InternGiftFragment(this);

                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            switch (position) {
                case 0:

                    title = "Sample";
                    break;

                case 1:
                    title = "Gift";
                    break;
                default:
                    break;
            }
            return  title;
        }

        @Override
        public List<IDCRProductsModel> getDCRRefreshList(int type){
            DateModel dateModel = DCRUtils.getToday();
            int day = dateModel.getDay();
            int month = dateModel.getMonth();
            int year = dateModel.getYear();
            ProductModel pm;
            int plannedCount = 0;
            List<DCRProductModelForSave> dcrProductRefreshList = new ArrayList<>();
            List<IDCRProductsModel> iDCRProductsModelList = new ArrayList<>();
            for(DCRProductModelForSave dcrProductModelForSave:dcrProductModelForSaveList){
                if(dcrProductModelForSave.getType() == type){
                    dcrProductRefreshList.add(dcrProductModelForSave);
                }
            }
            for (DCRProductModelForSave dcrProductRefresh:dcrProductRefreshList){
                IDCRProductsModel iDcrProductsModel = new IDCRProductsModel();
                pm = r.where(ProductModel.class)
                        .equalTo(ProductModel.COL_CODE, dcrProductRefresh.getProductID())
                        .equalTo(ProductModel.COL_MONTH, month)
                        .equalTo(ProductModel.COL_YEAR, year)
                        .equalTo(ProductModel.COL_ITEM_FOR, StringConstants.ITEM_FOR_INTERN)
                        .findFirst();
                if(pm!=null) {
                    iDcrProductsModel.setBalance(pm.getBalance());
                    iDcrProductsModel.setCount(dcrProductRefresh.getQuantity());
                    iDcrProductsModel.setCode(pm.getCode());
                    iDcrProductsModel.setName(pm.getName()+"("+pm.getPackSize()+")");
                    iDcrProductsModel.setQuantity(pm.getQuantity());
                    plannedCount = WPUtils.getPlannedCount(r, pm.getCode(), month, year);
                    if (!dcrProductRefresh.isPlanned() && plannedCount == 0) {
                        plannedCount = 0;//dcrProductModel.getQuantity();
                    }
                    iDcrProductsModel.setPlannedCount(plannedCount);
                    iDcrProductsModel.setPlanned(dcrProductRefresh.isPlanned());
                    iDCRProductsModelList.add(iDcrProductsModel);
                }

            }
            return iDCRProductsModelList;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Activity) context).setTitle("DCR for "+DCRUtils.DOCTOR_NAME);

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @OnClick(R.id.llSave)
    void save(){

        cause = "present";
        isAbsent = false;
        dialogFragment = new DCRUploadDialog(this, dcrModel.getRemarks(), accompanyId);
        dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "dcr");
    }

    @OnClick(R.id.llAbsent)
    void absent(View v){
        showPopupAbsentOptions();

    }

    @OnClick(R.id.llDCR)
    void goDCR(){

    }


    public void showPopupAbsentOptions(){
        dialogFragment = new DCRUploadDialog(this, dcrModel.getRemarks(), accompanyId);
        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.pop_up_absent, (ViewGroup) ((Activity) context).findViewById(R.id.popup));

            final PopupWindow popupWindow = new PopupWindow(layout, (WindowManager.LayoutParams.WRAP_CONTENT), (WindowManager.LayoutParams.WRAP_CONTENT), true);
            popupWindow.setAnimationStyle(R.style.popupAnimation);
            popupWindow.setOutsideTouchable(false);
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                }
            });
            layout.setOnTouchListener(new View.OnTouchListener() {
                private int popX = 0;
                private int popY = 0;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int sides;
                    int topBot;
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            popX = (int) event.getRawX();
                            popY = (int) event.getRawY();
                            break;

                        case MotionEvent.ACTION_MOVE:
                            sides = (int) (event.getRawX() - popX);
                            topBot = (int) (event.getRawY() - popY);
                            popupWindow.update(sides, topBot, -1, -1);
                            break;
                    }
                    return true;
                }
            });

            final ATextView absent, notAllow, missed;
            absent = (ATextView) layout.findViewById(R.id.option_0);
            notAllow = (ATextView) layout.findViewById(R.id.option_1);
            missed = (ATextView) layout.findViewById(R.id.option_2);

            absent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cause = "absent";
                    isAbsent = true;
                    dialogFragment.show(getFragmentManager(), "absent");
                    popupWindow.dismiss();

                }
            });
            notAllow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cause = "not allowed";
                    isAbsent = true;
                    dialogFragment.show(getFragmentManager(), "not allow");
                    popupWindow.dismiss();
                }
            });
            missed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cause = "missed";
                    isAbsent = true;
                    dialogFragment.show(getFragmentManager(), "missed");
                    popupWindow.dismiss();

                }
            });


        } catch (Exception e){
            e.printStackTrace();
        }
    }




    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccompanyEvent(AccompanyEvent event) {
        if (event.getAccompanyID()!=null && !TextUtils.isEmpty(event.getAccompanyID())){
            accompanyId = event.getAccompanyID();
            //ToastUtils.shortToast("Accompany added");
            txtAccompanyInfo.setText("Accompany with: "+accompanyId);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddProduct(DCRProductModelForSave dcrProductModelForSave) {
        dcrProductModelForSave.setId(dcrProductModelForSave.getId() + dcrProductModelForSaveList.size());
        dcrProductModelForSaveList.add(dcrProductModelForSave);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateProduct(IDCRProductsModel item) {
        DCRProductModelForSave dcrProductModelForUpdate = null;
        for(DCRProductModelForSave dcrProductModelForSave1:dcrProductModelForSaveList){
            if(item.getCode().equals(dcrProductModelForSave1.getProductID())){
                dcrProductModelForUpdate = dcrProductModelForSave1;
            }
        }
        if(dcrProductModelForUpdate != null){
            dcrProductModelForSaveList.remove(dcrProductModelForUpdate);
            dcrProductModelForUpdate.setQuantity(item.getCount());
            dcrProductModelForSaveList.add(dcrProductModelForUpdate);
        }

    }

    public void onUpdateProductToZero(List<DCRProductModelForSave> items) {
        DCRProductModelForSave dcrProductModelForUpdate = null;

        dcrProductModelForSaveList.clear();
        dcrProductModelForSaveList.addAll(items);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addNewSampleItem(SampleProductsModel spModel) {
        boolean isAdded = true;
        for (DCRProductModelForSave dcrProductModelForSave:dcrProductModelForSaveList){
            if(dcrProductModelForSave.getProductID().equals(spModel.getCode())){
                isAdded = false;
            }
        }
        if(isAdded){
            DCRProductModelForSave dcrProductModel = new DCRProductModelForSave();
            dcrProductModel.setId(DCRUtils.getProductId(r) + dcrProductModelForSaveList.size());
            dcrProductModel.setQuantity(1);
            dcrProductModel.setDcrID(DCRUtils.DCR_ID);
            dcrProductModel.setProductID(spModel.getCode());
            dcrProductModel.setType(spModel.getpType());
            dcrProductModel.setPlanned(false);
            onAddProduct(dcrProductModel);
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_newdcr, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_dcr_info);
        if(menuItem != null){
            menuItem.setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }

    }

    @Override
    public void onStop() {
        super.onStop();

    }




    @Override
    public void onUpload(String remarks) {
        isUploaded = false;
        uploadDCR(StringUtils.getAndFormAmp(remarks));
    }

    @Override
    public void onSave(final String remarks) {
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if(dcrModel != null) {

                    if (isUploaded) {
                        dcrModel.setSent(true);
                        dcrModel.setNew(false);
                        dcrModel.setSendDate(DateTimeUtils.getTodaysDateTime(FORMAT9));
                        if (!isAbsent) {
                            dcrModel.setStatus(StringConstants.STATUS_PRESENT);
                            dcrModel.setStatusCause(StringConstants.STATUS_CAUSE_PRESENT);
                        } else {
                            dcrModel.setStatus(StringConstants.STATUS_ABSENT);
                            dcrModel.setStatusCause(cause);
                        }
                        dcrModel.setAccompanyID(accompanyId);
                        dcrModel.setRemarks(StringUtils.getAndFormAmp(remarks));

                        //dcr product saving

                        RealmResults<DCRProductModel> dcrProductModels = r.where(DCRProductModel.class)
                                .equalTo(DCRProductModel.COL_DCR_ID, dcrModel.getId()).findAll();
                        if(dcrProductModels != null && dcrProductModels.size() > 0){
                            dcrProductModels.deleteAllFromRealm();
                        }
                        for(DCRProductModelForSave dcrProductModelForSave:dcrProductModelForSaveList){
                            DCRProductModel dcrProductModel = new DCRProductModel();
                            dcrProductModel.setId(dcrProductModelForSave.getId());
                            dcrProductModel.setProductID(dcrProductModelForSave.getProductID());
                            dcrProductModel.setDcrID(dcrProductModelForSave.getDcrID());
                            dcrProductModel.setQuantity(dcrProductModelForSave.getQuantity());
                            dcrProductModel.setPlanned(dcrProductModelForSave.isPlanned());
                            dcrProductModel.setType(dcrProductModelForSave.getType());
                            //DCRUtils.setBalanceAfterDCRSent(realm, dcrProductModelForSave.getProductID(), dcrProductModelForSave.getQuantity());
                            r.insertOrUpdate(dcrProductModel);
                        }
                        r.insertOrUpdate(dcrModel);
                        setDVRFixed(r, dcrModel);
                    } else {
                        if (!isAbsent) {
                            dcrModel.setStatus(StringConstants.STATUS_PRESENT);
                            dcrModel.setStatusCause(StringConstants.STATUS_CAUSE_PRESENT);
                        } else {
                            getDcrModelForSendList();
                            dcrModel.setStatus(StringConstants.STATUS_ABSENT);
                            dcrModel.setStatusCause(cause);
                        }
                        dcrModel.setSent(false);
                        dcrModel.setAccompanyID(accompanyId);
                        dcrModel.setRemarks(StringUtils.getAndFormAmp(remarks));
                        dcrModel.setNew(false);
                    }

                    //dcr product saving

                    RealmResults<DCRProductModel> dcrProductModels = r.where(DCRProductModel.class)
                            .equalTo(DCRProductModel.COL_DCR_ID, dcrModel.getId()).findAll();
                    if(dcrProductModels != null && dcrProductModels.size() > 0){
                        dcrProductModels.deleteAllFromRealm();
                    }
                    for(DCRProductModelForSave dcrProductModelForSave:dcrProductModelForSaveList){
                        DCRProductModel dcrProductModel = new DCRProductModel();
                        dcrProductModel.setId(dcrProductModelForSave.getId());
                        dcrProductModel.setProductID(dcrProductModelForSave.getProductID());
                        dcrProductModel.setDcrID(dcrProductModelForSave.getDcrID());
                        dcrProductModel.setQuantity(dcrProductModelForSave.getQuantity());
                        dcrProductModel.setPlanned(dcrProductModelForSave.isPlanned());
                        dcrProductModel.setType(dcrProductModelForSave.getType());
                        r.insertOrUpdate(dcrProductModel);
                    }
                    r.insertOrUpdate(dcrModel);


                }
                ((Activity) context).finish();
            }
        });
    }

    public void setDVRFixed(Realm r, DCRModel dcrModel){
        DateModel dateModel = getToday();
        String id = dateModel.getYear()
                + DateTimeUtils.getMonthNumber(dateModel.getMonth())
                + DateTimeUtils.getMonthNumber(dateModel.getDay())
                + (dcrModel.getShift().equalsIgnoreCase(StringConstants.MORNING)? "0":"1");
        long dvrID = Long.parseLong(id);
        DVRDoctorRealm dvrDoctorRealm = r.where(DVRDoctorRealm.class)
                .equalTo(DVRDoctorRealm.COL_DOCTOR_ID, dcrModel.getdID())
                .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, dvrID)
                .findFirst();
        if(dvrDoctorRealm != null){
            dvrDoctorRealm.deleteFromRealm();
        }
        DVRDoctorRealm dvrDoctorRealm1 = new DVRDoctorRealm();
        dvrDoctorRealm1.setEditable(false);
        dvrDoctorRealm1.setDvrLocalId(dvrID);
        dvrDoctorRealm1.setDoctorID(dcrModel.getdID());
        r.insertOrUpdate(dvrDoctorRealm1);
    }

    public void uploadDCR(final String remarks){

        String status;
        if(!isAbsent) {
            status = StringConstants.STATUS_PRESENT;
        } else {
            status = StringConstants.STATUS_ABSENT;
        }
        dcrModelForSendList = getDcrModelForSendList();
        if(!isAbsent && dcrModelForSendList.size() <= 0){
            isUploaded = false;
            displayDCRErrorAlert();
            return;
        }
        DCRSendModel dcrSendModel = new DCRSendModel();
        dcrSendModel.setUserId(userModel.getUserId());
        dcrSendModel.setAccompanyIds(accompanyId);
        dcrSendModel.setCreateDate(dcrModel.getCreateDate());
        dcrSendModel.setShift(dcrModel.getShift());
        dcrSendModel.setDoctorId(dcrModel.getdID());
        dcrSendModel.setRemarks(StringUtils.getAndFormAmp(remarks));
        dcrSendModel.setStatus(status);
        dcrSendModel.setStatusCause(cause);
        dcrSendModel.setSampleList(dcrModelForSendList);
        dcrSendModel.setDcrSubType(1);
        requestServices.uploadDCR(context, dcrSendModel, apiServices, r, this);
    }

    public void displayDCRErrorAlert(){
        String msg = "No DCR found to upload.";
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();

            }
        });
        alert.show();
    }

    public List<DCRModelForSend> getDcrModelForSendList(){
        List<DCRModelForSend> dcrModelForSendList = new ArrayList<>();
        List<DCRProductModelForSave> absentDCRProductModelForSaveList = new ArrayList<>();
        if(isAbsent){
            for(DCRProductModelForSave dcrProductModelForSave : dcrProductModelForSaveList){
                DCRProductModelForSave absentDcrProduct = dcrProductModelForSave;
                absentDcrProduct.setQuantity(0);
                DCRModelForSend dcrModelForSend = new DCRModelForSend();
                dcrModelForSend.setProductCode(dcrProductModelForSave.getProductID());
                dcrModelForSend.setQuantity("0");
                dcrModelForSendList.add(dcrModelForSend);
                absentDCRProductModelForSaveList.add(absentDcrProduct);
            }
            onUpdateProductToZero(absentDCRProductModelForSaveList);

        } else {
            for (DCRProductModelForSave dcrProductModelForSave : dcrProductModelForSaveList) {
                DCRModelForSend dcrModelForSend = new DCRModelForSend();
                dcrModelForSend.setProductCode(dcrProductModelForSave.getProductID());
                dcrModelForSend.setQuantity(String.valueOf(dcrProductModelForSave.getQuantity()));
                dcrModelForSendList.add(dcrModelForSend);
            }
        }
        return dcrModelForSendList;
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
}
