package bd.com.aristo.edcr.modules.dcr.dcr.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.models.db.InternModel;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.models.response.PostResponse;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.buttons.AButton;
import bd.com.aristo.edcr.utils.ui.texts.AnEditText;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 8/5/2018.
 */

public class InternRestUploadDialog extends DialogFragment {
    private static final String TAG = "InternRestUploadDialog";

    @Inject
    Realm r;
    @Inject
    APIServices apiServices;
    public DateModel dateModel;
    public boolean isMorning;

    UserModel userModel;


    android.app.AlertDialog alertDialog;

    AnEditText etKeyPersonName;
    AnEditText etNoOfIntern;
    AButton btnCancel;
    AButton btnUpload;

    //Activity activity;
    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void getUserModel(){
        userModel = r.where(UserModel.class).findFirst();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        App.getComponent().inject(this);
        getUserModel();
        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_dialog_upload_intern_info_rest, null);
        dateModel = DCRUtils.getToday();
        btnUpload = v.findViewById(R.id.btnUpload);
        btnCancel = v.findViewById(R.id.btnCancel);
        etKeyPersonName = v.findViewById(R.id.etKeyPersonName);
        etNoOfIntern = v.findViewById(R.id.etNoOfIntern);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        builder.setView(v);

        alertDialog = builder.create();


        alertDialog.setCanceledOnTouchOutside(false);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleInputData();
                //alertDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


        return alertDialog;
    }

    public void handleInputData() {
        String keyPerson = etKeyPersonName.getText().toString().trim();
        String noOfIntern = etNoOfIntern.getText().toString().trim();
        isMorning = DCRUtils.DCR_IS_MORNING;

        if (TextUtils.isEmpty(keyPerson)) {
            etKeyPersonName.setError("Key person name field is required!");
            return;
        }

        if (TextUtils.isEmpty(noOfIntern)) {
            etNoOfIntern.setError("No. of intern is required!");
            return;
        }



        saveInternInfo(keyPerson, noOfIntern);
    }

    public void saveInternInfo(String keyPerson, String noOfIntern){
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                InternModel internModel = r.where(InternModel.class)
                        .equalTo(InternModel.COL_DATE, DateTimeUtils.getDayMonthYear(dateModel))
                        .equalTo(InternModel.COL_ID, DCRUtils.DOCTOR_ID)
                        .equalTo(InternModel.COL_SHIFT, isMorning).findFirst();
                if(internModel != null) {
                    internModel.setKeyPerson(keyPerson);
                    internModel.setNoOfIntern(DateTimeUtils.getMonthNumber(Integer.parseInt(noOfIntern)));
                    r.insertOrUpdate(internModel);
                    uploadIntern(internModel);
                }
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void uploadIntern(InternModel internModel) {

        CompositeDisposable mCompositeDisposable = new CompositeDisposable();
        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please Wait...");
        loadingDialog.show();
        String id = internModel.getInternId();
        String instName = internModel.getInstitute();
        String wardName = internModel.getUnit();
        String keyPerson = internModel.getKeyPerson();
        String noOfIntern = internModel.getNoOfIntern();
        String contact = "";
        MyLog.show("Intern Query Param:", userModel.getUserId()+" "+DateTimeUtils.getDayMonthYear(dateModel)
                +" "+isMorning+" "+id+" "+instName+" "+wardName+" "+keyPerson+" "+contact+" "+noOfIntern);
        mCompositeDisposable.add(apiServices.postIntern(
                userModel.getUserId(),
                DateTimeUtils.getDayMonthYear(dateModel),
                isMorning?StringConstants.MORNING:StringConstants.EVENING,
                id,
                instName,
                wardName,
                keyPerson,
                contact,
                noOfIntern)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<PostResponse>() {
                    @Override
                    public void onComplete() {
                        //ToastUtils.shortToast("Upload Intern Success!!");
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.shortToast(StringConstants.UPLOAD_FAIL_MSG);
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(PostResponse value) {
                        if(value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)){
                            ToastUtils.shortToast(StringConstants.UPLOAD_SUCCESS_MSG);
                            alertDialog.dismiss();
                            Fragment fragment = new InternDCRViewPager();
                            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("intern_dcr_view_pager_fragment").commit();

                        } else {
                            ToastUtils.shortToast("Intern Upload Failed!!");
                        }

                    }
                }));
    }

}
