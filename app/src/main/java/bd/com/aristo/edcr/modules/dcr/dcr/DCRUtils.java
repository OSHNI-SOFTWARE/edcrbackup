package bd.com.aristo.edcr.modules.dcr.dcr;

import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.models.db.ProductModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRDoctorModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRDoctorUnplanModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRProductModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRProductModelForSave;
import bd.com.aristo.edcr.modules.dcr.newdcr.model.NewDCRModel;
import bd.com.aristo.edcr.modules.dvr.model.DVRDoctorRealm;
import bd.com.aristo.edcr.modules.dvr.model.DVRForServer;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.modules.dvr.model.DayShift;
import bd.com.aristo.edcr.modules.tp.model.TPPlaceRealmModel;
import bd.com.aristo.edcr.modules.wp.model.WPModel;
import bd.com.aristo.edcr.modules.wp.WPUtils;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import io.realm.Realm;

import static bd.com.aristo.edcr.utils.DateTimeUtils.FORMAT9;

/**
 * Created by monir.sobuj on 11/9/2017.
 */

public class DCRUtils {

    private static final String TAG = "DCRUtils";
    public static boolean DCR_IS_MORNING = false;
    public static boolean DCR_IS_INTERN = false;
    public static String DOCTOR_ID = "";
    public static String DOCTOR_NAME = "";
    public static String SHIFT = "";

    public static long DCR_ID = 0;

    public static DCRModel getDCRModel(Realm r, final String date, int month, int year){

        final DCRModel dcrModel = r.where(DCRModel.class)
                .equalTo(DCRModel.COL_CREATE_DATE, date)
                .equalTo(DCRModel.COL_DID, DOCTOR_ID)
                .equalTo(DCRModel.COL_SHIFT, SHIFT)
                .findFirst();
        if(dcrModel == null){
                    DCRModel dcrModel1 = new DCRModel();
                    dcrModel1.setId(getId(r));
                    dcrModel1.setCreateDate(date);
                    dcrModel1.setdID(DOCTOR_ID);
                    dcrModel1.setSendDate("");
                    dcrModel1.setShift(SHIFT);
                    dcrModel1.setMonth(month);
                    dcrModel1.setYear(year);
                    dcrModel1.setNew(true);
                    DCR_ID = dcrModel1.getId();
                    return dcrModel1;
        } else {
            DCR_ID = dcrModel.getId();
            return  dcrModel;
        }

    }

    public static List<DCRModel> getDCRList(Realm r, final String date){

        List<DCRModel> dcrModels = r.where(DCRModel.class)
                .equalTo(DCRModel.COL_CREATE_DATE, date)
                .equalTo(DCRModel.COL_IS_SENT, true)
                .findAll();
        if(dcrModels != null){
            return dcrModels;
        } else {
            return new ArrayList<DCRModel>();
        }

    }

    public static List<DCRModel> getDCRListMonth(Realm r, int month, int year){

        List<DCRModel> dcrModels = r.where(DCRModel.class)
                .equalTo(DCRModel.COL_MONTH, month)
                .equalTo(DCRModel.COL_YEAR, year)
                .equalTo(DCRModel.COL_IS_SENT, true)
                .notEqualTo(DCRModel.COL_STATUS, StringConstants.STATUS_ABSENT)
                .findAll();
        if(dcrModels != null){
            return dcrModels;
        } else {
            return new ArrayList<DCRModel>();
        }

    }

    public static List<DCRModel> getDCRListMonthForSummary(Realm r, int month, int year){

        List<DCRModel> dcrModels = r.where(DCRModel.class)
                .equalTo(DCRModel.COL_MONTH, month)
                .equalTo(DCRModel.COL_YEAR, year)
                .equalTo(DCRModel.COL_IS_SENT, true)
                .findAll();
        if(dcrModels != null){
            return dcrModels;
        } else {
            return new ArrayList<DCRModel>();
        }

    }

