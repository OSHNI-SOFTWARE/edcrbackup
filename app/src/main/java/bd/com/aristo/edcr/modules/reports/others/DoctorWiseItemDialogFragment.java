package bd.com.aristo.edcr.modules.reports.others;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.reports.model.DoctorWiseItemModel;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;


public class DoctorWiseItemDialogFragment extends DialogFragment {

    public List<DoctorWiseItemModel> itemsExecutedList = new ArrayList<>();
    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public static DoctorWiseItemDialogFragment newInstance(String title, String itemType, List<DoctorWiseItemModel> itemsExecutedList) {
        DoctorWiseItemDialogFragment frag = new DoctorWiseItemDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("itemType", itemType);
        args.putSerializable("itemData", (Serializable) itemsExecutedList);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_dialog_statement, null);
        ATextView aTextView = (ATextView) v.findViewById(R.id.name);
        ATextView closeTV = (ATextView) v.findViewById(R.id.closeTV);
        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.ssList);

        String title = getArguments().getString("title");
        String itemType = getArguments().getString("itemType");
        itemsExecutedList = (List<DoctorWiseItemModel>) getArguments().getSerializable("itemData");
//product name sort
        if(itemsExecutedList!=null && itemsExecutedList.size()>0)
        {
            Collections.sort(itemsExecutedList, (o1, o2) -> o1.getSetDate().compareTo(o2.getSetDate()));
        }
        aTextView.setText(itemType + "[" + title + "]");

        FastItemAdapter<DoctorWiseItemModel> fastAdapter = new FastItemAdapter<>();

        MyLog.show("Dialog", "itemsExecutedList size:" + itemsExecutedList.size());
        fastAdapter.add(itemsExecutedList);
        fastAdapter.setHasStableIds(false);
        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<DoctorWiseItemModel>() {
            @Override
            public boolean onClick(View v, IAdapter<DoctorWiseItemModel> adapter, DoctorWiseItemModel item, int position) {
                return false;
            }
        });

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
                itemsExecutedList.clear();
                alertDialog.dismiss();
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }


}
