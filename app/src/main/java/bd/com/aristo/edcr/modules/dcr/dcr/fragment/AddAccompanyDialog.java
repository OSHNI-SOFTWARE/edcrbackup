package bd.com.aristo.edcr.modules.dcr.dcr.fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.location.DoctorLocationListener;
import bd.com.aristo.edcr.modules.location.IDoctorLocation;
import bd.com.aristo.edcr.modules.location.ILocationModel;
import bd.com.aristo.edcr.utils.SystemUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 8/5/2018.
 */

@SuppressLint("ValidFragment")
public class AddAccompanyDialog extends DialogFragment {
    private static final String TAG = "DCRUploadDialog";

    List<ILocationModel> iPlacesModelList = new ArrayList<>();
    FastItemAdapter<ILocationModel> fastItemAdapter;

    AlertDialog alertDialog;
    IDoctorLocation iDoctorLocation;
    DoctorLocationListener listener;
    boolean isMorning;
    List<String> location;


    Context context;
    @Inject
    Realm r;
    @BindView(R.id.txtNoItem)
    TextView txtNoItem;
    @BindView(R.id.rvAccompany)
    RecyclerView rvAccompany;
    @BindView(R.id.etAccompany)
    AutoCompleteTextView etAccompany;
    Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public AddAccompanyDialog() {

    }

    @SuppressLint("ValidFragment")
    public AddAccompanyDialog(DoctorLocationListener listener, IDoctorLocation iDoctorLocation, boolean isMorning, List<String> location) {
        this.listener = listener;
        this.iDoctorLocation = iDoctorLocation;
        this.isMorning = isMorning;
        this.location = location;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        App.getComponent().inject(this);
        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_dialog_add_accompany, null);
        ButterKnife.bind(v);

        setDateModel();
        setPlaces();

        String shift = isMorning ? "morning" : "evening";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        builder.setView(v);

        alertDialog = builder.create();


        alertDialog.setCanceledOnTouchOutside(false);


        /*btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etPlace.getText().toString())) {
                    String loc = StringUtils.getAndFormAmp(etPlace.getText().toString());
                    boolean isFound = false;
                    for (String l : location) {
                        if (loc.equalsIgnoreCase(l)) {
                            isFound = true;
                            if (isMorning) {
                                iDoctorLocation.setmLoc(l);

                            } else {
                                iDoctorLocation.seteLoc(l);
                            }
                        }
                    }
                    if (!isFound) {
                        if (isMorning) {
                            iDoctorLocation.setmLoc(StringUtils.getAndFormAmp(etPlace.getText().toString()));
                        } else {
                            iDoctorLocation.seteLoc(StringUtils.getAndFormAmp(etPlace.getText().toString()));
                        }
                    }
                    SystemUtils.hideSoftKeyboard(v, context);
                    listener.onSave(iDoctorLocation, isMorning);
                    alertDialog.dismiss();

                } else {
                    ToastUtils.longToast("Please enter place!!");
                }
            }
        });*/
        return alertDialog;
    }


    public void setPlaces() {
        List<String> locationListForSuggestion = new ArrayList<>();

        for (String loc : location) {
            ILocationModel iPlacesModel = new ILocationModel();
            iPlacesModel.setName(loc);
            iPlacesModel.setCode(loc);
            locationListForSuggestion.add(loc);
            if (isMorning) {
                if (TextUtils.equals(loc, iDoctorLocation.getmLoc())) {
                    iPlacesModel.setClicked(true);
                } else {
                    iPlacesModel.setClicked(false);
                }
            } else {
                if (TextUtils.equals(loc, iDoctorLocation.geteLoc())) {
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
        /*fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<ILocationModel>() {
            @Override
            public boolean onClick(View v, IAdapter<ILocationModel> adapter, ILocationModel item, int position) {
                if (item.isClicked()) {
                    item.setClicked(false);
                } else {
                    item.setClicked(true);
                }
                fastItemAdapter.notifyAdapterDataSetChanged();
                return false;
            }
        });*/
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        //rvPlace.setLayoutManager(layoutManager);
        //rvPlace.setAdapter(fastItemAdapter);

        fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<ILocationModel>() {
            @Override
            public boolean onClick(View v, IAdapter<ILocationModel> adapter, ILocationModel item, int position) {
                if (item.isClicked()) {
                    if (isMorning) {
                        iDoctorLocation.setmLoc("");

                    } else {
                        iDoctorLocation.seteLoc("");
                    }
                    listener.onSave(iDoctorLocation, isMorning);
                } else {
                    if (isMorning) {
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
        //etPlace.setAdapter(adapter);

    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setDateModel() {
       /* nextDateModel = new DateModel(dateModel.getDay(), dateModel.getMonth(), dateModel.getYear(), 0, 0);
        if (dateModel.getMonth() == 12) {
            nextDateModel.setMonth(1);
            nextDateModel.setYear(dateModel.getYear() + 1);
        } else if (dateModel.getMonth() == 1) {
            nextDateModel.setMonth(dateModel.getMonth() + 1);
        } else {
            nextDateModel.setMonth(dateModel.getMonth() + 1);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btnAdd, R.id.btnCancel, R.id.btnOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                break;
            case R.id.btnCancel:
                break;
            case R.id.btnOk:
                break;
        }
    }
}
