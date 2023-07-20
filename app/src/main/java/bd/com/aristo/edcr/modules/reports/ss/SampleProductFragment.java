package bd.com.aristo.edcr.modules.reports.ss;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.response.ResponseDetail;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.reports.ss.model.ItemExecuteModel;
import bd.com.aristo.edcr.modules.reports.ss.model.SampleStatementModel;
import bd.com.aristo.edcr.modules.reports.ss.model.SampleStatementResponse;
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

public class SampleProductFragment extends Fragment {

    private final String TAG = SampleProductFragment.class.getSimpleName();

    @Inject
    Realm r;

    @Inject
    APIServices apiServices;
    static int month, year;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;


    public static ArrayList<ItemExecuteModel> itemExecuteModelList = new ArrayList<>();


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


    public static SampleProductFragment newInstance(int m, int y) {
        month = m;
        year = y;
        return new SampleProductFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initiate dependencies
        App.getComponent().inject(this);
        // remove all selections
        View rootView                                   = inflater.inflate(R.layout.fragment_sample_product, container, false);

        ButterKnife.bind(this, rootView);
        getUserInfo();
        //setHasOptionsMenu(true);
        refreshList();
        /*if (ConnectionUtils.isNetworkConnected(context)){
            downloadItemStatement();
        }else{

        }*/

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void refreshList(){
        //List<ItemStatementModel> itemStatementModels = SampleUtils.getItemStatementList(r,StringConstants.SAMPLE_ITEM, StringConstants.ITEM_FOR_REGULAR);
        SampleStatementResponse sampleStatementResponse = SampleStatementResponse.getInstance();
        List<SampleStatementModel> itemStatementModels = sampleStatementResponse.getSampleStatementModelList(StringConstants.SAMPLE_ITEM, StringConstants.ITEM_FOR_REGULAR);
        FastItemAdapter<SampleStatementModel> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(itemStatementModels);
        fastAdapter.withSelectable(true);
        fastAdapter.setHasStableIds(false);
        fastAdapter.withOnClickListener((v, adapter, item, position) -> {
            if (ConnectionUtils.isNetworkConnected(context)){
                if (item.getTotalExecution() > 0){
                    getDoctors(item.getProductCode(), item.getProductName());
                }else{
                    ToastUtils.shortToast(StringConstants.YOU_DID_NOT_EXECUTE);
                }
            }else{
                ToastUtils.shortToast(StringConstants.INTERNET_CONNECTION);
            }


            return false;
        });


        //fill the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(fastAdapter);
    }

    private CompositeDisposable mCompositeDisposable;

    private void getDoctors(final String pCode,final String pName){

        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please Wait...");
        loadingDialog.show();
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(apiServices.getItemExecute(userModel.getUserId(),
                String.valueOf(year),
                DateTimeUtils.getMonthNumber(month),
                pCode)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDetail<ItemExecuteModel>>() {
                    @Override
                    public void onComplete() {
                        MyLog.show("onComplete","called");
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        MyLog.show("onError",e.toString());
                    }

                    @Override
                    public void onNext(ResponseDetail<ItemExecuteModel> value) {
                        loadingDialog.dismiss();
                      //  MyLog.show(TAG,"getDoctors:value:"+value.getStatus());
                        if (value.getStatus()!=null && value.getStatus().equals("True")) {
                            itemExecuteModelList.clear();
                            itemExecuteModelList.addAll(value.getDataModelList());
                            StatementDialogFragment newFragment = StatementDialogFragment.newInstance(pName,StringConstants.SAMPLE_ITEM);
                            newFragment.show(getFragmentManager(), "dialog");
                        }else{
                            ToastUtils.shortToast(StringConstants.NO_DATA_FOUND_MSG);
                        }
                    }
                }));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mCompositeDisposable != null)
            mCompositeDisposable.clear();
    }


    @Override
    public void onResume() {
        super.onResume();


    }



}
