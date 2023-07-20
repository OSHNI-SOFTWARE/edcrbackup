package bd.com.aristo.edcr.modules.wp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bd.com.aristo.edcr.models.DOTPlan;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.models.db.InternModel;
import bd.com.aristo.edcr.models.db.ProductModel;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRProductModel;
import bd.com.aristo.edcr.modules.dcr.dcr.model.SampleProductsModel;
import bd.com.aristo.edcr.modules.dss.DSSModel;
import bd.com.aristo.edcr.modules.dvr.model.DVRDoctorRealm;
import bd.com.aristo.edcr.modules.dvr.model.DVRForServer;
import bd.com.aristo.edcr.modules.gwds.GWDSModel;
import bd.com.aristo.edcr.modules.pwds.PWDSModel;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.modules.wp.fragment.WPInternViewPager;
import bd.com.aristo.edcr.modules.wp.fragment.WPViewPager;
import bd.com.aristo.edcr.modules.wp.model.WPDoctorsModel;
import bd.com.aristo.edcr.modules.wp.model.WPDoctorsModelForSend;
import bd.com.aristo.edcr.modules.wp.model.WPForSend;
import bd.com.aristo.edcr.modules.wp.model.WPModel;
import bd.com.aristo.edcr.modules.wp.model.WPModelForSend;
import bd.com.aristo.edcr.modules.wp.model.WPProductsModel;
import bd.com.aristo.edcr.modules.wp.model.WPUtilsModel;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by monir.sobuj on 11/2/2017.
 */

public class WPUtils {

    private static final String TAG = "WPUtils";
    public static boolean IS_CHANGED = false;

