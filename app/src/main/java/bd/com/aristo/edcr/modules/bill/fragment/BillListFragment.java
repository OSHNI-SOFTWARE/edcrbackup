package bd.com.aristo.edcr.modules.bill.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.TARateModel;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.models.response.ResponseDetail;
import bd.com.aristo.edcr.modules.bill.model.Bill;
import bd.com.aristo.edcr.modules.bill.model.IBillListItem;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.ConnectionUtils;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by monir.sobuj on 6/21/17.
 */

public class BillListFragment extends Fragment {

    public static final String TAG = "BillListFragment";

    @Inject
    Realm r;
    @Inject
    APIServices apiServices;

    @BindView(R.id.txtNoItem)
    ATextView txtNoItem;
    @BindView(R.id.rvBillList)
    RecyclerView rvBillList;
    @BindView(R.id.txtTotalBill)
    ATextView txtTotalBill;
    @BindView(R.id.rlMain)
    RelativeLayout rlMain;
    @BindView(R.id.ivLeftArrow)
    ImageView ivLeftArrow;
    @BindView(R.id.ivRightArrow)
    ImageView ivRightArrow;
    @BindView(R.id.txtMonth)
    ATextView txtMonth;

    Context context;

    FastItemAdapter<IBillListItem> iBillListItemFastAdapter;
    List<IBillListItem> billListItemList = new ArrayList<>();
    private CompositeDisposable mCompositeDisposableForGet;

