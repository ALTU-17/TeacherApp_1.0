package in.aceventura.evolvuschool.teacherapp.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.adapter.AnswerPaperAdapter;
import in.aceventura.evolvuschool.teacherapp.adapter.QuestionPaperAdapter;
import in.aceventura.evolvuschool.teacherapp.pojo.AnsPaperModel;
import in.aceventura.evolvuschool.teacherapp.pojo.EvaluationQuestionModel;
import in.aceventura.evolvuschool.teacherapp.pojo.QuesPaperModel;
import in.aceventura.evolvuschool.teacherapp.pojo.UploadEvaluationQuestionModel;
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

import static in.aceventura.evolvuschool.teacherapp.R.layout;

public class PenPaperEvaluationActivity extends AppCompatActivity {

    QuestionPaperAdapter questionPaperAdapter;
    AnswerPaperAdapter answerPaperAdapter;
    RecyclerView rv_quesPaper, rv_ansPaper;
    TextView exam_name, subject_name, class_name, marks;
    EditText marksbyteacher;
    Button btn_save_exam;
    int weightageMarks, obtainedMarks;
    String viewOnly;
    private List<QuesPaperModel> qpList = new ArrayList<>();
    private List<AnsPaperModel> apList = new ArrayList<>();
    private String name, newUrl, dUrl, proUrl, academic_yr, reg_id, question_bank_id, student_id, weightage,
            qb_name, examName, className, subjectName, ques_paper_attachments, ans_paper_attachments, marks_obt,
            download_url;
    private ProgressBar pb_saveExam;
    ArrayList<UploadEvaluationQuestionModel> details = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_pen_paper_evaluation);


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

        hooks();

        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
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


        Intent i = getIntent();
        student_id = i.getStringExtra("student_id");

//-------------------------------------------------------------------------------------------

        //for View only hide the save btn
        viewOnly = i.getStringExtra("viewOnly");

        if (viewOnly != null) {
            if (viewOnly.equals("true")) {
                marksbyteacher.setEnabled(false);
                btn_save_exam.setVisibility(View.GONE);
            }
            else if (viewOnly.equals("false")) {
                btn_save_exam.setVisibility(View.VISIBLE);
                marksbyteacher.setEnabled(true);
            }
        }
        else {
            marksbyteacher.setEnabled(true);
            btn_save_exam.setVisibility(View.VISIBLE);
        }

