package bd.com.aristo.edcr.modules.dcr.dcr.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.dcr.dcr.listener.UploadSaveListener;
import bd.com.aristo.edcr.utils.ConnectionUtils;
import bd.com.aristo.edcr.utils.ui.buttons.AButton;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import bd.com.aristo.edcr.utils.ui.texts.AnEditText;

/**
 * Created by monir.sobuj on 8/5/2018.
 */

@SuppressLint("ValidFragment")
public class DCRUploadDialog extends DialogFragment {
    private static final String TAG = "DCRUploadDialog";
    ATextView title, txtAccompany;
    AnEditText etRemarks;
    AButton btnCancel, btnUpload, btnSave;
    android.app.AlertDialog alertDialog;

    private UploadSaveListener listener;
    String remarks, accompanyIds;
    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @SuppressLint("ValidFragment")
    public DCRUploadDialog(UploadSaveListener uploadSaveListener, String remarks, String accompanyIds){
        listener = uploadSaveListener;
        this.remarks = remarks;
        this.accompanyIds = accompanyIds;
    }

    public DCRUploadDialog(){

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        App.getComponent().inject(this);

        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_dialog_upload_dcr, null);
        btnUpload = v.findViewById(R.id.btnUpload);
        btnCancel = v.findViewById(R.id.btnCancel);
        btnSave = v.findViewById(R.id.btnSave);
        etRemarks = v.findViewById(R.id.etRemarks);
        txtAccompany = v.findViewById(R.id.txtAccompany);
        if(!ConnectionUtils.isNetworkConnected(context)){
            btnUpload.setVisibility(View.GONE);
        } else {
            btnUpload.setVisibility(View.VISIBLE);
        }
        if(!TextUtils.isEmpty(remarks)){
            etRemarks.setText(remarks);
        }
        if(TextUtils.isEmpty(accompanyIds)){
            txtAccompany.setText("Without Accompany!!");
            txtAccompany.setTextColor(getResources().getColor(R.color.red));
        } else {
            txtAccompany.setText("Accompany with: "+accompanyIds);
            txtAccompany.setTextColor(getResources().getColor(R.color.color2));
        }
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        builder.setView(v);

         alertDialog = builder.create();


        alertDialog.setCanceledOnTouchOutside(false);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //uploadDCR(isAbsent, cause, etRemarks.getText().toString());
                listener.onUpload(etRemarks.getText().toString());
                alertDialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSave(etRemarks.getText().toString());
                //saveDCR(isAbsent, cause, etRemarks.getText().toString(), false);
                alertDialog.dismiss();
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







    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