    public static boolean isExistNewDCR(Realm r, String docId, String date, String shift){
        NewDCRModel dcrModel = r.where(NewDCRModel.class)
                .equalTo(NewDCRModel.COL_DATE, date)
                .equalTo(NewDCRModel.COL_DOCTOR_ID, docId)
                .equalTo(NewDCRModel.COL_SHIFT, shift)
                .findFirst();
        if(dcrModel != null){
            return true;
        }
        return false;
    }

    public static List<NewDCRModel> getNewDCRList(Realm r, final String date){

        List<NewDCRModel> dcrModels = r.where(NewDCRModel.class)
                .equalTo(NewDCRModel.COL_DATE, date)
                .findAll();
        if(dcrModels != null){
            return dcrModels;
        } else {
            return new ArrayList<NewDCRModel>();
        }

    }

    public static int getMonthlyNewDcrCount(Realm r, int month, int year){
        List<String> newDCRCountList = new ArrayList<>();
        String currentMonth = DateTimeUtils.getMonthYear(month, year);
        List<NewDCRModel> dcrModels = r.where(NewDCRModel.class)
                .findAll();
        if(dcrModels != null && dcrModels.size() > 0){
            int i = 0;
            for(NewDCRModel newDCRModel:dcrModels){
                String[] day = newDCRModel.getDate().split("-");
                String dcrMonth = day[1]+"-"+day[2];
                if(dcrMonth.equalsIgnoreCase(currentMonth)){
                    newDCRCountList.add(""+i);
                }
                i++;
            }
        }

        return newDCRCountList.size();

    }





    public static List<DCRDoctorModel> getDCRDoctors(Realm realm, String shift){
        DateModel dateModel = getToday();
        boolean isMorning = shift.equalsIgnoreCase(StringConstants.MORNING);
        List<DCRDoctorModel> dcrDoctorModels = new ArrayList<>();
        List<WPModel> uniqueWPS;
        List<WPModel> wpModels = realm.where(WPModel.class)
                .equalTo(WPModel.COL_DAY, dateModel.getDay())
                .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                .equalTo(WPModel.COL_IS_MORNING, isMorning)
                .greaterThan(WPModel.COL_COUNT, 0)
                .findAll();
        uniqueWPS = uniqueWP(wpModels);
        for(WPModel wpModel:uniqueWPS) {
            DoctorsModel doctorsModel = realm.where(DoctorsModel.class)
                    .equalTo(DoctorsModel.COL_ID, wpModel.getDoctorID())
                    .findFirst();
            if(doctorsModel != null) {
                int status = getDCRStatus(realm, doctorsModel.getId(), isMorning);
                if (status != 2 || status != 3) {
                    DCRDoctorModel dcrDoctorModel = new DCRDoctorModel();
                    dcrDoctorModel.setName(doctorsModel.getName());
                    dcrDoctorModel.setDegree(doctorsModel.getDegree());
                    dcrDoctorModel.setId(doctorsModel.getId());
                    dcrDoctorModel.setContact("Phone no not found");
                    dcrDoctorModel.setSelected(WPUtils.getDCRCount(realm, 0, doctorsModel.getId(), dateModel.getMonth(), dateModel.getYear()));
                    dcrDoctorModel.setSample(WPUtils.getDCRCount(realm, 1, doctorsModel.getId(), dateModel.getMonth(), dateModel.getYear()));
                    dcrDoctorModel.setGift(WPUtils.getDCRCount(realm, 2, doctorsModel.getId(), dateModel.getMonth(), dateModel.getYear()));
                    dcrDoctorModel.setMorning(isMorning);
                    dcrDoctorModel.setStatus(status);
                    dcrDoctorModel.setDotList(getDotList(realm, doctorsModel.getId(), dateModel));
                    dcrDoctorModel.setDotExeList(getDotExeList(realm, doctorsModel.getId(), dateModel));
                    dcrDoctorModel.setDotAdditionalList(getDotAdditionalList(realm, doctorsModel.getId(), dateModel));
                    dcrDoctorModels.add(dcrDoctorModel);
                }
            }

        }
        return dcrDoctorModels;
    }

