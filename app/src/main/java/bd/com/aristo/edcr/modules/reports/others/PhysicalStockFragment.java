package bd.com.aristo.edcr.modules.reports.others;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.models.response.ResponseDetail;
import bd.com.aristo.edcr.modules.reports.model.PSCDateModel;
import bd.com.aristo.edcr.modules.reports.model.PhysicalStockGet;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.ConnectionUtils;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 6/15/17.
 */

public class PhysicalStockFragment extends Fragment {

    public static final String TAG = "PhysicalStockFragment";


    @Inject
    APIServices apiServices;

    @Inject
    Realm r;

    UserModel userModel;
    int month, year;

    @BindView(R.id.llNoPsc)
    LinearLayout llNoPsc;

    @BindView(R.id.pscDateRecycler)
    RecyclerView pscDateRecycler;
    @BindView(R.id.pscRecycler)
    RecyclerView pscRecycler;
    @BindView(R.id.txtDate)
    TextView txtDate;
    @BindView(R.id.txtRemarks)
    TextView txtRemarks;

    private CompositeDisposable mCompositeDisposable;

    List<PSCDateModel> pscDateModelList = new ArrayList<>();
    FastItemAdapter<PSCDateModel> dateItemAdapter;
    FastItemAdapter<PhysicalStockGet> fastItemAdapter;
    HashMap<String, List<PhysicalStockGet>> listHashMap = new HashMap<>();
    Context context;
    LoadingDialog loadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public PhysicalStockFragment() {

    }

    public void setUserModel() {
        userModel = r.where(UserModel.class).findFirst();
        if (userModel == null) {
            ((AppCompatActivity) context).onBackPressed();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView = inflater.inflate(R.layout.fragment_report_psc, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        setUserModel();
        if (getArguments() != null) {
            month = getArguments().getInt("month");
            year = getArguments().getInt("year");
            if (ConnectionUtils.isNetworkConnected(context)) {
                syncMarketDVRList();
            } else {
                ToastUtils.longToast("No Internet Connection!!");
                ((AppCompatActivity) context).onBackPressed();
            }
        } else {
            ((AppCompatActivity) context).onBackPressed();
        }

        return rootView;
    }


    public void syncMarketDVRList() {
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        mCompositeDisposable = new CompositeDisposable();
        String monthYear = DateTimeUtils.getMonthYear(month, year);
        mCompositeDisposable.add(apiServices.getPhysicalStock(userModel.getUserId(), "MPO", monthYear, userModel.getUserId())
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<PhysicalStockGet>>() {
                    @Override
                    public void onComplete() {
                        MyLog.show(TAG, "onComplete doctors coverage sync");
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        //refreshList();
                    }

                    @Override
                    public void onNext(final ResponseDetail<PhysicalStockGet> value) {
                        if (value != null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {

                            if (value.getDataModelList() != null && value.getDataModelList().size() > 0) {
                                setAdapter(value.getDataModelList());
                            }
                        }
                    }
                }));
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_report_psc, menu);
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint(getString(R.string.search_psc));

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (fastItemAdapter != null)
                    fastItemAdapter.getItemAdapter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (fastItemAdapter != null)
                    fastItemAdapter.getItemAdapter().filter(query);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {

        }
        return super.onOptionsItemSelected(item);

    }


    public void setAdapter(List<PhysicalStockGet> physicalStockList) {
        if (physicalStockList != null && physicalStockList.size() > 0) {

            for(PhysicalStockGet physicalStock:physicalStockList){
                List<PhysicalStockGet> physicalStockGetList = new ArrayList<>();
                if(listHashMap.get(physicalStock.getDate()) != null && listHashMap.get(physicalStock.getDate()).size() > 0){
                    physicalStockGetList = listHashMap.get(physicalStock.getDate());
                }
                physicalStockGetList.add(physicalStock);
                listHashMap.put(physicalStock.getDate(), physicalStockGetList);
            }

            for(String date:listHashMap.keySet()){
                PSCDateModel pscDateModel = new PSCDateModel();
                pscDateModel.setDate(date);
                pscDateModel.setRemarks(listHashMap.get(date).get(0).getRemarks());
                pscDateModel.setPhysicalStockGetList(listHashMap.get(date));
                pscDateModel.withSetSelected(false);
                pscDateModel.setClicked(false);
                pscDateModelList.add(pscDateModel);
            }

            dateItemAdapter = new FastItemAdapter<>();
            dateItemAdapter.add(pscDateModelList);
            dateItemAdapter.withSelectable(true);
            dateItemAdapter.withMultiSelect(false);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            pscDateRecycler.setAdapter(dateItemAdapter);
            pscDateRecycler.setLayoutManager(layoutManager);
            llNoPsc.setVisibility(View.GONE);

            dateItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<PSCDateModel>() {
                @Override
                public boolean onClick(View v, IAdapter<PSCDateModel> adapter, PSCDateModel item, int position) {
                    generatePSCList(item.getPhysicalStockGetList());
                    setDateText(item.getDate(), item.getRemarks());
                    return false;
                }
            });
        } else {
            pscRecycler.setVisibility(View.GONE);
        }

    }

    private void setDateText(String date, String remarks){
        String strRemarks = "No remarks";
        txtDate.setText(date);
        if(!TextUtils.isEmpty(remarks)){
            strRemarks = "Remarks: "+remarks;
        }
        txtRemarks.setText(strRemarks);

    }

    public void generatePSCList(List<PhysicalStockGet> pscList){
        if (pscList != null && pscList.size() > 0) {
            Collections.sort(pscList, new Comparator<PhysicalStockGet>() {
                @Override
                public int compare(PhysicalStockGet physicalStockGet, PhysicalStockGet t1) {
                    return t1.getProductName().compareTo(physicalStockGet.getProductName());
                }
            });
        }
        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(pscList);
        fastItemAdapter.withSelectable(false);
        fastItemAdapter.setHasStableIds(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        pscRecycler.setLayoutManager(layoutManager);
        pscRecycler.setAdapter(fastItemAdapter);

        fastItemAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<PhysicalStockGet>() {
            @Override
            public boolean filter(PhysicalStockGet item, CharSequence constraint) {
                if (item.getProductName().toLowerCase().contains(constraint.toString().toLowerCase()))
                    return false;
                else
                    return true;
            }
        });
    }
}
