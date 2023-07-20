package bd.com.aristo.edcr.modules.wp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Spinner;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.InternModel;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.modules.wp.model.WPUtilsModel;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.buttons.AButton;
import bd.com.aristo.edcr.utils.ui.texts.AnEditText;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 8/5/2018.
 */

public class InternUploadDialog extends DialogFragment {
    private static final String TAG = "InternUploadDialog";

    @Inject
    Realm r;
    public DateModel dateModel;
    public WPUtilsModel wpUtilsModel;
    public String id;
    public boolean isMorning;


    android.app.AlertDialog alertDialog;

    AnEditText etInstituteName;
    Spinner spWardName;
    AButton btnCancel;
    AButton btnUpload;

    //Activity activity;
    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        App.getComponent().inject(this);

        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_dialog_upload_intern_info, null);
        if(getArguments() != null){
            dateModel = (DateModel) getArguments().getSerializable(StringConstants.DATE_MODEL);
            wpUtilsModel = (WPUtilsModel) getArguments().getSerializable(StringConstants.WORK_PLAN_UTIL_MODEL);
            id = wpUtilsModel.getDocId();
            isMorning = wpUtilsModel.isMorning();
        } else {
            return null;
        }
        btnUpload = v.findViewById(R.id.btnUpload);
        btnCancel = v.findViewById(R.id.btnCancel);
        etInstituteName = v.findViewById(R.id.etInstituteName);
        spWardName = v.findViewById(R.id.spWardName);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        builder.setView(v);

        alertDialog = builder.create();


        alertDialog.setCanceledOnTouchOutside(false);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleInputData();
                SystemUtils.hideSoftKeyboard(etInstituteName, context);
                //alertDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                SystemUtils.hideSoftKeyboard(etInstituteName, context);
            }
        });


        return alertDialog;
    }

    public void handleInputData() {
        String instName = etInstituteName.getText().toString().trim();
        String wardName = spWardName.getSelectedItem().toString();

        if (TextUtils.isEmpty(instName)) {
            etInstituteName.setError("Inst. Name field is required!");
            return;
        }



        saveInternInfo(instName,wardName);
    }

    public void saveInternInfo(String instName, String wardName){
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                InternModel internModel = new InternModel();
                internModel.setInternId(id);
                internModel.setDate(DateTimeUtils.getDayMonthYear(dateModel));
                internModel.setInstitute(instName);
                internModel.setMorning(isMorning);
                internModel.setUnit(wardName);
                r.insertOrUpdate(internModel);
                alertDialog.dismiss();
                //activity.onBackPressed();
                Fragment fragment = new WPInternViewPager();
                Bundle b = new Bundle();
                b.putSerializable(StringConstants.WORK_PLAN_UTIL_MODEL, wpUtilsModel);
                b.putSerializable(StringConstants.DATE_MODEL, dateModel);
                fragment.setArguments(b);
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("intern_wp_pager").commit();
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

}
