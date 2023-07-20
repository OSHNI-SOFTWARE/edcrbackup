package bd.com.aristo.edcr.modules.tp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DoctorsModel;
import bd.com.aristo.edcr.models.db.UserModel;
import bd.com.aristo.edcr.modules.tp.model.IPlacesModel;
import bd.com.aristo.edcr.modules.tp.model.ITPPlacesModel;
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.AnEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by monir.sobuj on 6/15/17.
 */

public class WorkPlaceActivity extends AppCompatActivity {

    private final static String TAG = "WorkPlaceActivity";
    @Inject
    Realm r;

    @BindView(R.id.rvWorkPlace)
    RecyclerView rvWorkPlace;
    @BindView(R.id.etFilterPlace)
    AnEditText etFilterText;
    @BindView(R.id.fabSave)
    FloatingActionButton fabSave;
    int month, year;

    FastItemAdapter<IPlacesModel> fastAdapter;
    List<String> places = new ArrayList<>();
    List<IPlacesModel> fastPlacesList = new ArrayList<>();
    List<IPlacesModel> fastPlacesAdapterList = new ArrayList<>();
    List<ITPPlacesModel> fastTPPlaceList = new ArrayList<>();
    boolean isMorning;


    public UserModel userModel;

    public void getUserInfo() {
        userModel = r.where(UserModel.class).findFirst();
    }

    public WorkPlaceActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workplaces);
        App.getComponent().inject(this);
        ButterKnife.bind(this);
        getUserInfo();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isMorning = getIntent().getExtras().get("shift").equals(true);
        month = getIntent().getExtras().getInt("month");
        year = getIntent().getExtras().getInt("year");
        fastTPPlaceList = (List<ITPPlacesModel>) getIntent().getSerializableExtra("placeList");
        for (ITPPlacesModel fastTPPlace : fastTPPlaceList) {
            IPlacesModel fastPlaces = new IPlacesModel();
            fastPlaces.setClicked(true);
            fastPlaces.setCode(fastTPPlace.getCode());
            fastPlaces.setId(fastTPPlace.getId());
            fastPlaces.setName(fastTPPlace.getName());
            fastPlaces.setShift(fastTPPlace.getShift());
            fastPlacesList.add(fastPlaces);
        }
        setTitle("Work Places: " + (isMorning ? StringConstants.CAPITAL_MORNING : StringConstants.CAPITAL_EVENING));
        fastAdapter = new FastItemAdapter<>();
        getPlaces();
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUtils.hideSoftKeyboard(v, getApplicationContext());
                fastTPPlaceList.clear();
                for (IPlacesModel fastPlace : fastPlacesAdapterList) {
                    if (fastPlace.isClicked()) {
                        ITPPlacesModel fastTPPlace = new ITPPlacesModel();
                        fastTPPlace.setCode(fastPlace.getCode());
                        fastTPPlace.setId(fastPlace.getId());
                        fastTPPlace.setName(fastPlace.getName());
                        fastTPPlace.setShift(fastPlace.getShift());
                        fastTPPlaceList.add(fastTPPlace);
                    }
                }
                Intent returnIntent = new Intent();
                returnIntent.putExtra("placeList", (Serializable) fastTPPlaceList);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }


    public void getPlaces() {

        List<DoctorsModel> doctorsModels = r.where(DoctorsModel.class)
                .equalTo(DoctorsModel.COL_MONTH, month)
                .equalTo(DoctorsModel.COL_YEAR, year)
                .findAll();
        if(doctorsModels != null && doctorsModels.size() > 0){
            for(DoctorsModel dm:doctorsModels){
                if(dm.getId().equals(dm.getId().toLowerCase())) {
                    if (isMorning) {
                        if (!TextUtils.isEmpty(dm.getMorningLoc())) {
                            places.add(dm.getMorningLoc());
                        }
                    } else {
                        if (!TextUtils.isEmpty(dm.getEveningLoc())) {
                            places.add(dm.getEveningLoc());
                        }
                    }
                }


            }
        }
        Set<String> placeSet = new HashSet<>(places);
        places.clear();
        places.addAll(placeSet);
        if (places != null && places.size() > 0) {
            Log.d(TAG, "getPlaces: ");
            setRecyclerView();
        } else {
            places.add("Other");
            setRecyclerView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void setRecyclerView() {
        fastPlacesAdapterList = getWorkPlacesSelectionList();
        fastAdapter.add(fastPlacesAdapterList);
        //onClick
        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<IPlacesModel>() {
            @Override
            public boolean onClick(View v, IAdapter<IPlacesModel> adapter, final IPlacesModel item, int position) {
                if (item.isClicked()) {
                    item.setClicked(false);
                } else {
                    item.setClicked(true);
                }
                fastAdapter.notifyAdapterDataSetChanged();
                return false;
            }
        });

        //fill the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvWorkPlace.setLayoutManager(layoutManager);
        rvWorkPlace.setAdapter(fastAdapter);

        fastAdapter.getItemAdapter().withFilterPredicate(new IItemAdapter.Predicate<IPlacesModel>() {
            @Override
            public boolean filter(IPlacesModel item, CharSequence constraint) {
                return !item.getName().toLowerCase().contains(constraint.toString().toLowerCase());
                //return !item.getName().startsWith(String.valueOf(constraint));
            }
        });

        etFilterText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fastAdapter.getItemAdapter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //fastAdapter.getItemAdapter().filter(s);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            SystemUtils.hideSoftKeyboard(etFilterText, getApplicationContext());
            finish();
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    public List<IPlacesModel> getWorkPlacesSelectionList() {
        List<IPlacesModel> iPlacesList = new ArrayList<>();
        int i = 0;
        for (String institute : places) {
            IPlacesModel fastPlaces = new IPlacesModel();
            fastPlaces.setId(i++);
            fastPlaces.setShift(isMorning ? StringConstants.MORNING : StringConstants.EVENING);
            fastPlaces.setCode(institute);
            fastPlaces.setName(institute);
            fastPlaces.setClicked(false);
            for (IPlacesModel fastPlace : fastPlacesList) {
                if (institute.equals(fastPlace.getCode())) {
                    fastPlaces.setClicked(true);
                }
            }
            iPlacesList.add(fastPlaces);
        }
        return iPlacesList;
    }
}