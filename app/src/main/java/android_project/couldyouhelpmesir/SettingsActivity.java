package android_project.couldyouhelpmesir;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    EditText first_name;
    EditText second_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        first_name = (EditText) findViewById(R.id.first_name);
        second_name = (EditText) findViewById(R.id.second_name);
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        SharedPreferences.Editor editor = MainActivity.mSettings.edit();
        editor.putString("first_name", first_name.getText().toString());
        editor.putString("second_name", second_name.getText().toString());
        editor.putString("id", second_name.getText().toString());
        editor.commit();
        finish();
    }
}
