package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.ClassSubjectPojo;
import in.aceventura.evolvuschool.teacherapp.pojo.EvaluationStudentListModel;
import in.aceventura.evolvuschool.teacherapp.pojo.ExaminationPojo;
import in.aceventura.evolvuschool.teacherapp.pojo.QBPojo;
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

public class EvaluationActivity extends AppCompatActivity {

    String name, dUrl, newUrl, examName, exam_id, classSubject, className, class_id, section_id, subject_id,
            qb_name, question_bank_id, reg_id, academic_yr, question_bank_type;
    Spinner selectExam, selectClassSubject, selectQuesBank;
    Button search;
    DatabaseHelper mDatabaseHelper;
    ProgressDialog progressDialog;
    EvaluationStudentListModel evaluationStudentListModel;
    private ArrayList<String> listExams;
    private ArrayList<String> listClassSubjects;
    private ArrayList<String> listQuesBanks;
    private ArrayList<EvaluationStudentListModel> studentsList = new ArrayList<>();
    private JSONArray result, examArray, classSubjectArray;

    /*//calling api to get the
    private View.OnTouchListener class_subject_onTouch = (v, event) -> {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            getClassSubjects(name, reg_id);
        }
        return true;
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
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
        ht_Teachernote.setText("Question Bank Evaluation");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        selectExam = findViewById(R.id.sp_select_exam);
        selectClassSubject = findViewById(R.id.sp_select_class_subject);
        selectQuesBank = findViewById(R.id.sp_select_quesBank);
        search = findViewById(R.id.search);

        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }

        listExams = new ArrayList<>();
        listClassSubjects = new ArrayList<>();
        listQuesBanks = new ArrayList<>();

        getExamination(name);
        getClassSubjects(name, reg_id);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        //Exam Spinner
        selectExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("Select Examination")) {
                    return;
                }
                else {
                    try {
                        examName = (String) parent.getItemAtPosition(position);
                        exam_id = getExam_id(position);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //ClassSubject Spinner
        selectClassSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Class and Subject")) {
                    return;
                }
                else {
                    try {
                        class_id = getClassId(position);
                        subject_id = getSubjectId(position);
                        section_id = getSectionId(position);
                        listQuesBanks.clear();
                        getQuestionBanks(class_id, subject_id);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //QuestionBank Spinner
        selectQuesBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select QuestionBank")) {
                    return;
                }
                else {
                    try {
                        qb_name = (String) parent.getItemAtPosition(position);
                        question_bank_id = getQuestionBankId(position);
                        question_bank_type = getQuestionBankType(position);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search.setOnClickListener(v -> {
            if (selectExam.getSelectedItemPosition() <= 0 | selectClassSubject.getSelectedItemPosition() <= 0 | selectQuesBank.getSelectedItemPosition() <= 0) {
                Toast.makeText(EvaluationActivity.this, "Select All Fields...", Toast.LENGTH_SHORT).show();
            }
            else {

                if (question_bank_type.equals("mcq")) {
                    getMcqStudentsList(question_bank_type);
                }
                else if (question_bank_type.equals("upload")) {
                    getUploadStudentsList(question_bank_type);
                }
            }

        });


        String searchBtnShowed = SharedClass.getInstance(this).getEvaluationScreen1();
        if (searchBtnShowed.equals("No")) {

            //showing guideView
            ConstantsFile.showGuideView(this, search, "Search Students", "Click here to see the list of " +
                    "students" + " " + "who has submitted the Examination.");

            //setting that the guideview was shown "Yes"
            SharedClass.getInstance(this).setEvaluationScreen1();
        }

//        selectClassSubject.setOnTouchListener(class_subject_onTouch);

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
                        Intent intent = new Intent(EvaluationActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(EvaluationActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(EvaluationActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(EvaluationActivity.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///
    }//Oncreate

    // qb_type => upload
    private void getUploadStudentsList(final String question_bank_type) {
        studentsList.clear();
        progressDialog.show();
        String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        HashMap<String, String> params = new HashMap<>();
        params.put("class_id", class_id);
        params.put("acd_yr", academic_yr);
        params.put("short_name", name);
        params.put("section_id", section_id);
        params.put("question_bank_id", question_bank_id);
        System.out.println(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                "/get_stu_list_submitted_qb_upload", new JSONObject(params), response -> {
            try {
                if (response.getString("status").equals("true")) {
                    progressDialog.dismiss();
                    Log.d("STUDENTS_LIST_RESPONSE", String.valueOf(response));
                    result = response.getJSONArray("student_list_for_upload_type");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jsonObject = result.getJSONObject(i);
                        String mAcademicYr = jsonObject.getString("academic_yr");
                        String mClassId = jsonObject.getString("class_id");
                        String mFirstName = jsonObject.getString("first_name");
                        String mMidName = jsonObject.getString("mid_name");
                        String mLastName = jsonObject.getString("last_name");
                        String mParentId = jsonObject.getString("parent_id");
                        String mRollNo = jsonObject.getString("roll_no");
                        String mSectionId = jsonObject.getString("section_id");
                        String mStudentId = jsonObject.getString("student_id");
                        String date = jsonObject.getString("date");
                        String up_id = jsonObject.getString("up_id");
                        int count = Integer.parseInt(jsonObject.getString("count"));

                        evaluationStudentListModel = new EvaluationStudentListModel(mAcademicYr, mClassId,
                                mFirstName, mLastName, mMidName, mParentId, mRollNo, mSectionId, mStudentId,
                                question_bank_id, date, up_id, question_bank_type, count);

                        studentsList.add(evaluationStudentListModel);
                    }

                    Intent intent = new Intent(EvaluationActivity.this, EvaluationStudentsListActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("students_lists", studentsList);
                    intent.putExtras(b);
                    startActivity(intent);

                }
                else {
                    //false
                    System.out.println(response);
                    progressDialog.dismiss();
                    Toast.makeText(EvaluationActivity.this, "No Students has submitted the exam...",
                            Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                progressDialog.dismiss();
                System.out.println("ERROR - " + e.getMessage());
                e.printStackTrace();
                Toast.makeText(EvaluationActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            progressDialog.dismiss();
            System.out.println("ERROR_RESPONSE - " + error.getMessage());
            Toast.makeText(EvaluationActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);
    }

    // qb_type => mcq
    private void getMcqStudentsList(final String question_bank_type) {
        studentsList.clear();
        progressDialog.show();
        String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        HashMap<String, String> params = new HashMap<>();
        params.put("class_id", class_id);
        params.put("acd_yr", academic_yr);
        params.put("short_name", name);
        params.put("section_id", section_id);
        params.put("question_bank_id", question_bank_id);
        System.out.println(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                "/get_stu_list_submitted_qb", new JSONObject(params), response -> {
                    try {
                        if (response.getString("status").equals("true")) {
                            progressDialog.dismiss();
                            Log.d("STUDENTS_LIST_RESPONSE", String.valueOf(response));
                            result = response.getJSONArray("student_list");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject jsonObject = result.getJSONObject(i);
                                String mAcademicYr = jsonObject.getString("academic_yr");
                                String mClassId = jsonObject.getString("class_id");
                                String mFirstName = jsonObject.getString("first_name");
                                String mMidName = jsonObject.getString("mid_name");
                                String mLastName = jsonObject.getString("last_name");
                                String mParentId = jsonObject.getString("parent_id");
                                String mRollNo = jsonObject.getString("roll_no");
                                String mSectionId = jsonObject.getString("section_id");
                                String mStudentId = jsonObject.getString("student_id");
                                String date = jsonObject.getString("date");
                                int count = Integer.parseInt(jsonObject.getString("count"));
                                evaluationStudentListModel = new EvaluationStudentListModel(mAcademicYr, mClassId,
                                        mFirstName, mLastName, mMidName, mParentId, mRollNo, mSectionId, mStudentId,
                                        question_bank_id, date, "", question_bank_type, count);
                                studentsList.add(evaluationStudentListModel);
                            }

                            Intent intent = new Intent(EvaluationActivity.this, EvaluationStudentsListActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable("students_lists", studentsList);
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                        else {
                            //false
                            System.out.println(response);
                            progressDialog.dismiss();
                            Toast.makeText(EvaluationActivity.this, "No Students has submitted the exam...",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (JSONException e) {
                        progressDialog.dismiss();
                        System.out.println("ERROR - " + e.getMessage());
                        e.printStackTrace();
                        Toast.makeText(EvaluationActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            progressDialog.dismiss();
            System.out.println("ERROR_RESPONSE - " + error.getMessage());
            Toast.makeText(EvaluationActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);
    }

    private void getExamination(String name) {
        String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        HashMap<String, String> params = new HashMap<>();
        params.put("short_name", this.name);
        params.put("acd_yr", academic_yr);
        System.out.println(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi/get_exams",
                new JSONObject(params), response -> {
            try {
                if (response.getString("status").equals("true")) {
                    listExams.clear();
                    Log.d("Examination_RESPONSE", String.valueOf(response));
                    examArray = response.getJSONArray("exams");
                    for (int i = 0; i < examArray.length(); i++) {
                        JSONObject jsonObject = examArray.getJSONObject(i);
                        String exam_id = jsonObject.getString("exam_id");
                        String name1 = jsonObject.getString("name");
                        String start_date = jsonObject.getString("start_date");
                        String end_date = jsonObject.getString("end_date");
                        String open_day = jsonObject.getString("open_day");
                        String term_id = jsonObject.getString("term_id");
                        String comment = jsonObject.getString("comment");
                        String academic_yr1 = jsonObject.getString("academic_yr");

                        ExaminationPojo examinationPojo = new ExaminationPojo(exam_id, name1, start_date,
                                end_date, open_day, term_id, comment, academic_yr1);
                        //adding examNames to list for spinner
                        listExams.add(name1);
                    }
                    listExams.add(0, "Select Examination");
                    selectExam.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.mytextview,
                            listExams));
                }
                else {
                    //false
                    System.out.println(response);
                }
            }
            catch (JSONException e) {
                System.out.println("ERROR - " + e.getMessage());
                e.printStackTrace();
                Toast.makeText(EvaluationActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            System.out.println("ERROR_RESPONSE - " + error.getMessage());
            Toast.makeText(EvaluationActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);
    }

    private void getClassSubjects(String name, String reg_id) {
        String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        HashMap<String, String> params = new HashMap<>();
        params.put("short_name", this.name);
        params.put("acd_yr", academic_yr);
        params.put("reg_id", this.reg_id);
        System.out.println("CLASS_SUBJECT_PARAMS" + params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                "/get_class_and_subject_allotted_to_teacher", new JSONObject(params), response -> {
            try {
                if (response.getString("status").equals("true")) {
                    listClassSubjects.clear();
                    listQuesBanks.clear();
                    Log.d("CLASS_SUBJECT_RESPONSE", String.valueOf(response));
                    classSubjectArray = response.getJSONArray("classAndsubject");
                    for (int i = 0; i < classSubjectArray.length(); i++) {
                        JSONObject jsonObject = classSubjectArray.getJSONObject(i);
                        String sm_id = jsonObject.getString("sm_id");
                        String subject_name = jsonObject.getString("subject_name");
                        String section_name = jsonObject.getString("section_name");
                        String class_name = jsonObject.getString("class_name");
                        String class_id = jsonObject.getString("class_id");
                        String section_id = jsonObject.getString("section_id");
                        ClassSubjectPojo classSubjectPojo = new ClassSubjectPojo(sm_id, subject_name, class_name
                                , class_id, section_id, section_name);
                        listClassSubjects.add(class_name + " " + section_name + " - " + subject_name);
                    }
                    listClassSubjects.add(0, "Select Class and Subject");
                    selectClassSubject.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                            R.layout.mytextview, listClassSubjects));
                }
                else {
                    //false
                    System.out.println(response);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                System.out.println("ERROR" + e.getMessage());
                Toast.makeText(EvaluationActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            System.out.println("ERROR_RESPONSE - " + error.getMessage());
            Toast.makeText(EvaluationActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);
    }

    private void getQuestionBanks(String class_id, String subject_id) {
        listQuesBanks.clear();
        HashMap<String, String> params = new HashMap<>();
        params.put("class_id", class_id);
        params.put("subject_id", subject_id);
        params.put("acd_yr", academic_yr);
        params.put("exam_id", exam_id);
        params.put("short_name", name);
        System.out.println("QB_RESPONSE" + params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                "/get_questionbanknames_of_subject_of_class", new JSONObject(params), response -> {
                    try {
                        if (response.getString("status").equals("true")) {
                            Log.d("QB_RESPONSE", String.valueOf(response));
                            result = response.getJSONArray("question_bank_names");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject jsonObject = result.getJSONObject(i);
                                String question_bank_id = jsonObject.getString("question_bank_id");
                                String qb_name = jsonObject.getString("qb_name");
                                String qb_type = jsonObject.getString("qb_type");
                                QBPojo qbPojo = new QBPojo(question_bank_id, qb_name, qb_type);
                                //adding examNames to list for spinner
                                listQuesBanks.add(qb_name);
                            }
                            listQuesBanks.add(0, "Select QuestionBank");
                            selectQuesBank.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                                    R.layout.mytextview, listQuesBanks));

                        }
                        else {
                            //false
                            System.out.println(response);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("ERROR - " + e.getMessage());
                        Toast.makeText(EvaluationActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            System.out.println("ERROR_RESPONSE - " + error.getMessage());
            Toast.makeText(EvaluationActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);

    }


    //get subject Id
    private String getSubjectId(int position) {
        String subjid = "";

        try {
            //Getting object of given index
            JSONObject json = classSubjectArray.getJSONObject(position - 1);
            subjid = json.getString("sm_id");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        //Returning the id
        return subjid;
    }

    private String getClassId(int position) {
        String classId = "";
        try {
            JSONObject json = classSubjectArray.getJSONObject(position - 1);
            classId = json.getString("class_id");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the id
        return classId;
    }

    private String getSectionId(int position) {
        String section = "";
        try {
            //Getting object of given index
            JSONObject json = classSubjectArray.getJSONObject(position - 1);

            //Fetching name from that object
            section = json.getString("section_id");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        //Returning the id
        return section;
    }

    private String getExam_id(int position) {
        String examID = "";
        try {
            //Getting object of given index
            JSONObject json = examArray.getJSONObject(position - 1);
            examID = json.getString("exam_id");
            //Fetching name from that object
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return examID;
    }

    private String getQuestionBankId(int position) {
        String qbID = "";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position - 1);
            qbID = json.getString("question_bank_id");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return qbID;
    }

    private String getQuestionBankType(int position) {
        String qbType = "";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position - 1);
            qbType = json.getString("qb_type");
            //Fetching name from that object
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return qbType;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EvaluationActivity.this, OnlineExamDashboard.class);
        intent.putExtra("reg_id", reg_id);
        intent.putExtra("academic_yr", academic_yr);
        startActivity(intent);
        EvaluationActivity.this.finish();
    }
}
