package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

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
import in.aceventura.evolvuschool.teacherapp.adapter.HomeworkAdapter;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.HomeworkPojo;


public class HomeworkActivity extends AppCompatActivity {
    ArrayList<HomeworkPojo> homeworkPojoArrayList;
    HomeworkAdapter homeworkAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    String name,newUrl,dUrl,academic_yr,reg_id;
    DatabaseHelper mDatabaseHelper;
    TextView nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

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
        ht_Teachernote.setText("Homeworks");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        FloatingActionButton fabcreate = findViewById(R.id.fabhomework);
        EditText edtsearch = findViewById(R.id.searchHomeworkEdt);
        recyclerView = findViewById(R.id.recyclerHome);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl=mDatabaseHelper.getURL(1);
        newUrl=mDatabaseHelper.getTURL(1);

        if (name ==null || name.equals("")){
            name = mDatabaseHelper.getName(1);
            dUrl=mDatabaseHelper.getURL(1);
            newUrl=mDatabaseHelper.getTURL(1);
        }
        nodata = findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        homeworkPojoArrayList = new ArrayList<>();
        homeworkAdapter = new HomeworkAdapter(this, homeworkPojoArrayList);

        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        recyclerView.setAdapter(homeworkAdapter);
        fabcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeworkActivity.this, CreateHomeworkActivity.class);
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            reg_id = bundle.getString("reg_id");
            academic_yr = bundle.getString("academic_yr");
        }
        getAllHomework();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
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
                        Intent intent = new Intent(HomeworkActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(HomeworkActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(HomeworkActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(HomeworkActivity.this, HomePageDrawerActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.contact, menu);

        //getting the search view from the menu
        MenuItem searchViewItem = menu.findItem(R.id.menuSearch);

        //getting search rvManager from systemservice
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        //getting the search view
        final SearchView searchView = (SearchView) searchViewItem.getActionView();

        //you can put a hint for the search input field
        searchView.setQueryHint("Search Subjects...");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //by setting it true we are making it iconified
        //so the search input will show up after taping the search iconified
        //if you want to make it visible all the time make it false
        searchView.setIconifiedByDefault(true);

        //here we will get the search query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //do the search here
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);

                return true;
            }
        });

        return true;
    }

    private void getAllHomework() {
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final HashMap<String, String> params = new HashMap<>();
        params.put("acd_yr", academic_yr);
        params.put("reg_id", reg_id);
        params.put("short_name", name);
        System.out.println(params);
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl+"AdminApi/"+"get_homework", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("ALLHOMEWORK"+ response);
                try {
                    if (response.getString("status").equals("true")) {
                        progressBar.setVisibility(View.GONE);
                        JSONArray jsonArray = response.getJSONArray("homework_details");
                        System.out.println("ALLHOMEWORK_ARRAY"+jsonArray);
                        for (int i = 0; i <jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String hmkid = jsonObject.getString("homework_id");
                            String description = jsonObject.getString("description");
                            String assigndate = jsonObject.getString("start_date");
                            String submissiondate = jsonObject.getString("end_date");
                            String teacher_id = jsonObject.getString("teacher_id");
                            String section_id = jsonObject.getString("section_id");
                            String subject_id = jsonObject.getString("sm_id");
                            String class_id = jsonObject.getString("class_id");
                            String classname = jsonObject.getString("cls_name");
                            String sectionname = jsonObject.getString("sec_name");
                            String subjectname = jsonObject.getString("sub_name");
                            String academic_yr = jsonObject.getString("academic_yr");
                            String publish = jsonObject.getString("publish");
                            String pubhDate = jsonObject.getString("publish_date");
                            String parent_comment_count = jsonObject.getString("comment_count");// TODO: 16-07-2020
                            if (!pubhDate.equals("null")){
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                Date date1 = null;
                                Date date2 = null;
                                Date date3 = null;
                                try {
                                    date1 = simpleDateFormat.parse(assigndate);
                                    date2 = simpleDateFormat.parse(submissiondate);
                                    date3 = simpleDateFormat.parse(pubhDate);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                String reqassigndate = newformat.format(date1);
                                String reqsubmitdate = newformat.format(date2);
                                String publishdate = newformat.format(date3);

                                HomeworkPojo homeworkPojo = new HomeworkPojo(classname, subjectname, reqassigndate, reqsubmitdate
                                        , hmkid, class_id, teacher_id, section_id, subject_id, description, academic_yr, publish, sectionname,publishdate, false,parent_comment_count);
                                homeworkPojoArrayList.add(homeworkPojo);
                                homeworkAdapter.notifyDataSetChanged();
                            }
                            else {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                Date date1 = null;
                                Date date2 = null;
                                try {
                                    date1 = simpleDateFormat.parse(assigndate);
                                    date2 = simpleDateFormat.parse(submissiondate);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                String reqassigndate = newformat.format(date1);
                                String reqsubmitdate = newformat.format(date2);
                                String publishdate = "";

                                HomeworkPojo homeworkPojo = new HomeworkPojo(classname, subjectname, reqassigndate, reqsubmitdate
                                        , hmkid, class_id, teacher_id, section_id, subject_id, description, academic_yr, publish, sectionname,publishdate, false,parent_comment_count);
                                homeworkPojoArrayList.add(homeworkPojo);
                                homeworkAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                        Toast.makeText(HomeworkActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                error.printStackTrace();
                nodata.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(jsonObjectRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(HomeworkActivity.this, HomePageDrawerActivity.class);
        intent.putExtra("reg_id",reg_id);
        intent.putExtra("academic_yr",academic_yr);
        startActivity(intent);
        HomeworkActivity.this.finish();
    }

    private void filter(String s) {
        ArrayList<HomeworkPojo> filtername = new ArrayList<>();

        for (HomeworkPojo subjectnm : homeworkPojoArrayList) {
            if (subjectnm.getSubjectnm().toLowerCase().contains(s.toLowerCase())) {
                filtername.add(subjectnm);
            }
        }

        homeworkAdapter.filterlist(filtername);
    }
}