    public static List<DCRDoctorUnplanModel> getDCRDoctorsUnplanned(Realm realm, String shift){
        DateModel dateModel = getToday();
        boolean isMorning = shift.equalsIgnoreCase(StringConstants.MORNING);
        List<DCRDoctorUnplanModel> dcrDoctorModels = new ArrayList<>();
        List<WPModel> uniqueWPS;
        List<WPModel> wpModels = realm.where(WPModel.class)
                .equalTo(WPModel.COL_DAY, dateModel.getDay())
                .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                .equalTo(WPModel.COL_IS_MORNING, isMorning)
                .greaterThan(WPModel.COL_COUNT, 0)
                .findAll();
        uniqueWPS = uniqueWP(wpModels);

        List<String> docIds = new ArrayList<>();
        for(WPModel wpModel:uniqueWPS) {
            docIds.add(wpModel.getDoctorID());
        }
        DVRForServer dvrRealmModel = realm.where(DVRForServer.class)
                .equalTo(DVRForServer.COL_DAY, dateModel.getDay())
                .equalTo(DVRForServer.COL_MONTH, dateModel.getMonth())
                .equalTo(DVRForServer.COL_YEAR, dateModel.getYear())
                .equalTo(DVRForServer.COL_SHIFT, isMorning)
                .findFirst();
        if(dvrRealmModel != null){
            List<DVRDoctorRealm> doctorRealms = realm.where(DVRDoctorRealm.class)
                    .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, dvrRealmModel.getId())
                    .distinct(DVRDoctorRealm.COL_DOCTOR_ID).findAll();
            for(DVRDoctorRealm dvrDoctorRealm:doctorRealms){
                DoctorsModel dm = realm.where(DoctorsModel.class)
                        .equalTo(DoctorsModel.COL_ID, dvrDoctorRealm.getDoctorID())
                        .findFirst();
                if(dm != null && !docIds.contains(dm.getId())) {
                        int status = getDCRStatus(realm, dm.getId(), shift.equalsIgnoreCase(StringConstants.MORNING));
                        if (status != 2 || status != 3) {
                            DCRDoctorUnplanModel dcrDoctorModel = new DCRDoctorUnplanModel();
                            dcrDoctorModel.setName(dm.getName());
                            dcrDoctorModel.setDegree(dm.getDegree());
                            dcrDoctorModel.setId(dm.getId());
                            dcrDoctorModel.setContact("phone no not found");
                            dcrDoctorModel.setSelected(WPUtils.getDCRCount(realm, 0, dm.getId(), dateModel.getMonth(), dateModel.getYear()));
                            dcrDoctorModel.setSample(WPUtils.getDCRCount(realm, 1, dm.getId(), dateModel.getMonth(), dateModel.getYear()));
                            dcrDoctorModel.setGift(WPUtils.getDCRCount(realm, 2, dm.getId(), dateModel.getMonth(), dateModel.getYear()));
                            dcrDoctorModel.setMorning(shift.equalsIgnoreCase(StringConstants.MORNING));
                            dcrDoctorModel.setStatus(status);
                            dcrDoctorModel.setDotList(getDotList(realm, dm.getId(), dateModel));
                            dcrDoctorModel.setDotExeList(getDotExeList(realm, dm.getId(), dateModel));
                            dcrDoctorModel.setDotAdditionalList(getDotAdditionalList(realm, dm.getId(), dateModel));
                            boolean toBeAdded = true;
                            for (String docId : docIds) {
                                if (docId.equalsIgnoreCase(dm.getId())) {
                                    toBeAdded = false;
                                    break;
                                }
                            }
                            if (toBeAdded)
                                dcrDoctorModels.add(dcrDoctorModel);
                    }
                }
            }
        }
        return dcrDoctorModels;
    }


    public static int getDCRStatus(Realm r, final String docId, boolean isMorning){
        NewDCRModel newDCRModel = r.where(NewDCRModel.class)
                .equalTo(NewDCRModel.COL_DATE, DateTimeUtils.getToday(FORMAT9))
                .equalTo(NewDCRModel.COL_DOCTOR_ID, docId)
                .equalTo(NewDCRModel.COL_SHIFT, isMorning?StringConstants.MORNING:StringConstants.EVENING)
                .findFirst();
        if(newDCRModel != null){
            return 3;
        }

        final DCRModel dcrModel = r.where(DCRModel.class)
                .equalTo(DCRModel.COL_CREATE_DATE, DateTimeUtils.getToday(FORMAT9))
                .equalTo(DCRModel.COL_DID, docId)
                .equalTo(DCRModel.COL_SHIFT, isMorning?StringConstants.MORNING:StringConstants.EVENING)
                .findFirst();
        if(dcrModel == null){
            return 0;
        } else if(dcrModel.isSent()) {
            return 2;
        } else {
            return 1;
        }

    }


    public static long getId(Realm r){
        Number currentIdNum = r.where(DCRModel.class).max(DCRModel.COL_ID);
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    public static long getProductId(Realm r){
        Number currentIdNum = r.where(DCRProductModel.class).max(DCRProductModel.COL_ID);
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }


    public static void setDCRProductList(Realm r) {
        DateModel dateModel = getToday();
        List<WPModel> wpModelList = r.where(WPModel.class)
                .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                .equalTo(WPModel.COL_DOCTOR_ID, DOCTOR_ID)
                .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                .equalTo(WPModel.COL_DAY, dateModel.getDay())
                .equalTo(WPModel.COL_IS_MORNING, DCR_IS_MORNING)
                .equalTo(WPModel.COL_FLAG, 0)
                .greaterThan(WPModel.COL_COUNT, 0)
                .findAll();
        for (WPModel wpModel : wpModelList) {
            DCRProductModelForSave dcrProductModel = new DCRProductModelForSave();
            ProductModel sm = r.where(ProductModel.class)
                    .equalTo(ProductModel.COL_CODE, wpModel.getProductID())
                    .findFirst();

            if (sm != null) {
                dcrProductModel.setId(getProductId(r));
                dcrProductModel.setQuantity(wpModel.getCount());
                dcrProductModel.setDcrID(DCR_ID);
                dcrProductModel.setProductID(sm.getCode());
                dcrProductModel.setType(0);
                dcrProductModel.setPlanned(true);
                dcrProductModel.setQuantity(setSaveBalance(r, dcrProductModel));
                EventBus.getDefault().post(dcrProductModel);
            }
        }
    }


    public static void setDCRSampleList(Realm r) {
        DateModel dateModel = getToday();
        List<WPModel> wpModelList = r.where(WPModel.class)
                .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                .equalTo(WPModel.COL_DOCTOR_ID, DOCTOR_ID)
                .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                .equalTo(WPModel.COL_DAY, dateModel.getDay())
                .equalTo(WPModel.COL_IS_MORNING, DCR_IS_MORNING)
                .equalTo(WPModel.COL_FLAG, 1)
                .greaterThan(WPModel.COL_COUNT, 0)
                .findAll();
        for (WPModel wpModel : wpModelList) {
            DCRProductModelForSave dcrProductModel = new DCRProductModelForSave();
            ProductModel sm = r.where(ProductModel.class)
                    .equalTo(ProductModel.COL_CODE, wpModel.getProductID())
                    .equalTo(ProductModel.COL_ITEM_TYPE, StringConstants.SAMPLE_ITEM)
                    .equalTo(ProductModel.COL_YEAR, dateModel.getYear())
                    .equalTo(ProductModel.COL_MONTH, dateModel.getMonth())
                    .findFirst();
            if(sm != null) {
                dcrProductModel.setId(getProductId(r));
                dcrProductModel.setQuantity(wpModel.getCount());
                dcrProductModel.setDcrID(DCR_ID);
                dcrProductModel.setProductID(sm.getCode());
                dcrProductModel.setType(1);
                dcrProductModel.setPlanned(true);
                dcrProductModel.setQuantity(setSaveBalance(r, dcrProductModel));
                EventBus.getDefault().post(dcrProductModel);
            }
        }
    }

    public static void setDCRGiftList(Realm r) {
        DateModel dateModel = getToday();
        List<WPModel> wpModelList = r.where(WPModel.class)
                .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                .equalTo(WPModel.COL_DOCTOR_ID, DOCTOR_ID)
                .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                .equalTo(WPModel.COL_DAY, dateModel.getDay())
                .equalTo(WPModel.COL_IS_MORNING, DCR_IS_MORNING)
                .equalTo(WPModel.COL_FLAG, 2)
                .greaterThan(WPModel.COL_COUNT, 0)
                .findAll();

        for (WPModel wpModel : wpModelList) {
            DCRProductModelForSave dcrProductModel = new DCRProductModelForSave();
            ProductModel sm = r.where(ProductModel.class)
                    .equalTo(ProductModel.COL_CODE, wpModel.getProductID())
                    .equalTo(ProductModel.COL_ITEM_TYPE, StringConstants.GIFT_ITEM)
                    .equalTo(ProductModel.COL_YEAR, dateModel.getYear())
                    .equalTo(ProductModel.COL_MONTH, dateModel.getMonth())
                    .findFirst();
            if(sm != null) {
                dcrProductModel.setId(getProductId(r));
                dcrProductModel.setQuantity(wpModel.getCount());
                dcrProductModel.setDcrID(DCR_ID);
                dcrProductModel.setProductID(sm.getCode());
                dcrProductModel.setType(2);
                dcrProductModel.setPlanned(true);
                dcrProductModel.setQuantity(setSaveBalance(r, dcrProductModel));
                EventBus.getDefault().post(dcrProductModel);
            }

        }
    }

    public static DateModel getToday(){
        DateModel dateModel = new DateModel();
        Calendar cal = Calendar.getInstance();
        dateModel.setDay(cal.get(Calendar.DAY_OF_MONTH));
        dateModel.setMonth(cal.get(Calendar.MONTH)+1);
        dateModel.setYear(cal.get(Calendar.YEAR));
        dateModel.setLastDay(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateModel;
    }


    private static List<WPModel> uniqueWP(List<WPModel> list) {
        List<WPModel> uniqueList = new ArrayList<>();
        Set<WPModel> uniqueSet = new HashSet<>();
        for (WPModel obj : list) {
            if (uniqueSet.add(obj)) {
                uniqueList.add(obj);
            }
        }
        return uniqueList;
    }

    private static List<TPPlaceRealmModel> uniquePlaces(List<TPPlaceRealmModel> list) {
        List<TPPlaceRealmModel> uniqueList = new ArrayList<>();
        Set<TPPlaceRealmModel> uniqueSet = new HashSet<>();
        for (TPPlaceRealmModel obj : list) {
            if (uniqueSet.add(obj)) {
                uniqueList.add(obj);
            }
        }
        return uniqueList;
    }





    public static List<DayShift> getDotList(Realm r, String docId, DateModel dateModel){
        List<DayShift> dotList = new ArrayList<>();
        for(int i = 1; i <= dateModel.getLastDay(); i++){

            List<DVRForServer> dvrRealmModelList = r.where(DVRForServer.class)
                    .equalTo(DVRForServer.COL_DAY, i)
                    .equalTo(DVRForServer.COL_MONTH, dateModel.getMonth())
                    .equalTo(DVRForServer.COL_YEAR, dateModel.getYear())
                    .findAll();
            for(DVRForServer dvrForServer:dvrRealmModelList){
                if(r.where(DVRDoctorRealm.class)
                        .equalTo(DVRDoctorRealm.COL_DOCTOR_ID, docId)
                        .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, dvrForServer.getId())
                        .findFirst() != null){
                    DayShift dayShift = new DayShift();
                    dayShift.setWeekDay(formatDate(dateModel.getYear(), dateModel.getMonth(), i));
                    dayShift.setDay(i);
                    dayShift.setMorning(dvrForServer.isMorning());
                    dotList.add(dayShift);
                    break;
                } else{

                }
            }
        }
        return dotList;
    }


    public static List<DayShift> getDotExeList(Realm r, String docId, DateModel dateModel){
        List<DayShift> dotExeList = new ArrayList<>();
        List<DCRModel> dcrModels = r.where(DCRModel.class)
                .equalTo(DCRModel.COL_DID, docId)
                .equalTo(DCRModel.COL_MONTH, dateModel.getMonth())
                .equalTo(DCRModel.COL_YEAR, dateModel.getYear())
                .findAll();
        for(DCRModel dcrModel:dcrModels){
            if(!TextUtils.isEmpty(dcrModel.getSendDate())) {
                int i = Integer.valueOf(DateTimeUtils.getDayFormDate(dcrModel.getSendDate()));
                DayShift dayShift = new DayShift();
                dayShift.setWeekDay(formatDate(dateModel.getYear(), dateModel.getMonth(), i));
                dayShift.setDay(i);
                dayShift.setMorning(dcrModel.getShift().equalsIgnoreCase(StringConstants.MORNING));
                dotExeList.add(dayShift);
            }
        }
        return dotExeList;
    }

    public static List<DayShift> getDotAdditionalList(Realm r, String docId, DateModel dateModel){
        List<DayShift> dotAdditionalList = new ArrayList<>();
        for(int i = 1; i <= dateModel.getLastDay(); i++) {
            DateModel dateModel1 = new DateModel(i, dateModel.getMonth(), dateModel.getYear(), 0, dateModel.getLastDay());
            if(r.where(NewDCRModel.class)
                    .equalTo(NewDCRModel.COL_DOCTOR_ID, docId)
                    .equalTo(NewDCRModel.COL_DATE, DateTimeUtils.getDayMonthYear(dateModel1))
                    .findFirst() != null){
                DayShift dayShift = new DayShift();
                dayShift.setWeekDay(formatDate(dateModel.getYear(), dateModel.getMonth(), i));
                dayShift.setDay(i);
                dayShift.setMorning(true);
                dotAdditionalList.add(dayShift);
            }

        }
        return dotAdditionalList;
    }

    public static int setSaveBalance(Realm r, DCRProductModel dcrProductModel){
        DateModel dateModel = getToday();
        int balance = 0;
        int count = dcrProductModel.getQuantity();
        ProductModel pm = r.where(ProductModel.class)
                .equalTo(ProductModel.COL_MONTH, dateModel.getMonth())
                .equalTo(ProductModel.COL_YEAR, dateModel.getYear())
                .equalTo(ProductModel.COL_CODE, dcrProductModel.getProductID())
                .findFirst();
        if(pm != null){
            balance = pm.getBalance();
        }
        return balance >= count ? count : balance;
    }


    private static int setSaveBalance(Realm r, DCRProductModelForSave dcrProductModel){
        DateModel dateModel = getToday();
        int balance = 0;
        int count = dcrProductModel.getQuantity();
        ProductModel pm = r.where(ProductModel.class)
                .equalTo(ProductModel.COL_MONTH, dateModel.getMonth())
                .equalTo(ProductModel.COL_YEAR, dateModel.getYear())
                .equalTo(ProductModel.COL_CODE, dcrProductModel.getProductID())
                .findFirst();
        if(pm != null){
            balance = pm.getBalance();
        }
        return balance >= count ? count : balance;
    }

    public static String formatDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        return DateTimeUtils.WEEK_DAY_1[cal.get(Calendar.DAY_OF_WEEK)];
    }

    /*public static void setBalanceAfterDCRSent(Realm r, String code, int deductQty){
        DateModel dateModel = getToday();
        ProductModel pm = r.where(ProductModel.class)
                .equalTo(ProductModel.COL_MONTH, dateModel.getMonth())
                .equalTo(ProductModel.COL_YEAR, dateModel.getYear())
                .equalTo(ProductModel.COL_CODE, code)
                .findFirst();
        if(pm != null) {
            pm.setBalance(pm.getBalance() - deductQty);
            r.insertOrUpdate(pm);
        }
    }*/
}
