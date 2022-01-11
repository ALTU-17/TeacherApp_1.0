package in.aceventura.evolvuschool.teacherapp.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.adapter.EvaluationStudentListAdapter;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.EvaluationQuestionModel;
import in.aceventura.evolvuschool.teacherapp.pojo.EvaluationStudentListModel;

import static in.aceventura.evolvuschool.teacherapp.R.layout;
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





public class EvaluationStudentsListActivity extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView rv_evaluationStudents;
    TextView nodata;
    EvaluationStudentListAdapter evaluationStudentListAdapter;
    LinearLayoutManager manager;
    ArrayList<EvaluationStudentListModel> mEvaluationStudentsList = new ArrayList<>();
    String name,newUrl,academic_yr,reg_id,dUrl;
    LinearLayout l1,l2;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_evaluation_students_list);

        getSupportActionBar().hide();
        View tb_main1 = findViewById(R.id.icd_tb_homeworkdetails);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("("+SharedClass.getInstance(getApplicationContext()).getAcademicYear()+")");

        school_title.setText(" Evolvu Teacher App");
        ht_Teachernote.setText("Question Bank Evaluation");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl=mDatabaseHelper.getURL(1);
        newUrl=mDatabaseHelper.getTURL(1);

        nodata = findViewById(R.id.nodata);
        rv_evaluationStudents = findViewById(R.id.rv_evaluationStudents);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        mEvaluationStudentsList.clear();


        Intent intent = getIntent();


        if (intent != null) {
            nodata.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            rv_evaluationStudents.setVisibility(View.VISIBLE);
            mEvaluationStudentsList =
                    (ArrayList<EvaluationStudentListModel>) getIntent().getSerializableExtra(
                            "students_lists");

        }
        else {
            progressBar.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
            rv_evaluationStudents.setVisibility(View.GONE);
        }

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_evaluationStudents.setLayoutManager(manager);
        evaluationStudentListAdapter = new EvaluationStudentListAdapter(getApplicationContext(),
                mEvaluationStudentsList);
        rv_evaluationStudents.setAdapter(evaluationStudentListAdapter);


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
                        Intent intent = new Intent(EvaluationStudentsListActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(EvaluationStudentsListActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(EvaluationStudentsListActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(EvaluationStudentsListActivity.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }


    }
}