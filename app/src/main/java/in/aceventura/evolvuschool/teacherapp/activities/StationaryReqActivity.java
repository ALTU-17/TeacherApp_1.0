package in.aceventura.evolvuschool.teacherapp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import in.aceventura.evolvuschool.teacherapp.R;


public class StationaryReqActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stationary_req);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
