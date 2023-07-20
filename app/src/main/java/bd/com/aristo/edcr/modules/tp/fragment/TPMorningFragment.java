package bd.com.aristo.edcr.modules.tp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.tp.activity.WorkPlaceActivity;
import bd.com.aristo.edcr.modules.tp.model.AddressModel;
import bd.com.aristo.edcr.models.Day;
import bd.com.aristo.edcr.modules.tp.model.ITPPlacesModel;
import bd.com.aristo.edcr.modules.tp.model.TPMorningModel;
import bd.com.aristo.edcr.modules.tp.model.TPPlaceRealmModel;
import bd.com.aristo.edcr.modules.tp.model.TPServerModel;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.buttons.AButton;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by monir.sobuj on 6/15/17.
 */

public class TPMorningFragment extends Fragment {
    // Bundle savedInstanceState;

    List<String> shiftTypesList                         = new ArrayList<>(Arrays.asList("Working","Meeting","Leave"));//Arrays.asList("Working","Meeting","Leave");
    //List<String> timeHour                               = new ArrayList<>(Arrays.asList("01","02","03","04","05","06", "07","08","09","10","11","12"));//Arrays.asList("Working","Meeting","Leave");

    @Inject
    List<String> natureOfDA;

    List<String> minutes                                = new ArrayList<>(Arrays.asList("00", "15", "30", "45"));
    List<String> hoursMorning                           = new ArrayList<>(Arrays.asList("08", "09", "10", "11", "12", "01", "02", "03", "04", "05", "06", "07"));

    @Inject
    Realm r;

    @BindView(R.id.shiftTypeSpinner)
    AppCompatSpinner shiftTypeSpinner;

    @BindView(R.id.spDA)
    AppCompatSpinner spNatureOfDa;

    @BindView(R.id.spReportMinute)
    AppCompatSpinner spReportMinute;

    @BindView(R.id.spReportHour)
    AppCompatSpinner spReportHour;

    @BindView(R.id.rvWorkPlace)
    RecyclerView rvWorkPlace;

    @BindView(R.id.etContact)
    AutoCompleteTextView etContact;

    @BindView(R.id.llFab)
    LinearLayout btnAddPlaces;

    @BindView(R.id.btnReset)
    AButton btnReset;


    @BindView(R.id.llMorning)
    LinearLayout llMorning;

    @BindView(R.id.timeLayout)
    LinearLayout timeLayout;

    @BindView(R.id.amPm)
    ATextView amPm;
    /*@BindView(R.id.txtTimePicker)
    TimePickerText txtTimePicker;*/

    Day day;

    public boolean isAm = true;

    public TPMorningModel tpMorningModel = new TPMorningModel();
    public List<ITPPlacesModel> fastTPPlacesList = new ArrayList<>();
    public List<ITPPlacesModel> fastTPPlacesListTemp = new ArrayList<>();
    FastItemAdapter<ITPPlacesModel> fastItemAdapter;

    final static int STATUS_CODE_ALONE = 1250;
    final static int STATUS_CODE_ALONG = 1251;

    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public TPMorningFragment() {
    }

    public static TPMorningFragment newInstance(Day day) {

        TPMorningFragment fragment = new TPMorningFragment();
        Bundle b = new Bundle();
        b.putSerializable("day", day);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View rootView                                   = inflater.inflate(R.layout.fragment_tp_morning, container, false);
        // this.savedInstanceState                         = savedInstanceState;
        //initiate dependencies
        App.getComponent().inject(this);
        ButterKnife.bind(this, rootView);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        if(getArguments() != null){
            day = (Day) getArguments().getSerializable("day");
        }
        if(day != null) {
            //********Contact Address Suggestion**********
            getSuggestContactAddress();
            //*************End contact address*************
            loadPreviousTP();
            handleSpinner();
        } else if(context != null){
            ((Activity) context).finish();
        }
        return rootView;
    }

    @OnClick(R.id.amPm)
    public void amPmClick(){
        if(isAm){
            isAm = false;
            amPm.setText("PM");
            String reportTime = spReportHour.getSelectedItem().toString()+":"+spReportMinute.getSelectedItem().toString()+":"+amPm.getText().toString();
            tpMorningModel.setrTime(DateTimeUtils.getHourMinutes(reportTime));
        } else {
            isAm = true;
            amPm.setText("AM");
            String reportTime = spReportHour.getSelectedItem().toString()+":"+spReportMinute.getSelectedItem().toString()+":"+amPm.getText().toString();
            tpMorningModel.setrTime(DateTimeUtils.getHourMinutes(reportTime));
        }
        eventFire(tpMorningModel);
    }