    DateModel dateModel;
    public UserModel userModel;
    Calendar calCurrent;
    LoadingDialog loadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    //get user info from db
    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }


    public BillListFragment() {
    }

    public static BillListFragment newInstance() {
        return new BillListFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        App.getComponent().inject(this);
        View rootView = inflater.inflate(R.layout.fragment_monthly_bill_list, container, false);
        ButterKnife.bind(this, rootView);
        SystemUtils.hideSoftKeyboard(rootView, context);
        //user info from db
        getUserInfo();
        calCurrent = Calendar.getInstance();
        dateModel = getDateModel(calCurrent);
        txtMonth.setText(DateTimeUtils.MONTH_NAME[dateModel.getMonth()-1].toUpperCase());
        ivRightArrow.setVisibility(View.INVISIBLE);
        loadingDialog = LoadingDialog.newInstance(getContext(), "Please Wait...");

        //get bill from server.
        getMonthlyBill();

        return rootView;
    }

    public DateModel getDateModel(Calendar c) {
        DateModel dateModel = new DateModel(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR), 0, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateModel;
    }

    public void initializeView() {

        billListItemList.clear();
        billListItemList.addAll(getBillList());
        if (billListItemList.size() > 0) {
            rlMain.setVisibility(View.VISIBLE);
            txtNoItem.setVisibility(View.GONE);
            iBillListItemFastAdapter = new FastItemAdapter<>();
            iBillListItemFastAdapter.add(billListItemList);
            iBillListItemFastAdapter.setHasStableIds(true);
            iBillListItemFastAdapter.withSelectable(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            rvBillList.setLayoutManager(layoutManager);
            rvBillList.setAdapter(iBillListItemFastAdapter);

            iBillListItemFastAdapter.withOnClickListener(new FastAdapter.OnClickListener<IBillListItem>() {
                @Override
                public boolean onClick(View v, IAdapter<IBillListItem> adapter, IBillListItem item, int position) {
                    TARateModel ta = r.where(TARateModel.class).findFirst();
                    if (ta != null) {
                        DetailsFragment fragment = new DetailsFragment();
                        Bundle args = new Bundle();
                        args.putInt("billDay", Integer.valueOf(item.getDay()));
                        args.putInt("billMonth", dateModel.getMonth());
                        args.putInt("billYear", dateModel.getYear());
                        args.putBoolean("billFromServer", item.isFromServer());
                        fragment.setArguments(args);
                        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.hide(fm.findFragmentByTag("bill_list"));
                        ft.add(R.id.billContainer, fragment);
                        ft.addToBackStack(null);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                        //((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.billContainer, fragment).addToBackStack("bill_detail").commit();
                    } else {
                        displayAlert();
                    }
                    return  false;
                }
            });

        } else {
            rlMain.setVisibility(View.GONE);
            txtNoItem.setVisibility(View.VISIBLE);
        }
    }

    public void getMonthlyBill(){
        String userID = userModel.getUserId();
        String mpoCode = userModel.getLoginID();
        String year = String.valueOf(dateModel.getYear());
        String month = DateTimeUtils.getMonthNumber(dateModel.getMonth());
        MyLog.show("GetBill", "UserId: "+userID+" MPOCode: "+mpoCode+" month: "+month+" year: "+year);
        mCompositeDisposableForGet = new CompositeDisposable();

        if(ConnectionUtils.isNetworkConnected(getContext())) { //internet connected.
            loadingDialog.show();
            mCompositeDisposableForGet.add(apiServices.getMonthlyBill(userID,
                    "MPO",
                    mpoCode,
                    year,
                    month)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<ResponseDetail<Bill>>() {
                        @Override
                        public void onComplete() {
                            MyLog.show(TAG, "onComplete");
                            loadingDialog.dismiss();
                            initializeView();
                        }

                        @Override
                        public void onError(Throwable e) {
                            MyLog.show(e.getLocalizedMessage(), e.getMessage());
                            ToastUtils.shortToast(StringConstants.SERVER_ERROR_MSG);
                            loadingDialog.dismiss();
                            initializeView();
                        }

                        @Override
                        public void onNext(ResponseDetail<Bill> value) {
                            if (value.getStatus() != null && value.getStatus().equalsIgnoreCase(StringConstants.SERVICE_RESPONSE_STATUS)) {
                                if (value.getDataModelList().size() > 0) {
                                    updateBill(value.getDataModelList());
                                }
                            }
                        }
                    }));
        } else { //No internet
            initializeView();
        }
    }

    public List<IBillListItem> getBillList() {
        int monthlyBilAmount = 0;
        List<IBillListItem> iBillListItems = new ArrayList<>();
        List<Bill> bills = r.where(Bill.class).equalTo(Bill.COL_MONTH, dateModel.getMonth())
                .equalTo(Bill.COL_YEAR, dateModel.getYear())
                .sort(Bill.COL_DAY, Sort.ASCENDING)
                .findAll();
        int i = 100;
        for (Bill bill : bills) {
            IBillListItem iBillListItem = new IBillListItem();
            iBillListItem.setDay("" + bill.getDay());
            iBillListItem.setNod("" + bill.getnDA());
            iBillListItem.setTa("" + bill.getTa());
            iBillListItem.setDa("" + bill.getDaAmount());
            iBillListItem.setTotal("" + bill.getBillAmount());
            iBillListItem.setReview(bill.getIsReviewEnabled().equalsIgnoreCase(StringConstants.YES));
            iBillListItem.setStatus(bill.getStatus());
            iBillListItem.setFromServer(bill.isUploaded());
            iBillListItem.setId(++i);
            monthlyBilAmount += bill.getBillAmount();
            iBillListItems.add(iBillListItem);
        }
        txtTotalBill.setText(String.format(Locale.ENGLISH, "Total Bill :  %d", monthlyBilAmount));
        return iBillListItems;
    }
    @OnClick(R.id.flAddBill)
    public void addBill(){
        TARateModel ta = r.where(TARateModel.class).findFirst();
        if (ta != null) {
            Fragment fragment = new DetailsFragment();
            FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(fm.findFragmentByTag("bill_list"));
            ft.add(R.id.billContainer, fragment);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            /*((AppCompatActivity) context)
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.billContainer, fragment)
                    .addToBackStack("bill_detail")
                    .commit();*/
        } else {
            displayAlert();
        }
    }
    @OnClick(R.id.ivLeftArrow)
    public void prevMonth(View v){
        v.setVisibility(View.INVISIBLE);
        ivRightArrow.setVisibility(View.VISIBLE);
        int month = calCurrent.get(Calendar.MONTH);
        int year = calCurrent.get(Calendar.YEAR);
        if(calCurrent.get(Calendar.MONTH) == 0){
            calCurrent.set(year-1, 11, 1);
        } else {

            calCurrent.set(year, month-1, 1);
        }
        dateModel = getDateModel(calCurrent);
        txtMonth.setText(DateTimeUtils.MONTH_NAME[dateModel.getMonth()-1].toUpperCase());
        getMonthlyBill();
    }
    @OnClick(R.id.ivRightArrow)
    public void nextMonth(View v){
        v.setVisibility(View.INVISIBLE);
        ivLeftArrow.setVisibility(View.VISIBLE);
        calCurrent.roll(Calendar.MONTH, true);
        dateModel = getDateModel(calCurrent);
        txtMonth.setText(DateTimeUtils.MONTH_NAME[dateModel.getMonth()-1].toUpperCase());
        getMonthlyBill();
    }

    public void updateBill(final List<Bill> billList){
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(Bill bill:billList){
                    bill.setMonth(dateModel.getMonth());
                    bill.setYear(dateModel.getYear());
                    bill.setUploaded(true);
                    realm.insertOrUpdate(bill);
                }
            }
        });
    }

    public  void displayAlert() {

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Bill");
        alert.setMessage("Bill not allowed now");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();

            }
        });
        alert.show();
    }
}
