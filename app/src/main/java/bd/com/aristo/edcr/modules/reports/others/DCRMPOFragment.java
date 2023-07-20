package bd.com.aristo.edcr.modules.reports.others;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.models.response.ResponseDetail;
import bd.com.aristo.edcr.modules.reports.model.IDCRMPOModel;
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

public class DCRMPOFragment extends Fragment {

    public static final String TAG = "DCRMPOFragment";


    @Inject
    APIServices apiServices;
    @Inject
    UserModel userModel;
    @Inject
    Realm r;
    int month, year;
    int totalDot=0, totalPlan=0, totalNew=0, totalAbsent=0, totalDCR=0, totalSelected=0, totalSample=0, totalGift=0;
    int totalMDot=0, totalMPlan=0, totalMNew=0, totalMAbsent=0, totalMDCR=0, totalEDot=0, totalEPlan=0, totalENew=0, totalEAbsent=0, totalEDCR=0;
    private CompositeDisposable mCompositeDisposable;
    @BindView(R.id.dcrList)
    RecyclerView dcrList;
    @BindView(R.id.txtSelected)
    TextView txtSelected;
    @BindView(R.id.txtSample)
    TextView txtSample;
    @BindView(R.id.txtGift)
    TextView txtGift;

    @BindView(R.id.txtEAbsent)
    TextView txtEAbsent;
    @BindView(R.id.txtENew)
    TextView txtENew;
    @BindView(R.id.txtEPlan)
    TextView txtEPlan;
    @BindView(R.id.txtEDot)
    TextView txtEDot;
    @BindView(R.id.txtEDCR)
    TextView txtEDCR;

    @BindView(R.id.txtMDCR)
    TextView txtMDCR;
    @BindView(R.id.txtMAbsent)
    TextView txtMAbsent;
    @BindView(R.id.txtMNew)
    TextView txtMNew;
    @BindView(R.id.txtMPlan)
    TextView txtMPlan;
    @BindView(R.id.txtMDot)
    TextView txtMDot;

    @BindView(R.id.txtTotalDot)
    TextView txtTotalDot;
    @BindView(R.id.txtTotalPlan)
    TextView txtTotalPlan;
    @BindView(R.id.txtTotalNew)
    TextView txtTotalNew;
    @BindView(R.id.txtTotalAbsent)
    TextView txtTotalAbsent;
    @BindView(R.id.txtTotalDCR)
    TextView txtTotalDCR;
    FastItemAdapter<IDCRMPOModel> fastAdapter;
    Context context;
    LoadingDialog loadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public DCRMPOFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView = inflater.inflate(R.layout.fragment_report_dcr_mpo, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
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
        mCompositeDisposable.add(apiServices.getDCRMonthlyForMPO(userModel.getUserId(),
                DateTimeUtils.getMonthYear(month, year))
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<IDCRMPOModel>>() {
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
                    public void onNext(final ResponseDetail<IDCRMPOModel> value) {
                        if (value != null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {

                            if (value.getDataModelList() != null && value.getDataModelList().size() > 0) {

                                refreshList(value.getDataModelList());
                            }
                        }
                    }
                }));
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void refreshList(List<IDCRMPOModel> idcrmpoModelList) {
        totalDot=0;
        totalPlan=0;
        totalNew=0;
        totalAbsent=0;
        totalDCR=0;
        totalSelected=0;
        totalSample=0;
        totalGift=0;
        totalMDot=0;
        totalMPlan=0;
        totalMNew=0;
        totalMAbsent=0;
        totalMDCR=0;
        totalEDot=0;
        totalEPlan=0;
        totalENew=0;
        totalEAbsent=0;
        totalEDCR=0;
        fastAdapter = new FastItemAdapter<>();
        if (idcrmpoModelList != null && idcrmpoModelList.size() > 0) {
            Collections.sort(idcrmpoModelList, (idcrmpoModel, t1) -> t1.getDate().compareTo(idcrmpoModel.getDate()));
            for(int i = 0; i < idcrmpoModelList.size(); i++){
                IDCRMPOModel idcrmpoModel = idcrmpoModelList.get(i);
                totalSelected += idcrmpoModel.getSelectedCount();
                totalSample += (idcrmpoModel.getSampleIntern() + idcrmpoModel.getSampleRegular());
                totalGift += (idcrmpoModel.getGiftIntern() + idcrmpoModel.getGiftRegular());



                totalEDot += idcrmpoModel.geteDOT();
                totalEPlan += idcrmpoModel.geteWP();
                totalEAbsent += idcrmpoModel.geteAbsent();
                totalEDCR += idcrmpoModel.geteDCR();
                totalENew += idcrmpoModel.geteNewDCR();

                totalMDot += idcrmpoModel.getmDOT();
                totalMPlan += idcrmpoModel.getmWP();
                totalMAbsent += idcrmpoModel.getmAbsent();
                totalMDCR += idcrmpoModel.getmDCR();
                totalMNew += idcrmpoModel.getmNewDCR();
            }

            totalDot += (totalEDot + totalMDot);
            totalPlan += (totalEPlan + totalMPlan);
            totalAbsent += (totalEAbsent + totalMAbsent);

            totalNew += (totalMNew + totalENew);

            txtEAbsent.setText(""+totalEAbsent);
            txtMAbsent.setText(""+totalMAbsent);
            txtTotalAbsent.setText(""+totalAbsent);

            txtENew.setText(""+totalENew);
            txtMNew.setText(""+totalMNew);
            txtTotalNew.setText(""+totalNew);

            txtEDot.setText(""+totalEDot);
            txtMDot.setText(""+totalMDot);
            txtTotalDot.setText(""+totalDot);

            txtEPlan.setText(""+totalEPlan);
            txtMPlan.setText(""+totalMPlan);
            txtTotalPlan.setText(""+totalPlan);

            totalMDCR = totalMDCR - totalMAbsent;
            totalEDCR = totalEDCR - totalEAbsent;
            totalDCR += (totalEDCR + totalMDCR);
            txtEDCR.setText(""+totalEDCR);
            txtMDCR.setText(""+totalMDCR);
            txtTotalDCR.setText(""+totalDCR);
        }

        fastAdapter.add(idcrmpoModelList);
        fastAdapter.withSelectable(true);
        fastAdapter.setHasStableIds(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        dcrList.setLayoutManager(layoutManager);
        dcrList.setAdapter(fastAdapter);
        fastAdapter.getItemAdapter().withFilterPredicate((item, constraint) -> {
            if (item.getDate().toLowerCase().contains(constraint.toString().toLowerCase()))
                return false;
            else
                return true;
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_report_dcr_sum, menu);
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint(getString(R.string.search_dcr_sum));

        //listening to search query text change
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
        if (item.getItemId() == R.id.action_search) {

        }
        return super.onOptionsItemSelected(item);

    }


}
