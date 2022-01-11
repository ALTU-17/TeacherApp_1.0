package in.aceventura.evolvuschool.teacherapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.SystemTime;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImages;
import in.aceventura.evolvuschool.teacherapp.utils.FileManager;
import in.aceventura.evolvuschool.teacherapp.utils.PermissionUtils;
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
public class CreateHomeworkActivity extends AppCompatActivity implements View.OnClickListener {

    long totalMb = 0;
    public static int REQUEST_CODE_GET_CAMERA_FILE_PATH = 222;
    public static int REQUEST_CODE_GET_FILE_PATH = 1;
    public static String fileBase64Code, fileName, path, PDFpath, displayName, strFile;
    static Uri uri, outputFileUri, selectedPDF, filePathPdf;
    final Random rand = new Random();
    Bitmap bitmap;
    EventImages eventImages;
    ArrayList<EventImages> meventImagesArrayList;
    ArrayList<EventImages> meventImagesArrayListPDF;
    ArrayList<String> attachmentsFile = new ArrayList<>();
    ArrayList<String> attachmentsFile1 = new ArrayList<>();
    LinearLayout micici_listView, micici_listViewpdf;
    int diceRoll;
    File pdfFile;
    Calendar myCalender;
    Context context;
    String subid, assigndate, atchPDF, fName, reg_id, academic_yr;
    TextView txtdatepick, pickhwfiles, txtpdfname;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    String class_id, section_id, subject_id, desc, date, randomId;
    String name, newUrl, dUrl;
    DatabaseHelper mDatabaseHelper;
    private int PICK_PDF_REQUEST = 12;
    private Spinner spinclass, spinsubject;
    private EditText editdate, edtDesc;
    final DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            myCalender.set(Calendar.YEAR, year);
            myCalender.set(Calendar.MONTH, monthOfYear);
            myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };
    private JSONArray result;
    private ArrayList<String> listClass;
    private ArrayList<String> listSubject;

    private View.OnTouchListener classSpinnerOnTouch = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
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
        @SuppressLint("ClickableViewAccessibility")
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (spinclass.getSelectedItemId() > -1) {
                    listSubject.clear();
                    getSubjects();
                } else {
                    Toast.makeText(getApplicationContext(), "Please select class first", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        }
    };

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_homework);
        myCalender = Calendar.getInstance();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        context = this;
        listClass = new ArrayList<>();
        listSubject = new ArrayList<>();
        assigndate = SystemTime.getSystemDate1(); //System Date //Hw Creation date

        //Generating 4 digit randomId
        Random rand = new Random();
        randomId = String.format("%04d", rand.nextInt(10000));
        System.out.println("RANDOMID" + randomId);

        getId();
        getSupportActionBar().hide();
        View tb_main1 = findViewById(R.id.icd_tb_homeworkdetails);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("("+SharedClass.getInstance(getApplicationContext()).getAcademicYear()+")");

        school_title.setText(" Evolvu Teacher App");
        ht_Teachernote.setText("Create Homework");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        spinclass.setOnTouchListener(classSpinnerOnTouch);
        spinsubject.setOnTouchListener(subjectSpinnerOnTouch);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }

        spinclass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {   //String option;

                listSubject.clear();
                try {
                    class_id = getClassId(position);
                    section_id = getSectionId(position);
                } catch (Exception e) {

                    e.printStackTrace();
                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinsubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {   //String option;

                subject_id = getSubjectId(position);

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        meventImagesArrayList = new ArrayList<>();
        meventImagesArrayListPDF = new ArrayList<>();


        pickhwfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtils.checkPermission(CreateHomeworkActivity.this)) {
                    selectImage(savedInstanceState);
                }
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
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {


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
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
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
                        Intent intent = new Intent(CreateHomeworkActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(CreateHomeworkActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(CreateHomeworkActivity.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(CreateHomeworkActivity.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
    }//on create

    //File Selection Dialog Box...
    private void selectImage(final Bundle savedInstanceState) {
        final CharSequence[] options = {"Take Photo", "Choose Images", "Choose Files", "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(
                this);
        builder.setTitle("Choose File");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    showCameraChooser(savedInstanceState);
                } else if (options[item].equals("Choose Images")) {
                    showFileChooser1();
                } else if (options[item].equals("Choose Files")) {
                    showPDFChooser();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //CAMERA
    protected void showCameraChooser(Bundle savedInstanceState) {


        if (savedInstanceState == null) {
            diceRoll = rand.nextInt(100) + 1;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //For external directory
            File photo = new File(Environment.getExternalStorageDirectory(), "selectedPhoto" + diceRoll + ".jpeg");
            if(photo.exists()){
                photo.mkdirs();
            }
            Uri outputFileUri = Uri.fromFile(photo);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, REQUEST_CODE_GET_CAMERA_FILE_PATH);
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

    //IMAGES
    protected void showFileChooser1() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent = Intent.createChooser(intent, "Choose a file");
        startActivityForResult(intent, REQUEST_CODE_GET_FILE_PATH);
    }

    //DOCUMENTS
    private void showPDFChooser() {
        Intent intent = new Intent();
        intent.setType("application/*"); //show all file format except images...
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST);
    }


    @SuppressLint("SourceLockedOrientationActivity")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Image file from gallery
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GET_FILE_PATH
                && resultCode == Activity.RESULT_OK) {

            assert data != null;
            if (data.getData() != null) {
                uri = data.getData();
                InputStream stream;
                try {
                    stream = this
                            .getContentResolver().openInputStream(
                                    data.getData());
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
                    } else {
                        outHeight = maxSize;
                        outWidth = (inWidth * maxSize) / inHeight;
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, outWidth,
                            outHeight, true);
                    ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream1); //90

                    byte[] byte_arr = stream1.toByteArray();
                    File tmpDir = new File(Environment
                            .getExternalStorageDirectory().getAbsolutePath()
                            + "/SchoolApp/");
                    tmpDir.mkdir();

                    fileBase64Code = Base64.encodeToString(byte_arr,
                            Base64.DEFAULT);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //To getFile name
                File f = new File(uri.getPath());
                fileName = f.getName() + ".jpg";

                //upload attachment method
                progressDialog.show();
                HashMap<String, String> params = new HashMap<>();
                params.put("upload_date", assigndate);
                params.put("random_no", randomId);
                params.put("short_name", name);
                params.put("doc_type_folder", "homework");
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
                                eventImages = new EventImages();
                                eventImages.setImgName(fileName);
                                eventImages.setFileBase64Code(fileBase64Code);
                                eventImages.setExtension(":image/jpg;base64");
                                meventImagesArrayList.add(eventImages);
                                updateListImagesFromGallery();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(CreateHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(CreateHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(CreateHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(CreateHomeworkActivity.this);
                requestQueue1.add(jsonObjectRequest);
            }
        }

        //Take Pic from camera
        else if (requestCode == REQUEST_CODE_GET_CAMERA_FILE_PATH
                && resultCode == Activity.RESULT_OK) {
            try {
                fileName = diceRoll + ".jpeg";

                File file = null;

                try {
                    file = new File(Environment.getExternalStorageDirectory(), "selectedPhoto" + diceRoll + ".jpeg");
                    fileName = file.getName();
                } catch (Exception e) {
                    //unable to create  temporary file
                    Toast.makeText(CreateHomeworkActivity.this, "Unable to create temporary file", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CreateHomeworkActivity.this, "Unable to convert image to byte array", Toast.LENGTH_SHORT).show();
                }

                //upload attachment method
                progressDialog.show();
                HashMap<String, String> params = new HashMap<>();
                params.put("upload_date", assigndate);
                params.put("random_no", randomId);
                params.put("short_name", name);
                params.put("doc_type_folder", "homework");
                params.put("filename", fileName);
                params.put("datafile", fileBase64Code);

                System.out.println("Camera Params" + params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                eventImages = new EventImages();
                                eventImages.setImgName(fileName);
                                eventImages.setFileBase64Code(fileBase64Code);
                                eventImages.setExtension(":image/jpg;base64");
                                meventImagesArrayList.add(eventImages);
                                updateListImagesFromGallery();
                                progressDialog.dismiss();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(CreateHomeworkActivity.this, "HWCameraError1..Please Try again...", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            System.out.println(response);
                            Toast.makeText(CreateHomeworkActivity.this, "HWCameraError2..Please Try again...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(CreateHomeworkActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue1 = Volley.newRequestQueue(CreateHomeworkActivity.this);
                requestQueue1.getCache().clear();// TODO: 27-07-2020
                requestQueue1.add(jsonObjectRequest);

            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }

            //for version >= O
        } else if (requestCode == 101) {

            if (resultCode == RESULT_OK && data != null) {
                selectedPDF = data.getData();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    PDFpath = getPathForPdf(selectedPDF);
                    pdfFile = new File(PDFpath);
                    convertFileToString(PDFpath);
                    displayName = pdfFile.getName();

                    /*eventImages = new EventImages();
                    eventImages.setImgName(displayName);
                    eventImages.setPdfString(strFile);
                    meventImagesArrayListPDF.add(eventImages);
                    updateListPdf();*/

                    //--------------------------upload attachment method----------------------------

                    progressDialog.show();
                    HashMap<String, String> params = new HashMap<>();
                    params.put("upload_date", assigndate);
                    params.put("random_no", randomId);
                    params.put("short_name", name);
                    params.put("doc_type_folder", "homework");
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
                                    eventImages = new EventImages();
                                    eventImages.setImgName(displayName);
                                    eventImages.setPdfString(strFile);
                                    meventImagesArrayListPDF.add(eventImages);
                                    updateListPdf();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(CreateHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(CreateHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("ERROR_RESPONSE - " + error.getMessage());
                            Toast.makeText(CreateHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                    RequestQueue requestQueue1 = Volley.newRequestQueue(CreateHomeworkActivity.this);
                    requestQueue1.add(jsonObjectRequest);
                    //------------------------------------------------------------------------------

                    pdfFile = new File(FileManager.getPath(CreateHomeworkActivity.this, -1, selectedPDF));
                }
            }
        }

        // File from FileManager...
        else if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePathPdf = data.getData();
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
                        convertFileToString(PDFpath);
                        displayName = pdfFile.getName();

                        /*
                        eventImages = new EventImages();
                        eventImages.setImgName(displayName);
                        eventImages.setPdfString(strFile);
                        meventImagesArrayListPDF.add(eventImages);
                        updateListPdf();
                        */

                        //--------------------------upload attachment method----------------------------

                        progressDialog.show();
                        HashMap<String, String> params = new HashMap<>();
                        params.put("upload_date", assigndate);
                        params.put("random_no", randomId);
                        params.put("short_name", name);
                        params.put("doc_type_folder", "homework");
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
                                        eventImages = new EventImages();
                                        eventImages.setImgName(displayName);
                                        eventImages.setPdfString(strFile);
                                        meventImagesArrayListPDF.add(eventImages);
                                        updateListPdf();

                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(CreateHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    Toast.makeText(CreateHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                Toast.makeText(CreateHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                        RequestQueue requestQueue1 = Volley.newRequestQueue(CreateHomeworkActivity.this);
                        requestQueue1.add(jsonObjectRequest);
                        //------------------------------------------------------------------------------

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
                    } else {
                        convertFileToString(PDFpath);
                        displayName = pdfFile.getName();

                        /*eventImages = new EventImages();
                        eventImages.setImgName(displayName);
                        eventImages.setPdfString(strFile);
                        meventImagesArrayListPDF.add(eventImages);
                        updateListPdf();*/

                        //--------------------------upload attachment method----------------------------

                        progressDialog.show();
                        HashMap<String, String> params = new HashMap<>();
                        params.put("upload_date", assigndate);
                        params.put("random_no", randomId);
                        params.put("short_name", name);
                        params.put("doc_type_folder", "homework");
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
                                        eventImages = new EventImages();
                                        eventImages.setImgName(displayName);
                                        eventImages.setPdfString(strFile);
                                        meventImagesArrayListPDF.add(eventImages);
                                        updateListPdf();

                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(CreateHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    Toast.makeText(CreateHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                Toast.makeText(CreateHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                        RequestQueue requestQueue1 = Volley.newRequestQueue(CreateHomeworkActivity.this);
                        requestQueue1.add(jsonObjectRequest);
                        //------------------------------------------------------------------------------
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

    private void updateListPdf() {
        micici_listViewpdf.removeAllViews();
        if (meventImagesArrayListPDF.size() > 0) {
            for (int m = 0; m < meventImagesArrayListPDF.size(); m++) {
                setViewPdf(m);
            }
        }
    }

    private void setViewPdf(final int m) {

        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("InflateParams") final View baseViewpdf = layoutInflater.inflate(R.layout.custom_horizontal_files_homework,
                null);

        txtpdfname = baseViewpdf.findViewById(R.id.txtimagenamedisplay);
        ImageView cancelimagepdf = baseViewpdf.findViewById(R.id.imgcancelpdf);

        if (PDFpath != null) {
            txtpdfname.setVisibility(View.VISIBLE);
            cancelimagepdf.setVisibility(View.VISIBLE);
            txtpdfname.setText(meventImagesArrayListPDF.get(m).getImgName());
        } else {
            txtpdfname.setVisibility(View.GONE);
            cancelimagepdf.setVisibility(View.GONE);
        }

        cancelimagepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meventImagesArrayListPDF.remove(m);
                updateListPdf();
            }
        });
        micici_listViewpdf.addView(baseViewpdf);
    }

    public void convertFileToString(String pathOnSdCard) {

        // String strFile=null;
        File file = new File(pathOnSdCard);
        try {
            byte[] data = FileUtils.readFileToByteArray(file);//Convert any file, image or video into byte array
            strFile = Base64.encodeToString(data, Base64.NO_WRAP);//Convert byte array into string
            File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "TeacherAppFiles");
            if (!root.exists()) root.mkdirs();
            File gpxfile = new File(root, "/file.txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(strFile);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
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

            byte[] data = new byte[8192];
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

    //Image file from gallery
    private void updateListImagesFromGallery() {

        micici_listView.removeAllViews();
        if (meventImagesArrayList.size() > 0) {

            for (int j = 0; j < meventImagesArrayList.size(); j++) {
                setViewgallery(j);
            }
        }
    }

    private void setViewgallery(final int j) {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.custom_horizontal_homework,
                null);
        final TextView imageName = baseView.findViewById(R.id.imageName);
        final ImageView imgcancel = baseView.findViewById(R.id.imgcancel);
        String img_data = meventImagesArrayList.get(j).getFileBase64Code();
        String file_name = meventImagesArrayList.get(j).getImgName();
        imageName.setVisibility(View.VISIBLE);
        imageName.setText(file_name);
        String imagename = meventImagesArrayList.get(j).getImgName();
        String urlmy = path;

        final String loadurl = urlmy + "/" + imagename;
        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*meventImagesArrayList.remove(j);
                updateListImagesFromGallery();*/

                //Remove attachment api
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                HashMap<String, String> params = new HashMap<>();
                params.put("upload_date", assigndate);
                params.put("random_no", randomId);
                params.put("short_name", name);
                params.put("doc_type_folder", "homework");
                params.put("filename", fileName);
                System.out.println(params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/delete_uploaded_files", new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("DELETENOTE" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                progressDialog.dismiss();
                                meventImagesArrayList.remove(j);
                                updateListImagesFromGallery();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(CreateHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(CreateHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(CreateHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(CreateHomeworkActivity.this);
                requestQueue1.add(jsonObjectRequest);

            }
        });
        micici_listView.addView(baseView);
    }

    //============================================================
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

    private String getClassId(int position) {
        String classId = "";
        try {
            JSONObject json = result.getJSONObject(position);
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
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            section = json.getString("section_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return section;
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //  listSubject.add(0,"Select");
                spinsubject.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.mytextview, listSubject));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CreateHomeworkActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(jsonObjectRequest);
    }

    private void getClasses() {
        final String reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        HashMap<String, String> params = new HashMap<>();
        params.put("academic_yr", academic_yr);
        params.put("reg_id", reg_id);
        params.put("short_name", name);

        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "getClassAndSection_teacheralloted", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {
                    if (response.getString("status").equals("true")) {
                        result = response.getJSONArray("class_name");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject jsonObject = result.getJSONObject(i);
                            String a = jsonObject.getString("classname");
                            String b = jsonObject.getString("sectionname");
                            class_id = jsonObject.getString("class_id");
                            section_id = jsonObject.getString("section_id");
                            listClass.add(a + b);
                        }
                    } else {
                        Toast.makeText(CreateHomeworkActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinclass.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.mytextview, listClass));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(jsonObjectRequest);
    }

    private boolean validate() {
        boolean b = true;
        if (edtDesc.getText().toString().equals("")) {
            b = false;
        }

        if (editdate.getText().toString().equals("")) {

            b = false;
        }
        if (spinclass.getSelectedItemPosition() < 0) {
            b = false;
        }

        if (spinsubject.getSelectedItemPosition() < 0) {
            b = false;
        }
        return b;
    }

    private void getId() {
        spinclass = findViewById(R.id.classSpinhmk);
        spinsubject = findViewById(R.id.subjectSpinhmk);
        editdate = findViewById(R.id.edtdatehmk);
        Button btnsave = findViewById(R.id.btnsavehmk);
        txtdatepick = findViewById(R.id.txtdatehmk);
        pickhwfiles = findViewById(R.id.pickhwfiles);
        micici_listView = findViewById(R.id.micici_listView);
        micici_listViewpdf = findViewById(R.id.micici_listViewpdf);

        edtDesc = findViewById(R.id.edtdeschmk);
        Button btnReset = findViewById(R.id.btnResethmk);

        editdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtdatepick.setFocusable(false);

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 1);
                //Set yesterday time milliseconds as date pickers minimum date
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateHomeworkActivity.this, startDate,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                Field mDatePickerField;
                try {
                    mDatePickerField = datePickerDialog.getClass().getDeclaredField("mDatePicker");
                    mDatePickerField.setAccessible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        btnsave.setOnClickListener(this);
        btnReset.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void clear() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editdate.setText(sdf.format(myCalender.getTimeInMillis()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnsavehmk:
                if (validate()) {
                    createHomework();

                } else {
                    dialogBox();
                }
                break;
            case R.id.btnResethmk:
                clear();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        Intent in = new Intent(CreateHomeworkActivity.this, HomeworkActivity.class);
        startActivity(in);
        in.putExtra("reg_id", reg_id);
        in.putExtra("academic_yr", academic_yr);
        startActivity(in);
        CreateHomeworkActivity.this.finish();
    }

    private void dialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateHomeworkActivity.this);
        {
            builder.setCancelable(true);
            builder.setTitle("Alert");
            builder.setMessage("Fill All * Fields ");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            builder.create().show();
        }
    }

    private void createHomework() {
        System.out.println("SystemDate - " + assigndate);
        progressBar.setVisibility(View.VISIBLE);

        desc = edtDesc.getText().toString();
        date = editdate.getText().toString();   //Submission Date
        final String reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                newUrl + "AdminApi/homework",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            if (response != null) {
                                Toast.makeText(CreateHomeworkActivity.this, "Homework created successfully!!!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(CreateHomeworkActivity.this, HomeworkActivity.class);
                                startActivity(intent);
                                CreateHomeworkActivity.this.finish();
                            } else {
                                Toast.makeText(CreateHomeworkActivity.this, "Can't Create Homework", Toast.LENGTH_SHORT).show();
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
                        System.out.println("HW_error" + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                final HashMap<String, String> params = new HashMap<>();
                params.put("random_no", randomId);
                params.put("academic_yr", academic_yr);
                params.put("teacher_id", reg_id);
                params.put("section_id", section_id);
                params.put("class_id", class_id);
                params.put("sm_id", subject_id);
                params.put("description", desc);
                params.put("publish", "N");
                params.put("end_date", date);
                params.put("start_date", assigndate);//dd-MM-yyyy
                params.put("login_type", "T");
                params.put("operation", "create");
                params.put("short_name", name);

                if (meventImagesArrayListPDF.size() > 0 && meventImagesArrayList.size() > 0) {
                    for (int i = 0; i < meventImagesArrayListPDF.size(); i++) {
                        String base = '"' + meventImagesArrayListPDF.get(i).getPdfString() + '"';
                        String fname = '"' + meventImagesArrayListPDF.get(i).getImgName() + '"';
                        attachmentsFile.add(base);
                        attachmentsFile1.add(fname);
                    }
                    for (int j = 0; j < meventImagesArrayList.size(); j++) {
                        String base = '"' + meventImagesArrayList.get(j).getFileBase64Code() + '"';
                        String iname = '"' + meventImagesArrayList.get(j).getImgName() + '"';
                        attachmentsFile.add(base);
                        attachmentsFile1.add(iname);
                    }
                } else if (meventImagesArrayListPDF.size() > 0) {
                    for (int i = 0; i < meventImagesArrayListPDF.size(); i++) {
                        String base = '"' + meventImagesArrayListPDF.get(i).getPdfString() + '"';
                        String fname = '"' + meventImagesArrayListPDF.get(i).getImgName() + '"';
                        attachmentsFile.add(base);
                        attachmentsFile1.add(fname);
                    }
                } else if (meventImagesArrayList.size() > 0) {
                    for (int i = 0; i < meventImagesArrayList.size(); i++) {
                        String base = '"' + meventImagesArrayList.get(i).getFileBase64Code() + '"';
                        String fname = '"' + meventImagesArrayList.get(i).getImgName() + '"';
                        attachmentsFile.add(base);
                        attachmentsFile1.add(fname);
                    }
                }

                if (attachmentsFile.size() == 0 && attachmentsFile1.size() == 0) {
                    params.put("filename", "");
                } else {
                    atchPDF = attachmentsFile.toString();
                    fName = attachmentsFile1.toString();
                    params.put("filename", fName);
                }
                Log.d("test", "getParams: call " + params);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}
