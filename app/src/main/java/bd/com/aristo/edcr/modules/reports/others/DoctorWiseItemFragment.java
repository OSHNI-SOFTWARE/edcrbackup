package bd.com.aristo.edcr.modules.reports.others;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.models.response.ResponseDetail;
import bd.com.aristo.edcr.modules.reports.model.DoctorWiseItemModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.AnEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 02/11/17.
 */

public class DoctorWiseItemFragment extends Fragment {

    private static final String TAG = "DoctorsFragment";
    List<DoctorsModel> doctorsModels;
    @Inject
    Realm r;

    @BindView(R.id.doctorList)
    RecyclerView pwdsList;

    private CompositeDisposable mCompositeDisposable;

    LoadingDialog loadingDialog;
    @Inject
    APIServices apiServices;
    @Inject
    UserModel userModel;



    @BindView(R.id.etFilterDoctor)
    AnEditText etFilterDoctor;

    Context context;
    int month, year;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public DoctorWiseItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_doctor_dcr, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(false);
        if(getArguments() != null){
            month = getArguments().getInt("month");
            year = getArguments().getInt("year");
            setupList();
        } else {
            ((AppCompatActivity) context).onBackPressed();
        }

        return rootView;
    }

    @OnClick(R.id.flColorInfo)
    void onClickColorInfo(){
        DialogFragment dialogFragment = new ColorInfoDCRDialog();
        dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "color_info");
    }


    public void setTitle(long count){
        ((Activity) context).setTitle("Doctors for "+DateTimeUtils.getMonthForInt(month)+"("+count+")");
    }


    private void setupList() {
        doctorsModels = r.where(DoctorsModel.class)
                .equalTo(DoctorsModel.COL_MONTH, month)
                .equalTo(DoctorsModel.COL_YEAR, year)
                .sort(DoctorsModel.COL_NAME)
                .findAll();
        refreshList();
    }

    public void refreshList(){
        List<IDoctorItem> dvrIDoctors = new ArrayList<>();
        int i = 10;
        for(DoctorsModel dm: doctorsModels){
            IDoctorItem dvrIDoctor = new IDoctorItem();
            dvrIDoctor.setId(dm.getId());
            dvrIDoctor.setName(dm.getName());
            dvrIDoctor.setSpecial(dm.getSpecial());
            dvrIDoctor.setDegree(dm.getDegree());
            dvrIDoctor.setmLoc(dm.getMorningLoc());
            dvrIDoctor.seteLoc(dm.getEveningLoc());
            dvrIDoctor.setDotList(null);
            dvrIDoctor.withIdentifier(i);
            dvrIDoctors.add(dvrIDoctor);
            i++;
        }
        final FastItemAdapter<IDoctorItem> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(dvrIDoctors);
        fastAdapter.setHasStableIds(false);
        fastAdapter.withSelectable(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        pwdsList.setLayoutManager(layoutManager);
        pwdsList.setAdapter(fastAdapter);

        fastAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<IDoctorItem>() {
            @Override
            public boolean filter(IDoctorItem item, CharSequence constraint) {
                return !item.getName().toLowerCase().contains(constraint.toString().toLowerCase());
            }
        });

        fastAdapter.withOnClickListener((v, adapter, item, position) -> {
            syncMarketDVRList(item.getId(), item.getName());
            return false;
        });

        etFilterDoctor.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fastAdapter.getItemAdapter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void syncMarketDVRList(String id, String name) {
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(apiServices.getDoctorWiseItem(userModel.getUserId(),
                DateTimeUtils.getMonthYear(month, year),
                id)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<DoctorWiseItemModel>>() {
                    @Override
                    public void onComplete() {
                        MyLog.show(TAG, "onComplete doctors coverage sync");
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        //refreshList();
                    }

                    @Override
                    public void onNext(final ResponseDetail<DoctorWiseItemModel> value) {
                        if (value != null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {

                            if (value.getDataModelList() != null && value.getDataModelList().size() > 0) {

                                DoctorWiseItemDialogFragment doctorWiseItemDialogFragment = DoctorWiseItemDialogFragment.newInstance(id, name, value.getDataModelList());
                                doctorWiseItemDialogFragment.show(getFragmentManager(), "dialog");
                            }
                        }
                    }
                }));
    }


}
