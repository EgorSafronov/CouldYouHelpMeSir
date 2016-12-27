package android_project.couldyouhelpmesir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    EditText first_name;
    EditText second_name;
    SharedPreferences mSettings;

    public static final String DATA_FIRST_NAME = "first_name";
    public static final String DATA_SECOND_NAME = "second_name";

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
    }

    @Override
    public void onClick(View view) {
        SharedPreferences.Editor editor = MainActivity.mSettings.edit();
        boolean flag_filled = true;
        editor.putString(DATA_FIRST_NAME, first_name.getText().toString());
        if (first_name.getText().toString().equals("")) flag_filled = false;

        editor.putString(DATA_SECOND_NAME, second_name.getText().toString());
        if (second_name.getText().toString().equals("")) flag_filled = false;

//        editor.putString("id", second_name.getText().toString());

        editor.putBoolean(MainActivity.FILLED_PERSONAL_INFORMATION, flag_filled);

        editor.commit();
        if (flag_filled) finish();
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

        } else if (id == R.id.users_requests) {
            startActivity(new Intent(this, UsersRequestsActivity.class));

        } else if (id == R.id.request_list) {
            startActivity(new Intent(this, RequestListActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
