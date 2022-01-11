package in.aceventura.evolvuschool.teacherapp.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.adapter.EvaluationQuestionAdapter;
import in.aceventura.evolvuschool.teacherapp.pojo.EvaluationQuestionModel;
import in.aceventura.evolvuschool.teacherapp.utils.ConstantsFile;
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

import static in.aceventura.evolvuschool.teacherapp.R.layout.activity_evaluation_question;


public class EvaluationQuestionActivity extends AppCompatActivity {

    EvaluationQuestionAdapter evaluationQuestionAdapter;
    ArrayList<EvaluationQuestionModel> evaluationQuestionsList = new ArrayList<>();
    LinearLayoutManager lm;
    DatabaseHelper mDatabaseHelper;
    String name, dUrl, newUrl, proUrl, date, reg_id, academic_yr, viewOnly;
    TextView nodata;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    Button update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_evaluation_question);

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
        nodata = findViewById(R.id.nodata);
        update = findViewById(R.id.updateQB);
        nodata.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.rv_evaluationQuestions);

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

        //from intent
        date = getIntent().getStringExtra("date");
        //for View only hide the save btn
        viewOnly = getIntent().getStringExtra("viewOnly");

        if (viewOnly != null) {
            if (viewOnly.equals("true")) {
                update.setVisibility(View.GONE);
            }
            else if (viewOnly.equals("false")) {
                update.setVisibility(View.VISIBLE);
            }
        }
        else {
            update.setVisibility(View.VISIBLE);
        }

        //from sharedPref
        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());


//        evaluationQuestionsList.clear();
        evaluationQuestionsList = (ArrayList<EvaluationQuestionModel>) getIntent().getSerializableExtra("qbank");


        if (evaluationQuestionsList != null) {
            if (evaluationQuestionsList.size() > 0) {
                progressBar.setVisibility(View.GONE);
                nodata.setVisibility(View.GONE);
                lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(lm);
                evaluationQuestionAdapter = new EvaluationQuestionAdapter(getApplicationContext(),
                        evaluationQuestionsList);
                recyclerView.setAdapter(evaluationQuestionAdapter);
            }
            else {
                progressBar.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
            }
        }

        //UpdateExam Button...
        update.setOnClickListener(v -> updateExam(evaluationQuestionsList));

        String updateBtnShowed = SharedClass.getInstance(this).getEvaluationScreen2();
        if (updateBtnShowed.equals("No")) {

            //showing guideView
            ConstantsFile.showGuideView(this, update, "Evaluate Exam", "Click here to evaluate the " +
                    "examination");

            //setting that the guideview was shown "Yes"
            SharedClass.getInstance(this).setEvaluationScreen2();
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




    }

    private void updateExam(ArrayList<EvaluationQuestionModel> evaluationQuestionsList) {


        final JSONObject mainJsonObject = new JSONObject();

        JSONArray quesJSONArray = new JSONArray();

        for (int i = 0; i < evaluationQuestionsList.size(); i++) {
                JSONObject subJsonObject = new JSONObject();
                try {
                    subJsonObject.put("question_bank_id", evaluationQuestionsList.get(i).getQuestion_bank_id());
                    subJsonObject.put("student_id", evaluationQuestionsList.get(i).getStudent_id());
                    subJsonObject.put("teacher_id", reg_id);
                    subJsonObject.put("academic_yr", academic_yr);
                    subJsonObject.put("question_id", evaluationQuestionsList.get(i).getQuestion_id());
                    subJsonObject.put("weightage", evaluationQuestionsList.get(i).getWeightage());
                    subJsonObject.put("marks_obt", evaluationQuestionsList.get(i).getMarks_obt());
                    quesJSONArray.put(subJsonObject);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
        }

        try {
            mainJsonObject.put("questionlist", quesJSONArray);
            System.out.println(mainJsonObject.toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "OnlineExamApi" +
                "/question_bank_evaluation", response -> {
            try {
                JSONObject obj = new JSONObject(response);
                if (obj.getString("status").equals("true")) {
                    Toast.makeText(EvaluationQuestionActivity.this, obj.getString("message"),
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EvaluationQuestionActivity.this, EvaluationActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    //false
                    System.out.println(response);
                    Toast.makeText(EvaluationQuestionActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException ex) {
                ex.printStackTrace();
                System.out.println("ERROR - " + ex.getMessage());
                ex.printStackTrace();
                Toast.makeText(EvaluationQuestionActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            progressBar.setVisibility(View.GONE);
            System.out.println("ERROR - " + error.getMessage());
            error.printStackTrace();
            Toast.makeText(EvaluationQuestionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("operation", "eval");
                params.put("login_type", "T");
                params.put("reg_id", reg_id);
                params.put("questionlist", mainJsonObject.toString());
                params.put("short_name", name);
                System.out.println("evaluate_params" + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}