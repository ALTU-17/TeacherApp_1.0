package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.CHangePasswordpojo;

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


public class ChangePasswordActivity extends AppCompatActivity {
    private EditText edt_motherName, edt_currentPwd, edt_newPwd, edt_renewPwd;
    private String userid;
    private String password;
    private String password_new;
    RequestQueue requestQueue;
    SharedClass sharedClass;
    Context context;
    private ProgressDialog progressDialog;
    DatabaseHelper mDatabaseHelper;
    String Sname, newUrl, name, dUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getId();
        getSupportActionBar().hide();
        View tb_main1 = findViewById(R.id.icd_tb_homeworkdetails);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("("+SharedClass.getInstance(getApplicationContext()).getAcademicYear()+")");

        school_title.setText(" Evolvu Teacher App");
        ht_Teachernote.setText("Change Password");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        context = this;
        requestQueue = Volley.newRequestQueue(getBaseContext());
        sharedClass = new SharedClass(context);
        progressDialog = new ProgressDialog(context);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);
        userid = SharedClass.getInstance(this).getUserName(); // getting user_id

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
                        Intent intent = new Intent(ChangePasswordActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(ChangePasswordActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(ChangePasswordActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(ChangePasswordActivity.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }

    }


    private void getId() {
        edt_motherName = findViewById(R.id.edt_motherName);
        edt_currentPwd = findViewById(R.id.edt_currentPwd);
        edt_newPwd = findViewById(R.id.edt_newPwd);
        edt_renewPwd = findViewById(R.id.edt_renewPwd);
        Button update = findViewById(R.id.btn_updatePwd);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set();
                String rePwd = edt_renewPwd.getText().toString();
                String currentPwd = edt_currentPwd.getText().toString();
                String newPwd = edt_newPwd.getText().toString();
                password_new = edt_newPwd.getText().toString();

                if (edt_motherName.getText().toString().equals("")) {
                    edt_motherName.setError("Enter Mother's Name");
                    edt_motherName.requestFocus();
                } else if (edt_currentPwd.getText().toString().equals("")) {
                    edt_currentPwd.setError("Enter Current Password");
                    edt_currentPwd.requestFocus();
                } else if (edt_newPwd.getText().toString().equals("")) {
                    edt_newPwd.setError("Enter New Password");
                    edt_newPwd.requestFocus();
                }else if (edt_renewPwd.getText().toString().equals("")) {
                    edt_renewPwd.setError("Enter Re-enter Password");
                    edt_renewPwd.requestFocus();
                }


                else if (edt_newPwd.length() < 8) {
                    edt_newPwd.requestFocus();
                    Toast.makeText(context, "New Password must be between 8 - 20 characters ", Toast.LENGTH_SHORT).show();
                } else if (edt_newPwd.length() > 20) {
                    Toast.makeText(context, "New Password must be between 8 - 20 characters ", Toast.LENGTH_SHORT).show();
                    edt_newPwd.requestFocus();
                } else if (currentPwd.equals(newPwd)) {
                    Toast.makeText(context, "Old Password & New Password cannot be same.", Toast.LENGTH_SHORT).show();
                } else if (!newPwd.equals(rePwd)) {
                    Toast.makeText(context, "New Password & ReEnter Password must be same.", Toast.LENGTH_SHORT).show();
                } else if (!validatepass(password_new)) {
                    edt_newPwd.requestFocus();
                    edt_newPwd.setError("Password must contain 8 to 20 characters,number and one special character i.e !@#$%");
                } else {
                    Changepassword();
                }

            }
        });
    }

    private void Changepassword() {
        String motherName = edt_motherName.getText().toString();
        String password_old = edt_currentPwd.getText().toString();
        password_new = edt_newPwd.getText().toString();
        String password_re_enter = edt_renewPwd.getText().toString();
        progressDialog.setMessage("Changing Password...");

        progressDialog.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("short_name", name);
        params.put("answerone", motherName);
        params.put("user_id", userid);
        params.put("password_new", password_new);
        params.put("password_old", password_old);
        params.put("password_re", password_re_enter);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/change_password", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(ChangePasswordActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                System.out.println(response.toString());
                progressDialog.dismiss();
                try {
                    if (response.getString("status").equals("true")) {
                        String success_msg = response.getString("success_msg");
                        Toast.makeText(ChangePasswordActivity.this, success_msg, Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(ChangePasswordActivity.this, HomePageDrawerActivity.class);
                        sharedClass.saveSharedPrefrences("password", password_new);
                        startActivity(in);
                        finish();
                    } else {
                        String error_msg = response.getString("error_msg");
                        Toast.makeText(ChangePasswordActivity.this, error_msg + "  Try Again...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChangePasswordActivity.this, "Unable to change the password", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public boolean validate() {
        String rePwd = edt_renewPwd.getText().toString();
        String currentPwd = edt_currentPwd.getText().toString();
        String newPwd = edt_newPwd.getText().toString();
        boolean b = true;
        if (edt_motherName.getText().toString().equals("")) {
            edt_motherName.setError("Enter Mother's Name");
            Toast.makeText(context, "Enter Mother's Name", Toast.LENGTH_SHORT).show();
            edt_motherName.requestFocus();
            b = false;
        } else if (edt_currentPwd.getText().toString().equals("")) {
            b = false;
        } else if (edt_newPwd.getText().toString().equals("")) {
            b = false;
        } else if (edt_newPwd.length() < 8) {
            b = false;
            Toast.makeText(context, "Password must be between 8 - 20 characters ", Toast.LENGTH_SHORT).show();
        } else if (edt_newPwd.length() > 20) {
            b = false;
            Toast.makeText(context, "Password must be between 8 - 20 characters ", Toast.LENGTH_SHORT).show();
        } else if (edt_newPwd.getText().toString().length() < 8) {
            b = false;
        } else if (edt_renewPwd.getText().toString().equals("")) {
            b = false;
        } else if (currentPwd.equals(newPwd)) {
            Toast.makeText(context, "Old Password & New Password cannot be same.", Toast.LENGTH_SHORT).show();
            b = false;
        } else if (!newPwd.equals(rePwd)) {
            Toast.makeText(context, "New Password & ReEnter Password must be same.", Toast.LENGTH_SHORT).show();
            b = false;

        }
        return b;
    }

    public boolean validatepassNewRe() {
        boolean b = true;
        if (!edt_newPwd.getText().toString().equals(edt_renewPwd.getText().toString())) {
            edt_renewPwd.setError("Enter Correct password");
            return false;
        }


        return b;
    }

    public boolean validatepass(final String pass) {
        String PASSWORD_PATTERN =
                "((?=.*\\d)(?=.*[@#$%]).{8,20})";

        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(pass);
        return matcher.matches();


    }

    public void set() {
        CHangePasswordpojo cHangePasswordpojo = new CHangePasswordpojo();

        cHangePasswordpojo.setUser_id(userid);
        cHangePasswordpojo.setPassword(password);
        cHangePasswordpojo.setAnswerone(edt_motherName.getText().toString());
        cHangePasswordpojo.setPassword(edt_currentPwd.getText().toString());
        cHangePasswordpojo.setPassword_new(edt_newPwd.getText().toString());
        cHangePasswordpojo.setPassword_re(edt_renewPwd.getText().toString());


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