//-------------------------------------------------------------------------------------------




            details = (ArrayList<UploadEvaluationQuestionModel>) getIntent().getSerializableExtra(
                    "upload_model");

            if (details != null) {
                for (int j = 0; j <details.size() ; j++) {
                    question_bank_id = details.get(j).getQuestion_bank_id();

                    qb_name = details.get(j).getQb_name();
                    exam_name.setText(qb_name);


                    weightage = details.get(j).getWeightage(); //marks
                    marks.setText(String.format("Weightage : %s", weightage));

                    subjectName = details.get(j).getSubjectname();
                    subject_name.setText(String.format("( %s -", subjectName));

                    className = details.get(j).getClassname();
                    class_name.setText(String.format("Class %s )", className));

                    ques_paper_attachments = details.get(j).getQues_array();
                    ans_paper_attachments = details.get(j).getAns_array();

                    if (details.get(j).getMarks_obt() == null | details.get(j).getMarks_obt().equals("null")) {
                        marks_obt = "";
                    }
                    else {
                        marks_obt = details.get(j).getMarks_obt();
                        // TODO: Check the condition at the time of re-evaluation
                        if (marks_obt.equals("0") | marks_obt.equals("null") | marks_obt.equals("")) {
                            marksbyteacher.setText("");
                        }
                        else {
                            marksbyteacher.setText(marks_obt);
                        }
                    }

                    if (weightage == null) {
                        weightageMarks = 0;
                    }
                    else {
                        weightageMarks = Integer.parseInt(weightage);
                    }
                    download_url = details.get(j).getDownload_url();
                }
            }else{
                return;
            }






        //Question Paper attachments
        JSONArray ques_papers = null;
        try {
            ques_papers = new JSONArray(ques_paper_attachments);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        if (ques_papers != null) {
            for (int j = 0; j < ques_papers.length(); j++) {
                try {
                    JSONObject quesObj = ques_papers.getJSONObject(j);
                    String up_id = quesObj.getString("uploaded_qp_id");
                    String question_bank_id = quesObj.getString("question_bank_id");
                    String file_size = quesObj.getString("file_size");
                    String image_name = quesObj.getString("image_name");

                    QuesPaperModel qpModel = new QuesPaperModel(up_id, question_bank_id, academic_yr, image_name
                            , file_size, download_url,"");
                    qpList.add(qpModel);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        //Answer Paper attachments
        JSONArray ans_papers = null;
        try {
            ans_papers = new JSONArray(ans_paper_attachments);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        if (ans_papers != null) {
            for (int j = 0; j < ans_papers.length(); j++) {
                try {
                    JSONObject ansObj = ans_papers.getJSONObject(j);
                    String up_id = ansObj.getString("up_id");
                    String question_bank_id = ansObj.getString("question_bank_id");
                    String student_id = ansObj.getString("student_id");
                    String file_size = ansObj.getString("file_size");
                    String academic_yr = ansObj.getString("academic_yr");
                    String image_name = ansObj.getString("image_name");

                    AnsPaperModel ansModel = new AnsPaperModel(up_id, question_bank_id, file_size, image_name,
                            download_url, academic_yr, student_id);

                    apList.add(ansModel);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //Question papers
        questionPaperAdapter = new QuestionPaperAdapter(this, qpList);
        rv_quesPaper.setAdapter(questionPaperAdapter);
        final LinearLayoutManager newsLinearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        rv_quesPaper.setLayoutManager(newsLinearLayoutManager);

        //Answer papers
        answerPaperAdapter = new AnswerPaperAdapter(this, apList);
        rv_ansPaper.setAdapter(answerPaperAdapter);

        final LinearLayoutManager ansPaperLinearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        rv_ansPaper.setLayoutManager(ansPaperLinearLayoutManager);

        btn_save_exam.setOnClickListener(v -> {
            if (marksbyteacher.getText().toString().trim().isEmpty()) {
                Toast.makeText(PenPaperEvaluationActivity.this, "Enter Marks", Toast.LENGTH_SHORT).show();
            }
            else if (!marksbyteacher.getText().toString().trim().isEmpty()) {
                obtainedMarks = Integer.parseInt(marksbyteacher.getText().toString());
                if (obtainedMarks > weightageMarks) {
                    Toast.makeText(PenPaperEvaluationActivity.this, "Marks cannot be greater " + "the " +
                            "Weightage.", Toast.LENGTH_SHORT).show();
                }
                else {
                    evaluateExam();
                }
            }
            else {
                evaluateExam();
            }
        });

        String updateBtnShowed = SharedClass.getInstance(this).getEvaluationScreen3();
        if (updateBtnShowed.equals("No")) {

            //showing guideView
            ConstantsFile.showGuideView(this, btn_save_exam, "Evaluate Exam", "Click here to evaluate the " +
                    "examination");

            //setting that the guideview was shown "Yes"
            SharedClass.getInstance(this).setEvaluationScreen3();
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

    private void evaluateExam() {
        pb_saveExam.setVisibility(View.VISIBLE);
        btn_save_exam.setVisibility(View.GONE);
        HashMap<String, String> params = new HashMap<>();
        params.put("short_name", name);
        params.put("question_bank_id", question_bank_id);
        params.put("student_id", student_id);
        params.put("reg_id", reg_id);
        params.put("acd_yr", academic_yr);
        params.put("weightage", weightage);
        params.put("marks_obt", marksbyteacher.getText().toString().trim());
        params.put("login_type", "T");
        params.put("operation", "uploadeval");
        System.out.println("UPLOAD_EVALUATE_PARAMS" + params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                "/question_bank_evaluate_upload_data", new JSONObject(params), response -> {
            System.out.println("UPLOAD_Evaluate_RESPONSE" + response);
            try {
                if (response.getString("status").equals("true")) {
                    pb_saveExam.setVisibility(View.GONE);
                    btn_save_exam.setVisibility(View.VISIBLE);
                    Toast.makeText(PenPaperEvaluationActivity.this, response.getString("message"),
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(PenPaperEvaluationActivity.this, EvaluationActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    pb_saveExam.setVisibility(View.GONE);
                    btn_save_exam.setVisibility(View.VISIBLE);
                    Toast.makeText(PenPaperEvaluationActivity.this, response.getString("error_msg"),
                            Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e) {
                pb_saveExam.setVisibility(View.GONE);
                btn_save_exam.setVisibility(View.VISIBLE);
                e.printStackTrace();
                Toast.makeText(PenPaperEvaluationActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            pb_saveExam.setVisibility(View.GONE);
            btn_save_exam.setVisibility(View.VISIBLE);
            System.out.println("ERROR_RESPONSE - " + error.getMessage());
            Toast.makeText(PenPaperEvaluationActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);


    }

    private void hooks() {
        exam_name = findViewById(R.id.exam_name);
        subject_name = findViewById(R.id.subject_name);
        class_name = findViewById(R.id.class_name);
        marks = findViewById(R.id.marks);
        rv_quesPaper = findViewById(R.id.rv_quesPaper);
        rv_ansPaper = findViewById(R.id.rv_ansPaper);
        marksbyteacher = findViewById(R.id.marksbyteacher);
        btn_save_exam = findViewById(R.id.btn_save_exam);
        pb_saveExam = findViewById(R.id.pb_saveExam);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}