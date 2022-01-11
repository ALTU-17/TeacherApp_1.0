package in.aceventura.evolvuschool.teacherapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.MenuItem;
import android.view.MotionEvent;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.pojo.ClassSubjectPojo;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImages;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImagesEdit;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImagesPdfEdit;
import in.aceventura.evolvuschool.teacherapp.pojo.ExaminationPojo;
import in.aceventura.evolvuschool.teacherapp.pojo.QbankModel;
import in.aceventura.evolvuschool.teacherapp.utils.FileManager;
import in.aceventura.evolvuschool.teacherapp.utils.PermissionUtils;

import static in.aceventura.evolvuschool.teacherapp.R.layout;

public class EditQuestionPaperActivity extends AppCompatActivity {

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
            randomId, class_id, section_id, subject_id, exam_id, reg_id, academic_yr, question_bank_id;
    RequestQueue requestQueue;
    ArrayList<EventImages> meventImagesArrayList;
    ArrayList<EventImages> meventImagesArrayListPDF;
    EventImagesPdfEdit eventImagesPdfEdit;
    ArrayList<String> fileSizeArray = new ArrayList<>();
    ArrayList<String> fileNameArray = new ArrayList<>();
    ArrayList<String> deleted_Images_Array = new ArrayList<>();
    EventImages eventImages;
    File pdfFile;
    long totalMb = 0;
    TextView txtpdfname;
    ArrayList<String> attachmentsfile = new ArrayList<>();
    ArrayList<String> attachmentsfile1 = new ArrayList<>();
    TextView addNotes, urltxt1, urltxt;
    LinearLayout linearLayoutlistview, linearLayoutlistview1;
    DownloadManager dm;
    String prourl;
    EditText exam_instructions;
    int e_id;
    String createdExamName, createdClassName, createdSubjectName;
    LinearLayout micici_listViewpdf, micici_listViewpdf1;
    QbankModel qbankModel;
    ArrayList<EventImagesPdfEdit> meventImagesArrayListPDFEdit = new ArrayList<>();
    ArrayList<EventImages> meventImagesArrayList1 = new ArrayList<>();
    ArrayList<EventImages> meventImagesArrayListDelete = new ArrayList<>();
    EventImagesEdit eventImagesEdit;
    ArrayList<EventImagesEdit> meventImagesArrayListEdit = new ArrayList<>();
    String createdClassSubjectName;
    private Spinner selectExamSpinner, selectClassSubjectSpinner;
    private EditText etQuestionBankName;
    private EditText etWeightage;
    private ProgressDialog progressDialog;
    private ArrayList<String> listClassSubjects = new ArrayList<>();
    private JSONArray classSubjectArray;
    private ArrayList<String> listExams;
    private JSONArray result;
    View.OnTouchListener examSpinnerOnTouch = (v, event) -> {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            getExamination();
        }
        return false;
    };
    private JSONArray classResult;
    private Button saveexamdetails, save_and_complete;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(layout.activity_edit_question_paper);

        //For camera
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Random rand = new Random();
        randomId = String.format("%08d", rand.nextInt(10000));
        System.out.println("RANDOMID" + randomId);
        selectExamSpinner = findViewById(R.id.sp_select_exam);
        selectClassSubjectSpinner = findViewById(R.id.sp_select_class_subject);
        etQuestionBankName = findViewById(R.id.et_question_bank_name);
        exam_instructions = findViewById(R.id.exam_instructions);
        etWeightage = findViewById(R.id.et_weightage);
        saveexamdetails = findViewById(R.id.save);
        save_and_complete = findViewById(R.id.save_and_complete);
        addNotes = findViewById(R.id.addAttachments);
        micici_listViewPdf = findViewById(R.id.micici_listViewpdf);
        micici_listView = findViewById(R.id.micici_listView);
        requestQueue = Volley.newRequestQueue(getBaseContext());
        meventImagesArrayList = new ArrayList<>();
        meventImagesArrayListPDF = new ArrayList<>();

        listExams = new ArrayList<>();
        eventImages = new EventImages();
        urltxt = findViewById(R.id.txturl12);
        linearLayoutlistview = findViewById(R.id.viewAttachmentlistview);
        linearLayoutlistview1 = findViewById(R.id.viewAttachmentlistview1);

        micici_listViewpdf = findViewById(R.id.micici_listViewpdf);
        micici_listViewpdf1 = findViewById(R.id.micici_listViewpdf1);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
            prourl = mDatabaseHelper.getPURL(1);

        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            qbankModel = (QbankModel) bundle.getSerializable("user");
            reg_id = bundle.getString("reg_id");
            academic_yr = bundle.getString("academic_yr");
            String qb_status = qbankModel.getStatus();
            question_bank_id = qbankModel.getQuestion_bank_id();

            //save
            if (qb_status.equals("N")) {
                saveexamdetails.setVisibility(View.VISIBLE);
            }
            //save & complete
            else {
                saveexamdetails.setVisibility(View.GONE);
            }
            save_and_complete.setVisibility(View.VISIBLE);
        }

        getExamination();
        getClassSubjects(name, reg_id);

        //Checking for the question paper attachments
        checkAttachments(qbankModel);

        /*First Set the old values of question bank */
        createdExamName = qbankModel.getExam_name();
        createdClassName = qbankModel.getClass_name();
        createdSubjectName = qbankModel.getSubject_name();
