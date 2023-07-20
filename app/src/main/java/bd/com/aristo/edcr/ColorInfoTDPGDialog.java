package bd.com.aristo.edcr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import bd.com.aristo.edcr.utils.ui.buttons.AButton;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by monir.sobuj on 8/5/2018.
 */

@SuppressLint("ValidFragment")
public class ColorInfoTDPGDialog extends DialogFragment {
    private static final String TAG = "ColorInfoTDPGDialog";
    ATextView title;
    LinearLayout llChangedTP;
    AButton btnOk;
    android.app.AlertDialog alertDialog;
    String type;
    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @SuppressLint("ValidFragment")
    public ColorInfoTDPGDialog(String type){
        this.type = type;
    }

    public ColorInfoTDPGDialog(){

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        App.getComponent().inject(this);

        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_dialog_color_info_tpg, null);
        btnOk = v.findViewById(R.id.btnOk);
        title = v.findViewById(R.id.title);
        llChangedTP = v.findViewById(R.id.llChangedTP);
        if(!type.equalsIgnoreCase("TP")){
            llChangedTP.setVisibility(View.GONE);
        }
        title.setText(getString(R.string.title_color_info_tdpg, type));
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
