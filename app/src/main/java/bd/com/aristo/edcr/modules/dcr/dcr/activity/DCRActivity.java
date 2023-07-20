package bd.com.aristo.edcr.modules.dcr.dcr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.dcr.accompany.AccompanyActivity;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.DCRColorInfoDialog;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.DoctorListViewPager;
import bd.com.aristo.edcr.modules.dcr.newdcr.fragment.NewDCRListFragment;
import bd.com.aristo.edcr.utils.constants.StringConstants;

public class DCRActivity extends AppCompatActivity {
    Activity activity;
    int type;

    Fragment fragment ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dcr);
        App.getComponent().inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity = this;
        Bundle args;

        type = getIntent().getIntExtra("TYPE", 1);
        switch (type){
            case 0: //new dcr
                fragment = new NewDCRListFragment();
                break;
            case 1: // morning
                fragment = new DoctorListViewPager();
                args = new Bundle();

                args.putString("dcr_shift", StringConstants.MORNING);
                fragment.setArguments(args);
                break;
            case 2: // evening
                fragment = new DoctorListViewPager();
                args = new Bundle();
                args.putString("dcr_shift",StringConstants.EVENING);
                fragment.setArguments(args);
                break;
                default:
                    fragment = new NewDCRListFragment();
                    break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("main_fragment").commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if(id == R.id.action_dcr_info){
            DialogFragment dialogFragment = new DCRColorInfoDialog();
            dialogFragment.show(getSupportFragmentManager(), "dcr_color_info");
        }
        if (id == R.id.action_add_accom) {
            //Add accompany option menu
            startActivity(new Intent(this, AccompanyActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dcr, menu);
        return true;
    }
}
