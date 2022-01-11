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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.adapter.TeacherNoteAdapter;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.TeacherNoteePojo;


public class TeachersNoteACtivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    RelativeLayout linearLayout;
    TextView nodata;
    TeacherNoteAdapter teacherNoteAdapter;
    LinearLayoutManager manager;
    RecyclerView recyclerView;
    ArrayList<TeacherNoteePojo> mArrayList;
    Context context;
    ProgressBar progressBar;
    String name, newUrl, dUrl, proUrl, reg_id, academic_yr;
    DatabaseHelper mDatabaseHelper;
    String class_id, section_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_note_activity);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

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
        ht_Teachernote.setText("Teacher Note");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        ImageView fabcreate = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclernote);
        nodata = findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);


        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);
        proUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }

        linearLayout = findViewById(R.id.lin);
        EditText edtSearch = findViewById(R.id.searchEdt);
        context = this;
        mArrayList = new ArrayList<>();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        teacherNoteAdapter = new TeacherNoteAdapter(context, mArrayList);
        recyclerView.setAdapter(teacherNoteAdapter);

        fabcreate.setOnClickListener(view -> {

            Intent intent = new Intent(TeachersNoteACtivity.this, CreateTeachernote.class);
            startActivity(intent);
            finish();
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            reg_id = bundle.getString("reg_id");
            academic_yr = bundle.getString("academic_yr");
        }

        getALlNotes();

        edtSearch.addTextChangedListener(new TextWatcher() {
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
                    Intent intent = new Intent(TeachersNoteACtivity.this, TeacherProfileActivity.class);
                    startActivity(intent);
                }
                if (tabId == R.id.tab_calendar) {
                    Intent intent = new Intent(TeachersNoteACtivity.this, MyCalendar.class);
                    startActivity(intent);
                }

            }
        });
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(int tabId) {


                if (tabId == R.id.tab_profile) {
                    Intent intent = new Intent(TeachersNoteACtivity.this, TeacherProfileActivity.class);
                    startActivity(intent);
                }
                if (tabId == R.id.tab_dashboard) {

                    Intent intent = new Intent(TeachersNoteACtivity.this, HomePageDrawerActivity.class);
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

        //getting search rvManager from system service
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

    private void filter(String s) {
        ArrayList<TeacherNoteePojo> filtername = new ArrayList<>();
        for (TeacherNoteePojo subjectnm : mArrayList) {
            if (subjectnm.getSubjectname().toLowerCase().contains(s.toLowerCase())) {
                filtername.add(subjectnm);
            }
        }
        teacherNoteAdapter.filterlist(filtername);
    }

    private void getALlNotes() {
        mArrayList.clear(); // clear arrayList before loading teacher notes
        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                newUrl + "AdminApi/get_daily_notes",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            if (response != null) {
                                JSONObject jsonObject1 = new JSONObject(response);
                                JSONArray jsonArray = jsonObject1.getJSONArray("daily_notes");
                                Log.d("TeacherNotesModule", "DailyNotes: " + jsonArray);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String tempdate = jsonObject.getString("date");
                                    String notes_id = jsonObject.getString("notes_id");
                                    section_id = jsonObject.getString("section_id");
                                    class_id = jsonObject.getString("class_id");
                                    String teacher_id = jsonObject.getString("teacher_id");
                                    String subject_id = jsonObject.getString("subject_id");
                                    String description = jsonObject.getString("description");
                                    String classname = jsonObject.getString("classname");
                                    String sectionname = jsonObject.getString("sectionname");
                                    String subjectname = jsonObject.getString("subjectname");
                                    String academic_yr = jsonObject.getString("academic_yr");
                                    String publish = jsonObject.getString("publish");
                                    String pDate = jsonObject.getString("publish_date");

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                    Date date1 = null;
                                    try {
                                        date1 = simpleDateFormat.parse(tempdate);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    String dateInReqFormat = newformat.format(date1);
                                    TeacherNoteePojo teacherNoteePojo = new TeacherNoteePojo(notes_id, dateInReqFormat, class_id, teacher_id, section_id, subject_id, description, classname, sectionname, subjectname, academic_yr, publish, pDate, false, class_id, section_id);

                                    mArrayList.add(teacherNoteePojo);
                                    teacherNoteAdapter.notifyDataSetChanged();
                                }
                            } else {
                                nodata.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            nodata.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                }, error -> {
                    error.getMessage();
                    nodata.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    System.out.println("Response Call_Post_api error.getMessage() =>" + error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                final HashMap<String, String> params = new HashMap<>();
                params.put("acd_yr", academic_yr);
                params.put("reg_id", reg_id);
                params.put("short_name", name);
                return params;
            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TeachersNoteACtivity.this, HomePageDrawerActivity.class);
        intent.putExtra("reg_id", reg_id);
        intent.putExtra("academic_yr", academic_yr);
        startActivity(intent);
        TeachersNoteACtivity.this.finish();
    }

}