    private void handleSpinner() {
        etContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String ca = etContact.getText().toString().trim();
                if(!TextUtils.isEmpty(ca)){
                    tpMorningModel.setContactAddress(ca);
                    eventFire(tpMorningModel);
                }else{
                    tpMorningModel.setContactAddress("");
                    eventFire(tpMorningModel);
                }
            }
        });



        final Intent intent = new Intent(context, WorkPlaceActivity.class);
        btnAddPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("shift", true);
                intent.putExtra("placeList", (Serializable) fastTPPlacesList);
                intent.putExtra("month", day.getMonth());
                intent.putExtra("year", day.getYear());
                startActivityForResult(intent, STATUS_CODE_ALONE);
            }
        });


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fastTPPlacesList.clear();
                tpMorningModel.setPlaceList(fastTPPlacesList);
                tpMorningModel.setrTime("08:00:AM");
                tpMorningModel.setContactAddress("");
                tpMorningModel.setShiftType(StringConstants.WORKING);
                tpMorningModel.setNda("HQ");
                eventFire(tpMorningModel);

                updateUI();

            }
        });

        spNatureOfDa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tpMorningModel.setNda(natureOfDA.get(i));
                eventFire(tpMorningModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        shiftTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                MyLog.show("TPMorning", "shiftType pos: "+position);

                if(tpMorningModel.getShiftType()!=null && tpMorningModel.getShiftType().equalsIgnoreCase("Working")){

                }else{
                    MyLog.show("TPMorning", "shiftType null found: "+position);
                }

                switch (position){
                    case 0:
                        timeLayout.setVisibility(View.VISIBLE);
                        btnAddPlaces.setVisibility(View.VISIBLE);
                        fastTPPlacesList.addAll(fastTPPlacesListTemp);
                        fastTPPlacesListTemp.clear();
                        spNatureOfDa.setVisibility(View.VISIBLE);
                        rvWorkPlace.setVisibility(View.VISIBLE);
                        etContact.setHint(R.string.contact_address);
                        break;
                    case 1:
                        timeLayout.setVisibility(View.VISIBLE);
                        btnAddPlaces.setVisibility(View.GONE);
                        fastTPPlacesListTemp.addAll(fastTPPlacesList);
                        fastTPPlacesList.clear();
                        rvWorkPlace.setVisibility(View.GONE);
                        spNatureOfDa.setVisibility(View.VISIBLE);
                        etContact.setHint(R.string.meeting_place);
                        break;
                    case 2:
                        timeLayout.setVisibility(View.GONE);
                        btnAddPlaces.setVisibility(View.GONE);
                        rvWorkPlace.setVisibility(View.GONE);
                        fastTPPlacesListTemp.addAll(fastTPPlacesList);
                        fastTPPlacesList.clear();
                        spNatureOfDa.setVisibility(View.GONE);
                        etContact.setHint(R.string.leave_cause);
                        tpMorningModel.setNda("NA");
                        break;
                }
                tpMorningModel.setShiftType(shiftTypesList.get(position));
                tpMorningModel.setPlaceList(fastTPPlacesList);
                eventFire(tpMorningModel);
                fastItemAdapter.notifyAdapterDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spReportHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = spReportMinute.getSelectedItemPosition();
                if(pos == -1)
                    pos = 0;
                String reportTime = hoursMorning.get(i)+":"+minutes.get(pos)+":"+amPm.getText().toString();
                tpMorningModel.setrTime(DateTimeUtils.getHourMinutes(reportTime));
                eventFire(tpMorningModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spReportMinute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = spReportHour.getSelectedItemPosition();
                if(pos == -1)
                    pos = 0;
                String reportTime = hoursMorning.get(pos)+":"+minutes.get(i)+":"+amPm.getText().toString();
                tpMorningModel.setrTime(DateTimeUtils.getHourMinutes(reportTime));
                eventFire(tpMorningModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void updateUI(){
        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(fastTPPlacesList);
        fastItemAdapter.withSelectable(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rvWorkPlace.setLayoutManager(layoutManager);
        rvWorkPlace.setAdapter(fastItemAdapter);
        ArrayAdapter<String> shiftAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, shiftTypesList);
        ArrayAdapter<String> daAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, natureOfDA);
        ArrayAdapter<String> hourAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, hoursMorning);
        ArrayAdapter<String> minuteAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, minutes);
        shiftTypeSpinner.setAdapter(shiftAdapter);
        shiftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNatureOfDa.setAdapter(daAdapter);
        daAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spReportHour.setAdapter(hourAdapter);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spReportMinute.setAdapter(minuteAdapter);
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shiftTypeSpinner.setSelection(shiftTypesList.indexOf(tpMorningModel.getShiftType()));
        spNatureOfDa.setSelection(natureOfDA.indexOf(tpMorningModel.getNda()));
        etContact.setText(tpMorningModel.getContactAddress());
        MyLog.show("getrTime","Val:"+tpMorningModel.getrTime());
        String hourMinuted[] = DateTimeUtils.getAmPm(tpMorningModel.getrTime(), true);
        amPm.setText(hourMinuted[2]);
        if (hourMinuted[0].equalsIgnoreCase("00")){
            spReportHour.setSelection(0);
        }else{
            spReportHour.setSelection(hoursMorning.indexOf(hourMinuted[0]));
        }
        if(hourMinuted[1].equalsIgnoreCase("00")){
            spReportMinute.setSelection(0);
        } else {
            spReportMinute.setSelection(minutes.indexOf(hourMinuted[1]));
        }
        MyLog.show("TPM","Report Time"+hourMinuted[0]+ ":"+hourMinuted[1]+""+hourMinuted[2]);
        MyLog.show("TPM","Val:index:"+hoursMorning.indexOf(hourMinuted[0])+ ":"+minutes.indexOf(hourMinuted[1])+""+hourMinuted[2]);

        MyLog.show("indexes", " "+shiftTypesList.indexOf(tpMorningModel.getShiftType()) +" "+natureOfDA.indexOf(tpMorningModel.getNda())
                +" "+hoursMorning.indexOf(hourMinuted[0])+ " "+minutes.indexOf(hourMinuted[1]));
    }
    @Override
    public void onResume() {
        super.onResume();
    }


    private List<String> conAdrsList = new ArrayList<>();
    public void getSuggestContactAddress(){
        if (conAdrsList.size()>0){
            conAdrsList.clear();
        }
        RealmQuery<AddressModel> query = r.where(AddressModel.class);
        RealmResults<AddressModel> realmAddressList = query.findAll();

        for (AddressModel addressModel:realmAddressList){
            conAdrsList.add(addressModel.getAddress());
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, conAdrsList);
        etContact.setAdapter(adapter);
    }

    //event bus
    public void eventFire(TPMorningModel tpMorningModel){
        EventBus.getDefault().post(tpMorningModel);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void dateChange(Day day){
        this.day = day;
        loadPreviousTP();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    public List<TPPlaceRealmModel> getWorkPlaceList(long id){
        return r.where(TPPlaceRealmModel.class)
                .equalTo(TPPlaceRealmModel.COL_TP_ID, id)
                .findAll();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == STATUS_CODE_ALONE) {
            if(resultCode == Activity.RESULT_OK){
                fastTPPlacesList.clear();
                fastTPPlacesList.addAll((List<ITPPlacesModel>) data.getSerializableExtra("placeList"));
                //fastItemAdapter.notifyAdapterDataSetChanged();
                updateUI();
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        } else if(requestCode == STATUS_CODE_ALONG){
            if(resultCode == Activity.RESULT_OK){
                fastTPPlacesList.clear();
                fastTPPlacesList.addAll((List<ITPPlacesModel>) data.getSerializableExtra("placeList"));
                fastTPPlacesList.clear();
                //fastItemAdapter.notifyAdapterDataSetChanged();
                updateUI();
            } else if(resultCode == Activity.RESULT_CANCELED){

            }
        }
    }

    public long getTpId(){
        return Long.valueOf(day.getYear()+DateTimeUtils.getMonthNumber(day.getMonth())+day.getDay()+ "0");
    }

    private void loadPreviousTP() {
        fastTPPlacesList.clear();
        fastTPPlacesListTemp.clear();
        TPServerModel tpModel = r.where(TPServerModel.class)
                .equalTo(TPServerModel.COL_DAY, String.valueOf(day.getCopyDate()))
                .equalTo(TPServerModel.COL_MONTH, day.getMonth())
                .equalTo(TPServerModel.COL_YEAR, day.getYear())
                .equalTo(TPServerModel.COL_SHIFT, StringConstants.MORNING).findFirst();
        if(tpModel != null){
            tpMorningModel.setContactAddress(tpModel.getContactPlace());
            tpMorningModel.setId(getTpId());
            tpMorningModel.setrTime(tpModel.getReportTime());
            tpMorningModel.setNda(tpModel.getnDA());
            tpMorningModel.setShiftType(tpModel.getShiftType());
            tpMorningModel.setApproved(tpModel.isApproved());

            MyLog.show("LoadPreviousTP","loadPreviousTP shift type"+tpModel.getShiftType());

            for(TPPlaceRealmModel tpPlaceRealm : getWorkPlaceList(tpModel.getLocalId())){
                ITPPlacesModel realmPlace = new ITPPlacesModel();
                realmPlace.setShift(tpPlaceRealm.getShift());
                realmPlace.setCode(tpPlaceRealm.getCode());
                realmPlace.setId(tpPlaceRealm.getId());
                realmPlace.setName(tpPlaceRealm.getCode());
                fastTPPlacesList.add(realmPlace);
            }
            tpMorningModel.setPlaceList(fastTPPlacesList);

        }
        eventFire(tpMorningModel);
        updateUI();
    }
}
