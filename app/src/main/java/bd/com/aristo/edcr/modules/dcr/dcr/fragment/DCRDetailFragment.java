package bd.com.aristo.edcr.modules.dcr.dcr.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.List;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRListModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by monir.sobuj on 8/11/17.
 */
@SuppressLint("ValidFragment")
public class DCRDetailFragment extends Fragment {

    @BindView(R.id.dcrList)
    RecyclerView rv;

    FastItemAdapter<DCRListModel> dcrAdapter = new FastItemAdapter<>();
    List<DCRListModel> dcrPlanList;
    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public DCRDetailFragment() {
    }


    public DCRDetailFragment(List<DCRListModel> dcrPlanList) {
        this.dcrPlanList = dcrPlanList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_dcr_detail, container, false);
        ButterKnife.bind(this, rootView);
        setupList();

        return rootView;
    }

    private void setupList() {

        dcrAdapter.add(dcrPlanList);
        dcrAdapter.withSelectable(false);
        dcrAdapter.setHasStableIds(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(dcrAdapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
