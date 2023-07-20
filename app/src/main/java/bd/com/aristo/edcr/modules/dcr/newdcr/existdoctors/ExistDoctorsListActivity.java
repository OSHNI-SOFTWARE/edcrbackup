package bd.com.aristo.edcr.modules.dcr.newdcr.existdoctors;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.Doctors;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.modules.dcr.dcr.DCRUtils;
import bd.com.aristo.edcr.utils.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;


public class ExistDoctorsListActivity extends AppCompatActivity implements DoctorAdapterListener {

    private ExistDoctorsListActivity activity = this;

    private static final String TAG = ExistDoctorsListActivity.class.getSimpleName();


    List<DoctorsModel> doctorsModelsList;
    List<Doctors> doctorsList;

    ExistDoctorsAdapter existDoctorsAdapter;
    static String shift;


    @Inject
    Realm r;

    DateModel dateModel;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private SearchView searchView;

    public static void start(Activity context){
        Intent intent = new Intent(context,ExistDoctorsListActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exist_doctors_list);

        initialize();
    }

    public void initialize(){
        //initiate dependencies
        App.getComponent().inject(this);
        ButterKnife.bind(this);
        dateModel = DCRUtils.getToday();
        doctorsModelsList = r.where(DoctorsModel.class)
                .equalTo(DoctorsModel.COL_YEAR, dateModel.getYear())
                .equalTo(DoctorsModel.COL_MONTH, dateModel.getMonth())
                .distinct(DoctorsModel.COL_ID)
                .sort(DoctorsModel.COL_NAME)
                .findAll();

        doctorsList = new ArrayList<>();
        long id = 0;
        for (DoctorsModel doctorsModel:doctorsModelsList){
            if(doctorsModel.getId().equals(doctorsModel.getId().toLowerCase())) {
                Doctors doctors = new Doctors();
                doctors.setId(id++);
                doctors.setDId(doctorsModel.getId());
                doctors.setName(doctorsModel.getName());
                doctors.setDegree(doctorsModel.getDegree());
                doctors.setSpecial(doctorsModel.getSpecial());
                doctors.setAddr(StringUtils.getAndFormAmp(doctorsModel.getAddress()));
                doctorsList.add(doctors);
            }
        }

        existDoctorsAdapter = new ExistDoctorsAdapter(this, doctorsList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(existDoctorsAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu_searchview, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                existDoctorsAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                existDoctorsAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onDoctorSelected(Doctors doctors) {
        EventBus.getDefault().post(doctors);
        finish();
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}
