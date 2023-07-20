package bd.com.aristo.edcr.modules.dcr.dcr.fragment;

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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.ProductModel;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.modules.dcr.dcr.listener.DCRProductListener;
import bd.com.aristo.edcr.modules.dcr.dcr.model.IDCRProductsModel;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.modules.wp.WPUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 8/11/17.
 */
@SuppressLint("ValidFragment")
public class InternSampleFragment extends Fragment {

    @Inject
    Realm r;

    @BindView(R.id.rvTodayProducts)
    RecyclerView rv;



    List<ProductModel> sampleModelList = new ArrayList<>();
    public static List<IDCRProductsModel> productsModels;
    final DateModel dateModel = DCRUtils.getToday();

    public UserModel userModel;
    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }
    DCRProductListener dcrProductListener;
    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public InternSampleFragment() {
    }
    @SuppressLint("ValidFragment")
    public InternSampleFragment(DCRProductListener dcrProductListener) {
        this.dcrProductListener = dcrProductListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_today_doctor_promoted, container, false);
        ButterKnife.bind(this, rootView);
        getUserInfo();

        setupList();


        return rootView;
    }

    @OnClick(R.id.addItemIV)
    void addItemButton(){
       AddSampleItemActivity.start((Activity) context, 1);
    }


    private void setupList() {
        sampleModelList = WPUtils.getMonthWiseSamplesForIntern(r, dateModel.getYear(), dateModel.getMonth());
        if(sampleModelList.size() > 0)  { //doctors available, load list
            //DCRUtils.setDCRSampleList(r, 2);
            refreshList();
        }else{ // no intern sample is available, download now

        }
    }

    private void refreshList(){

        productsModels = dcrProductListener.getDCRRefreshList(1);//DCRUtils.getDCRSampleRefreshList(r, 2);
        final FastItemAdapter<IDCRProductsModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(productsModels);
        fastAdapter.withSelectable(true);
        fastAdapter.setHasStableIds(false);
        fastAdapter.withItemEvent(new ClickEventHook<IDCRProductsModel>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof IDCRProductsModel.ViewHolder) {
                    return ((IDCRProductsModel.ViewHolder) viewHolder).ivPlus;
                }
                return null;
            }

            @Override
            public void onClick(View v, int position, FastAdapter<IDCRProductsModel> fastAdapter1, final IDCRProductsModel item) {
                //react on the click event
                if(item.getBalance() > item.getCount()){
                    item.setCount(item.getCount() + 1);

                    if(item.isPlanned()){
                        item.setPlannedCount(item.getPlannedCount() +1);
                    }
                    //DCRUtils.setCount(item, r, 2);
                    EventBus.getDefault().post(item);
                    fastAdapter1.notifyDataSetChanged();
                }
            }
        });

        fastAdapter.withItemEvent(new ClickEventHook<IDCRProductsModel>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof IDCRProductsModel.ViewHolder) {
                    return ((IDCRProductsModel.ViewHolder) viewHolder).ivMinus;
                }
                return null;
            }

            @Override
            public void onClick(View v, int position, FastAdapter<IDCRProductsModel> fastAdapter1, final IDCRProductsModel item) {
                //react on the click event
                if(item.getCount() > 0){
                    item.setCount(item.getCount() - 1);
                    if(item.isPlanned()){
                        item.setPlannedCount(item.getPlannedCount() -1);
                    }
                    //DCRUtils.setCount(item, r, 2);
                    EventBus.getDefault().post(item);
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

        //Updating list
        refreshList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
