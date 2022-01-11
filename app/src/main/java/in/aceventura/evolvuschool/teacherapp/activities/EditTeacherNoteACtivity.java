package in.aceventura.evolvuschool.teacherapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImages;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImagesEdit;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImagesPdfEdit;
import in.aceventura.evolvuschool.teacherapp.utils.PermissionUtils;

public class EditTeacherNoteACtivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    public static int REQUEST_CODE_GET_CAMERA_FILE_PATH = 1888;
    public static int REQUEST_CODE_GET_FILE_PATH = 1;
    public static String fileBase64Code;
    public static String fileName;
    static Uri uri;
    final Random rand = new Random();
    public String atchPDF, atchIMG, fName, iName;
    int diceRoll;
    Uri outputFileUri;
    Uri filePathpdf;
    Bitmap bitmap;
    Random Number;
    EditText et_teachernoteDate, et_teachernoteDesc, edtmy, edtmy1;
    TextView txtimage;
    int Rnumber;
    LinearLayout micici_listViewpdf, micici_listViewpdf1;
    Button selectFile, updateTeachernote, resetTeachernote, backTeachernote;
    Spinner spinsub, spinclass;
    TextView txtpdfname, selectimage;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String descN, logintype, editedNoteDate;
    String subid;
    SharedClass sh;
    String class_id, section_id, subject_id, notes_id, name, newUrl, dUrl, prourl, reg_id, academic_yr;
    Calendar calendar;
    final DatePickerDialog.OnDateSetListener startDate1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel2();
        }
    };
    Context context;
    String pdfString = null;
    String path;
    String displayName = null;
    String strFile = null;
    ArrayList<EventImages> meventImagesArrayList;
    ArrayList<EventImages> meventImagesArrayList1;
    ArrayList<EventImages> meventImagesArrayListDelete;
    ArrayList<EventImagesEdit> meventImagesArrayListEdit;
    ArrayList<EventImagesPdfEdit> meventImagesArrayListPDFEdit;
    ArrayList<String> deletefile = new ArrayList<>();
    ArrayList<String> attachmentsfile = new ArrayList<>();
    ArrayList<String> attachmentsfile1 = new ArrayList<>();
    EventImages eventImages;
    EventImagesEdit eventImagesEdit;
    EventImagesPdfEdit eventImagesPdfEdit;
    TextView urltxt, urltxt1;
    Button btnviewattchment, selectimage1;
    LinearLayout linearLayoutlistview, linearLayoutlistview1;
    String dateImage;
    DatabaseHelper mDatabaseHelper;
    ProgressBar progressBar;
    //new
    Uri selectedIDImage, selectedPDF;
    String PDFpath;
    File pdfFile;
    long totalMb = 0;
    DownloadManager dm;
    private int PICK_PDF_REQUEST = 12;
    private JSONArray result;
    private Spinner classSpinner, subjectSpinner;
    private ArrayList<String> listClass;
    private ArrayList<String> listSubject;
    private View.OnTouchListener classSpinnerOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                //Your code
                listClass.clear();
                listSubject.clear();
                getClasses();
            }
            return false;
        }
    };
    private View.OnTouchListener subjectSpinnerOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                listSubject.clear();
                getSubjects();
                subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {   //String option;
                        subject_id = getSubjectId(position);
                    } // to close the onItemSelected

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            return false;
        }
    };

    String randomId,created_note_date;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_teacher_note_activity2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Generating 4 digit randomId
        Random rand = new Random();
        randomId = String.format("%04d", rand.nextInt(10000));
        System.out.println("RANDOMID" + randomId);

        context = this;
        edtmy = findViewById(R.id.edt);
        selectimage = findViewById(R.id.selectimage);
        edtmy1 = findViewById(R.id.edt1);
        selectFile = findViewById(R.id.file);
        requestQueue = Volley.newRequestQueue(context);
        calendar = Calendar.getInstance();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        et_teachernoteDate = findViewById(R.id.date1);
        et_teachernoteDesc = findViewById(R.id.et_notekDescription);
        updateTeachernote = findViewById(R.id.updatenote);
        resetTeachernote = findViewById(R.id.resetnote);
        classSpinner = findViewById(R.id.sp_class1);
        subjectSpinner = findViewById(R.id.sp_subject1);
        //btnviewattchment = findViewById(R.id.viewnotes);
        linearLayoutlistview = findViewById(R.id.viewAttachmentlistview);
        linearLayoutlistview1 = findViewById(R.id.viewAttachmentlistview1);
        urltxt = findViewById(R.id.txturl12);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        listClass = new ArrayList<>();
        listSubject = new ArrayList<>();

        Intent intent = getIntent();
        String classname = intent.getStringExtra("classname1");
        String subjectname = intent.getStringExtra("subjectname1");
        String sectionname = intent.getStringExtra("sectionname1");

        created_note_date = intent.getStringExtra("date1");


        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(created_note_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateImage = newFormat.format(date2);//yyyy-mm-dd

        descN = intent.getStringExtra("desc1");
        notes_id = intent.getStringExtra("notes_id");
        class_id = intent.getStringExtra("class_id");
        section_id = intent.getStringExtra("section_id");
        subject_id = intent.getStringExtra("subject_id");

        meventImagesArrayList = new ArrayList<>();
        meventImagesArrayList1 = new ArrayList<>();
        meventImagesArrayListEdit = new ArrayList<>();
        meventImagesArrayListDelete = new ArrayList<>();
        meventImagesArrayListPDFEdit = new ArrayList<>();

        micici_listViewpdf = findViewById(R.id.micici_listViewpdf);
        micici_listViewpdf1 = findViewById(R.id.micici_listViewpdf1);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);
        prourl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
            prourl = mDatabaseHelper.getPURL(1);
        }

        resetTeachernote.setOnClickListener(this);
        checkAttachments();

        meventImagesArrayListPDFEdit = new ArrayList<>();
        edtmy.setText(classname + " " + sectionname);
        edtmy.setEnabled(false);

        edtmy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getClasses();
            }
        });
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (subjectname.equalsIgnoreCase("null")) {
            edtmy1.setText("No Subject");
        } else {
            edtmy1.setText(subjectname);
        }

        edtmy1.setEnabled(false);

        edtmy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  getSubjects();
            }
        });

        //get Today's Date of System
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateq = sdf.format(System.currentTimeMillis());
        et_teachernoteDate.setText(created_note_date);

        editedNoteDate = et_teachernoteDate.getText().toString();//dd mm yyyy

        et_teachernoteDesc.setText(descN);

        updateTeachernote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_teachernoteDesc.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(context, "Enter Mandatory Fields", Toast.LENGTH_SHORT).show();
                } else {
                    editTeacherNote();
                }
            }
        });

        try {
            classSpinner.setOnTouchListener(classSpinnerOnTouch);
            subjectSpinner.setOnTouchListener(subjectSpinnerOnTouch);
        } catch (Exception e) {
            Log.i("", "onCreate: " + e.getMessage());
        }

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtils.checkPermission(EditTeacherNoteACtivity.this)) {
                    selectImage(savedInstanceState);
                }
            }
        });


    }

    private void selectImage(final Bundle savedInstanceState) {

        final CharSequence[] options = {"Take Photo", "Choose Images", "Choose Files",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    showCameraChooser(savedInstanceState);
                } else if (options[item].equals("Choose Images")) {
                    showFileChooser1();
                } else if (options[item].equals("Choose Files")) {
                    showPDFChooser();
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void showPDFChooser() {
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST);
    }

    protected void showCameraChooser(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            try {
                diceRoll = rand.nextInt(100) + 1;

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photo = new File(Environment.getExternalStorageDirectory(),  "selectedPhoto"+diceRoll+".jpeg");
                if(photo.exists()){
                    photo.mkdirs();
                }
                Uri outputFileUri = Uri.fromFile(photo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent, REQUEST_CODE_GET_CAMERA_FILE_PATH);
            } catch (Exception e) {
                e.printStackTrace();
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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("photopath")) {
                outputFileUri = Uri.parse(savedInstanceState.getString("photopath"));
            }
        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    protected void showFileChooser1() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent = Intent.createChooser(intent, "Choose a file");
            startActivityForResult(intent,
                    REQUEST_CODE_GET_FILE_PATH);
        }
    }

    private void checkAttachments() {
        HashMap<String, String> params = new HashMap<>();
        params.put("note_id", notes_id);
        params.put("dailynote_date", dateImage);//yyyy-MM-dd
        params.put("section_id", section_id);
        params.put("short_name", name);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "get_images_daily_notes", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("EDIT_NOTE" + response);
//                progressBar.setVisibility(View.GONE);
                try {
                    if (response.getString("status").equals("true")) {
                        meventImagesArrayListPDFEdit.clear();
                        meventImagesArrayList.clear();

                        String url = response.getString("url");
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
                                meventImagesArrayList.add(eventImages);
                                urltxt.setText(url);
                                updateListPdf();

                            }
                            else {
                                EventImages eventImages = new EventImages();
                                eventImages.setImgName(imagename);
                                meventImagesArrayList1.add(eventImages);
                                urltxt.setText(url);
                                updateListUrlImage();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(jsonObjectRequest);
    }

    //Showing Images from Response
    private void updateListUrlImage() {
        linearLayoutlistview.removeAllViews();
        if (meventImagesArrayList1.size() > 0) {

            for (int i = 0; i < meventImagesArrayList1.size(); i++) {
                setViewImageUrl(i);
            }
        }
    }

    //Selecting Images from gallery
    private void updateListImagesFromGallery() {

        linearLayoutlistview1.removeAllViews();
        if (meventImagesArrayListEdit.size() > 0) {

            for (int j = 0; j < meventImagesArrayListEdit.size(); j++) {
                setViewgallery(j);
            }
        }
    }

    //Selecting Image from Gallery
    private void setViewgallery(final int j) {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.custom_horizontal_imageview,
                null);

        final TextView imageName = baseView.findViewById(R.id.imageName);
        final Button downloadpdf = baseView.findViewById(R.id.downloadpdf);
        final String file_name = meventImagesArrayListEdit.get(j).getImgName();
        imageName.setVisibility(View.VISIBLE);
        imageName.setText(file_name);

        final ImageView imgcancel = baseView.findViewById(R.id.imgcancel);

        final String urlmy = urltxt.getText().toString();
        final String imagename = meventImagesArrayListEdit.get(j).getImgName();

        imageName.setVisibility(View.VISIBLE);
        imageName.setText(meventImagesArrayListEdit.get(j).getImgName());
        imageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditTeacherNoteACtivity.this, ViewFullSizeImage.class);
                intent.putExtra("url", urlmy + "/" + dateImage + "/" + imagename);
                startActivity(intent);
            }
        });

        downloadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadNotice(file_name);
                Toast.makeText(EditTeacherNoteACtivity.this, "Downloading  " + file_name, Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: 29-06-2020 delete api
        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*meventImagesArrayListEdit.remove(j);
                updateListImagesFromGallery();*/

                //Remove attachment api
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                HashMap<String, String> params = new HashMap<>();
//                params.put("upload_date", editedNoteDate);
                params.put("upload_date", dateImage);//yyyy-mm-dd
                params.put("random_no", notes_id);
                params.put("short_name", name);
                params.put("doc_type_folder", "daily_notes");
                params.put("filename", meventImagesArrayListEdit.get(j).getImgName());
                System.out.println(params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/delete_uploaded_files", new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                progressDialog.dismiss();
                                meventImagesArrayListEdit.remove(j);
                                updateListImagesFromGallery();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(EditTeacherNoteACtivity.this);
                requestQueue1.add(jsonObjectRequest);

            }
        });
        linearLayoutlistview1.addView(baseView);
    }

    //Showing Images from Response
    private void setViewImageUrl(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseView = layoutInflater.inflate(R.layout.custom_horizontal_image,
                null);

        final TextView imageName = baseView.findViewById(R.id.imageName);
        final ImageView imgcancel = baseView.findViewById(R.id.imgcancel);
        final Button downloadpdf = baseView.findViewById(R.id.downloadpdf);

        final String file_name = meventImagesArrayList1.get(i).getImgName();
        imageName.setVisibility(View.VISIBLE);
        imageName.setText(file_name);

        downloadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadNotice(file_name);
                Toast.makeText(EditTeacherNoteACtivity.this, "Downloading  " + file_name, Toast.LENGTH_SHORT).show();
            }
        });

        String urlmy = urltxt.getText().toString();

        final String loadurl = urlmy + "/" + file_name;

        imageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditTeacherNoteACtivity.this, ViewFullSizeImage.class);
                intent.putExtra("url", loadurl);
                startActivity(intent);
            }
        });

        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventImages deleted_image = meventImagesArrayList1.get(i);
                meventImagesArrayListDelete.add(deleted_image);
                meventImagesArrayList1.remove(i);
                updateListUrlImage();
            }
        });
        linearLayoutlistview.addView(baseView);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {   //String option;

                listSubject.clear();
                class_id = getClassId(position);
                section_id = getSectionId(position);
                subjectSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listSubject));

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        Intent in = new Intent(EditTeacherNoteACtivity.this, TeachersNoteACtivity.class);
        startActivity(in);
        in.putExtra("reg_id", reg_id);
        in.putExtra("academic_yr", academic_yr);
        EditTeacherNoteACtivity.this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void updateLabel2() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        et_teachernoteDate.setText(sdf.format(calendar.getTime()));
    }

    private String getSubjectId(int position) {
        String subjid = "";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            subjid = json.getString("sm_id");

            //Fetching name from that object

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return subjid;
    }

    private void getSubjects() {
        progressBar.setVisibility(View.VISIBLE);
        final String reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        HashMap<String, String> params = new HashMap<>();
        params.put("academic_yr", academic_yr);
        params.put("reg_id", reg_id);
        params.put("section_id", section_id);
        params.put("class_id", class_id);
        params.put("short_name", name);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "get_subject_alloted_to_teacher_by_class", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressBar.setVisibility(View.GONE);
                try {
                    if (response.getString("status").equals("true")) {
                        result = response.getJSONArray("subject_name");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject jsonObject = result.getJSONObject(i);
                            String subName = jsonObject.getString("name");
                            subid = jsonObject.getString("sm_id");
                            listSubject.add(subName);
                        }
                    } else {
                        Toast.makeText(EditTeacherNoteACtivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                edtmy1.setVisibility(View.GONE);
                subjectSpinner.setVisibility(View.VISIBLE);
                subjectSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listSubject));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getClasses() {
        final String reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        HashMap<String, String> params = new HashMap<>();
        params.put("academic_yr", academic_yr);
        params.put("reg_id", reg_id);
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "get_class_of_classteacher", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {
                    if (response.getString("status").equals("true")) {
                        JSONArray jsonArray = response.getJSONArray("class_name");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String a = jsonObject.getString("classname");
                            String b = jsonObject.getString("sectionname");
                            class_id = jsonObject.getString("class_id");
                            section_id = jsonObject.getString("section_id");
                            listClass.add(a + b);
                        }
                    } else {
                        Toast.makeText(EditTeacherNoteACtivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                classSpinner.setVisibility(View.VISIBLE);
                classSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listClass));

                edtmy.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(jsonObjectRequest);
    }

    private String getClassId(int position) {
        String classs = "";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            classs = json.getString("class_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return classs;
    }

    private String getSectionId(int position) {
        String section = "";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            section = json.getString("section_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return section;
    }

    public void reset() {

        et_teachernoteDesc.setText("");

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.resetnote) {
            reset();
        }
    }

    private boolean validate() {
        boolean b = true;
        String a = et_teachernoteDesc.getText().toString();

        if (a.equals("")) {
            b = false;
        }
        return b;
    }


    private void editTeacherNote() {
        
        progressBar.setVisibility(View.VISIBLE);
        String ImageDataname = "";
        JSONObject mainRequestObject = new JSONObject();
        JSONArray requestArray = new JSONArray();
        JSONArray requestArray2 = new JSONArray();
        String ImageData1 = "";
        final String subid = subject_id;
        final String reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        final String stringTypeRequest = requestArray.toString();
        final String desc;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                newUrl + "AdminApi/daily_notes",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            if (response != null) {
                                Toast.makeText(EditTeacherNoteACtivity.this, "Note updated successfully!!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditTeacherNoteACtivity.this, TeachersNoteACtivity.class);
                                startActivity(intent);
                                EditTeacherNoteACtivity.this.finish();
                            } else {
                                Toast.makeText(EditTeacherNoteACtivity.this, "Can't Update Note", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getMessage();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                final HashMap<String, String> params = new HashMap<>();
                params.put("academic_yr", academic_yr);
                params.put("teacher_id", reg_id);
                params.put("section_id", section_id);
                params.put("class_id", class_id);
                params.put("notes_id", notes_id);
                params.put("short_name", name);
                if (subid == null) {
                    params.put("subject_id", "0");
                } else {
                    params.put("subject_id", subject_id);
                }
                params.put("description", et_teachernoteDesc.getText().toString());
                params.put("login_type", "T");
                params.put("publish", "N");
                //yyyy-mm-dd
                params.put("dailynote_date", dateImage);
                params.put("operation", "edit");


                // for position wise images
                if (meventImagesArrayListPDFEdit.size() > 0 && meventImagesArrayListEdit.size() > 0) {
                    for (int i = 0; i < meventImagesArrayListPDFEdit.size(); i++) {
                        String base = '"' + meventImagesArrayListPDFEdit.get(i).getPdfString() + '"';
                        String fname = '"' + meventImagesArrayListPDFEdit.get(i).getFileName() + '"';
                        attachmentsfile.add(base);
                        attachmentsfile1.add(fname);
                    }
                    for (int j = 0; j < meventImagesArrayListEdit.size(); j++) {
                        String base = '"' + meventImagesArrayListEdit.get(j).getFileBase64Code() + '"';
                        String iname = '"' + meventImagesArrayListEdit.get(j).getImgName() + '"';
                        attachmentsfile.add(base);
                        attachmentsfile1.add(iname);
                    }

                } else if (meventImagesArrayListPDFEdit.size() > 0) {
                    for (int i = 0; i < meventImagesArrayListPDFEdit.size(); i++) {
                        String base = '"' + meventImagesArrayListPDFEdit.get(i).getPdfString() + '"';
                        String fname = '"' + meventImagesArrayListPDFEdit.get(i).getFileName() + '"';
                        attachmentsfile.add(base);
                        attachmentsfile1.add(fname);
                    }
                } else if (meventImagesArrayListEdit.size() > 0) {
                    for (int i = 0; i < meventImagesArrayListEdit.size(); i++) {
                        String base = '"' + meventImagesArrayListEdit.get(i).getFileBase64Code() + '"';
                        String fname = '"' + meventImagesArrayListEdit.get(i).getImgName() + '"';
                        attachmentsfile.add(base);
                        attachmentsfile1.add(fname);
                    }
                }

                if (attachmentsfile.size() == 0 && attachmentsfile1.size() == 0) {
                    params.put("filename", "");
                } else {
                    atchPDF = attachmentsfile.toString();
                    fName = attachmentsfile1.toString();
                    params.put("filename", fName);
                }

                if (meventImagesArrayListDelete.size() > 0) {
                    for (int i = 0; i < meventImagesArrayListDelete.size(); i++) {
                        String delete = '"' + meventImagesArrayListDelete.get(i).getImgName() + '"';
                        deletefile.add(delete);
                        String dfile = deletefile.toString();
                        params.put("deleteimagelist", dfile);
                    }
                }

                System.out.println("EDIT_NOTE_PARAMS"+params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @SuppressLint({"SourceLockedOrientationActivity", "SwitchIntDef"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //images
        if (requestCode == REQUEST_CODE_GET_FILE_PATH
                && resultCode == Activity.RESULT_OK) {

            if (data.getData() != null) {
                uri = data.getData();
                InputStream stream;
                try {
                    stream = this
                            .getContentResolver().openInputStream(
                                    data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);
                    stream.close();
                    final int maxSize = 500;
                    int outWidth;
                    int outHeight;
                    int inWidth = bitmap.getWidth();
                    int inHeight = bitmap.getHeight();
                    if (inWidth > inHeight) {
                        outWidth = maxSize;
                        outHeight = (inHeight * maxSize) / inWidth;
                    } else {
                        outHeight = maxSize;
                        outWidth = (inWidth * maxSize) / inHeight;
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, outWidth,
                            outHeight, true);
                    ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream1);

                    byte[] byte_arr = stream1.toByteArray();
                    File tmpDir = new File(Environment
                            .getExternalStorageDirectory().getAbsolutePath()
                            + "/SchoolApp/");
                    tmpDir.mkdir();

                    fileBase64Code = Base64.encodeToString(byte_arr,
                            Base64.DEFAULT);

                    Log.d("testramanora", "onActivityResult: " + tmpDir.getAbsolutePath());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //To getFile name
                final File f = new File(uri.getPath());
                fileName = f.getName() + ".jpeg";

                //Checking for duplicate name
                HashMap<String, String> params = new HashMap<>();
                params.put("note_id", notes_id);
                params.put("dailynote_date", dateImage);
                params.put("section_id", section_id);
                params.put("short_name", name);
                progressBar.setVisibility(View.VISIBLE);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "get_images_daily_notes", new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            if (response.getString("status").equals("true")) {
                                JSONArray jsonArray = response.getJSONArray("images");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String imagename = jsonObject.getString("image_name");

                                    if (fileName.equals(imagename)) {
                                        fileName = "1_" + f.getName() + ".jpg";
                                    } else {
                                        fileName = f.getName() + ".jpg";
                                    }

                                }

                                //upload attachment method
                                progressDialog.show();

                                HashMap<String, String> params = new HashMap<>();
                                params.put("upload_date", dateImage);//yyyy-mm-dd
                                params.put("random_no", notes_id);
                                params.put("short_name", name);
                                params.put("doc_type_folder", "daily_notes");
                                params.put("filename", fileName);
                                params.put("datafile", fileBase64Code);

                                System.out.println(params);

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println("" + response);
                                        try {
                                            if (response.getString("status").equals("true")) {
                                                progressDialog.dismiss();
                                                //To getFile name
                                                eventImagesEdit = new EventImagesEdit();
                                                eventImagesEdit.setImgName(fileName);
                                                eventImagesEdit.setFileBase64Code(fileBase64Code);
                                                eventImagesEdit.setExtension(":image/jpg;base64");
                                                meventImagesArrayListEdit.add(eventImagesEdit);
                                                updateListImagesFromGallery();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            progressDialog.dismiss();
                                            Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                        Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                                RequestQueue requestQueue1 = Volley.newRequestQueue(EditTeacherNoteACtivity.this);
                                requestQueue1.add(jsonObjectRequest);


                            } else {
                                fileName = f.getName() + ".jpg";
                                /*eventImagesEdit = new EventImagesEdit();
                                eventImagesEdit.setImgName(fileName);
                                eventImagesEdit.setFileBase64Code(fileBase64Code);
                                eventImagesEdit.setExtension(":image/jpg;base64");
                                meventImagesArrayListEdit.add(eventImagesEdit);
                                updateListImagesFromGallery();*/

                                //upload attachment method
                                progressDialog.show();

                                HashMap<String, String> params = new HashMap<>();
//                                params.put("upload_date", editedNoteDate);
                                params.put("upload_date", dateImage);//yyyy-mm-dd
                                params.put("random_no", notes_id);
                                params.put("short_name", name);
                                params.put("doc_type_folder", "daily_notes");
                                params.put("filename", fileName);
                                params.put("datafile", fileBase64Code);

                                System.out.println(params);

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println("" + response);
                                        try {
                                            if (response.getString("status").equals("true")) {
                                                progressDialog.dismiss();
                                                //To getFile name
                                                eventImagesEdit = new EventImagesEdit();
                                                eventImagesEdit.setImgName(fileName);
                                                eventImagesEdit.setFileBase64Code(fileBase64Code);
                                                eventImagesEdit.setExtension(":image/jpg;base64");
                                                meventImagesArrayListEdit.add(eventImagesEdit);
                                                updateListImagesFromGallery();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            progressDialog.dismiss();
                                            Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                        Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                                RequestQueue requestQueue1 = Volley.newRequestQueue(EditTeacherNoteACtivity.this);
                                requestQueue1.add(jsonObjectRequest);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(this);
                requestQueue1.add(jsonObjectRequest);
            }
        }

        //camera images
        else if (requestCode == REQUEST_CODE_GET_CAMERA_FILE_PATH && resultCode == Activity.RESULT_OK) {

            //For camera image blur
            try {
                fileName = diceRoll + ".jpeg";

                //File file = new File(getApplicationContext().getExternalCacheDir().getPath(), "selectedPhoto.jpeg");

                File file = null;
                try {
                    file = new File(Environment.getExternalStorageDirectory(), "selectedPhoto" + diceRoll + ".jpeg");
                    fileName = file.getName();
                }
                catch (Exception e) {
                    //unable to create  temporary file
                    Toast.makeText(EditTeacherNoteACtivity.this, "Unable to create temporary file", Toast.LENGTH_SHORT).show();
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
                    System.out.println(fileBase64Code);
                } catch (Exception e) {
                    //Unable to convert image to byte array
                    Toast.makeText(EditTeacherNoteACtivity.this, "Unable to convert image to byte array", Toast.LENGTH_SHORT).show();
                }

                //upload attachment method
                progressDialog.show();
                HashMap<String, String> params = new HashMap<>();
                params.put("upload_date", dateImage);//yyyy-mm-dd
                params.put("random_no", notes_id);
                params.put("short_name", name);
                params.put("doc_type_folder", "daily_notes");
                params.put("filename", fileName);
                params.put("datafile", fileBase64Code);

                System.out.println(params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                progressDialog.dismiss();
                                //To getFile name
                                eventImagesEdit = new EventImagesEdit();
                                eventImagesEdit.setImgName(fileName);
                                eventImagesEdit.setFileBase64Code(fileBase64Code);
                                eventImagesEdit.setExtension(":image/jpg;base64");
                                meventImagesArrayListEdit.add(eventImagesEdit);
                                updateListImagesFromGallery();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(EditTeacherNoteACtivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));// TODO: 27-07-2020
                RequestQueue requestQueue1 = Volley.newRequestQueue(EditTeacherNoteACtivity.this);
                requestQueue1.getCache().clear();// TODO: 27-07-2020
                requestQueue1.add(jsonObjectRequest);


            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        }
        //Files
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
                    } else if (totalMb > 25) {
                        Toast.makeText(this, "Total File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    } else {
                        //Checking for duplicate name
                        final String fName = pdfFile.getName();
                        HashMap<String, String> params = new HashMap<>();
                        params.put("note_id", notes_id);
                        params.put("dailynote_date", dateImage);
                        params.put("section_id", section_id);
                        params.put("short_name", name);
                        progressBar.setVisibility(View.VISIBLE);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "get_images_daily_notes", new JSONObject(params), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    if (response.getString("status").equals("true")) {

                                        JSONArray jsonArray = response.getJSONArray("images");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String imagename = jsonObject.getString("image_name");

                                            if (fName.equals(imagename)) {
                                                displayName = "1_" + pdfFile.getName();
                                            } else {
                                                displayName = pdfFile.getName();
                                            }

                                        }
                                        convertFileToString(PDFpath);

                                        //upload attachment method
                                        progressDialog.show();
                                        HashMap<String, String> params = new HashMap<>();
//                                        params.put("upload_date", editedNoteDate);
                                        params.put("upload_date", dateImage);//yyyy-mm-dd
                                        params.put("random_no", notes_id);
                                        params.put("short_name", name);
                                        params.put("doc_type_folder", "daily_notes");
                                        params.put("filename", displayName);
                                        params.put("datafile", strFile);

                                        System.out.println(params);

                                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                System.out.println("" + response);
                                                try {
                                                    if (response.getString("status").equals("true")) {
                                                        progressDialog.dismiss();
                                                        //To getFile name
                                                        eventImagesPdfEdit = new EventImagesPdfEdit();
                                                        eventImagesPdfEdit.setFileName(displayName);
                                                        eventImagesPdfEdit.setPdfString(strFile);
                                                        meventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                                        updateListPdfView();
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                                Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                        RequestQueue requestQueue1 = Volley.newRequestQueue(EditTeacherNoteACtivity.this);
                                        requestQueue1.add(jsonObjectRequest);

                                    } else {
                                        convertFileToString(PDFpath);
                                        displayName = pdfFile.getName();

                                        //upload attachment method
                                        progressDialog.show();
                                        HashMap<String, String> params = new HashMap<>();
                                        params.put("upload_date", dateImage);//yyyy-mm-dd
                                        params.put("random_no", notes_id);
                                        params.put("short_name", name);
                                        params.put("doc_type_folder", "daily_notes");
                                        params.put("filename", displayName);
                                        params.put("datafile", strFile);

                                        System.out.println(params);

                                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                System.out.println("" + response);
                                                try {
                                                    if (response.getString("status").equals("true")) {
                                                        progressDialog.dismiss();
                                                        //To getFile name
                                                        eventImagesPdfEdit = new EventImagesPdfEdit();
                                                        eventImagesPdfEdit.setFileName(displayName);
                                                        eventImagesPdfEdit.setPdfString(strFile);
                                                        meventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                                        updateListPdfView();
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                                Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                        RequestQueue requestQueue1 = Volley.newRequestQueue(EditTeacherNoteACtivity.this);
                                        requestQueue1.add(jsonObjectRequest);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
                        requestQueue1.add(jsonObjectRequest);

                    }

                }
            } else {
                if (uriString.startsWith("content://") || uriString.startsWith("file://")) {
                    selectedPDF = data.getData();
                    PDFpath = getPathForPdf(selectedPDF);
                    pdfFile = new File(PDFpath);

                    //Checking the file size before conversion it should be less than 25mb

                    long bytes = pdfFile.length();
                    long mb = bytes / (1024 * 1024);

                    if (mb > 25) {
                        Toast.makeText(this, "File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    } else if (totalMb > 25) {
                        Toast.makeText(this, "Total File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    } else {
                        //Checking for duplicate name
                        final String fName = pdfFile.getName();
                        HashMap<String, String> params = new HashMap<>();
                        params.put("note_id", notes_id);
                        params.put("dailynote_date", dateImage);
                        params.put("section_id", section_id);
                        params.put("short_name", name);
                        progressBar.setVisibility(View.VISIBLE);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "get_images_daily_notes", new JSONObject(params), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    if (response.getString("status").equals("true")) {

                                        JSONArray jsonArray = response.getJSONArray("images");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String imagename = jsonObject.getString("image_name");

                                            if (fName.equals(imagename)) {
                                                displayName = "1_" + pdfFile.getName();
                                            } else {
                                                displayName = pdfFile.getName();
                                            }
                                        }
                                        convertFileToString(PDFpath);

                                        //upload attachment method
                                        progressDialog.show();
                                        HashMap<String, String> params = new HashMap<>();
//                                        params.put("upload_date", editedNoteDate);
                                        params.put("upload_date", dateImage);//yyyy-mm-dd
                                        params.put("random_no", notes_id);
                                        params.put("short_name", name);
                                        params.put("doc_type_folder", "daily_notes");
                                        params.put("filename", displayName);
                                        params.put("datafile", strFile);

                                        System.out.println(params);

                                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                System.out.println("" + response);
                                                try {
                                                    if (response.getString("status").equals("true")) {
                                                        progressDialog.dismiss();
                                                        //To getFile name
                                                        eventImagesPdfEdit = new EventImagesPdfEdit();
                                                        eventImagesPdfEdit.setFileName(displayName);
                                                        eventImagesPdfEdit.setPdfString(strFile);
                                                        meventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                                        updateListPdfView();
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                                Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                        RequestQueue requestQueue1 = Volley.newRequestQueue(EditTeacherNoteACtivity.this);
                                        requestQueue1.add(jsonObjectRequest);


                                    } else {
                                        convertFileToString(PDFpath);
                                        //upload attachment method
                                        progressDialog.show();
                                        HashMap<String, String> params = new HashMap<>();
//                                        params.put("upload_date", editedNoteDate);
                                        params.put("upload_date", dateImage);//yyyy-mm-dd
                                        params.put("random_no", notes_id);
                                        params.put("short_name", name);
                                        params.put("doc_type_folder", "daily_notes");
                                        params.put("filename", displayName);
                                        params.put("datafile", strFile);

                                        System.out.println(params);

                                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                System.out.println("" + response);
                                                try {
                                                    if (response.getString("status").equals("true")) {
                                                        progressDialog.dismiss();
                                                        //To getFile name
                                                        displayName = pdfFile.getName();
                                                        eventImagesPdfEdit = new EventImagesPdfEdit();
                                                        eventImagesPdfEdit.setFileName(displayName);
                                                        eventImagesPdfEdit.setPdfString(strFile);
                                                        meventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                                        updateListPdfView();
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                                Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                        RequestQueue requestQueue1 = Volley.newRequestQueue(EditTeacherNoteACtivity.this);
                                        requestQueue1.add(jsonObjectRequest);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
                        requestQueue1.add(jsonObjectRequest);
                    }
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            uri = null;
            Toast.makeText(this, "Cancelled!",
                    Toast.LENGTH_LONG).show();
        }

        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getOrientation();

        if (orientation == Configuration.ORIENTATION_PORTRAIT || orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        int rotation = display.getRotation();
        if (rotation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (rotation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

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
            } catch (FileNotFoundException e) {
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

        } catch (IOException e) {
            Log.e("IOException = ", String.valueOf(e));
        }

        //Finally we can pass the path of the file we have copied
        return file.getAbsolutePath();
    }

    public String convertFileToString(String pathOnSdCard) {


        File file = new File(pathOnSdCard);
        try {
            byte[] data = FileUtils.readFileToByteArray(file);//Convert any file, image or video into byte array
            strFile = Base64.encodeToString(data, Base64.NO_WRAP);//Convert byte array into string
            Log.d("base", "convertFileToString: " + strFile);
            try {
                File root = new File(Environment.getExternalStorageDirectory(), "TeacherAppFiles");
                if (!root.exists()) {
                    root.mkdirs();
                }
                File gpxfile = new File(root, "/file.txt");
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(strFile);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strFile;
    }

    //Showing attachments from response
    private void updateListPdf() {
        micici_listViewpdf.removeAllViews();
        if (meventImagesArrayList.size() > 0) {
            for (int l = 0; l < meventImagesArrayList.size(); l++) {
                setViewPdf(l);
            }
        }
    }

    //Showing attachments from Response
    private void setViewPdf(final int l) {

        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseViewpdf = layoutInflater.inflate(R.layout.custom_horizontal_files_layout_view,
                null);

        txtpdfname = baseViewpdf.findViewById(R.id.txtimagenamedisplay);
        ImageView cancelimagepdf = baseViewpdf.findViewById(R.id.imgcancelpdf);
        Button downloadpdf = baseViewpdf.findViewById(R.id.downloadpdf);
        String filename = meventImagesArrayList.get(l).getImgName();

        if (filename != null) {
            txtpdfname.setVisibility(View.VISIBLE);
            cancelimagepdf.setVisibility(View.VISIBLE);
            downloadpdf.setVisibility(View.VISIBLE);
            txtpdfname.setText(filename);
        } else {
            txtpdfname.setVisibility(View.GONE);
            cancelimagepdf.setVisibility(View.GONE);
            downloadpdf.setVisibility(View.GONE);
        }

        downloadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = meventImagesArrayList.get(l).getImgName();
                downloadNotice(fName);
                Toast.makeText(EditTeacherNoteACtivity.this, "Downloading  " + fName, Toast.LENGTH_SHORT).show();
            }
        });

        cancelimagepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventImages eventImages = meventImagesArrayList.get(l);
                meventImagesArrayListDelete.add(eventImages);
                meventImagesArrayList.remove(l);
                updateListPdf();
            }
        });

        micici_listViewpdf.addView(baseViewpdf);
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

        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseViewpdf = layoutInflater.inflate(R.layout.custom_horizontal_files_layout_view,
                null);

        txtpdfname = baseViewpdf.findViewById(R.id.txtimagenamedisplay);
        ImageView cancelimagepdf = baseViewpdf.findViewById(R.id.imgcancelpdf);
        Button downloadpdf = baseViewpdf.findViewById(R.id.downloadpdf);
        String filename = meventImagesArrayListPDFEdit.get(l).getFileName();

        if (filename != null) {
            txtpdfname.setVisibility(View.VISIBLE);
            cancelimagepdf.setVisibility(View.VISIBLE);
            downloadpdf.setVisibility(View.GONE);
            txtpdfname.setText(filename);
        } else {
            txtpdfname.setVisibility(View.GONE);
            cancelimagepdf.setVisibility(View.GONE);
            downloadpdf.setVisibility(View.GONE);
        }

        downloadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = meventImagesArrayListPDFEdit.get(l).getFileName();
                downloadNotice(fName);
                Toast.makeText(EditTeacherNoteACtivity.this, "Downloading to Downloads Folder " + fName, Toast.LENGTH_LONG).show();
            }
        });

        cancelimagepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Remove attachment api
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                HashMap<String, String> params = new HashMap<>();
//                params.put("upload_date", editedNoteDate);
                params.put("upload_date", dateImage);//yyyy-mm-dd
                params.put("random_no", notes_id);
                params.put("short_name", name);
                params.put("doc_type_folder", "daily_notes");
                params.put("filename", meventImagesArrayListPDFEdit.get(l).getFileName());
                System.out.println(params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/delete_uploaded_files", new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                progressDialog.dismiss();
                                meventImagesArrayListPDFEdit.remove(l);
                                updateListPdfView();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(EditTeacherNoteACtivity.this,"Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(EditTeacherNoteACtivity.this);
                requestQueue1.add(jsonObjectRequest);


            }
        });

        micici_listViewpdf1.addView(baseViewpdf);
    }

    private void downloadNotice(String fName) {
        if (isReadStorageAllowed()) {
            String downloadUrl;

            if (name.equals("SACS")) {
                downloadUrl = prourl + "uploads/daily_notes/" + dateImage + "/" + notes_id + "/" + fName;
            } else {
                downloadUrl = prourl + "uploads/" + name + "/daily_notes/" + dateImage + "/" + notes_id + "/" + fName;
            }

            dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(downloadUrl);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

            String folder = "Evolvuschool/Teacher";
            request.setDestinationInExternalPublicDir(folder, fName);

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

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;

        //If permission is not granted returning false
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            Toast.makeText(EditTeacherNoteACtivity.this, "To Download Notice Attachment Please Allow the Storage Permission", Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission
        int STORAGE_PERMISSION_CODE = 23;
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

}
