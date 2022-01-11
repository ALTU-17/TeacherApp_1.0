package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;


public class EditLeaveAPplicationActivity extends AppCompatActivity {

    public String staffname, name, newUrl, dUrl, remLeaveDays, type_name;
    double lAlloted, lAleaveavailed;
    Button btnsave, btnreset, btnback;
    Spinner spinleavetype;
    Calendar myCalender;
    String leaveStartDate, leaveEndDate;
    ProgressBar progressBar;
    JSONArray result;
    DatabaseHelper mDatabaseHelper;
    String leaveAdd;
    String nodays, reason;
    RequestQueue requestQueue;
    List<String> Pnames = new ArrayList<>();
    String reg_id;
    String academic_yr;
    ArrayAdapter<String> pAdapter;
    String leave_type;
    String start_date;
    String end_date;
    String no_of_days;
    String status;
    String reason_rejection;
    String staff_id;
    String acd_yr;
    String operation;
    String leaveid;
    String leavetype;
    String staffid;
    String leavealloted;
    String leaveavailed, leave_app_id, leave_name;
    String reasonone, leave_type_id, leaveName;
    JSONArray jsonArray;
    int typeId, apiLeaveId,newLeaveId,pId;
    EditText edtstartdate, edtenddate, edtnodays, edtreason,approverscomment;
    float nDays;
    String remainingDays;
    TextView leave_status;

