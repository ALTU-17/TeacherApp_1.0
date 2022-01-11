package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;

public class TeacherProfileActivity extends AppCompatActivity implements View.OnClickListener {
    public String proQual, training, Genders, blood;
    EditText et_Sname, et_Sdesignation;
    Button btnUpdate, btnCancel;
    EditText et_adharno, et_SubjectforDEdBEd, tv_sdob, tv_sdoj, et_experience, et_Religion, et_saddress, et_smobile, et_semail;
    RequestQueue mQueue;
    TextView tv_applicable, tv_1, tv_2, tv_3, tv_4;
    private ProgressDialog progressDialog;
    ProgressBar progressBar;
    String selectProQualValue, selectTraningStatusValue, selectBloodGroupValue, selectGenderValue;
    CheckBox[] cbs;
    ArrayList<String> selectedCheckboxes = new ArrayList<>();
    CheckBox chk1, chk2, chk3, chk4, chk5,
            chk6, chk7, chk8, chk9, chk10,
            chk11, chk12, chk13, chk14, chk15,
            chk16, chk17, chk18, chk19, chk20;
    String name;
    Spinner sProQual, sTS, sGender, sBG;
    String teacher_name = "";
    String dob = "";
    String doj = "";
    String designation = "";
    String newUrl, dUrl;
    int pro_qual = 0;
    String subject = "";
    int trainingstatus = 0;
    int tgender = 0;
    int bloodgrp = 0;

    String experience = "";
    String religion = "";
    String address = "";
    String mob = "";
    String email = "";
    String adhar_no = "";
    String applicable = "";
    public String reg_id;
    String academic = "";

    /*public List<String> Gender = new ArrayList<>();
    public List<String> BG = new ArrayList<>();
    public List<String> TS = new ArrayList<>();
    public List<String> ProQual = new ArrayList<>();*/

    String[] GenderValue = {"", "male", "female"};
    String[] Gender = {"Select Gender", "Male", "Female"};
    String[] BGValue = {"", "AB+", "AB-", "B+", "B-", "A+", "A-", "O+", "O-"};
    String[] BG = {"Select Blood Group", "AB+", "AB-", "B+", "B-", "A+", "A-", "O+", "O-"};


    String[] TSValue = {"", "Trained-PGT", "Trained-TGT", "Untrained", "NA"};
    String[] TS = {"Select Training Status", "Trained-PGT", "Trained-TGT", "Untrained", "NA"};


    String[] ProQualValue = {"", "TTC", "D.Ed", "B.Ed", "B.P.Ed", "M.P.Ed"};
    String[] ProQual = {"Select Professional Qualification", "TTC", "D.Ed", "B.Ed", "B.P.Ed", "M.P.Ed"};


    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().hide();
        View tb_main1 = findViewById(R.id.icd_tb_homeworkdetails);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("("+SharedClass.getInstance(getApplicationContext()).getAcademicYear()+")");

