package bd.com.aristo.edcr.modules.wp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.buttons.AButton;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 8/5/2018.
 */

public class SampleEmptyDialog extends DialogFragment {
    private static final String TAG = "SampleEmptyDialog";



    android.app.AlertDialog alertDialog;

    AButton btnOk;
    ATextView txtTitle, txtMsg;

    String title, msg;
    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_dialog_sample_empty, null);
        btnOk = v.findViewById(R.id.btnOk);
        txtTitle = v.findViewById(R.id.title);
        txtMsg = v.findViewById(R.id.txtMsg);
        if(getArguments() != null){
            title = getArguments().getString(StringConstants.SAMPLE_EMPTY_DIALOG_TITLE);
            msg = getArguments().getString(StringConstants.SAMPLE_EMPTY_DIALOG_MSG);

            txtTitle.setText(title);
            txtMsg.setText(msg);
        } else {
            return null;
        }
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        builder.setView(v);

        alertDialog = builder.create();


        alertDialog.setCanceledOnTouchOutside(false);

        btnOk.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

}