    final DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            myCalender.set(Calendar.YEAR, year);
            myCalender.set(Calendar.MONTH, monthOfYear);
            myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel1();
        }
    };


    DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            myCalender.set(Calendar.YEAR, year);
            myCalender.set(Calendar.MONTH, monthOfYear);
            myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabelenddate();


        }
    };

    private String getLeaveId(int position) {
        String leave_Tid = "";
        try {
//            JSONObject json = jsonArray.getJSONObject(position-1);
            JSONObject json = jsonArray.getJSONObject(position);
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
        setContentView(R.layout.activity_edit_leave_application);
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

        leave_type = getIntent().getStringExtra("leave_type_id");

        start_date = getIntent().getStringExtra("start_date");
        end_date = getIntent().getStringExtra("end_date");
        no_of_days = getIntent().getStringExtra("no_of_days");
        status = getIntent().getStringExtra("status");
        reason_rejection = getIntent().getStringExtra("reason_rejection");
        staff_id = getIntent().getStringExtra("staff_id");
        acd_yr = getIntent().getStringExtra("operation");
        operation = getIntent().getStringExtra("operation");
        reason = getIntent().getStringExtra("reason");
        leaveName = getIntent().getStringExtra("leave_type_name");
        leave_app_id = getIntent().getStringExtra("leave_app_id");
        leave_name = getIntent().getStringExtra("leave_name");

        getLeaves();

        switch (status) {
            case "A":
                leave_status.setText("Applied");
                break;
            case "P":
                leave_status.setText("Approved");
                break;
            case "H":
                leave_status.setText("Hold");
                break;
            case "R":
                leave_status.setText("Reject");
                break;
        }



        typeId = Integer.parseInt(leave_type);

        //no of days
        edtnodays.setText(no_of_days);

        //reason
        edtreason.setText(getIntent().getStringExtra("reason"));

        approverscomment.setEnabled(false);
        //reason of rejection
        if (reason_rejection.isEmpty()){
            approverscomment.setText("");
        }else {
            approverscomment.setText(reason_rejection);
        }



        nodays = edtnodays.getText().toString();
        reason = edtreason.getText().toString();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        //=========================================================================
        if (!start_date.equals("null")) {
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = null;
            try {
                newDate = spf.parse(start_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spf = new SimpleDateFormat("dd-MM-yyyy");
            start_date = spf.format(newDate);
            edtstartdate.setText(start_date);

        }

        if (!end_date.equals("null")) {
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = null;
            try {
                newDate = spf.parse(end_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spf = new SimpleDateFormat("dd-MM-yyyy");
            end_date = spf.format(newDate);
            edtenddate.setText(end_date);
        }


        //========================================================================

        spinleavetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i != 0) {
                    leave_type_id = (getLeaveId(i));
                    remLeaveDays = getRemainingdays(i);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnreset = findViewById(R.id.resetleaveupdate);
        btnsave = findViewById(R.id.saveleaveupdate);

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetlleave();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totaldays = edtnodays.getText().toString();
                float days = Float.parseFloat(totaldays);

                if (spinleavetype.getSelectedItemPosition() < 0) {
                    Toast.makeText(EditLeaveAPplicationActivity.this, "Select Leave Type", Toast.LENGTH_SHORT).show();
                } else if (edtstartdate.getText().toString().equals("")) {
                    Toast.makeText(EditLeaveAPplicationActivity.this, "Select Start date", Toast.LENGTH_LONG).show();
                } else if (edtenddate.getText().toString().equals("")) {
                    Toast.makeText(EditLeaveAPplicationActivity.this, "Select End date", Toast.LENGTH_LONG).show();
                } else if (edtnodays.getText().toString().equals("")) {
                    Toast.makeText(EditLeaveAPplicationActivity.this, "Enter number of days", Toast.LENGTH_LONG).show();
                } else if (days <= 0) {
                    Toast.makeText(EditLeaveAPplicationActivity.this, "Please Select End Date Greater than Start Date", Toast.LENGTH_LONG).show();
                } else {
                    addLeave();
                }
            }
        });

    }

    private void getLeaves() {
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
                                int j = 0;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    leave_type_id = String.valueOf(jsonObject1.getInt("leave_type_id"));
                                    leavetype = jsonObject1.getString("name");
                                    staffid = jsonObject1.getString("staff_id");
                                    leavealloted = jsonObject1.getString("leaves_allocated");
                                    leaveavailed = jsonObject1.getString("leaves_availed");
                                    lAlloted = Double.parseDouble(leavealloted);
                                    lAleaveavailed = Double.parseDouble(leaveavailed);
                                    remainingDays = String.valueOf(lAlloted - lAleaveavailed);

                                    int lid = Integer.parseInt(leave_type_id);
                                    Pnames.add(leavetype + " " + "(" + remainingDays + ")");

                                    if (typeId == lid) {
                                        j = i;
                                    }
                                }

                                spinleavetype.setAdapter(new ArrayAdapter<>(EditLeaveAPplicationActivity.this,
                                        android.R.layout.simple_spinner_dropdown_item, Pnames));

                                //Setting the previously selected value on CreateLeaveAct in EditLeaveAct
                                spinleavetype.setSelection(j);
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
                        Toast.makeText(EditLeaveAPplicationActivity.this, "" + error, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                final HashMap<String, String> params = new HashMap<>();
                params.put("acd_yr", SharedClass.getInstance(EditLeaveAPplicationActivity.this).loadSharedPreference_acdYear());
                params.put("reg_id", SharedClass.getInstance(EditLeaveAPplicationActivity.this).getRegId().toString());
                params.put("short_name", name);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private String getRemainingdays(int i) {
        String remDays = "";
        try {
//            if (i != 0) {
//                JSONObject json = jsonArray.getJSONObject(i - 1);
                JSONObject json = jsonArray.getJSONObject(i);
                leavealloted = json.getString("leaves_allocated");
                leaveavailed = json.getString("leaves_availed");

                lAlloted = Double.parseDouble(leavealloted);
                lAleaveavailed = Double.parseDouble(leaveavailed);
                remDays = String.valueOf(lAlloted - lAleaveavailed);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return remDays;
    }


    private void getId() {
        spinleavetype = findViewById(R.id.leavetypeSpinupdate);
        edtenddate = findViewById(R.id.leaveEndDatetxtupdate);
        edtstartdate = findViewById(R.id.leaveStartDatetxtupdate);
        edtnodays = findViewById(R.id.daysLeaveupdate);
        edtreason = findViewById(R.id.reasonLeaveupdate);
        approverscomment = findViewById(R.id.approverscomment);
        leave_status = findViewById(R.id.leave_status);

        final Calendar cal1 = Calendar.getInstance();
        final Calendar cal2 = Calendar.getInstance();

        edtstartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtstartdate.setFocusable(false);
                cal1.add(Calendar.DATE, 0);
                DatePickerDialog dpd = new DatePickerDialog(EditLeaveAPplicationActivity.this, startDate, cal1.get(Calendar.YEAR),
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
                DatePickerDialog dpd = new DatePickerDialog(EditLeaveAPplicationActivity.this, endDate, cal2.get(Calendar.YEAR),
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
            long value = diff + 1;
            System.out.println("Days: " + TimeUnit.DAYS.convert(value, TimeUnit.MILLISECONDS));
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
        String edt = edtnodays.getText().toString();
        float nDays = Float.parseFloat(edt);
        float rDays = Float.parseFloat(remLeaveDays);
        if (nDays > rDays) {
            Toast.makeText(EditLeaveAPplicationActivity.this, "You don't have sufficient leave available", Toast.LENGTH_LONG).show();
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "AdminApi/leave_application",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jo1 = new JSONObject(response);
                                String succmsg = jo1.getString("status");
                                if (succmsg.equals("true")) {
                                    Toast.makeText(EditLeaveAPplicationActivity.this, "Application Edited Successfully", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                            Log.i("", "onResponse: " + response);
                            Intent intent = new Intent(EditLeaveAPplicationActivity.this, LeaveApplicationActivity.class);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    final HashMap<String, String> params = new HashMap<>();
                    params.put("leave_type_id", leave_type_id);
                    params.put("start_date", edtstartdate.getText().toString());
                    params.put("end_date", edtenddate.getText().toString());
                    params.put("no_of_days", edtnodays.getText().toString());
                    params.put("status", "A");
                    params.put("reason_rejection", "");
                    params.put("staff_id", SharedClass.getInstance(EditLeaveAPplicationActivity.this).getRegId().toString());
                    params.put("acd_yr", SharedClass.getInstance(EditLeaveAPplicationActivity.this).loadSharedPreference_acdYear());
                    params.put("operation", "edit");
                    params.put("reason", edtreason.getText().toString());
                    params.put("leave_app_id", leave_app_id);
                    params.put("short_name", name);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }
    }

    private void resetlleave() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LeaveApplicationActivity.class);
        startActivity(intent);
    }

}
