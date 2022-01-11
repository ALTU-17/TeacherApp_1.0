package in.aceventura.evolvuschool.teacherapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.activities.AboutUs;
import in.aceventura.evolvuschool.teacherapp.activities.AllotDeallotActivity;
import in.aceventura.evolvuschool.teacherapp.activities.BookAVailableActivity;
import in.aceventura.evolvuschool.teacherapp.activities.ChangePasswordActivity;
import in.aceventura.evolvuschool.teacherapp.activities.CheckLeaveBalanceActivity;
import in.aceventura.evolvuschool.teacherapp.activities.DailyAttendanceActivity;
import in.aceventura.evolvuschool.teacherapp.activities.EvaluationActivity;
import in.aceventura.evolvuschool.teacherapp.activities.HomeworkActivity;
import in.aceventura.evolvuschool.teacherapp.activities.LeaveApplicationActivity;
import in.aceventura.evolvuschool.teacherapp.activities.LoginActivity;
import in.aceventura.evolvuschool.teacherapp.activities.MonitorOngoingExamActivity;
import in.aceventura.evolvuschool.teacherapp.activities.NewLoginPage;
import in.aceventura.evolvuschool.teacherapp.activities.NoticeActivity;
import in.aceventura.evolvuschool.teacherapp.activities.NoticeStaffActivity;
import in.aceventura.evolvuschool.teacherapp.activities.OnlineExamActivity;
import in.aceventura.evolvuschool.teacherapp.activities.OnlineExamDashboard;
import in.aceventura.evolvuschool.teacherapp.activities.RemarksActivity;
import in.aceventura.evolvuschool.teacherapp.activities.RequestHandler;
import in.aceventura.evolvuschool.teacherapp.activities.SharedClass;
import in.aceventura.evolvuschool.teacherapp.activities.TeacherProfileActivity;
import in.aceventura.evolvuschool.teacherapp.activities.TeachersNoteACtivity;
import in.aceventura.evolvuschool.teacherapp.activities.ViewAttendenceActivity;
import in.aceventura.evolvuschool.teacherapp.activities.ViewHmkACtivity;
import in.aceventura.evolvuschool.teacherapp.activities.ViewNotes;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.utils.ConstantsFile;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;

import static in.aceventura.evolvuschool.teacherapp.utils.ConstantsFile.flagVersion;


