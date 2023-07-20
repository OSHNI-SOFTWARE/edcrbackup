package bd.com.aristo.edcr.modules.bill.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DANatureModel;
import bd.com.aristo.edcr.models.response.ResponseDetail;
import bd.com.aristo.edcr.models.db.TARateModel;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.bill.model.Bill;
import bd.com.aristo.edcr.modules.bill.model.BillForSend;
import bd.com.aristo.edcr.modules.bill.model.TAForSend;
import bd.com.aristo.edcr.modules.bill.model.TempBill;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.modules.tp.model.TPPlaceRealmModel;
import bd.com.aristo.edcr.modules.tp.model.TPServerModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.ConnectionUtils;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.buttons.AButton;
import bd.com.aristo.edcr.utils.ui.texts.ARadioGroup;
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
import io.realm.RealmResults;

/**
 * Created by monir.sobuj on 6/21/17.
 */

public class DetailsFragment extends Fragment implements DatePickerFragment.DateDialogListener {
    final public static String TAG = "DetailsFragment";
    @Inject
    Realm r;
    @Inject
    APIServices apiServices;
    TempBill tempBill = new TempBill();
    TARateModel taRateModel;
    Map<String, Float> daMap = new HashMap<>();
    float daFactor = 1;
    long billId;
    private static final String DIALOG_DATE = "HomeActivity.DateDialog";
    private CompositeDisposable mCompositeDisposableForSend;

    String strNDAMorning = "", strNDAEvening = "";
    StringBuilder placeMorning = new StringBuilder(), placeEvening = new StringBuilder();
    boolean isBillExist = false;

    DateModel dateModel;
    public UserModel userModel;

    @BindView(R.id.date)
    ATextView date;
    @BindView(R.id.totalBill)
    ATextView totalBill;
    @BindView(R.id.llDate)
    LinearLayout llDate;
    @BindView(R.id.llDA)
    LinearLayout llDA;
    @BindView(R.id.llTA)
    LinearLayout llTA;
    @BindView(R.id.txtMorningPlace)
    ATextView txtMorningPlace;
    @BindView(R.id.llMorningPlace)
    LinearLayout llMorningPlace;
    @BindView(R.id.txtEveningPlace)
    ATextView txtEveningPlace;
    @BindView(R.id.llEveningPlace)
    LinearLayout llEveningPlace;
    @BindView(R.id.txtNDA)
    ATextView txtNDA;
    @BindView(R.id.txtDAAmount)
    ATextView txtDAAmount;
    @BindView(R.id.rgMileage)
    ARadioGroup rgMileage;
    @BindView(R.id.txtFixedTAAmount)
    ATextView txtFixedTAAmount;
    @BindView(R.id.rbMcCity)
    RadioButton rbMcCity;
    @BindView(R.id.rbMileage)
    RadioButton rbMileage;
    @BindView(R.id.etDistance)
    AnEditText etDistance;
    @BindView(R.id.txtTotalTAAmount)
    ATextView txtTotalTAAmount;
    @BindView(R.id.send)
    AButton send;
    @BindView(R.id.cbHolidayWork)
    CheckBox cbHolidayWork;
    @BindView(R.id.txtTAAmountMileage)
    ATextView txtTAAmountMileage;
    @BindView(R.id.etRemarks)
    AnEditText etRemarks;
    @BindView(R.id.txtSupervisorRemark)
    ATextView txtSupervisorRemark;

    Context context;

    boolean isManualClick = false;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    Activity activity;

