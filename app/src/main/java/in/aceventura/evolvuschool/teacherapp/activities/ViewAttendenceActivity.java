package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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


import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.adapter.ViewATtendanceAdapterZero;
import in.aceventura.evolvuschool.teacherapp.adapter.ViewAttendenceAdapter;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.DailyAttendancePojo;
import androidx.annotation.IdRes;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;


public class ViewAttendenceActivity extends AppCompatActivity {

    private Spinner spinviewAttendence;
    private TextView txtDate;
    Calendar calendar;
    private ArrayList<String> listClass;
    String class_id, section_id;
    private JSONArray result;
    private SharedClass sh;
    TextView count;
    public static String clssnm = "";
    String datesystem;
    String date,name,newUrl,dUrl;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    private Button btnUpdate;
    RecyclerView recyclerViewone,recyclerViewzero;
    ViewAttendenceAdapter viewAttendenceAdapter;
    ViewATtendanceAdapterZero viewATtendanceAdapterZero;
    ArrayList<DailyAttendancePojo> dailyAttendancePojoArrayList;
    ArrayList<DailyAttendancePojo> dailyAttendancePojoArrayListzero;
    ArrayList<DailyAttendancePojo> checkedArraylist;
    LinearLayoutManager manager;
    LinearLayoutManager manager1;
    LinearLayout linearLayoutattendancelist,rollstudlayout;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendence);

        getIds();
        getSupportActionBar().hide();
        View tb_main1 = findViewById(R.id.icd_tb_homeworkdetails);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("("+SharedClass.getInstance(getApplicationContext()).getAcademicYear()+")");

        school_title.setText(" Evolvu Teacher App");
        ht_Teachernote.setText("View Attendance");
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
        manager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewone.setLayoutManager(manager);
        recyclerViewzero.setLayoutManager(manager1);

        listClass = new ArrayList<>();

        try {
            spinviewAttendence.setOnTouchListener(classSpinnerOnTouch);

        } catch (Exception e) {

        }
        sh = new SharedClass(this);
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
//        Sname = (SharedClass.getInstance(this).getShortname());
//        newUrl=(SharedClass.getInstance(this).getUrl());
        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl=mDatabaseHelper.getURL(1);
        newUrl=mDatabaseHelper.getTURL(1);

        if (name ==null || name.equals("")){
            name = mDatabaseHelper.getName(1);
            dUrl=mDatabaseHelper.getURL(1);
            newUrl=mDatabaseHelper.getTURL(1);
        }





