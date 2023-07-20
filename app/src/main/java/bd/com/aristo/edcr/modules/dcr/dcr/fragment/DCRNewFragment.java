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
public class DCRNewFragment extends Fragment {

    @BindView(R.id.dcrList)
    RecyclerView rv;
    List<DCRListModel> dcrNewList;
    FastItemAdapter<DCRListModel> newDcrAdapter = new FastItemAdapter<>();
    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public DCRNewFragment() {
    }


    public DCRNewFragment(List<DCRListModel> dcrNewList) {
        this.dcrNewList = dcrNewList;
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
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(context);
        newDcrAdapter.add(dcrNewList);
        newDcrAdapter.withSelectable(true);
        newDcrAdapter.setHasStableIds(false);
        rv.setLayoutManager(layoutManager1);
        rv.setAdapter(newDcrAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
