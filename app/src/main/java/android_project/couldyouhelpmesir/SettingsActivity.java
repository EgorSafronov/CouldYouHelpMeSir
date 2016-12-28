package android_project.couldyouhelpmesir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    EditText first_name;
    EditText second_name;
    EditText email;
    EditText phone_number;
    Spinner spinner_country;
    Spinner spinner_city;
    ScrollView scrollView;
    SharedPreferences mSettings;
    Integer Country = 0;
    Integer City = 0;

    public static String[] Countries = {"", "Россия", "Франция", "Испания", "Германия"};
    public static String[][] Cities = {{}, {"Москва", "Санкт-Петербург", "Новосибирск", "Челябинск"}, {"Париж", "Лион", "Ницца", "Бордо"}, {"Мадрид", "Барселона", "Севилья", "Малага"}, {"Берлин", "Гамбург", "Мюнхен", "Дортмунд"}};

    public static final String DATA_FIRST_NAME = "first_name";
    public static final String DATA_SECOND_NAME = "second_name";
    public static final String DATA_EMAIL = "email";
    public static final String DATA_PHONE_NUMBER = "phone_number";
    public static final String DATA_COUNTRY= "country";
    public static final String DATA_CITY = "city";

    public android.support.v7.widget.Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mSettings = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        first_name = (EditText) findViewById(R.id.first_name);
        first_name.setText(mSettings.getString(DATA_FIRST_NAME, ""));

        second_name = (EditText) findViewById(R.id.second_name);
        second_name.setText(mSettings.getString(DATA_SECOND_NAME, ""));

        email = (EditText) findViewById(R.id.email);
        email.setText(mSettings.getString(DATA_EMAIL, ""));

        phone_number = (EditText) findViewById(R.id.phone_number);
        phone_number.setText(mSettings.getString(DATA_PHONE_NUMBER, ""));

        ArrayAdapter<String> adapter_coutries = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Countries);
        adapter_coutries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinner_country.setAdapter(adapter_coutries);
        spinner_country.setSelection(mSettings.getInt(DATA_COUNTRY, 0));


        spinner_city = (Spinner) findViewById(R.id.spinner_city);
 //       spinner_city.setOnItemClickListener(this);
        spinner_city.setVisibility(View.GONE);

        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
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
        scrollView = (ScrollView) findViewById(R.id.settings_scroll_view);


        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_city.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adapter_cities = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, Cities[position]);
                adapter_cities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_city.setAdapter(adapter_cities);
                Country = position;
                spinner_city.setSelection(mSettings.getInt(DATA_CITY, 0));
                spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        City = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onClick(View view) {
        if (MainActivity.inDanger) {
            Integer delCountry = mSettings.getInt(DATA_COUNTRY, 0);
            Integer delCity = mSettings.getInt(DATA_CITY, 0);
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
            String userID = telephonyManager.getDeviceId();
            if (userID == null) {
                userID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            FirebaseDatabase.getInstance().getReference().child(Integer.toString(delCountry)).child(Integer.toString(delCity)).child(userID).removeValue();
            MainActivity.inDanger = false;
        }
        SharedPreferences.Editor editor = MainActivity.mSettings.edit();
        boolean flag_filled = true;
        editor.putString(DATA_FIRST_NAME, first_name.getText().toString());
        if (first_name.getText().toString().equals("")) flag_filled = false;

        editor.putString(DATA_SECOND_NAME, second_name.getText().toString());
        if (second_name.getText().toString().equals("")) flag_filled = false;

        editor.putString(DATA_EMAIL, email.getText().toString());
        if (email.getText().toString().equals("")) flag_filled = false;

        editor.putString(DATA_PHONE_NUMBER, phone_number.getText().toString());
        if (phone_number.getText().toString().equals("")) flag_filled = false;
//        editor.putString("id", second_name.getText().toString());

        editor.putBoolean(MainActivity.FILLED_PERSONAL_INFORMATION, flag_filled);

        editor.putInt(DATA_COUNTRY, Country);
        editor.putInt(DATA_CITY, City);


        editor.commit();
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

        if (id == R.id.settings) {
            //Остаемся
        } else if (id == R.id.new_request) {
            startActivity(new Intent(this, MainActivity.class));

        } else if (id == R.id.request_list) {
            startActivity(new Intent(this, RequestListActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
