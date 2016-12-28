package android_project.couldyouhelpmesir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static SharedPreferences mSettings;
    private static final String TAG = "MainActivity";

    public static final String APP_PREFERENCES = "helpsettings";
    public static final String FILLED_PERSONAL_INFORMATION = "filledinfo";

    public android.support.v7.widget.Toolbar toolbar;

    Button templates[] = new Button[6];
    Button sendRequest;
    DatabaseReference mDatabase;
    EditText problem;
    boolean inDanger = false;
    boolean button_touched = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck == -1) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
        }


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        boolean flag_filled_info = false;
        if (mSettings.contains(FILLED_PERSONAL_INFORMATION)) {
            flag_filled_info = mSettings.getBoolean(FILLED_PERSONAL_INFORMATION, false);
        }
        if (!flag_filled_info) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        templates[0] = (Button) findViewById(R.id.main_button_1_1);
        templates[1] = (Button) findViewById(R.id.main_button_1_2);
        templates[2] = (Button) findViewById(R.id.main_button_1_3);
        templates[3] = (Button) findViewById(R.id.main_button_2_1);
        templates[4] = (Button) findViewById(R.id.main_button_2_2);
        templates[5] = (Button) findViewById(R.id.main_button_2_3);
        sendRequest = (Button) findViewById(R.id.send_request);
        for (int i = 0; i < 6; i++) {
            templates[i].setOnClickListener(this);
        }
        sendRequest.setOnClickListener(this);
        problem = (EditText) findViewById(R.id.help_text);
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
    public void onClick(View view) {
        if (inDanger) {
            return;
        }

        int request_type = -1;
        String first_name = mSettings.getString(SettingsActivity.DATA_FIRST_NAME, "");
        String second_name = mSettings.getString(SettingsActivity.DATA_SECOND_NAME, "");
        String email = mSettings.getString(SettingsActivity.DATA_EMAIL, "");
        String phone_number = mSettings.getString(SettingsActivity.DATA_PHONE_NUMBER, "");
//        String userID = mSettings.getString("id", "");

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String userID = telephonyManager.getDeviceId();

        for (int i = 0; i < 6; i++) {
            if (view == templates[i]) {
                if (!button_touched) {
                    request_type = i;
                    button_touched = true;
                    for (int j = 0; j < 6; j++) {
                        if (j != i) templates[j].setAlpha((float) 0.3);

                    }
                } else {
                    button_touched = false;
                    request_type = -1;
                    for (int j = 0; j < 6; j++) {
                        if (j != i) templates[j].setAlpha(1);
                    }
                }
//                templates[i].setVisibility(View.INVISIBLE);
            }
        }
        if (view == sendRequest){
            mDatabase.child(userID).setValue(new Request(first_name, second_name, email, phone_number,
                    problem.getText().toString(), request_type));
            inDanger = true;
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.new_request) {
            //Остаемся
        } else if (id == R.id.request_list) {
            startActivity(new Intent(this, RequestListActivity.class));

        } else if (id == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
