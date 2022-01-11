package in.aceventura.evolvuschool.teacherapp.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.pojo.LoginData;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView edtusernm, schoolname, tv_forget;
    ImageView btnLogin, childimg;
    String Uname, Sname, newUrl, name, dUrl;
    ProgressBar progressBar;
    DatabaseHelper mDatabaseHelper;
    private EditText edtpass;
    private SharedClass sharedClass;
    private LoginData loginData;
    String note_priority, homework_priority, remark_priority, notice_priority, dailyattendance_prority, leave_priority;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getId();
        childimg = findViewById(R.id.childimg);
        schoolname = findViewById(R.id.schoolname);
        edtusernm = findViewById(R.id.edtUsername);
        Uname = getIntent().getStringExtra("user_id");
        newUrl = (SharedClass.getInstance(this).getUrl());
        Log.i("LOGIN", "onCreate: " + newUrl);
        edtusernm.setText(Uname);
        progressBar = findViewById(R.id.progressBar);
        progressBar.bringToFront();
        progressBar.setVisibility(View.GONE);
        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }

        System.out.println("URL - " + newUrl);


        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name != null) {
                    //going to forgot password activity
                    Intent i = new Intent(LoginActivity.this, forgotpassword.class);
                    startActivity(i);

                } else {
                    String TO = "aceventuraservices@gmail.com";
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.setDataAndType(Uri.parse(TO), "message/rfc822");
                    startActivity(Intent.createChooser(email, "Support Mail :"));
                }
            }
        });


        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        note_priority = bundle.getString("note_priority");
        homework_priority = bundle.getString("homework_priority");
        remark_priority = bundle.getString("remark_priority");
        notice_priority = bundle.getString("notice_priority");
        dailyattendance_prority = bundle.getString("dailyattendance_prority");
        leave_priority = bundle.getString("leave_priority");


        if (name != null) {
            switch (name) {
                case "SACS":
                    childimg.setBackgroundResource(R.drawable.newarnolds_logo);
                    break;
                case "SFSNE":
                    childimg.setBackgroundResource(R.drawable.transparentsfsne);
                    break;
                case "SFSNW":
                    childimg.setBackgroundResource(R.drawable.transparentsfsnw);
                    break;
                case "SJSKW":
                    childimg.setBackgroundResource(R.drawable.sjskw);
                    break;
                case "SFSPUNE":
                    childimg.setBackgroundResource(R.drawable.sfspune);
                    break;
            }
        } else {
            childimg.setBackgroundResource(R.drawable.evolvuteacer);
        }
        if (name != null) {
            schoolname.setText(name + "  EvolvU Smart Teacher App");
        }
        if (SharedClass.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, HomePageDrawerActivity.class));
            return;
        }

        btnLogin.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        clearApplicationData();
        Intent i = new Intent(LoginActivity.this, NewLoginPage.class);
        startActivity(i);
        finish();
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            if (children != null) {
                for (String s : children) {
                    if (!s.equals("lib")) {
                        deleteDir(new File(appDir, s));

                    }
                }
            }
        }
    }

    private void getId() {
        sharedClass = new SharedClass(getApplicationContext());
        edtpass = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tv_forget = findViewById(R.id.tv_forget);

        final CheckBox checkbox = findViewById(R.id.checkBox);
        checkbox.setOnCheckedChangeListener((compoundButton, checked) -> {

            if (checked) {
                edtpass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                edtpass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {
            if (validate()) {
                loginUser();
            } else {
                dialogBox();

            }
        }
    }

    private void dialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        {
            builder.setCancelable(true);
            builder.setTitle("Alert");
            builder.setMessage("Enter Valid Username and password");
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        }
    }

    private void loginUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String password = edtpass.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "LoginApi/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOGINRESPONSE", "onCreate: " + response);
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) {
                                loginData = new LoginData();
                                SharedClass.getInstance(getApplicationContext())
                                        .userLogin(
                                                obj.getString("user_id"),
                                                obj.getInt("reg_id"),
                                                obj.getString("name"),
                                                obj.getString("academic_yr")
                                        );
                                loginData.setAcd_yr(obj.getString("academic_yr"));
                                sharedClass.saveSharedPrefrences("role_id", obj.getString("role_id"));
                                progressBar.setVisibility(View.GONE);
                                Intent in = new Intent(LoginActivity.this, HomePageDrawerActivity.class);
                                in.putExtra("note_priority", note_priority);
                                in.putExtra("homework_priority", homework_priority);
                                in.putExtra("remark_priority", remark_priority);
                                in.putExtra("notice_priority", notice_priority);
                                in.putExtra("dailyattendance_prority", dailyattendance_prority);
                                in.putExtra("leave_priority", leave_priority);
                                in.putExtra("user_id", Uname);
                                in.putExtra("pwd", password);
                                startActivity(in);
                                finish();
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                String error_msg = obj.getString("error_msg");
                                Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", Uname);
                params.put("password", password);
                params.put("short_name", name);
                System.out.println("Credentials " + params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public boolean validate() {
        boolean b = true;

        if (edtusernm.getText().toString().equals("")) {
            b = false;
        }
        if (edtpass.getText().toString().equals("")) {
            b = false;
        }
        return b;
    }
}