    public DetailsFragment() {
    }

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        App.getComponent().inject(this);
        View rootView = inflater.inflate(R.layout.fragment_bill_details, container, false);
        ButterKnife.bind(this, rootView);
        SystemUtils.hideSoftKeyboard(rootView, context);
        activity = ((AppCompatActivity) context);
        getUserInfo();
        setDate(getDateModel(Calendar.getInstance()));
        initializeView();
        return rootView;
    }

    @OnClick({R.id.date, R.id.send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.date:
                DatePickerFragment dialog = DatePickerFragment.newInstance();
                dialog.setDateDialogListener(this);
                dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), DIALOG_DATE);
                break;
            case R.id.send:
                if (ConnectionUtils.isNetworkConnected(context)){
                    sendBill();
                }else{
                    ToastUtils.shortToast(StringConstants.INTERNET_CONNECTION_ERROR);
                }

                break;
        }
    }

    @Override
    public void setDate(DateModel date1) {
        isBillExist = false;
        dateModel = date1;
        isManualClick = false;
        date.setText(formatDate(dateModel));
        long deviation = 999999;
        for(TARateModel taRateModel1:r.where(TARateModel.class)
                .lessThanOrEqualTo(TARateModel.COL_EFFECT_DATE, getDate()).findAll()){

            long diff = getDateDiff(taRateModel1.getEffectDate(), getDate(), TimeUnit.DAYS);//getDate().getDay() - taRateModel1.getEffectDate().getDay();
            if(diff <= deviation){
                deviation = diff;
                taRateModel = taRateModel1;
            }
        }

        deviation = 999999;
        List<DANatureModel> daNatureModels = new ArrayList<>();
        for(DANatureModel daNatureModel:r.where(DANatureModel.class)
                .lessThanOrEqualTo(DANatureModel.COL_EFFECT_DATE, getDate())
                .findAll()){
            long diff = getDateDiff(daNatureModel.getEffectDate(), getDate(), TimeUnit.DAYS);//getDate().getDay() - taRateModel1.getEffectDate().getDay();
            if(diff <= deviation){
                deviation = diff;
                daNatureModels = new ArrayList<>();
                daNatureModels.addAll(r.where(DANatureModel.class)
                        .equalTo(DANatureModel.COL_EFFECT_DATE, daNatureModel.getEffectDate())
                        .findAll());
            }
        }

        for (DANatureModel daNatureModel : daNatureModels) {
            daMap.put(daNatureModel.getName(), daNatureModel.getRate());
        }
        billId = getBillId();
        Bill bill = r.where(Bill.class).equalTo(Bill.COL_ID, billId)
                .findFirst();
        if (bill != null) {
            copyToTempBill(bill);
        } else {
            initializeBill();
        }
    }

