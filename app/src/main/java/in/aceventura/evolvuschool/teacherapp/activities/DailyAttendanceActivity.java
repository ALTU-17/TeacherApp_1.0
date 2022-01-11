package in.aceventura.evolvuschool.teacherapp.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.aceventura.evolvuschool.teacherapp.SystemTime;
import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.adapter.DailyAttendanceAdapter;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.DailyAttendanceLeftCheckedPojo;
import in.aceventura.evolvuschool.teacherapp.pojo.DailyAttendancePojo;

public class DailyAttendanceActivity extends AppCompatActivity {
    public static String clssnm = "";
    Calendar calendar;
    String class_id, section_id;
    TextView count;
    String datesystem;
    String date;
    String strDate;
    String dateAttendance;
    Button btnDelete;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    RelativeLayout linearLayout;
    String name, newUrl, dUrl;
    DatabaseHelper mDatabaseHelper;

    //This adapter is for if that date attendance is already marked
    DailyAttendanceAdapter dailyATtendanceAdapter;

    ArrayList<DailyAttendancePojo> dailyAttendancePojoArrayList;
    ArrayList<DailyAttendancePojo> dailyAttendancePojoArrayListnew;
    ArrayList<DailyAttendancePojo> dailyAttendancePojoArrayListnew2;
    ArrayList<DailyAttendanceLeftCheckedPojo> checkedArraylist;
    LinearLayoutManager manager;
    LinearLayout linearLayoutattendancestudent;
    Date date1 = null;
    private Spinner classSpinAttendance;
    private TextView pickdate, search;
    final DatePickerDialog.OnDateSetListener startDate1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel2();
        }
    };
    private ArrayList<String> listClass;
    private JSONArray result;
    private ProgressDialog progressDialog;
    private ArrayList<String> selectedCheckboxes;
    private View.OnTouchListener classSpinnerOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                //Your code
                listClass.clear();
                getClasses();
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_attendance);
        getIds();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();
//Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_homeworkdetails);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        // tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        tv_academic_yr.setText("("+SharedClass.getInstance(getApplicationContext()).getAcademicYear()+")");

        school_title.setText(" Evolvu Teacher App");
        ht_Teachernote.setText("Daily Attendance");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        calendar = Calendar.getInstance();
        checkedArraylist = new ArrayList<>();
        result = new JSONArray();

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        listClass = new ArrayList<>();
        dailyAttendancePojoArrayList = new ArrayList<>();
        dailyAttendancePojoArrayListnew = new ArrayList<>();
        dailyAttendancePojoArrayListnew2 = new ArrayList<>();
        btnDelete.setVisibility(View.GONE);

        try {
            dailyATtendanceAdapter = new DailyAttendanceAdapter(this, dailyAttendancePojoArrayList, selectedCheckboxes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            classSpinAttendance.setOnTouchListener(classSpinnerOnTouch);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            classSpinAttendance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {   //String option;

                    class_id = getClassId(position);
                    section_id = getSectionId(position);
                } // to close the onItemSelected

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        String logoUrl;
        if (name.equals("SACS")) {
            logoUrl = dUrl + "uploads/logo.png";
        } else {
            logoUrl = dUrl + "uploads/" + name + "/logo.png";
        }
        Log.e("LogoUrl", "Values:" + logoUrl);
        Glide.with(this).load(logoUrl)
                .thumbnail(0.5f)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {


                        if (name != null) {
                            switch (name) {
                                case "SACS":
                                    drawer.setBackgroundResource(R.drawable.newarnolds_logo);
                                    break;
                                case "SFSNE":
                                    drawer.setBackgroundResource(R.drawable.transparentsfsne);
                                    break;
                                case "SFSNW":
                                    drawer.setBackgroundResource(R.drawable.transparentsfsnw);
                                    break;
                                case "SJSKW":
                                    drawer.setBackgroundResource(R.drawable.sjskw);
                                    break;
                                case "SFSPUNE":
                                    drawer.setBackgroundResource(R.drawable.sfspune);
                                    break;
                            }
                        } else {
                            drawer.setBackgroundResource(R.drawable.evolvuteacer);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //on load success
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(drawer);


        try {
            BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
            //
            View view = findViewById(R.id.bb_bookavailability);
            TextView supportEmail = view.findViewById(R.id.email);
            //---------------Support email--------------------------
            if (name != null) {
                String supportname = name.toLowerCase();

                supportEmail.setText("For app support email to : " + "support" + supportname + "@aceventura.in");
            } else {
                supportEmail.setText("For app support email to : " + "aceventuraservices@gmail.com");
                return;
            }
            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(DailyAttendanceActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(DailyAttendanceActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(DailyAttendanceActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(DailyAttendanceActivity.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        DailyAttendanceActivity.this.finish();
    }

    private void getIds() {
        classSpinAttendance = findViewById(R.id.classSpinAttendance);
        pickdate = findViewById(R.id.pickdate);
        linearLayoutattendancestudent = findViewById(R.id.studentattendancelayout);
        count = findViewById(R.id.count);
        linearLayout = findViewById(R.id.linear);
        recyclerView = findViewById(R.id.recycl);
        Button btnUpdate = findViewById(R.id.updateAtteBtn);
        btnDelete = findViewById(R.id.deleteAtteBtn);
        search = findViewById(R.id.searchbtnattendance);
        pickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickdate.setFocusable(false);

                //Get yesterday's date
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 0);

                //Set yesterday time milliseconds as date pickers minimum date
                DatePickerDialog datePickerDialog = new DatePickerDialog(DailyAttendanceActivity.this, startDate1,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                if (Build.VERSION.SDK_INT < 22) {

                    datePickerDialog.getDatePicker().setCalendarViewShown(true);
                    datePickerDialog.getDatePicker().setSpinnersShown(false);
                    datePickerDialog.getDatePicker().getCalendarView().setShowWeekNumber(false);

                }

                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DailyAttendanceActivity.this);
                {
                    builder.setCancelable(true);
                    builder.setTitle("Alert");
                    builder.setMessage(
                            "Are you sure to save this data? SMS will be sent immediate??");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        dialog.dismiss();
                        if (validate1()) {

                            updateAttendance();

                        } else {
                            Toast.makeText(DailyAttendanceActivity.this, "Enter Mandatory Fields", Toast.LENGTH_SHORT).show();
                        }

                    });
                    builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                    builder.create().show();
                }
            }
        });

        //delete attendance btn
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DailyAttendanceActivity.this);
                {
                    builder.setCancelable(true);
                    builder.setTitle("Alert");
                    builder.setMessage(
                            "Are you sure to delete this data?");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        dialog.dismiss();
                        if (validate1()) {
                            deleteAttendance();
                        } else {
                            Toast.makeText(DailyAttendanceActivity.this, "Enter Mandatory Fields", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                    builder.create().show();
                }
            }
        });


        search.setOnClickListener(view -> {
            if (validate1()) {
                dailyAttendancePojoArrayList.clear();
                dailyAttendancePojoArrayListnew.clear();
                get_roll_num_count();
                getStuds();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(DailyAttendanceActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Alert");
                builder.setMessage("Fill All * Field ");
                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                builder.create().show();
            }
        });
    }

    //delete attendance
    private void deleteAttendance() {
        Toast.makeText(DailyAttendanceActivity.this, "Deleting Attendance", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        datesystem = SystemTime.getSystemDate();
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        final String teacherid = (SharedClass.getInstance(this).getRegId().toString());

        JSONObject mainRequestObject = new JSONObject();

        JSONArray requestArray = new JSONArray();
        JSONArray requestArray1 = new JSONArray();

        final String dateAttendance = pickdate.getText().toString();

        if (dailyAttendancePojoArrayList.size() == 0) {
            for (int i = 0; i < dailyAttendancePojoArrayListnew.size(); i++) {
                DailyAttendancePojo attendancePojo;
                attendancePojo = dailyAttendancePojoArrayListnew.get(i);
                if (attendancePojo.isCheckedStatus()) {
                    JSONObject jsonObject1 = new JSONObject();
                    try {
                        jsonObject1.put("student_id", attendancePojo.getStudentid());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    requestArray1.put(jsonObject1);
                }

                JSONObject jsonObject = new JSONObject();

                if (attendancePojo.isCheckedStatus() && attendancePojo.isCheckedLeftStatus()) {

                    try {
                        jsonObject.put("attendance_status", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        jsonObject.put("attendance_status", "0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    jsonObject.put("fName", attendancePojo.getfName());
                    jsonObject.put("lName", attendancePojo.getlName());
                    if (attendancePojo.getRollno().equals(" ")) {
                        jsonObject.put("rollno", "");
                    } else {
                        jsonObject.put("rollno", attendancePojo.getRollno());
                    }
                    jsonObject.put("student_id", attendancePojo.getStudentid());
                    jsonObject.put("classid", class_id);
                    jsonObject.put("sectionid", section_id);
                    jsonObject.put("date", datesystem);

                    try {
                        //create SimpleDateFormat object with source string date format
                        SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yyyy");

                        //parse the string into Date object
                        Date date = sdfSource.parse(dateAttendance);

                        //create SimpleDateFormat object with desired date format
                        SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd");

                        //parse the date into another format
                        String strDate = sdfDestination.format(date);
                        jsonObject.put("only_date", strDate);
                    } catch (ParseException pe) {
                        System.out.println("Parse Exception : " + pe);
                    }
                    jsonObject.put("teacher_id", teacherid);
                    jsonObject.put("academic_yr", academic_yr);
//                    jsonObject.put("short_name", name);

                    System.out.println("First - " + jsonObject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                requestArray.put(jsonObject);
//                requestArray.put(name);
            }
        } else {

            for (int i = 0; i < dailyAttendancePojoArrayList.size(); i++) {
                DailyAttendancePojo attendancePojo;
                attendancePojo = dailyAttendancePojoArrayList.get(i);
                if (attendancePojo.isCheckedStatus()) {
                    JSONObject jsonObject1 = new JSONObject();
                    try {
                        jsonObject1.put("student_id", attendancePojo.getStudentid());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestArray1.put(jsonObject1);
                }

                JSONObject jsonObject = new JSONObject();

                if (attendancePojo.isCheckedStatus()) {

                    try {
                        jsonObject.put("attendance_status", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        jsonObject.put("attendance_status", "0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    jsonObject.put("fName", attendancePojo.getfName());
                    jsonObject.put("lName", attendancePojo.getlName());
                    if (attendancePojo.getRollno().equals(" ")) {
                        jsonObject.put("rollno", "");
                    } else {
                        jsonObject.put("rollno", attendancePojo.getRollno());
                    }
                    jsonObject.put("student_id", attendancePojo.getStudentid());
                    jsonObject.put("classid", class_id);
                    jsonObject.put("sectionid", section_id);
                    jsonObject.put("date", datesystem);

                    try {
                        //create SimpleDateFormat object with source string date format
                        SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yyyy");

                        //parse the string into Date object
                        Date date = sdfSource.parse(dateAttendance);

                        //create SimpleDateFormat object with desired date format
                        SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd");

                        //parse the date into another format
                        strDate = sdfDestination.format(date);
                        jsonObject.put("only_date", strDate);
                    } catch (ParseException pe) {
                        System.out.println("Parse Exception : " + pe);
                    }

                    jsonObject.put("teacher_id", teacherid);
                    jsonObject.put("academic_yr", academic_yr);
                    System.out.println("Second - " + jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                requestArray.put(jsonObject);
            }
        }
        try {
            mainRequestObject.put("arraylist", requestArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String stringTypeRequest = mainRequestObject.toString();
        System.out.println(stringTypeRequest);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, newUrl + "AdminApi/" + "mark_attendance",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                Toast.makeText(DailyAttendanceActivity.this, "Attendance deleted successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(DailyAttendanceActivity.this, HomePageDrawerActivity.class);

                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(DailyAttendanceActivity.this, "Failed to delete attendance", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, error -> {
            progressBar.setVisibility(View.GONE);
            error.printStackTrace();
            Toast.makeText(DailyAttendanceActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                final HashMap<String, String> params = new HashMap<>();
                params.put("short_name", name);

                params.put("class_id", class_id);
                params.put("section_id", section_id);
                params.put("academic_yr", academic_yr);

                try {
                    SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = sdfSource.parse(dateAttendance);
                    SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd");
                    strDate = sdfDestination.format(date);
                } catch (ParseException pe) {
                    System.out.println("Parse Exception : " + pe);
                }
                params.put("only_date", strDate);
                params.put("data", stringTypeRequest);
                params.put("login_type", "T");
                params.put("operation", "delete_attendance");

                return params;
            }

        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    private boolean validate1() {
        String date = pickdate.getText().toString();
        boolean b = true;
        if (classSpinAttendance.getSelectedItemPosition() < 0) {
            b = false;
        }
        if (date.isEmpty() || date.equals("null") || date.equals("* Select Date")) {
            b = false;
        }
        return b;
    }

    private void updateAttendance() {
        progressBar.setVisibility(View.VISIBLE);
        datesystem = SystemTime.getSystemDate();
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        final String teacherid = (SharedClass.getInstance(this).getRegId().toString());

        JSONObject mainRequestObject = new JSONObject();
        JSONArray requestArray = new JSONArray();
        JSONArray requestArray1 = new JSONArray();

        dateAttendance = pickdate.getText().toString();

        //empty list from start....
        if (dailyAttendancePojoArrayList.size() == 0) {
            for (int i = 0; i < dailyAttendancePojoArrayListnew.size(); i++) {
                DailyAttendancePojo attendancePojo;
                attendancePojo = dailyAttendancePojoArrayListnew.get(i);
                JSONObject jsonObject = new JSONObject();
                if (attendancePojo.isCheckedLeftStatus()) {

                    if (attendancePojo.isCheckedStatus()) {
                        JSONObject jsonObject1 = new JSONObject();
                        try {
                            jsonObject1.put("student_id", attendancePojo.getStudentid());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        requestArray1.put(jsonObject1);
                        try {
                            jsonObject.put("attendance_status", "1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            jsonObject.put("attendance_status", "0");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        jsonObject.put("fName", attendancePojo.getfName());
                        jsonObject.put("lName", attendancePojo.getlName());
                        if (attendancePojo.getRollno().equals(" ")) {
                            jsonObject.put("rollno", "");
                        } else {
                            jsonObject.put("rollno", attendancePojo.getRollno());
                        }
                        jsonObject.put("student_id", attendancePojo.getStudentid());
                        jsonObject.put("classid", class_id);
                        jsonObject.put("sectionid", section_id);

                        jsonObject.put("date", datesystem);

                        //date of which attendance is marked...
                        try {
                            //create SimpleDateFormat object with source string date format
                            SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yyyy");

                            //parse the string into Date object
                            Date date = sdfSource.parse(dateAttendance);

                            //create SimpleDateFormat object with desired date format
                            SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd");

                            //parse the date into another format
                            String strDate = sdfDestination.format(date);
                            jsonObject.put("only_date", strDate);
                        } catch (ParseException pe) {
                            System.out.println("Parse Exception : " + pe);
                        }
                        jsonObject.put("teacher_id", teacherid);
                        jsonObject.put("academic_yr", academic_yr);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestArray.put(jsonObject);
//                }
                } else {
                    JSONObject jsonObject1 = new JSONObject();
                    try {
                        jsonObject1.put("student_id", attendancePojo.getStudentid());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestArray1.put(jsonObject1);
                }
            }
        }

        //some students are marked already...
        else {
            for (int i = 0; i < dailyAttendancePojoArrayList.size(); i++) {
                DailyAttendancePojo attendancePojo;
                attendancePojo = dailyAttendancePojoArrayList.get(i);

                JSONObject jsonObject = new JSONObject();
                if (attendancePojo.isCheckedLeftStatus()) {
                    if (attendancePojo.isCheckedStatus()) {
                        JSONObject jsonObject1 = new JSONObject();
                        try {
                            jsonObject1.put("student_id", attendancePojo.getStudentid());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        requestArray1.put(jsonObject1);
                        try {
                            jsonObject.put("attendance_status", "1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            jsonObject.put("attendance_status", "0");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        jsonObject.put("fName", attendancePojo.getfName());
                        jsonObject.put("lName", attendancePojo.getlName());
                        if (attendancePojo.getRollno().equals(" ")) {
                            jsonObject.put("rollno", "");
                        } else {
                            jsonObject.put("rollno", attendancePojo.getRollno());
                        }
                        jsonObject.put("student_id", attendancePojo.getStudentid());
                        jsonObject.put("classid", class_id);
                        jsonObject.put("sectionid", section_id);
                        jsonObject.put("date", datesystem);

                        try {
                            //create SimpleDateFormat object with source string date format
                            SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yyyy");

                            //parse the string into Date object
                            Date date = sdfSource.parse(dateAttendance);

                            //create SimpleDateFormat object with desired date format
                            SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd");

                            //parse the date into another format
                            String strDate = sdfDestination.format(date);
                            jsonObject.put("only_date", strDate);
                        } catch (ParseException pe) {
                            System.out.println("Parse Exception : " + pe);
                        }
                        jsonObject.put("teacher_id", teacherid);
                        jsonObject.put("academic_yr", academic_yr);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestArray.put(jsonObject);

                }

            }
        }

        //requestArray1 is for absent student...
        final String listSize = String.valueOf(requestArray1.length());
        count.setText("Absent Students :  " + listSize);

        try {
            mainRequestObject.put("arraylist", requestArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String stringTypeRequest = mainRequestObject.toString();

        System.out.println(stringTypeRequest);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, newUrl + "AdminApi/mark_attendance",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                Toast.makeText(DailyAttendanceActivity.this, "Attendance updated successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DailyAttendanceActivity.this, HomePageDrawerActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(DailyAttendanceActivity.this, "Failed to update Attendance", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> {
            progressBar.setVisibility(View.GONE);
            error.printStackTrace();
            Toast.makeText(DailyAttendanceActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                final HashMap<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("class_id", class_id);
                params.put("section_id", section_id);
                params.put("academic_yr", academic_yr);

                try {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = sdfSource.parse(dateAttendance);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd");
                    strDate = sdfDestination.format(date);
                } catch (ParseException pe) {
                    System.out.println("Parse Exception : " + pe);
                }
                params.put("only_date", strDate);
                params.put("data", stringTypeRequest);
                Log.i("", "AttendanceArrayList:" + stringTypeRequest);
                params.put("login_type", "T");
                params.put("operation", "check_insert");

                Log.e("DailyUpdate", "======================Start===================================");
                Log.e("DailyUpdate", "short_name===>>" + name);
                Log.e("DailyUpdate", "class_id===>>" + class_id);
                Log.e("DailyUpdate", "section_id===>>" + section_id);
                Log.e("DailyUpdate", "academic_yr===>>" + academic_yr);
                Log.e("DailyUpdate", "only_date===>>" + strDate);
                Log.e("DailyUpdate", "AttendanceArrayList===>>" + stringTypeRequest);

                Log.e("DailyUpdate", "login_type===>>" + "T");
                Log.e("DailyUpdate", "operation===>>" + "check_insert");
                Log.e("DailyUpdate", "=====================END====================================");


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    private String getClassId(int position) {
        String classs = "";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            classs = json.getString("class_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return classs;
    }

    private String getSectionId(int position) {
        String section = "";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);
            //Fetching name from that object
            section = json.getString("section_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return section;

    }

    private void getClasses() {

        final String reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        HashMap<String, String> params = new HashMap<>();
        params.put("academic_yr", academic_yr);
        params.put("reg_id", reg_id);
        params.put("short_name", name);

        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "getClassAndSection_classteacheralloted", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressBar.setVisibility(View.GONE);
                try {

                    if (response.getString("status").equals("true")) {
                        result = response.getJSONArray("class_name");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject jsonObject = result.getJSONObject(i);
                            String a = jsonObject.getString("classname");
                            String b = jsonObject.getString("sectionname");
                            class_id = jsonObject.getString("class_id");
                            section_id = jsonObject.getString("section_id");
                            listClass.add(a + b);
                            clssnm = a;
                        }
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(DailyAttendanceActivity.this);
                        {
                            builder.setCancelable(true);
                            builder.setTitle("Alert");
                            builder.setMessage("This feature is applicable only for class teacher");
                            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

                            builder.create().show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                classSpinAttendance.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.mytextview, listClass));

            }

        }, error -> Log.d("Error", error.toString()));
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(jsonObjectRequest);
    }

    //Getting Students List with attendance OR without attendance
    private void getStuds() {
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        dateAttendance = pickdate.getText().toString();
        try {
            SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yyyy");
            Date date = sdfSource.parse(dateAttendance);
            SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd");
            strDate = sdfDestination.format(date);
        } catch (ParseException pe) {
            System.out.println("Parse Exception : " + pe);
        }
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("academic_yr", academic_yr);
        params.put("class_id", class_id);
        params.put("section_id", section_id);
        params.put("only_date", strDate);
        params.put("short_name", name);
        Log.e("Daily", "DailyAttendance=" + params);
        Log.e("Daily", "DailyURl=" + newUrl + "AdminApi/" + "get_attendance_students");
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "get_attendance_students", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                System.out.println("AttendanceList - " + response);
                Log.e("Daily", "DailyAttendance=" + response);

                progressBar.setVisibility(View.GONE);
                try {
                    if (response.getString("status").equals("true")) {
                        linearLayoutattendancestudent.setVisibility(View.VISIBLE);
                        JSONArray jsonArray;
                        try {
                            jsonArray = response.getJSONArray("students");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.getJSONObject(i);
                                String rollNo = json.getString("roll_no");
                                String studentid = json.getString("student_id");
                                String fName = json.getString("first_name");
                                String lName = json.getString("last_name");
                                String attendencestatus = json.getString("attendance_status");
                                String markStatus = json.getString("mark_attendance");
                                String delete_btn = json.getString("Delete_btn");

                                if (delete_btn.equals("0")) {
                                    btnDelete.setVisibility(View.GONE);
                                } else {
                                    btnDelete.setVisibility(View.VISIBLE);
                                }

                                if (rollNo.equals("null")) {
                                    if (markStatus.equals("1")) {
                                        rollNo = " ";
                                        DailyAttendancePojo vpojo = new DailyAttendancePojo(rollNo, fName, lName, studentid, attendencestatus, false, true);
                                        dailyAttendancePojoArrayList.add(vpojo);
                                    } else if (markStatus.equals("0")) {
                                        rollNo = " ";
                                        DailyAttendancePojo vpojo = new DailyAttendancePojo(rollNo, fName, lName, studentid, attendencestatus, false, false);
                                        dailyAttendancePojoArrayList.add(vpojo);
                                    }
                                    recyclerView.setAdapter(dailyATtendanceAdapter);
                                    dailyATtendanceAdapter.notifyDataSetChanged();
                                } else {
                                    if (markStatus.equals("1")) {
                                        DailyAttendancePojo vpojo = new DailyAttendancePojo(rollNo, fName, lName, studentid, attendencestatus, false, true);
                                        dailyAttendancePojoArrayList.add(vpojo);
                                    } else if (markStatus.equals("0")) {
                                        DailyAttendancePojo vpojo = new DailyAttendancePojo(rollNo, fName, lName, studentid, attendencestatus, false, false);
                                        dailyAttendancePojoArrayList.add(vpojo);
                                    }
                                    recyclerView.setAdapter(dailyATtendanceAdapter);
                                }
                                dailyATtendanceAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> progressBar.setVisibility(View.GONE));
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);
    }

    //Method to check whether roll numbers are allotted or not ??
    private void get_roll_num_count() {
        final RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        final HashMap<String, String> params = new HashMap<>();
        params.put("section_id", section_id);
        params.put("class_id", class_id);
        params.put("short_name", name);
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "get_roll_no_count", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("REMARK", "onResponse: " + response);
                progressBar.setVisibility(View.GONE);
                try {
                    String msg = response.getString("count_status");
                    if (msg.equals("FALSE")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DailyAttendanceActivity.this);
                        builder.setMessage("Please allot roll numbers using Web Application.")
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, error -> progressBar.setVisibility(View.GONE));
        requestQueue2.add(jsonObjectRequest);
    }

    private void updateLabel2() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        //Current date
        DateFormat.getDateTimeInstance().format(new Date());
        String currentDateTimeString;

        // editText is the EditText  that should display it
        currentDateTimeString = sdf.format(calendar.getTime());
        pickdate.setText(currentDateTimeString);
        search.setVisibility(View.VISIBLE);
    }
}

