package bd.com.aristo.edcr.modules.reports.ss;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.itemanimators.SlideLeftAlphaAnimator;

import java.util.ArrayList;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.reports.ss.model.GridModel;
import bd.com.aristo.edcr.modules.reports.ss.model.SampleStatementResponse;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by altaf.sil on 3/13/18.
 */

public class StatementSummaryDialogFragment extends DialogFragment{

    Context context;
    public String[] values;
    private FastAdapter<GridModel> mFastAdapter;
    RecyclerView mRecyclerView;
    ItemAdapter<GridModel> itemAdapter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public static StatementSummaryDialogFragment newInstance(String monthName) {
        StatementSummaryDialogFragment frag = new StatementSummaryDialogFragment();
        Bundle args = new Bundle();
        args.putString("MONTH_NAME", monthName);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_dialog_statement_summary, null);
        ATextView aTextView = (ATextView) v.findViewById(R.id.txtMonth);
        ATextView closeTV = (ATextView) v.findViewById(R.id.closeTV);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.ssSummary);
        values                                            = new String[56];
        String monthName = getArguments().getString("MONTH_NAME");
        aTextView.setText(monthName);
        setValues();
        setAdapter();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        closeTV.setOnClickListener(view -> {
            alertDialog.dismiss();
        });
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }




    public void setAdapter(){
        mFastAdapter = new FastAdapter<>();
        itemAdapter = new ItemAdapter<>();
        mFastAdapter.setHasStableIds(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context,7));
        mRecyclerView.setItemAnimator(new SlideLeftAlphaAnimator());
        mRecyclerView.setAdapter(itemAdapter.wrap(mFastAdapter));
        List<GridModel> items = new ArrayList<>();
        for (int i = 0; i < 56; i++) {
            GridModel item = new GridModel();
            item
                    .withName(values[i])
                    .withIdentifier(100 + i);
            items.add(item);
        }

        itemAdapter.add(items);

    }

    public void setValues(){
        SampleStatementResponse sampleStatementResponse = SampleStatementResponse.getInstance();
        values[0] = "";
        values[1] = "Total";
        values[2] = "1st";
        values[3] = "2nd";
        values[4] = "3rd";
        values[5] = "4th";
        values[6] = "Bal.";

        values[7] = "SmR";
        values[14] = "SlR";
        values[21] = "SmI";
        values[28] = "Total";
        values[35] = "GtR";
        values[42] = "GtI";
        values[49] = "Total";
        int[][] summary = sampleStatementResponse.getSummary();
        for(int i = 8; i < 14; i++){
            values[i] = String.valueOf(summary[0][i-8]);
        }
        for(int i = 15; i < 21; i++){
            values[i] = String.valueOf(summary[1][i-15]);
        }
        for(int i = 22; i < 28; i++){
            values[i] = String.valueOf(summary[2][i-22]);
        }
        for(int i = 29; i < 35; i++){
            values[i] = String.valueOf(Integer.parseInt(values[i - 7]) + Integer.parseInt(values[i - 14]) + Integer.parseInt(values[i - 21]));
        }
        for(int i = 36; i < 42; i++){
            values[i] = String.valueOf(summary[3][i - 36]);
        }

        for(int i = 43; i < 49; i++){
            values[i] = String.valueOf(summary[4][i - 43]);
        }
        for(int i = 50; i < 56; i++){
            values[i] = String.valueOf(Integer.parseInt(values[i - 7]) + Integer.parseInt(values[i - 14]));
        }

    }



}