//--------------------------------------------------- Initialize Purpose -----------------------------------------------------------------------------------------------

    public void initializeBill() {
            tempBill = new TempBill();
            //set initial data
            tempBill.setId(billId);
            tempBill.setDay(dateModel.getDay());
            tempBill.setMonth(dateModel.getMonth());
            tempBill.setYear(dateModel.getYear());
            tempBill.setReview(false);
            tempBill.setHoliday(false);
            tempBill.setUploaded(false);
            tempBill.setApproved(false);
            strNDAMorning = "";
            strNDAEvening = "";
            placeMorning = new StringBuilder();
            placeEvening = new StringBuilder();

            setDAFactor();

            if ((strNDAEvening.equalsIgnoreCase("") &&
                    strNDAMorning.equalsIgnoreCase(""))) { // 0
                setPlaceAndNDA(0, false); // both leave
            } else if (strNDAEvening.equals("")) {
                setPlaceAndNDA(1, false); // evening leave
            } else if (strNDAMorning.equals("")) {
                setPlaceAndNDA(2, false); //morning leave
            } else if (strNDAMorning.equalsIgnoreCase(strNDAEvening)) {
                setPlaceAndNDA(3, false); // both working/meeting but morning and evening da same
            } else {
                setPlaceAndNDA(3, true); // both working/meeting but morning and evening da different
            }
    }

    private void setDAFactor(){
        daFactor = 1;
        long[] tpIds = getTpLocalIdsFromDate(dateModel);

        TPServerModel tpMorning = r.where(TPServerModel.class)
                .equalTo(TPServerModel.COL_LOCAL_ID, tpIds[0])
                .equalTo(TPServerModel.COL_IS_APPROVED, true)
                .findFirst();
        TPServerModel tpEvening = r.where(TPServerModel.class)
                .equalTo(TPServerModel.COL_LOCAL_ID, tpIds[1])
                .equalTo(TPServerModel.COL_IS_APPROVED, true)
                .findFirst();
        if(tpEvening == null || tpMorning == null){
            ToastUtils.longToast("Create TP or sync TP first!!");
            ((AppCompatActivity) context).onBackPressed();

        }
        if (tpMorning != null) {
            if (tpMorning.getShiftType().equalsIgnoreCase(StringConstants.WORKING)) {
                strNDAMorning = tpMorning.getnDA();
                RealmResults<TPPlaceRealmModel> tpPlaceRealmModels = r.where(TPPlaceRealmModel.class)
                        .equalTo(TPPlaceRealmModel.COL_TP_ID, tpIds[0]).findAll();
                String prefix = "";
                for (TPPlaceRealmModel tpPlaceRealmModel : tpPlaceRealmModels) {
                    placeMorning.append(prefix);
                    prefix = ", ";
                    TPPlaceRealmModel place = r.where(TPPlaceRealmModel.class).equalTo(TPPlaceRealmModel.COL_CODE, tpPlaceRealmModel.getCode()).findFirst();
                    if (place != null)
                        placeMorning.append(place.getCode());
                }
            } else if (tpMorning.getShiftType().equalsIgnoreCase(StringConstants.MEETING)) {
                strNDAMorning = tpMorning.getnDA();
                placeMorning.append(tpMorning.getContactPlace());
            }
        }

        if (tpEvening != null) {
            if (tpEvening.getShiftType().equalsIgnoreCase(StringConstants.WORKING)) {
                strNDAEvening = tpEvening.getnDA();
                RealmResults<TPPlaceRealmModel> tpPlaceRealmModels = r.where(TPPlaceRealmModel.class)
                        .equalTo(TPPlaceRealmModel.COL_TP_ID, tpIds[1]).findAll();
                String prefix = "";
                for (TPPlaceRealmModel tpPlaceRealmModel : tpPlaceRealmModels) {
                    placeEvening.append(prefix);
                    prefix = ", ";
                    TPPlaceRealmModel place = r.where(TPPlaceRealmModel.class).equalTo(TPPlaceRealmModel.COL_CODE, tpPlaceRealmModel.getCode()).findFirst();
                    if (place != null)
                        placeEvening.append(place.getCode());
                }
            } else if (tpEvening.getShiftType().equalsIgnoreCase(StringConstants.MEETING)) {
                placeEvening.append(tpEvening.getContactPlace());
                strNDAEvening = tpEvening.getnDA();
            }

        }
        llDA.setVisibility(View.VISIBLE);
        llTA.setVisibility(View.VISIBLE);

        if ((strNDAEvening.equalsIgnoreCase("") &&
                strNDAMorning.equalsIgnoreCase(""))) { // 0
            daFactor = 0;
        } else if (strNDAEvening.equals("")) {
            daFactor = (float) (0.5 * daFactor);
        } else if (strNDAMorning.equals("")) {
            daFactor = (float) (0.5);
        } else if (strNDAMorning.equalsIgnoreCase(strNDAEvening)) {
            daFactor = 1;
        }

    }

    public void setPlaceAndNDA(int shift, boolean nature){
        switch (shift){
            case 0: // both leave
                tempBill.setBillAmount(0);
                tempBill.setDaAmount(0);
                tempBill.setnDA("NA, NA");
                tempBill.setPlacesEvening("Leave");
                tempBill.setPlacesMorning("Leave");
                isBillExist = false;
                break;
            case 1: // morning - working/meeting and evening - leave
                tempBill.setPlacesMorning(placeMorning.toString());
                tempBill.setPlacesEvening("Leave");
                tempBill.setnDA(strNDAMorning + ", NA");
                tempBill.setDaAmount(Math.round(daFactor * daMap.get(strNDAMorning)));
                isBillExist = true;
                break;
            case 2: // evening - working/meeting and morning - leave
                tempBill.setPlacesEvening(placeEvening.toString());
                tempBill.setPlacesMorning("Leave");
                tempBill.setnDA("NA, " + strNDAEvening);
                tempBill.setDaAmount(Math.round(daFactor * daMap.get(strNDAEvening)));
                isBillExist = true;
                break;
            case 3: // both working/meeting
                tempBill.setPlacesEvening(placeEvening.toString());
                tempBill.setPlacesMorning(placeMorning.toString());

                if(!nature) {
                    tempBill.setnDA(strNDAMorning +", "+ strNDAEvening);
                    tempBill.setDaAmount(Math.round(daFactor * daMap.get(strNDAMorning)));
                } else {
                    tempBill.setnDA(strNDAMorning +", "+ strNDAEvening);
                    tempBill.setDaAmount(Math.round(daFactor/2 * daMap.get(strNDAEvening) + daFactor/2 * daMap.get(strNDAMorning)));

                }
                isBillExist = true;
                break;
        }

        //if not leave unlock input event, in case of leave lock input events.
        if(isBillExist){
            unlockEvent();
            tempBill.setDistance(1);
            tempBill.setMillage(false);
            tempBill.setTa(Math.round(taRateModel.getRate() * taRateModel.getFuelPrice() * daFactor));
            tempBill.setBillAmount(tempBill.getDaAmount() + tempBill.getTa());
            updateBill();
        } else {
            tempBill.setMillage(false);
            tempBill.setTa(0);
            tempBill.setDistance(1);
            lockEvent();
        }


    }
