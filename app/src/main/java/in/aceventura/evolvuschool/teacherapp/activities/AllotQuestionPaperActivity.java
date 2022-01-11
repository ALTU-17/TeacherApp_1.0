package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
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
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.ClassSubjectPojo;
import in.aceventura.evolvuschool.teacherapp.pojo.QBPojo;
import in.aceventura.evolvuschool.teacherapp.utils.ConstantsFile;

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

public class AllotQuestionPaperActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    ProgressDialog progressDialog;
    Spinner selectClassSubject, selectQuesBank;
    String name, newUrl, dUrl, reg_id, academic_yr, class_id, subject_id, section_id, qb_name, question_bank_id;
    Button btn_allot;
    private ArrayList<String> listClassSubjects;
    private ArrayList<String> listQuesBanks;
    private JSONArray result, examArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(layout.activity_allot_question_paper);

        getSupportActionBar().hide();
        View tb_main1 = findViewById(R.id.icd_tb_homeworkdetails);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("("+SharedClass.getInstance(getApplicationContext()).getAcademicYear()+")");
        tv_academic_yr.setText("("+SharedClass.getInstance(getApplicationContext()).getAcademicYear()+")");

        school_title.setText(" Evolvu Teacher App");
        ht_Teachernote.setText("Upload Question Bank");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        selectClassSubject = findViewById(R.id.sp_select_class_subject);
        selectQuesBank = findViewById(R.id.sp_select_quesBank);
        btn_allot = findViewById(R.id.btn_allot);

        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }

        listClassSubjects = new ArrayList<>();
        listQuesBanks = new ArrayList<>();

        getClassSubjects(name, reg_id);

        //ClassSubject Spinner
        selectClassSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Class")) {
                    return;
                }
                else {
                    try {
                        class_id = getClassId(position);
                        subject_id = getSubjectId(position);
                        section_id = getSectionId(position);
                        listQuesBanks.clear();
                        getQuestionBanks(class_id,subject_id);
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
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btn_allot.setOnClickListener(v -> {
            if (selectClassSubject.getSelectedItemPosition() <= 0 | selectQuesBank.getSelectedItemPosition() <= 0 | selectQuesBank.getSelectedItemPosition() <= 0) {
                Toast.makeText(AllotQuestionPaperActivity.this, "Select All Fields...", Toast.LENGTH_SHORT).show();
            }
            else {
                allotQuestionBank();
            }
        });

        // by default showed = No
        String allotDeallotScreen2Showed = SharedClass.getInstance(this).getAllotDeallotScreen2();
        //Guideview check
        if (allotDeallotScreen2Showed.equals("No")) {

            //showing guideView
            ConstantsFile.showGuideView(this, btn_allot, "Allot Question Bank", "Click here to allot " +
                    "Question Banks to selected Class & Subject");

            //setting that the guideview was seen
            SharedClass.getInstance(this).setAllotDeallotScreen2();
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
                        Intent intent = new Intent(AllotQuestionPaperActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(AllotQuestionPaperActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(AllotQuestionPaperActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(AllotQuestionPaperActivity.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }

    }

    private void allotQuestionBank() {
        progressDialog.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("short_name", name);
        params.put("operation", "create");
        params.put("class_id", class_id);
        params.put("section_id", section_id);
        params.put("subject_id", subject_id);
        params.put("question_bank_id", question_bank_id);
        params.put("acd_yr", academic_yr);
        params.put("teacher_id", reg_id);
        System.out.println("ALLOT_QB" + params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                "/question_bank_allot_dellot", new JSONObject(params), response -> {
            Log.d("ALLOTQB", response.toString());
            try {
                if (response.getString("status").equals("true")) {
                    progressDialog.dismiss();
                    Toast.makeText(AllotQuestionPaperActivity.this, "Question Bank Allotted",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AllotQuestionPaperActivity.this, AllotDeallotActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    progressDialog.dismiss();
                    //false
                    System.out.println(response);
                    Toast.makeText(AllotQuestionPaperActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e) {
                progressDialog.dismiss();
                e.printStackTrace();
                System.out.println("ERROR" + e.getMessage());
                Toast.makeText(AllotQuestionPaperActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            progressDialog.dismiss();
            System.out.println("ERROR_RESPONSE - " + error.getMessage());
            Toast.makeText(AllotQuestionPaperActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
        params.put("teacher_id", reg_id);
        params.put("short_name", name);
        System.out.println("QB_RESPONSE" + params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi/get_qbname_by_class"
                , new JSONObject(params), response -> {
            try {

                if (response.getString("status").equals("true")) {
                    Log.d("QB_RESPONSE", String.valueOf(response));
                    result = response.getJSONArray("qb_data");
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
                    selectQuesBank.setAdapter(new ArrayAdapter<>(getApplicationContext(), layout.mytextview,
                            listQuesBanks));
                }
                else {
                    //false
                    System.out.println(response);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                System.out.println("ERROR - " + e.getMessage());
                Toast.makeText(AllotQuestionPaperActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            System.out.println("ERROR_RESPONSE - " + error.getMessage());
            Toast.makeText(AllotQuestionPaperActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
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
        }
        catch (JSONException e) {
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
        }
        catch (JSONException e) {
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
        }
        catch (JSONException e) {
            e.printStackTrace();
        }


        //Returning the name
        return section;
    }

    private String getQuestionBankId(int position) {
        String qbID = "";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position - 1);
            qbID = json.getString("question_bank_id");
            //Fetching name from that object
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return qbID;
    }

    private void getClassSubjects(String name, String reg_id) {

        String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        HashMap<String, String> params = new HashMap<>();
        params.put("short_name", name);
        params.put("acd_yr", academic_yr);
        params.put("reg_id", reg_id);
        System.out.println("CLASS_SUBJECT_PARAMS" + params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                "/get_class_and_subject_allotted_to_teacher", new JSONObject(params), response -> {
            try {
                if (response.getString("status").equals("true")) {
                    listClassSubjects.clear();
                    listQuesBanks.clear();
                    Log.d("CLASS_SECTION_RESPONSE", String.valueOf(response));
                    result = response.getJSONArray("classAndsubject");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jsonObject = result.getJSONObject(i);
                        String sm_id = jsonObject.getString("sm_id");
                        String subject_name = jsonObject.getString("subject_name");
                        String class_name = jsonObject.getString("class_name");
                        String class_id = jsonObject.getString("class_id");
                        String section_id = jsonObject.getString("section_id");
                        String section_name = jsonObject.getString("section_name");
                        ClassSubjectPojo classSubjectPojo = new ClassSubjectPojo(sm_id, subject_name, class_name
                                , class_id, section_id, section_name);
                        listClassSubjects.add(class_name + " " + section_name + " " + subject_name);
                    }

                    listClassSubjects.add(0, "Select Class");

                    selectClassSubject.setAdapter(new ArrayAdapter<>(getApplicationContext(), layout.mytextview,
                            listClassSubjects));

                }
                else {
                    //false
                    System.out.println(response);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                System.out.println("ERROR" + e.getMessage());
                Toast.makeText(AllotQuestionPaperActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            System.out.println("ERROR_RESPONSE - " + error.getMessage());
            Toast.makeText(AllotQuestionPaperActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
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
        super.onBackPressed();
    }

}