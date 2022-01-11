package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.Locale;

import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.adapter.NoticeStaffAdapter;
import in.aceventura.evolvuschool.teacherapp.adapter.NoticeStaffAdapter1;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.NoticeStaffPojo;
import in.aceventura.evolvuschool.teacherapp.pojo.NoticeStaffPojo1;

public class NoticeStaffActivity extends AppCompatActivity implements View.OnClickListener {
    TextView nDate, searchbtn;
    ProgressDialog progressDialog;

    ProgressBar progressBar;
    RequestQueue mQueue;
    public static ArrayList<NoticeStaffPojo> noticeStaffPojoArrayList = new ArrayList<>();
    public static ArrayList<NoticeStaffPojo1> noticeStaffPojoArrayList1 = new ArrayList<>();
    RecyclerView recycleridnotice1,recycleridnotice2;
    private String fDate;
    NoticeStaffAdapter noticeStaffAdapter;
    NoticeStaffAdapter1 noticeStaffAdapter1;
    String name,newUrl,academic_yr,reg_id,dUrl;
    LinearLayout l1,l2;
    DatabaseHelper mDatabaseHelper;
    TextView nodata;
    TextView nodata1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_staff);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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
        ht_Teachernote.setText("Notice/SMS for Staff");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        nodata = findViewById(R.id.nodata);
        nodata1 = findViewById(R.id.nodata1);
        nodata.setVisibility(View.GONE);
        nodata1.setVisibility(View.GONE);

        reg_id = (SharedClass.getInstance(this).getRegId().toString());
//        Sname = (SharedClass.getInstance(this).getShortname());
//        newUrl=(SharedClass.getInstance(this).getUrl());

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl=mDatabaseHelper.getURL(1);
        newUrl=mDatabaseHelper.getTURL(1);

        if (name ==null || name.equals("")){
            name = mDatabaseHelper.getName(1);
            dUrl=mDatabaseHelper.getURL(1);
            newUrl=mDatabaseHelper.getTURL(1);
        }

        l1=findViewById(R.id.l1);
        l2=findViewById(R.id.l2);
        l1.setVisibility(View.GONE);
        l2.setVisibility(View.GONE);



        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        Log.i("Data", "onCreate: "+ reg_id + name + academic_yr);

        mQueue= Volley.newRequestQueue(this);
//        progressDialog = new ProgressDialog(this);

        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        nDate = findViewById(R.id.nDate);
        searchbtn = findViewById(R.id.searchbtn);
        recycleridnotice1=findViewById(R.id.recycleridnotice1);
        recycleridnotice2=findViewById(R.id.recycleridnotice2);
//        noticeStaffAdapter = new NoticeStaffAdapter(NoticeStaffActivity.this, noticeStaffPojoArrayList);
       noticeStaffPojoArrayList=new ArrayList<>();
       noticeStaffPojoArrayList1=new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recycleridnotice1.setLayoutManager(linearLayoutManager);
        recycleridnotice1.setItemAnimator(new DefaultItemAnimator());
        recycleridnotice1.setAdapter(new NoticeStaffAdapter(getApplicationContext(),noticeStaffPojoArrayList));

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager2.setSmoothScrollbarEnabled(true);
        recycleridnotice2.setLayoutManager(linearLayoutManager2);
        recycleridnotice2.setItemAnimator(new DefaultItemAnimator());
        recycleridnotice2.setAdapter(new NoticeStaffAdapter1(getApplicationContext(),noticeStaffPojoArrayList1));


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
                        Intent intent = new Intent(NoticeStaffActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(NoticeStaffActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(NoticeStaffActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(NoticeStaffActivity.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }

        //method to get all notices/sms
        getNoticeSms(reg_id, name, academic_yr);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.YEAR, year);

                    updateLabel();



                ///
    }

            //get the notice using date filter
       private void updateLabel() {
                l1.setVisibility(View.GONE);
                l2.setVisibility(View.VISIBLE);
                String myFormat = "dd-MM-yyyy"; //format in which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                nDate.setText(sdf.format(myCalendar.getTime()));
                fDate = nDate.getText().toString();

           //calling api for all notices on Create on this activity
           final HashMap<String, String> params = new HashMap<>();
           params.put("teacher_id",reg_id);
           params.put("academic_yr", academic_yr);
           params.put("notice_date", fDate);
           params.put("short_name",name);
           System.out.println(params);

           progressBar.setVisibility(View.VISIBLE);
           JsonObjectRequest request = new JsonObjectRequest(newUrl+"AdminApi/"+"get_notice_sms_for_staff", new JSONObject(params),
                   new Response.Listener<JSONObject>() {
                       @Override
                       public void onResponse(JSONObject response) {
                           Log.i("RESPONSE", "onResponse: "+response);
                           if (response !=null) {
                               progressBar.setVisibility(View.GONE);

                               try {
                                   if (response.getString("status").equals("true")) {
                                       recycleridnotice2.setVisibility(View.VISIBLE);
                                       nodata1.setVisibility(View.GONE);
                                       noticeStaffPojoArrayList1.clear();
                                       JSONArray jsonArray = response.getJSONArray("notice_sms_all");
                                       for (int i = 0; i <jsonArray.length(); i++) {
                                           JSONObject jsonObject = jsonArray.getJSONObject(i);
                                           String notice_id = jsonObject.getString("t_notice_id");
                                           String subject = jsonObject.getString("subject");
                                           String notice_desc = jsonObject.getString("notice_desc");
                                           String notice_date = jsonObject.getString("notice_date");
                                           String notice_type = jsonObject.getString("notice_type");
                                           String teacher_id = jsonObject.getString("teacher_id");
                                           String academic_yr = jsonObject.getString("academic_yr");
                                           String publish = jsonObject.getString("publish");
                                           String name = jsonObject.getString("name");

                                           SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                           Date date1=null;
                                           try {
                                               date1 = simpleDateFormat.parse(notice_date);
                                           } catch (ParseException e) {
                                               e.printStackTrace();
                                           }
                                           SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                           String reqNoticedate = newformat.format(date1);
                                               NoticeStaffPojo1 noticeStaffPojo1 = new NoticeStaffPojo1(notice_id, subject, notice_desc, reqNoticedate,notice_type,name);
                                               noticeStaffPojoArrayList1.add(noticeStaffPojo1);
                                               recycleridnotice2.setAdapter(new NoticeStaffAdapter1(getApplicationContext(), noticeStaffPojoArrayList1));

                                           int s = noticeStaffPojoArrayList1.size();

                                       }
                                   } else {
                                       progressBar.setVisibility(View.GONE);
                                       nodata1.setVisibility(View.VISIBLE);
                                       nodata.setVisibility(View.GONE);
                                       recycleridnotice1.setVisibility(View.GONE);
                                       recycleridnotice2.setVisibility(View.GONE);
                                   }

                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }
                           else {
                               progressBar.setVisibility(View.GONE);
                               nodata1.setVisibility(View.VISIBLE);
                               nodata.setVisibility(View.GONE);
                               recycleridnotice1.setVisibility(View.GONE);
                               recycleridnotice2.setVisibility(View.GONE);
                               Toast.makeText(NoticeStaffActivity.this, "NO RESPONSE FROM SERVER", Toast.LENGTH_SHORT).show();
                           }
                       }
                   }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   progressBar.setVisibility(View.GONE);
                   nodata1.setVisibility(View.VISIBLE);
                   nodata.setVisibility(View.GONE);
                   recycleridnotice1.setVisibility(View.GONE);
                   recycleridnotice2.setVisibility(View.GONE);
                   Toast.makeText(NoticeStaffActivity.this, "No Notice/SMS", Toast.LENGTH_SHORT).show();
               }
           });
           mQueue.add(request);
            }