//------------------------------------------------------------------------------------------copying saved bill--------------------------------------------------------
    /**
     *
     * @param bill
     */


    public void copyToTempBill(Bill bill){

        isBillExist = true;
        tempBill.setDaAmount(bill.getDaAmount());
        tempBill.setId(bill.getId());
        tempBill.setnDA(bill.getnDA());
        tempBill.setHoliday(bill.getIsHolidayWork().equalsIgnoreCase(StringConstants.YES));
        tempBill.setPlacesMorning(bill.getPlacesMorning());
        tempBill.setPlacesEvening(bill.getPlacesEvening());
        tempBill.setTa(bill.getTa());
        tempBill.setBillAmount(bill.getBillAmount());
        tempBill.setReview(bill.getIsReviewEnabled().equalsIgnoreCase(StringConstants.YES));
        tempBill.setApproved(bill.getStatus().equalsIgnoreCase(StringConstants.APPROVED_STATUS_APPROVED));
        tempBill.setUploaded(true);
        tempBill.setDistance(bill.getDistance());

        tempBill.setRemarks(bill.getRemarks());
        tempBill.setSuperRemarks(bill.getSuperRemarks());
        if(bill.getDistance() == 1){
            tempBill.setMillage(false);
        } else {
            tempBill.setMillage(true);
        }


        if(tempBill.isReview()){
            setDAFactor();
            unlockEvent();
        } else {
            lockEvent();
        }
        updateBill();

    }







    public void setTAForMCCity() { //Only for Main TA Click Event

        if(isBillExist) {
            tempBill.setTa(Math.round(daFactor * taRateModel.getFuelPrice() * taRateModel.getRate()));
            tempBill.setBillAmount(tempBill.getDaAmount() + tempBill.getTa());
            tempBill.setDistance(1);
            updateBill();
        }
    }

    public void setTAForMillage(float distance) { //Only for Main TA Click Event

        if(isBillExist) {
            tempBill.setTa(Math.round(distance * taRateModel.getMileageRate() * taRateModel.getFuelPrice()));
            tempBill.setBillAmount(tempBill.getDaAmount() + tempBill.getTa());
            tempBill.setDistance((int)distance);
            updateBill();
        }
    }


    public void updateBill() {

        if(tempBill.isUploaded()){
            etRemarks.setEnabled(false);
            if(!TextUtils.isEmpty(tempBill.getRemarks())){
                etRemarks.setText(tempBill.getRemarks());
            } else {
                etRemarks.setText("");
            }
        } else {
            etRemarks.setEnabled(true);
            if(!TextUtils.isEmpty(tempBill.getRemarks())){
                etRemarks.setText(tempBill.getRemarks());
            } else {
                etRemarks.setText("");
            }
        }


        if(!TextUtils.isEmpty(tempBill.getSuperRemarks())){
            txtSupervisorRemark.setText(tempBill.getSuperRemarks());
        } else {
            txtSupervisorRemark.setText("");
        }
        totalBill.setText(String.format(Locale.ENGLISH,"Total Bill : %d", tempBill.getBillAmount()));
        txtMorningPlace.setText(tempBill.getPlacesMorning());
        txtEveningPlace.setText(tempBill.getPlacesEvening());
        txtNDA.setText(tempBill.getnDA());
        txtDAAmount.setText(String.format(Locale.ENGLISH, "%d", tempBill.getDaAmount()));
        if(tempBill.isMillage()){
            txtFixedTAAmount.setText(String.format(Locale.ENGLISH, "%d", 0));
            txtTAAmountMileage.setText(String.format(Locale.ENGLISH, "%d", tempBill.getTa()));
        } else {
            txtFixedTAAmount.setText(String.format(Locale.ENGLISH, "%d", tempBill.getTa()));
            txtTAAmountMileage.setText(String.format(Locale.ENGLISH, "%d", 0));
        }
        txtTotalTAAmount.setText(String.format(Locale.ENGLISH, "%d", tempBill.getTa()));
    }




