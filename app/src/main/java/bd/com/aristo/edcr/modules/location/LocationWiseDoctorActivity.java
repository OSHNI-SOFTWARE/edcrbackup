package bd.com.aristo.edcr.modules.location;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.ui.collections.AViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class LocationWiseDoctorActivity extends AppCompatActivity {

    private final String TAG = LocationWiseDoctorActivity.class.getSimpleName();

    private LocationWiseDoctorActivity activity = this;

    @Inject
    Realm r;
    @BindView(R.id.tp_view_pager)
    AViewPager aViewpager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    static DateModel nextDateModel;
    List<String> morningLocation  = new ArrayList<>();
    List<String> eveningLocation  = new ArrayList<>();
    int morningCount = 0, eveningCount = 0, noLocCount = 0;

    public static void start(Activity context, DateModel dateModel){
        nextDateModel = dateModel;
        Intent intent = new Intent(context, LocationWiseDoctorActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().inject(this);
        setContentView(R.layout.activity_location_wise_doctor);
        ButterKnife.bind(this);
        getDoctor();
        setTitle(getString(R.string.location_wise_doctors, DateTimeUtils.MONTH_NAME[nextDateModel.getMonth() - 1]));
        setupTabs();
    }


    public void getDoctor(){
        List<DoctorsModel> doctorsModels = r.where(DoctorsModel.class)
                .equalTo(DoctorsModel.COL_MONTH, nextDateModel.getMonth())
                .equalTo(DoctorsModel.COL_YEAR, nextDateModel.getYear())
                .findAll();
        if(doctorsModels != null && doctorsModels.size() > 0){
            for(DoctorsModel dm:doctorsModels){
                if(dm.getId().equals(dm.getId().toLowerCase())) {
                    if (!TextUtils.isEmpty(dm.getMorningLoc())) {
                        morningLocation.add(dm.getMorningLoc());
                        morningCount++;
                    }
                    if (!TextUtils.isEmpty(dm.getEveningLoc())) {
                        eveningLocation.add(dm.getEveningLoc());
                        eveningCount++;
                    }
                    if(TextUtils.isEmpty(dm.getMorningLoc()) && TextUtils.isEmpty(dm.getEveningLoc())){
                        noLocCount++;
                    }
                }
            }
        }
        Set<String> locSet = new HashSet<>(morningLocation);
        morningLocation.clear();
        morningLocation.addAll(locSet);
        Set<String> locSetE = new HashSet<>(eveningLocation);
        eveningLocation.clear();
        eveningLocation.addAll(locSetE);

    }
    @Override
    protected void onResume() {
        super.onResume();
    }


    private void setupTabs() {

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),activity);

        aViewpager.setAdapter(mSectionsPagerAdapter);
        aViewpager.setPagingEnabled(true);
        tabs.setupWithViewPager(aViewpager);
        for (int i = 0; i < tabs.getTabCount(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            tab.setCustomView(mSectionsPagerAdapter.getTabView(i));
        }
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();

    }






    //Pager adapter

    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private String tabTitles[] = new String[] { "Morning", "Evening", "No Loc"};
        private int[] imageResId = { R.drawable.ic_mini_morning_inverted, R.drawable.ic_mini_evening_inverted, R.drawable.ic_mini_evening_inverted };
        Context context;
        SectionsPagerAdapter(FragmentManager fm,Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MorningLocationFragment(morningLocation, nextDateModel);
                case 1:
                    return new EveningLocationFragment(eveningLocation, nextDateModel);
                case 2:
                    return new NoLocationFragment(nextDateModel);
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        public View getTabView(int position) {
            View v = LayoutInflater.from(context).inflate(R.layout.tp_custom_tab, null);
            TextView tv = (TextView) v.findViewById(R.id.textViewTabTitle);
            String title = "";
            switch (position){
                case 0:
                    title = tabTitles[position]+"("+morningCount+")";
                    break;
                case 1:
                    title = tabTitles[position]+"("+eveningCount+")";
                    break;
                case 2:
                    title = tabTitles[position]+"("+noLocCount+")";
                    break;
            }
            tv.setText(title);
            ImageView img = (ImageView) v.findViewById(R.id.imageViewTab);
            img.setImageResource(imageResId[position]);
            img.setVisibility(View.GONE);
            return v;
        }
    }

}
