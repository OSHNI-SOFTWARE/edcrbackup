package bd.com.aristo.edcr.modules.dcr.newdcr.fragment;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.dcr.newdcr.ViewPagerListener;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by monir.sobuj on 7/11/17.
 */
@SuppressLint("ValidFragment")
public class  NewGiftFragment extends Fragment {

    @BindView(R.id.rvTodayProducts)
    RecyclerView rv;

    @BindView(R.id.addItemIV)
    ATextView addItemIV;
    public List<DCRProductsModel> dcrProductsModels = new ArrayList<>();
    ViewPagerListener viewPagerListener;
    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public NewGiftFragment() {

    }

    public NewGiftFragment(ViewPagerListener viewPagerListener, List<DCRProductsModel> dcrProductsModels) {
        this.viewPagerListener = viewPagerListener;
        this.dcrProductsModels = dcrProductsModels;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_today_doctor_promoted, container, false);
        ButterKnife.bind(this, rootView);
        refreshList();
        addItemIV.setVisibility(View.GONE);
        return rootView;
    }

    private void refreshList(){
        if(dcrProductsModels.size() > 0) {
            Collections.sort(dcrProductsModels, new Comparator<DCRProductsModel>() {
                @Override
                public int compare(DCRProductsModel dcrProductsModel, DCRProductsModel t1) {
                    return t1.getCount() > dcrProductsModel.getCount() ? 1 : -1;
                }
            });
            FastItemAdapter<DCRProductsModel> fastAdapter = new FastItemAdapter<>();
            fastAdapter.add(dcrProductsModels);
            fastAdapter.withSelectable(true);
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
                    if (item.getBalance() > item.getCount()) {
                        item.setCount(item.getCount() + 1);
                        item.setPlanned(item.getPlanned() + 1);
                        viewPagerListener.updateDCRGiftProduct(item);
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
                    if (item.getCount() > 0) {
                        item.setCount(item.getCount() - 1);
                        item.setPlanned(item.getPlanned() - 1);
                        viewPagerListener.updateDCRGiftProduct(item);
                        fastAdapter1.notifyDataSetChanged();
                    }
                }
            });


            //fill the recycler view
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(fastAdapter);
        } else {
            //downloadGift();
            ToastUtils.shortToast("No Gift Available!!!");
        }
    }



}
