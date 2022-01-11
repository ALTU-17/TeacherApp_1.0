package in.aceventura.evolvuschool.teacherapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import in.aceventura.evolvuschool.teacherapp.HomePageDrawerActivity;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.adapter.TeacherNoteAdapter;
import in.aceventura.evolvuschool.teacherapp.bottombar.MyCalendar;
import in.aceventura.evolvuschool.teacherapp.pojo.ClassPojo;
import in.aceventura.evolvuschool.teacherapp.pojo.DataSet;
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

public class CreateTeachernote extends AppCompatActivity implements View.OnClickListener {
    public static int REQUEST_CODE_GET_CAMERA_FILE_PATH = 222;
    public static int REQUEST_CODE_GET_FILE_PATH = 1;
    public static String fileBase64Code, fileName;
    static Uri uri;
    final Random rand = new Random();
    public String selectedclasssection;
    public String atchPDF, atchIMG, iName, fName;
    EditText et_teachernoteDate, et_teachernoteDesc, file_path, date;
    ImageView selectedImage;
    Button selectFile, saveTeachernote, resetTeachernote, backTeachernote, createTeachernote, manageTeachernote,
            btnpick;
    String logintype, dateImage;
    String desc, name, newUrl, dUrl, reg_id, academic_yr;
    String mCurrentPhotoPath;
    TextView pickdate, pickphoto;
    SharedClass sh;
    String thisDate;
    String class_id, section_id, subject_id;
    Calendar calendar;
    RequestQueue requestQueue;
    String subid;
    EventImages eventImages;
    long totalMb = 0;
    //new
    Uri selectedIDImage, selectedPDF;
    String PDFpath;
    File pdfFile;
    ArrayList<EventImages> meventImagesArrayList;
    String Companyname, Imagedata, ImageName;
    int width, height, btnwidth, btnwidth_fortwo;
    int diceRoll;
    Bitmap bitmap;
    Uri outputFileUri;
    Random Number;
    TextView txtimage;
    int Rnumber;
    ImageView img;
    TextView txt;
    //pdf
    LinearLayout mUploadCatlog;
    Uri filePathpdf;
    TextView txtpdfname;
    String displayName = null;
    String strFile = null;
    ArrayList<EventImages> meventImagesArrayListPDF;
    ImageButton imageButtonSUbmit;
    String path;
    //class and subject checkbox
    List<String> valSetOne;
    LinearLayout linearLayoutClasscheckbox;
    LinearLayout linearLayoutlistview1;
    ArrayList<ClassPojo> classPojoArrayList;
    CheckBox[] checkBoxes;
    ArrayList<String> selectedCheckboxes;
    ArrayList<String> attachments = new ArrayList<>();
    ArrayList<String> attachments1 = new ArrayList<>();
    LinearLayout micici_listViewpdf, micici_listView;
    ArrayList<String> attachmentsfile = new ArrayList<>();
    ArrayList<String> attachmentsfile1 = new ArrayList<>();
    ProgressBar progressBar;
    DatabaseHelper mDatabaseHelper;
    String randomId;
    Thread t;
    int status = 0;
    Handler handler = new Handler();
    int jumpTime = 0;
    private int PICK_PDF_REQUEST = 12;
    private Spinner classSpinner, subjectSpinner;
    private ArrayList<String> listClass;
    private ArrayList<String> listSubject;
    private List<DataSet> list = new ArrayList<>();
    private ListView listView;
    private JSONArray result;
    private ProgressDialog progressDialog;
    private TeacherNoteAdapter tmTeachernoteAdapter;
    private View.OnTouchListener subjectSpinnerOnTouch = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (selectedCheckboxes.size() == 0) {
                    Toast.makeText(CreateTeachernote.this, "Select Class First", Toast.LENGTH_SHORT).show();
                } else {
                    listSubject.clear();
                    getSubjects();
                }
            }
            return false;
        }
    };

    public static boolean checkPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_teachernote);
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
        ht_Teachernote.setText("View Teacher Note");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        //Generating 4 digit randomId
        Random rand = new Random();
        randomId = String.format("%04d", rand.nextInt(10000));
        System.out.println("RANDOMID" + randomId);

        sh = new SharedClass(this);
        txtimage = findViewById(R.id.txtimage);
        requestQueue = Volley.newRequestQueue(getBaseContext());
        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }

        System.out.println("NEW_URl" + newUrl);

        date = findViewById(R.id.date);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        calendar = Calendar.getInstance();
        selectedImage = findViewById(R.id.iv_image);
        pickphoto = findViewById(R.id.pickphoto);
        selectFile = findViewById(R.id.uploadFile);
        saveTeachernote = findViewById(R.id.saveTeachernote);
        resetTeachernote = findViewById(R.id.resetTeachernote);
        pickdate = findViewById(R.id.datepick);
        micici_listView = findViewById(R.id.micici_listView);
        linearLayoutClasscheckbox = findViewById(R.id.linearlayoutclassCHeckbox);
        saveTeachernote.setOnClickListener(this);
        resetTeachernote.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        Display display = CreateTeachernote.this.getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        btnwidth = (width / 3) - 10;
        btnwidth_fortwo = (width / 2) - 10;
        // SetUI();
        selectFile = findViewById(R.id.pick);

        //For camera image blur
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        meventImagesArrayList = new ArrayList<>();


        pickphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtils.checkPermission(CreateTeachernote.this)) {
                    selectImage(savedInstanceState);
                } else {
                    Toast.makeText(CreateTeachernote.this, "Permissions not granted !!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        et_teachernoteDesc = findViewById(R.id.et_teachernote_desc);
        listClass = new ArrayList<>();
        listSubject = new ArrayList<>();

        subjectSpinner = findViewById(R.id.sp_tn_subject);
        file_path = findViewById(R.id.file_Path);


        //pdf
        getClassescheckbox();
        meventImagesArrayListPDF = new ArrayList<>();
        micici_listViewpdf = findViewById(R.id.micici_listViewpdf);

        eventImages = new EventImages();
        subjectSpinner.setOnTouchListener(subjectSpinnerOnTouch);

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {   //String option;

                subject_id = getSubjectId(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //get Today's Date of System
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateq = sdf.format(System.currentTimeMillis());
        date.setText(dateq);


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
                        Intent intent = new Intent(CreateTeachernote.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(CreateTeachernote.this, MyCalendar.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {


                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(CreateTeachernote.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(CreateTeachernote.this, HomePageDrawerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }


    }//OnCreate

    @Override
    public boolean onSupportNavigateUp() {
        CreateTeachernote.this.finish();
        return true;
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
        } else {
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
                params.put("upload_date", date.getText().toString());
                params.put("random_no", randomId);
                params.put("short_name", name);
                params.put("doc_type_folder", "daily_notes");
                params.put("filename", fileName);
                System.out.println("delete1" + params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi" +
                        "/delete_uploaded_files", new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                progressDialog.dismiss();
                                meventImagesArrayList.remove(i);
                                updateList();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(CreateTeachernote.this, "Error..Please Try again...",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(CreateTeachernote.this, "Error..Please Try again...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(CreateTeachernote.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });

                RequestQueue requestQueue1 = Volley.newRequestQueue(CreateTeachernote.this);
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
        } else {

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
                params.put("upload_date", date.getText().toString());
                params.put("random_no", randomId);
                params.put("short_name", name);
                params.put("doc_type_folder", "daily_notes");
                params.put("filename", fileName);
                System.out.println("delete2" + params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi" +
                        "/delete_uploaded_files", new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                progressDialog.dismiss();
                                meventImagesArrayListPDF.remove(j);
                                updateList();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(CreateTeachernote.this, "Error..Please Try again...",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(CreateTeachernote.this, "Error..Please Try again...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, error -> {
                    System.out.println("ERROR_RESPONSE - " + error.getMessage());
                    Toast.makeText(CreateTeachernote.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(CreateTeachernote.this);
                requestQueue1.add(jsonObjectRequest);


            }
        });


        micici_listView.addView(baseView);
    }

    private void selectImage(final Bundle savedInstanceState) {

        final CharSequence[] options = {"Take Photo", "Choose Images", "Choose Files", "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
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

    //Code for camera files
    protected void showCameraChooser(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            try {
                diceRoll = rand.nextInt(100) + 1;
                //For external directory
                File photo = new File(Environment.getExternalStorageDirectory(), "selectedPhoto" + diceRoll +
                        ".jpeg");
                if (photo.exists()) {
                    photo.mkdirs();
                }
                Uri outputFileUri = Uri.fromFile(photo);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent, REQUEST_CODE_GET_CAMERA_FILE_PATH);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(CreateTeachernote.this, "NoteCameraError1" + e.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("Error" + e.getMessage());
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    //Code for gallery images
    protected void showFileChooser1() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        // intent.setType("*/*");
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
                    } else {
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

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("FILESERROR0","ERRROR>"+e.getMessage());
                }
                catch (Exception e){
                    Log.e("FILESERROR0","ERRROR>"+e.getMessage());

                }

                //To getFile name
                File f = new File(uri.getPath());
                fileName = f.getName() + ".jpg";

                //upload attachment method

                progressDialog.show();
                HashMap<String, String> params = new HashMap<>();
                params.put("upload_date", date.getText().toString());
                params.put("random_no", randomId);
                params.put("doc_type_folder", "daily_notes");
                params.put("short_name", name);
                params.put("filename", fileName);
                params.put("datafile", fileBase64Code);

                System.out.println(params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_files",
                        new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                progressDialog.dismiss();
                                //To getFile name
                                eventImages = new EventImages();
                                eventImages.setImgName(fileName);
                                eventImages.setFileBase64Code(fileBase64Code);
                                eventImages.setExtension(":image/jpg;base64");
                                meventImagesArrayList.add(eventImages);
                                updateListImagesFromGallery();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(CreateTeachernote.this, "Error..Please Try again...",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(CreateTeachernote.this, "Error..Please Try again...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(CreateTeachernote.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(this);
                requestQueue1.add(jsonObjectRequest);
            }
        }


        //Images from camera
        else if (requestCode == REQUEST_CODE_GET_CAMERA_FILE_PATH && resultCode == Activity.RESULT_OK) {

            //For camera image blur
            try {
                fileName = diceRoll + ".jpeg";
                File file = null;

                try {
                    file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                            "selectedPhoto" + diceRoll + ".jpeg");
                    fileName = file.getName();
                } catch (Exception e) {
                    //unable to create  temporary file
                    Toast.makeText(CreateTeachernote.this, "Unable to create temporary file",
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
                } catch (Exception e) {
                    //Unable to convert image to byte array
                    Toast.makeText(CreateTeachernote.this, "Unable to convert image to byte array",
                            Toast.LENGTH_SHORT).show();
                }

                //upload attachment method
                progressDialog.show();
                HashMap<String, String> params = new HashMap<>();
                params.put("upload_date", date.getText().toString());
                params.put("random_no", randomId);
                params.put("short_name", name);
                params.put("doc_type_folder", "daily_notes");
                params.put("filename", fileName);
                params.put("datafile", fileBase64Code);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_files",
                        new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("CAMERA_UPLOAD_RESPONSE" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                //To getFile name
                                eventImages = new EventImages();
                                eventImages.setImgName(fileName);
                                eventImages.setFileBase64Code(fileBase64Code);
                                eventImages.setExtension(":image/jpeg;base64");
                                meventImagesArrayList.add(eventImages);
                                updateListImagesFromGallery();
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(CreateTeachernote.this, "NoteCameraError2_Please Try again...",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(CreateTeachernote.this, "NoteCameraError4..Please Try again...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(CreateTeachernote.this, error.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        progressDialog.dismiss();
                    }
                });

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue1 = Volley.newRequestQueue(this);
                requestQueue1.getCache().clear();// TODO: 27-07-2020
                requestQueue1.add(jsonObjectRequest);

            } catch (NullPointerException | IOException e) {
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
                    displayName = pdfFile.getName();

                    //upload attachment method
                    progressDialog.show();
                    HashMap<String, String> params = new HashMap<>();
                    params.put("upload_date", date.getText().toString());
                    params.put("random_no", randomId);
                    params.put("short_name", name);
                    params.put("doc_type_folder", "daily_notes");
                    params.put("filename", displayName);
                    params.put("datafile", strFile);
                    System.out.println(params);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_files"
                            , new JSONObject(params), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("" + response);
                            try {
                                if (response.getString("status").equals("true")) {
                                    progressDialog.dismiss();
                                    //To getFile name
                                    eventImages = new EventImages();
                                    eventImages.setImgName(displayName);
                                    eventImages.setPdfString(strFile);
                                    meventImagesArrayListPDF.add(eventImages);
                                    updateListPdf();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(CreateTeachernote.this, "Error..Please Try again...",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(CreateTeachernote.this, "Error..Please Try again...",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("ERROR_RESPONSE - " + error.getMessage());
                            Toast.makeText(CreateTeachernote.this, "Error..Please Try again...",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                    RequestQueue requestQueue1 = Volley.newRequestQueue(this);
                    requestQueue1.add(jsonObjectRequest);

                    pdfFile = new File(FileManager.getPath(CreateTeachernote.this, -1, selectedPDF));
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
                    } else if (totalMb > 25) {
                        Toast.makeText(this, "Total File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    } else {
                        convertFileToString(PDFpath);
                        displayName = pdfFile.getName();

                        //upload attachment method
                        progressDialog.show();

                        HashMap<String, String> params = new HashMap<>();
                        params.put("upload_date", date.getText().toString());
                        params.put("random_no", randomId);
                        params.put("short_name", name);
                        params.put("doc_type_folder", "daily_notes");
                        params.put("filename", displayName);
                        params.put("datafile", strFile);

                        System.out.println(params);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi" +
                                "/upload_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println("" + response);
                                try {
                                    if (response.getString("status").equals("true")) {
                                        progressDialog.dismiss();
                                        //To getFile name
                                        eventImages = new EventImages();
                                        eventImages.setImgName(displayName);
                                        eventImages.setPdfString(strFile);
                                        meventImagesArrayListPDF.add(eventImages);
                                        updateListPdf();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(CreateTeachernote.this, "Error..Please Try again...",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    Toast.makeText(CreateTeachernote.this, "Error..Please Try again...",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                Toast.makeText(CreateTeachernote.this, "Error..Please Try again...",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
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
                    totalMb = totalMb + mb;

                    if (mb > 25) {
                        Toast.makeText(this, "File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    } else if (totalMb > 25) {
                        Toast.makeText(this, "Total File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    } else {
                        convertFileToString(PDFpath);
                        displayName = pdfFile.getName();

                        //upload attachment method
                        progressDialog.show();

                        HashMap<String, String> params = new HashMap<>();
                        params.put("upload_date", date.getText().toString());
                        params.put("random_no", randomId);
                        params.put("short_name", name);
                        params.put("doc_type_folder", "daily_notes");
                        params.put("filename", displayName);
                        params.put("datafile", strFile);

                        System.out.println(params);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi" +
                                "/upload_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println("" + response);
                                try {
                                    if (response.getString("status").equals("true")) {
                                        progressDialog.dismiss();
                                        //To getFile name
                                        eventImages = new EventImages();
                                        eventImages.setImgName(displayName);
                                        eventImages.setPdfString(strFile);
                                        meventImagesArrayListPDF.add(eventImages);
                                        updateListPdf();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(CreateTeachernote.this, "Error1..Please Try again...",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    Toast.makeText(CreateTeachernote.this, "Error2..Please Try again...",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                Toast.makeText(CreateTeachernote.this, "Error3..Please Try again...",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
                        requestQueue1.add(jsonObjectRequest);

                    }

                }
            }
        } else if (resultCode == RESULT_CANCELED) {
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
        } else if (rotation == Configuration.ORIENTATION_LANDSCAPE) {
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
//
                //Remove attachment api
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                HashMap<String, String> params = new HashMap<>();
                params.put("upload_date", date.getText().toString());
                params.put("random_no", randomId);
                params.put("short_name", name);
                params.put("doc_type_folder", "daily_notes");
                params.put("filename", fileName);
                System.out.println("delete3" + params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi" +
                        "/delete_uploaded_files", new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("DELETE_NOTE" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                progressDialog.dismiss();
                                meventImagesArrayList.remove(j);
                                updateListImagesFromGallery();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(CreateTeachernote.this, "Error..Please Try again...",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(CreateTeachernote.this, "Error..Please Try again...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(CreateTeachernote.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(CreateTeachernote.this);
                requestQueue1.add(jsonObjectRequest);


            }
        });
        micici_listView.addView(baseView);
    }

    public void convertFileToString(String pathOnSdCard) {

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

    //2.FileManager Documents
    private void updateListPdf() {
        micici_listViewpdf.removeAllViews();
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
            String file_name = meventImagesArrayListPDF.get(m).getImgName();
        } else {
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

                params.put("upload_date", date.getText().toString());
                params.put("random_no", randomId);
                params.put("short_name", name);
                params.put("doc_type_folder", "daily_notes");
                params.put("filename", meventImagesArrayListPDF.get(m).getImgName());
                System.out.println("delete4" + params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi" +
                        "/delete_uploaded_files", new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                progressDialog.dismiss();
                                meventImagesArrayListPDF.remove(m);
                                updateListPdf();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(CreateTeachernote.this, "Error..Please Try again...",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(CreateTeachernote.this, "Error..Please Try again...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, error -> {
                    System.out.println("ERROR_RESPONSE - " + error.getMessage());
                    Toast.makeText(CreateTeachernote.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(CreateTeachernote.this);
                requestQueue1.add(jsonObjectRequest);

            }
        });
        micici_listViewpdf.addView(baseViewpdf);
    }

    //Dynamic checkboxes
    private void getClassescheckbox() {

        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        HashMap<String, String> params = new HashMap<>();
        params.put("academic_yr", academic_yr);
        params.put("reg_id", reg_id);
        params.put("short_name", name);
        System.out.println(params);

        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi" +
                "/getClassAndSection_teacheralloted", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                if (response != null) {
                    try {
                        if (response.getString("status").equals("true")) {
                            valSetOne = new ArrayList<>();
                            classPojoArrayList = new ArrayList<>();
                            result = response.getJSONArray("class_name");
                            checkBoxes = new CheckBox[result.length()];
                            selectedCheckboxes = new ArrayList<>();
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject jsonObject = result.getJSONObject(i);
                                String a = jsonObject.getString("classname");
                                String b = jsonObject.getString("sectionname");
                                class_id = jsonObject.getString("class_id");
                                section_id = jsonObject.getString("section_id");

                                //Set dynamic checkbox and get values of particular checkboxes
                                final ClassPojo classPojo = new ClassPojo(class_id, section_id, a, b, false,
                                        Integer.parseInt(section_id));
                                classPojoArrayList.add(classPojo);
                                checkBoxes[i] = new CheckBox(CreateTeachernote.this); //here i is iteration
                                // variable
                                checkBoxes[i].setText(a + " " + b);
                                checkBoxes[i].setTag(i + 1);
                                checkBoxes[i].setId(i + 1);
                                checkBoxes[i].setChecked(false);
                                checkBoxes[i].setTextColor(Color.BLACK);
                                checkBoxes[i].setTextSize(12f);
                                linearLayoutClasscheckbox.addView(checkBoxes[i]);

                                final int finalI = i;
                                checkBoxes[i].setOnCheckedChangeListener((compoundButton, isChecked) -> {

                                    ClassPojo classPojo1 = classPojoArrayList.get(finalI);
                                    if (isChecked) {
                                        selectedCheckboxes.add('"' + classPojo1.getClassid() + "^" + classPojo1.getSectionid() + '"');
                                    } else {
                                        selectedCheckboxes.remove('"' + classPojo1.getClassid() + "^" + classPojo1.getSectionid() + '"');

                                    }
                                });
                            }

                        } else {
                            Toast.makeText(CreateTeachernote.this, "No Record Found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(CreateTeachernote.this, "No Response from Server", Toast.LENGTH_SHORT).show();
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

    //get class spinner
    private void getClasses() {

        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        HashMap<String, String> params = new HashMap<>();
        params.put("academic_yr", academic_yr);
        params.put("reg_id", reg_id);
        params.put("short_name", name);
        System.out.println(params);
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" +
                "getClassAndSection_teacheralloted", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                progressBar.setVisibility(View.GONE);
                if (response != null) {
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
                            Toast.makeText(CreateTeachernote.this, "No Record Found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //  listClass.add(0,"Select");
                classSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.mytextview,
                        listClass));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(jsonObjectRequest);
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

    private String getClass(int position) {
        String classId = "";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            classId = json.getString("classname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return classId;
    }

    private String getSection(int position) {
        String section = "";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            section = json.getString("sectionname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return section;
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

    private boolean validate() {
        boolean b = true;
        if (et_teachernoteDesc.getText().toString().equals("")) {
            b = false;
        }

        if (date.getText().toString().equals("")) {

            b = false;
        }
        if (selectedCheckboxes.size() == 0) {
            b = false;
        }

        return b;
    }

    private void CreateTeacherNote() {
        selectedclasssection = selectedCheckboxes.toString();
        progressBar.setVisibility(View.VISIBLE);
        final String subid = subject_id;
        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        desc = et_teachernoteDesc.getText().toString();
        thisDate = date.getText().toString();
        Log.e("createTech", "ValueUrl>>" + newUrl + "AdminApi/daily_notes");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "AdminApi/daily_notes",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("", "onResponse: " + response);
                        Log.e("createTech", "response>>" + response);
                        progressBar.setVisibility(View.GONE);
                        try {
                            if (response != null) {
                                Toast.makeText(CreateTeachernote.this, "Note Created Successfully!!!",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CreateTeachernote.this, TeachersNoteACtivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(CreateTeachernote.this, "Can't Create Note", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("createTech", "responseerrrojsom>>" + response);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
                Log.e("createTech", "responseerrrojsom>>" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                final HashMap<String, String> params = new HashMap<>();
                //new
                params.put("random_no", randomId);
                params.put("short_name", name);
                params.put("academic_yr", academic_yr);
                params.put("teacher_id", reg_id);
                params.put("str_array", selectedclasssection);

                if (subid == null) {
                    params.put("subject_id", "0");
                } else {
                    params.put("subject_id", subject_id);
                }

                params.put("description", desc);
                params.put("login_type", "T");
                params.put("publish", "N");
                params.put("dailynote_date", thisDate);
                params.put("operation", "create");

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
                } else if (meventImagesArrayListPDF.size() > 0) {
                    for (int i = 0; i < meventImagesArrayListPDF.size(); i++) {

                        String base = '"' + meventImagesArrayListPDF.get(i).getPdfString() + '"';
                        String fname = '"' + meventImagesArrayListPDF.get(i).getImgName() + '"';

                        attachmentsfile.add(base);
                        attachmentsfile1.add(fname);
                    }
                } else if (meventImagesArrayList.size() > 0) {
                    for (int i = 0; i < meventImagesArrayList.size(); i++) {

                        String base = '"' + meventImagesArrayList.get(i).getFileBase64Code() + '"';
                        String fname = '"' + meventImagesArrayList.get(i).getImgName() + '"';

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
                System.out.println("CREATE_NOTE_PARAMS" + params);

                Log.e("createTech", "params>>" + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        Intent in = new Intent(CreateTeachernote.this, TeachersNoteACtivity.class);
        startActivity(in);
        in.putExtra("reg_id", reg_id);
        in.putExtra("academic_yr", academic_yr);
        CreateTeachernote.this.finish();
    }

    private void getSubjectName(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);
                listSubject.add(json.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        subjectSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.mytextview, listSubject));
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    // Get Path of selected image
    private String getPathnew(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @Override
    public void onClick(View v) {

        if (v == selectFile) {
           /* Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);*/
            // showFileChooser();

        } else if (v == saveTeachernote) {
            if (validate()) {
                CreateTeacherNote();
            } else {
                dialogBox();
            }

        } else if (v == resetTeachernote) {
            reset();

        } else if (v == backTeachernote) {
            onBackPressed();
        }
    }

    private void dialogBox() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateTeachernote.this);
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

    public void reset() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);

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
        String classarrayfinal;
        progressBar.setVisibility(View.VISIBLE);

        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        // TODO:Check for null values of classArray...
        String classArray = selectedCheckboxes.toString();
        classarrayfinal = classArray.replace(" ", "");

        HashMap<String, String> params = new HashMap<>();
        params.put("academic_yr", academic_yr);
        params.put("reg_id", reg_id);
        params.put("str_array", classarrayfinal);
        params.put("short_name", name);

        Log.i("TN", "getSubjects: " + params);
        System.out.println(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" +
                "get_subject_alloted_to_teacher_by_multiple_class", new JSONObject(params),
                new Response.Listener<JSONObject>() {
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
                                Toast.makeText(CreateTeachernote.this, "No Record Found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        subjectSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.mytextview,
                                listSubject));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

}


