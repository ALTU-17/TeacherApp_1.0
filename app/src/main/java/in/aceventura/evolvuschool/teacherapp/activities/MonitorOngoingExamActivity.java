package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.pojo.ClassSubjectPojo;
import in.aceventura.evolvuschool.teacherapp.pojo.EvaluationStudentListModel;
import in.aceventura.evolvuschool.teacherapp.pojo.ExaminationPojo;
import in.aceventura.evolvuschool.teacherapp.pojo.QBPojo;

import static in.aceventura.evolvuschool.teacherapp.R.layout;

public class MonitorOngoingExamActivity extends AppCompatActivity {

    String name, dUrl, newUrl, examName, exam_id, classSubject, className, class_id, section_id, subject_id, qb_name, question_bank_id, reg_id, academic_yr, question_bank_type;
    Spinner selectExam, selectClassSubject, selectQuesBank;
    Button search;
    DatabaseHelper mDatabaseHelper;
    ProgressDialog progressDialog;
    private ArrayList<String> listExams;
    private ArrayList<String> listClassSubjects;
    private ArrayList<String> listQuesBanks;
    private JSONArray result, examArray;
    EvaluationStudentListModel evaluationStudentListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_monitor_ongoing_exam);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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
                } else {
                    try {
                        examName = (String) parent.getItemAtPosition(position);
                        exam_id = getExam_id(position);
                    } catch (Exception e) {
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
                } else {
                    try {
                        class_id = getClassId(position);
                        subject_id = getSubjectId(position);
                        section_id = getSectionId(position);
                        getQuestionBanks();
                    } catch (Exception e) {
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
                } else {
                    try {
                        qb_name = (String) parent.getItemAtPosition(position);
                        question_bank_id = getQuestionBankId(position);
                        question_bank_type = getQuestionBankType(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Search Button
        search.setOnClickListener(v -> {

            if (selectExam.getSelectedItemPosition() <= 0 | selectClassSubject.getSelectedItemPosition() <= 0 | selectQuesBank.getSelectedItemPosition() <= 0) {
                Toast.makeText(MonitorOngoingExamActivity.this, "Select All Fields...", Toast.LENGTH_SHORT).show();
            } else {
//                    getExamAppearingStudentsList(question_bank_type);
                Intent intent = new Intent(MonitorOngoingExamActivity.this,
                        MonitorStudentsListActivity.class);
                startActivity(intent);
            }

        });

    }

    // get list of students appearing the exam
    private void getExamAppearingStudentsList(final String question_bank_type) {
        progressDialog.show();
        String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        HashMap<String, String> params = new HashMap<>();
        params.put("class_id", class_id);
        params.put("acd_yr", academic_yr);
        params.put("short_name", name);
        params.put("section_id", section_id);
        params.put("question_bank_id", question_bank_id);
        System.out.println(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi/get_stu_list_submitted_qb_upload",
                new JSONObject(params), response -> {
                    try {
                        if (response.getString("status").equals("true")) {
                            progressDialog.dismiss();
                            Log.d("MONITORING_STUDENT_LIST", String.valueOf(response));
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
                                evaluationStudentListModel = new EvaluationStudentListModel(mAcademicYr, mClassId, mFirstName, mLastName, mMidName, mParentId, mRollNo, mSectionId, mStudentId, question_bank_id, date, "", question_bank_type);
                            }

                            Intent intent = new Intent(MonitorOngoingExamActivity.this, EvaluationStudentsListActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable("students_lists", evaluationStudentListModel);
                            intent.putExtras(b);
                            startActivity(intent);
                        } else {
                            //false
                            System.out.println(response);
                            progressDialog.dismiss();
                            Toast.makeText(MonitorOngoingExamActivity.this, "No Students Available...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        System.out.println("ERROR - " + e.getMessage());
                        e.printStackTrace();
                        Toast.makeText(MonitorOngoingExamActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                Toast.makeText(MonitorOngoingExamActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);
    }

    private void getExamination(String name) {
        String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        HashMap<String, String> params = new HashMap<>();
        params.put("short_name", name);
        params.put("acd_yr", academic_yr);
        System.out.println(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi/get_exams", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("true")) {
                        listExams.clear();
                        Log.d("Examination_RESPONSE", String.valueOf(response));
                        examArray = response.getJSONArray("exams");
                        for (int i = 0; i < examArray.length(); i++) {
                            JSONObject jsonObject = examArray.getJSONObject(i);
                            String exam_id = jsonObject.getString("exam_id");
                            String name = jsonObject.getString("name");
                            String start_date = jsonObject.getString("start_date");
                            String end_date = jsonObject.getString("end_date");
                            String open_day = jsonObject.getString("open_day");
                            String term_id = jsonObject.getString("term_id");
                            String comment = jsonObject.getString("comment");
                            String academic_yr = jsonObject.getString("academic_yr");

                            ExaminationPojo examinationPojo = new ExaminationPojo(exam_id, name, start_date, end_date, open_day, term_id, comment, academic_yr);
                            //adding examNames to list for spinner
                            listExams.add(name);
                        }
                        listExams.add(0, "Select Examination");
                        selectExam.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.mytextview, listExams));
                    } else {
                        //false
                        System.out.println(response);
                    }
                } catch (JSONException e) {
                    System.out.println("ERROR - " + e.getMessage());
                    e.printStackTrace();
                    Toast.makeText(MonitorOngoingExamActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                Toast.makeText(MonitorOngoingExamActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);
    }

    private void getClassSubjects(String name, String reg_id) {
        String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        HashMap<String, String> params = new HashMap<>();
        params.put("short_name", name);
        params.put("acd_yr", academic_yr);
        params.put("reg_id", reg_id);
        System.out.println("CLASS_SUBJECT_PARAMS" + params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi/get_class_and_subject_allotted_to_teacher", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("true")) {
                        listClassSubjects.clear();
                        listQuesBanks.clear();
                        Log.d("CLASS_SUBJECT_RESPONSE", String.valueOf(response));
                        result = response.getJSONArray("classAndsubject");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject jsonObject = result.getJSONObject(i);
                            String sm_id = jsonObject.getString("sm_id");
                            String subject_name = jsonObject.getString("subject_name");
                            String section_name = jsonObject.getString("section_name");
                            String class_name = jsonObject.getString("class_name");
                            String class_id = jsonObject.getString("class_id");
                            String section_id = jsonObject.getString("section_id");
                            ClassSubjectPojo classSubjectPojo = new ClassSubjectPojo(sm_id, subject_name, class_name, class_id, section_id, section_name);
                            listClassSubjects.add(class_name + " " + section_name + " - " + subject_name);
                        }
                        listClassSubjects.add(0, "Select Class and Subject");
                        selectClassSubject.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.mytextview, listClassSubjects));
                    } else {
                        //false
                        System.out.println(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("ERROR" + e.getMessage());
                    Toast.makeText(MonitorOngoingExamActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                Toast.makeText(MonitorOngoingExamActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);
    }

    private void getQuestionBanks() {

        HashMap<String, String> params = new HashMap<>();
        params.put("class_id", class_id);
        params.put("subject_id", subject_id);
        params.put("acd_yr", academic_yr);
        params.put("exam_id", exam_id);
        params.put("short_name", name);
        System.out.println("QB_RESPONSE" + params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi/get_questionbanknames_of_subject_of_class", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("true")) {
                        listQuesBanks.clear();
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
                        selectQuesBank.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.mytextview, listQuesBanks));

                    } else {
                        //false
                        System.out.println(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("ERROR - " + e.getMessage());
                    Toast.makeText(MonitorOngoingExamActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                Toast.makeText(MonitorOngoingExamActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);

    }

    private String getSubjectId(int position) {
        String subjid = "";

        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position - 1);
            subjid = json.getString("sm_id");
            //Fetching name from that object
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Returning the name
        return subjid;
    }

    private String getClassId(int position) {
        String classId = "";
        try {
            JSONObject json = result.getJSONObject(position - 1);
            classId = json.getString("class_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return classId;
    }

    private String getSectionId(int position) {
        String section = "";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position - 1);

            //Fetching name from that object
            section = json.getString("section_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Returning the name
        return section;
    }

    private String getExam_id(int position) {
        String examID = "";
        try {
            //Getting object of given index
            JSONObject json = examArray.getJSONObject(position - 1);
            examID = json.getString("exam_id");
            //Fetching name from that object
        } catch (JSONException e) {
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
            //Fetching name from that object
        } catch (JSONException e) {
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
        } catch (JSONException e) {
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
        Intent intent = new Intent(MonitorOngoingExamActivity.this, OnlineExamDashboard.class);
        intent.putExtra("reg_id", reg_id);
        intent.putExtra("academic_yr", academic_yr);
        startActivity(intent);
        MonitorOngoingExamActivity.this.finish();
    }
}