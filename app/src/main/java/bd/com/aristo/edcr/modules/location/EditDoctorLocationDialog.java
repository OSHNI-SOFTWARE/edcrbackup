package bd.com.aristo.edcr.modules.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.ToastUtils;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 8/5/2018.
 */

@SuppressLint("ValidFragment")
public class EditDoctorLocationDialog extends DialogFragment {
    private static final String TAG = "DCRUploadDialog";
    TextView txtDoctorName, txtTitle, btnAdd;
    AutoCompleteTextView etPlace;
    RecyclerView rvPlace;

    List<ILocationModel> iPlacesModelList = new ArrayList<>();
    FastItemAdapter<ILocationModel> fastItemAdapter;

    AlertDialog alertDialog;
    IDoctorLocation iDoctorLocation;
    DoctorLocationListener listener;
    boolean isMorning;
    List<String> location;
    int month, year;



    Context context;
    @Inject
    Realm r;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public EditDoctorLocationDialog(){

    }

    @SuppressLint("ValidFragment")
    public EditDoctorLocationDialog(DoctorLocationListener listener, IDoctorLocation iDoctorLocation, boolean isMorning, int month, int year){
        this.listener = listener;
        this.iDoctorLocation = iDoctorLocation;
        this.isMorning = isMorning;
        this.month = month;
        this.year = year;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        App.getComponent().inject(this);
        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_dialog_edit_doctor_location, null);
        txtDoctorName = v.findViewById(R.id.txtDoctorName);
        txtTitle = v.findViewById(R.id.txtTitle);
        btnAdd = v.findViewById(R.id.btnAdd);
        etPlace = v.findViewById(R.id.etPlace);
        rvPlace = v.findViewById(R.id.rvPlace);
        setPlaces();
        txtDoctorName.setText(iDoctorLocation.getName());
        String shift = isMorning?"morning":"evening";
        txtTitle.setText(String.format(context.getResources().getString(R.string.hint_set_place), shift));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        builder.setView(v);

         alertDialog = builder.create();


        alertDialog.setCanceledOnTouchOutside(false);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(etPlace.getText().toString())){
                    String loc = StringUtils.getAndFormAmp(etPlace.getText().toString());
                    boolean isFound = false;
                    for(String l:location){
                        if(loc.equalsIgnoreCase(l)){
                            isFound = true;
                            if(isMorning){
                                iDoctorLocation.setmLoc(l);

                            } else {
                                iDoctorLocation.seteLoc(l);
                            }
                        }
                    }
                    if(!isFound){
                        if(isMorning){
                            iDoctorLocation.setmLoc(StringUtils.getAndFormAmp(etPlace.getText().toString()));
                        } else {
                            iDoctorLocation.seteLoc(StringUtils.getAndFormAmp(etPlace.getText().toString()));
                        }
                    }
                    SystemUtils.hideSoftKeyboard(v, context);
                    listener.onSave(iDoctorLocation, isMorning);
                    alertDialog.dismiss();

                } else{
                    ToastUtils.longToast("Please enter place!!");
                }
            }
        });
        return alertDialog;
    }



    public void setPlaces(){
        setLocationList();
        List<String> locationListForSuggestion = new ArrayList<>();

        for(String loc:location){
            ILocationModel iPlacesModel = new ILocationModel();
            iPlacesModel.setName(loc);
            iPlacesModel.setCode(loc);
            locationListForSuggestion.add(loc);
            if(isMorning){
                if(TextUtils.equals(loc, iDoctorLocation.getmLoc())){
                    iPlacesModel.setClicked(true);
                } else {
                    iPlacesModel.setClicked(false);
                }
            } else {
                if(TextUtils.equals(loc, iDoctorLocation.geteLoc())){
                    iPlacesModel.setClicked(true);
                } else {
                    iPlacesModel.setClicked(false);
                }
            }
            iPlacesModelList.add(iPlacesModel);
        }

        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(iPlacesModelList);
        fastItemAdapter.withSelectable(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvPlace.setLayoutManager(layoutManager);
        rvPlace.setAdapter(fastItemAdapter);
        fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<ILocationModel>() {
            @Override
            public boolean onClick(View v, IAdapter<ILocationModel> adapter, ILocationModel item, int position) {
                if(item.isClicked()){
                    if(isMorning){
                        iDoctorLocation.setmLoc("");

                    } else {
                        iDoctorLocation.seteLoc("");
                    }
                    listener.onSave(iDoctorLocation, isMorning);
                } else {
                    if(isMorning){
                        iDoctorLocation.setmLoc(item.getName());

                    } else {
                        iDoctorLocation.seteLoc(item.getName());
                    }
                    listener.onSave(iDoctorLocation, isMorning);
                }

                SystemUtils.hideSoftKeyboard(v, context);

                alertDialog.dismiss();
                return false;
            }
        });
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, locationListForSuggestion);
        etPlace.setAdapter(adapter);

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setLocationList(){
        location = new ArrayList<>();
        List<DoctorsModel> doctorsModels = r.where(DoctorsModel.class)
                .equalTo(DoctorsModel.COL_MONTH, month)
                .equalTo(DoctorsModel.COL_YEAR, year)
                .findAll();
        if(doctorsModels != null && doctorsModels.size() > 0){
            for(DoctorsModel dm:doctorsModels){
                if(dm.getId().equals(dm.getId().toLowerCase())) {

                    if(isMorning){
                        if (!TextUtils.isEmpty(dm.getMorningLoc())) {
                            location.add(dm.getMorningLoc());
                        }
                    } else {
                        if (!TextUtils.isEmpty(dm.getEveningLoc())) {
                            location.add(dm.getEveningLoc());
                        }
                    }
                }
            }
        }
        Set<String> locSet = new HashSet<>(location);
        location.clear();
        location.addAll(locSet);
    }
}