    public static DOTPlan getDateWiseDOTAndPlan(Realm r, int day, int month, int year){
        DOTPlan dotPlan = new DOTPlan();
        DVRForServer mDVR = r.where(DVRForServer.class)
                .equalTo(DVRForServer.COL_MONTH, month)
                .equalTo(DVRForServer.COL_YEAR, year)
                .equalTo(DVRForServer.COL_DAY, day)
                .equalTo(DVRForServer.COL_SHIFT, true)
                .findFirst();
        DVRForServer eDVR = r.where(DVRForServer.class)
                .equalTo(DVRForServer.COL_MONTH, month)
                .equalTo(DVRForServer.COL_YEAR, year)
                .equalTo(DVRForServer.COL_DAY, day)
                .equalTo(DVRForServer.COL_SHIFT, false)
                .findFirst();
        if(mDVR != null){
            List<DVRDoctorRealm> dvrDoctorRealms = r.where(DVRDoctorRealm.class)
                    .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, mDVR.getId())
                    .findAll();
            dotPlan.setMorningDOT(dvrDoctorRealms.size());
        }
        if(eDVR != null){
            List<DVRDoctorRealm> dvrDoctorRealms = r.where(DVRDoctorRealm.class)
                    .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, eDVR.getId())
                    .findAll();
            dotPlan.setEveningDOT(dvrDoctorRealms.size());
        }

        List<WPModel> savedWPM = r.where(WPModel.class)
                .equalTo(WPModel.COL_MONTH, month)
                .equalTo(WPModel.COL_YEAR, year)
                .equalTo(WPModel.COL_DAY, day)
                .equalTo(WPModel.COL_IS_MORNING, true)
                .distinct(WPModel.COL_DOCTOR_ID)
                .greaterThan(WPModel.COL_COUNT, 0)
                .findAll();
        List<WPModel> savedWPE = r.where(WPModel.class)
                .equalTo(WPModel.COL_MONTH, month)
                .equalTo(WPModel.COL_YEAR, year)
                .equalTo(WPModel.COL_DAY, day)
                .equalTo(WPModel.COL_IS_MORNING, false)
                .distinct(WPModel.COL_DOCTOR_ID)
                .greaterThan(WPModel.COL_COUNT, 0)
                .findAll();

        if(savedWPM != null){
            dotPlan.setMorningWP(savedWPM.size());
        }
        if(savedWPE != null){
            dotPlan.setEveningWP(savedWPE.size());
        }
        return dotPlan;
    }

    public static int[] getSavedDVRDayList(Realm realm, int month, int year){
        int[] dvrCount = new int[35];
        List<DVRForServer> savedDVRs = realm.where(DVRForServer.class)
                .equalTo(DVRForServer.COL_MONTH, month)
                .equalTo(DVRForServer.COL_YEAR, year)
                .findAll();
        for(DVRForServer dvrs: savedDVRs){
            List<DVRDoctorRealm> dvrDoctorRealms = realm.where(DVRDoctorRealm.class)
                    .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, dvrs.getId())
                    .findAll();
            dvrCount[dvrs.getDay()] = dvrCount[dvrs.getDay()] + dvrDoctorRealms.size();
        }
        return dvrCount;
    }

    public static int[] getSavedWPDayList(Realm realm, int month, int year){
        int[] planCount = new int[35];
        for(int i = 1; i < 35; i++){
            List<WPModel> savedWPM = realm.where(WPModel.class)
                    .equalTo(WPModel.COL_MONTH, month)
                    .equalTo(WPModel.COL_YEAR, year)
                    .equalTo(WPModel.COL_DAY, i)
                    .equalTo(WPModel.COL_IS_MORNING, true)
                    //.equalTo(WPModel.COL_IS_UPLOADED, true)
                    .distinct(WPModel.COL_DOCTOR_ID)
                    .greaterThan(WPModel.COL_COUNT, 0)
                    .findAll();
            List<WPModel> savedWPE = realm.where(WPModel.class)
                    .equalTo(WPModel.COL_MONTH, month)
                    .equalTo(WPModel.COL_YEAR, year)
                    .equalTo(WPModel.COL_DAY, i)
                    .equalTo(WPModel.COL_IS_MORNING, false)
                    //.equalTo(WPModel.COL_IS_UPLOADED, true)
                    .distinct(WPModel.COL_DOCTOR_ID)
                    .greaterThan(WPModel.COL_COUNT, 0)
                    .findAll();
            int size = 0;
            if(savedWPE != null && savedWPE.size() >  0){
                size += savedWPE.size();
            }
            if(savedWPM != null && savedWPM.size() >  0){
                size += savedWPM.size();
            }
            planCount[i] = size;
        }

        return planCount;
    }

    public static List<WPDoctorsModel> getWPDoctors(Realm r, DateModel dateModel){

        List<WPDoctorsModel> wpDoctorsModels = new ArrayList<>();

        List<DVRForServer> dvrRealmModels = r.where(DVRForServer.class)
                .equalTo(DVRForServer.COL_DAY, dateModel.getDay())
                .equalTo(DVRForServer.COL_MONTH, dateModel.getMonth())
                .equalTo(DVRForServer.COL_YEAR, dateModel.getYear())
                .findAll();

        Log.e(TAG,"wpdoctorslist size:"+dvrRealmModels.size());

        for(DVRForServer dvrRealmModel : dvrRealmModels){

            List<DVRDoctorRealm> doctorRealms = r.where(DVRDoctorRealm.class)
                    .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, dvrRealmModel.getId())
                    .sort(DVRDoctorRealm.COL_DOCTOR_ID).findAll();//was findAll() :Altaf

            Log.e(TAG,"doctorRealms size:"+doctorRealms.size());

            for(DVRDoctorRealm dvrDoctorRealm:doctorRealms){
                DoctorsModel dm = r.where(DoctorsModel.class)
                        .equalTo(DoctorsModel.COL_ID, dvrDoctorRealm.getDoctorID())
                        .findFirst();
                if(dvrDoctorRealm.getDoctorID().contains("I")) {
                    InternModel internModel = r.where(InternModel.class).equalTo(InternModel.COL_ID, dvrDoctorRealm.getDoctorID())
                            .equalTo(InternModel.COL_DATE, DateTimeUtils.getDayMonthYear(dateModel))
                            .equalTo(InternModel.COL_SHIFT, dvrRealmModel.isMorning())
                            .findFirst();
                    if(internModel != null && dm != null){
                        WPDoctorsModel wpDoctorsModel = new WPDoctorsModel();
                        wpDoctorsModel.setId(internModel.getInternId());
                        wpDoctorsModel.setName(dm.getName());
                        wpDoctorsModel.setSpecial(internModel.getUnit());
                        wpDoctorsModel.setIns("Intern");
                        wpDoctorsModel.setInsName(internModel.getInstitute());
                        wpDoctorsModel.setSaved(getWPs(r, dm, dvrRealmModel.isMorning(), dateModel));
                        wpDoctorsModel.setSynced(getSyncedWPs(r, dm, dvrRealmModel.isMorning(), dateModel));
                        wpDoctorsModel.setMorning(dvrRealmModel.isMorning());
                        wpDoctorsModels.add(wpDoctorsModel);
                    } else if(internModel == null && dm != null){
                        WPDoctorsModel wpDoctorsModel = new WPDoctorsModel();
                        wpDoctorsModel.setId(dm.getId());
                        wpDoctorsModel.setName(dm.getName());
                        wpDoctorsModel.setSpecial("Intern");
                        wpDoctorsModel.setIns("Intern");
                        wpDoctorsModel.setInsName("Intern");
                        wpDoctorsModel.setSaved(getWPs(r, dm, dvrRealmModel.isMorning(), dateModel));
                        wpDoctorsModel.setSynced(getSyncedWPs(r, dm, dvrRealmModel.isMorning(), dateModel));
                        wpDoctorsModel.setMorning(dvrRealmModel.isMorning());
                        wpDoctorsModels.add(wpDoctorsModel);
                    }
                } else if(dm != null) {
                    WPDoctorsModel wpDoctorsModel = new WPDoctorsModel();
                    wpDoctorsModel.setId(dm.getId());
                    wpDoctorsModel.setName(dm.getName());
                    wpDoctorsModel.setSpecial(dm.getSpecial());
                    if(dvrRealmModel.isMorning()){
                        wpDoctorsModel.setIns(dm.getMorningLoc());
                        wpDoctorsModel.setInsName(dm.getMorningLoc());
                    } else {
                        wpDoctorsModel.setIns(dm.getEveningLoc());
                        wpDoctorsModel.setInsName(dm.getEveningLoc());
                    }
                    wpDoctorsModel.setSaved(getWPs(r, dm, dvrRealmModel.isMorning(), dateModel));
                    wpDoctorsModel.setSynced(getSyncedWPs(r, dm, dvrRealmModel.isMorning(), dateModel));
                    wpDoctorsModel.setMorning(dvrRealmModel.isMorning());
                    wpDoctorsModels.add(wpDoctorsModel);
                }
            }

        }

        Collections.sort(wpDoctorsModels, new Comparator<WPDoctorsModel>() {
            @Override
            public int compare(WPDoctorsModel wpDoctorsModel, WPDoctorsModel t1) {
                return t1.isMorning()?1:-1;
            }
        });


        return wpDoctorsModels;
    }



    public static boolean getShift(Realm r, String dId, DateModel dateModel){
        boolean returnVal = false;
        DVRForServer dvrForServerMorning = r.where(DVRForServer.class)
                .equalTo(DVRForServer.COL_DAY, dateModel.getDay())
                .equalTo(DVRForServer.COL_MONTH, dateModel.getMonth())
                .equalTo(DVRForServer.COL_YEAR, dateModel.getYear())
                .equalTo(DVRForServer.COL_SHIFT, true)
                .findFirst();
        DVRForServer dvrForServerEvening = r.where(DVRForServer.class)
                .equalTo(DVRForServer.COL_DAY, dateModel.getDay())
                .equalTo(DVRForServer.COL_MONTH, dateModel.getMonth())
                .equalTo(DVRForServer.COL_YEAR, dateModel.getYear())
                .equalTo(DVRForServer.COL_SHIFT, false)
                .findFirst();
        if(dvrForServerEvening != null){
            DVRDoctorRealm dvrDoctorRealm = r.where(DVRDoctorRealm.class)
                    .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, dvrForServerEvening.getId())
                    .equalTo(DVRDoctorRealm.COL_DOCTOR_ID, dId)
                    .findFirst();
            if(dvrDoctorRealm != null){
                returnVal = false;
            }
        }
        if(dvrForServerMorning != null){
            DVRDoctorRealm dvrDoctorRealm = r.where(DVRDoctorRealm.class)
                    .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, dvrForServerMorning.getId())
                    .equalTo(DVRDoctorRealm.COL_DOCTOR_ID, dId)
                    .findFirst();
            if(dvrDoctorRealm != null){
                returnVal = true;
            }
        }

        return returnVal;

    }

    public static List<ProductModel> getMonthWiseProducts(Realm realm, int year, int month) {

        List<ProductModel> productModels = realm.where(ProductModel.class)
                .equalTo(ProductModel.COL_MONTH, month)
                .equalTo(ProductModel.COL_YEAR, year)
                .equalTo(ProductModel.COL_ITEM_TYPE, StringConstants.SELECTED_ITEM)
                .equalTo(ProductModel.COL_ITEM_FOR, StringConstants.ITEM_FOR_REGULAR)
                .findAll();
        if(productModels != null && productModels.size() > 0) {
            return productModels;
        }
        return new ArrayList<>();
    }



    // getMonthWiseSamplesForIntern
    public static List<ProductModel> getMonthWiseSamples(Realm realm, int year, int month){
        return realm.where(ProductModel.class)
                .equalTo(ProductModel.COL_MONTH, month)
                .equalTo(ProductModel.COL_YEAR, year)
                .equalTo(ProductModel.COL_ITEM_TYPE, StringConstants.SAMPLE_ITEM)
                .equalTo(ProductModel.COL_ITEM_FOR, StringConstants.ITEM_FOR_REGULAR)
                .findAll();
    }

    public static List<ProductModel> getMonthWiseSamplesForIntern(Realm realm, int year, int month){
        return realm.where(ProductModel.class)
                .equalTo(ProductModel.COL_MONTH, month)
                .equalTo(ProductModel.COL_YEAR, year)
                .equalTo(ProductModel.COL_ITEM_TYPE, StringConstants.SAMPLE_ITEM)
                .equalTo(ProductModel.COL_ITEM_FOR, StringConstants.ITEM_FOR_INTERN)
                .findAll();
    }


    public static List<ProductModel> getMonthWiseGifts(Realm realm, int year, int month) {
        List<ProductModel> giftModels = realm.where(ProductModel.class)
                .equalTo(ProductModel.COL_MONTH, month)
                .equalTo(ProductModel.COL_YEAR, year)
                .equalTo(ProductModel.COL_ITEM_TYPE, StringConstants.GIFT_ITEM)
                .equalTo(ProductModel.COL_ITEM_FOR, StringConstants.ITEM_FOR_REGULAR)
                .findAll();
        return giftModels;
    }

    public static List<ProductModel> getMonthWiseGiftsForIntern(Realm realm, int year, int month) {
        List<ProductModel> giftModels = realm.where(ProductModel.class)
                .equalTo(ProductModel.COL_MONTH, month)
                .equalTo(ProductModel.COL_YEAR, year)
                .equalTo(ProductModel.COL_ITEM_TYPE, StringConstants.GIFT_ITEM)
                .equalTo(ProductModel.COL_ITEM_FOR, StringConstants.ITEM_FOR_INTERN)
                .findAll();
        return giftModels;
    }

    public static boolean getWPs(Realm r, DoctorsModel dm, boolean isMorning, DateModel dateModel){
        List<WPModel> wpModels = r.where(WPModel.class)
                .equalTo(WPModel.COL_DOCTOR_ID, dm.getId())
                .equalTo(WPModel.COL_DAY, dateModel.getDay())
                .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                .equalTo(WPModel.COL_IS_MORNING, isMorning)
                .greaterThan(WPModel.COL_COUNT, 0)
                .findAll();
        return wpModels.size() > 0;
    }

    public static boolean getSyncedWPs(Realm r, DoctorsModel dm, boolean isMorning, DateModel dateModel){
        List<WPModel> wpModels = r.where(WPModel.class)
                .equalTo(WPModel.COL_DOCTOR_ID, dm.getId())
                .equalTo(WPModel.COL_DAY, dateModel.getDay())
                .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                .equalTo(WPModel.COL_IS_MORNING, isMorning)
                .equalTo(WPModel.COL_IS_UPLOADED, false)
                .greaterThan(WPModel.COL_COUNT, 0)
                .findAll();
        return wpModels.size() == 0;
    }


    public static List<WPProductsModel> getGiftProducts(Realm r, DateModel dateModel, WPUtilsModel wpUtilsModel, boolean isRegular, WPViewPager wpViewPager){
        int day = dateModel.getDay();
        int month = dateModel.getMonth();
        int year = dateModel.getYear();
        List<WPProductsModel> wpProductsModels = new ArrayList<>();
        List<GWDSModel> gwdsModels = r.where(GWDSModel.class)
                .equalTo(GWDSModel.COL_DOCTOR_ID, wpUtilsModel.getDocId())
                .equalTo(GWDSModel.COL_MONTH, month)
                .equalTo(GWDSModel.COL_YEAR, year)
                .equalTo(GWDSModel.COL_IS_APPROVED, true)
                .findAll();
        int planned = 0;
        for(GWDSModel gwdsm : gwdsModels){
            ProductModel gm = r.where(ProductModel.class)
                    .equalTo(ProductModel.COL_CODE, gwdsm.getGiftID())
                    .equalTo(ProductModel.COL_ITEM_FOR, isRegular?StringConstants.ITEM_FOR_REGULAR:StringConstants.ITEM_FOR_INTERN)
                    .findFirst();

            if(gm!=null ){
                int count = getCount(r, gm.getCode(), dateModel, wpUtilsModel);
                WPProductsModel wpProductsModel = new WPProductsModel();
                wpProductsModel.setCode(gm.getCode());
                wpProductsModel.setName(gm.getName()+"("+gm.getPackSize()+")");
                wpProductsModel.setQuantity(gm.getQuantity());
                wpProductsModel.setPlanned(getPlannedCount(r, gm.getCode(), month, year));
                wpProductsModel.setBalance(gm.getBalance());
                wpProductsModel.setCount((int)count);
                wpProductsModel.setGiven(isGiftGiven(r, gm.getCode(), gwdsm.getDoctorID(), month, year));
                wpProductsModels.add(wpProductsModel);
                if(count > 0)
                wpViewPager.addProduct(gm.getCode(), 2, count, gm.getName());

            }
        }

        return wpProductsModels;
    }

    public static List<WPProductsModel> getGiftProductsForIntern(Realm r, DateModel dateModel, WPUtilsModel wpUtilsModel, WPInternViewPager wpViewPager){
        int day = dateModel.getDay();
        int month = dateModel.getMonth();
        int year = dateModel.getYear();
        List<WPProductsModel> wpProductsModels = new ArrayList<>();

        int planned = 0;
        List<ProductModel> giftModelList;
        giftModelList = r.where(ProductModel.class)
                .equalTo(ProductModel.COL_MONTH, month)
                .equalTo(ProductModel.COL_YEAR, year)
                .equalTo(ProductModel.COL_ITEM_TYPE, StringConstants.GIFT_ITEM)
                .equalTo(ProductModel.COL_ITEM_FOR, StringConstants.ITEM_FOR_INTERN)
                .findAll();

        for (ProductModel gm : giftModelList) {
            int count = getCount(r, gm.getCode(), dateModel, wpUtilsModel);
            WPProductsModel wpProductsModel = new WPProductsModel();
            wpProductsModel.setCode(gm.getCode());
            wpProductsModel.setName(gm.getName()+"("+gm.getPackSize()+")");
            wpProductsModel.setQuantity(gm.getQuantity());
            wpProductsModel.setPlanned(getPlannedCount(r, gm.getCode(), month, year));
            wpProductsModel.setBalance(gm.getBalance());
            wpProductsModel.setCount((int)count);
            wpProductsModels.add(wpProductsModel);
            if(count > 0)
                wpViewPager.addProduct(gm.getCode(), 2, count, gm.getName());

            }

        return wpProductsModels;
    }

    public static List<WPModel> uniqueWP(List<WPModel> list) {
        List<WPModel> uniqueList = new ArrayList<>();
        Set<WPModel> uniqueSet = new HashSet<>();
        for (WPModel obj : list) {
            if (uniqueSet.add(obj)) {
                uniqueList.add(obj);
            }
        }
        return uniqueList;
    }


    public static List<WPProductsModel> getPromotedProducts(Realm r, DateModel dateModel, WPUtilsModel wpUtilsModel, WPViewPager wpViewPager){

        int day = dateModel.getDay();
        int month = dateModel.getMonth();
        int year = dateModel.getYear();
        List<WPProductsModel> wpProductsModels = new ArrayList<>();
        List<PWDSModel> pwdsModels = r.where(PWDSModel.class)
                    .equalTo(PWDSModel.COL_DOCTOR_ID, wpUtilsModel.getDocId())
                    .equalTo(PWDSModel.COL_MONTH, month)
                    .equalTo(PWDSModel.COL_YEAR, year)
                .equalTo(PWDSModel.COL_IS_APPROVED, true)
                .findAll();
            int planned = 0;
            for(PWDSModel pwdsm : pwdsModels){
                final ProductModel pm = r.where(ProductModel.class)
                        .equalTo(ProductModel.COL_CODE, pwdsm.getProductID())
                        .equalTo(ProductModel.COL_MONTH, month)
                        .equalTo(ProductModel.COL_YEAR, year)
                        .equalTo(ProductModel.COL_ITEM_TYPE, StringConstants.SELECTED_ITEM)
                        .equalTo(ProductModel.COL_ITEM_FOR, StringConstants.ITEM_FOR_REGULAR)
                        .findFirst();

                if(pm != null) {
                    int count = getCount(r, pm.getCode(), dateModel, wpUtilsModel);
                    WPProductsModel wpProductsModel = new WPProductsModel();
                    wpProductsModel.setCode(pm.getCode());
                    wpProductsModel.setName(pm.getName()+"("+pm.getPackSize()+")");
                    wpProductsModel.setQuantity(pm.getQuantity());
                    wpProductsModel.setPlanned(getPlannedCount(r, pm.getCode(), month, year));
                    wpProductsModel.setCount((int) count);
                    wpProductsModel.setBalance(pm.getBalance());
                    wpProductsModels.add(wpProductsModel);
                    if(count > 0)
                        wpViewPager.addProduct(pm.getCode(), 0, count, pm.getName());
                }
            }


        return wpProductsModels;
    }

    public static List<WPProductsModel> getSampleProducts(Realm r, List<ProductModel> sampleModels, DateModel dateModel, WPUtilsModel wpUtilsModel, WPViewPager wpViewPager){

        int day = dateModel.getDay();
        int month = dateModel.getMonth();
        int year = dateModel.getYear();
        List<WPProductsModel> wpProductsModels = new ArrayList<>();
        List<ProductModel> tempSamples = new ArrayList<>();
        List<ProductModel> nonSelectedSamples = new ArrayList<>();
        tempSamples.addAll(sampleModels);
        int planned = 0;
        Collections.sort(tempSamples, new Comparator<ProductModel>() {
            @Override
            public int compare(ProductModel sampleModel, ProductModel t1) {
                return sampleModel.getName().compareTo(t1.getName());
            }
        });
        nonSelectedSamples.addAll(tempSamples);
        for(ProductModel sampleModel : tempSamples){
            int count = getCount(r, sampleModel.getCode(), dateModel, wpUtilsModel);
            if(count > 0) {
                WPProductsModel wpProductsModel = new WPProductsModel();
                wpProductsModel.setCode(sampleModel.getCode());
                wpProductsModel.setName(sampleModel.getName()+"("+sampleModel.getPackSize()+")");
                wpProductsModel.setQuantity(sampleModel.getQuantity());
                wpProductsModel.setPlanned(getPlannedCount(r, sampleModel.getCode(), month, year));
                wpProductsModel.setCount((int)count);
                //wpProductsModel.setPackSize(sampleModel.getPackSize());
                wpProductsModel.setBalance(sampleModel.getBalance());
                wpViewPager.addProduct(sampleModel.getCode(), 1, count, sampleModel.getName());
                wpProductsModels.add(wpProductsModel);
                nonSelectedSamples.remove(sampleModel);
            }
        }

        for(ProductModel sampleModel : nonSelectedSamples){
            WPProductsModel wpProductsModel = new WPProductsModel();
            wpProductsModel.setCode(sampleModel.getCode());
            wpProductsModel.setName(sampleModel.getName()+"("+sampleModel.getPackSize()+")");
            wpProductsModel.setQuantity(sampleModel.getQuantity());
            wpProductsModel.setPlanned(getPlannedCount(r, sampleModel.getCode(), month, year));
            wpProductsModel.setCount(0);
            wpProductsModel.setBalance(sampleModel.getBalance());
            wpProductsModels.add(wpProductsModel);
        }



        return wpProductsModels;
    }


    public static List<WPProductsModel> getSampleProductsForIntern(Realm r, List<ProductModel> sampleModels, DateModel dateModel, WPUtilsModel wpUtilsModel, WPInternViewPager wpViewPager){

        int day = dateModel.getDay();
        int month = dateModel.getMonth();
        int year = dateModel.getYear();
        List<WPProductsModel> wpProductsModels = new ArrayList<>();
        List<ProductModel> tempSamples = new ArrayList<>();
        List<ProductModel> nonSelectedSamples = new ArrayList<>();
        tempSamples.addAll(sampleModels);
        int planned = 0;
        Collections.sort(tempSamples, new Comparator<ProductModel>() {
            @Override
            public int compare(ProductModel sampleModel, ProductModel t1) {
                return sampleModel.getName().compareTo(t1.getName());
            }
        });
        nonSelectedSamples.addAll(tempSamples);
        for(ProductModel sampleModel : tempSamples){
            int count = getCount(r, sampleModel.getCode(), dateModel, wpUtilsModel);
            if(count > 0) {
                WPProductsModel wpProductsModel = new WPProductsModel();
                wpProductsModel.setCode(sampleModel.getCode());
                wpProductsModel.setName(sampleModel.getName()+"("+sampleModel.getPackSize()+")");
                wpProductsModel.setQuantity(sampleModel.getQuantity());
                wpProductsModel.setPlanned(getPlannedCount(r, sampleModel.getCode(), month, year));
                wpProductsModel.setCount((int)count);
                wpProductsModel.setBalance(sampleModel.getBalance());
                wpViewPager.addProduct(sampleModel.getCode(), 1, count, sampleModel.getName());
                wpProductsModels.add(wpProductsModel);
                nonSelectedSamples.remove(sampleModel);
            }
        }

        for(ProductModel sampleModel : nonSelectedSamples){
            WPProductsModel wpProductsModel = new WPProductsModel();
            wpProductsModel.setCode(sampleModel.getCode());
            wpProductsModel.setName(sampleModel.getName()+"("+sampleModel.getPackSize()+")");
            wpProductsModel.setQuantity(sampleModel.getQuantity());
            wpProductsModel.setPlanned(getPlannedCount(r, sampleModel.getCode(), month, year));
            wpProductsModel.setCount(0);
            wpProductsModel.setBalance(sampleModel.getBalance());
            wpProductsModels.add(wpProductsModel);
        }



        return wpProductsModels;
    }

    //reuseable for wp's gift, sample, product. Day sample summary and dcr's gift, sample, product
    public static int getPlannedCount(Realm r, String code, int month, int year){
        List<WPModel> wpModels = r.where(WPModel.class)
                .equalTo(WPModel.COL_PRODUCT_ID, code)
                .equalTo(WPModel.COL_MONTH, month)
                .equalTo(WPModel.COL_YEAR, year)
                .findAll();
        int count = 0;
        for(WPModel wpModel : wpModels){
            count += wpModel.getCount();
        }
        return count;
    }
    //reuseable for wp's gift, sample, product. Day sample summary and dcr's gift, sample, product
    public static int getCount(Realm r, String code, DateModel dateModel, WPUtilsModel wpUtilsModel){
        WPModel wpModel = r.where(WPModel.class)
                .equalTo(WPModel.COL_DAY, dateModel.getDay())
                .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                .equalTo(WPModel.COL_PRODUCT_ID, code)
                .equalTo(WPModel.COL_DOCTOR_ID, wpUtilsModel.getDocId())
                .equalTo(WPModel.COL_IS_MORNING, wpUtilsModel.isMorning())
                .equalTo(WPModel.COL_INST_CODE, wpUtilsModel.getDocIns())
                .findFirst();
        int wpArray ;//= new long[2];
        if(wpModel != null) {
            wpArray = wpModel.getCount();
            return wpArray;
        }
        return 0;
    }
    //reuseable for wp's gift, sample, product. dcr's gift, sample, product
    public static void setCount(final WPProductsModel item, Realm realm, DateModel dateModel, WPUtilsModel wpUtilsModel, int flag){
        WPModel wpModel = realm.where(WPModel.class)
                .equalTo(WPModel.COL_DAY, dateModel.getDay())
                .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                .equalTo(WPModel.COL_PRODUCT_ID, item.getCode())
                .equalTo(WPModel.COL_DOCTOR_ID, wpUtilsModel.getDocId())
                .equalTo(WPModel.COL_IS_MORNING, wpUtilsModel.isMorning())
                .equalTo(WPModel.COL_INST_CODE, wpUtilsModel.getDocIns())
                .equalTo(WPModel.COL_FLAG, flag)
                .greaterThan(WPModel.COL_COUNT, 0)
                .findFirst();

                if(wpModel != null){
                    wpModel.setCount(item.getCount());
                    realm.insertOrUpdate(wpModel);
                } else {
                    WPModel wpModel1 = new WPModel();
                    wpModel1.setCount(item.getCount());
                    wpModel1.setProductID(item.getCode());
                    wpModel1.setName(item.getName());
                    wpModel1.setMorning(wpUtilsModel.isMorning());
                    wpModel1.setInstCode(wpUtilsModel.getDocIns());
                    wpModel1.setDoctorID(wpUtilsModel.getDocId());
                    wpModel1.setDay(dateModel.getDay());
                    wpModel1.setMonth(dateModel.getMonth());
                    wpModel1.setYear(dateModel.getYear());
                    wpModel1.setFlag(flag);
                    realm.insertOrUpdate(wpModel1);
                }
    }

    public static List<DSSModel> getDSSList(Realm realm, boolean isMorning, DateModel dateModel){
        List<DSSModel> dssModelList = new ArrayList<>();
        List<WPModel> wpModels = realm.where(WPModel.class)
                .equalTo(WPModel.COL_DAY, dateModel.getDay())
                .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                .equalTo(WPModel.COL_IS_MORNING, isMorning)
                .greaterThan(WPModel.COL_COUNT, 0)
                .distinct(WPModel.COL_PRODUCT_ID).findAll();
        for(WPModel wpModel : wpModels){
            DSSModel dssModel = new DSSModel();
            dssModel.setName(wpModel.getName());
            dssModel.setCount(getPlannedCountForToday(realm, wpModel.getProductID(), isMorning, dateModel, wpModel.getFlag()));
            dssModel.setCode(wpModel.getProductID());
            dssModel.setIntern(wpModel.getProductID().contains("I"));
            dssModel.setPackSize(getPackSize(realm, wpModel.getProductID(), wpModel.getFlag()));
            dssModel.setFlag(wpModel.getFlag());
            dssModelList.add(dssModel);
        }
        return dssModelList;
    }

    public static String getPackSize(Realm r, String productId, int flag){
        return r.where(ProductModel.class).equalTo(ProductModel.COL_CODE, productId).findFirst().getPackSize();
    }

    //public static boolean isInternSample(Realm r, String)

    public static int getPlannedCountForToday(Realm r, String code, boolean isMorning, DateModel dateModel, int flag){
        List<WPModel> wpModels = r.where(WPModel.class)
                .equalTo(WPModel.COL_PRODUCT_ID, code)
                .equalTo(WPModel.COL_DAY, dateModel.getDay())
                .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                .equalTo(WPModel.COL_FLAG, flag)
                .equalTo(WPModel.COL_IS_MORNING, isMorning)
                .findAll();
        int count = 0;
        for(WPModel wpModel : wpModels){
            count += wpModel.getCount();
        }
        return count;
    }

    public static String getDCRCount(Realm realm, int flag, String docId, int month, int year){
        return getTotalPlan(realm, flag, docId, month, year)+"/"+ getDCRExeCount(realm, flag, docId, month, year);
    }

    public static String getTotalPlan(Realm realm, int flag, String docId, int month, int year){
        Number count = realm.where(WPModel.class)
                        .equalTo(WPModel.COL_MONTH, month)
                        .equalTo(WPModel.COL_YEAR, year)
                        .equalTo(WPModel.COL_FLAG, flag)
                        .equalTo(WPModel.COL_DOCTOR_ID, docId)
                        .greaterThan(WPModel.COL_COUNT, 0)
                .sum(WPModel.COL_COUNT);
        return ""+count;
    }

    public static String getDCRExeCount(Realm realm, int flag, String docId, int month, int year){
        List<DCRModel> dcrModelList = realm.where(DCRModel.class)
                .equalTo(DCRModel.COL_MONTH, month)
                .equalTo(DCRModel.COL_YEAR, year)
                .equalTo(DCRModel.COL_DID, docId)
                .findAll();// m y
        long count = 0;
        for(DCRModel dcrModel: dcrModelList) {
            Number quantity = realm.where(DCRProductModel.class)
                    .equalTo(DCRProductModel.COL_DCR_ID, dcrModel.getId())
                    .equalTo(DCRProductModel.COL_TYPE, flag)
                    .greaterThan(DCRProductModel.COL_QUANTITY, 0)
                    .sum(DCRProductModel.COL_QUANTITY);
            count = count + (long) quantity;
        }
        return ""+count;
    }

    public static List<WPModelForSend> getWPForSend(Realm r, DateModel dateModel){
        List<WPModelForSend> wpModelForSendList = new ArrayList<>();
        RealmResults<WPModel> wpModels = r.where(WPModel.class)
                .equalTo(WPModel.COL_DAY, dateModel.getDay())
                .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                .greaterThan(WPModel.COL_COUNT, 0)
                .distinct(WPModel.COL_PRODUCT_ID).findAll();

        for(WPModel wpModel:wpModels){
            List<WPDoctorsModelForSend> wpDoctorForSendList= new ArrayList<>();
            WPModelForSend wpModelForSend = new WPModelForSend();
            wpModelForSend.setProductCode(wpModel.getProductID());
            RealmResults<WPModel> wpModelDetail = r.where(WPModel.class)
                    .equalTo(WPModel.COL_DAY, dateModel.getDay())
                    .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                    .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                    .equalTo(WPModel.COL_PRODUCT_ID, wpModel.getProductID())
                    .greaterThan(WPModel.COL_COUNT, 0)
                    .distinct(WPModel.COL_DOCTOR_ID).findAll();
            for(WPModel wpDetail:wpModelDetail){
                WPDoctorsModelForSend wpDoctorForSend = new WPDoctorsModelForSend();
                DoctorsModel dm = r.where(DoctorsModel.class).equalTo(DoctorsModel.COL_ID, wpDetail.getDoctorID()).findFirst();
                if(dm != null) {
                    wpDoctorForSend.setDoctorID(wpDetail.getDoctorID());
                    wpDoctorForSend.setQuantity(String.valueOf(wpDetail.getCount()));
                    wpDoctorForSend.setAnSL("0");
                    wpDoctorForSend.setInstiCode(wpDetail.getInstCode());
                    wpDoctorForSend.setShiftName(wpDetail.isMorning() ? StringConstants.MORNING : StringConstants.EVENING);
                    wpDoctorForSendList.add(wpDoctorForSend);
                }
            }
            wpModelForSend.setDetailList(wpDoctorForSendList);
            wpModelForSendList.add(wpModelForSend);
        }
        return wpModelForSendList;
    }
    //New plain format
    public static List<WPForSend> getWorkPlanForSend(Realm r, DateModel dateModel, String marketCode){
        List<WPForSend> wpForSendList = new ArrayList<>();
        RealmResults<WPModel> wpModels = r.where(WPModel.class)
                .equalTo(WPModel.COL_DAY, dateModel.getDay())
                .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                .greaterThan(WPModel.COL_COUNT, 0)
                .findAll();

        for(WPModel wpModel:wpModels){
            WPForSend wpForSend = new WPForSend();
            wpForSend.setMarketCode(marketCode);
            wpForSend.setDoctorID(wpModel.getDoctorID());
            wpForSend.setInstiCode(wpModel.getInstCode());
            wpForSend.setQuantity(String.valueOf(wpModel.getCount()));
            wpForSend.setProductCode(wpModel.getProductID());
            wpForSend.setShiftName(wpModel.isMorning()?"m":"e");
            wpForSendList.add(wpForSend);
        }
        return wpForSendList;
    }

    public static void setWorkPlanAfterSend(Realm r, DateModel dateModel){
        List<WPForSend> wpForSendList = new ArrayList<>();
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<WPModel> wpModels = r.where(WPModel.class)
                        .equalTo(WPModel.COL_DAY, dateModel.getDay())
                        .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                        .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                        .greaterThan(WPModel.COL_COUNT, 0)
                        .findAll();

                for(WPModel wpModel:wpModels){
                    wpModel.setUploaded(true);
                }
            }
        });

    }
    

    public static List<SampleProductsModel> getSampleModels(Realm r, List<ProductModel> sampleModels, DateModel dateModel){
        int day = dateModel.getDay();
        int month = dateModel.getMonth();
        int year = dateModel.getYear();
        List<SampleProductsModel> sampleProductsModelList = new ArrayList<>();
        int planned = 0;
        for(final ProductModel sampleModel : sampleModels){
            //int count = getCount(r, sampleModel.getCode());
            SampleProductsModel wpProductsModel = new SampleProductsModel();
            wpProductsModel.setCode(sampleModel.getCode());
            wpProductsModel.setName(sampleModel.getName()+"("+sampleModel.getPackSize()+")");
            wpProductsModel.setQuantity(sampleModel.getQuantity());
            wpProductsModel.setPlanned(getPlannedCount(r, sampleModel.getCode(), month, year));
            wpProductsModel.setCount(1);
            wpProductsModel.setpType(1);
            wpProductsModel.setBalance(sampleModel.getBalance());
            sampleProductsModelList.add(wpProductsModel);

        }
        return sampleProductsModelList;
    }

    public static List<SampleProductsModel> getSampleProductModels(Realm r, List<ProductModel> productModels, DateModel dateModel){
        int day = dateModel.getDay();
        int month = dateModel.getMonth();
        int year = dateModel.getYear();
        List<SampleProductsModel> sampleProductsModelList = new ArrayList<>();
        int planned = 0;
        for(final ProductModel sampleModel : productModels){
            //int count = getCount(r, sampleModel.getCode());
            SampleProductsModel wpProductsModel = new SampleProductsModel();
            wpProductsModel.setCode(sampleModel.getCode());
            wpProductsModel.setName(sampleModel.getName()+"("+sampleModel.getPackSize()+")");
            wpProductsModel.setQuantity(sampleModel.getQuantity());
            wpProductsModel.setPlanned(getPlannedCount(r, sampleModel.getCode(), month, year));
            wpProductsModel.setCount(1);
            wpProductsModel.setpType(0);
            wpProductsModel.setDoctorNotSelected(getIsNotInPWDS(r, sampleModel.getCode(), month, year));
            wpProductsModel.setBalance(sampleModel.getBalance());
            sampleProductsModelList.add(wpProductsModel);

        }
        return sampleProductsModelList;
    }

    public static boolean getIsNotInPWDS(Realm r, String productCode, int month, int year){
        if(r.where(PWDSModel.class)
                .equalTo(PWDSModel.COL_DOCTOR_ID, DCRUtils.DOCTOR_ID)
        .equalTo(PWDSModel.COL_PRODUCT_ID, productCode)
        .equalTo(PWDSModel.COL_MONTH, month)
        .equalTo(PWDSModel.COL_YEAR, year)
        .equalTo(PWDSModel.COL_IS_APPROVED, true).count() == 1){
            return false;
        } else {
            return true;
        }
    }

    public static List<SampleProductsModel> getSampleGiftModels(Realm r, List<ProductModel> giftModels, DateModel dateModel){
        int day = dateModel.getDay();
        int month = dateModel.getMonth();
        int year = dateModel.getYear();
        List<SampleProductsModel> sampleProductsModelList = new ArrayList<>();
        for(final ProductModel sampleModel : giftModels){
            //int count = getCount(r, sampleModel.getCode());
            SampleProductsModel wpProductsModel = new SampleProductsModel();
            wpProductsModel.setCode(sampleModel.getCode());
            wpProductsModel.setName(sampleModel.getName()+"("+sampleModel.getPackSize()+")");
            wpProductsModel.setQuantity(sampleModel.getQuantity());
            wpProductsModel.setCount(1);
            wpProductsModel.setpType(2);
            wpProductsModel.setDoctorNotSelected(getIsNotInGWDS(r, sampleModel.getCode(), month, year));
            wpProductsModel.setPlanned(getPlannedCount(r, sampleModel.getCode(), month, year));
            wpProductsModel.setBalance(sampleModel.getBalance());
            sampleProductsModelList.add(wpProductsModel);

        }
        return sampleProductsModelList;
    }

    public static boolean getIsNotInGWDS(Realm r, String productCode, int month, int year){
        if(r.where(GWDSModel.class)
                .equalTo(GWDSModel.COL_DOCTOR_ID, DCRUtils.DOCTOR_ID)
                .equalTo(GWDSModel.COL_GIFT_ID, productCode)
                .equalTo(GWDSModel.COL_MONTH, month)
                .equalTo(GWDSModel.COL_YEAR, year)
                .equalTo(GWDSModel.COL_IS_APPROVED, true).count() == 1){
            return false;
        } else {
            return true;
        }
    }



    public static boolean isGiftGiven(Realm r, String code, String docID, int month, int year){

        boolean isGiven = false;
        RealmResults<DCRModel> dcrModels = r.where(DCRModel.class)
                .equalTo(DCRModel.COL_MONTH, month)
                .equalTo(DCRModel.COL_YEAR, year)
                .equalTo(DCRModel.COL_DID, docID)
                .findAll();
        for(DCRModel dcrModelCount:dcrModels){
            DCRProductModel dcrProductModel = null;
            dcrProductModel = r.where(DCRProductModel.class)
                    .equalTo(DCRProductModel.COL_DCR_ID, dcrModelCount.getId())
                    .equalTo(DCRProductModel.COL_PRODUCT_ID, code)
                    .greaterThan(DCRProductModel.COL_QUANTITY, 0)
                    .findFirst();
            if(dcrProductModel != null){
                isGiven = true;
            }
        }
        return isGiven;
    }
}
