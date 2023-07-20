package bd.com.aristo.edcr.modules.dvr;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bd.com.aristo.edcr.modules.dvr.model.DVRDoctor;
import bd.com.aristo.edcr.modules.dvr.model.DVRDoctorRealm;
import bd.com.aristo.edcr.modules.dvr.model.DVRForSend;
import bd.com.aristo.edcr.modules.dvr.model.DVRForSendMaster;
import bd.com.aristo.edcr.modules.dvr.model.DVRForServer;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by monir.sobuj on 12/24/2017.
 */

public class DVRUtils {
    public static final String TAG = "DVRUtils";
    public static boolean isApproved = false;


    /*public static long getDVRDoctorID(Realm r){

        Number currentIdNum = r.where(DVRDoctorRealm.class).max(DVRDoctorRealm.COL_ID);
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() ;
        }
        return nextId;
    }*/

    public static boolean hasDoctorInDVR(Realm r, String doctorId, int day, int month, int year, boolean isMorning){
        DVRForServer dvrForServer = r.where(DVRForServer.class)
                .equalTo(DVRForServer.COL_DAY, day)
                .equalTo(DVRForServer.COL_MONTH, month)
                .equalTo(DVRForServer.COL_YEAR, year)
                .equalTo(DVRForServer.COL_SHIFT, isMorning)
                .findFirst();
        if(dvrForServer != null){
            DVRDoctorRealm dvrDoctorRealm = r.where(DVRDoctorRealm.class)
                    .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, dvrForServer.getId())
                    .equalTo(DVRDoctorRealm.COL_DOCTOR_ID, doctorId)
                    .findFirst();
            if (dvrDoctorRealm != null){
                return true;
            }
        }
        return false;
    }

