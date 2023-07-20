package bd.com.aristo.edcr.modules.dvr.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mikepenz.itemanimators.SlideLeftAlphaAnimator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.dvr.DVRSaveListener;
import bd.com.aristo.edcr.modules.dvr.model.DVRCalender;
import bd.com.aristo.edcr.modules.dvr.model.DayShift;
import bd.com.aristo.edcr.modules.dvr.model.IDoctorsModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 8/5/2018.
 */

@SuppressLint("ValidFragment")
public class DVRCalendarDialog extends DialogFragment {
    private static final String TAG = "DCRUploadDialog";

    AlertDialog alertDialog;
    @BindView(R.id.txtDoctorName)
    TextView txtDoctorName;
    @BindView(R.id.rvCalender)
    RecyclerView rvCalender;
    //Unbinder unbinder;
    int month, year, dayOfMonth, lastDay;
    boolean isCurrent;
    IDoctorsModel iDoctorsModel;
    List<DayShift> dayShiftList;

    public String[] days;
    public Calendar cal;

    private DVRSaveListener listener;
    Context context;
    private FastAdapter<DVRCalender> mFastAdapter;
    ItemAdapter<DVRCalender> itemAdapter;
    boolean[] mornings = new boolean[35];
    boolean[] evenings = new boolean[35];


    @Inject
    Realm r;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @SuppressLint("ValidFragment")
    public DVRCalendarDialog(DVRSaveListener dvrSaveListener,
                             int month,
                             int year,
                             IDoctorsModel iDoctorsModel,
                             List<DayShift> dayShiftList,
                             boolean isCurrent) {
        this.listener = dvrSaveListener;
        this.month = month;
        this.year = year;
        this.iDoctorsModel = iDoctorsModel;
        this.dayShiftList = dayShiftList;
        this.isCurrent = isCurrent;
        setMorningEvening();
    }

    public DVRCalendarDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        App.getComponent().inject(this);
        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_dialog_dvr_calendar, null);
        ButterKnife.bind(this, v);
        days                                            = new String[35];
        setCalendar();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setView(v);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        return alertDialog;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind();
    }



    public void setCalendar(){
        cal = Calendar.getInstance();
        if(isCurrent){
            dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        } else {
            dayOfMonth = 0;
            cal.set(year, month - 1, dayOfMonth);
        }

        for(int i = 0; i < 35; i++){
            days[i] = "";
        }
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
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
        mFastAdapter = new FastAdapter<>();

        itemAdapter = new ItemAdapter<>();
        mFastAdapter.setHasStableIds(true);
        mFastAdapter.withItemEvent(new ClickEventHook<DVRCalender>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof DVRCalender.ViewHolder) {
                    return ((DVRCalender.ViewHolder) viewHolder).txtMorning;
                }
                return null;
            }
            @Override
            public void onClick(View v, int position, FastAdapter<DVRCalender> fastAdapter, DVRCalender item) {
                if(!TextUtils.isEmpty(item.date.toString())){
                    mornings[position] = !mornings[position];
                    item.setMorning(mornings[position]);
                    fastAdapter.notifyDataSetChanged();
                }
            }
        });

        mFastAdapter.withItemEvent(new ClickEventHook<DVRCalender>() {

            @Nullable
            @Override
            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                //return the views on which you want to bind this event
                if (viewHolder instanceof DVRCalender.ViewHolder) {
                    return ((DVRCalender.ViewHolder) viewHolder).txtEvening;
                }
                return null;
            }
            @Override
            public void onClick(View v, int position, FastAdapter<DVRCalender> fastAdapter, DVRCalender item) {
                if(!TextUtils.isEmpty(item.date.toString())){
                    evenings[position] = !evenings[position];
                    item.setEvening(evenings[position]);
                    fastAdapter.notifyDataSetChanged();
                }
            }
        });


        rvCalender.setLayoutManager(new GridLayoutManager(context,7));
        rvCalender.setItemAnimator(new SlideLeftAlphaAnimator());
        rvCalender.setAdapter(itemAdapter.wrap(mFastAdapter));
        List<DVRCalender> items = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            DVRCalender item = new DVRCalender();
            item
                    .withName(days[i])
                    .withIdentifier(100 + i);
            item.setEvening(evenings[i]);
            item.setMorning(mornings[i]);
            if(String.valueOf(dayOfMonth).equals(days[i])){
                item.withIsCurrent(true);
            }
            items.add(item);
        }

        itemAdapter.add(items);
        //mFastAdapter.withSavedInstanceState(savedInstanceState);



    }

    public void setMorningEvening(){
        for(DayShift dayShift:dayShiftList){
            if(dayShift.isMorning()){
                mornings[dayShift.getDay()] = true;
            } else {
                evenings[dayShift.getDay()] = true;
            }
        }
    }
}
