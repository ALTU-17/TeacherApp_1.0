package in.aceventura.evolvuschool.teacherapp.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;

import static in.aceventura.evolvuschool.teacherapp.R.layout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class OnlineExamDashboard extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    String reg_id,academic_yr,teacher;
    CardView  onlineExamCard, evaluationCardView, allotDeallotCard, monitorExamCard;
    String name, dUrl, newUrl,gender;
    TextView  tName, className, tvClass, tvSupport;
    RelativeLayout teacherDetails;
    ImageView tlogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_online_exam_dashboard);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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

        onlineExamCard = findViewById(R.id.OnlineExamCard);
        evaluationCardView = findViewById(R.id.evaluationCardView);
        allotDeallotCard = findViewById(R.id.allotDeallotCard);
        monitorExamCard = findViewById(R.id.monitorExamCard);
        tlogo = findViewById(R.id.tlogo);
        tvClass = findViewById(R.id.tvClass);
        className = findViewById(R.id.className);
        tvClass.setVisibility(View.GONE);
        className.setVisibility(View.GONE);
        teacherDetails = findViewById(R.id.teacherDetails);
        tvSupport = findViewById(R.id.tvSupport);
        tName = findViewById(R.id.tName);
        className = findViewById(R.id.className);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }
         reg_id = (SharedClass.getInstance(this).getRegId().toString());
         academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
         teacher = (SharedClass.getInstance(this).getTeachername());


        //Getting teacher details for teacher card
        getTeacherDetails(reg_id, academic_yr);

        onlineExamCard.setOnClickListener(v -> {
            Intent onlineExamIntent = new Intent(getApplicationContext(), OnlineExamActivity.class);
            onlineExamIntent.putExtra("teacher", teacher);
            onlineExamIntent.putExtra("reg_id", reg_id);
            onlineExamIntent.putExtra("academic_yr", academic_yr);
            startActivity(onlineExamIntent);
        });

        allotDeallotCard.setOnClickListener(v -> {
            Intent onlineExamIntent = new Intent(getApplicationContext(), AllotDeallotActivity.class);
            onlineExamIntent.putExtra("teacher", teacher);
            onlineExamIntent.putExtra("reg_id", reg_id);
            onlineExamIntent.putExtra("academic_yr", academic_yr);
            startActivity(onlineExamIntent);
        });


        evaluationCardView.setOnClickListener(v -> {
            Intent onlineExamIntent = new Intent(getApplicationContext(), EvaluationActivity.class);
            onlineExamIntent.putExtra("teacher", teacher);
            onlineExamIntent.putExtra("reg_id", reg_id);
            onlineExamIntent.putExtra("academic_yr", academic_yr);
            startActivity(onlineExamIntent);
        });

        monitorExamCard.setOnClickListener(v -> {
            Intent onlineExamIntent = new Intent(getApplicationContext(), MonitorOngoingExamActivity.class);
            onlineExamIntent.putExtra("teacher", teacher);
            onlineExamIntent.putExtra("reg_id", reg_id);
            onlineExamIntent.putExtra("academic_yr", academic_yr);
            startActivity(onlineExamIntent);
        });

        //commented for now // TODO: 21-10-2020
        /*//Guide view conditions
        String onlineExamCardShowed = SharedClass.getInstance(this).getOnlineExamNewFeature();
        //Guideview check for Online Exam Card
        if (onlineExamCardShowed.equals("No")){
            //showing guideView
            ConstantsFile.showGuideView(this,onlineExamCard, "This is a New Feature", "Create Question banks");
            //setting that the guideview was shown "Yes"
            SharedClass.getInstance(this).setOnlineExamNewFeature();
        }

        //Guideview check for Allot deallot card
        String allotDeallotCardShowed = SharedClass.getInstance(this).getAllotDeallotNewFeature();
        if (allotDeallotCardShowed.equals("No")) {
            //showing guideView
            ConstantsFile.showGuideView(this, allotDeallotCard, "This is a New Feature", "Allot Deallot " +
                    "Question Banks");
            //setting that the guideview was shown "Yes"
            SharedClass.getInstance(this).setAllotDeallotNewFeature();
        }
        
        //Guideview check for Evaluation card
        String evaluationCardShowed = SharedClass.getInstance(this).getEvaluationNewFeature();
        if (evaluationCardShowed.equals("No")) {
            //showing guideView
            ConstantsFile.showGuideView(this, evaluationCardView, "This is a New Feature", "Online Exam " +
                    "Evaluation ");
            //setting that the guideview was shown "Yes"
            SharedClass.getInstance(this).setEvaluationNewFeature();
        }*/


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
                        Intent intent = new Intent(OnlineExamDashboard.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(OnlineExamDashboard.this, MyCalendar.class);
                        startActivity(intent);
                    }
                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(OnlineExamDashboard.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(OnlineExamDashboard.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }

    }

    private void getTeacherDetails(String reg_id, String academic_yr) {
        HashMap<String, String> params3 = new HashMap<>();
        params3.put("academic_yr", academic_yr);
        params3.put("reg_id", reg_id);
        params3.put("short_name", name);
        System.out.println(params3);
        JsonObjectRequest jsonObjectRequest4 = new JsonObjectRequest(newUrl + "AdminApi/get_teacher",
                new JSONObject(params3), response -> {
                    System.out.println("TeacherName " + response);
                    if (response != null) {
                        try {
                            if (response.getString("status").equals("true")) {
                                teacherDetails.setVisibility(View.VISIBLE);
                                teacher = response.getString("teacher_name");
                                gender = response.getString("gender");
                                tName.setText(teacher);

                                //Saving Teacher Name in Pref
                                SharedClass.getInstance(getApplicationContext()).TeacherName(teacher);

                                if (gender.equals("Male") || gender.equals("male")) {
                                    tlogo.setImageResource(R.drawable.maleteacher);
                                }
                                else if (gender.equals("Female") || gender.equals("female")) {
                                    tlogo.setImageResource(R.drawable.femaleteacher);
                                }

                                JSONArray result = response.getJSONArray("class");
                                JSONObject jsonObject = result.getJSONObject(0);
                                String a = jsonObject.getString("classname");
                                String b = jsonObject.getString("sectionname");

                                if (!a.equals("") && !b.equals("")) {
                                    tvClass.setVisibility(View.VISIBLE);
                                    className.setVisibility(View.VISIBLE);
                                    className.setText(String.format("%s %s", a, b));
                                }
                                else {
                                    tvClass.setVisibility(View.GONE);
                                    className.setVisibility(View.GONE);
                                }

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        teacherDetails.setVisibility(View.GONE);
                        tvClass.setVisibility(View.GONE);
                    }
                }, error -> {
            teacherDetails.setVisibility(View.GONE);
            tvClass.setVisibility(View.GONE);

        });
        RequestQueue requestQueue4 = Volley.newRequestQueue(this);
        jsonObjectRequest4.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue4.add(jsonObjectRequest4);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OnlineExamDashboard.this, HomePageDrawerActivity.class);
        intent.putExtra("reg_id", reg_id);
        intent.putExtra("academic_yr", academic_yr);
        startActivity(intent);
        super.onBackPressed();
    }
}