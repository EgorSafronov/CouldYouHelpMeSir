package android_project.couldyouhelpmesir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    Button template1;
    Button sendRequest;
    DatabaseReference mDatabase;
    EditText problem;
    boolean inDanger = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        template1 = (Button) findViewById(R.id.main_button_1_1);
        sendRequest = (Button) findViewById(R.id.send_request);
        template1.setOnClickListener(this);
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
        String first_name = mSettings.getString("first_name", "");
        String second_name = mSettings.getString("second_name", "");
        String userId = mSettings.getString("id", "");//TODO: генерировать ID
        if (view == template1) {
            mDatabase.child(userId).setValue(new Request(first_name, second_name, "УБИВАЮТ ****"));
            inDanger = true;
        }
        else if (view == sendRequest){
            mDatabase.child(userId).setValue(new Request(first_name, second_name, problem.getText().toString()));
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

        } else if (id == R.id.users_requests) {
            startActivity(new Intent(this, UsersRequestsActivity.class));

        } else if (id == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
