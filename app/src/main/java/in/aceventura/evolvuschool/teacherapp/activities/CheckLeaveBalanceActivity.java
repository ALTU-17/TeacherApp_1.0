package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.adapter.CheckLeaveBalanceAdapter;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.Checkbalance;
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

public class CheckLeaveBalanceActivity extends AppCompatActivity {
private CheckLeaveBalanceAdapter checkLeaveBalanceAdapter;
private ArrayList<Checkbalance> checkbalanceArrayList;
private RecyclerView recyclerView;
private Context context;
private RequestQueue requestQueue;
private ProgressDialog progressDialog;
ProgressBar progressBar;
private TextView txtStaffnanme;
private LinearLayoutManager linearLayoutManager;
public String staffname,name,newUrl,dUrl;
DatabaseHelper mDatabaseHelper;
SharedClass sharedClass;
 String details;
 String teacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_leave_balance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportActionBar().hide();
        View tb_main1 = findViewById(R.id.icd_tb_homeworkdetails);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("("+SharedClass.getInstance(getApplicationContext()).getAcademicYear()+")");

        school_title.setText(" Evolvu Teacher App");
        ht_Teachernote.setText("Leave Balance");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        requestQueue= Volley.newRequestQueue(getBaseContext());
        checkbalanceArrayList=new ArrayList<>();
        sharedClass=new SharedClass(this);
        txtStaffnanme=findViewById(R.id.staffname);

        recyclerView=findViewById(R.id.recyclerbalance);
        linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        checkLeaveBalanceAdapter=new CheckLeaveBalanceAdapter(this,checkbalanceArrayList);
        recyclerView.setAdapter(checkLeaveBalanceAdapter);
        final String reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl=mDatabaseHelper.getURL(1);
        newUrl=mDatabaseHelper.getTURL(1);

        if (name ==null || name.equals("")){
            name = mDatabaseHelper.getName(1);
            dUrl=mDatabaseHelper.getURL(1);
            newUrl=mDatabaseHelper.getTURL(1);
        }
        getLeaveBalance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        String details =  newUrl + "AdminApi/get_teacher";
        //Api for TeacherName
       /* HashMap<String, String> params3 = new HashMap<>();
        params3.put("reg_id", reg_id);
        params3.put("short_name", name);
        System.out.println(params3);
        JsonObjectRequest jsonObjectRequest4 = new JsonObjectRequest(nameUrl, new JSONObject(params3), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Gender "+response);
//              Toast.makeText(HomePageDrawerActivity.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();

                if (response != null) {
                    Log.i("Gender Respose", "sex "+ response);
                    try {
                        if (response.getString("status").equals("true")) {
                            teacher = response.getString("teacher_name");
                        }
                        else {
//                            Toast.makeText(HomePageDrawerActivity.this, "No Class Found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                tlogo.setImageResource(R.drawable.teacher);
            }
        });
        RequestQueue requestQueue4 = Volley.newRequestQueue(this);
        requestQueue4.add(jsonObjectRequest4);
*/

        /*HashMap<String, String> params3 = new HashMap<>();
        params3.put("academic_yr", academic_yr);
        params3.put("reg_id", reg_id);
        params3.put("short_name", name);
        System.out.println(params3);
        JsonObjectRequest jsonObjectRequest4 = new JsonObjectRequest(details, new JSONObject(params3), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("TeacherName "+response);
//              Toast.makeText(HomePageDrawerActivity.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();

                if (response != null) {
                    try {
                        if (response.getString("status").equals("true")) {
                            JSONArray result = response.getJSONArray("class");
                            JSONObject jsonObject = result.getJSONObject(0);
                            String a = jsonObject.getString("classname");
                            String b = jsonObject.getString("sectionname");
                            String class_id = jsonObject.getString("class_id");
                            String section_id = jsonObject.getString("section_id");
                            teacher = response.getString("teacher_name");
                            txtStaffnanme.setText("Staff Name : " +teacher);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue4 = Volley.newRequestQueue(this);
        requestQueue4.add(jsonObjectRequest4);

*/


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
                        Intent intent = new Intent(CheckLeaveBalanceActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(CheckLeaveBalanceActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(CheckLeaveBalanceActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(CheckLeaveBalanceActivity.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
    }






    private void getLeaveBalance() {
//        progressDialog.setMessage("Loading..");
//        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);

        final String reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                newUrl+"AdminApi/"+"get_balance_leave",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        progressDialog.dismiss();
                            progressBar.setVisibility(View.GONE);
                        try {

                            if (response!=null){
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("balance_leave");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String leaveid=jsonObject1.getString("leave_type_id");
                                    String leavetype=jsonObject1.getString("name");
                                    String staffid=jsonObject1.getString("staff_id");
                                    String leavealloted=jsonObject1.getString("leaves_allocated");
                                    String leaveavailed=jsonObject1.getString("leaves_availed");

                                    Checkbalance checkbalance=new Checkbalance(leaveid,leavetype,staffid,leavealloted,leaveavailed);
                                    checkbalanceArrayList.add(checkbalance);
                                    checkLeaveBalanceAdapter.notifyDataSetChanged();
                                }
                            }
                            else {
                                Toast.makeText(context, "No Record Found", Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                final HashMap<String, String> params = new HashMap<>();
                params.put("acd_yr", academic_yr);
                params.put("reg_id", reg_id);
                params.put("short_name",name);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
     finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}
