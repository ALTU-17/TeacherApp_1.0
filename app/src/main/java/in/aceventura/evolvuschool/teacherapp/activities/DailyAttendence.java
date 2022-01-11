package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.adapter.DailyAttendanceAdapter;
import in.aceventura.evolvuschool.teacherapp.pojo.DailyAttendancePojo;

public class DailyAttendence extends AppCompatActivity {
    TableLayout tableLayout;
    private Spinner classSpinAttendance;
    private TextView pickdate, search;
    Calendar calendar;
    private ArrayList<String> listClass;
    String class_id, section_id;
    private JSONArray result;
    private SharedClass sh;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    ProgressBar progressBar;
    String clssnm;
    Button btnupdate;
    RecyclerView recyclerView;
    DailyAttendanceAdapter dailyATtendanceAdapter;
    ArrayList<DailyAttendancePojo> dailyAttendancePojoArrayList;
    ArrayList<DailyAttendancePojo> checkdArraylist;
    LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        progressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_daily_attendence);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        tableLayout = findViewById(R.id.table_layout_of_relative_detail);
        btnupdate = findViewById(R.id.btnUpdate);
        dailyAttendancePojoArrayList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getBaseContext());

       // getViews();

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkdArraylist=new ArrayList<>();
                for(int i = 0; i< dailyAttendancePojoArrayList.size(); i++){
                    DailyAttendancePojo currentStudent = new DailyAttendancePojo();
                    currentStudent = dailyAttendancePojoArrayList.get(i);
                    if(currentStudent.isCheckedStatus()){
                        checkdArraylist.add(currentStudent);
                }
            }
            String listSize = String.valueOf(checkdArraylist.size());
                Toast.makeText(DailyAttendence.this, listSize+"  Students selected", Toast.LENGTH_SHORT).show();
        }
        });
    }

    public void displayData(ArrayList<DailyAttendancePojo> relativeFriendBeansesarraylist) {
        tableLayout.setVisibility(View.VISIBLE);
        TextView th1, th2, th6, th7, th8, th9;
        CheckBox checkBox;

        RadioButton rdbtn;
        tableLayout.removeAllViews();
        TableRow tableRow = null;
        HeadingForTable();
        for (int i = 0; i < relativeFriendBeansesarraylist.size(); i++) {
            tableRow = new TableRow(getApplicationContext());

            th1 = new TextView(this);
            th2 = new TextView(this);
            checkBox = new CheckBox(this);
            rdbtn = new RadioButton(this);

            checkBox.setVisibility(View.INVISIBLE);
            th1.setTextSize(15);
            th2.setTextSize(15);
            checkBox.setTextSize(15);
            th1.setText("" + i);
            th2.setText(" " + relativeFriendBeansesarraylist.get(i).getfName() + " " + relativeFriendBeansesarraylist.get(i).getlName());

            th1.setGravity(Gravity.CENTER);
            th2.setGravity(Gravity.CENTER);
            checkBox.setGravity(Gravity.CENTER);
            tableRow.setGravity(Gravity.CENTER);
            tableLayout.setGravity(Gravity.CENTER);

            tableRow.addView(th1);
            tableRow.addView(th2);
            tableRow.addView(checkBox);
          //  tableRow.addView(rdbtn);

            /*View v = new View(this);
            v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
            v.setBackgroundColor(Color.rgb(51, 51, 51));
            tableRow.addView(v);*/
            tableRow.setBackgroundResource(R.drawable.edittextstyle);
            tableRow.setPadding(5, 10, 0, 10);
            tableRow.setId(i);
           /* tableRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int clicked_id=view.getId();

                    ArrayList<DailyAttendancePojo> listTemp;

                    listTemp = databaseAdapter.mobNoPresentRelative(mobbumber);

                    ArrayList<RelativeFriendBeans> list= new ArrayList<RelativeFriendBeans>();
                    // list = databaseAdapter.getRelativeBean(arrayList, );
                    deleteRecord(listTemp.get(clicked_id));

                    return false;
                }
            });*/

           /* tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clicked_id=v.getId();
                    ArrayList<RelativeFriendBeans> list= new ArrayList<RelativeFriendBeans>();
                    list = databaseAdapter.getAllRelative(mobbumber);
                    if(list.size()>0)
                    {
                        updateRelativeRecord(list.get(clicked_id));
                    }
                }
            });
*/
            final CheckBox finalCheckBox = checkBox;
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                  int clickedid = view.getId();

                    dailyAttendancePojoArrayList.get(clickedid).setCheckedStatus(true);
                    finalCheckBox.setChecked(true);
                    finalCheckBox.setVisibility(View.VISIBLE);
                    finalCheckBox.setGravity(Gravity.CENTER);

                }
            });



            rdbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int clickedid = view.getId();
                    dailyAttendancePojoArrayList.get(clickedid).setCheckedStatus(true);
                    finalCheckBox.setChecked(true);

                }
            });
           /* checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                    int clicked = (int) buttonView.getTag();


                    int clicked =buttonView.getId();


                    if (b){


                            dailyAttendancePojoArrayList.get(clicked).setCheckedStatus(true);



                    }

                }
            });*/
            tableLayout.addView(tableRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }


    private void HeadingForTable() {
        TextView th1, th2, th3;

        tableLayout.removeAllViews();
        TableRow tableRow = null;
        tableRow = new TableRow(getApplicationContext());
        th1 = new TextView(this);
        th2 = new TextView(this);
        th3 = new TextView(this);
        th2.setTextSize(18);
        th1.setTextSize(18);
        th3.setTextSize(18);

        th1.setText("Roll No");
        th2.setText("Student Name");
       th3.setText("Absentees");

         //th1.setBackgroundResource(R.drawable.edittextstyle);
         //th2.setBackgroundResource(R.drawable.edittextstyle);
         //th3.setBackgroundResource(R.drawable.edittextstyle);

        th2.setTextColor(ColorStateList.valueOf(Color.BLACK));
        th1.setTextColor(ColorStateList.valueOf(Color.BLACK));
        th3.setTextColor(ColorStateList.valueOf(Color.BLACK));
        /*th2.setBackgroundColor(Color.parseColor("#DDDDDD"));
        th1.setBackgroundColor(Color.parseColor("#DDDDDD"));
        th3.setBackgroundColor(Color.parseColor("#DDDDDD"));*/

        th1.setGravity(Gravity.CENTER);
        th2.setGravity(Gravity.CENTER);
        th3.setGravity(Gravity.CENTER);

        tableRow.setGravity(Gravity.CENTER);
        tableLayout.setGravity(Gravity.CENTER);

        tableRow.addView(th1);
        tableRow.addView(th2);
        tableRow.addView(th3);
        tableLayout.addView(tableRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f));
    }


}
