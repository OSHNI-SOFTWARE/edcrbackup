package bd.com.aristo.edcr.modules.wp.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;

import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.ProductModel;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.modules.wp.WPUtils;
import bd.com.aristo.edcr.modules.wp.model.WPProductsModel;
import bd.com.aristo.edcr.modules.wp.model.WPUtilsModel;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import bd.com.aristo.edcr.utils.ui.texts.AnEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 8/11/17.
 */

public class InternSampleFragment extends Fragment {

    @Inject
    Realm r;

    @BindView(R.id.rvTodayProducts)
    RecyclerView rv;

    @BindView(R.id.addItemIV)
    ATextView addItemIV;

    @BindView(R.id.llSearchLayout)
    LinearLayout llSearchLayout;

    @BindView(R.id.searchEditText)
    AnEditText searchEditText;

    List<ProductModel> sampleModelList;

    public UserModel userModel;
    //public WPInternViewPager wpViewPager;
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



    public InternSampleFragment() {
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
            //wpViewPager = (WPInternViewPager) getArguments().getSerializable(StringConstants.WORK_PLAN_PAGER);
            getUserInfo();
            setupList();
            llSearchLayout.setVisibility(View.VISIBLE);
        } else {
            ((Activity) context).onBackPressed();
        }

        addItemIV.setVisibility(View.GONE);


        return rootView;
    }


    private void setupList() {
        sampleModelList = WPUtils.getMonthWiseSamplesForIntern(r, dateModel.getYear(), dateModel.getMonth());
        if(sampleModelList.size() > 0)  { //doctors available, load list
            refreshList();
        }else{ // no doctor is available, download now
            //TODO: alert dialog
            Bundle bundle = new Bundle();
            bundle.putString(StringConstants.SAMPLE_EMPTY_DIALOG_TITLE, "Intern Sample Empty");
            bundle.putString(StringConstants.SAMPLE_EMPTY_DIALOG_MSG, "There is no sample for Intern, please sync!!");
            SampleEmptyDialog dialog = new SampleEmptyDialog();
            dialog.setArguments(bundle);
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "sample_empty_dialog");
        }
    }

    private void refreshList(){
        List<WPProductsModel> productsModels = WPUtils.getSampleProductsForIntern(r, sampleModelList, dateModel, wpUtilsModel, WPInternViewPager.wpViewPager);
        final FastItemAdapter<WPProductsModel> fastAdapter = new FastItemAdapter<>();
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
                if(item.getQuantity() >= item.getBalance()){
                    item.setCount(item.getCount() + 1);
                    item.setPlanned(item.getPlanned() +1);
                    WPUtils.IS_CHANGED = true;
                    //WPUtils.setCount(item, r, dateModel, wpUtilsModel, 2);
                    WPInternViewPager.wpViewPager.addProduct(item.getCode(), 1, item.getCount(), item.getName());
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
                if(item.getCount() > 0){
                    item.setCount(item.getCount() - 1);
                    item.setPlanned(item.getPlanned() -1);
                    //WPUtils.setCount(item, r, dateModel, wpUtilsModel, 2);
                    WPInternViewPager.wpViewPager.addProduct(item.getCode(), 1, item.getCount(), item.getName());
                    fastAdapter1.notifyDataSetChanged();
                }

            }
        });


        //fill the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(fastAdapter);

        //Search

        fastAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<WPProductsModel>() {
            @Override
            public boolean filter(WPProductsModel item, CharSequence constraint) {
                return !item.getName().toLowerCase().contains(constraint.toString().toLowerCase());
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {

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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
