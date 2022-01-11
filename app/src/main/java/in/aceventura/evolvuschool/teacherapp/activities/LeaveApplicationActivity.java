package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.adapter.LeaveAdapter;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.LeavePojo;

public class LeaveApplicationActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton fab;
    ArrayList<LeavePojo> mArrayList;
    LeaveAdapter leaveAdapter;
    ImageView img;
    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private ProgressDialog progressDialog;
    private LeavePojo leavePojo;

    String name, dUrl, newUrl, teacher, reg_id, academic_yr;

    DatabaseHelper mDatabaseHelper;
    LeaveAdapter mHomeDataAdapter;

    ArrayList<LeavePojo> mSearchArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application);
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
        ht_Teachernote.setText("Leave Application");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        getId();

        teacher = getIntent().getStringExtra("teacher");
        recyclerView = findViewById(R.id.recyclerleave);


        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);


        LinearLayoutManager LayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(LayoutManager);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            reg_id = bundle.getString("reg_id");
            academic_yr = bundle.getString("academic_yr");
        }


        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "AdminApi/get_leave_applied_by_staff_info",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            System.out.println();
                            if (obj.optString("status").equals("true")) {
                                mSearchArrayList = new ArrayList<>();
                                JSONArray dataArrayinfo = obj.getJSONArray("leave_info");
                                for (int i = 0; i < dataArrayinfo.length(); i++) {
                                    LeavePojo searchModel = new LeavePojo();
                                    JSONObject dataobj = dataArrayinfo.getJSONObject(i);
                                    searchModel.setLeave_app_id(dataobj.getString("leave_app_id"));
                                    searchModel.setStaff_id(dataobj.getString("staff_id"));
                                    searchModel.setLeave_type_id(String.valueOf(dataobj.getInt("leave_type_id")));
                                    searchModel.setLeave_start_date(dataobj.getString("leave_start_date"));
                                    searchModel.setLeave_end_date(dataobj.getString("leave_end_date"));
                                    searchModel.setNo_of_date(dataobj.getString("no_of_days"));
                                    searchModel.setApproved_by(dataobj.getString("approved_by"));
                                    searchModel.setStatus(dataobj.getString("status"));
                                    searchModel.setReason(dataobj.getString("reason"));
                                    searchModel.setReason_for_rejection(dataobj.getString("reason_for_rejection"));
                                    searchModel.setAcademic_yr(dataobj.getString("academic_yr"));
                                    searchModel.setName(dataobj.getString("name"));
                                    mSearchArrayList.add(searchModel);
                                }

                                mHomeDataAdapter = new LeaveAdapter(mSearchArrayList);
                                recyclerView.setAdapter(mHomeDataAdapter);
                                mHomeDataAdapter.notifyDataSetChanged();
                                Log.i("", "LeaveListResponse: " + response);

                            } else {
                                Toast.makeText(LeaveApplicationActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
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

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                final HashMap<String, String> params = new HashMap<>();
                params.put("acd_yr", academic_yr);
                params.put("staff_id", reg_id);
                params.put("short_name", name);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveApplicationActivity.this);
        requestQueue.add(stringRequest);


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
                        Intent intent = new Intent(LeaveApplicationActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(LeaveApplicationActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(LeaveApplicationActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(LeaveApplicationActivity.this, HomePageDrawerActivity.class);
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

    private void getId() {

        fab = findViewById(R.id.fableave);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LeaveApplicationActivity.this, CreateLeaveApplicationActivity.class);
                in.putExtra("teacher", teacher);
                startActivity(in);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LeaveApplicationActivity.this, HomePageDrawerActivity.class);
        intent.putExtra("reg_id", reg_id);
        intent.putExtra("academic_yr", academic_yr);
        startActivity(intent);
        LeaveApplicationActivity.this.finish();
    }
}
