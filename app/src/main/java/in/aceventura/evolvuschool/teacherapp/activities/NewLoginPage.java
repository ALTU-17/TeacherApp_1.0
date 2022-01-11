package in.aceventura.evolvuschool.teacherapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.aceventura.evolvuschool.teacherapp.Config;
import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;

public class NewLoginPage extends AppCompatActivity {
    private EditText Uname;
    public static String sn;
    public static String url;
    public static String dUrl, pUrl;
    public static String email;
    ProgressBar progressBar;
    TextView tvVersion;
    TextView tvSupport;
    DatabaseHelper mDatabaseHelper;
    String androidversion, forced_update;
    String note_priority, homework_priority, remark_priority, notice_priority, dailyattendance_prority, leave_priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login_page);

        //Crashlytics initialize code // 24July2020
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        progressBar = findViewById(R.id.progressBar);
        progressBar.bringToFront();
        progressBar.setVisibility(View.GONE);
        tvVersion = findViewById(R.id.tvVersion);
        tvSupport = findViewById(R.id.tvSupport);
        mDatabaseHelper = new DatabaseHelper(this);

        tvSupport.setOnClickListener(v -> {
            String TO = "aceventuraservices@gmail.com";
            Intent email = new Intent(Intent.ACTION_SEND);
            email.setDataAndType(Uri.parse(TO), "message/rfc822");
            startActivity(Intent.createChooser(email, "Support Mail :"));
        });


        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tvVersion.setText("v " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (SharedClass.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, HomePageDrawerActivity.class));
            return;
        }

        getVersion();
        if (forced_update != null && forced_update.equals("Y")) {
            forcedUpdate();
        }


        Uname = findViewById(R.id.et_uid);
        ImageView next = findViewById(R.id.bt_Next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                email = Uname.getText().toString().trim();

                if (email.matches("")) {
                    Uname.setError("UserId cannot be empty");
                    progressBar.setVisibility(View.GONE);
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.NEW_LOGIN,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.e("Login","==Responce==>>"+response);
                                        JSONObject obj = new JSONObject(response.replace("ï»¿<html>", ""));
                                        System.out.println("NEWLOGIN - " + response);
                                        sn = obj.getString("short_name");
                                        url = obj.getString("url");
                                        dUrl = obj.getString("teacherapk_url");
                                        pUrl = obj.getString("project_url");
                                        String default_pwd = obj.getString("default_password");
                                        SharedClass.getInstance(getApplicationContext())
                                                .setPassword(default_pwd);

                                        SharedClass.getInstance(getApplicationContext())
                                                .newdUrl(obj.getString("project_url"));

                                        mDatabaseHelper.addDetails(sn, url, dUrl, pUrl);
                                        String shortname = mDatabaseHelper.getName(1);
                                        if (shortname == null || shortname.equals("")) {
                                            return;
                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                            Intent i = new Intent(NewLoginPage.this, LoginActivity.class);
                                            i.putExtra("user_id", email);
                                            i.putExtra("short_name", sn);
                                            startActivity(i);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }, error -> {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(NewLoginPage.this, "Invalid UserId...Try again", Toast.LENGTH_SHORT).show();
                                Uname.setError("Invalid UserId");
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            //params.put("Content-Type", "application/json; charset=utf-8");
                            params.put("user_id", email);
                            return params;
                        }
                    };

                    RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                }
            }

        });
    }

    // TODO: For forcefully update
    private void forcedUpdate() {
        getVersion();
    }


    // TODO: For forcefully update
    /*@Override
    protected void onResume() {
        super.onResume();
        getVersion();
    }*/

    // TODO: "IMPORTANT" Update the utils/Config - "LOCAL_ANDROID_VERSION" code
    // TODO: "IMPORTANT" Update the build.gradle(Module:app) - "versionName" and "versionCode"
    //         BEFORE uploading on the play store.....

    //Version Check Method
    private void getVersion() {

        String LiveVersionUrl = "http://aceventura.in/evolvuUserService/teacher_apk_lastest_version";

//        String DemoVersionUrl = "http://aceventura.in/demo/evolvuUserService/teacher_apk_lastest_version";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LiveVersionUrl,
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, DemoVersionUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            System.out.println("UPDATE_RESPONSE" + jsonObject);
                            androidversion = jsonObject.getString("latest_version");
                            String release_notes = jsonObject.getString("release_notes");
                            forced_update = jsonObject.getString("forced_update");

                            if (androidversion != null) {
                                float androidversion_num = Float.parseFloat(androidversion);
                                float localandroidversion = Float.parseFloat(Config.LOCAL_ANDROID_VERSION);

                                if (localandroidversion < androidversion_num) {
                                    if (forced_update.equals("N")) {
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(NewLoginPage.this);
                                        dialog.setCancelable(false);
                                        dialog.setIcon(android.R.drawable.ic_dialog_alert);
                                        dialog.setTitle(Config.LOCAL_ANDROID_VERSION_DAILOG_TITLE);
                                        dialog.setCancelable(false);
                                        dialog.setMessage(release_notes);
                                        dialog.setPositiveButton("Update", (dialog1, id) -> startActivity(new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("https://play.google.com/store/apps/details?id=in.aceventura.evolvuschool.teacherapp"))));
                                        dialog.setNegativeButton("Cancel", (dialog12, which) -> dialog12.dismiss());
                                        final AlertDialog alert = dialog.create();
                                        alert.show();
                                    } else if (forced_update.equals("Y")) {
                                        androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(NewLoginPage.this);
                                        dialog.setCancelable(false);
                                        dialog.setIcon(android.R.drawable.ic_dialog_alert);
                                        dialog.setTitle(Config.LOCAL_ANDROID_VERSION_DAILOG_TITLE);
                                        dialog.setCancelable(false);
                                        dialog.setMessage(release_notes);
                                        dialog.setPositiveButton("Update", (dialog1, id) -> startActivity(new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("https://play.google.com/store/apps/details?id=in.aceventura.evolvuschool.teacherapp"))));
                                        final androidx.appcompat.app.AlertDialog alert = dialog.create();
                                        alert.show();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> Log.d("Error Response", error.toString()));

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}