public class HomePageDrawerActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    String sId, reg_id, academic_yr;
    CardView cardNote, cardHmk, cardRmk, cardNotice, cardleavs, cardattendance, TeacherProfileCardView,
            StaffNoticeView, onlineExamCard, evaluationCardView, allotDeallotCard, monitorExamCard;
    GridView grid;
    RelativeLayout teacherDetails;
    SharedClass sharedClass;
    TextView txtusername, txtusernmnavigation, versionnView, tName, className, tvClass, versionView1, tvSupport;
    ImageView tlogo, imageView;
    String username, name, dUrl, newUrl;
    DatabaseHelper mDatabaseHelper;
    DrawerLayout drawer;
    String tUrl, gender, nameUrl, teacher;
    String androidversion, user_id;
    String loginPwd, default_pwd;
    String forced_update;

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        assert dir != null;
        return dir.delete();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        tlogo = findViewById(R.id.tlogo);
        tvClass = findViewById(R.id.tvClass);
        className = findViewById(R.id.className);
        tvClass.setVisibility(View.GONE);
        className.setVisibility(View.GONE);
        teacherDetails = findViewById(R.id.teacherDetails);
       // tvSupport = findViewById(R.id.tvSupport);

        tName = findViewById(R.id.tName);
        className = findViewById(R.id.className);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }


        // TODO: 18-07-2020
        //Refreshing the academic year in shared pref...
        getAcademicYear();

        user_id = SharedClass.getInstance(this).getUserName(); // getting user_id
        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        default_pwd = (SharedClass.getInstance(this).getPassword());

        Intent in = getIntent();
        loginPwd = in.getStringExtra("pwd");

        if (name != null) {
           // tvSupport.setText("support@aceventura.in"); //Change told by Lija Ma'am(24June2020)
        }
        else {
           // tvSupport.setText("aceventuraservices@gmail.com");
        }


        getVersion();

        if (forced_update != null && forced_update.equals("Y")) {
            forcedUpdate();
        }

        getTeacherDetails(reg_id, academic_yr);


        String logoUrl = newUrl + "AdminApi/get_teacher_gender";

        //Class and division from Api
        tUrl = newUrl + "AdminApi/get_class_of_classteacher";

        //TeacherName
        nameUrl = newUrl + "AdminApi/get_teacher_name";

        toolbar.setTitle("EvolvU Smart Teacher App");
        toolbar.setSubtitle("("+academic_yr+")");
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);

        getcardIds();

        String versionNameTeacherApp = Config.LOCAL_ANDROID_VERSION;
        if (flagVersion == 0) {
            sendVersionToServer(versionNameTeacherApp, reg_id, user_id);
            flagVersion = 1;
        }


        //From TeacherProfile Activity Using Intent
        Intent intent = getIntent();
        String Sex = intent.getStringExtra("sex");

        //On HOMEPAGE
        if (Sex != null) {
            if (Sex.equals("Male")) {
                tlogo.setImageResource(R.drawable.maleteacher);
            }
            else if (Sex.equals("Female")) {
                tlogo.setImageResource(R.drawable.femaleteacher);
            }
        }

        sharedClass = new SharedClass(getApplicationContext());
        username = sharedClass.loadSharedPreference_userName();


        if (!SharedClass.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));

        }

        cardNote.setOnClickListener(this);
        cardHmk.setOnClickListener(this);
        cardRmk.setOnClickListener(this);
        StaffNoticeView.setOnClickListener(this);
        cardleavs.setOnClickListener(this);
        cardattendance.setOnClickListener(this);
        evaluationCardView.setOnClickListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        txtusernmnavigation = headerView.findViewById(R.id.txtusernm);
        imageView = headerView.findViewById(R.id.imageView1);
        txtusernmnavigation.setText(teacher);

        Menu nav_Menu = navigationView.getMenu();
        versionView1 = headerView.findViewById(R.id.versionView1);
        if (name != null) {
            if (name.equals("EVOLVU")) {
                versionView1.setText("EVOLVU");
            }
            else {
                versionView1.setText(name);
            }
        }
        else {
            versionView1.setText("");
        }

        versionnView = headerView.findViewById(R.id.versionView);

        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            versionnView.setText("v " + versionName);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //method to check pwd with default pwd
        checkPwd(loginPwd, default_pwd);

        onlineExamCard.setOnClickListener(v -> {
            Intent onlineExamIntent = new Intent(getApplicationContext(), OnlineExamActivity.class);
            onlineExamIntent.putExtra("teacher", teacher);
            onlineExamIntent.putExtra("reg_id", reg_id);
            onlineExamIntent.putExtra("academic_yr", academic_yr);
            startActivity(onlineExamIntent);
        });

        allotDeallotCard.setOnClickListener(v -> {
            Intent onlineExamIntent = new Intent(getApplicationContext(), AllotDeallotActivity.class);
            onlineExamIntent.putExtra("teacher", teacher);
            onlineExamIntent.putExtra("reg_id", reg_id);
            onlineExamIntent.putExtra("academic_yr", academic_yr);
            startActivity(onlineExamIntent);
        });


        monitorExamCard.setOnClickListener(v -> {
            Intent onlineExamIntent = new Intent(getApplicationContext(), MonitorOngoingExamActivity.class);
            onlineExamIntent.putExtra("teacher", teacher);
            onlineExamIntent.putExtra("reg_id", reg_id);
            onlineExamIntent.putExtra("academic_yr", academic_yr);
            startActivity(onlineExamIntent);
        });


        //commented for now // TODO: 21-10-2020


        //bottomBar
        try {
//            View view = findViewById(R.id.bb_resultwebview);
//            TextView supportEmail = view.findViewById(R.id.email);
//            //---------------Support email--------------------------
//            if (name != null) {
//                String supportname = name.toLowerCase();
//
//                supportEmail.setText("For app support email to : " + "support" + supportname + "@aceventura.in");
//            } else {
//                supportEmail.setText("For app support email to : " + "aceventuraservices@gmail.com");
//                return;
//            }
            //bottomBar

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
            try {
                bottomBar.setDefaultTabPosition(0);
                bottomBar.setActiveTabColor(getResources().getColor(R.color.bottomactivateColor));
            } catch (Exception e) {
                e.printStackTrace();
            }
            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(HomePageDrawerActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(HomePageDrawerActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(HomePageDrawerActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(HomePageDrawerActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(HomePageDrawerActivity.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///
    }//OnCreate

    // TODO: For forcefully update
    private void forcedUpdate() {
        getVersion();

    }

    // TODO: 16-07-2020
    //update the academic_yr with the server side academic_yr
    private void getAcademicYear() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "AdminApi" +
                "/get_academic_year", response -> {
                    try {
                        if (response != null) {
                            JSONObject obj = new JSONObject(response.replace("ï»¿", ""));
                            if (obj.getString("status").equals("true")) {
                                Log.i("HomePageDrawerActivity", "Academic_yr" + obj.getString("acd_yr"));
                                try {
                                    SharedClass.getInstance(HomePageDrawerActivity.this.getApplicationContext()).setAcademicYear(obj.getString("acd_yr")//getting & setting acd from API
                                    );
                                }
                                catch (Exception ignored) {
                                }
                            }
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
            System.out.println(error.getMessage());
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                params.put("short_name", name);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void checkPwd(String loginPwd, String default_pwd) {
        if (loginPwd != null) {
            if (loginPwd.equals(default_pwd)) {
                Intent i = new Intent(HomePageDrawerActivity.this, ChangePasswordActivity.class);
                startActivity(i);
            }
        }
    }

    private void sendVersionToServer(final String versionNameTeacherApp, final String reg_id,
                                     final String user_id) {
        StringRequest stringRequestAppVersion = new StringRequest(Request.Method.POST, newUrl + "AdminApi" +
                "/add_teacher_app_version", response -> {

                    if (response != null && !response.equals("") && !response.equals("null") && response.equals(
                            "true")) {
                        System.out.println(response);
                    }
                    else {
                        System.out.println("False");
                    }
                }, error -> {
            error.printStackTrace();
            Log.i("", "get_app_version_error: " + error.getMessage());
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                params.put("short_name", name);
                params.put("user_id", user_id);
                params.put("app_version", versionNameTeacherApp);
                params.put("teacher_id", reg_id);
                System.out.println("APP_VERSION_PARAMS" + params);
                return params;
            }
        };
        stringRequestAppVersion.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestHandler.getInstance(HomePageDrawerActivity.this).addToRequestQueue(stringRequestAppVersion);
    }

    private void getTeacherDetails(String reg_id, String academic_yr) {
        HashMap<String, String> params3 = new HashMap<>();
        params3.put("academic_yr", academic_yr);
        params3.put("reg_id", reg_id);
        params3.put("short_name", name);
        System.out.println(params3);
        JsonObjectRequest jsonObjectRequest4 = new JsonObjectRequest(newUrl + "AdminApi/get_teacher",
                new JSONObject(params3), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("TeacherName " + response);
                if (response != null) {
                    try {
                        if (response.getString("status").equals("true")) {
                            teacherDetails.setVisibility(View.VISIBLE);
                            teacher = response.getString("teacher_name");
                            gender = response.getString("gender");
                            tName.setText(teacher);

                            //Saving Teacher Name in Pref
                            SharedClass.getInstance(getApplicationContext()).TeacherName(teacher);
                            if (teacher != null) {
                                txtusernmnavigation.setText(teacher);
                            }
                            else {
                                txtusernmnavigation.setText("");
                            }

                            if (gender.equals("Male") || gender.equals("male")) {
                                tlogo.setImageResource(R.drawable.maleteacher);
                                imageView.setImageResource(R.drawable.maleteacher);
                            }
                            else if (gender.equals("Female") || gender.equals("female")) {
                                tlogo.setImageResource(R.drawable.femaleteacher);
                                imageView.setImageResource(R.drawable.femaleteacher);
                            }

                            JSONArray result = response.getJSONArray("class");
                            JSONObject jsonObject = result.getJSONObject(0);
                            String a = jsonObject.getString("classname");
                            String b = jsonObject.getString("sectionname");

                            if (!a.equals("") && !b.equals("")) {
                                tvClass.setVisibility(View.VISIBLE);
                                className.setVisibility(View.VISIBLE);
                                className.setText(a + " " + b);
                            }
                            else {
                                tvClass.setVisibility(View.GONE);
                                className.setVisibility(View.GONE);
                            }

                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    teacherDetails.setVisibility(View.GONE);
                    tvClass.setVisibility(View.GONE);
                }
            }
        }, error -> {
            teacherDetails.setVisibility(View.GONE);
            tvClass.setVisibility(View.GONE);

        });
        RequestQueue requestQueue4 = Volley.newRequestQueue(this);
        jsonObjectRequest4.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue4.add(jsonObjectRequest4);
    }

    private void getcardIds() {
        drawer = findViewById(R.id.drawer_layout);
        cardNote = findViewById(R.id.noteCardView);
        cardHmk = findViewById(R.id.homeworkcardView);
        cardRmk = findViewById(R.id.remarkCardView);
        cardleavs = findViewById(R.id.leaveCardview);
        cardattendance = findViewById(R.id.dailyAttendanceCardView);
        StaffNoticeView = findViewById(R.id.StaffNoticeView);
        onlineExamCard = findViewById(R.id.OnlineExamCard);
        evaluationCardView = findViewById(R.id.evaluationCardView);
        allotDeallotCard = findViewById(R.id.allotDeallotCard);
        monitorExamCard = findViewById(R.id.monitorExamCard);

    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            finishAffinity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it  is present.
        getMenuInflater().inflate(R.menu.home_page_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logout_user();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(HomePageDrawerActivity.this, TeacherProfileActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_bookavail) {
            Intent intent = new Intent(HomePageDrawerActivity.this, BookAVailableActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_attendence) {
            Intent intent = new Intent(HomePageDrawerActivity.this, ViewAttendenceActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_bal) {
            Intent intent = new Intent(HomePageDrawerActivity.this, CheckLeaveBalanceActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_viewnotes) {
            Intent intent = new Intent(HomePageDrawerActivity.this, ViewNotes.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_viewhmk) {
            Intent intent = new Intent(HomePageDrawerActivity.this, ViewHmkACtivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_viewCLassNotes) {
            Intent intent = new Intent(HomePageDrawerActivity.this, NoticeActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_aboutus) {
            Intent intent = new Intent(HomePageDrawerActivity.this, AboutUs.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_chngpass) {
            Intent intent = new Intent(HomePageDrawerActivity.this, ChangePasswordActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.allotDeallotCard) {
            Intent intent = new Intent(HomePageDrawerActivity.this, AllotDeallotActivity.class);
            startActivity(intent);

        }

        else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomePageDrawerActivity.this);
            {
                builder.setCancelable(true);
                builder.setTitle("Alert");
                builder.setMessage("Do you want to Logout ?");
                builder.setPositiveButton("Yes", (dialog, which) -> logout_user());
                builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                builder.create().show();
            }

        }
        else if (id == R.id.nav_menu_online_exam){
            Intent intent = new Intent(HomePageDrawerActivity.this, OnlineExamDashboard.class);
            intent.putExtra("teacher", teacher);
            intent.putExtra("reg_id", reg_id);
            intent.putExtra("academic_yr", academic_yr);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout_user() {
        clearApplicationData();
        SharedClass.getInstance(this).logout();
        startActivity(new Intent(this, NewLoginPage.class));
        finish();


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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.noteCardView:
                Intent intent = new Intent(HomePageDrawerActivity.this, TeachersNoteACtivity.class);
                startActivity(intent);

                break;
            case R.id.homeworkcardView:
                Intent intent1 = new Intent(HomePageDrawerActivity.this, HomeworkActivity.class);
                startActivity(intent1);
                break;
            case R.id.remarkCardView:
                Intent intent2 = new Intent(HomePageDrawerActivity.this, RemarksActivity.class);
                startActivity(intent2);
                break;

            case R.id.dailyAttendanceCardView:
                Intent intent7 = new Intent(HomePageDrawerActivity.this, DailyAttendanceActivity.class);
                startActivity(intent7);
                break;

            case R.id.leaveCardview:
                Intent intent8 = new Intent(HomePageDrawerActivity.this, LeaveApplicationActivity.class);
                intent8.putExtra("teacher", teacher);
                intent8.putExtra("reg_id", reg_id);
                intent8.putExtra("academic_yr", academic_yr);
                startActivity(intent8);
                break;

            case R.id.StaffNoticeView:
                Intent intent10 = new Intent(HomePageDrawerActivity.this, NoticeStaffActivity.class);
                startActivity(intent10);
                break;

            case R.id.evaluationCardView:
                Intent intent3 = new Intent(HomePageDrawerActivity.this, EvaluationActivity.class);
                intent3.putExtra("reg_id", reg_id);
                startActivity(intent3);
                break;

        }
    }

    // Home page grid view images

    //Version Check Method
    private void getVersion() {

        String LiveVersionUrl = "http://aceventura.in/evolvuUserService/teacher_apk_lastest_version";
//        String DemoVersionUrl = "http://aceventura.in/demo/evolvuUserService/teacher_apk_lastest_version";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LiveVersionUrl,
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, DemoVersionUrl,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VERSION_RESPONSE", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            androidversion = jsonObject.getString("latest_version");
                            String release_notes = jsonObject.getString("release_notes");
                            forced_update = jsonObject.getString("forced_update");


                            if (androidversion != null) {
                                float androidversion_num = Float.parseFloat(androidversion);
                                float localandroidversion = Float.parseFloat(Config.LOCAL_ANDROID_VERSION);

                                if (localandroidversion < androidversion_num) {

                                    if (forced_update.equals("N")) {
                                        androidx.appcompat.app.AlertDialog.Builder dialog =
                                                new androidx.appcompat.app.AlertDialog.Builder(HomePageDrawerActivity.this);
                                        dialog.setCancelable(false);
                                        dialog.setIcon(android.R.drawable.ic_dialog_alert);
                                        dialog.setTitle(Config.LOCAL_ANDROID_VERSION_DAILOG_TITLE);
                                        dialog.setCancelable(false);
                                        dialog.setMessage(release_notes);
                                        dialog.setPositiveButton("Update", (dialog1, id) -> startActivity(new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("https" + "://play.google" + ".com/store/apps" + "/details?id=in.aceventura" + ".evolvuschool" + ".teacherapp"))));
                                        dialog.setNegativeButton("Cancel", (dialog12, which) -> dialog12.dismiss());
                                        final androidx.appcompat.app.AlertDialog alert = dialog.create();
                                        alert.show();
                                    }
                                    else if (forced_update.equals("Y")) {
                                        androidx.appcompat.app.AlertDialog.Builder dialog =
                                                new androidx.appcompat.app.AlertDialog.Builder(HomePageDrawerActivity.this);
                                        dialog.setCancelable(false);
                                        dialog.setIcon(android.R.drawable.ic_dialog_alert);
                                        dialog.setTitle(Config.LOCAL_ANDROID_VERSION_DAILOG_TITLE);
                                        dialog.setCancelable(false);
                                        dialog.setMessage(release_notes);
                                        dialog.setPositiveButton("Update", (dialog1, id) -> startActivity(new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("https" + "://play.google" + ".com/store/apps" + "/details?id=in.aceventura" + ".evolvuschool" + ".teacherapp"))));
                                        final androidx.appcompat.app.AlertDialog alert = dialog.create();
                                        alert.show();
                                    }
                                }
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> Log.d("Error Response", error.toString()));

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    public static class Holder {
        TextView tv;
        ImageView img;
    }

    public class CustomGrid extends BaseAdapter {
        private final String[] societyIcon;
        private final int[] Imageid;
        private Context mContext;
        private LayoutInflater inflater = null;

        public CustomGrid(Context c, String[] web, int[] imageid) {
            mContext = c;
            this.societyIcon = web;
            Imageid = imageid;

        }

        @Override
        public int getCount() {
            return societyIcon.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            Holder holder = null;
            if (convertView == null) {

                holder = new Holder();

                LayoutInflater inflater =
                        (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = inflater.inflate(R.layout.activity_grid_view, null);

                holder.tv = convertView.findViewById(R.id.grid_text);
                holder.img = convertView.findViewById(R.id.grid_image);

                convertView.setTag(holder);
            }
            else {
                holder = (Holder) convertView.getTag();

            }
            holder.tv.setText(societyIcon[position]);
            holder.img.setImageResource(Imageid[position]);

            return convertView;

        }
    }
}




