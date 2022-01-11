package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.adapter.NoticeAdapter;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.DBManager;
import in.aceventura.evolvuschool.teacherapp.pojo.NoticePojo;
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


public class NoticeActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner classSpinner;
    private ArrayList<String> listClass;
    String class_id, section_id;
    private JSONArray result;
    private ProgressDialog progressDialog;

    ProgressBar progressBar;
    private NoticeAdapter noticeAdapter;
    private ArrayList<NoticePojo> noticePojoArrayList;
    public static String url;
    TextView txtsearch;
    String clssnm;
    private DBManager dbManager;
    DBConnection dbConnection;
    TextView txturl,nodata;
    Button btnadd;
    String name,newUrl,dUrl;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        btnadd=findViewById(R.id.addtoexcel);
        nodata = findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);



        getSupportActionBar().hide();
        View tb_main1 = findViewById(R.id.icd_tb_homeworkdetails);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("("+SharedClass.getInstance(getApplicationContext()).getAcademicYear()+")");

        school_title.setText(" Evolvu Teacher App");
        ht_Teachernote.setText("Notice/SMS for Parents");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        dbConnection=new DBConnection(this);
        RecyclerView recyclerView = findViewById(R.id.recycleridnotice);
        txtsearch=findViewById(R.id.searchbtn);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        noticePojoArrayList=new ArrayList<>();
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


        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        noticeAdapter=new NoticeAdapter(NoticeActivity.this,noticePojoArrayList);
        recyclerView.setAdapter(noticeAdapter);
        result = new JSONArray();
        classSpinner = findViewById(R.id.classspinnotice);
        listClass = new ArrayList<>();

//        listClass.add("Select Class");
        final ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, listClass);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classAdapter.notifyDataSetChanged();
//        classSpinner.setAdapter(classAdapter);
        try {
            classSpinner.setOnTouchListener(classSpinnerOnTouch);

        } catch (Exception e) {

        }
        SharedClass sh = new SharedClass(this);
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());

