package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;

public class forgotpassword extends AppCompatActivity {

    String newUrl, dUrl, name;
    DatabaseHelper mDatabaseHelper;
    Calendar calendar;
    TextView acd_yr, SchoolName;
    private ProgressDialog progressDialog;
    RequestQueue resetPwdRequestQueue, requestQueue1;
    EditText userid, mothername, dob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgetpw);
//        getSupportActionBar().hide();

        TextView txt = findViewById(R.id.ftxt2); //footer text
        TextClock textClock = findViewById(R.id.textClock); //date and time
        acd_yr = findViewById(R.id.acd_yr); //acd yr
        SchoolName = findViewById(R.id.appname);
        progressDialog = new ProgressDialog(this);

        resetPwdRequestQueue = Volley.newRequestQueue(this);
        requestQueue1 = Volley.newRequestQueue(this);


        userid = findViewById(R.id.et_userId);
        mothername = findViewById(R.id.et_motherName);
        dob = findViewById(R.id.et_DOB);
        Button resetpwd = findViewById(R.id.bt_Reset);
        Button back = findViewById(R.id.bt_LoginPage);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);
        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }

        getSchoolDetails(name);
        final String ResetPwdUrl = newUrl + "AdminApi/reset_password";


        textClock.setFormat12Hour("EEEE MMM d yyyy hh:mm:ss a");
        textClock.setTimeZone("Asia/Kolkata");
        calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener startDate1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();
            }

            private void updateLabel2() {
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                //Current date
                DateFormat.getDateTimeInstance().format(new Date());
                String currentDateTimeString;

                // setting date to dob edittext
                currentDateTimeString = sdf.format(calendar.getTime());
                dob.setText(currentDateTimeString);

            }
        };


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(forgotpassword.this, startDate1,
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


        //footer for opening company's website
        txt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myWebLink = new Intent(Intent.ACTION_VIEW);
                myWebLink.setData(Uri.parse("http://aceventura.in"));
                startActivity(myWebLink);
            }
        });

        //send back to newLoginPage
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearApplicationData();
                Intent bck = new Intent(forgotpassword.this, NewLoginPage.class);
                startActivity(bck);
            }
        });


        //Api calling or reset pwd & send back to newLoginPage
        resetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Resetting the password...");

                    final String dateOfBirth = dob.getText().toString();            //date of birth
                    final String userId = userid.getText().toString();             //userId
                    final String motherName = mothername.getText().toString();    //motherName


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, ResetPwdUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    Log.i("PWDRESPONSE", "onResponse: " + response);
                                    try {

                                        JSONObject obj = new JSONObject(response);
                                        if (obj.getString("status").equals("true")) {
                                            progressDialog.dismiss();
                                            String msg = obj.getString("message");
                                            AlertDialog.Builder dialog = new AlertDialog.Builder(forgotpassword.this);
                                            dialog.setCancelable(false);
                                            dialog.setIcon(android.R.drawable.ic_dialog_alert);
                                            dialog.setTitle("Password Reset Successful");
                                            dialog.setMessage(msg);

                                            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog1, int id) {
                                                    clearApplicationData();
                                                    Intent i = new Intent(forgotpassword.this, NewLoginPage.class);
                                                    startActivity(i);
                                                }
                                            });

                                            final AlertDialog alert = dialog.create();
                                            alert.show();

                                        } else if (obj.getString("status").equals("false")) {
                                            progressDialog.dismiss();
                                            String msg = obj.getString("message");
                                            Toast.makeText(forgotpassword.this, "" + msg, Toast.LENGTH_LONG).show();
                                        }

                                    } catch (JSONException e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(forgotpassword.this, "Please check the details", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(forgotpassword.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> resetPasswordParams = new HashMap<>();
                            resetPasswordParams.put("short_name", name);
                            resetPasswordParams.put("user_id", userId);
                            resetPasswordParams.put("answer_one", motherName);
                            resetPasswordParams.put("dob", dateOfBirth);
                            resetPasswordParams.put("role_id", "T");
                            return resetPasswordParams;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    resetPwdRequestQueue.add(stringRequest);

                }
            }
        });
    }

    private void getSchoolDetails(String name) {

        HashMap<String, String> params = new HashMap<>();
        params.put("short_name", name);

        JsonObjectRequest schoolDetailJsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/get_settings_data", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                if (response.getString("status").equals("true")) {
                                    JSONArray acdArray = response.getJSONArray("acd_data");
                                    JSONObject acdObject = acdArray.getJSONObject(0);
                                    SchoolName.setText(acdObject.getString("institute_name"));
                                    acd_yr.setText(acdObject.getString("academic_yr"));
                                }
                            } catch (JSONException e) {
                                SchoolName.setText("Evolvu Teacher Application");
                                acd_yr.setVisibility(View.GONE);
                                e.printStackTrace();
                            }
                        } else {
                            SchoolName.setText("Evolvu Teacher Application");
                            acd_yr.setVisibility(View.GONE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SchoolName.setText("Evolvu Teacher Application");
                acd_yr.setVisibility(View.GONE);
                Toast.makeText(forgotpassword.this, "Restart the application and try again...\n " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue1.add(schoolDetailJsonObjectRequest);

    }

    public boolean validate() {
        boolean b = true;

        if (userid.getText().toString().trim().equals("")) {
            userid.setError("Enter UserId");
            userid.requestFocus();
            b = false;
        } else if (mothername.getText().toString().trim().equals("")) {
            mothername.setError("Enter Mother's Name");
            mothername.requestFocus();
            b = false;
        } else if (dob.getText().toString().trim().equals("")) {
            dob.setError("Enter Date of birth");
            dob.requestFocus();
            b = false;
        }
        return b;
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));

                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        assert dir != null;
        return dir.delete();
    }

}

