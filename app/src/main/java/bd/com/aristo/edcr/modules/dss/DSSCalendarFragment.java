package bd.com.aristo.edcr.modules.dss;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.itemanimators.SlideLeftAlphaAnimator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.modules.wp.WPUtils;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.constants.MainMenuConstants;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 6/5/17.
 */

public class DSSCalendarFragment extends Fragment {

    //saveAndNext our FastAdapter
    private FastAdapter<CalenderModel> mFastAdapter;



    public String[] days;
    public Calendar calCurrent;
    int dayOfMonth, month, year, lastDay;

    public List<String> listOfSavedDate;
    public List<String> listOfSavedDateStatus;


    @Inject
    APIServices apiServices;
    @Inject
    Realm r;
    @BindView(R.id.rv)
    RecyclerView rv;
    public Bundle savedInstanceState;
    ItemAdapter<CalenderModel> itemAdapter;
    public int[] isWorkPlanned = new int[35];

    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public DSSCalendarFragment() {
        listOfSavedDateStatus                           = new ArrayList<>();
        listOfSavedDate                                 = new ArrayList<>();
        days                                            = new String[35];
    }

    DateModel dateModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        View rootView                                   = inflater.inflate(R.layout.fragment_calendar_dss, container, false);
        ButterKnife.bind(this, rootView);
        this.savedInstanceState                         = savedInstanceState;
        calCurrent = Calendar.getInstance();
        MainMenuConstants.getInstance().setActivityWH((Activity) context);
        setupCalendar();
        setAdapter();
        return rootView;
    }



    public  void setupCalendar(){

        refreshDays(calCurrent, true);

        ((Activity) context).setTitle("DSS for "+ DateTimeUtils.getMonthForInt(calCurrent.get(Calendar.MONTH) + 1) +","+calCurrent.get(Calendar.YEAR));


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the adapter to the bundel
        outState = mFastAdapter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }




    public void refreshDays(Calendar cal, boolean isCurrentMonth) {
        for(int i = 0; i < 35; i++){
            days[i] = "";
        }
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        if(isCurrentMonth){
            dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        } else dayOfMonth = 0;

        cal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDay = (int) cal.get(Calendar.DAY_OF_WEEK);
        int realFirstDay = (firstDay) % 7;

        // populate days
        int dayNumber = 1;
        for (int i = realFirstDay; i < days.length; i++) {
            days[i] = "" + dayNumber;

            dayNumber++;
            if((lastDay) < dayNumber){
                break;
            }
        }
        if (realFirstDay + lastDay == 36) {
            days[0] = "" + dayNumber;
        } else if (realFirstDay + lastDay == 37) {
            days[0] = "" + dayNumber;
            days[1] = "" + (dayNumber + 1);
        }
    }

    public void setAdapter(){

        mFastAdapter = new FastAdapter<>();
        itemAdapter = new ItemAdapter<>();
        mFastAdapter.setHasStableIds(true);
        mFastAdapter.withOnClickListener(new FastAdapter.OnClickListener<CalenderModel>() {
            @Override
            public boolean onClick(View v, IAdapter<CalenderModel> adapter, CalenderModel item, int position) {
                if(!item.date.getText().equals("")){
                    DateModel dateModel = new DateModel(Integer.valueOf(item.date.getText().toString()), month, year, 0, lastDay);
                    Fragment fragment = new DSSFragment();
                    Bundle b = new Bundle();
                    b.putSerializable("dateModel", dateModel);
                    fragment.setArguments(b);
                    getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("dss_products").commit();
                }
                return false;
            }
        });


        rv.setLayoutManager(new GridLayoutManager(context,7));
        rv.setItemAnimator(new SlideLeftAlphaAnimator());
        rv.setAdapter(itemAdapter.wrap(mFastAdapter));
        List<CalenderModel> items = new ArrayList<>();
        isWorkPlanned           = WPUtils.getSavedWPDayList(r, month, year);

        for (int i = 0; i < 35; i++) {
            CalenderModel item = new CalenderModel();
            item
                    .withName(days[i])
                    .withIdentifier(100 + i);
            if(!days[i].equals("")) {
                if (String.valueOf(dayOfMonth).equals(days[i])) {
                    item.withIsCurrent(true);
                }
                if (isWorkPlanned[Integer.valueOf(days[i])] > 0) {
                    item.setSaved(true);
                }
            }
            items.add(item);
        }
        itemAdapter.add(items);
        mFastAdapter.withSavedInstanceState(savedInstanceState);

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }


}
