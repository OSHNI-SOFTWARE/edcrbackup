package bd.com.aristo.edcr.modules.reports.others;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.models.response.ResponseDetail;
import bd.com.aristo.edcr.modules.reports.model.AccompanyAdapter;
import bd.com.aristo.edcr.modules.reports.model.DCRAccompany;
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

public class DCRAccompanyFragment extends Fragment {

    public static final String TAG = "DCRAccompanyFragment";


    @Inject
    APIServices apiServices;
    @Inject
    UserModel userModel;
    @Inject
    Realm r;
    int month, year;
    String userID = "", designation;
    private CompositeDisposable mCompositeDisposable;
    @BindView(R.id.rvDcrAccompany)
    RecyclerView rv;
    List<DCRAccompany> dcrAccompanyList = null;
    List<AccompanyAdapter> IDoctorCoverageList;
    final FastItemAdapter<AccompanyAdapter> fastAdapter = new FastItemAdapter<>();
    private Context context;
    LoadingDialog loadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public DCRAccompanyFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView = inflater.inflate(R.layout.fragment_dcr_accompany, container, false);
        ButterKnife.bind(this, rootView);
        if(getArguments() != null){
            month = getArguments().getInt("month");
            year = getArguments().getInt("year");
            if (ConnectionUtils.isNetworkConnected(context)) {
                syncDCRAccompany();
            } else {
                ToastUtils.longToast("No Internet Connection!!");
                ((AppCompatActivity) context).onBackPressed();
            }
        } else {
            ((AppCompatActivity) context).onBackPressed();
        }
        setHasOptionsMenu(true);
        return rootView;
    }


    public void syncDCRAccompany() {
        loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(apiServices.getDCRAccompany(userModel.getUserId(), "MPO", DateTimeUtils.getMonthYear(month, year))
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<DCRAccompany>>() {
                    @Override
                    public void onComplete() {
                        MyLog.show(TAG, "onComplete Accompany sync");
                        loadingDialog.dismiss();
                        ToastUtils.longToast("Accompany sync Complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        ToastUtils.longToast("e");
                        refreshList();
                    }

                    @Override
                    public void onNext(final ResponseDetail<DCRAccompany> value) {
                        Log.e(TAG, value.getStatus());
                        if (value != null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {

                            ToastUtils.longToast(value.getStatus());

                            if (value.getDataModelList() != null && value.getDataModelList().size() > 0) {
                                dcrAccompanyList = new ArrayList<>();
                                dcrAccompanyList = value.getDataModelList();
                                refreshList();
                            }
                        }
                    }
                }));
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void refreshList() {
        List<AccompanyAdapter> accompanyAdapters = getDCRAccompany();
        if (accompanyAdapters != null && accompanyAdapters.size() > 0) {
            Collections.sort(accompanyAdapters, new Comparator<AccompanyAdapter>() {
                @Override
                public int compare(AccompanyAdapter accompanyAdapter, AccompanyAdapter t1) {
                    return t1.getVisitDate().compareTo(accompanyAdapter.getVisitDate());
                }
            });
        }

        fastAdapter.add(accompanyAdapters);
        fastAdapter.withSelectable(true);
        fastAdapter.setHasStableIds(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(fastAdapter);
        fastAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<AccompanyAdapter>() {
            @Override
            public boolean filter(AccompanyAdapter item, CharSequence constraint) {
                if (item.getVisitDate().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    return false;
                } else if (item.getAccompanyName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    return false;
                } else return true;
            }
        });
    }

    public List<AccompanyAdapter> getDCRAccompany() {
        IDoctorCoverageList = new ArrayList<>();
        //dcrAccompanyList
        if (dcrAccompanyList != null && dcrAccompanyList.size() > 0) {

            for (DCRAccompany model : dcrAccompanyList) {
                AccompanyAdapter accompanyAdapter = new AccompanyAdapter();
                accompanyAdapter.setMpoCode(model.getMpoCode());
                accompanyAdapter.setMpoName(model.getMpoName());
                accompanyAdapter.setAccompanyName(model.getAccompanyCode());
                accompanyAdapter.setVisitDate(model.getVisitDate());
                accompanyAdapter.setMorningCount(model.getMorningCount());
                accompanyAdapter.setEveningCount(model.getEveningCount());
                accompanyAdapter.setTotal(String.valueOf(Integer.parseInt(model.getMorningCount()) + Integer.parseInt(model.getEveningCount())));

                IDoctorCoverageList.add(accompanyAdapter);
            }


        }
        return IDoctorCoverageList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.accompany_report_menu, menu);
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint(getString(R.string.search_hint_accompany));

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (fastAdapter != null)
                    fastAdapter.getItemAdapter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (fastAdapter != null)
                    fastAdapter.getItemAdapter().filter(query);
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
        return super.onOptionsItemSelected(item);

    }


}
