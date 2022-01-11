package in.aceventura.evolvuschool.teacherapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
import in.aceventura.evolvuschool.teacherapp.adapter.QuestionBankListAdapter;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.QbankModel;
import in.aceventura.evolvuschool.teacherapp.utils.ConstantsFile;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;

public class OnlineExamActivity extends AppCompatActivity {
    RelativeLayout linearLayout;
    TextView nodata;
    Context context;
    ProgressBar progressBar;
    String name, newUrl, dUrl, proUrl, reg_id, academic_yr;
    DatabaseHelper mDatabaseHelper;
    FloatingActionButton uploadQbFab;

    RecyclerView rv_Qbanks;
    LinearLayoutManager manager;
    ArrayList<QbankModel> mQbArrayList = new ArrayList<>();
    QuestionBankListAdapter questionBankListAdapter;

    String guideViewShowed;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_exam);
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
        ht_Teachernote.setText("Online Exam");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        Log.e("Vakakakak","jsjsjsjs");
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        uploadQbFab = findViewById(R.id.fab_addQbanks);
        context = this;
        rv_Qbanks = findViewById(R.id.rv_qbanks);
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
            proUrl = mDatabaseHelper.getTURL(1);
        }
        Log.d("newUrl: ",newUrl);
        try {
            Log.e("Vakakakak","Values00");
            View view = findViewById(R.id.bb_bookavailability);
            BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
            //


            TextView supportEmail = view.findViewById(R.id.email);
            Log.e("Vakakakak","Values01");
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
                        Intent intent = new Intent(OnlineExamActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(OnlineExamActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(OnlineExamActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(OnlineExamActivity.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
            Log.e("Vakakakak","Values01");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
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




        Log.e("Vakakakak","Values00");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            reg_id = bundle.getString("reg_id");
            academic_yr = bundle.getString("academic_yr");
        }

        uploadQbFab.setOnClickListener(v -> {
            Intent intent = new Intent(OnlineExamActivity.this, UploadQuestionPaperActivity.class);
            intent.putExtra("reg_id", reg_id);
            intent.putExtra("academic_yr", academic_yr);
            startActivity(intent);
        });

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_Qbanks.setLayoutManager(manager);
        questionBankListAdapter = new QuestionBankListAdapter(context, mQbArrayList);
        rv_Qbanks.setAdapter(questionBankListAdapter);

        getAllQuestionBanks(reg_id, academic_yr);

        // by default showed = No
        guideViewShowed = SharedClass.getInstance(this).getOnlineExamScreen1();

        //Guideview check
        if (guideViewShowed.equals("No")){

            //showing guideView
            ConstantsFile.showGuideView(this,uploadQbFab, "Create New Exam", "Click here to create " +
                    "new Question Banks");
            //setting that the guideview was shown
            SharedClass.getInstance(this).setOnlineExamScreen1();
        }else{
            return;
        }





    }

    private void getAllQuestionBanks(final String reg_id, final String academic_yr) {
        // clear arrayList before loading qBanks
        mQbArrayList.clear();
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "OnlineExamApi" +
                "/get_question_bank_of_upload_type", response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        Log.d("Question_banks", response);
                        if (response != null) {
                            JSONObject jsonObject1 = new JSONObject(response);
                            if (jsonObject1.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject1.getJSONArray("qb_data");
                                Log.d("OnlineExamResponse", jsonArray.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String questionBankName = jsonObject.getString("qb_name");
                                    String createdOn = jsonObject.getString("create_date");
                                    String status = jsonObject.getString("complete");
                                    String question_bank_id = jsonObject.getString("question_bank_id");
                                    String exam_id = jsonObject.getString("exam_id");
                                    String class_id = jsonObject.getString("class_id");
                                    String subject_id = jsonObject.getString("subject_id");
                                    String qb_type = jsonObject.getString("qb_type");
                                    String instructions = jsonObject.getString("instructions");
                                    String weightage = jsonObject.getString("weightage");
                                    String teacher_id = jsonObject.getString("teacher_id");
                                    String complete = jsonObject.getString("complete");
                                    String academic_yr1 = jsonObject.getString("academic_yr");
                                    String allotted_answered = jsonObject.getString("allotted_answered");
                                    String class_name = jsonObject.getString("class_name");
                                    String subject_name = jsonObject.getString("subject_name");
                                    String exam_name = jsonObject.getString("exam_name");
//                                    String section_id = jsonObject.getString("section_id");

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",
                                            Locale.getDefault());
                                    Date date1 = null;
                                    try {
                                        date1 = simpleDateFormat.parse(createdOn);
                                    }
                                    catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    String newCreatedOnDate = newformat.format(date1);

                                    QbankModel qbModel = new QbankModel(questionBankName, newCreatedOnDate, status,
                                            question_bank_id, exam_id, class_id, subject_id, qb_type, instructions,
                                            weightage, teacher_id, complete, academic_yr1, allotted_answered, class_name,
                                            subject_name, exam_name,reg_id/*,section_id*/);

                                    mQbArrayList.add(qbModel);
                                    questionBankListAdapter.notifyDataSetChanged();
                                }
                            }
                            else {
                                nodata.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }

                        }
                        else {
                            nodata.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        nodata.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

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
        Intent intent = new Intent(OnlineExamActivity.this, OnlineExamDashboard.class);
        intent.putExtra("reg_id", reg_id);
        intent.putExtra("academic_yr", academic_yr);
        startActivity(intent);
        OnlineExamActivity.this.finish();
    }
}
