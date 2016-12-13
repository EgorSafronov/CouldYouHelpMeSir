package android_project.couldyouhelpmesir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        EditText first_name = (EditText) findViewById(R.id.first_name);
        EditText second_name = (EditText) findViewById(R.id.second_name);
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //TODO: сохранить данные
        finish();
    }
}
