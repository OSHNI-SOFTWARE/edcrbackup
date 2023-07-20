package bd.com.aristo.edcr.modules.wp.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.modules.wp.WPListener;
import bd.com.aristo.edcr.modules.wp.WPUtils;
import bd.com.aristo.edcr.modules.wp.model.WPModel;
import bd.com.aristo.edcr.modules.wp.model.WPProductModel_T;
import bd.com.aristo.edcr.modules.wp.model.WPUtilsModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.ToastUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.collections.AViewPager;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 6/5/17.
 */

public class WPInternViewPager extends Fragment implements WPListener{

    public static final String TAG = "WorkPlan";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    @Inject
    APIServices apiServices;
    @Inject
    Realm r;

    @BindView(R.id.wp_view_pager)
    AViewPager aViewpager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.llUpload)
    LinearLayout llUpload;


    @BindView(R.id.llSave)
    LinearLayout llSave;

    public UserModel userModel;
    public DateModel dateModel;
    public WPUtilsModel wpUtilsModel;
    Map<String, WPProductModel_T> wpProductModel_tMap = new HashMap<>();
    public static WPInternViewPager wpViewPager;
    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }

    private int[] tabIcons = {
            R.drawable.ic_mini_product,
            R.drawable.ic_mini_gift_color
    };

    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }



    public WPInternViewPager() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initiate dependencies
        App.getComponent().inject(this);
        View rootView = inflater.inflate(R.layout.fragment_wp_view_pager, container, false);
        ButterKnife.bind(this, rootView);
        wpViewPager = this;
        getUserInfo();
        if(getArguments() != null) {
            dateModel = (DateModel) getArguments().getSerializable(StringConstants.DATE_MODEL);
            wpUtilsModel = (WPUtilsModel) getArguments().getSerializable(StringConstants.WORK_PLAN_UTIL_MODEL);
            setupTabs();
            if(isEligible()){
                llSave.setVisibility(View.VISIBLE);
            } else {
                llSave.setVisibility(View.GONE);
            }
            ((Activity) context).setTitle("Workplan: " + wpUtilsModel.getDocName());
        }else {
            ((Activity) context).onBackPressed();
        }
        llUpload.setVisibility(View.GONE);

        return rootView;
    }

    private void setupTabs() {

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        aViewpager.setAdapter(mSectionsPagerAdapter);
        aViewpager.setOffscreenPageLimit(2);
        aViewpager.setCurrentItem(0);
        aViewpager.setPagingEnabled(true);
        tabs.setupWithViewPager(aViewpager);
        tabs.getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabIcons[1]);

    }

    @Override
    public void addProduct(String code, int type, int count, String name) {
        WPProductModel_T wpProductModelT = new WPProductModel_T();
        wpProductModelT.setName(name);
        wpProductModelT.setProductID(code);
        wpProductModelT.setType(type);
        wpProductModelT.setQuantity(count);
        if(count == 0){
            wpProductModel_tMap.remove(code);
        } else {
            wpProductModel_tMap.put(code, wpProductModelT);
        }
    }


    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(StringConstants.DATE_MODEL, dateModel);
            bundle.putSerializable(StringConstants.WORK_PLAN_UTIL_MODEL, wpUtilsModel);
            //bundle.putSerializable(StringConstants.WORK_PLAN_PAGER, wpViewPager);
            Fragment fragment = null;
            switch (position) {

                case 0:
                    fragment = new InternSampleFragment();

                    fragment.setArguments(bundle);
                    return fragment;
                case 1:
                    fragment = new InternGiftFragment();

                    fragment.setArguments(bundle);
                    return fragment;
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            switch (position) {
                case 0:
                    title = "Sample";
                    break;
                case 1:
                    title = "Gift";
                    break;
                default:
                    break;
            }

            return  title;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static void setCountText(ATextView view, int count) {
        view.setText("" + count);
    }

    @OnClick(R.id.llSave)
    public void save(){
        if(isEligible()){
            ToastUtils.shortToast(StringConstants.SAVING_MSG);
            saveWP();
        } else {
            ToastUtils.longToast("Time Out!!");
        }

    }

    @OnClick(R.id.llHome)
    public void goHome(){

        ((Activity) context).finish();
    }

    public void saveWP(){
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                r.where(WPModel.class)
                        .equalTo(WPModel.COL_DOCTOR_ID, wpUtilsModel.getDocId())
                        .equalTo(WPModel.COL_IS_MORNING, wpUtilsModel.isMorning())
                        .equalTo(WPModel.COL_DAY, dateModel.getDay())
                        .equalTo(WPModel.COL_MONTH, dateModel.getMonth())
                        .equalTo(WPModel.COL_YEAR, dateModel.getYear())
                        .findAll().deleteAllFromRealm();
                for (String key : wpProductModel_tMap.keySet()){
                    MyLog.show("","Key:"+key+" value:"+wpProductModel_tMap.get(key));
                    WPProductModel_T wpProductModelT = wpProductModel_tMap.get(key);
                    WPModel wpModel = new WPModel();
                    wpModel.setDay(dateModel.getDay());
                    wpModel.setMonth(dateModel.getMonth());
                    wpModel.setYear(dateModel.getYear());
                    wpModel.setDoctorID(wpUtilsModel.getDocId());
                    wpModel.setProductID(wpProductModelT.getProductID());
                    wpModel.setInstCode(wpUtilsModel.getDocIns());
                    wpModel.setCount(wpProductModelT.getQuantity());
                    wpModel.setName(wpProductModelT.getName());
                    wpModel.setMorning(wpUtilsModel.isMorning());
                    wpModel.setFlag(wpProductModelT.getType());
                    r.insertOrUpdate(wpModel);
                }
                WPUtils.IS_CHANGED = false;
                ((Activity) context).onBackPressed();

            }

        });
    }

    public boolean isEligible(){
        long currentMillis = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        if(cal.get(Calendar.DAY_OF_MONTH) == dateModel.getDay() && cal.get(Calendar.MONTH)+1 == dateModel.getMonth()){
            //Today 9:00 am
            cal.set(dateModel.getYear(), dateModel.getMonth() -1, dateModel.getDay(), 9, 0, 0);
            //return (currentMillis < cal.getTimeInMillis());
        } else {
            cal.set(dateModel.getYear(), dateModel.getMonth() -1, dateModel.getDay());
            //return (currentMillis < cal.getTimeInMillis());
        }
        return true;
    }

}