//        String createdSectionName = qbankModel.getS;

        createdClassSubjectName = createdClassName + " " + createdSubjectName;

        etQuestionBankName.setText(qbankModel.getQuestionBankName().equals("null") ? "" :
                qbankModel.getQuestionBankName());
        etWeightage.setText(qbankModel.getWeightage().equals("null") ? "" : qbankModel.getWeightage());
        exam_instructions.setText(qbankModel.getInstructions().equals("null") ? "" : qbankModel.getInstructions());


        //Adding notes button
        addNotes.setOnClickListener(view -> {
            if (PermissionUtils.checkPermission(EditQuestionPaperActivity.this)) {
                selectNotes(savedInstanceState);
            }
            else {
                Toast.makeText(EditQuestionPaperActivity.this, "Permissions not granted !!", Toast.LENGTH_SHORT).show();
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
                if (parent.getItemAtPosition(position).equals("Select Class & Subject")) {
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

        //save
        saveexamdetails.setOnClickListener(v -> {
            if (selectExamSpinner.getSelectedItemId() < 0) {
                Toast.makeText(EditQuestionPaperActivity.this, "Select Examination", Toast.LENGTH_SHORT).show();
            }
            else if (selectClassSubjectSpinner.getSelectedItemId() <= 0) {
                Toast.makeText(EditQuestionPaperActivity.this, "Select Class", Toast.LENGTH_SHORT).show();
            }
            else if (etQuestionBankName.getText().toString().trim().isEmpty()) {
                Toast.makeText(EditQuestionPaperActivity.this, "Enter Question Bank Name", Toast.LENGTH_SHORT).show();
            }
            else if (etWeightage.getText().toString().trim().isEmpty()) {
                Toast.makeText(EditQuestionPaperActivity.this, "Enter Weightage", Toast.LENGTH_SHORT).show();
            }
            else if (meventImagesArrayList.isEmpty() && meventImagesArrayList1.isEmpty()) {
                Toast.makeText(EditQuestionPaperActivity.this, "Upload Question Papers", Toast.LENGTH_SHORT).show();
            }
            else {
                editExam("1");
            }
        });

        //save and complete
        save_and_complete.setOnClickListener(v -> {
            if (selectExamSpinner.getSelectedItemId() < 0) {
                Toast.makeText(EditQuestionPaperActivity.this, "Select Examination", Toast.LENGTH_SHORT).show();
            }
            else if (selectClassSubjectSpinner.getSelectedItemId() <= 0) {
                Toast.makeText(EditQuestionPaperActivity.this, "Select Class", Toast.LENGTH_SHORT).show();
            }
            else if (etQuestionBankName.getText().toString().trim().isEmpty()) {
                Toast.makeText(EditQuestionPaperActivity.this, "Enter Question Bank Name", Toast.LENGTH_SHORT).show();
            }
            else if (etWeightage.getText().toString().trim().isEmpty()) {
                Toast.makeText(EditQuestionPaperActivity.this, "Enter Weightage", Toast.LENGTH_SHORT).show();
            }
            else if (meventImagesArrayList.isEmpty() && meventImagesArrayList1.isEmpty()) {
                Toast.makeText(EditQuestionPaperActivity.this, "Upload Question Papers", Toast.LENGTH_SHORT).show();
            }
            else {
                editExam("2");
            }
        });

        selectExamSpinner.setOnTouchListener(examSpinnerOnTouch);

        //Not to call as soon as the activity is created
        /*getExamination();
        getClasses();*/

    }//OnCreate

    private void checkAttachments(QbankModel qbankModel) {
        HashMap<String, String> params = new HashMap<>();
        params.put("question_bank_id", qbankModel.getQuestion_bank_id());
        params.put("short_name", name);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                "/get_images_question_paper", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Question_Paper_ATTACHMENTS" + response);
                try {
                    if (response.getString("status").equals("true")) {
                        meventImagesArrayListPDFEdit.clear();
                        meventImagesArrayList.clear();

                        String url = response.getString("url");
                        Log.d("viewAttachmentUrl: ", url);
                        JSONArray jsonArray = response.getJSONArray("images");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            System.out.println(jsonObject);
                            String imagename = jsonObject.getString("image_name");
                            boolean isFound = imagename.contains(".pdf");
                            boolean isFound1 = imagename.contains(".docx");
                            boolean isFound2 = imagename.contains(".doc");
                            boolean isFound3 = imagename.contains(".xls");
                            boolean isFound4 = imagename.contains(".xlsx");
                            boolean isFound5 = imagename.contains(".txt");
                            boolean isFound6 = imagename.contains(".PDF");

                            //--------------------------------------

                            if (isFound || isFound1 || isFound2 || isFound3 || isFound4 || isFound5 || isFound6) {
                                EventImages eventImages = new EventImages();
                                eventImages.setImgName(imagename);
                                eventImages.setImgUrl(url);
                                meventImagesArrayList.add(eventImages);
                                urltxt.setText(url);
                                updateListPdf(qbankModel.getQuestion_bank_id());

                            }
                            else {
                                EventImages eventImages = new EventImages();
                                eventImages.setImgName(imagename);
                                eventImages.setImgUrl(url);
                                meventImagesArrayList1.add(eventImages);
                                urltxt.setText(url);
                                updateListUrlImage(qbankModel.getQuestion_bank_id());
                            }
                        }
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {

        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(jsonObjectRequest);
    }

    //Showing Attachments from Response
    private void updateListUrlImage(String question_bank_id) {
        linearLayoutlistview.removeAllViews();
        if (meventImagesArrayList1.size() > 0) {

            for (int i = 0; i < meventImagesArrayList1.size(); i++) {
                setViewImageUrl(i, question_bank_id);
            }
        }
    }

    //Showing Question Papers from Response
    private void setViewImageUrl(final int i, String question_bank_id) {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseView = layoutInflater.inflate(R.layout.custom_horizontal_image, null);

        final TextView imageName = baseView.findViewById(R.id.imageName);
        final ImageView imgcancel = baseView.findViewById(R.id.imgcancel);
        final Button downloadpdf = baseView.findViewById(R.id.downloadpdf);

        final String file_name = meventImagesArrayList1.get(i).getImgName();
        final String file_link = meventImagesArrayList1.get(i).getImgUrl();

        imageName.setVisibility(View.VISIBLE);
        imageName.setText(file_name);

        downloadpdf.setOnClickListener(v -> {
            downloadQuestionPaper(file_name, file_link, this.question_bank_id);
            Toast.makeText(EditQuestionPaperActivity.this, "Downloading  " + file_name, Toast.LENGTH_SHORT).show();
        });

        String urlmy = urltxt.getText().toString();


        final String loadurl =
                urlmy + name + "/" + "uploaded_ques_paper/" + question_bank_id + "/" + file_name.trim();
        Log.d("viewAttachmentUrl: ", loadurl);

        imageName.setOnClickListener(view -> {
            Intent intent = new Intent(EditQuestionPaperActivity.this, ViewFullSizeImage.class);
            intent.putExtra("url", loadurl.trim());
            Log.d("setViewImageUrl: ", loadurl);
            startActivity(intent);
        });


        imgcancel.setOnClickListener(v -> {
            EventImages deleted_image = meventImagesArrayList1.get(i);
            meventImagesArrayListDelete.add(deleted_image);
            meventImagesArrayList1.remove(i);
            updateListUrlImage(qbankModel.getQuestion_bank_id());
            progressDialog.dismiss();
        });

        linearLayoutlistview.addView(baseView);
    }

    private void downloadQuestionPaper(String file_name, String file_link, String question_bank_id) {
        if (isReadStorageAllowed()) {
            String downloadUrl;


            downloadUrl =
                    file_link + name + "/" + "uploaded_ques_paper/" + question_bank_id + "/" + file_name.trim();

            dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(downloadUrl);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            String folder = "Evolvuschool/Teacher";
            request.setDestinationInExternalPublicDir(folder, file_name);

            File directory = new File(folder);

            //If directory not exists create one....
            if (!directory.exists()) {
                directory.mkdirs();
            }
            dm.enqueue(request);
            return;

        }
        requestStoragePermission();
    }

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
        params.put("short_name", name);
        params.put("acd_yr", academic_yr);
        params.put("reg_id", reg_id);
        System.out.println("CLASS_SUBJECT_PARAMS" + params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                "/get_class_and_subject_allotted_to_teacher_for_qb", new JSONObject(params), response -> {
            try {
                if (response.getString("status").equals("true")) {
                    listClassSubjects.clear();
                    Log.d("CLASS_SUBJECT_RESPONSE", String.valueOf(response));
                    classSubjectArray = response.getJSONArray("classAndsubject");
                    int j = 0;
                    for (int i = 0; i < classSubjectArray.length(); i++) {
                        JSONObject jsonObject = classSubjectArray.getJSONObject(i);
                        String class_name = jsonObject.getString("classname");
                        String sm_id = jsonObject.getString("sm_id");
                        String subject_name = jsonObject.getString("subject_name");
                        String class_id = jsonObject.getString("class_id");

                        /*String section_name = jsonObject.getString("section_name");
                        String section_id = jsonObject.getString("section_id");*/
                        ClassSubjectPojo classSubjectPojo = new ClassSubjectPojo(sm_id, subject_name, class_name
                                , class_id/*, section_id, section_name*/);
                        listClassSubjects.add(class_name + " - " + subject_name);

                        //setting previous
                        if (createdClassSubjectName.equals(class_name + " " + subject_name)) {
                            j = i;
                        }
                    }
                    listClassSubjects.add(0, "Select Class and Subject");
                    selectClassSubjectSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                            R.layout.mytextview, listClassSubjects));
                    selectClassSubjectSpinner.setSelection(j + 1);
                }
                else {
                    //false
                    System.out.println(response);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                System.out.println("ERROR" + e.getMessage());
                Toast.makeText(EditQuestionPaperActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            System.out.println("ERROR_RESPONSE - " + error.getMessage());
            Toast.makeText(EditQuestionPaperActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                    int j = 0;
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
                        if (createdExamName.equals(name)) {
                            j = i;
                        }
                    }
                    listExams.add(0, "Select Examination");
                    selectExamSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), layout.mytextview,
                            listExams));
                    selectExamSpinner.setSelection(j + 1);
                }
                else {
                    //false
                    System.out.println(response);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(EditQuestionPaperActivity.this, "Error1..Please Try again...",
                        Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            System.out.println("ERROR_RESPONSE - " + error.getMessage());
            Toast.makeText(EditQuestionPaperActivity.this, "Error2..Please Try again...", Toast.LENGTH_SHORT).show();
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);
    }

    private void editExam(String button_value) {
        final String reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        HashMap<String, String> params = new HashMap<>();
        params.put("button_value", button_value);
        params.put("exam_id", exam_id);
        params.put("class_id", class_id);
        params.put("subject_id", subject_id);
        params.put("reg_id", reg_id);
        params.put("qb_name", etQuestionBankName.getText().toString());
        params.put("weightage", etWeightage.getText().toString());
        params.put("instructions", exam_instructions.getText().toString()); //new 25/08/2020 (LM)
        params.put("acd_yr", academic_yr);
        params.put("question_bank_id", question_bank_id);
        params.put("operation", "edit");
        params.put("short_name", name);

        if (fileSizeArray.size() != 0) {
            params.put("filesize", fileSizeArray.toString());
        }


        //attachments removed
        if (meventImagesArrayListDelete.size() > 0) {
            for (int i = 0; i < meventImagesArrayListDelete.size(); i++) {
                String delete = meventImagesArrayListDelete.get(i).getImgName();
                deleted_Images_Array.add(delete);
                String dfile = deleted_Images_Array.toString().replace("[", "").replace("]", "");
                params.put("deleted_images", dfile);
            }
        }


        /*if (fileNameArray.size() != 0) {
            params.put("filename", fileNameArray.toString());
        }*/


        if (meventImagesArrayListPDFEdit.size() > 0 && meventImagesArrayListEdit.size() > 0) {
            for (int i = 0; i < meventImagesArrayListPDFEdit.size(); i++) {
                String base = '"' + meventImagesArrayListPDFEdit.get(i).getPdfString() + '"';
                String fname = '"' + meventImagesArrayListPDFEdit.get(i).getFileName() + '"';

                attachmentsfile.add(base);
                attachmentsfile1.add(fname);
            }
            for (int j = 0; j < meventImagesArrayListEdit.size(); j++) {
                String base = '"' + meventImagesArrayListEdit.get(j).getFileBase64Code() + '"';
                String fname = '"' + meventImagesArrayListEdit.get(j).getImgName() + '"';
                attachmentsfile.add(base);
                attachmentsfile1.add(fname);

            }
        }
        else if (meventImagesArrayListPDFEdit.size() > 0) {
            for (int i = 0; i < meventImagesArrayListPDFEdit.size(); i++) {

                String base = '"' + meventImagesArrayListPDFEdit.get(i).getPdfString() + '"';
                String fname = '"' + meventImagesArrayListPDFEdit.get(i).getFileName() + '"';

                attachmentsfile.add(base);
                attachmentsfile1.add(fname);
            }
        }
        else if (meventImagesArrayListEdit.size() > 0) {
            for (int i = 0; i < meventImagesArrayListEdit.size(); i++) {

                String base = '"' + meventImagesArrayListEdit.get(i).getFileBase64Code() + '"';
                String fname = '"' + meventImagesArrayListEdit.get(i).getImgName() + '"';

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
        System.out.println("EDIT_QUES_PAPER_PARAM" + params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" + "/question_bank",
                new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("EDIT_QUES_PAPER_RESPONSE" + response);
                try {
                    if (response.getString("status").equals("true")) {
                        Toast.makeText(EditQuestionPaperActivity.this, "Question Paper " + "edited Successful!!"
                                , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditQuestionPaperActivity.this, OnlineExamActivity.class);
                        intent.putExtra("academic_yr", academic_yr);
                        intent.putExtra("reg_id", reg_id);
                        startActivity(intent);

                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(EditQuestionPaperActivity.this, response.getString("error_msg"),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("save_exam" + e.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(EditQuestionPaperActivity.this, "Error..Please Try " + "again...",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> {
            error.printStackTrace();
            System.out.println("ERROR_RESPONSE - " + error.getMessage());
            Toast.makeText(EditQuestionPaperActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
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
        builder.setItems(options, (dialog, item) -> {
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
                Toast.makeText(EditQuestionPaperActivity.this, "CameraError1" + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                System.out.println("Error" + e.getMessage());
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (outState != null) {
            outState.putString("photopath", String.valueOf(outputFileUri));
            super.onSaveInstanceState(outState);
        }


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
                params.put("random_no", question_bank_id);
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
                                fileNameArray.add(fileName);

                                eventImagesEdit = new EventImagesEdit();
                                eventImagesEdit.setImgName(fileName);
                                eventImagesEdit.setFileBase64Code(fileBase64Code);
                                eventImagesEdit.setExtension(":image/jpg;base64");
                                meventImagesArrayListEdit.add(eventImagesEdit);
                                updateListImagesFromGallery();
                                progressDialog.dismiss();

                            }
                            else {
                                Toast.makeText(EditQuestionPaperActivity.this, "Please try " + "again",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        }
                        catch (JSONException ex) {
                            ex.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("ERROR", ex.getMessage());
                            Toast.makeText(EditQuestionPaperActivity.this, "Please " + "Try again...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, error -> {
                    error.printStackTrace();
                    System.out.println("ERROR_RESPONSE - " + error);
                    Toast.makeText(EditQuestionPaperActivity.this, "Error3..Please Try again." + "..",
                            Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
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
                    Toast.makeText(EditQuestionPaperActivity.this, "Unable to create temporary " + "file",
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
                    Toast.makeText(EditQuestionPaperActivity.this, "Unable to convert image to " + "byte array",
                            Toast.LENGTH_SHORT).show();
                }

                //upload attachment method
                progressDialog.show();
                HashMap<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("filename", fileName.replace("4142-15F8:", "").trim());
                params.put("filedata", fileBase64Code);
                params.put("doc_type_folder", "uploaded_ques_paper");
                params.put("random_no", question_bank_id);

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
                                fileNameArray.add(fileName);
                                //To getFile name

                                eventImagesEdit = new EventImagesEdit();
                                eventImagesEdit.setImgName(fileName);
                                eventImagesEdit.setFileBase64Code(fileBase64Code);
                                eventImagesEdit.setExtension(":image/jpg;base64");
                                meventImagesArrayListEdit.add(eventImagesEdit);
                                updateListImagesFromGallery();
                                progressDialog.dismiss();

                            }
                            else {
                                Toast.makeText(EditQuestionPaperActivity.this, "Please try " + "again",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        }
                        catch (JSONException ex) {
                            ex.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("ERROR", ex.getMessage());
                            Toast.makeText(EditQuestionPaperActivity.this, "Please " + "Try again...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, error -> {
                    error.printStackTrace();
                    Toast.makeText(EditQuestionPaperActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                    params.put("random_no", question_bank_id);
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
                                    fileNameArray.add(displayName);
                                    //To getFile name

                                    eventImagesPdfEdit = new EventImagesPdfEdit();
                                    eventImagesPdfEdit.setFileName(displayName);
                                    eventImagesPdfEdit.setPdfString(strFile);
                                    meventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                    updateListPdfView();
                                    progressDialog.dismiss();
                                }
                                else {
                                    Toast.makeText(EditQuestionPaperActivity.this, "Please try " + "again",
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            }
                            catch (JSONException ex) {
                                ex.printStackTrace();
                                progressDialog.dismiss();
                                Log.e("ERROR", ex.getMessage());
                                Toast.makeText(EditQuestionPaperActivity.this, "Please " + "Try again...",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, error -> {
                        error.printStackTrace();
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(EditQuestionPaperActivity.this, "Error3..Please Try " + "again...",
                                Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    });
                    RequestQueue requestQueue1 = Volley.newRequestQueue(this);
                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue1.add(jsonObjectRequest);

                    pdfFile = new File(FileManager.getPath(EditQuestionPaperActivity.this, -1, selectedPDF));
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
                        params.put("random_no", question_bank_id);

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
                                        fileNameArray.add(displayName);

                                        //To getFile name
                                        eventImagesPdfEdit = new EventImagesPdfEdit();
                                        eventImagesPdfEdit.setFileName(displayName);
                                        eventImagesPdfEdit.setPdfString(strFile);
                                        meventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                        updateListPdfView();


                                    }
                                    else {
                                        Toast.makeText(EditQuestionPaperActivity.this, "Please " + "try " +
                                                "again", Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                }
                                catch (JSONException ex) {
                                    ex.printStackTrace();
                                    progressDialog.dismiss();
                                    Log.e("ERROR", ex.getMessage());
                                    Toast.makeText(EditQuestionPaperActivity.this, "Please " + "Try again...",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, error -> {
                            error.printStackTrace();
                            System.out.println("ERROR_RESPONSE - " + error.getMessage());
                            Toast.makeText(EditQuestionPaperActivity.this, "Error3..Please " + "Try again...",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
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
                        params.put("filename", displayName);
                        params.put("filedata", strFile);
                        params.put("doc_type_folder", "uploaded_ques_paper");
                        params.put("random_no", question_bank_id);
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
                                        fileNameArray.add(displayName);
                                        //To getFile name
                                        eventImagesPdfEdit = new EventImagesPdfEdit();
                                        eventImagesPdfEdit.setFileName(displayName);
                                        eventImagesPdfEdit.setPdfString(strFile);
                                        meventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                        updateListPdfView();
                                    }
                                    else {
                                        Toast.makeText(EditQuestionPaperActivity.this, "Please " + "try " +
                                                "again", Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                }
                                catch (JSONException ex) {
                                    ex.printStackTrace();
                                    progressDialog.dismiss();
                                    Log.e("ERROR", ex.getMessage());
                                    Toast.makeText(EditQuestionPaperActivity.this, "Please " + "Try again...",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, error -> {
                            progressDialog.dismiss();
                            error.printStackTrace();
                            System.out.println("ERROR_RESPONSE - " + error.getMessage());
                            Toast.makeText(EditQuestionPaperActivity.this, "Error3..Please " + "Try again...",
                                    Toast.LENGTH_SHORT).show();
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

    //Selecting Attachments
    private void updateListPdfView() {
        micici_listViewpdf1.removeAllViews();
        if (meventImagesArrayListPDFEdit.size() > 0) {
            for (int l = 0; l < meventImagesArrayListPDFEdit.size(); l++) {
                setViewPdfView(l);
            }
        }
    }

    //Selecting Attachments
    private void setViewPdfView(final int l) {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseViewpdf = layoutInflater.inflate(R.layout.custom_horizontal_files_layout_view, null);

        txtpdfname = baseViewpdf.findViewById(R.id.txtimagenamedisplay);
        ImageView cancelimagepdf = baseViewpdf.findViewById(R.id.imgcancelpdf);
        Button downloadpdf = baseViewpdf.findViewById(R.id.downloadpdf);
        String filename = meventImagesArrayListPDFEdit.get(l).getFileName();

        if (filename != null) {
            txtpdfname.setVisibility(View.VISIBLE);
            cancelimagepdf.setVisibility(View.VISIBLE);
            downloadpdf.setVisibility(View.GONE);
            txtpdfname.setText(filename);
        }
        else {
            txtpdfname.setVisibility(View.GONE);
            cancelimagepdf.setVisibility(View.GONE);
            downloadpdf.setVisibility(View.GONE);
        }

        cancelimagepdf.setOnClickListener(view -> {
            String fName = meventImagesArrayListPDFEdit.get(l).getFileName();
            EventImagesPdfEdit deleted_image = meventImagesArrayListPDFEdit.get(l);
            meventImagesArrayListDelete.add(deleted_image);
            meventImagesArrayListPDFEdit.remove(l);
            updateListPdfView();
        });

        micici_listViewpdf1.addView(baseViewpdf);
    }

    //NEW ATTACHMENT
    //1.Images from gallery / Camera
    private void updateListImagesFromGallery() {

//        micici_listView.removeAllViews();
        linearLayoutlistview1.removeAllViews();
        if (meventImagesArrayListEdit.size() > 0) {

            for (int j = 0; j < meventImagesArrayListEdit.size(); j++) {
                setViewgallery(j);
            }
        }
    }


    //NEW ATTACHMENTS
    //1.Images from gallery / Camera
    private void setViewgallery(final int j) {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.custom_horizontal_createimage, null);
        final TextView imageName = baseView.findViewById(R.id.imageName);
        final ImageView imgcancel = baseView.findViewById(R.id.imgcancel);
        String img_data = meventImagesArrayListEdit.get(j).getFileBase64Code();
        final String file_name = meventImagesArrayListEdit.get(j).getImgName();
        imageName.setVisibility(View.VISIBLE);
        imageName.setText(file_name);
        final String imagename = meventImagesArrayListEdit.get(j).getImgName();
        String urlmy = path;

        final String loadurl = urlmy + "/" + imagename;


        imgcancel.setOnClickListener(v -> {
            EventImagesEdit eventImagesedit = meventImagesArrayListEdit.get(j);
            meventImagesArrayListDelete.add(eventImagesedit);
            meventImagesArrayListEdit.remove(j);
            updateListImagesFromGallery();

        });

        linearLayoutlistview1.addView(baseView);
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

    //attachments from response
    private void updateListPdf(String question_bank_id) {
        micici_listViewPdf.removeAllViews();
        if (meventImagesArrayList.size() > 0) {
            for (int l = 0; l < meventImagesArrayList.size(); l++) {
                setViewPdf(l, question_bank_id);
            }
        }
    }


    //Attachments from Response
    private void setViewPdf(final int l, String question_bank_id) {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseViewpdf = layoutInflater.inflate(R.layout.custom_horizontal_files_createlayout, null);

        txtpdfname = baseViewpdf.findViewById(R.id.txtimagenamedisplay);
        ImageView cancelimagepdf = baseViewpdf.findViewById(R.id.imgcancelpdf);
        Button download = baseViewpdf.findViewById(R.id.downloadpdf);

        final String filename = meventImagesArrayList.get(l).getImgName();
        final String file_link = meventImagesArrayList.get(l).getImgUrl();

        if (filename != null) {
            txtpdfname.setVisibility(View.VISIBLE);
            cancelimagepdf.setVisibility(View.VISIBLE);
            download.setVisibility(View.VISIBLE);
            txtpdfname.setText(filename);
        }
        else {
            txtpdfname.setVisibility(View.GONE);
            cancelimagepdf.setVisibility(View.GONE);
            download.setVisibility(View.GONE);
        }

        download.setOnClickListener(v -> {
            downloadQuestionPaper(filename, file_link, this.question_bank_id);
            Toast.makeText(EditQuestionPaperActivity.this, "Downloading  " + filename, Toast.LENGTH_SHORT).show();
        });

        cancelimagepdf.setOnClickListener(view -> {
//            EventImages eventImages = meventImagesArrayListPDF.get(l);
            EventImages eventImages = meventImagesArrayList.get(l);
            meventImagesArrayListDelete.add(eventImages);
            meventImagesArrayList.remove(l);
            updateListPdf(qbankModel.getQuestion_bank_id());
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


        imgcancel.setOnClickListener(view -> {
            EventImages eventImages = meventImagesArrayList.get(i);
            meventImagesArrayListDelete.add(eventImages);
            meventImagesArrayList.remove(i);
            updateList();
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

        imgcancel.setOnClickListener(view -> {
            EventImages eventImages = meventImagesArrayListPDF.get(j);
            meventImagesArrayListDelete.add(eventImages);
            meventImagesArrayListPDF.remove(j);
            updateList();
            progressDialog.dismiss();
        });

        micici_listView.addView(baseView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;

        //If permission is not granted returning false
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            Toast.makeText(EditQuestionPaperActivity.this, "To Download Notice Attachment Please " + "Allow the "
                    + "Storage Permission", Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission
        int STORAGE_PERMISSION_CODE = 23;
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}
        , STORAGE_PERMISSION_CODE);
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