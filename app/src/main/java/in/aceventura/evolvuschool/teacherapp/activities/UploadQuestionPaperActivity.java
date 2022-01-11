package in.aceventura.evolvuschool.teacherapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.ClassSubjectPojo;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImages;
import in.aceventura.evolvuschool.teacherapp.pojo.ExaminationPojo;
import in.aceventura.evolvuschool.teacherapp.utils.FileManager;
import in.aceventura.evolvuschool.teacherapp.utils.PermissionUtils;

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
public class UploadQuestionPaperActivity extends AppCompatActivity {

    public static int REQUEST_CODE_GET_FILE_PATH = 1;
    public static int REQUEST_CODE_GET_CAMERA_FILE_PATH = 222;
    public static String fileBase64Code, fileName;
    private static int PICK_PDF_REQUEST = 12;
    final Random rand = new Random();
    DatabaseHelper mDatabaseHelper;
    LinearLayout micici_listViewPdf, micici_listView;
    int diceRoll;
    Uri uri, outputFileUri, selectedPDF, filePathpdf;
    Bitmap bitmap;
    String name, dUrl, newUrl, pdfPath, displayName, strFile, PDFpath, path, examName, className, subjectName,
            randomId, class_id, section_id, subject_id, exam_id, reg_id, academic_yr;
    RequestQueue requestQueue;
    ArrayList<EventImages> meventImagesArrayList;
    ArrayList<EventImages> meventImagesArrayListPDF;
    ArrayList<String> fileSizeArray = new ArrayList<>();
    EventImages eventImages;
    File pdfFile;
    long totalMb = 0;
    TextView txtpdfname;
    ArrayList<String> attachmentsfile = new ArrayList<>();
    ArrayList<String> attachmentsfile1 = new ArrayList<>();
    private Spinner selectExamSpinner, selectClassSubjectSpinner;
    private EditText etQuestionBankName, etWeightage, exam_instructions;
    private ProgressDialog progressDialog;
    private ArrayList<String> listClass;
    private ArrayList<String> listClassSubjects = new ArrayList<>();
    private ArrayList<String> listExams;
    private JSONArray result;
    private JSONArray classResult;
    private JSONArray subjectResult;
    private JSONArray classSubjectArray;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(layout.activity_upload_question_paper);
        //For camera image blur
        getSupportActionBar().hide();
        View tb_main1 = findViewById(R.id.icd_tb_homeworkdetails);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("("+SharedClass.getInstance(getApplicationContext()).getAcademicYear()+")");

