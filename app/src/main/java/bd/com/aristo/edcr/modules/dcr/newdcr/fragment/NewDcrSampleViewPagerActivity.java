package bd.com.aristo.edcr.modules.dcr.newdcr.fragment;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;


import java.util.ArrayList;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.dcr.newdcr.ViewPagerListener;
import bd.com.aristo.edcr.utils.ui.collections.AViewPager;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by monir.sobuj on 6/5/17.
 */
public class NewDcrSampleViewPagerActivity extends AppCompatActivity implements ViewPagerListener{


    private SectionsPagerAdapter mSectionsPagerAdapter;


    @BindView(R.id.btnOk)
    ATextView btnOk;
    @BindView(R.id.wp_view_pager)
    AViewPager aViewpager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    NewDcrSampleViewPagerActivity newDcrSampleViewPager;

    private int[] tabIcons = {
            R.drawable.ic_mini_product,
            R.drawable.ic_mini_gift_color,
            R.drawable.ic_mini_star
    };
    public static List<DCRProductsModel> dcrPromoted  = new ArrayList<>();
    public static List<DCRProductsModel> dcrGifts  = new ArrayList<>();
    public static List<DCRProductsModel> dcrSample = new ArrayList<>();


    public static Intent start(Context context, List<DCRProductsModel> sampleList, List<DCRProductsModel> giftList, List<DCRProductsModel> promotedList){
        dcrGifts = giftList;
        dcrSample = sampleList;
        dcrPromoted = promotedList;
        return new Intent(context, NewDcrSampleViewPagerActivity.class);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_intern_view_pager);
        ButterKnife.bind(this);
        setTitle("New DCR");
        newDcrSampleViewPager = this;
        setupTabs();
    }


    private void setupTabs() {

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        aViewpager.setAdapter(mSectionsPagerAdapter);
        aViewpager.setOffscreenPageLimit(2);
        aViewpager.setCurrentItem(0);
        aViewpager.setPagingEnabled(true);
        tabs.setupWithViewPager(aViewpager);
        tabs.getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabIcons[1]);
        tabs.getTabAt(2).setIcon(tabIcons[2]);
    }


    @OnClick(R.id.btnOk)
    public void btnOk(){
        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            btnOk();
        }
        return true;
    }

    @Override
    public void updateDCRGiftProduct(DCRProductsModel dcrProductsModel) {
        DCRProductsModel dcrProductsModel1 = new DCRProductsModel();
        for(DCRProductsModel dcrProductsModel2:dcrGifts){
            if(dcrProductsModel.getCode().equalsIgnoreCase(dcrProductsModel2.getCode())){
                dcrProductsModel1 = dcrProductsModel2;
                break;
            }
        }
        dcrGifts.remove(dcrProductsModel1);
        dcrGifts.add(dcrProductsModel);

    }

    @Override
    public void updateDCRSelectedProduct(DCRProductsModel dcrProductsModel) {
        DCRProductsModel dcrProductsModel1 = new DCRProductsModel();
        boolean toBeRemove = false;
        for(DCRProductsModel dcrProductsModel2:dcrPromoted){
            if(dcrProductsModel.getCode().equalsIgnoreCase(dcrProductsModel2.getCode())){
                dcrProductsModel1 = dcrProductsModel2;
                toBeRemove = true;
                break;
            }
        }
        if(toBeRemove)
            dcrPromoted.remove(dcrProductsModel1);
        dcrPromoted.add(dcrProductsModel);
    }

    @Override
    public void updateDCRSampleProduct(DCRProductsModel dcrProductsModel) {
        DCRProductsModel dcrProductsModel1 = new DCRProductsModel();
        for(DCRProductsModel dcrProductsModel2:dcrSample){
            if(dcrProductsModel.getCode().equalsIgnoreCase(dcrProductsModel2.getCode())){
                dcrProductsModel1 = dcrProductsModel2;
                break;
            }
        }
        dcrSample.remove(dcrProductsModel1);
        dcrSample.add(dcrProductsModel);
    }


    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new NewSampleFragment(newDcrSampleViewPager, dcrSample);
                case 1:
                    return new NewGiftFragment(newDcrSampleViewPager, dcrGifts);
                case 2:
                    return new NewPromotedFragment(newDcrSampleViewPager, dcrPromoted);
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {

                case 0:
                    return "Sample";
                case 1:
                    return "Gift";
                case 2:
                    return "Selected";
                default:
                    return null;
            }
        }
    }

    public static void setCountText(ATextView view, int count) {
        view.setText("" + count);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
