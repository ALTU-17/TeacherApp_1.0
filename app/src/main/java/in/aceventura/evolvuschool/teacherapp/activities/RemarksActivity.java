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

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
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

import in.aceventura.evolvuschool.teacherapp.Config;
import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.adapter.RemarkAdapter;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.RemarkPojo;

public class RemarksActivity extends AppCompatActivity {
    String read_status;
    ArrayList<RemarkPojo> remarkPojoArrayList;
    RemarkAdapter remarkAdapter;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    RequestQueue requestQueue1;
    String name, newUrl, dUrl, reg_id, academic_yr, firstnm, lastnm, midlnm, subnm, remark_type, rmkid, description,
            rmksub,
            rmkdate,
            classid,
            section_id,
            studid, subject_id, teacherid, acdyr, reqremarkformatdate, publish, ack, clsnm, secnm;
    DatabaseHelper mDatabaseHelper;
    TextView nodata;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remarks);
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
        ht_Teachernote.setText("Remarks");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        FloatingActionButton fabremark = findViewById(R.id.fabrmk);
        recyclerView = findViewById(R.id.recyclerremk);
        EditText edtsearch = findViewById(R.id.searchEdtremk);
        nodata = findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);
        requestQueue1 = Volley.newRequestQueue(this);
        progressBar = findViewById(R.id.progressBar);
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

        remarkPojoArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);


        fabremark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RemarksActivity.this, CreateRemarkActivity.class);
                startActivity(intent);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            reg_id = bundle.getString("reg_id");
            academic_yr = bundle.getString("academic_yr");
        }
        getAllRemarks();

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
                        Intent intent = new Intent(RemarksActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(RemarksActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(RemarksActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(RemarksActivity.this, HomePageDrawerActivity.class);
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
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

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

    private void getAllRemarks() {


        String versionName = Config.LOCAL_ANDROID_VERSION; // new 18-02-2020
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final HashMap<String, String> params = new HashMap<>();
        params.put("academic_yr", academic_yr);
        params.put("reg_id", reg_id);
        params.put("login_type", "T");
        params.put("short_name", name);
        params.put("app_version", versionName); // new 18-02-2020
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "get_remark_teacherwise", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i("REMARK", "onResponse: " + response);
                progressBar.setVisibility(View.GONE);
                System.out.println(response.toString());
                try {
                    if (response.getString("status").equals("true")) {
                        JSONArray jsonArray = response.getJSONArray("remark");
//                        System.out.println(jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            rmkid = jsonObject.getString("remark_id");
                            description = jsonObject.getString("remark_desc");
                            rmksub = jsonObject.getString("remark_subject");
                            rmkdate = jsonObject.getString("remark_date");
                            classid = jsonObject.getString("class_id");
                            section_id = jsonObject.getString("section_id");
                            studid = jsonObject.getString("student_id");
                            subject_id = jsonObject.getString("subject_id");
                            teacherid = jsonObject.getString("teacher_id");
                            acdyr = jsonObject.getString("academic_yr");
                            publish = jsonObject.getString("publish");
                            ack = jsonObject.getString("acknowledge");
                            clsnm = jsonObject.getString("class_name");
                            secnm = jsonObject.getString("sec_name");
                            subnm = jsonObject.getString("sub_name");
                            firstnm = jsonObject.getString("first_name");
                            midlnm = jsonObject.getString("mid_name");
                            lastnm = jsonObject.getString("last_name");
                            String publishremDate = jsonObject.getString("publish_date");
                            remark_type = jsonObject.getString("remark_type");
                            read_status = jsonObject.getString("read_status");

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date date1 = null;

                            try {
                                date1 = simpleDateFormat.parse(rmkdate);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //HH:mm:ss.SSS'Z'
                            SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            reqremarkformatdate = newformat.format(date1);
//                            read_status = "1";

                            if (lastnm.equals("null") || lastnm == null) {
                                lastnm = "";
                                RemarkPojo remarkPojo = new RemarkPojo(clsnm, subnm, reqremarkformatdate, description, rmkid, rmksub,
                                        classid, section_id, subject_id, studid, teacherid, acdyr, publish, ack, firstnm, midlnm, lastnm, secnm, remark_type, false, read_status);
                                remarkPojoArrayList.add(remarkPojo);
                            } else if (midlnm.equals("null") || midlnm == null) {
                                midlnm = "";
                                RemarkPojo remarkPojo = new RemarkPojo(clsnm, subnm, reqremarkformatdate, description, rmkid, rmksub,
                                        classid, section_id, subject_id, studid, teacherid, acdyr, publish, ack, firstnm, midlnm, lastnm, secnm, remark_type, false, read_status);
                                remarkPojoArrayList.add(remarkPojo);
                            } else if (firstnm.equals("null") || firstnm == null) {
                                firstnm = "";
                                RemarkPojo remarkPojo = new RemarkPojo(clsnm, subnm, reqremarkformatdate, description, rmkid, rmksub,
                                        classid, section_id, subject_id, studid, teacherid, acdyr, publish, ack, firstnm, midlnm, lastnm, secnm, remark_type, false, read_status);
                                remarkPojoArrayList.add(remarkPojo);
                            } else {
                                RemarkPojo remarkPojo = new RemarkPojo(clsnm, subnm, reqremarkformatdate, description, rmkid, rmksub,
                                        classid, section_id, subject_id, studid, teacherid, acdyr, publish, ack, firstnm, midlnm, lastnm, secnm, remark_type, false, read_status);
                                remarkPojoArrayList.add(remarkPojo);
                            }
                            remarkAdapter = new RemarkAdapter(RemarksActivity.this, remarkPojoArrayList);
                            recyclerView.setAdapter(remarkAdapter);


                        }
                        remarkAdapter.notifyDataSetChanged();

                    } else {
                        progressBar.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    nodata.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                nodata.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
        requestQueue1.add(jsonObjectRequest);

    }

    private void filter(String s) {
        ArrayList<RemarkPojo> filtername = new ArrayList<>();

        for (RemarkPojo subjectnm : remarkPojoArrayList) {
            if (subjectnm.getRmkSubNm().toLowerCase().contains(s.toLowerCase())) {
                filtername.add(subjectnm);
            }

        }
        remarkAdapter.filterlist(filtername);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RemarksActivity.this, HomePageDrawerActivity.class);
        intent.putExtra("reg_id", reg_id);
        intent.putExtra("academic_yr", academic_yr);
        startActivity(intent);
        RemarksActivity.this.finish();
    }
}