//---------------------------------------getting or Generating Ids------------------------------------------------------------------------------------------------------

    /**
     *
     * @param date
     * @return tpIds
     */
    public long[] getTpLocalIdsFromDate(DateModel date) {
        long[] tpIds = new long[2];
        tpIds[0] = Long.parseLong(date.getYear() + DateTimeUtils.getMonthNumber(date.getMonth())
                + date.getDay() + "0");

        tpIds[1] = Long.parseLong(date.getYear() + DateTimeUtils.getMonthNumber(date.getMonth())
                + date.getDay() + "1");
        return tpIds;
    }

    public long getBillId() {

        return Long.parseLong(dateModel.getYear() + DateTimeUtils.getMonthNumber(dateModel.getMonth())
                + dateModel.getDay());
    }



    public void lockEvent(){

        send.setVisibility(View.GONE);
        etDistance.setEnabled(false);
        if(tempBill.isHoliday()){
            cbHolidayWork.setChecked(true);
        } else {
            cbHolidayWork.setChecked(false);
        }
        if(tempBill.isMillage()){
            rbMileage.setChecked(true);
            rbMcCity.setChecked(false);
            etDistance.setVisibility(View.VISIBLE);
            etDistance.setText(""+tempBill.getDistance());
        } else {
            rbMileage.setChecked(false);
            rbMcCity.setChecked(true);
            etDistance.setVisibility(View.GONE);
        }
        cbHolidayWork.setEnabled(false);
        rbMcCity.setEnabled(false);
        rbMileage.setEnabled(false);
        etRemarks.setEnabled(false);
    }

    public void unlockEvent(){
        cbHolidayWork.setEnabled(true);
        rbMcCity.setEnabled(true);
        rbMileage.setEnabled(true);
        send.setVisibility(View.VISIBLE);
        etDistance.setEnabled(true);
        etRemarks.setEnabled(true);
        if(tempBill.isHoliday()){
            cbHolidayWork.setChecked(true);
        } else {
            cbHolidayWork.setChecked(false);
        }
        if(tempBill.isMillage()){
            rbMileage.setChecked(true);
            rbMcCity.setChecked(false);
            etDistance.setVisibility(View.VISIBLE);
            etDistance.setText(""+tempBill.getDistance());
        } else {
            rbMileage.setChecked(false);
            rbMcCity.setChecked(true);
            etDistance.setVisibility(View.GONE);
        }
        isManualClick = true;
    }


    //--------------------------------------------------------------------Date Utils----------------------------------
    /**
     * Date relate Utils**/

    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public DateModel getDateModel(Calendar c) {
        if(getArguments() != null){
            int day = getArguments().getInt("billDay");
            int month = getArguments().getInt("billMonth");
            int year = getArguments().getInt("billYear");
            dateModel = new DateModel(day, month, year, 0, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        } else {
            dateModel = new DateModel(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR), 0, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        }

        return dateModel;
    }

    public Date getDate(){
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = formatter.parse(DateTimeUtils.getDayMonthYear(dateModel));
            Log.e("Print result: ", String.valueOf(date));

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return date;
    }

    public String formatDate(DateModel date) {
        Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), date.getMonth() - 1, date.getDay());
        Date date1 = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMM yyyy");
        String hireDate = sdf.format(date1);
        return hireDate;
    }
    //---------------------------------------------------------------------------------------------------------
    /**Initialize Event Listener**/

    public void initializeView() {

        cbHolidayWork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (isManualClick) {
                    if (b) {
                        tempBill.setHoliday(true);
                        tempBill.setDaAmount(Math.round(tempBill.getDaAmount() + tempBill.getDaAmount() / 2));
                    } else {
                        tempBill.setHoliday(false);
                        tempBill.setDaAmount(Math.round(tempBill.getDaAmount() - tempBill.getDaAmount() / 3));
                    }
                    tempBill.setBillAmount(tempBill.getDaAmount() + tempBill.getTa());
                    updateBill();
                }
            }
        });

        rbMcCity.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isManualClick) {
                if (isChecked) {
                    tempBill.setMillage(false);
                    etDistance.setVisibility(View.GONE);
                    setTAForMCCity();
                }
            }
        });

        rbMileage.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isManualClick) {
                if (isChecked) {
                    tempBill.setMillage(true);
                    etDistance.setVisibility(View.VISIBLE);
                    setTAForMillage(TextUtils.isEmpty(etDistance.getText().toString()) ? 0 : Math.round(Double.parseDouble(etDistance.getText().toString())));
                }
            }
        });

        etDistance.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(isManualClick)
                    setTAForMillage(TextUtils.isEmpty(s.toString())?0:Math.round(Double.parseDouble(s.toString())));
            }
        });
    }


    /** For Sending purpose**/


    public void sendBill(){

        String userID = userModel.getUserId();
        String mpoCode = userModel.getLoginID();
        String designation = "MPO";
        String name = StringUtils.getAndFormAmp(userModel.getName());
        String year = String.valueOf(dateModel.getYear());
        String month = DateTimeUtils.getMonthNumber(dateModel.getMonth());
        String day = String.valueOf(dateModel.getDay());
        String DA = getDABillForSend();
        String TA = getTABillForSend();
        String TAOther = "{\"TAOther\":[]}";
        String Other = "{\"Other\":[]}";
        MyLog.show(TAG, userID+" "+mpoCode+" "+year+" "+month+" "+day);
        MyLog.show(TAG, "DA: "+DA);
        MyLog.show(TAG, "TA: "+TA);
        mCompositeDisposableForSend = new CompositeDisposable();
        if(ConnectionUtils.isNetworkConnected(getContext())) {
            //displayProgress("Bill Uploading...");
            final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please Wait...");
            loadingDialog.show();
            mCompositeDisposableForSend.add(apiServices.sendBill(userID,
                    mpoCode,
                    name,
                    "MPO",
                    year,
                    month,
                    day,
                    DA,
                    TA,
                    TAOther,
                    Other)
                    .subscribeOn(Schedulers.io())  // Run on a background thread
                    .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                    .subscribeWith(new DisposableObserver<ResponseDetail<String>>() {
                        @Override
                        public void onComplete() {
                            MyLog.show(TAG, "onCompletePost");
                            loadingDialog.dismiss();
                            //saveBill();
                        }

                        @Override
                        public void onError(Throwable e) {
                            MyLog.show(TAG, "onErrorPost");
                            MyLog.show(e.getLocalizedMessage(), e.getMessage());
                            ToastUtils.shortToast(StringConstants.SERVER_ERROR_MSG);
                            //hideProgress();
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onNext(ResponseDetail<String> value) {
                            if (value.getStatus() != null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                                ToastUtils.shortToast(StringConstants.UPLOAD_SUCCESS_MSG);
                                tempBill.setUploaded(true);
                                saveBill();

                            } else {
                                ToastUtils.shortToast(StringConstants.UPLOAD_FAIL_MSG);
                            }
                            //hideProgress();
                        }
                    }));
        }else {
            ToastUtils.shortToast(StringConstants.INTERNET_CONNECTION_ERROR);
        }

    }


    public String getDABillForSend(){
        BillForSend billForSend = new BillForSend();
        billForSend.setDaAmount(String.valueOf(tempBill.getDaAmount()));
        billForSend.setId(String.valueOf(billId));
        billForSend.setServerId("0");
        billForSend.setnDA(tempBill.getnDA());
        billForSend.setIsHoliday(tempBill.isHoliday()?StringConstants.YES:StringConstants.NO);
        billForSend.setPlacesMorning(tempBill.getPlacesMorning());
        billForSend.setPlacesEvening(tempBill.getPlacesEvening());
        if(!TextUtils.isEmpty(etRemarks.getText().toString())) {
            billForSend.setRemarks(etRemarks.getText().toString());
        } else {
            billForSend.setRemarks("");
        }
        Gson gson = new GsonBuilder().create();
        JsonObject jsonObject = gson.toJsonTree(billForSend).getAsJsonObject();
        return jsonObject.toString();
    }
    public String getTABillForSend(){
        TAForSend taForSend = new TAForSend();
        taForSend.setDistance(String.valueOf(tempBill.getDistance()));
        taForSend.setRegion(taRateModel.getRegionCode());
        taForSend.setTaAmount(String.valueOf(tempBill.getTa()));
        Gson gson = new GsonBuilder().create();
        JsonObject jsonObject = gson.toJsonTree(taForSend).getAsJsonObject();
        return jsonObject.toString();
    }
//------------------------------------------------------------------ Save Daily Bill ----------------------------------------------------------------------------------

    /**
     * save bill
     */
    public void saveBill() {
        r.executeTransaction(realm -> {


            Bill bill = new Bill();
            bill.setDaAmount(tempBill.getDaAmount());
            bill.setId(tempBill.getId());
            bill.setnDA(tempBill.getnDA());
            bill.setPlacesMorning(tempBill.getPlacesMorning());
            bill.setPlacesEvening(tempBill.getPlacesEvening());
            bill.setBillAmount(tempBill.getBillAmount());
            bill.setDay(dateModel.getDay());
            bill.setMonth(dateModel.getMonth());
            bill.setYear(dateModel.getYear());
            bill.setTa(tempBill.getTa());
            bill.setDistance(tempBill.getDistance());
            bill.setIsHolidayWork(tempBill.isHoliday()?StringConstants.YES:StringConstants.NO);
            bill.setStatus(tempBill.isApproved()?StringConstants.APPROVED_STATUS_APPROVED:StringConstants.APPROVED_STATUS_PENDING);
            bill.setIsReviewEnabled(tempBill.isReview()?StringConstants.YES:StringConstants.NO);
            bill.setBillAmount(tempBill.getBillAmount());
            bill.setUploaded(tempBill.isUploaded());
            if(!TextUtils.isEmpty(etRemarks.getText().toString())){
                tempBill.setRemarks(etRemarks.getText().toString());
                bill.setRemarks(tempBill.getRemarks());
            } else {
                bill.setRemarks("");
            }

            realm.insertOrUpdate(bill);

        });

        ToastUtils.shortToast("Saved successfully!");

        if(activity != null){
            activity.onBackPressed();
        }
    }




}
