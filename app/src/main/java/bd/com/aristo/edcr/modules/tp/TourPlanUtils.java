package bd.com.aristo.edcr.modules.tp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.modules.tp.model.PlaceModel;
import bd.com.aristo.edcr.modules.tp.model.TPModel;
import bd.com.aristo.edcr.modules.tp.model.TPPlaceRealmModel;
import bd.com.aristo.edcr.modules.tp.model.TPServerModel;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static bd.com.aristo.edcr.modules.dvr.DVRUtils.TAG;

/**
 * Created by monir.sobuj on 6/15/2017.
 */

public class TourPlanUtils {


    public static String getTPListForSend(Realm realm, int month, int year){
        List<TPModel> tpSendList = new ArrayList<>();
        List<TPServerModel> tpList = realm.where(TPServerModel.class)
                .equalTo(TPServerModel.COL_YEAR, year)
                .equalTo(TPServerModel.COL_MONTH, month)
                .equalTo(TPServerModel.COL_IS_CHANGED, true)
                .notEqualTo(TPServerModel.COL_CONTACT, "")
                .findAll();
        for (TPServerModel tpModel:tpList ){


            TPModel tpModelSend = new TPModel();
            RealmResults<TPPlaceRealmModel> placeList = realm.where(TPPlaceRealmModel.class)
                    .equalTo(TPPlaceRealmModel.COL_TP_ID, tpModel.getLocalId())
                    .findAll();

            Log.e(TAG, ""+placeList.size());
            tpModelSend.setDayNumber(DateTimeUtils.getMonthNumber(Integer.valueOf(tpModel.getDay())));  // tpModel.getDay()
            tpModelSend.setCalendarCell(String.valueOf(tpModel.getcCell()));
            tpModelSend.setMeetingPlace(tpModel.getContactPlace());
            tpModelSend.setAllowanceNature(tpModel.getnDA());
            tpModelSend.setTime(tpModel.getReportTime());
            tpModelSend.setShift(tpModel.getShift().equalsIgnoreCase(StringConstants.MORNING)?StringConstants.MORNING:StringConstants.EVENING);  // tpModel.getShift()
            tpModelSend.setAnDetailSL(String.valueOf(tpModel.getLocalId()));
            tpModelSend.setDetailSL(tpModel.getServerId());
            tpModelSend.setYear(String.valueOf(tpModel.getYear()));
            tpModelSend.setMonth(DateTimeUtils.getMonthNumber(tpModel.getMonth()));
            tpModelSend.setShiftType(tpModel.getShiftType());
            List<PlaceModel> workPlaces = new ArrayList<>();

            if(tpModelSend.getShiftType().equalsIgnoreCase(StringConstants.MEETING)){
                PlaceModel place = new PlaceModel();
                place.setInstitutionCode("Meeting");
                place.setShift(tpModel.getShift());
                workPlaces.add(place);
            } else if(tpModelSend.getShiftType().equalsIgnoreCase(StringConstants.LEAVE)){
                PlaceModel place = new PlaceModel();
                place.setInstitutionCode("Leave");
                place.setShift(tpModel.getShift());// tpModel.getShift()
                workPlaces.add(place);
            } else {
                for(TPPlaceRealmModel workPlaceModel: placeList){
                    PlaceModel place = new PlaceModel();
                    place.setInstitutionCode(workPlaceModel.getCode());
                    place.setShift(workPlaceModel.getShift()); // workPlaceModel.getShift()
                    place.setId(workPlaceModel.getId());
                    workPlaces.add(place);

                }
            }
            tpModelSend.setSubDetailList(workPlaces);


            if(tpModelSend.getTime() != null && !tpModelSend.getTime().equals(""))
                tpSendList.add(tpModelSend);

        }

        Log.e("Utils", tpList.size()+"");

        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = gson.toJsonTree(tpSendList).getAsJsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("DetailList", jsonArray);

        if (jsonArray.size()<1){
            return null;
        }else{
            return jsonObject.toString();
        }

    }

    public static void updateTPAfterSend(Realm realm, final int month, final int year){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm r) {
                List<TPServerModel> tpList = r.where(TPServerModel.class)
                        .equalTo(TPServerModel.COL_YEAR, year)
                        .equalTo(TPServerModel.COL_MONTH, month)
                        .equalTo(TPServerModel.COL_IS_CHANGED, true)
                        .notEqualTo(TPServerModel.COL_CONTACT, "")
                        .findAll();
                for (TPServerModel tpModel:tpList ) {
                    tpModel.setUploaded(true);
                    r.insertOrUpdate(tpModel);
                }
            }
        });


    }



    public static long getPrevIdForPlace(Realm r){
        Number currentIdNum = r.where(TPPlaceRealmModel.class).max(TPPlaceRealmModel.COL_ID);
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue();
        }
        return nextId;
    }

    public static List<String> getSavedTpDayList(Realm realm, int month, int year){
        List<String> savedTpDayListTemp = new ArrayList<>();

        List<Integer> numTpDayListTemp = new ArrayList<>();
        List<TPServerModel> savedTps = realm.where(TPServerModel.class)
                .equalTo(TPServerModel.COL_MONTH, month)
                .equalTo(TPServerModel.COL_YEAR, year)
                .notEqualTo(TPServerModel.COL_CONTACT, "")
                .sort(TPServerModel.COL_DAY, Sort.ASCENDING).findAll();
        for(TPServerModel tps: savedTps){
            if (!numTpDayListTemp.contains(Integer.valueOf(tps.getDay()))){
                numTpDayListTemp.add(Integer.valueOf(tps.getDay()));
            }
        }
        Collections.sort(numTpDayListTemp);
        for (int k=0;k<numTpDayListTemp.size();k++){
            savedTpDayListTemp.add(String.valueOf(numTpDayListTemp.get(k)));
        }

        savedTpDayListTemp.add(0,"copy");

        return savedTpDayListTemp;
    }




}
