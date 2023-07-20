package bd.com.aristo.edcr.modules.wp.fragment;

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

import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.modules.wp.WPUtils;
import bd.com.aristo.edcr.modules.wp.model.WPProductsModel;
import bd.com.aristo.edcr.modules.wp.model.WPUtilsModel;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 7/11/17.
 */

public class GiftFragment extends Fragment {

    private String TAG = "GWDSGiftFragment";

    @Inject
    Realm r;

    @BindView(R.id.rvTodayProducts)
    RecyclerView rv;

    @BindView(R.id.addItemIV)
    ATextView addItemIV;

    public UserModel userModel;
    //public WPViewPager wpViewPager;
    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }

    public DateModel dateModel;
    public WPUtilsModel wpUtilsModel;

    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public GiftFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_today_doctor_promoted, container, false);
        ButterKnife.bind(this, rootView);
        if(getArguments() != null) {
            dateModel = (DateModel) getArguments().getSerializable(StringConstants.DATE_MODEL);
            wpUtilsModel = (WPUtilsModel) getArguments().getSerializable(StringConstants.WORK_PLAN_UTIL_MODEL);
            //wpViewPager = (WPViewPager) getArguments().getSerializable(StringConstants.WORK_PLAN_PAGER);
            getUserInfo();
            refreshList();
        }else {
            ((Activity) context).onBackPressed();
        }
        addItemIV.setVisibility(View.GONE);

        return rootView;
    }


    private void refreshList(){
        List<WPProductsModel> productsModels = WPUtils.getGiftProducts(r, dateModel, wpUtilsModel, true, WPViewPager.wpViewPager);
        if(productsModels.size() > 0) {
            FastItemAdapter<WPProductsModel> fastAdapter = new FastItemAdapter<>();
            fastAdapter.add(productsModels);
            fastAdapter.withSelectable(true);
            fastAdapter.setHasStableIds(false);
            fastAdapter.withItemEvent(new ClickEventHook<WPProductsModel>() {

                @Nullable
                @Override
                public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                    //return the views on which you want to bind this event
                    if (viewHolder instanceof WPProductsModel.ViewHolder) {
                        return ((WPProductsModel.ViewHolder) viewHolder).ivPlus;
                    }
                    return null;
                }

                @Override
                public void onClick(View v, int position, FastAdapter<WPProductsModel> fastAdapter1, final WPProductsModel item) {
                    //react on the click event
                    if (item.getQuantity() >= item.getBalance()) {
                        item.setCount(item.getCount() + 1);
                        item.setPlanned(item.getPlanned() + 1);
                        WPViewPager.wpViewPager.addProduct(item.getCode(), 2, item.getCount(), item.getName());
                        WPUtils.IS_CHANGED = true;
                        fastAdapter1.notifyDataSetChanged();
                    }
                }
            });

            fastAdapter.withItemEvent(new ClickEventHook<WPProductsModel>() {

                @Nullable
                @Override
                public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                    //return the views on which you want to bind this event
                    if (viewHolder instanceof WPProductsModel.ViewHolder) {
                        return ((WPProductsModel.ViewHolder) viewHolder).ivMinus;
                    }
                    return null;
                }

                @Override
                public void onClick(View v, int position, FastAdapter<WPProductsModel> fastAdapter1, final WPProductsModel item) {
                    //react on the click event
                    if (item.getCount() > 0) {
                        item.setCount(item.getCount() - 1);
                        item.setPlanned(item.getPlanned() - 1);
                        WPViewPager.wpViewPager.addProduct(item.getCode(), 2, item.getCount(), item.getName());
                        fastAdapter1.notifyDataSetChanged();
                    }

                }
            });


            //fill the recycler view
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(fastAdapter);
        } else {
            //TODO: show alert dialog for empty sample
        }
    }

}