//        progressDialog = new ProgressDialog(this);
        try {
            classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {   //String option;

                    //listClass.clear();
                    //notifyAll();

                    //t1.setText(getClassId(position));
                    //t2.setText(getSectionId(position));
                    if (classSpinner.getSelectedItem()=="Select Class"){
                        return;
                    }else
                        {
                            class_id = getClassId(position);
                            section_id = getSectionId(position);
                        }


//                    classnm =  classSpinner.getItemAtPosition(classSpinner.getSelectedItemPosition()).toString();

                    //Toast.makeText(getApplicationContext(), "Class: " + class_id + " Section :" + section_id, Toast.LENGTH_LONG).show();;
                    //getData1(getClassId(position), getSectionId(position));

                  //  classSpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listClass));
                } // to close the onItemSelected

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            class_id=getIntent().getStringExtra("classid");
            //viewNotice(class_id);
        }
      catch (Exception e){
            e.printStackTrace();
      }
        noticePojoArrayList.clear();

        txtsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                noticePojoArrayList.clear();

                if (validate()){
                    noticePojoArrayList.clear();
                    viewNotice(class_id);
                }
                else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(NoticeActivity.this);
                    {
                        builder.setCancelable(true);
                        builder.setTitle("Alert");
                        builder.setMessage("Select Class");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                        builder.create().show();
                        //Toast.makeText(this, "Fill All * Fields", Toast.LENGTH_SHORT).show();

                    }
                  //  Toast.makeText(NoticeActivity.this, "Select Class", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtoexcel();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
                        Intent intent = new Intent(NoticeActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(NoticeActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(NoticeActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(NoticeActivity.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }

    }


    public void addtoexcel(){



    }
    public boolean validate(){
        boolean b=true;

        if (classSpinner.getSelectedItemPosition()<0){
            b=false;
        }

        return b;
    }

    private void viewNotice(String class_id) {
        noticePojoArrayList.clear();

        final String reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        HashMap<String, String> params = new HashMap<>();
        params.put("academic_yr", academic_yr);
        params.put("class_id", class_id);
        params.put("short_name",name);
        System.out.println(params);
//        progressDialog.setMessage("Fetching Notices...");
//        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl+"AdminApi/" + "get_notices_classwise", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

//                progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                try {

                    if (response.getString("status").equals("true")) {
                        noticePojoArrayList.clear();
                        JSONArray jsonArray = response.getJSONArray("notices");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String notice_id = jsonObject.getString("notice_id");
                            String subject = jsonObject.getString("subject");
                            String notice_desc = jsonObject.getString("notice_desc");
                            String notice_date = jsonObject.getString("notice_date");
                            String start_date = jsonObject.getString("start_date");
                            String end_date = jsonObject.getString("end_date");
                            String class_id = jsonObject.getString("class_id");
                            String section_id = jsonObject.getString("section_id");
                            String notice_type = jsonObject.getString("notice_type");
                            String teacher_id = jsonObject.getString("teacher_id");
                            String start_time = jsonObject.getString("start_time");
                            String end_time = jsonObject.getString("end_time");
                            String academic_yr = jsonObject.getString("academic_yr");
                            String publish = jsonObject.getString("publish");
                            String name = jsonObject.getString("name");

/*
                            String imagename = jsonObject.getString("image_name");
*/
                            String classname = jsonObject.getString("classname");
                           // String detailnoticeid = jsonObject.getString("detailnoticeid");

                           /* txturl.setText(urlname);

                            url=urlname;*/
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date date1=null;
//                            Date date2=null;
//                            Date date3=null;

                            try {
                                date1 = simpleDateFormat.parse(notice_date);
//                                date3 = simpleDateFormat.parse(end_date);
//                                date2 = simpleDateFormat.parse(start_date);
//
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            //HH:mm:ss.SSS'Z'
                            SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            String reqNoticedate = newformat.format(date1);
//                            String reqStartdate = newformat.format(date2);
//                            String reqEnddate = newformat.format(date3);
                            NoticePojo noticePojo = new NoticePojo(notice_id, subject, notice_desc, reqNoticedate,
                                    class_id,notice_type, teacher_id, academic_yr, publish,name,classname);

                          //  NoticePojo noticePojonew = new NoticePojo(notice_id, subject);

//                            noticePojoArrayList.clear();
                            noticePojoArrayList.add(noticePojo);
                            noticeAdapter.notifyDataSetChanged();

                        }
                    } else {
                        nodata.setVisibility(View.VISIBLE);
//                        Toast.makeText(NoticeActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //classSpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listClass));

            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                Toast.makeText(NoticeActivity.this, "Select Class", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(jsonObjectRequest);
    }

    private String getClassId(int position) {
        String classs = "";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            classs = json.getString("class_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return classs;
    }
    private String getSectionId(int position) {
        String section = "";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);
            //Fetching name from that object

//                section = json.getString("section_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return section;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        NoticeActivity.this.finish();
    }

    private View.OnTouchListener classSpinnerOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                //Your code
                listClass.clear();
                getClasses();

            }
            return false;
        }
    };

    private void getClasses() {

        final String reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        HashMap<String, String> params = new HashMap<>();
        params.put("academic_yr", academic_yr);
        params.put("reg_id", reg_id);
        params.put("short_name", name);


//        progressDialog.setMessage("Loading Classes...");
//        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl+"AdminApi/" + "getClassOnly_for_teacher", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

//                progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                try {

                    if (response.getString("status").equals("true")) {
                        result = response.getJSONArray("class_name");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject jsonObject = result.getJSONObject(i);
                            String a = jsonObject.getString("classname");
                            class_id = jsonObject.getString("class_id");
                            //section_id = jsonObject.getString("section_id");
                            listClass.add(a);
                            clssnm=a;
                            // FancyToast.makeText(CreateTeachernote.this,"Hello World !",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true);
                        }
                    } else {
                        Toast.makeText(NoticeActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                classSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.mytextview, listClass));

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View view) {
    }

    public static String getSelectedHomeworkStatus(){
        return url;
    }
}
