package bd.com.aristo.edcr.modules.wp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.View;
import android.view.Window;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.ui.buttons.AButton;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 8/5/2018.
 */

@SuppressLint("ValidFragment")
public class ColorInfoWPDialog extends DialogFragment {
    private static final String TAG = "ColorInfoTDPGDialog";
    ATextView title;
    AButton btnOk;
    android.app.AlertDialog alertDialog;
    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public ColorInfoWPDialog(){

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        App.getComponent().inject(this);

        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_dialog_color_info_work_plan, null);
        btnOk = v.findViewById(R.id.btnOk);
        title = v.findViewById(R.id.title);

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
}
