package bd.com.aristo.edcr.modules.reports.others;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.reports.model.ProductExecution;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by altaf.sil on 3/13/18.
 */

public class ExecutionDialogFragment extends DialogFragment{

    public static List<ProductExecution> productExecutions = new ArrayList<>();
    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public static ExecutionDialogFragment newInstance(String title, List<ProductExecution> productExecutionList) {
        ExecutionDialogFragment frag = new ExecutionDialogFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        frag.setArguments(args);
        productExecutions = productExecutionList;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_dialog_statement, null);
        ATextView aTextView = (ATextView) v.findViewById(R.id.name);
        ATextView closeTV = (ATextView) v.findViewById(R.id.closeTV);
        RecyclerView mRecyclerView = (RecyclerView)v.findViewById(R.id.ssList);

        String title = getArguments().getString("title");

        aTextView.setText(title);


        FastItemAdapter<ProductExecution> fastAdapter = new FastItemAdapter<>();



        fastAdapter.add(productExecutions);
        fastAdapter.withSelectable(true);

        //fill the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(fastAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        closeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productExecutions.clear();
                alertDialog.dismiss();
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }



}
