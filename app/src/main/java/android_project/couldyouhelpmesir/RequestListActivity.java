package android_project.couldyouhelpmesir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egorsafronov on 00.12.2016
 */

public class RequestListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public android.support.v7.widget.Toolbar toolbar;

    public static SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "helpsettings";

    private RecyclerView recyclerView;

    private List<Request> requests = new ArrayList<Request>();

    @Nullable
    private RequestsRecyclerAdapter adapter;

    DatabaseReference mDatabase;

    String userID;



    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            for (DataSnapshot snap : snapshot.getChildren()) {
                Request cur = snap.getValue(Request.class);
                if (cur != null) {
                //if ((cur != null) && (cur.ID != userID)) {
                    requests.add(cur);
                }
            }
            displayNonEmptyData();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.wtf("kapa", "start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        Integer country = mSettings.getInt(SettingsActivity.DATA_COUNTRY, 0);
        Integer city = mSettings.getInt(SettingsActivity.DATA_CITY, 0);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        //errorTextView = (TextView) findViewById(R.id.error_text);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Integer.toString(country)).child(Integer.toString(city));

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        userID = telephonyManager.getDeviceId();
        if (userID == null) {
            userID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(getResources().getColor(R.color.colorPrimaryDark)));

        loadData();

    }

    private void loadData() {
        requests.clear();
        new Thread(new Runnable() {
            public void run() {
                mDatabase.addListenerForSingleValueEvent(listener);
            }
        }).start();
    }


    private void displayNonEmptyData() {
        if (adapter == null) {
            adapter = new RequestsRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        adapter.setRequests(requests);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.request_list) {
            //Остаемся
        } else if (id == R.id.new_request) {
            startActivity(new Intent(this, MainActivity.class));

        } else if (id == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
