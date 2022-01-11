package in.aceventura.evolvuschool.teacherapp.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.adapter.AllotDeallotListAdapter;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.AllotDeallotModel;
import in.aceventura.evolvuschool.teacherapp.utils.ConstantsFile;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;

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

import static in.aceventura.evolvuschool.teacherapp.R.layout;

public class AllotDeallotActivity extends AppCompatActivity {

    private TextView nodata;
    private ProgressBar progressBar;
    private String name,academic_yr,reg_id;
    private String newUrl;
    ArrayList<AllotDeallotModel> mAllotDeallotList = new ArrayList<>();
    AllotDeallotListAdapter allotDeallotListAdapter;
    FloatingActionButton fab_allot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_allot_deallot);
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
        ht_Teachernote.setText("Alloted Question Bank");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        RecyclerView rv_allotdeallot = findViewById(R.id.rv_allotdeallot);
        fab_allot = findViewById(R.id.fab_allot);
        nodata = findViewById(R.id.nodata);
        progressBar = findViewById(R.id.progressBar);
        nodata.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

         reg_id = (SharedClass.getInstance(this).getRegId().toString());
         academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        String dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);
        String proUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
            proUrl = mDatabaseHelper.getTURL(1);
        }

        //getting all allotted question banks

        getAllottedQuestionBanks();

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_allotdeallot.setLayoutManager(manager);
        allotDeallotListAdapter = new AllotDeallotListAdapter(this, mAllotDeallotList);
        rv_allotdeallot.setAdapter(allotDeallotListAdapter);


        fab_allot.setOnClickListener(v -> {
            Intent intent = new Intent(AllotDeallotActivity.this, AllotQuestionPaperActivity.class);
            startActivity(intent);
        });

        // by default showed = No
        String allotDeallotScreen1Showed = SharedClass.getInstance(this).getAllotDeallotScreen1();
        //Guideview check
        if (allotDeallotScreen1Showed.equals("No")){

            //showing guideView
            ConstantsFile.showGuideView(this,fab_allot, "Allot Question Paper", "Click here to allot " + "the Question Banks to particular class");

            //setting that the guideview was shown
            SharedClass.getInstance(this).setAllotDeallotScreen1();
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
                        Intent intent = new Intent(AllotDeallotActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(AllotDeallotActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(AllotDeallotActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(AllotDeallotActivity.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }

    }

    private void getAllottedQuestionBanks() {
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("short_name", name);
        params.put("acd_yr", academic_yr);
        params.put("reg_id", reg_id);
        System.out.println("ALLOTTED_QB_PARAMS" + params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi/get_allotted_question_banks", new JSONObject(params), response -> {
            Log.d("ALLOTTED_QB", response.toString());
            try {
                if (response.getString("status").equals("true")) {
                    progressBar.setVisibility(View.GONE);
                    JSONArray jsonArray = response.getJSONArray("getAllotedQb");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String question_bank_id = jsonObject.getString("question_bank_id");
                        String class_id = jsonObject.getString("class_id");
                        String section_id = jsonObject.getString("section_id");
                        String subject_id = jsonObject.getString("subject_id");
                        String teacher_id = jsonObject.getString("teacher_id");
                        String status = jsonObject.getString("status");
                        String academic_yr1 = jsonObject.getString("academic_yr");
                        String exam_id = jsonObject.getString("exam_id");
                        String qb_name = jsonObject.getString("qb_name");
                        String qb_type = jsonObject.getString("qb_type");
                        String instructions = jsonObject.getString("instructions");
                        String weightage = jsonObject.getString("weightage");
                        String create_date = jsonObject.getString("create_date");
                        String complete = jsonObject.getString("complete");
                        String className = jsonObject.getString("classname");
                        String division = jsonObject.getString("sectionname");
                        String sm_id = jsonObject.getString("sm_id");
                        String subjectName = jsonObject.getString("subjectname");

                        AllotDeallotModel allotDeallotModel = new AllotDeallotModel(question_bank_id, class_id, section_id, subject_id, teacher_id, status, academic_yr1, exam_id, qb_name, qb_type, instructions, weightage, create_date, complete, className, division, sm_id, subjectName);
                        mAllotDeallotList.add(allotDeallotModel);
                    }
                    allotDeallotListAdapter.notifyDataSetChanged();
                } else {
                    //false
                    System.out.println(response);
                    Toast.makeText(AllotDeallotActivity.this, response.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    nodata.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("ERROR" + e.getMessage());
                Toast.makeText(AllotDeallotActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                nodata.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }, error -> {
            System.out.println("ERROR_RESPONSE - " + error.getMessage());
            Toast.makeText(AllotDeallotActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            nodata.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        });

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AllotDeallotActivity.this, OnlineExamDashboard.class);
        intent.putExtra("reg_id", reg_id);
        intent.putExtra("academic_yr", academic_yr);
        startActivity(intent);
        AllotDeallotActivity.this.finish();
    }
}