package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.adapter.TimetableAdapter;
import in.aceventura.evolvuschool.teacherapp.pojo.TeacherimetablePojo;

public class TimeTableActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView txtDay;
    Context context;
    private String day;
    private Button btnMon,btnTues,btnWed,btnThu,btnFri,btnSat;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    RecyclerView recyclerView;
    TimetableAdapter timetableAdapter;
    ArrayList<TeacherimetablePojo> teacherimetablePojoArrayList;
    TeacherimetablePojo teacherimetablePojo;
    LinearLayoutManager linearLayoutManager;
    LinearLayout lay1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        context=this;
        getId();
        btnMon.setOnClickListener(this);
        btnTues.setOnClickListener(this);
        btnWed.setOnClickListener(this);
        btnThu.setOnClickListener(this);
        btnFri.setOnClickListener(this);
        btnSat.setOnClickListener(this);
       /* linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        teacherimetablePojoArrayList=new ArrayList<>();
        timetableAdapter=new TimetableAdapter(context,teacherimetablePojoArrayList);
        recyclerView.setAdapter(timetableAdapter);*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getId() {
        txtDay = findViewById(R.id.slectDay);
        btnMon = findViewById(R.id.mon);
        btnTues = findViewById(R.id.tues);
        btnWed = findViewById(R.id.wed);
        btnThu = findViewById(R.id.thur);
        btnFri = findViewById(R.id.fri);
        btnSat = findViewById(R.id.sat);
        lay1=findViewById(R.id.daylayout1);
//recyclerView=findViewById(R.id.recyclerviewtimetable);
        /*txtDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context,txtDay);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.dayselection, popup.getMenu());

                //registering popup with OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.monday:
                                txtDay.setText("Monday");

                                break;

                            case R.id.tuesday:
                                txtDay.setText("Tuesday");
                                break;


                            case R.id.wednesday:
                                txtDay.setText("Wednesday");
                                break;

                            case R.id.thursday:
                                txtDay.setText("Thursday");
                                break;

                            case R.id.friday:
                                txtDay.setText("Friday");
                                break;

                            case R.id.saturday:
                                txtDay.setText("Saturday");
                                break;


                        }
                        return false;
                    }
                });

*//*
                popup.setGravity(Gravity.CENTER);
*//*

                popup.show();
            }
        });*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mon:
                lay1.setVisibility(View.VISIBLE);
                txtDay.setText("Monday");
                txtDay.setBackgroundResource(R.color.monpink);
                getTimetable("Monday");
                break;

            case R.id.tues:
                lay1.setVisibility(View.VISIBLE);

                txtDay.setText("Tuesday");
                txtDay.setBackgroundResource(R.color.tues);
                getTimetable("Tuesday");

                break;
            case R.id.wed:
                lay1.setVisibility(View.VISIBLE);

                txtDay.setText("Wednesday");
                txtDay.setBackgroundResource(R.color.wed);
                getTimetable("Wednesday");

                break;
            case R.id.thur:
                lay1.setVisibility(View.VISIBLE);

                txtDay.setText("Thursday");
                txtDay.setBackgroundResource(R.color.thu);
                getTimetable("Thursday");

                break;
            case R.id.fri:
                lay1.setVisibility(View.VISIBLE);

                txtDay.setText("Friday");
                txtDay.setBackgroundResource(R.color.fri);
                getTimetable("Friday");

                break;
            case R.id.sat:
                lay1.setVisibility(View.VISIBLE);

                txtDay.setText("Saturday");
                txtDay.setBackgroundResource(R.color.sat);
                getTimetable("Saturday");

                break;
        }
    }

    private void getTimetable(String day) {
        String selectedDay=day;

    }
}
