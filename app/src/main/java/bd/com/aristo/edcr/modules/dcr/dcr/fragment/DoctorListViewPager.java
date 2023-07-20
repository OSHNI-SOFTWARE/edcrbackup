package bd.com.aristo.edcr.modules.dcr.dcr.fragment;

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

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.networking.RequestServices;
import bd.com.aristo.edcr.utils.SharedPrefsUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.collections.AViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 6/5/17.
 */

public class DoctorListViewPager extends Fragment {

    private static final String TAG = "DCRViewPager";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    @BindView(R.id.dcr_view_pager)
    AViewPager aViewpager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    Context context;

    @Inject
    Realm r;
    @Inject
    APIServices apiServices;
    @Inject
    RequestServices requestServices;
    UserModel userModel;
    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static String shift;


    private int[] tabIcons = {
            R.drawable.ic_planned,
            R.drawable.ic_unplanned,
    };


    public DoctorListViewPager() {

    }

    public static DoctorListViewPager newInstance() {
        return new DoctorListViewPager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initiate dependencies
        App.getComponent().inject(this);
        shift = getArguments().getString("dcr_shift");
        View rootView = inflater.inflate(R.layout.fragment_dcr_doctor_view_pager, container, false);
        ButterKnife.bind(this, rootView);

        ((Activity) context).setTitle("Send DCR: "+ (shift.equalsIgnoreCase(StringConstants.MORNING)? StringConstants.CAPITAL_MORNING: StringConstants.CAPITAL_EVENING));

        getUserInfo();
        return rootView;
    }

    private void setupTabs() {

        mSectionsPagerAdapter = new DoctorListViewPager.SectionsPagerAdapter(getChildFragmentManager());

        aViewpager.setAdapter(mSectionsPagerAdapter);
        aViewpager.setOffscreenPageLimit(1);
        aViewpager.setCurrentItem(0);
        aViewpager.setPagingEnabled(true);
        tabs.setupWithViewPager(aViewpager);
        tabs.getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabIcons[1]);

    }



    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new DoctorListFragment();
                case 1:
                    return new DoctorListUnplanFragment();
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

                    title = "Planned";
                    break;

                case 1:
                    title = "Unplanned";
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
        if(!SharedPrefsUtils.getBooleanPreference(getContext(), StringConstants.IS_PRODUCT_SYNCED, false)){
            requestServices.getProductAfterDCR(context, apiServices, userModel.getUserId(), r);
        }
        setupTabs();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

}
