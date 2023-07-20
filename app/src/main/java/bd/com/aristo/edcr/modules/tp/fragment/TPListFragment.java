package bd.com.aristo.edcr.modules.tp.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.tp.activity.TourPlanActivity;
import bd.com.aristo.edcr.models.Day;
import bd.com.aristo.edcr.modules.tp.model.ITPList;
import bd.com.aristo.edcr.modules.tp.model.TPPlaceRealmModel;
import bd.com.aristo.edcr.modules.tp.model.TPServerModel;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by monir.sobuj on 7/11/17.
 */

public class TPListFragment extends Fragment {

    @Inject
    Realm r;

    @BindView(R.id.tpList)
    RecyclerView mRecyclerView;


    public UserModel userModel;
    int month, year, lastDate;

    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }

    public TPListFragment() {

    }
    public static TPListFragment newInstance() {
        return new TPListFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_tp_list, container, false);
        ButterKnife.bind(this, rootView);
        getUserInfo();
        month = getArguments().getInt("month");
        year = getArguments().getInt("year");
        lastDate = getLastDate(month, year);

        ((Activity) context).setTitle(String.format(getString(R.string.tp_list), DateTimeUtils.getFullMonthForInt(month)));

        //setHasOptionsMenu(true);

        setupList();
        return rootView;
    }


    private void setupList() {

        //setTestData();

        final FastItemAdapter<ITPList> fastAdapter = new FastItemAdapter<>();
        fastAdapter.add(getTourPlanForList());
        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<ITPList>() {
            @Override
            public boolean onClick(View v, IAdapter<ITPList> adapter, final ITPList item, int position) {
                ToastUtils.shortToast("Day:"+item.getDay());
                Day date = new Day();
                date.setCell(Integer.parseInt(item.getCell()));
                date.setDay(item.getDay());
                date.setMonth(item.getMonth());
                date.setYear(item.getYear());
                date.setLastDay(lastDate);
                date.setCopyDate(item.getDay());
                TourPlanActivity.start(((AppCompatActivity) context), date);
                return false;
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemViewCacheSize(100);
        mRecyclerView.setAdapter(fastAdapter);
    }

    public List<ITPList> getTourPlanForList(){

        //Get tp for current month,year and contact address is not empty

        List<ITPList> itpLists = new ArrayList<>();


        for (int i = 1; i <  lastDate+ 1; i++){

            ITPList itpListModel = new ITPList();

            List<TPServerModel> tpList = r.where(TPServerModel.class)
                    .equalTo(TPServerModel.COL_YEAR, year)
                    .equalTo(TPServerModel.COL_MONTH, month)
                    .notEqualTo(TPServerModel.COL_CONTACT, "")
                    .equalTo(TPServerModel.COL_DAY,String.valueOf(i))
                    .findAll();

            if (tpList!=null && tpList.size()>0){

                itpListModel.setDate(formatDate(i));

                for (TPServerModel tpModel:tpList ) {

                    itpListModel.setDay(Integer.parseInt(tpModel.getDay()));
                    itpListModel.setMonth(tpModel.getMonth());
                    itpListModel.setYear(tpModel.getYear());
                    itpListModel.setCell(tpModel.getcCell());


                    if (tpModel.getShift().equalsIgnoreCase(StringConstants.MORNING)){
                        itpListModel.setTPChanged(tpModel.isChangedFromServer());
                        if (tpModel.getContactPlace()!=null && !TextUtils.isEmpty(tpModel.getContactPlace())){
                            itpListModel.setMorningShiftType(tpModel.getShiftType());
                            itpListModel.setMorningContact(tpModel.getContactPlace());
                            itpListModel.setMorningRT(tpModel.getReportTime());
                            itpListModel.setMorningNDA(tpModel.getnDA());

                            RealmResults<TPPlaceRealmModel> placeList = r.where(TPPlaceRealmModel.class)
                                    .equalTo(TPPlaceRealmModel.COL_TP_ID, tpModel.getLocalId())
                                    .findAll();
                            StringBuilder places = new StringBuilder();
                            for (TPPlaceRealmModel workPlaceModel : placeList) {
                                places.append(workPlaceModel.getCode());
                                places.append(",");
                            }
                            itpListModel.setMorningPlacelist(places.toString());
                        }else{
                            //no morning tp found
                        }

                    }else{
                        if (tpModel.getContactPlace()!=null && !TextUtils.isEmpty(tpModel.getContactPlace())){
                            itpListModel.setEveningShiftType(tpModel.getShiftType());
                            itpListModel.setEveningContact(tpModel.getContactPlace());
                            itpListModel.setEveningRT(tpModel.getReportTime());
                            itpListModel.setEveningNDA(tpModel.getnDA());
                            RealmResults<TPPlaceRealmModel> placeList = r.where(TPPlaceRealmModel.class)
                                    .equalTo(TPPlaceRealmModel.COL_TP_ID, tpModel.getLocalId())
                                    .findAll();

                            StringBuilder places = new StringBuilder();
                            for (TPPlaceRealmModel workPlaceModel : placeList) {
                                places.append(workPlaceModel.getCode() );
                                places.append(",");
                            }
                            itpListModel.setEveningPlacelist(places.toString());

                        }else{
                            //no evening tp found
                        }
                    }

                }//end inner loop

                itpLists.add(itpListModel);
            }else{
                //no tp found
            }


        }//end month loop

        return itpLists;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_other_tp,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private int getLastDate(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public String formatDate(int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        Date date1 = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EE, dd");
        String hireDate = sdf.format(date1);
        return hireDate;
    }

}
