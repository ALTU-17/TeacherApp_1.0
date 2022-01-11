package in.aceventura.evolvuschool.teacherapp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import in.aceventura.evolvuschool.teacherapp.R;


public class CreateBookReqACtivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_book_req_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
