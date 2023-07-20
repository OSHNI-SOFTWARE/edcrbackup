package bd.com.aristo.edcr.modules.reports.others;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.Day;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.modules.dvr.model.DVRDoctorRealm;
import bd.com.aristo.edcr.modules.dvr.model.DVRForServer;
import bd.com.aristo.edcr.modules.tp.model.TPPlaceRealmModel;
import bd.com.aristo.edcr.modules.tp.model.TPServerModel;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by monir.sobuj on 02/11/17.
 */

public class DVRSummaryFragment extends Fragment {

    private static final String TAG = "DoctorsFragment";
    List<DoctorsModel> doctorsModels;
    @Inject
    Realm r;

    @BindView(R.id.doctorList)
    RecyclerView pwdsList;

    @BindView(R.id.cardBottom)
    CardView cardBottom;


    @BindView(R.id.llSearch)
    LinearLayout llSearch;

    int month, year, lastDay;

    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public DVRSummaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_pwds_doctor, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        cardBottom.setVisibility(View.GONE);
        llSearch.setVisibility(View.GONE);
        month = getArguments().getInt("month");
        year = getArguments().getInt("year");
        lastDay = getLastDate(month, year);
        setupList();
        setTitle();

        return rootView;
    }


    public void setTitle(){
        ((Activity) context).setTitle("DVR Summary for "+DateTimeUtils.getMonthForInt(month));
    }


    private void setupList() {
        refreshList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dvr, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void refreshList(){


       final FastItemAdapter<IDVRSummaryModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(getDvrSummary());
        fastAdapter.withSelectable(false);
        fastAdapter.setHasStableIds(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        pwdsList.setLayoutManager(layoutManager);
        pwdsList.setAdapter(fastAdapter);
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<IDVRSummaryModel>() {
            @Override
            public boolean onClick(View v, IAdapter<IDVRSummaryModel> adapter, IDVRSummaryModel item, int position) {
                Day date = new Day();
                date.setCell(position);
                date.setDay(Integer.valueOf(item.getId().substring(5, 7)));
                date.setMonth(month);
                date.setYear(year);
                date.setLastDay(lastDay);
                date.setCopyDate(Integer.valueOf(item.getId().substring(5, 7)));
                //DVRActivity.start(((Activity) context), date);
                return false;
            }
        });


    }


    public List<IDVRSummaryModel> getDvrSummary(){
        List<IDVRSummaryModel> idvrSummaryModelList = new ArrayList<>();
        for (int i = 1; i< lastDay + 1; i++){
            String morningCount = "Morning";
            String eveningCount = "Evening";
            StringBuilder morningList = new StringBuilder();
            StringBuilder eveningList = new StringBuilder();

            StringBuilder morningLoc = new StringBuilder();
            StringBuilder eveningLoc = new StringBuilder();
            TPServerModel morningTP = r.where(TPServerModel.class)
                    .equalTo(TPServerModel.COL_DAY, String.valueOf(i))
                    .equalTo(TPServerModel.COL_MONTH, month)
                    .equalTo(TPServerModel.COL_YEAR, year)
                    .equalTo(TPServerModel.COL_SHIFT, StringConstants.MORNING)
                    .findFirst();
            TPServerModel eveningTP = r.where(TPServerModel.class)
                    .equalTo(TPServerModel.COL_DAY, String.valueOf(i))
                    .equalTo(TPServerModel.COL_MONTH, month)
                    .equalTo(TPServerModel.COL_YEAR, year)
                    .equalTo(TPServerModel.COL_SHIFT, StringConstants.EVENING)
                    .findFirst();
            if(null == morningTP || null == eveningTP){
                displayAlert();
                return idvrSummaryModelList;
            }

            RealmResults<TPPlaceRealmModel> morningPlaces = r.where(TPPlaceRealmModel.class)
                    .equalTo(TPPlaceRealmModel.COL_TP_ID, morningTP.getLocalId())
                    .findAll();
            RealmResults<TPPlaceRealmModel> eveningPlaces = r.where(TPPlaceRealmModel.class)
                    .equalTo(TPPlaceRealmModel.COL_TP_ID, morningTP.getLocalId())
                    .findAll();
            if(morningPlaces != null && morningPlaces.size() > 0){
                String prefix = "";
                for(TPPlaceRealmModel morningDoctor:morningPlaces){
                    if(morningDoctor != null) {
                        morningLoc.append(prefix+morningDoctor.getCode());
                    }
                    prefix = "\n";
                }
            } else {
                morningLoc.append("No location");
            }

            if(eveningPlaces != null && eveningPlaces.size() > 0){
                String prefix = "";
                for(TPPlaceRealmModel morningDoctor:eveningPlaces){
                    if(morningDoctor != null) {
                        eveningLoc.append(prefix+morningDoctor.getCode());
                    }
                    prefix = "\n";
                }
            } else {
                eveningLoc.append("No location");
            }

            DVRForServer morningDVR = r.where(DVRForServer.class)
                    .equalTo(DVRForServer.COL_DAY, i)
                    .equalTo(DVRForServer.COL_MONTH, month)
                    .equalTo(DVRForServer.COL_YEAR, year)
                    .equalTo(DVRForServer.COL_SHIFT, true)
                    .findFirst();
            DVRForServer eveningDVR = r.where(DVRForServer.class)
                    .equalTo(DVRForServer.COL_DAY, i)
                    .equalTo(DVRForServer.COL_MONTH, month)
                    .equalTo(DVRForServer.COL_YEAR, year)
                    .equalTo(DVRForServer.COL_SHIFT, false)
                    .findFirst();
            if(morningDVR != null){
                List<DVRDoctorRealm> morningDVRDoctors = r.where(DVRDoctorRealm.class).equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, morningDVR.getId()).findAll();
                long countM = r.where(DVRDoctorRealm.class).equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, morningDVR.getId()).count();
                if(morningDVRDoctors != null && morningDVRDoctors.size() > 0){
                    int j = 1;
                    String prefix = "";
                    for(DVRDoctorRealm morningDoctor:morningDVRDoctors){
                        DoctorsModel doctorsModel = r.where(DoctorsModel.class).equalTo(DoctorsModel.COL_ID, morningDoctor.getDoctorID()).findFirst();
                        if(doctorsModel != null) {
                            morningList.append(prefix+j + ". " + doctorsModel.getName() + " [" + morningDoctor.getDoctorID() + "]");
                        } else {
                            morningList.append(prefix+j + ". " + "Name not Found" + " [" + morningDoctor.getDoctorID() + "]");
                        }
                        j++;
                        prefix = "\n";
                    }
                } else {
                    morningList.append("No DVR found");
                }
                morningCount = morningCount+"("+countM+")";
            } else {
                morningCount = morningCount+"(0)";
                morningList.append("No DVR found");
            }


            if(eveningDVR != null){
                List<DVRDoctorRealm> eveningDVRDoctors = r.where(DVRDoctorRealm.class).equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, eveningDVR.getId()).findAll();
                long countE = r.where(DVRDoctorRealm.class).equalTo(DVRDoctorRealm.COL_DVR_LOCAL_ID, eveningDVR.getId()).count();
                if(eveningDVRDoctors != null && eveningDVRDoctors.size() > 0){
                    int j = 1;
                    String prefix = "";
                    for(DVRDoctorRealm eveningDoctor:eveningDVRDoctors){
                        DoctorsModel doctorsModel = r.where(DoctorsModel.class).equalTo(DoctorsModel.COL_ID, eveningDoctor.getDoctorID()).findFirst();
                        if(doctorsModel != null) {
                            eveningList.append(prefix+j + ". " + doctorsModel.getName() + " [" + eveningDoctor.getDoctorID() + "]");
                        } else {
                            eveningList.append(prefix+j + ". " + "Name not Found" + " [" + eveningDoctor.getDoctorID() + "]");
                        }
                        j++;
                        prefix = "\n";
                    }
                } else {
                    eveningList.append("No DVR found");
                }
                eveningCount = eveningCount+"("+countE+")";
            } else {
                eveningCount = eveningCount+"(0)";
                eveningList.append("No DVR found");
            }

            IDVRSummaryModel idvrSummaryModel = new IDVRSummaryModel();
            idvrSummaryModel.setEveningCount(eveningCount);
            idvrSummaryModel.setMorningCount(morningCount);
            //idvrSummaryModel.setMorningList(morningList.toString());
            idvrSummaryModel.setMorningLoc(morningLoc.toString());
           // idvrSummaryModel.setEveningList(eveningList.toString());
            idvrSummaryModel.setEveningLoc(eveningLoc.toString());
            idvrSummaryModel.setId(formatDate(i));
            idvrSummaryModel.withIdentifier(i);
            idvrSummaryModelList.add(idvrSummaryModel);

        }

        return idvrSummaryModelList;
    }


    @Override
    public void onPause() {
        super.onPause();
        //onCancel();
    }

    private int getLastDate(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public void displayAlert(){
        String msg = "No TP Found. Please Create or Sync TP.";
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(msg);
        alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                ((Activity) context).onBackPressed();
            }
        });
        alert.show();
    }
    public String formatDate(int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        Date date1 = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EE, dd");
        String hireDate = sdf.format(date1);
        return hireDate;
    }
}