//need
    public static List<String> getSavedDVRDayList(Realm realm, int month, int year){
        List<String> savedDVRDayList = new ArrayList<>();
        List<DVRForServer> savedDVRs = realm.where(DVRForServer.class)
                .equalTo(DVRForServer.COL_MONTH, DateTimeUtils.getMonthNumber(month))
                .equalTo(DVRForServer.COL_YEAR, String.valueOf(year))
                .findAll();
        List<Integer> numDVRDayListTemp = new ArrayList<>();
        for(DVRForServer dvrs: savedDVRs){
            if (!numDVRDayListTemp.contains(dvrs.getDay())){
                numDVRDayListTemp.add(Integer.valueOf(dvrs.getDay()));
            }
        }
        Collections.sort(numDVRDayListTemp);
        for (int k = 0; k < numDVRDayListTemp.size(); k++){
            savedDVRDayList.add(String.valueOf(numDVRDayListTemp.get(k)));
        }

        return savedDVRDayList;
    }

    public static List<Integer> getSavedDVRDays(Realm realm, int month, int year){
        List<DVRForServer> savedDVRs = realm.where(DVRForServer.class)
                .equalTo(DVRForServer.COL_MONTH, DateTimeUtils.getMonthNumber(month))
                .equalTo(DVRForServer.COL_YEAR, String.valueOf(year))
                .findAll();
        List<Integer> numDVRDayListTemp = new ArrayList<>();
        for(DVRForServer dvrs: savedDVRs){
            if (!numDVRDayListTemp.contains(dvrs.getDay())){
                numDVRDayListTemp.add(Integer.valueOf(dvrs.getDay()));
            }
        }
        Collections.sort(numDVRDayListTemp);

        return numDVRDayListTemp;
    }

    //need
    public static List<String> getApprovedDVRDayList(Realm realm, int month, int year){
        List<String> savedDVRDayList = new ArrayList<>();
        List<DVRForServer> savedDVRs = realm.where(DVRForServer.class)
                .equalTo(DVRForServer.COL_MONTH, DateTimeUtils.getMonthNumber(month))
                .equalTo(DVRForServer.COL_YEAR, String.valueOf(year))
                .equalTo(DVRForServer.COL_STATUS, StringConstants.APPROVED_STATUS_APPROVED)
                .findAll();
        for(DVRForServer dvrs: savedDVRs){
            savedDVRDayList.add(String.valueOf(dvrs.getDay()));
            Log.e(TAG, String.valueOf(dvrs.getDay()));
        }
        return savedDVRDayList;
    }

    //need
    public static List<String> getInitializeDVRDayList(Realm realm, int month, int year){
        List<String> savedDVRDayList = new ArrayList<>();
        List<DVRForServer> savedDVRs = realm.where(DVRForServer.class)
                .equalTo(DVRForServer.COL_MONTH, DateTimeUtils.getMonthNumber(month))
                .equalTo(DVRForServer.COL_YEAR, String.valueOf(year))
                .equalTo(DVRForServer.COL_INIT, true)
                .findAll();
        for(DVRForServer dvrs: savedDVRs){
            savedDVRDayList.add(String.valueOf(dvrs.getDay()));
            Log.e(TAG, String.valueOf(dvrs.getDay()));
        }
        return savedDVRDayList;
    }

    //need
    public static boolean isApproved(Realm realm, int month, int year){
        List<String> savedDVRDayList = new ArrayList<>();
        List<DVRForServer> savedDVRs = realm.where(DVRForServer.class)
                .equalTo(DVRForServer.COL_MONTH, month)
                .equalTo(DVRForServer.COL_YEAR, year)
                .equalTo(DVRForServer.COL_STATUS, true)
                .findAll();
        for(DVRForServer dvrs: savedDVRs){
            savedDVRDayList.add(String.valueOf(dvrs.getDay()));
            Log.e(TAG, String.valueOf(dvrs.getDay()));
        }
        return savedDVRDayList.size() > 0;
    }

    public static boolean isApprovedDayWise(Realm realm, int month, int year, int day){
        if(day < 6){
            return true;
        }
        List<DVRForServer> savedDVRs = realm.where(DVRForServer.class)
                .equalTo(DVRForServer.COL_DAY, day)
                .equalTo(DVRForServer.COL_MONTH, month)
                .equalTo(DVRForServer.COL_YEAR, year)
                .equalTo(DVRForServer.COL_STATUS, true)
                .findAll();
        if(savedDVRs != null) {
            return savedDVRs.size() > 0;
        } else {
            return false;
        }
    }


    public static void deletePreviousMonthlyDVR(Realm r, int month, int year){
        RealmResults<DVRForServer> dvrServerModels =r.where(DVRForServer.class)
                .equalTo(DVRForServer.COL_MONTH, month)
                .equalTo(DVRForServer.COL_YEAR, year)
                .findAll();
        for(DVRForServer dvrServerModel:dvrServerModels){
            RealmResults<DVRDoctorRealm> dvrDoctorRealms = r.where(DVRDoctorRealm.class)
                    .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, dvrServerModel.getId())
                    .findAll();
            dvrDoctorRealms.deleteAllFromRealm();
        }
        dvrServerModels.deleteAllFromRealm();

    }



    //need
    public static List<DVRForSendMaster> getDVRForSend(Realm realm, int month, int year) {
        List<Integer> days = new ArrayList<>();
        RealmResults<DVRForServer> dvrRealmModelList = realm.where(DVRForServer.class)
                .equalTo(DVRForServer.COL_MONTH, month)
                .equalTo(DVRForServer.COL_YEAR, year)
                .equalTo(DVRForServer.COL_STATUS, false)
                .findAll();
        for(DVRForServer dvrForServer:dvrRealmModelList){
            days.add(dvrForServer.getDay());
        }

        Set<Integer> daySet = new HashSet<>(days);
        days.clear();
        days.addAll(daySet);  // distinct days
        List<DVRForSendMaster> dvrForSendMasters = new ArrayList<>();
        for (int day:days) {
            DVRForSendMaster dvrForSendMaster = new DVRForSendMaster();
            dvrForSendMaster.setDayNumber(DateTimeUtils.getMonthNumber(day));
            RealmResults<DVRForServer> dvrForServers = realm.where(DVRForServer.class)
                    .equalTo(DVRForServer.COL_DAY, day)
                    .equalTo(DVRForServer.COL_MONTH, month)
                    .equalTo(DVRForServer.COL_YEAR, year)
                    .findAll();
            List<DVRForSend> dvrForSendList = new ArrayList<>();
            for (DVRForServer dvrForServer : dvrForServers) {
                DVRForSend dvrForSend = new DVRForSend();
                dvrForSend.setShift(dvrForServer.isMorning()?StringConstants.MORNING:StringConstants.EVENING);
                dvrForSend.setDetailSL(dvrForServer.getServerId());
                List<DVRDoctorRealm> dvrDoctorRealms = realm.where(DVRDoctorRealm.class)
                        .equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, dvrForServer.getId())
                        .findAll();
                List<DVRDoctor> dvrDoctorList = new ArrayList<>();
                for (DVRDoctorRealm dvrDoctorRealm : dvrDoctorRealms) {
                    DVRDoctor dvrDoctor = new DVRDoctor();
                    dvrDoctor.setDoctorID(dvrDoctorRealm.getDoctorID());
                    dvrDoctorList.add(dvrDoctor);
                }
                dvrForSend.setSubDetailList(dvrDoctorList);
                dvrForSendList.add(dvrForSend);
            }
            dvrForSendMaster.setDetailList(dvrForSendList);
            dvrForSendMasters.add(dvrForSendMaster);
        }
        return dvrForSendMasters;
    }
}