        school_title.setText(" Evolvu Teacher App");
        ht_Teachernote.setText("Teacher Profile");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        cbs = new CheckBox[20];

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }
        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        mQueue = Volley.newRequestQueue(this);
        et_Sname = findViewById(R.id.et_Sname);
        tv_sdob = findViewById(R.id.tv_sdob);
        tv_sdob.setEnabled(false);
        tv_sdoj = findViewById(R.id.tv_sdoj);
        tv_sdoj.setEnabled(false);
        et_Sdesignation = findViewById(R.id.et_Sdesignation);
        et_adharno = findViewById(R.id.et_adharno);
        sProQual = findViewById(R.id.sProQual);
        et_SubjectforDEdBEd = findViewById(R.id.et_SubjectforDEdBEd);
        sTS = findViewById(R.id.sTS);
        et_experience = findViewById(R.id.et_experience);
        sGender = findViewById(R.id.sGender);
        sBG = findViewById(R.id.sBG);
        et_Religion = findViewById(R.id.et_Religion);
        et_saddress = findViewById(R.id.et_saddress);
        et_smobile = findViewById(R.id.et_smobile);
        et_semail = findViewById(R.id.et_semail);
        et_adharno = findViewById(R.id.et_adharno);


        tv_1 = findViewById(R.id.tv_1);
        tv_2 = findViewById(R.id.tv_2);
        tv_3 = findViewById(R.id.tv_3);
        tv_4 = findViewById(R.id.tv_4);


        chk1 = findViewById(R.id.chk1);
        chk2 = findViewById(R.id.chk2);
        chk3 = findViewById(R.id.chk3);
        chk4 = findViewById(R.id.chk4);
        chk5 = findViewById(R.id.chk5);

        chk6 = findViewById(R.id.chk6);
        chk7 = findViewById(R.id.chk7);
        chk8 = findViewById(R.id.chk8);
        chk9 = findViewById(R.id.chk9);
        chk10 = findViewById(R.id.chk10);

        chk11 = findViewById(R.id.chk11);
        chk12 = findViewById(R.id.chk12);
        chk13 = findViewById(R.id.chk13);
        chk14 = findViewById(R.id.chk14);
        chk15 = findViewById(R.id.chk15);

        chk16 = findViewById(R.id.chk16);
        chk17 = findViewById(R.id.chk17);
        chk18 = findViewById(R.id.chk18);
        chk19 = findViewById(R.id.chk19);
        chk20 = findViewById(R.id.chk20);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
        btnUpdate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        getTeacherDetails();

        final ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, Gender);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderAdapter.notifyDataSetChanged();
        sGender.setAdapter(genderAdapter);


        final ArrayAdapter<String> qualAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, ProQual);
        qualAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qualAdapter.notifyDataSetChanged();
        sProQual.setAdapter(qualAdapter);


        final ArrayAdapter<String> bloodAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, BG);
        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodAdapter.notifyDataSetChanged();
        sBG.setAdapter(bloodAdapter);

        final ArrayAdapter<String> trainingAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, TS);
        qualAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qualAdapter.notifyDataSetChanged();
        sTS.setAdapter(trainingAdapter);


        //Aqualification
        sProQual.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                proQual = sProQual.getItemAtPosition(position).toString();

                if (proQual.equals("Select Professional Qualification")) {
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sTS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                training = sTS.getItemAtPosition(position).toString();
                if (training.equals("Select Training Status")) {
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Genders = sGender.getItemAtPosition(position).toString();
                if (Genders.equals("Select Gender")) {
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sBG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                blood = sBG.getItemAtPosition(position).toString();
                if (blood.equals("Select Blood Group")) {
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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



        try
        {

        Log.e("bottomBar","bottomBar caaled")   ;
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);

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

            bottomBar.setDefaultTabPosition(2);
            try {
                bottomBar.setActiveTabColor(getResources().getColor(R.color.bottomactivateColor));
            } catch (Exception e) {
                e.printStackTrace();
            }
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Log.e("bottomBar","bottomBar onTabSelected")   ;

                if (tabId == R.id.tab_dashboard) {
                    Log.e("bottomBar","bottomBar tab_dashboard 222")   ;
                    Intent intent = new Intent(TeacherProfileActivity.this, HomePageDrawerActivity.class);
                    startActivity(intent);
                }
                if (tabId == R.id.tab_calendar) {
                    Intent intent = new Intent(TeacherProfileActivity.this, MyCalendar.class);
                    startActivity(intent);
                }
            }
        });
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(int tabId) {
                Log.e("bottomBar","bottomBar onTabReSelected")   ;

//                if (tabId == R.id.tab_profile) {
//                    Intent intent = new Intent(TeacherProfileActivity.this, TeacherProfileActivity.class);
//                    startActivity(intent);
//                }
                if (tabId == R.id.tab_dashboard) {
                    Log.e("bottomBar","bottomBar tab_dashboard")   ;
                    Intent intent = new Intent(TeacherProfileActivity.this, HomePageDrawerActivity.class);
                    startActivity(intent);
                }
            }
        });
    } catch (Exception e) {
        e.printStackTrace();
        Log.e("bottomErrr", "wee" + e.getMessage());
    }

    }

    private boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public void updateTeacherDetails() {
        if (chk1.isChecked()) {
            selectedCheckboxes.add("HSc");
        }
        if (chk2.isChecked()) {
            selectedCheckboxes.add("B.Com");
        }
        if (chk3.isChecked()) {
            selectedCheckboxes.add("BCA");
        }
        if (chk4.isChecked()) {
            selectedCheckboxes.add("BE");
        }
        if (chk5.isChecked()) {
            selectedCheckboxes.add("M.A");
        }
        if (chk6.isChecked()) {
            selectedCheckboxes.add("M.Sc");
        }
        if (chk7.isChecked()) {
            selectedCheckboxes.add("M.Phil");
        }
        if (chk8.isChecked()) {
            selectedCheckboxes.add("DCE");
        }
        if (chk9.isChecked()) {
            selectedCheckboxes.add("B.Sc");
        }
        if (chk10.isChecked()) {
            selectedCheckboxes.add("B.LIS");
        }
        if (chk11.isChecked()) {
            selectedCheckboxes.add("MSE");
        }
        if (chk12.isChecked()) {
            selectedCheckboxes.add("MCA");
        }
        if (chk13.isChecked()) {
            selectedCheckboxes.add("MBA");
        }
        if (chk14.isChecked()) {
            selectedCheckboxes.add("B.A");
        }
        if (chk15.isChecked()) {
            selectedCheckboxes.add("BCS");
        }
        if (chk16.isChecked()) {
            selectedCheckboxes.add("BPharm");
        }
        if (chk17.isChecked()) {
            selectedCheckboxes.add("MCom");
        }
        if (chk18.isChecked()) {
            selectedCheckboxes.add("M.LIS");
        }
        if (chk19.isChecked()) {
            selectedCheckboxes.add("PGDBM");
        }
        if (chk20.isChecked()) {
            selectedCheckboxes.add("B.Music n Dance");
        }

        //checkboxes values....
        final String Qual = selectedCheckboxes.toString();

        final String T_Pname = et_Sname.getText().toString().trim();
        final String designation = et_Sdesignation.getText().toString().trim();
        final String t_dob = tv_sdob.getText().toString().trim();
        final String t_doj = tv_sdoj.getText().toString().trim();
        final String subDB = et_SubjectforDEdBEd.getText().toString().trim();
        final String exp = et_experience.getText().toString().trim();
        final String Religion = et_Religion.getText().toString().trim();
        final String tAddress = et_saddress.getText().toString().trim();
        final String tMobile = et_smobile.getText().toString().trim();
        final String tAdharno = et_adharno.getText().toString().trim();
        final String tEmail = et_semail.getText().toString().trim();
        final String tSpecialSub = et_SubjectforDEdBEd.getText().toString().trim();


        int selectGender = sGender.getSelectedItemPosition();
        selectGenderValue = GenderValue[selectGender];

        int selectBloodGroup = sBG.getSelectedItemPosition();
        selectBloodGroupValue = BGValue[selectBloodGroup];

        int selectProQual = sProQual.getSelectedItemPosition();
        selectProQualValue = ProQualValue[selectProQual];

        int selectTraningStatus = sTS.getSelectedItemPosition();
        selectTraningStatusValue = TSValue[selectTraningStatus];

        final String tProQual = tv_1.getText().toString().trim();
        final String tStatus = tv_2.getText().toString().trim();
        final String tGender = tv_3.getText().toString().trim();
        final String tBlood = tv_4.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "AdminApi/" + "teachers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        if (response == null) {
                            Toast.makeText(TeacherProfileActivity.this, "Failed to Update", Toast.LENGTH_LONG).show();
                        }
                        Intent intent = new Intent(TeacherProfileActivity.this, HomePageDrawerActivity.class);
                        intent.putExtra("sex", selectGenderValue);
                        startActivity(intent);
                        Toast.makeText(TeacherProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error: Check Internet Connection", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                final Map<String, String> params = new HashMap<>();
                params.put("reg_id", reg_id);
                params.put("short_name", name);
                params.put("operation", "update");
                params.put("name", T_Pname);
                params.put("designation", designation);

                {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    Date date1 = null;
                    try {
                        date1 = simpleDateFormat.parse(t_dob);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String dob1 = newformat.format(date1);
                    params.put("birthday", dob1);
                }

                {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    Date date1 = null;
                    try {
                        date1 = simpleDateFormat.parse(t_doj);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String dob2 = newformat.format(date1);
                    params.put("joining", dob2);
                }

                params.put("subject", subDB);
                params.put("experience", exp);
                params.put("religion", Religion);
                params.put("address", tAddress);
                params.put("phone", tMobile);
                params.put("email", tEmail);
                params.put("aadhar_card_no", tAdharno);
                params.put("str_array", Qual);
                params.put("special_sub", tSpecialSub);
                params.put("professional_qual", selectProQualValue);
                params.put("trained", selectTraningStatusValue);
                params.put("sex", selectGenderValue);
                params.put("blood_group", selectBloodGroupValue);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void getTeacherDetails() {
        final HashMap<String, String> params = new HashMap<>();
        params.put("reg_id", reg_id);
        params.put("short_name", name);
        params.put("operation", "view");

        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest request = new JsonObjectRequest(newUrl + "AdminApi/" + "teachers", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("RESPONSE", "onResponse: " + response);
                        progressBar.setVisibility(View.GONE);
                        try {
                            if (response.getString("status").equals("true")) {
                                JSONArray jsonArray = response.getJSONArray("teachers_info");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    teacher_name = jsonObject.getString("name");
                                    dob = jsonObject.getString("birthday");
                                    doj = jsonObject.getString("date_of_joining");
                                    designation = jsonObject.getString("designation");

                                    //check box values
                                    academic = jsonObject.getString("academic_qual");

                                    subject = jsonObject.getString("special_sub");

                                    //professional_qual Spinner
                                    pro_qual = Arrays.asList(ProQualValue).indexOf(jsonObject.getString("professional_qual"));

                                    //trainingstatus spinner
                                    trainingstatus = Arrays.asList(TSValue).indexOf(jsonObject.getString("trained"));

                                    experience = jsonObject.getString("experience");

                                    //Gender spinner
                                    tgender = Arrays.asList(GenderValue).indexOf(jsonObject.getString("sex"));

                                    //BloodGroup spinner
                                    bloodgrp = Arrays.asList(BGValue).indexOf(jsonObject.getString("blood_group"));

                                    religion = jsonObject.getString("religion");
                                    address = jsonObject.getString("address");
                                    mob = jsonObject.getString("phone");
                                    email = jsonObject.getString("email");
                                    adhar_no = jsonObject.getString("aadhar_card_no");

                                }
                                //=====================================================
                                //                   Setting Spinner Values
                                // =====================================================


                                sGender.setSelection(tgender);
                                sBG.setSelection(bloodgrp);
                                sProQual.setSelection(pro_qual);
                                sTS.setSelection(trainingstatus);

                                //=======================================

                                if (teacher_name.equals("null")) {
                                    et_Sname.setText("");
                                } else {
                                    et_Sname.setText(teacher_name);
                                }

                                if (dob.equals("null")) {
                                    tv_sdob.setText("");
                                } else {

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                    Date date1 = null;
                                    try {
                                        date1 = simpleDateFormat.parse(dob);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    String dob1 = newformat.format(date1);
                                    tv_sdob.setText(dob1);
                                }

                                if (doj.equals("null")) {
                                    tv_sdoj.setText("");
                                } else {
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                    Date date1 = null;
                                    try {
                                        date1 = simpleDateFormat.parse(doj);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    String doj1 = newformat.format(date1);
                                    tv_sdoj.setText(doj1);
                                }

                                if (designation.equals("null")) {
                                    et_Sdesignation.setText("");
                                } else {
                                    et_Sdesignation.setText(designation);
                                }

                                //-------------Academic qualification---------------------------
                                if (!academic.equals("null")) {
                                    if (academic.contains("HSc")) {
                                        chk1.setChecked(true);
                                    }
                                    if (academic.contains("B.Com")) {
                                        chk2.setChecked(true);
                                    }
                                    if (academic.contains("BCA")) {
                                        chk3.setChecked(true);
                                    }
                                    if (academic.contains("BE")) {
                                        chk4.setChecked(true);
                                    }
                                    if (academic.contains("M.A")) {
                                        chk5.setChecked(true);
                                    }

                                    if (academic.contains("M.Sc")) {
                                        chk6.setChecked(true);
                                    }
                                    if (academic.contains("M.Phil")) {
                                        chk7.setChecked(true);
                                    }
                                    if (academic.contains("DCE")) {
                                        chk8.setChecked(true);
                                    }
                                    if (academic.contains("B.Sc")) {
                                        chk9.setChecked(true);
                                    }
                                    if (academic.contains("B.LIS")) {
                                        chk10.setChecked(true);
                                    }

                                    if (academic.contains("MSE")) {
                                        chk11.setChecked(true);
                                    }
                                    if (academic.contains("MCA")) {
                                        chk12.setChecked(true);
                                    }
                                    if (academic.contains("MBA")) {
                                        chk13.setChecked(true);
                                    }
                                    if (academic.contains("B.A")) {
                                        chk14.setChecked(true);
                                    }
                                    if (academic.contains("BCS")) {
                                        chk15.setChecked(true);
                                    }

                                    if (academic.contains("BPharm")) {
                                        chk16.setChecked(true);
                                    }
                                    if (academic.contains("MCom")) {
                                        chk17.setChecked(true);
                                    }
                                    if (academic.contains("M.LIS")) {
                                        chk18.setChecked(true);
                                    }
                                    if (academic.contains("PGDBM")) {
                                        chk19.setChecked(true);
                                    }
                                    if (academic.contains("B.Music n Dance")) {
                                        chk20.setChecked(true);
                                    }
                                } else {
                                    return;
                                }

//--------------------------------------------------------------------------------------------

                                if (subject.equals("null")) {
                                    et_SubjectforDEdBEd.setText("");
                                } else {
                                    et_SubjectforDEdBEd.setText(subject);
                                }

                                if (experience.equals("null")) {
                                    et_experience.setText("");
                                } else {
                                    et_experience.setText(experience);
                                }

                                if (religion.equals("null")) {
                                    et_Religion.setText("");
                                } else {
                                    et_Religion.setText(religion);
                                }

                                if (address.equals("null")) {
                                    et_saddress.setText("");
                                } else {
                                    et_saddress.setText(address);
                                }

                                if (mob.equals("null")) {
                                    et_smobile.setText("");
                                } else {
                                    et_smobile.setText(mob);
                                }

                                if (email.equals("null")) {
                                    et_semail.setText("");
                                } else {
                                    et_semail.setText(email);
                                }

                                if (adhar_no.equals("null")) {
                                    et_adharno.setText("");
                                } else {
                                    et_adharno.setText(adhar_no);
                                }
                            } else {
                                Toast.makeText(TeacherProfileActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.btnUpdate)) {

            if (et_Sname.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                et_Sname.setError("Field cannot be Blank");
                return;
            } else if (et_Sdesignation.getText().toString().length() == 0) {
                et_Sdesignation.setError("Enter Designation");
                return;
            } else if (sTS.getSelectedItem() == "Select Training Status") {
                Toast.makeText(this, "Please Select Training Status", Toast.LENGTH_SHORT).show();
                return;
            } else if (sGender.getSelectedItem() == "Select Gender") {
                Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                return;
            } else if (sBG.getSelectedItem() == "Select Blood Group") {
                Toast.makeText(this, "Please Select Blood Group", Toast.LENGTH_SHORT).show();
                return;
            } else if (!chk1.isChecked() && !chk2.isChecked() && !chk3.isChecked() && !chk4.isChecked() && !chk5.isChecked()
                    && !chk6.isChecked() && !chk7.isChecked() && !chk8.isChecked() && !chk9.isChecked() && !chk10.isChecked()
                    && !chk11.isChecked() && !chk12.isChecked() && !chk13.isChecked() && !chk14.isChecked() && !chk15.isChecked()
                    && !chk16.isChecked() && !chk17.isChecked() && !chk18.isChecked() && !chk19.isChecked() && !chk20.isChecked()) {
                Toast.makeText(this, "Please Select Acaedemic Qualification", Toast.LENGTH_SHORT).show();
                return;
            } else if (et_experience.getText().toString().length() == 0) {
                et_experience.setError("Enter Experience");
                return;
            } else if (et_saddress.getText().toString().length() == 0) {
                et_saddress.setError("Enter Address");
                return;
            } else if (et_smobile.getText().toString().length() == 0 || et_smobile.getText().toString().length() < 10) {
                et_smobile.setError("Invalid Mobile No");
                return;

            }/*else if (et_adharno.getText().toString().length() == 0 || et_adharno.getText().toString().length() < 12) {
                et_adharno.setError("Invalid Aadhar No");
                return;
            }*/ // TODO: 05-03-2020 Removed Aadhar Mandatory

            else if (et_semail.getText().toString().length() != 0 && !isValidEmailId(et_semail.getText().toString().trim())) {
                Toast.makeText(getApplicationContext(), "Enter Valid Email Address.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                updateTeacherDetails();
            }
        }
        if (v == findViewById(R.id.btnCancel)) {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        onBackPressed();
        return true;
    }
}