//        progressDialog = new ProgressDialog(this);
        try {
            spinviewAttendence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {   //String option;

                    //listClass.clear();
                    //notifyAll();

                    //t1.setText(getClassId(position));
                    //t2.setText(getSectionId(position));

                    class_id = getClassId(position);
                    section_id = getSectionId(position);


//                    classnm =  classSpinner.getItemAtPosition(classSpinner.getSelectedItemPosition()).toString();

                    //Toast.makeText(getApplicationContext(), "Class: " + class_id + " Section :" + section_id, Toast.LENGTH_LONG).show();;
                    //getData1(getClassId(position), getSectionId(position));

                    //  classSpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listClass));
                } // to close the onItemSelected

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                                                boolean isFirstResource) {


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
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
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
                        Intent intent = new Intent(ViewAttendenceActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(ViewAttendenceActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(ViewAttendenceActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(ViewAttendenceActivity.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
    }
    private boolean validate() {
        boolean b = true;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        String a = txtDate.getText().toString();
        try {
            compareDatesByCompareTo(df, df.parse(a), df.parse("01-09-2018"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }
    public void compareDatesByCompareTo(DateFormat df, Date oldDate, Date newDate) {

        if (oldDate.compareTo(newDate) < 0) {
            Toast.makeText(this, "Pick valid date", Toast.LENGTH_SHORT).show();        }
    }

    private void getIds() {
        spinviewAttendence=findViewById(R.id.classSpinAttendanceview);
        txtDate=findViewById(R.id.pickdateview);
        TextView txtSearch = findViewById(R.id.searchbtnattendanceview);
        recyclerViewone=findViewById(R.id.recycleridviewattendenceone);
        recyclerViewzero=findViewById(R.id.recycleridviewattendencezero);
        linearLayoutattendancelist=findViewById(R.id.statuslayout);
        rollstudlayout=findViewById(R.id.rollstudlayout);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date  =txtDate.getText().toString();
                if (date.isEmpty() || date.equals("null") || date.equals("* Select Date") || date.equals(null)) {
                    if (validate()) {
                        if (spinviewAttendence.getSelectedItemPosition()<0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(ViewAttendenceActivity.this);
                            {
                                builder.setCancelable(true);
                                builder.setTitle("Alert");
                                builder.setMessage("Fill All * Fields ");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                });
/*
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
*/
                                builder.create().show();
                                //Toast.makeText(this, "Fill All * Fields", Toast.LENGTH_SHORT).show();

                            }
                        }
                        else {
                            getStuds();

                        }
                    } else {
                        Toast.makeText(ViewAttendenceActivity.this, "Enter Mandatory Fields", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewAttendenceActivity.this);
                    {
                        builder.setCancelable(true);
                        builder.setTitle("Alert");
                        builder.setMessage("Fill All * Fields ");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
/*
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
*/
                        builder.create().show();
                        //Toast.makeText(this, "Fill All * Fields", Toast.LENGTH_SHORT).show();

                    }
                }
            }

        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 0);
               /* String month=calendar.get(Calendar.MONTH)+1+"";
                if(month.length()<2){
                    month="0"+month;
                }*/

                //Set yesterday time milliseconds as date pickers minimum date
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewAttendenceActivity.this, startDate1, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                if (Build.VERSION.SDK_INT < 22){
                    datePickerDialog.getDatePicker().setCalendarViewShown(true);
                    datePickerDialog.getDatePicker().setSpinnersShown(false);
                    datePickerDialog.getDatePicker().getCalendarView().setShowWeekNumber(false);
                }

              /*  calendar.set( calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH) -1,0,0);*/

           /*     Date mindate=new Date();
                mindate.setTime(calendar.getTimeInMillis());*/
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                //datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }
    final DatePickerDialog.OnDateSetListener startDate1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            //String a=SystemTime.getSystemDate1();

                updateLabel2();

        }
    };

    private void updateLabel2() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date = sdf.format(calendar.getTime());
        txtDate.setText(date);


    }
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

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("academic_yr", academic_yr);
        params.put("reg_id", reg_id);
        params.put("short_name", name);
//        progressDialog.setMessage("Loading Classes...");
//        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl+"AdminApi/"+"getClassAndSection_classteacheralloted", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                progressDialog.dismiss();
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
                            // FancyToast.makeText(CreateTeachernote.this,"Hello World !",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true);
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewAttendenceActivity.this);
                        {
                            builder.setCancelable(true);
                            builder.setTitle("Alert");
                            builder.setMessage("This feature is applicable only for class teacher");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });

                            builder.create().show();
                            //Toast.makeText(CreateRemarkActivity.this, "Fill All * Fields", Toast.LENGTH_SHORT).show();

                        }
                        //Toast.makeText(ViewAttendenceActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinviewAttendence.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.mytextview, listClass));

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            progressBar.setVisibility(View.GONE);
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(jsonObjectRequest);
    }


    private void getStuds() {
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

//        progressDialog.show();
//        progressDialog.setMessage("Loading Students...");
        progressBar.setVisibility(View.VISIBLE);

        HashMap<String, String> params = new HashMap<>();
        params.put("academic_yr", academic_yr);
        params.put("class_id", class_id);
        params.put("section_id", section_id);
        params.put("only_date", date);
        params.put("short_name", name);

//        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl+"AdminApi/" + "get_attendance_students", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {

//                progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                try {

                    if (response.getString("status").equals("true")) {

                        linearLayoutattendancelist.setVisibility(View.VISIBLE);
                        rollstudlayout.setVisibility(View.VISIBLE);
                        dailyAttendancePojoArrayList = new ArrayList<>();
                        dailyAttendancePojoArrayListzero = new ArrayList<>();

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
                                                // listStudents.add(rollNo+". "+fName+" "+lName);

                                                //attendancestatus one means absent and 0 means present
                                                if (attendencestatus.equalsIgnoreCase("1")){

                                                    DailyAttendancePojo vpojo = new DailyAttendancePojo(rollNo, fName, lName, studentid, attendencestatus,false,false);
                                                    dailyAttendancePojoArrayList.add(vpojo);

                                                    viewAttendenceAdapter = new ViewAttendenceAdapter(ViewAttendenceActivity.this, dailyAttendancePojoArrayList);
                                                    recyclerViewone.setAdapter(viewAttendenceAdapter);

                                                    viewAttendenceAdapter.notifyDataSetChanged();
                                                }

                                                if (attendencestatus.equalsIgnoreCase("0")){

                                                    DailyAttendancePojo vpojo = new DailyAttendancePojo(rollNo, fName, lName, studentid, attendencestatus,false,false);
                                                    dailyAttendancePojoArrayListzero.add(vpojo);
                                                    viewATtendanceAdapterZero= new ViewATtendanceAdapterZero(ViewAttendenceActivity.this,dailyAttendancePojoArrayListzero);

                                                    recyclerViewzero.setAdapter(viewATtendanceAdapterZero);

                                                    viewATtendanceAdapterZero.notifyDataSetChanged();
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else {
                                            linearLayoutattendancelist.setVisibility(View.GONE);
                                            rollstudlayout.setVisibility(View.GONE);
                                            Toast.makeText(ViewAttendenceActivity.this, "No Records Found", Toast.LENGTH_SHORT).show();
                                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //classSpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listClass));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);
    }
/*
    private void getStudent(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                String rollNo = json.getString("roll_no");
                String studentid = json.getString("student_id");

                String fName = json.getString("first_name");
                String lName = json.getString("last_name");
                String attendencestatus = json.getString("attendance_status");
                // listStudents.add(rollNo+". "+fName+" "+lName);

                DailyAttendancePojo vpojo = new DailyAttendancePojo(rollNo, fName, lName, studentid, attendencestatus, false);
                dailyAttendancePojoArrayList.add(vpojo);

                viewAttendenceAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        //  StudSpin.setAdapter(new ArrayAdapter<String>(StudentInfoActivity.this, android.R.layout.simple_spinner_dropdown_item, liststudent));
    }
*/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
