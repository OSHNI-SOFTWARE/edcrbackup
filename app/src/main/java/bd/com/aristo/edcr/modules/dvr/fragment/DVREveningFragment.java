package bd.com.aristo.edcr.modules.dvr.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.dvr.DVRSelectionListener;
import bd.com.aristo.edcr.modules.dvr.DVRUtils;
import bd.com.aristo.edcr.modules.dvr.model.IDoctorsModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.ui.texts.AnEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 6/15/17.
 */

public class DVREveningFragment extends Fragment implements DVRSelectionListener{

    public static final String TAG = "DVREveningFragment";


    @Inject
    APIServices apiServices;
    @Inject
    Realm r;
    @BindView(R.id.dvrList)
    RecyclerView dvrList;
    @BindView(R.id.etFilterDoctor)
    AnEditText etFilterDoctor;

    List<IDoctorsModel> dvrIDoctors = new ArrayList<>();


    final FastItemAdapter<IDoctorsModel> fastAdapter = new FastItemAdapter<>();

    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public DVREveningFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_dvr_detail, container, false);
        ButterKnife.bind(this, rootView);
        refreshList();
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void refreshList(){
        fastAdapter.add(dvrIDoctors);
        fastAdapter.withSelectable(true);
        fastAdapter.setHasStableIds(false);
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<IDoctorsModel>() {
            @Override
            public boolean onClick(View v, IAdapter<IDoctorsModel> adapter, final IDoctorsModel item, int position) {
                if(!DVRUtils.isApproved){
                    if(item.isClicked()){
                        item.setClicked(false);
                        //DVRActivity.activity.onRemove(false, item);

                    } else {
                        item.setClicked(true);
                        //DVRActivity.activity.onAdd(false, item);
                    }
                }
                adapter.getFastAdapter().notifyDataSetChanged();
                return false;
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        dvrList.setLayoutManager(layoutManager);
        dvrList.setAdapter(fastAdapter);
        fastAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<IDoctorsModel>() {
            @Override
            public boolean filter(IDoctorsModel item, CharSequence constraint) {
                return !item.getName().toLowerCase().contains(constraint.toString().toLowerCase());
            }
        });

        etFilterDoctor.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fastAdapter.getItemAdapter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    @Override
    public void onSelect(List<IDoctorsModel> iDoctorsModel) {
        dvrIDoctors.clear();
        dvrIDoctors.addAll(iDoctorsModel);
        fastAdapter.notifyAdapterDataSetChanged();
        //refreshList();
    }
}
