package bd.com.aristo.edcr.modules.reports.ss;

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

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.Collections;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.reports.ss.model.ItemExecuteModel;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

/**
 * Created by altaf.sil on 3/13/18.
 */

public class StatementDialogFragment extends DialogFragment{

    public ArrayList<ItemExecuteModel> itemsExecutedList = new ArrayList<>();
    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public static StatementDialogFragment newInstance(String title,String itemType) {
        StatementDialogFragment frag = new StatementDialogFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("itemType",itemType);
        frag.setArguments(args);
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
        String itemType = getArguments().getString("itemType");

        aTextView.setText(title);

        itemsExecutedList.clear();

        FastItemAdapter<ItemExecuteModel> fastAdapter = new FastItemAdapter<>();

        if (itemType.equalsIgnoreCase(StringConstants.SELECTED_ITEM)){
            itemsExecutedList.addAll(SelectiveProductFragment.itemExecuteModelList);
        }else if(itemType.equalsIgnoreCase(StringConstants.SAMPLE_ITEM)){
            itemsExecutedList.addAll(SampleProductFragment.itemExecuteModelList);
        }else if(itemType.equalsIgnoreCase(StringConstants.GIFT_ITEM)){
            itemsExecutedList.addAll(GiftItemFragment.itemExecuteModelList);
        } else {
            itemsExecutedList.addAll(InternItemFragment.itemExecuteModelList);
        }



        MyLog.show("Dialog","itemsExecutedList size:"+itemsExecutedList.size());
        Collections.sort(itemsExecutedList, (itemExecuteModel, itemExecuteModel1) -> itemExecuteModel.getSetDate().compareTo(itemExecuteModel1.getSetDate()));
        fastAdapter.add(itemsExecutedList);
        fastAdapter.setHasStableIds(false);
        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<ItemExecuteModel>() {
            @Override
            public boolean onClick(View v, IAdapter<ItemExecuteModel> adapter, ItemExecuteModel item, int position) {
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
        closeTV.setOnClickListener(view -> {
            itemsExecutedList.clear();
            alertDialog.dismiss();
        });
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }



}