//            */
       };

        //On click of Date Filter
        nDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NoticeStaffActivity.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }


    //get all notices...
    private void getNoticeSms(String reg_id, String Sname, String academic_yr) {
        l1.setVisibility(View.VISIBLE);
        l2.setVisibility(View.GONE);
        final HashMap<String, String> params = new HashMap<>();
        params.put("teacher_id",reg_id);
        params.put("academic_yr", academic_yr);
        params.put("short_name",Sname);
        System.out.println(params);

        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest request1 = new JsonObjectRequest(newUrl+"AdminApi/"+"get_notice_sms_for_staff", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("RESPONSE", "onResponse: "+response);
                        if (response !=null) {
//                            progressDialog.dismiss();
                            progressBar.setVisibility(View.GONE);
                            try {
                                if (response.getString("status").equals("true")) {
                                    noticeStaffPojoArrayList.clear();
                                    JSONArray jsonArray = response.getJSONArray("notice_sms_all");
//                                    JSONArray jsonArray = response.getJSONArray("notice_sms");
                                    for (int i = 0; i <jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String notice_id = jsonObject.getString("t_notice_id");
                                        String subject = jsonObject.getString("subject");
                                        String notice_desc = jsonObject.getString("notice_desc");
                                        String notice_date = jsonObject.getString("notice_date");
//                                      String class_id = jsonObject.getString("class_id");
                                        String notice_type = jsonObject.getString("notice_type");
                                        String teacher_id = jsonObject.getString("teacher_id");
                                        String academic_yr = jsonObject.getString("academic_yr");
                                        String publish = jsonObject.getString("publish");
                                        String name = jsonObject.getString("name");

                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                        Date date1=null;
                                        try {
                                            date1 = simpleDateFormat.parse(notice_date);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                        String reqNoticedate = newformat.format(date1);

                                        NoticeStaffPojo noticeStaffPojo = new NoticeStaffPojo(notice_id, subject, notice_desc, reqNoticedate,notice_type,name);

                                        noticeStaffPojoArrayList.add(noticeStaffPojo);
                                        int s = noticeStaffPojoArrayList.size();
//                                        System.out.println(s);

//                                noticeStaffAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    nodata.setVisibility(View.VISIBLE);
                                    nodata1.setVisibility(View.GONE);

//
//                                    progressDialog.dismiss();
                                    progressBar.setVisibility(View.GONE);
//                                    Toast.makeText(NoticeStaffActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            nodata.setVisibility(View.VISIBLE);
                            nodata1.setVisibility(View.GONE);
//                            Toast.makeText(NoticeStaffActivity.this, "NO RESPONSE FROM SERVER", Toast.LENGTH_SHORT).show();
                        }
                        recycleridnotice1.setAdapter(new NoticeStaffAdapter(getApplicationContext(), noticeStaffPojoArrayList));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
                nodata1.setVisibility(View.GONE);
//                Toast.makeText(NoticeStaffActivity.this, "No Notice/SMS", Toast.LENGTH_SHORT).show();

            }
        });
        mQueue.add(request1);
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        NoticeStaffActivity.this.finish();
    }
    @Override
    public void onClick(View v) {

    }
}