        school_title.setText(" Evolvu Teacher App");
        ht_Teachernote.setText("Upload Question Bank");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Random rand = new Random();
        randomId = String.format("%08d", rand.nextInt(10000));//8 digit
        System.out.println("RANDOMID" + randomId);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            reg_id = bundle.getString("reg_id");
            academic_yr = bundle.getString("academic_yr");
        }

        selectExamSpinner = findViewById(R.id.sp_select_exam);
        selectClassSubjectSpinner = findViewById(R.id.sp_select_class_subject);
        etQuestionBankName = findViewById(R.id.et_question_bank_name);
        exam_instructions = findViewById(R.id.exam_instructions);
        etWeightage = findViewById(R.id.et_weightage);


        Button saveExamDetails = findViewById(R.id.save);
        Button save_and_complete = findViewById(R.id.save_and_complete);
        TextView addNotes = findViewById(R.id.addAttachments);
        micici_listViewPdf = findViewById(R.id.micici_listViewpdf);
        micici_listView = findViewById(R.id.micici_listView);
        requestQueue = Volley.newRequestQueue(getBaseContext());
        meventImagesArrayList = new ArrayList<>();
        meventImagesArrayListPDF = new ArrayList<>();
        listExams = new ArrayList<>();
        eventImages = new EventImages();

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);
        Log.e("Vakakakak","uploa");
        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        //Adding notes button
        addNotes.setOnClickListener(view -> {
            if (PermissionUtils.checkPermission(UploadQuestionPaperActivity.this)) {
                selectNotes(savedInstanceState);
            }
            else {
                Toast.makeText(UploadQuestionPaperActivity.this, "Permissions not granted !!",
                        Toast.LENGTH_SHORT).show();
            }

        });

        //Exam Spinner
        selectExamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("Select Examination")) {
                    return;
                }
                else {
                    examName = (String) parent.getItemAtPosition(position);
                    exam_id = getExam_id(position);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //ClassSubject Spinner
        selectClassSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Class and Subject")) {
                    return;
                }
                else {
                    try {
                        class_id = getClassId(position);
                        subject_id = getSubjectId(position);
//                        section_id = getSectionId(position);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        saveExamDetails.setOnClickListener(v -> {
            if (selectExamSpinner.getSelectedItemId() <= 0) {
                Toast.makeText(UploadQuestionPaperActivity.this, "Select Examination", Toast.LENGTH_SHORT).show();
            }
            else if (selectClassSubjectSpinner.getSelectedItemId() <= 0) {
                Toast.makeText(UploadQuestionPaperActivity.this, "Select Class & Subject", Toast.LENGTH_SHORT).show();
            }
            else if (etQuestionBankName.getText().toString().trim().isEmpty()) {
                Toast.makeText(UploadQuestionPaperActivity.this, "Enter Question Bank Name", Toast.LENGTH_SHORT).show();
            }
            else if (etWeightage.getText().toString().trim().isEmpty()) {
                Toast.makeText(UploadQuestionPaperActivity.this, "Enter Weightage", Toast.LENGTH_SHORT).show();
            }
            else if (fileSizeArray.isEmpty()) {
                Toast.makeText(UploadQuestionPaperActivity.this, "Upload Question Papers", Toast.LENGTH_SHORT).show();
            }
            else {
                saveExam("1");
            }
        });

        save_and_complete.setOnClickListener(v -> {
            if (selectExamSpinner.getSelectedItemId() <= 0) {
                Toast.makeText(UploadQuestionPaperActivity.this, "Select Examination", Toast.LENGTH_SHORT).show();
            }
            else if (selectClassSubjectSpinner.getSelectedItemId() <= 0) {
                Toast.makeText(UploadQuestionPaperActivity.this, "Select Class & Subject", Toast.LENGTH_SHORT).show();
            }
            else if (etQuestionBankName.getText().toString().trim().isEmpty()) {
                Toast.makeText(UploadQuestionPaperActivity.this, "Enter Question Bank Name", Toast.LENGTH_SHORT).show();
            }
            else if (etWeightage.getText().toString().trim().isEmpty()) {
                Toast.makeText(UploadQuestionPaperActivity.this, "Enter Weightage", Toast.LENGTH_SHORT).show();
            }
            else if (fileSizeArray.isEmpty()) {
                Toast.makeText(UploadQuestionPaperActivity.this, "Upload Question Papers", Toast.LENGTH_SHORT).show();
            }
            else {
                saveExam("2");
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
                        Intent intent = new Intent(UploadQuestionPaperActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(UploadQuestionPaperActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(UploadQuestionPaperActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(UploadQuestionPaperActivity.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }


        getExamination();
        getClassSubjects(name, reg_id);


    }//OnCreate

    private String getExam_id(int position) {
        String exmID = "";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position - 1);
            exmID = json.getString("exam_id");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the exmId
        return exmID;

    }

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
        //Returning the name
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
        //Returning the name
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
        //Returning the name
        return section;
    }


    private void getClassSubjects(String name, String reg_id) {
        String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        HashMap<String, String> params = new HashMap<>();
        params.put("short_name", this.name);
        params.put("acd_yr", academic_yr);
        params.put("reg_id", this.reg_id);
        System.out.println("CLASS_SUBJECT_PARAMS" + params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                "/get_class_and_subject_allotted_to_teacher_for_qb", new JSONObject(params), response -> {
            try {
                if (response.getString("status").equals("true")) {
                    listClassSubjects.clear();
                    Log.d("CLASS_SUBJECT_RESPONSE", String.valueOf(response));
                    classSubjectArray = response.getJSONArray("classAndsubject");
                    for (int i = 0; i < classSubjectArray.length(); i++) {
                        JSONObject jsonObject = classSubjectArray.getJSONObject(i);
                        /*String sm_id = jsonObject.getString("sm_id");
                        String subject_name = jsonObject.getString("subject_name");
                        String section_name = jsonObject.getString("section_name");
                        String class_name = jsonObject.getString("class_name");
                        String class_id = jsonObject.getString("class_id");
                        String section_id = jsonObject.getString("section_id");
                        ClassSubjectPojo classSubjectPojo = new ClassSubjectPojo(sm_id, subject_name, class_name
                                , class_id, section_id, section_name);
                        listClassSubjects.add(class_name + " " + section_name + " - " + subject_name);*/


                        String class_name = jsonObject.getString("classname");
                        String sm_id = jsonObject.getString("sm_id");
                        String subject_name = jsonObject.getString("subject_name");
                        String class_id = jsonObject.getString("class_id");

                        /*String section_name = jsonObject.getString("section_name");
                        String section_id = jsonObject.getString("section_id");*/
                        ClassSubjectPojo classSubjectPojo = new ClassSubjectPojo(class_name,sm_id, subject_name
                                , class_id/*, section_id, section_name*/);
                        listClassSubjects.add(class_name + " - " + subject_name);
                    }
                    listClassSubjects.add(0, "Select Class and Subject");
                    selectClassSubjectSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(),
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
                Toast.makeText(UploadQuestionPaperActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            System.out.println("ERROR_RESPONSE - " + error.getMessage());
            Toast.makeText(UploadQuestionPaperActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);
    }


    private void getExamination() {
        String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        HashMap<String, String> params = new HashMap<>();
        params.put("short_name", name);
        params.put("acd_yr", academic_yr);
        System.out.println(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" + "/get_exams",
                new JSONObject(params), response -> {
            try {
                //true
                if (response.getString("status").equals("true")) {
                    listExams.clear();
                    Log.d("Examination_RESPONSE", String.valueOf(response));
                    result = response.getJSONArray("exams");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jsonObject = result.getJSONObject(i);
                        String exam_id = jsonObject.getString("exam_id");
                        String name = jsonObject.getString("name");
                        String start_date = jsonObject.getString("start_date");
                        String end_date = jsonObject.getString("end_date");
                        String open_day = jsonObject.getString("open_day");
                        String term_id = jsonObject.getString("term_id");
                        String comment = jsonObject.getString("comment");
                        String academic_yr1 = jsonObject.getString("academic_yr");

                        ExaminationPojo examinationPojo = new ExaminationPojo(exam_id, name, start_date,
                                end_date, open_day, term_id, comment, academic_yr1);
                        //adding examNames to list for spinner
                        listExams.add(name);
                    }
                    listExams.add(0, "Select Examination");
                    selectExamSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), layout.mytextview,
                            listExams));
                }
                else {
                    //false
                    System.out.println(response);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(UploadQuestionPaperActivity.this, "Error1..Please Try again.." + ".",
                        Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            System.out.println("ERROR_RESPONSE - " + error.getMessage());
            Toast.makeText(UploadQuestionPaperActivity.this, "Error2..Please Try again...", Toast.LENGTH_SHORT).show();
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);
    }

    private void saveExam(String button_value) {
        progressDialog.show();
        final String reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "upload_que_paper");
        params.put("button_value", button_value);
        params.put("exam_id", exam_id);
        params.put("class_id", class_id);
        params.put("subject_id", subject_id);
        params.put("reg_id", reg_id);
        params.put("qb_name", etQuestionBankName.getText().toString());
        params.put("weightage", etWeightage.getText().toString());
        params.put("instructions", exam_instructions.getText().toString()); //NewParam 25/08/2020
        // (LM)
        params.put("acd_yr", academic_yr);
        params.put("random_no", randomId);
        params.put("short_name", name);
        params.put("filesize", fileSizeArray.toString());

        if (meventImagesArrayListPDF.size() > 0 && meventImagesArrayList.size() > 0) {
            for (int i = 0; i < meventImagesArrayListPDF.size(); i++) {
                String base = '"' + meventImagesArrayListPDF.get(i).getPdfString() + '"';
                String fname = '"' + meventImagesArrayListPDF.get(i).getImgName() + '"';

                attachmentsfile.add(base);
                attachmentsfile1.add(fname);
            }
            for (int j = 0; j < meventImagesArrayList.size(); j++) {
                String base = '"' + meventImagesArrayList.get(j).getFileBase64Code() + '"';
                String fname = '"' + meventImagesArrayList.get(j).getImgName() + '"';
                attachmentsfile.add(base);
                attachmentsfile1.add(fname);

            }
        }
        else if (meventImagesArrayListPDF.size() > 0) {
            for (int i = 0; i < meventImagesArrayListPDF.size(); i++) {

                String base = '"' + meventImagesArrayListPDF.get(i).getPdfString() + '"';
                String fname = '"' + meventImagesArrayListPDF.get(i).getImgName() + '"';

                attachmentsfile.add(base);
                attachmentsfile1.add(fname);
            }
        }
        else if (meventImagesArrayList.size() > 0) {
            for (int i = 0; i < meventImagesArrayList.size(); i++) {

                String base = '"' + meventImagesArrayList.get(i).getFileBase64Code() + '"';
                String fname = '"' + meventImagesArrayList.get(i).getImgName() + '"';

                attachmentsfile.add(base);
                attachmentsfile1.add(fname);
            }
        }


        if (attachmentsfile.size() == 0 && attachmentsfile1.size() == 0) {
            params.put("file_name", "");
        }
        else {
            String fName = attachmentsfile1.toString();
            params.put("file_name", fName);
        }
        System.out.println("UPLOAD_QUES_PAPER" + params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" + "/question_bank",
                new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("QUES_PAPER_RESPONSE" + response);
                try {
                    if (response.getString("status").equals("true")) {
                        progressDialog.dismiss();
                        Toast.makeText(UploadQuestionPaperActivity.this, "Question Paper Uploaded " +
                                "Successfully!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UploadQuestionPaperActivity.this, OnlineExamActivity.class);
                        intent.putExtra("academic_yr", academic_yr);
                        intent.putExtra("reg_id", reg_id);
                        startActivity(intent);
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(UploadQuestionPaperActivity.this, "Unable to upload...Try " + "again",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    System.out.println("save_exam" + e.getMessage());
                    Toast.makeText(UploadQuestionPaperActivity.this, "Error..Please Try again...",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> {
            progressDialog.dismiss();
            error.printStackTrace();
            System.out.println("ERROR_RESPONSE - " + error.getMessage());
            Toast.makeText(UploadQuestionPaperActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);
    }

    private void selectNotes(final Bundle savedInstanceState) {

        final CharSequence[] options = {"Take Photo", "Choose Images", "Choose Files", "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Choose File");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    showCameraChooser(savedInstanceState);
                }
                else if (options[item].equals("Choose Images")) {
                    showFileChooser1();
                }
                else if (options[item].equals("Choose Files")) {
                    showPDFChooser();
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //Code for gallery images
    protected void showFileChooser1() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent = Intent.createChooser(intent, "Choose a file");
        startActivityForResult(intent, REQUEST_CODE_GET_FILE_PATH);
    }

    //Code for file manager files
    private void showPDFChooser() {
        Intent intent = new Intent();
        intent.setType("application/*"); //show all file format except images...
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST);
    }

    //Code for camera files
    protected void showCameraChooser(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            try {
                diceRoll = rand.nextInt(100) + 1;
                //For external directory
                File photo = new File(Environment.getExternalStorageDirectory(), "clickedPhoto" + diceRoll +
                        ".jpeg");
                if (photo.exists()) {
                    photo.mkdirs();
                }
                Uri outputFileUri = Uri.fromFile(photo);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent, REQUEST_CODE_GET_CAMERA_FILE_PATH);
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(UploadQuestionPaperActivity.this, "CameraError1" + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                System.out.println("Error" + e.getMessage());
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        outState.putString("photopath", String.valueOf(outputFileUri));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (savedInstanceState.containsKey("photopath")) {
            outputFileUri = Uri.parse(savedInstanceState.getString("photopath"));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @SuppressLint("SourceLockedOrientationActivity,SwitchIntDef")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Image file from gallery
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GET_FILE_PATH && resultCode == Activity.RESULT_OK) {

            assert data != null;
            if (data.getData() != null) {
                uri = data.getData();
                InputStream stream;
                try {
                    stream = this.getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);
                    assert stream != null;
                    stream.close();
                    final int maxSize = 1000; //500
                    int outWidth;
                    int outHeight;
                    int inWidth = bitmap.getWidth();
                    int inHeight = bitmap.getHeight();
                    if (inWidth > inHeight) {
                        outWidth = maxSize;
                        outHeight = (inHeight * maxSize) / inWidth;
                    }
                    else {
                        outHeight = maxSize;
                        outWidth = (inWidth * maxSize) / inHeight;
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, true);
                    ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream1); //90

                    byte[] byte_arr = stream1.toByteArray();
                    File tmpDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                            "/SchoolApp/");
                    tmpDir.mkdir();

                    fileBase64Code = Base64.encodeToString(byte_arr, Base64.DEFAULT);

                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                //To getFile name
                File f = new File(uri.getPath());
                fileName = f.getName().replace("4142-15F8:", "").trim() + ".jpeg";

                //upload attachment method

                progressDialog.show();
                HashMap<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("filename", fileName);
                params.put("filedata", fileBase64Code);
                params.put("doc_type_folder", "uploaded_ques_paper");
                params.put("random_no", randomId);
                System.out.println("GALLERY_PARAMS" + params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                        "/upload_question_paper_into_temp_folder", new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("GALLERY_FILE" + response);
                        try {
                            String fileSize =
                                    response.getString("size").replace("\\r\\n", "").replace("[", "").replace(
                                            "]", "");

                            if (!fileSize.equals("0") || !fileSize.equals("0.0")) {
                                fileSizeArray.add(fileSize);
                                //To getFile name
                                eventImages = new EventImages();
                                eventImages.setImgName(fileName);
                                eventImages.setFileBase64Code(fileBase64Code);
                                eventImages.setExtension(":image/jpg;base64");
                                meventImagesArrayList.add(eventImages);
                                updateListImagesFromGallery();

                            }
                            else {
                                Toast.makeText(UploadQuestionPaperActivity.this, "Please try " + "again",
                                        Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();
                        }
                        catch (JSONException ex) {
                            ex.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("ERROR", ex.getMessage());
                            Toast.makeText(UploadQuestionPaperActivity.this, "Please " + "Try again...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        System.out.println("ERROR_RESPONSE - " + error);
                        Toast.makeText(UploadQuestionPaperActivity.this, "Error3..Please Try " + "again...",
                                Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(this);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue1.add(jsonObjectRequest);
            }
        }


        //Images from camera
        else if (requestCode == REQUEST_CODE_GET_CAMERA_FILE_PATH && resultCode == Activity.RESULT_OK) {

            //For camera image blur
            try {
                fileName = diceRoll + ".jpeg".replace("4142-15F8:", "").trim();
                File file = null;

                try {
                    file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                            "clickedPhoto" + diceRoll + ".jpeg");
                    fileName = file.getName();
                }
                catch (Exception e) {
                    //unable to create  temporary file
                    Toast.makeText(UploadQuestionPaperActivity.this, "Unable to create temporary " + "file",
                            Toast.LENGTH_SHORT).show();
                }

                Uri uri = Uri.fromFile(file);// TODO: 22-07-2020 added
                Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                String pathNEW = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "Title", null);
                if (pathNEW != null) {
                    outputFileUri = Uri.parse(pathNEW);
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 80, stream);

                try {
                    byte[] byte_arr = stream.toByteArray();
                    fileBase64Code = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                }
                catch (Exception e) {
                    //Unable to convert image to byte array
                    Toast.makeText(UploadQuestionPaperActivity.this, "Unable to convert image to " + "byte " +
                            "array", Toast.LENGTH_SHORT).show();
                }

                //upload attachment method
                progressDialog.show();
                HashMap<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("filename", fileName);
                params.put("filedata", fileBase64Code);
                params.put("doc_type_folder", "uploaded_ques_paper");
                params.put("random_no", randomId);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                        "/upload_question_paper_into_temp_folder", new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("CAMERA_UPLOAD_RESPONSE" + response);

                        try {
                            String fileSize =
                                    response.getString("size").replace("\\r\\n", "").replace("[", "").replace(
                                            "]", "");

                            if (!fileSize.equals("0") || !fileSize.equals("0.0")) {
                                fileSizeArray.add(fileSize);
                                //To getFile name
                                eventImages = new EventImages();
                                eventImages.setImgName(fileName);
                                eventImages.setFileBase64Code(fileBase64Code);
                                eventImages.setExtension(":image/jpeg;base64");
                                meventImagesArrayList.add(eventImages);
                                updateListImagesFromGallery();
                                progressDialog.dismiss();
                            }
                            else {
                                Toast.makeText(UploadQuestionPaperActivity.this, "Please try " + "again",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        }
                        catch (JSONException ex) {
                            ex.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("ERROR", ex.getMessage());
                            Toast.makeText(UploadQuestionPaperActivity.this, "Please " + "Try again...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, error -> {
                    error.printStackTrace();
                    Toast.makeText(UploadQuestionPaperActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    System.out.println("ERROR_RESPONSE - " + error.getMessage());
                    progressDialog.dismiss();
                });

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue1 = Volley.newRequestQueue(this);
                requestQueue1.getCache().clear();// TODO: 27-07-2020
                requestQueue1.add(jsonObjectRequest);

            }
            catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        }
        //new FileManager Documents
        else if (requestCode == 101) {
            if (resultCode == RESULT_OK && data != null) {
                selectedPDF = data.getData();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    PDFpath = getPathForPdf(selectedPDF);
                    pdfFile = new File(PDFpath);
                    convertFileToString(PDFpath);
                    displayName = pdfFile.getName().replace("4142-15F8:", "").trim();

                    //upload attachment method
                    progressDialog.show();
                    HashMap<String, String> params = new HashMap<>();
                    params.put("short_name", name);
                    params.put("filename", displayName.replace("4142-15F8:", "").trim());
                    params.put("filedata", strFile);
                    params.put("doc_type_folder", "uploaded_ques_paper");
                    params.put("random_no", randomId);
                    System.out.println(params);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                            "/upload_question_paper_into_temp_folder", new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("" + response);
                            try {
                                String fileSize = response.getString("size").replace("\\r\\n", "").replace("[",
                                        "").replace("]", "");

                                if (!fileSize.equals("0") || !fileSize.equals("0.0")) {
                                    fileSizeArray.add(fileSize);
                                    //To getFile name
                                    eventImages = new EventImages();
                                    eventImages.setImgName(displayName);
                                    eventImages.setPdfString(strFile);
                                    meventImagesArrayListPDF.add(eventImages);
                                    updateListPdf();
                                    progressDialog.dismiss();
                                }
                                else {
                                    Toast.makeText(UploadQuestionPaperActivity.this, "Please try " + "again",
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            }
                            catch (JSONException ex) {
                                ex.printStackTrace();
                                progressDialog.dismiss();
                                Log.e("ERROR", ex.getMessage());
                                Toast.makeText(UploadQuestionPaperActivity.this, "Please " + "Try again...",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            System.out.println("ERROR_RESPONSE - " + error.getMessage());
                            Toast.makeText(UploadQuestionPaperActivity.this, "Error3..Please Try " + "again...",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                    RequestQueue requestQueue1 = Volley.newRequestQueue(this);
                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue1.add(jsonObjectRequest);

                    pdfFile = new File(FileManager.getPath(UploadQuestionPaperActivity.this, -1, selectedPDF));
                }
            }
        }

        //2.FileManager Documents
        else if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathpdf = data.getData();
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                if (uriString.startsWith("content://") || uriString.startsWith("file://")) {
                    selectedPDF = data.getData();
                    PDFpath = getPathForPdf(selectedPDF);
                    pdfFile = new File(PDFpath);

                    //Checking the file size before conversion it should be less than 25mb
                    long bytes = pdfFile.length();
                    long mb = bytes / (1024 * 1024);
                    totalMb = totalMb + mb; //Getting the TotalSize of files


                    if (mb > 25) {
                        Toast.makeText(this, "File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    }
                    else if (totalMb > 25) {
                        Toast.makeText(this, "Total File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    }
                    else {
                        convertFileToString(PDFpath);
                        displayName = pdfFile.getName().replace("4142-15F8:", "").trim();

                        //upload attachment method
                        progressDialog.show();

                        HashMap<String, String> params = new HashMap<>();
                        params.put("short_name", name);
                        params.put("filename", displayName.replace("4142-15F8:", "").trim());
                        params.put("filedata", strFile);
                        params.put("doc_type_folder", "uploaded_ques_paper");
                        params.put("random_no", randomId);

                        System.out.println(params);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                                "/upload_question_paper_into_temp_folder", new JSONObject(params),
                                new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println("" + response);

                                try {
                                    String fileSize = response.getString("size").replace("\\r\\n", "").replace(
                                            "[", "").replace("]", "");

                                    if (!fileSize.equals("0") || !fileSize.equals("0.0")) {
                                        fileSizeArray.add(fileSize);
                                        //To getFile name
                                        eventImages = new EventImages();
                                        eventImages.setImgName(displayName);
                                        eventImages.setPdfString(strFile);
                                        meventImagesArrayListPDF.add(eventImages);
                                        updateListPdf();
                                        progressDialog.dismiss();
                                    }
                                    else {
                                        Toast.makeText(UploadQuestionPaperActivity.this, "Please " + "try " +
                                                "again", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                    }
                                }
                                catch (JSONException ex) {
                                    ex.printStackTrace();
                                    progressDialog.dismiss();
                                    Log.e("ERROR", ex.getMessage());
                                    Toast.makeText(UploadQuestionPaperActivity.this, "Please " + "Try again...",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                Toast.makeText(UploadQuestionPaperActivity.this, "Error3..Please " + "Try again." +
                                        "..", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
                        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        requestQueue1.add(jsonObjectRequest);

                    }
                }
            }
            else {
                if (uriString.startsWith("content://") || uriString.startsWith("file://")) {
                    selectedPDF = data.getData();
                    PDFpath = getPathForPdf(selectedPDF);
                    pdfFile = new File(PDFpath);

                    //Checking the file size before conversion it should be less than 25mb
                    long bytes = pdfFile.length();
                    long mb = bytes / (1024 * 1024);
                    totalMb = totalMb + mb;

                    if (mb > 25) {
                        Toast.makeText(this, "File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    }
                    else if (totalMb > 25) {
                        Toast.makeText(this, "Total File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    }
                    else {
                        convertFileToString(PDFpath);
                        displayName = pdfFile.getName().replace("4142-15F8:", "").trim();

                        //upload attachment method
                        progressDialog.show();

                        HashMap<String, String> params = new HashMap<>();
                        params.put("short_name", name);
                        params.put("filename", displayName.replace("4142-15F8:", "").trim());
                        params.put("filedata", strFile);
                        params.put("doc_type_folder", "uploaded_ques_paper");
                        params.put("random_no", randomId);
                        System.out.println(params);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                                "/upload_question_paper_into_temp_folder", new JSONObject(params),
                                new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println("" + response);

                                try {
                                    String fileSize = response.getString("size").replace("\\r\\n", "").replace(
                                            "[", "").replace("]", "");

                                    if (!fileSize.equals("0") || !fileSize.equals("0.0")) {
                                        fileSizeArray.add(fileSize);
                                        //To getFile name
                                        eventImages = new EventImages();
                                        eventImages.setImgName(displayName);
                                        eventImages.setPdfString(strFile);
                                        meventImagesArrayListPDF.add(eventImages);
                                        updateListPdf();
                                        progressDialog.dismiss();
                                    }
                                    else {
                                        Toast.makeText(UploadQuestionPaperActivity.this, "Please " + "try " +
                                                "again", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                    }
                                }
                                catch (JSONException ex) {
                                    ex.printStackTrace();
                                    progressDialog.dismiss();
                                    Log.e("ERROR", ex.getMessage());
                                    Toast.makeText(UploadQuestionPaperActivity.this, "Please " + "Try again...",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                error.printStackTrace();
                                fileSizeArray.clear();
                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                Toast.makeText(UploadQuestionPaperActivity.this, "Error3..Please " + "Try again." +
                                        "..", Toast.LENGTH_SHORT).show();
                            }
                        });
                        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
                        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        requestQueue1.add(jsonObjectRequest);
                    }
                }
            }
        }
        else if (resultCode == RESULT_CANCELED) {
            uri = null;
            Toast.makeText(this, "Cancelled!", Toast.LENGTH_LONG).show();
        }

        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getOrientation();

        if (orientation == Configuration.ORIENTATION_PORTRAIT || orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        int rotation = display.getRotation();
        if (rotation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else if (rotation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    //1.Images from gallery / Camera
    private void updateListImagesFromGallery() {

        micici_listView.removeAllViews();
        if (meventImagesArrayList.size() > 0) {

            for (int j = 0; j < meventImagesArrayList.size(); j++) {
                setViewgallery(j);
            }
        }
    }


    //1.Images from gallery / Camera
    private void setViewgallery(final int j) {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.custom_horizontal_createimage, null);
        final TextView imageName = baseView.findViewById(R.id.imageName);
        final ImageView imgcancel = baseView.findViewById(R.id.imgcancel);
        String img_data = meventImagesArrayList.get(j).getFileBase64Code();
        final String file_name = meventImagesArrayList.get(j).getImgName();
        imageName.setVisibility(View.VISIBLE);
        imageName.setText(file_name);
        String imagename = meventImagesArrayList.get(j).getImgName();
        String urlmy = path;

        final String loadurl = urlmy + "/" + imagename;

        // TODO: 29-06-2020 delete api
        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove attachment api
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                HashMap<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("filename", fileName.replace("4142-15F8:", "").trim());
                params.put("doc_type_folder", "uploaded_ques_paper");
                params.put("deleted_images", "deleted_images");

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                        "/delete_uploaded_file_from_tmp_folder", new JSONObject(params),
                        new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("DELETE_NOTE" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                meventImagesArrayList.remove(j);
                                fileSizeArray.clear();
                                updateListImagesFromGallery();
                                progressDialog.dismiss();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(UploadQuestionPaperActivity.this, "Error..Please " + "Try again.." +
                                        ".", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(UploadQuestionPaperActivity.this, "Error..Please Try " + "again...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(UploadQuestionPaperActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(UploadQuestionPaperActivity.this);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue1.add(jsonObjectRequest);


            }
        });
        micici_listView.addView(baseView);
    }

    public void convertFileToString(String pathOnSdCard) {

        File file = new File(pathOnSdCard);
        try {
            byte[] data = FileUtils.readFileToByteArray(file);//Convert any file, image or video
            // into byte array
            strFile = Base64.encodeToString(data, Base64.NO_WRAP);//Convert byte array into string
            File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "TeacherAppFiles");
            if (!root.exists()) root.mkdirs();
            File gpxfile = new File(root, "/file.txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(strFile);
            writer.flush();
            writer.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //2.FileManager Documents
    private void updateListPdf() {
        micici_listViewPdf.removeAllViews();
        if (meventImagesArrayListPDF.size() > 0) {
            for (int m = 0; m < meventImagesArrayListPDF.size(); m++) {
                setViewPdf(m);
            }
        }
    }

    //2.FileManager Documents
    private void setViewPdf(final int m) {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseViewpdf = layoutInflater.inflate(R.layout.custom_horizontal_files_createlayout, null);

        txtpdfname = baseViewpdf.findViewById(R.id.txtimagenamedisplay);
        ImageView cancelimagepdf = baseViewpdf.findViewById(R.id.imgcancelpdf);

        if (PDFpath != null) {
            txtpdfname.setVisibility(View.VISIBLE);
            cancelimagepdf.setVisibility(View.VISIBLE);
            txtpdfname.setText(meventImagesArrayListPDF.get(m).getImgName());
        }
        else {
            txtpdfname.setVisibility(View.GONE);
            cancelimagepdf.setVisibility(View.GONE);
        }

        // TODO: 29-06-2020  delete api
        cancelimagepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Remove attachment api
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                HashMap<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("doc_type_folder", "uploaded_ques_paper");
                params.put("filename", meventImagesArrayListPDF.get(m).getImgName());
                System.out.println("delete4" + params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                        "/delete_uploaded_file_from_tmp_folder", new JSONObject(params),
                        new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                fileSizeArray.clear();
                                meventImagesArrayListPDF.remove(m);
                                updateListPdf();
                                progressDialog.dismiss();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(UploadQuestionPaperActivity.this, "Error1..Please " + "Try again." +
                                        "..", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(UploadQuestionPaperActivity.this, "Error2..Please Try " + "again...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(UploadQuestionPaperActivity.this, "Error3..Please Try " + "again...",
                                Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(UploadQuestionPaperActivity.this);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue1.add(jsonObjectRequest);

            }
        });
        micici_listViewPdf.addView(baseViewpdf);
    }

    //get path of PDF
    public String getPathForPdf(Uri uri) {
        File file = null;
        try {
            //Create a temporary folder where the copy will be saved to
            File temp_folder = this.getExternalFilesDir("TempFolder");

            //Use ContentResolver to get the name of the original name
            //Create a cursor and pass the Uri to it
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
            //Check that the cursor is not null
            assert cursor != null;
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            //Get the file name
            String filename = cursor.getString(nameIndex);
            //Close the cursor
            cursor.close();

            //open a InputStream by passing it the Uri
            //We have to do this in a try/catch
            InputStream is = null;
            try {
                is = this.getContentResolver().openInputStream(uri);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //We now have a folder and a file name, now we can create a file
            file = new File(temp_folder + "/" + filename);

            //We can now use a BufferedInputStream to pass the InputStream we opened above to it
            BufferedInputStream bis = new BufferedInputStream(is);
            //We will write the byte data to the FileOutputStream, but we first have to create it
            FileOutputStream fos = new FileOutputStream(file);

            byte[] data = new byte[1024];
            long total = 0;
            int count;
            //Below we will read all the byte data and write it to the FileOutputStream
            while ((count = bis.read(data)) != -1) {
                total += count;
                fos.write(data, 0, count);
            }
            //The FileOutputStream is done and the file is created and we can clean and close it
            fos.flush();
            fos.close();

        }
        catch (IOException e) {
            Log.e("IOException = ", String.valueOf(e));
        }

        //Finally we can pass the path of the file we have copied
        return file.getAbsolutePath();
    }

    private void updateList() {
        micici_listView.removeAllViews();
        if (meventImagesArrayList.size() > 0) {
            Log.d("testramanora", "updateList: eventImagesArrayList.size()  " + meventImagesArrayList.size());

            for (int i = 0; i < meventImagesArrayList.size(); i++) {
                setView(i);
            }
        }

        if (meventImagesArrayListPDF.size() > 0) {
            Log.d("testramanora", "updateList:" + meventImagesArrayListPDF.size());

            for (int j = 0; j < meventImagesArrayListPDF.size(); j++) {
                setView1(j);
            }
        }

    }

    //images files
    @SuppressLint("SetTextI18n")
    private void setView(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseView = layoutInflater.inflate(R.layout.custom_horizontal_image, null);
        TextView imageName = baseView.findViewById(R.id.txtimagenamedisplay);

        final ImageView imgcancel = baseView.findViewById(R.id.imgcancel);
        if (meventImagesArrayList.get(i).getImgName() == null) {
            return;
        }
        else {
            imageName.setText(meventImagesArrayList.get(i).getImgName() + meventImagesArrayList.get(i).getExtension());
        }

        // TODO: 29-06-2020 delete api
        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Remove attachment api
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                HashMap<String, String> params = new HashMap<>();
                params.put("random_no", randomId);
                params.put("short_name", name);
                params.put("doc_type_folder", "uploaded_ques_paper");
                params.put("filename", fileName.replace("4142-15F8:", "").trim());
                params.put("deleted_images", "deleted_images");
                System.out.println("delete1" + params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                        "/delete_uploaded_file_from_tmp_folder", new JSONObject(params),
                        new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                fileSizeArray.clear();
                                meventImagesArrayList.remove(i);
                                updateList();
                                progressDialog.dismiss();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(UploadQuestionPaperActivity.this, "Error..Please " + "Try again.." +
                                        ".", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(UploadQuestionPaperActivity.this, "Error..Please Try " + "again...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(UploadQuestionPaperActivity.this, "Error..Please Try again" + "...",
                                Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });

                RequestQueue requestQueue1 = Volley.newRequestQueue(UploadQuestionPaperActivity.this);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue1.add(jsonObjectRequest);
            }
        });

        micici_listView.addView(baseView);

    }

    //documents files
    @SuppressLint("SetTextI18n")
    private void setView1(final int j) {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("InflateParams") final View baseView =
                layoutInflater.inflate(R.layout.custom_horizontal_image, null);

        TextView imageName = baseView.findViewById(R.id.txtimagenamedisplay);

        final ImageView imgcancel = baseView.findViewById(R.id.imgcancel);
        if (meventImagesArrayListPDF.get(j).getImgName() == null) {
            return;
        }
        else {

            imageName.setText(meventImagesArrayListPDF.get(j).getImgName() + meventImagesArrayListPDF.get(j).getExtension());
        }


        // TODO: 29-06-2020 delete api
        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Remove attachment api
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                HashMap<String, String> params = new HashMap<>();
                params.put("random_no", randomId);
                params.put("short_name", name);
                params.put("doc_type_folder", "uploaded_ques_paper");
                params.put("filename", fileName.replace("4142-15F8:", "").trim());
                params.put("deleted_images", "deleted_images");
                System.out.println("delete2" + params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                        "/delete_uploaded_file_from_tmp_folder", new JSONObject(params),
                        new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                fileSizeArray.clear();
                                meventImagesArrayListPDF.remove(j);
                                updateList();
                                progressDialog.dismiss();

                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(UploadQuestionPaperActivity.this, "Error..Please " + "Try again.." +
                                        ".", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(UploadQuestionPaperActivity.this, "Error..Please Try " + "again...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(UploadQuestionPaperActivity.this, "Error..Please Try again" + "...",
                                Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(UploadQuestionPaperActivity.this);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue1.add(jsonObjectRequest);


            }
        });
        micici_listView.addView(baseView);
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