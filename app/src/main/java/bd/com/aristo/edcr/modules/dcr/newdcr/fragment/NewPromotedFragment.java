package bd.com.aristo.edcr.modules.dcr.newdcr.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.modules.dcr.dcr.model.SampleProductsModel;
import bd.com.aristo.edcr.modules.dcr.newdcr.ViewPagerListener;
import bd.com.aristo.edcr.models.db.DateModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 8/11/17.
 */
@SuppressLint("ValidFragment")
public class NewPromotedFragment extends Fragment {

    private String TAG = NewPromotedFragment.class.getSimpleName();

    @Inject
    Realm r;

    @BindView(R.id.rvTodayProducts)
    RecyclerView rv;

    public List<DCRProductsModel> dcrProductsModels = new ArrayList<>();
    ViewPagerListener viewPagerListener;
    public NewPromotedFragment(ViewPagerListener viewPagerListener, List<DCRProductsModel> dcrProductsModels) {
        this.viewPagerListener = viewPagerListener;
        this.dcrProductsModels = dcrProductsModels;
    }


    public UserModel userModel;
    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }

    final DateModel dateModel = DCRUtils.getToday();
    public NewPromotedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_today_doctor_promoted, container, false);
        ButterKnife.bind(this, rootView);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        getUserInfo();
        setupList();
        return rootView;
    }

    private void setupList() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addNewSampleItem(SampleProductsModel spModel) {
        boolean isAdded = true;
        for (DCRProductsModel dcrProductModelForSave:dcrProductsModels){
            if(dcrProductModelForSave.getCode().equals(spModel.getCode())){
                isAdded = false;
            }
        }
        if(isAdded){
            DCRProductsModel dcrProductsModel = new DCRProductsModel();
            dcrProductsModel.setQuantity(spModel.getQuantity());
            dcrProductsModel.setBalance(spModel.getBalance());
            dcrProductsModel.setItemType(0);
            dcrProductsModel.setCode(spModel.getCode());
            dcrProductsModel.setCount(1);
            dcrProductsModel.setName(spModel.getName());
            dcrProductsModel.setGeneric(spModel.getGeneric());
            dcrProductsModel.setPackSize(spModel.getPackSize());
            dcrProductsModel.setPlanned(spModel.getPlanned());
            dcrProductsModels.add(dcrProductsModel);
            viewPagerListener.updateDCRSelectedProduct(dcrProductsModel);
        }

    }

    @OnClick(R.id.addItemIV)
    void addItemButton(){
        bd.com.aristo.edcr.modules.dcr.newdcr.fragment.AddSampleItemActivity.start((Activity) context);
    }

    private void refreshList(){

        if(dcrProductsModels.size() > 0) {
            Collections.sort(dcrProductsModels, new Comparator<DCRProductsModel>() {
                @Override
                public int compare(DCRProductsModel dcrProductsModel, DCRProductsModel t1) {
                    return t1.getCount() > dcrProductsModel.getCount() ? 1 : -1;
                }
            });
        }
        FastItemAdapter<DCRProductsModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(dcrProductsModels);
        fastAdapter.withSelectable(true);
        fastAdapter.setHasStableIds(false);
        fastAdapter.withItemEvent(new ClickEventHook<DCRProductsModel>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof DCRProductsModel.ViewHolder) {
                    return ((DCRProductsModel.ViewHolder) viewHolder).ivPlus;
                }
                return null;
            }

            @Override
            public void onClick(View v, int position, FastAdapter<DCRProductsModel> fastAdapter1, final DCRProductsModel item) {
                //react on the click event
                if(item.getBalance() > item.getCount()){
                    item.setCount(item.getCount() + 1);
                    //DCRUtils.setCount(item, r, 1);
                    viewPagerListener.updateDCRSelectedProduct(item);
                    fastAdapter1.notifyDataSetChanged();
                }
            }
        });

        fastAdapter.withItemEvent(new ClickEventHook<DCRProductsModel>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof DCRProductsModel.ViewHolder) {
                    return ((DCRProductsModel.ViewHolder) viewHolder).ivMinus;
                }
                return null;
            }

            @Override
            public void onClick(View v, int position, FastAdapter<DCRProductsModel> fastAdapter1, final DCRProductsModel item) {
                //react on the click event
                if(item.getCount() > 0){
                    item.setCount(item.getCount() - 1);
                    viewPagerListener.updateDCRSelectedProduct(item);
                    fastAdapter1.notifyDataSetChanged();
                }

            }
        });


        //fill the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(fastAdapter);

    }


    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
