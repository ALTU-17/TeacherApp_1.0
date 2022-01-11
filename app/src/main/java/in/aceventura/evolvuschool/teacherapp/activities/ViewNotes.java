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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

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
import in.aceventura.evolvuschool.teacherapp.adapter.VewOnlyNoteAdapter;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.Viewonlynotespojo;

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

public class ViewNotes extends AppCompatActivity {
    ArrayList<Viewonlynotespojo> viewonlynotespojoArrayList;
    private ArrayList<String> listClass;
    String class_id, section_id;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    ProgressBar progressBar;
    private Spinner spinclass;
    VewOnlyNoteAdapter vewOnlyNoteAdapter;
    private JSONArray result;
    String clssnm,name,newUrl,dUrl,prourl;
    DatabaseHelper mDatabaseHelper;
    TextView nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);


        getSupportActionBar().hide();
        View tb_main1 = findViewById(R.id.icd_tb_homeworkdetails);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("("+SharedClass.getInstance(getApplicationContext()).getAcademicYear()+")");

        school_title.setText(" Evolvu Teacher App");
        ht_Teachernote.setText("View Notes");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getId();
        progressBar.setVisibility(View.GONE);
        nodata = findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl=mDatabaseHelper.getURL(1);
        newUrl=mDatabaseHelper.getTURL(1);
        prourl = mDatabaseHelper.getPURL(1);


        if (name ==null || name.equals("")){
            name = mDatabaseHelper.getName(1);
            dUrl=mDatabaseHelper.getURL(1);
            newUrl=mDatabaseHelper.getTURL(1);
            prourl = mDatabaseHelper.getPURL(1);

        }

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
                        Intent intent = new Intent(ViewNotes.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(ViewNotes.this, MyCalendar.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(ViewNotes.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(ViewNotes.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }

    }
    private void getId(){
        RecyclerView recyclerView = findViewById(R.id.recycleridnotes);
        spinclass=findViewById(R.id.classspinnotes);
        TextView btnsearch = findViewById(R.id.searchbtnnotes);
        name = (SharedClass.getInstance(this).getShortname());
        newUrl=(SharedClass.getInstance(this).getUrl());
//        progressDialog=new ProgressDialog(this);
        progressBar=findViewById(R.id.progressBar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        viewonlynotespojoArrayList=new ArrayList<>();
        vewOnlyNoteAdapter=new VewOnlyNoteAdapter(this,viewonlynotespojoArrayList);
        requestQueue=Volley.newRequestQueue(getBaseContext());
        recyclerView.setAdapter(vewOnlyNoteAdapter);
        listClass = new ArrayList<>();
        spinclass.setOnTouchListener(classSpinnerOnTouch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewonlynotespojoArrayList.clear();
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewonlynotespojoArrayList.clear();

                if (validate()){
                    viewNotes(class_id,section_id);

                }
                else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewNotes.this);
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


    }

    private void viewNotes(String class_id,String sectionid) {
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        HashMap<String, String> params = new HashMap<>();
        params.put("academic_yr", academic_yr);
        params.put("class_id", class_id);
        params.put("section_id", sectionid);
        params.put("short_name", name);
        System.out.println(params);
//        progressDialog.setMessage("Fetching Notes...");
//        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl+"AdminApi/"+ "get_notes_classwise",
                new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("ViewNotes", "onResponse: "+response);
//                progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                try {

                    if (response.getString("status").equals("true")) {
                        viewonlynotespojoArrayList.clear();
                        JSONArray jsonArray = response.getJSONArray("notes");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String notes_id = jsonObject.getString("notes_id");
                            String subjectname = jsonObject.getString("sub_name");
                            String sectionname = jsonObject.getString("sec_name");
                            String clasname = jsonObject.getString("class_name");
                            String notice_desc = jsonObject.getString("description");
                            String notes_date = jsonObject.getString("date");
                            String classid = jsonObject.getString("class_id");
                            String sectionid = jsonObject.getString("section_id");
                            String subjectid = jsonObject.getString("subject_id");
                            String publish = jsonObject.getString("publish");
                            String publishDate = jsonObject.getString("publish_date");

/*
                            String imagename = jsonObject.getString("image_name");
*/
                            // String detailnoticeid = jsonObject.getString("detailnoticeid");

                           /* txturl.setText(urlname);

                            url=urlname;*/
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date date1=null;
                            Date date2=null;
                            Date date3=null;

                            try {
                                date1 = simpleDateFormat.parse(notes_date);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            //HH:mm:ss.SSS'Z'
                            SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            String reqNoticedate = newformat.format(date1);
                            Viewonlynotespojo viewonlynotespojo = new Viewonlynotespojo(notes_id, subjectname, sectionname, clasname,
                                    notice_desc,reqNoticedate,classid,
                                    sectionid, subjectid, publish,publishDate);

                            //  NoticePojo noticePojonew = new NoticePojo(notice_id, subject);

                            //  dbConnection.insertReport(noticePojonew);
                            viewonlynotespojoArrayList.add(viewonlynotespojo);
                            vewOnlyNoteAdapter.notifyDataSetChanged();


                        }
                    } else {
                        nodata.setVisibility(View.VISIBLE);
//                        Toast.makeText(ViewNotes.this, "No Record Found", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(ViewNotes.this, "Select Class", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

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
    public boolean validate(){
        boolean b=true;

        if (spinclass.getSelectedItemPosition()<0){
            //edtusernm.setError("Enter Username");
            b=false;
        }

        return b;
    }

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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl+"AdminApi/" + "getClassAndSection_classteacheralloted", new JSONObject(params), new Response.Listener<JSONObject>() {
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
                            String b = jsonObject.getString("sectionname");
                            class_id = jsonObject.getString("class_id");
                            section_id = jsonObject.getString("section_id");
                            listClass.add(a + b);
                            clssnm = a;
                        }
                    } else {

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ViewNotes.this);
                        {
                            builder.setCancelable(true);
                            builder.setTitle("Alert");
                            builder.setMessage("This feature is applicable only for class teacher");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });

                            builder.create().show();
                            //Toast.makeText(CreateRemarkActivity.this, "Fill All * Fields", Toast.LENGTH_SHORT).show();

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinclass.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.mytextview, listClass));

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
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        finish();
        onBackPressed();
        return true;
    }

}
