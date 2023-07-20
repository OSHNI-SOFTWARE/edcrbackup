package bd.com.aristo.edcr.modules.reports.ss;


import android.content.Context;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.reports.ss.model.SSResponse;
import bd.com.aristo.edcr.modules.reports.ss.model.SampleStatementResponse;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.networking.RequestServices;
import bd.com.aristo.edcr.utils.MyLog;
import bd.com.aristo.edcr.utils.ui.collections.AViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by altaf.sil on 1/2/18.
 */

public class SampleStatementFragment extends Fragment implements SSListener{

    public static final String TAG = "SampleStatementFragment";

    @Inject
    APIServices apiServices;
    @Inject
    Realm r;
    @Inject
    RequestServices requestServices;

    @BindView(R.id.ss_view_pager)
    AViewPager aViewpager;
    @BindView(R.id.tabs)
    TabLayout tabs;

    SelectiveProductFragment selectiveProductFragment;
    SampleProductFragment sampleProductFragment;
    GiftItemFragment giftItemFragment;
    InternItemFragment internItemFragment;
    int month, year;



    public UserModel userModel;
    public boolean isAttached = false;

    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.isAttached = true;
    }


    public void getUserInfo(){
        userModel = r.where(UserModel.class).findFirst();
    }

    private int[] tabIcons = {
            R.drawable.ic_mini_star,
            R.drawable.ic_mini_product,
            R.drawable.ic_mini_gift_color,
            R.drawable.ic_mini_dcr_new
    };


    public SampleStatementFragment() {

    }

    public static SampleStatementFragment newInstance() {
        return new SampleStatementFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initiate dependencies
        App.getComponent().inject(this);
        View rootView = inflater.inflate(R.layout.fragment_sample_statement, container, false);
        ButterKnife.bind(this, rootView);
        getUserInfo();
        setHasOptionsMenu(true);
        if(getArguments() != null) {
            month = getArguments().getInt("month");
            year = getArguments().getInt("year");
            selectiveProductFragment = SelectiveProductFragment.newInstance(month, year);
            sampleProductFragment = SampleProductFragment.newInstance(month, year);
            giftItemFragment = GiftItemFragment.newInstance(month, year);
            internItemFragment = InternItemFragment.newInstance(month, year);
            requestServices.getDetailSampleStatement(context, apiServices, userModel.getUserId(), month, year, this);
            //getGifts();
        } else {
            ((AppCompatActivity) context).onBackPressed();
        }


        return rootView;
    }



    private void setupTabs() {
        if(isAttached) {
            SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
            aViewpager.setAdapter(mSectionsPagerAdapter);
            aViewpager.setOffscreenPageLimit(2);
            aViewpager.setCurrentItem(0);
            aViewpager.setPagingEnabled(true);
            tabs.setupWithViewPager(aViewpager);
            tabs.getTabAt(0).setIcon(tabIcons[0]);
            tabs.getTabAt(1).setIcon(tabIcons[1]);
            tabs.getTabAt(2).setIcon(tabIcons[2]);
            tabs.getTabAt(3).setIcon(tabIcons[3]);
        } else {
            MyLog.show("SampleStatement", "Fragment not attached yet!");
        }
    }

    @Override
    public void getSS(SSResponse ssResponse) {
        setupTabs();
    }

    @Override
    public void getSamples(SampleStatementResponse ssResponse) {
        setupTabs();
    }

    @Override
    public void onError() {
        setupTabs();
    }


    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return selectiveProductFragment;
                case 1:
                    return sampleProductFragment;
                case 2:
                    return giftItemFragment;
                case 3:
                    return internItemFragment;
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            switch (position) {
                case 0:
                    title = "Selected";
                    break;
                case 1:
                    title = "Sample";
                    break;
                case 2:
                    title = "Gift";
                    break;
                case 3:
                    title = "Intern S&G";
                    break;
                default:
                    break;
            }

            return  title;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ss,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
