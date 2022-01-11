package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;


public class CreateLeaveApplicationActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    public static final String LOGGEDIN_SHARED_PREF1 = "loggedin1";
    public static final String tradrrid = "trader_id";
    private final String TAG = "LoginActivity";
    public String staffname, name, newUrl, dUrl;
    Button btnsave, btnreset, btnback;
    Spinner spinleavetype;
    Calendar myCalender;
    double lAlloted, lAleaveavailed;
    DatePicker datePicker;

    SharedPreferences sharedpreferences;
    String leaveStartDate;
    String leaveEndDate;
    String leaveAdd;
    String nodays;
    String reg_id;
    String academic_yr;
    String teacher;
    String leaveid;
    String leavetype;
    String staffid;
    String leavealloted;
    String leaveavailed;
    String reason;
    String leave_type_id, remainingLeaves, remLeaveDays;
    JSONArray result;
    DatabaseHelper mDatabaseHelper;
    RequestQueue requestQueue;
    ProgressBar progressBar;
    ArrayList<String> Pnames = new ArrayList<>();
    List<String> id = new ArrayList<>();
    JSONArray jsonArray;
    Date leaveStart;
    Date leaveEnd;
    private SharedClass sharedClass;
    int position;


    private EditText edtstartdate, edtenddate, edtnodays, edtreason;
    final DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            myCalender.set(Calendar.YEAR, year);
            myCalender.set(Calendar.MONTH, monthOfYear);
            myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            updateLabelenddate();
        }
    };
    final DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            myCalender.set(Calendar.YEAR, year);
            myCalender.set(Calendar.MONTH, monthOfYear);
            myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel1();
        }
    };

    private String getLeaveId(int position) {
        String leave_Tid = "";
        try {
            JSONObject json = jsonArray.getJSONObject(position - 1);
            leave_Tid = json.getString("leave_type_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return leave_Tid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myCalender = Calendar.getInstance();
        setContentView(R.layout.activity_create_leave_application);

        getSupportActionBar().hide();
        View tb_main1 = findViewById(R.id.icd_tb_homeworkdetails);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        // tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        tv_academic_yr.setText("("+SharedClass.getInstance(getApplicationContext()).getAcademicYear()+")");

        school_title.setText(" Evolvu Teacher App");
        ht_Teachernote.setText("Create Leave Application");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        edtenddate = findViewById(R.id.leaveEndDatetxt);
        edtstartdate = findViewById(R.id.leaveStartDatetxt);
        edtnodays = findViewById(R.id.daysLeave);
        edtreason = findViewById(R.id.reasonLeave);

        nodays = edtnodays.getText().toString();
        reason = edtreason.getText().toString();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        spinleavetype = findViewById(R.id.leavetypeSpin);
        TextView staffnametxt = findViewById(R.id.staffnametxt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getId();

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }

        //getting teacher name using intent
        teacher = getIntent().getStringExtra("teacher");
        staffnametxt.setText(teacher);

        spinleavetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i != 0) {
                    leave_type_id = (getLeaveId(i));
                    remLeaveDays = getRemainingdays(i);
                    position = adapterView.getSelectedItemPosition();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                newUrl + "AdminApi/" + "get_balance_leave",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("LeaveResponse - " + response);
                        try {
                            if (response != null) {
                                JSONObject jsonObject = new JSONObject(response);
                                jsonArray = jsonObject.getJSONArray("balance_leave");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    leave_type_id = String.valueOf(jsonObject1.getInt("leave_type_id"));
                                    leavetype = jsonObject1.getString("name");
                                    staffid = jsonObject1.getString("staff_id");
                                    leavealloted = jsonObject1.getString("leaves_allocated");
                                    leaveavailed = jsonObject1.getString("leaves_availed");

                                    lAlloted = Double.parseDouble(leavealloted);
                                    lAleaveavailed = Double.parseDouble(leaveavailed);
                                    remainingLeaves = String.valueOf(lAlloted - lAleaveavailed);
                                    Pnames.add(leavetype + " " + "(" + remainingLeaves + ")");

                                }
                                Pnames.add(0, "Select Leave Type");
                                spinleavetype.setAdapter(new ArrayAdapter<>(CreateLeaveApplicationActivity.this,
                                        android.R.layout.simple_spinner_dropdown_item, Pnames));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getMessage();
                        Toast.makeText(CreateLeaveApplicationActivity.this, "" + error, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                final HashMap<String, String> params = new HashMap<>();
                params.put("acd_yr", academic_yr);
                params.put("reg_id", reg_id);
                params.put("short_name", name);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


        btnreset = findViewById(R.id.resetleave);
        btnsave = findViewById(R.id.saveleave);

        //==============================================================


        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetlleave();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (validate()){
                        addLeave();
                    } /*else {
                        dialogBox();
                    }*/
            }
        });

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
                        Intent intent = new Intent(CreateLeaveApplicationActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(CreateLeaveApplicationActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(CreateLeaveApplicationActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(CreateLeaveApplicationActivity.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }

    }

    private void dialogBox() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateLeaveApplicationActivity.this);
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
            builder.create().show();
        }
    }


    private boolean validate() {
        boolean validate = true;
        String totaldays = edtnodays.getText().toString();

        if (spinleavetype.getSelectedItemPosition() == 0) {
            Toast.makeText(CreateLeaveApplicationActivity.this, "Select Leave Type", Toast.LENGTH_SHORT).show();
            validate = false;
        }
        else if (edtstartdate.getText().toString().equals("")) {
            Toast.makeText(CreateLeaveApplicationActivity.this, "Select Start date", Toast.LENGTH_LONG).show();
            validate = false;
        }
        else if (edtenddate.getText().toString().equals("")) {
            Toast.makeText(CreateLeaveApplicationActivity.this, "Select End date", Toast.LENGTH_LONG).show();
            validate = false;
        }
        else if (edtnodays.getText().toString().isEmpty()) {
            Toast.makeText(CreateLeaveApplicationActivity.this, "Enter Total Days of Leave", Toast.LENGTH_SHORT).show();
            validate = false;
        }
        else if (!totaldays.isEmpty()) {
            int days = Integer.parseInt(totaldays);
            double rDays = Double.parseDouble(remLeaveDays);
            double nDays = Double.parseDouble(totaldays);
            if (days <= 0) {
                Toast.makeText(CreateLeaveApplicationActivity.this, "Please Select End Date Greater than Start Date", Toast.LENGTH_LONG).show();
                validate = false;
            } else if (nDays > rDays) {
                Toast.makeText(CreateLeaveApplicationActivity.this, "You don't have sufficient leave available", Toast.LENGTH_LONG).show();
                validate = false;
            }
        }
        return validate;
    }


    private String getRemainingdays(int i) {
        String remDays = "";
        try {
            if (i != 0) {
                JSONObject json = jsonArray.getJSONObject(i - 1);
                leavealloted = json.getString("leaves_allocated");
                leaveavailed = json.getString("leaves_availed");
                lAlloted = Double.parseDouble(leavealloted);
                lAleaveavailed = Double.parseDouble(leaveavailed);
                remDays = String.valueOf(lAlloted - lAleaveavailed);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return remDays;
    }

    private void getId() {

        final Calendar cal1 = Calendar.getInstance();
        final Calendar cal2 = Calendar.getInstance();


        edtstartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtstartdate.setFocusable(false);
                cal1.add(Calendar.DATE, 0);
                DatePickerDialog dpd = new DatePickerDialog(CreateLeaveApplicationActivity.this, startDate, cal1.get(Calendar.YEAR),
                        cal1.get(Calendar.MONTH),
                        cal1.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });

        edtenddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtenddate.setFocusable(false);
                cal2.add(Calendar.DATE, 0);
                DatePickerDialog dpd = new DatePickerDialog(CreateLeaveApplicationActivity.this, endDate, cal2.get(Calendar.YEAR),
                        cal2.get(Calendar.MONTH),
                        cal2.get(Calendar.DAY_OF_MONTH));


                dpd.show();
            }
        });

    }

    private void updateLabel1() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        leaveStartDate = sdf.format(myCalender.getTime());
        edtstartdate.setText(leaveStartDate);
    }

    private void updateLabelenddate() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        leaveEndDate = sdf.format(myCalender.getTime());
        edtenddate.setText(leaveEndDate);


        SimpleDateFormat myFt = new SimpleDateFormat("dd MM yyyy");
        String inputString1 = edtstartdate.getText().toString().replace("-", " ");
        String inputString2 = edtenddate.getText().toString().replace("-", " ");


        try {
            Date date1 = myFt.parse(inputString1);
            Date date2 = myFt.parse(inputString2);
            long diff = date2.getTime() - date1.getTime();
            System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            int d = Integer.parseInt(String.valueOf(days));
            String v = String.valueOf(d + 1);
            edtnodays.setText(v);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void addLeave() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "AdminApi/leave_application",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jo1 = new JSONObject(response);
                            String succmsg = jo1.getString("status");
                            if (succmsg.equals("true")) {
                                Toast.makeText(CreateLeaveApplicationActivity.this, "Leave Applied Successfully", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        Intent intent = new Intent(CreateLeaveApplicationActivity.this, LeaveApplicationActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateLeaveApplicationActivity.this, ""+ error.toString(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onErrorResponse Leave: " + error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                final HashMap<String, String> params = new HashMap<>();
                params.put("leave_type_id", leave_type_id);
                params.put("start_date", leaveStartDate);
                params.put("end_date", leaveEndDate);
                params.put("no_of_days", edtnodays.getText().toString());
                params.put("status", "A");
                params.put("reason_rejection", "");
                params.put("staff_id", reg_id);
                params.put("acd_yr", academic_yr);
                params.put("operation", "create");
                params.put("reason", edtreason.getText().toString());
                params.put("short_name", name);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    //    }
    } //Oncreate

    private void resetlleave() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LeaveApplicationActivity.class);
        intent.putExtra("reg_id", reg_id);
        intent.putExtra("academic_yr", academic_yr);
        startActivity(intent);
    }
}






















