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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.pojo.DataSet;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImages;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImagesEdit;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImagesPdfEdit;
import in.aceventura.evolvuschool.teacherapp.utils.PermissionUtils;

public class EditHomeworkActivity extends AppCompatActivity implements View.OnClickListener {

    long totalMb = 0;
    public String atchPDF, fName;
    private static final int PICK_IMAGE_REQUEST = 1;
    public static int REQUEST_CODE_GET_CAMERA_FILE_PATH = 1888, REQUEST_CODE_GET_FILE_PATH = 1;
    public static String subid, name, newUrl, dUrl, fileBase64Code, fileName, desc, logintype, PDFpath, displayName;
    final Random rand = new Random();
    TextView urltxt;
    int diceRoll;
    File pdfFile;
    ArrayList<String> deletefile = new ArrayList<>();
    ArrayList<String> attachmentsfile = new ArrayList<>();
    ArrayList<String> attachmentsfile1 = new ArrayList<>();
    ArrayList<EventImages> meventImagesArrayList;
    ArrayList<EventImages> meventImagesArrayList1;
    ArrayList<EventImages> meventImagesArrayListDelete;
    ArrayList<EventImagesEdit> meventImagesArrayListEdit;
    ArrayList<EventImagesPdfEdit> meventImagesArrayListPDFEdit;
    EventImages eventImages;
    EventImagesEdit eventImagesEdit;
    EventImagesPdfEdit eventImagesPdfEdit;
    String strFile = null;
    Uri uri, outputFileUri, filePathpdf, selectedPDF;
    Bitmap bitmap;
    LinearLayout micici_listViewpdf, micici_listViewpdf1, linearLayoutlistview, linearLayoutlistview1;
    TextView txtpdfname;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    DatabaseHelper mDatabaseHelper;
    String classid, subjectid, sectionid, homeworkid, homeworkdesc, submitdate, reg_id, academic_yr, randomId;
    SharedClass sh;
    Calendar calendar;
    private final DatePickerDialog.OnDateSetListener startDate1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabe2();
        }
    };
    Context context;
    DownloadManager dm;
    private int PICK_PDF_REQUEST = 12;
    private Button btnback;
    private EditText edtdesc, edtdatehmk1;
    private EditText edtmy;
    private EditText edtmy1;
    private String startdate;
    private String acdyr;
    private String teacherid;
    private JSONArray result;
    private Spinner classSpinner, subjectSpinner;
    private ArrayList<String> listClass;
    private ArrayList<String> listSubject;
    private TextView datepick, selectimage;
    private List<DataSet> list = new ArrayList<>();
    private ListView listView;
    String hwEditedDate, prourl, section_id;
    ProgressBar progressBar;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_homework);
        getId();
        context = this;
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        calendar = Calendar.getInstance();

        Intent intent = getIntent();
        String classname = intent.getStringExtra("classname1");
        String subjectname = intent.getStringExtra("subjectname1");
        String sectionname = intent.getStringExtra("sectionname1");
        acdyr = intent.getStringExtra("academicyr");
        teacherid = intent.getStringExtra("teacherid");
        homeworkid = intent.getStringExtra("HOMEWORKID");
        classid = intent.getStringExtra("CLASSID");
        sectionid = intent.getStringExtra("SECTIONID");
        subjectid = intent.getStringExtra("SUBJECTID");
        homeworkdesc = intent.getStringExtra("HOMEWORKDESC");
        submitdate = intent.getStringExtra("SUBMITDATE");

        //hw created date
        startdate = intent.getStringExtra("startdate");
        hwEditedDate = startdate;//dd mm yyyy

        //Generating 4 digit randomId
        Random rand = new Random();
        randomId = String.format("%04d", rand.nextInt(10000));
        System.out.println("RANDOMID" + randomId);

        edtmy.setText(classname + " " + sectionname);
        edtmy.setEnabled(false);
        edtdatehmk1.setText(submitdate);
        edtdesc.setText(homeworkdesc);
        edtmy1.setText(subjectname);
        edtmy1.setEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        meventImagesArrayList = new ArrayList<>();
        meventImagesArrayList1 = new ArrayList<>();
        meventImagesArrayListEdit = new ArrayList<>();
        meventImagesArrayListDelete = new ArrayList<>();
        meventImagesArrayListPDFEdit = new ArrayList<>();

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

        //for checking attachments in Home
        checkAttachments();

        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtils.checkPermission(EditHomeworkActivity.this)) {
                    selectImage(savedInstanceState);
                }

            }
        });
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    @SuppressLint("SimpleDateFormat")
    private void checkAttachments() {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
        Date newDate = null;
        try {
            newDate = spf.parse(hwEditedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("yyyy-MM-dd");

        HashMap<String, String> params = new HashMap<>();
        params.put("homework_id", homeworkid);
        params.put("homework_date", spf.format(newDate));//yyyy-mm-dd
        params.put("short_name", name);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/get_images_homework", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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

                            } else {
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

    //    Showing images from response
    private void updateListUrlImage() {
        linearLayoutlistview.removeAllViews();
        if (meventImagesArrayList1.size() > 0) {

            for (int i = 0; i < meventImagesArrayList1.size(); i++) {
                setViewImageUrl(i);
            }
        }
    }

    //Showing Images from Response
    private void setViewImageUrl(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("InflateParams") final View baseView = layoutInflater.inflate(R.layout.custom_horizontal_image,
                null);

        final TextView imageName = baseView.findViewById(R.id.imageName);
        final ImageView imgcancel = baseView.findViewById(R.id.imgcancel);
        final Button downloadpdf = baseView.findViewById(R.id.downloadpdf);

        final String name = meventImagesArrayList1.get(i).getImgName();
        imageName.setVisibility(View.VISIBLE);
        imageName.setText(name);

        downloadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadHomework(name);
                Toast.makeText(EditHomeworkActivity.this, "Downloading  " + name, Toast.LENGTH_SHORT).show();
            }
        });

        String urlmy = urltxt.getText().toString();

        final String loadurl = urlmy + "/" + name;

        imageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditHomeworkActivity.this, ViewFullSizeImage.class);
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

    //File Chooser Options
    private void selectImage(final Bundle savedInstanceState) {

        final CharSequence[] options = {"Take Photo", "Choose Images", "Choose Files",
                "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(
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
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //Take pic from  camera
    protected void showCameraChooser(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            diceRoll = rand.nextInt(100) + 1;

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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

    //Choose images from gallery
    protected void showFileChooser1() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent = Intent.createChooser(intent, "Choose a file");
        startActivityForResult(intent,
                REQUEST_CODE_GET_FILE_PATH);
    }

    //choose documents only
    private void showPDFChooser() {
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST);
    }

    @SuppressLint({"SourceLockedOrientationActivity", "SwitchIntDef"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                    // photo = Bitmap.createScaledBitmap(photo, 300, 300, true);
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
                fileName = f.getName() + ".jpg";

                //Checking for duplicate name
                HashMap<String, String> params = new HashMap<>();
                params.put("homework_id", homeworkid);
                params.put("homework_date", hwEditedDate);
                params.put("short_name", name);
                progressBar.setVisibility(View.VISIBLE);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/get_images_homework", new JSONObject(params), new Response.Listener<JSONObject>() {
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

                                //--------------------------upload attachment method----------------------------

                                progressDialog.show();
                                HashMap<String, String> params = new HashMap<>();
                                params.put("upload_date", hwEditedDate);
                                params.put("random_no", homeworkid);
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
                                                eventImagesEdit = new EventImagesEdit();
                                                eventImagesEdit.setImgName(fileName);
                                                eventImagesEdit.setFileBase64Code(fileBase64Code);
                                                eventImagesEdit.setExtension(":image/jpg;base64");
                                                meventImagesArrayListEdit.add(eventImagesEdit);
                                                updateListImagesFromGallery();

                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            progressDialog.dismiss();
                                            Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                        Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                                RequestQueue requestQueue1 = Volley.newRequestQueue(EditHomeworkActivity.this);
                                requestQueue1.add(jsonObjectRequest);
                                //------------------------------------------------------------------------------

                            } else {
                                fileName = f.getName() + ".jpg";


                                //--------------------------upload attachment method----------------------------

                                progressDialog.show();
                                HashMap<String, String> params = new HashMap<>();
                                params.put("upload_date", hwEditedDate);
                                params.put("random_no", homeworkid);
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

                                                eventImagesEdit = new EventImagesEdit();
                                                eventImagesEdit.setImgName(fileName);
                                                eventImagesEdit.setFileBase64Code(fileBase64Code);
                                                eventImagesEdit.setExtension(":image/jpg;base64");
                                                meventImagesArrayListEdit.add(eventImagesEdit);
                                                updateListImagesFromGallery();

                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            progressDialog.dismiss();
                                            Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                        Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                                RequestQueue requestQueue1 = Volley.newRequestQueue(EditHomeworkActivity.this);
                                requestQueue1.add(jsonObjectRequest);
                                //------------------------------------------------------------------------------
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
        //Camera
        else if (requestCode == REQUEST_CODE_GET_CAMERA_FILE_PATH
                && resultCode == Activity.RESULT_OK) {

            //For camera image blur
            try {
                fileName = diceRoll + ".jpeg";
                File file = null;

                try {
                    file = new File(Environment.getExternalStorageDirectory(), "selectedPhoto" + diceRoll + ".jpeg");
                    fileName = file.getName();
                } catch (Exception e) {
                    //unable to create  temporary file
                    Toast.makeText(EditHomeworkActivity.this, "Unable to create temporary file", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditHomeworkActivity.this, "Unable to convert image to byte array", Toast.LENGTH_SHORT).show();
                }

                //--------------------------upload attachment method----------------------------

                progressDialog.show();

                HashMap<String, String> params = new HashMap<>();
                params.put("upload_date", hwEditedDate);
                params.put("random_no", homeworkid);
                params.put("short_name", name);
                params.put("doc_type_folder", "homework");
                params.put("filename", fileName);
                params.put("datafile", fileBase64Code);

                System.out.println("HW_CAMERA_PARAMS" + params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                eventImagesEdit = new EventImagesEdit();
                                eventImagesEdit.setImgName(fileName);
                                eventImagesEdit.setFileBase64Code(fileBase64Code);
                                eventImagesEdit.setExtension(":image/jpg;base64");
                                meventImagesArrayListEdit.add(eventImagesEdit);
                                updateListImagesFromGallery();
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(EditHomeworkActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));// TODO: 27-07-2020
                RequestQueue requestQueue1 = Volley.newRequestQueue(EditHomeworkActivity.this);
                requestQueue1.getCache().clear();// TODO: 27-07-2020
                requestQueue1.add(jsonObjectRequest);
                //------------------------------------------------------------------------------


            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }

        }
        //Files
        else if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePathpdf = data.getData();
            Uri uri = data.getData();
            String uriString = uri.toString();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
                        params.put("homework_id", homeworkid);
                        params.put("homework_date", hwEditedDate);
                        params.put("short_name", name);
                        progressBar.setVisibility(View.VISIBLE);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/get_images_homework", new JSONObject(params), new Response.Listener<JSONObject>() {
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
                                                convertFileToString(PDFpath);
                                                displayName = pdfFile.getName();
                                            }
                                        }
                                        convertFileToString(PDFpath);

                                        /*eventImagesPdfEdit = new EventImagesPdfEdit();
                                        eventImagesPdfEdit.setFileName(displayName);
                                        eventImagesPdfEdit.setPdfString(strFile);
                                        meventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                        updateListPdfView();*/
                                        //--------------------------upload attachment method----------------------------

                                        progressDialog.show();
                                        HashMap<String, String> params = new HashMap<>();
                                        params.put("upload_date", hwEditedDate);
                                        params.put("random_no", homeworkid);
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
                                                        eventImagesPdfEdit = new EventImagesPdfEdit();
                                                        eventImagesPdfEdit.setFileName(displayName);
                                                        eventImagesPdfEdit.setPdfString(strFile);
                                                        meventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                                        updateListPdfView();

                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                                Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                        RequestQueue requestQueue1 = Volley.newRequestQueue(EditHomeworkActivity.this);
                                        requestQueue1.add(jsonObjectRequest);
                                        //------------------------------------------------------------------------------

                                    } else {
                                        displayName = pdfFile.getName();
                                        convertFileToString(PDFpath);

                                        /*eventImagesPdfEdit = new EventImagesPdfEdit();
                                        eventImagesPdfEdit.setFileName(displayName);
                                        eventImagesPdfEdit.setPdfString(strFile);
                                        meventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                        updateListPdfView();*/

                                        //--------------------------upload attachment method----------------------------

                                        progressDialog.show();
                                        HashMap<String, String> params = new HashMap<>();
                                        params.put("upload_date", hwEditedDate);
                                        params.put("random_no", homeworkid);
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
                                                        eventImagesPdfEdit = new EventImagesPdfEdit();
                                                        eventImagesPdfEdit.setFileName(displayName);
                                                        eventImagesPdfEdit.setPdfString(strFile);
                                                        meventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                                        updateListPdfView();
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                                Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                        RequestQueue requestQueue1 = Volley.newRequestQueue(EditHomeworkActivity.this);
                                        requestQueue1.add(jsonObjectRequest);
                                        //------------------------------------------------------------------------------
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
                    totalMb = totalMb + mb; //Getting the TotalSize of files

                    if (mb > 25) {
                        Toast.makeText(this, "File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    } else if (totalMb > 25) {
                        Toast.makeText(this, "Total File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    } else {

                        //Checking for duplicate name
                        final String fName = pdfFile.getName();
                        HashMap<String, String> params = new HashMap<>();
                        params.put("homework_id", homeworkid);
                        params.put("homework_date", hwEditedDate);
                        params.put("short_name", name);
                        progressBar.setVisibility(View.VISIBLE);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "get_images_homework", new JSONObject(params), new Response.Listener<JSONObject>() {
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
                                                convertFileToString(PDFpath);
                                                displayName = pdfFile.getName();
                                            }

                                        }
                                        convertFileToString(PDFpath);

                                        /*eventImagesPdfEdit = new EventImagesPdfEdit();
                                        eventImagesPdfEdit.setFileName(displayName);
                                        eventImagesPdfEdit.setPdfString(strFile);
                                        meventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                        updateListPdfView();*/


                                        //--------------------------upload attachment method----------------------------

                                        progressDialog.show();

                                        HashMap<String, String> params = new HashMap<>();
                                        params.put("upload_date", hwEditedDate);
                                        params.put("random_no", homeworkid);
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

                                                        eventImagesPdfEdit = new EventImagesPdfEdit();
                                                        eventImagesPdfEdit.setFileName(displayName);
                                                        eventImagesPdfEdit.setPdfString(strFile);
                                                        meventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                                        updateListPdfView();
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                                Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                        RequestQueue requestQueue1 = Volley.newRequestQueue(EditHomeworkActivity.this);
                                        requestQueue1.add(jsonObjectRequest);
                                        //------------------------------------------------------------------------------
                                    } else {
                                        displayName = pdfFile.getName();
                                        convertFileToString(PDFpath);

                                        /*eventImagesPdfEdit = new EventImagesPdfEdit();
                                        eventImagesPdfEdit.setFileName(displayName);
                                        eventImagesPdfEdit.setPdfString(strFile);
                                        meventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                        updateListPdfView();*/

                                        //--------------------------upload attachment method----------------------------

                                        progressDialog.show();
                                        HashMap<String, String> params = new HashMap<>();
                                        params.put("upload_date", hwEditedDate);
                                        params.put("random_no", homeworkid);
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
                                                        eventImagesPdfEdit = new EventImagesPdfEdit();
                                                        eventImagesPdfEdit.setFileName(displayName);
                                                        eventImagesPdfEdit.setPdfString(strFile);
                                                        meventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                                        updateListPdfView();
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                                Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                        RequestQueue requestQueue1 = Volley.newRequestQueue(EditHomeworkActivity.this);
                                        requestQueue1.add(jsonObjectRequest);
                                        //------------------------------------------------------------------------------
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

    //Selecting Files Attachments
    private void updateListPdfView() {
        micici_listViewpdf1.removeAllViews();
        if (meventImagesArrayListPDFEdit.size() > 0) {
            for (int l = 0; l < meventImagesArrayListPDFEdit.size(); l++) {
                setViewPdfView(l);
            }
        }
    }

    //Selecting Files Attachments view
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
                downloadHomework(fName);
                Toast.makeText(EditHomeworkActivity.this, "Downloading  " + fName, Toast.LENGTH_SHORT).show();
            }
        });

        cancelimagepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meventImagesArrayListPDFEdit.remove(l);
                updateListPdfView();
            }
        });

        micici_listViewpdf1.addView(baseViewpdf);
    }

    //Showing  Images from Response
    private void setViewPdf(final int l) {

        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseViewpdf = layoutInflater.inflate(R.layout.custom_horizontal_files_layout_viewhomewoekedit,
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
                downloadHomework(fName);
                Toast.makeText(EditHomeworkActivity.this, "Downloading  " + fName, Toast.LENGTH_SHORT).show();
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

    //Showing Files From Response
    private void updateListPdf() {
        micici_listViewpdf.removeAllViews();
        if (meventImagesArrayList.size() > 0) {
            for (int l = 0; l < meventImagesArrayList.size(); l++) {
                setViewPdf(l);
            }
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

    //Showing Images from  Gallery & Camera
    private void updateListImagesFromGallery() {

        linearLayoutlistview1.removeAllViews();
        if (meventImagesArrayListEdit.size() > 0) {

            for (int j = 0; j < meventImagesArrayListEdit.size(); j++) {
                setViewGallery(j);
            }
        }
    }

    //Showing Images from  Gallery view
    private void setViewGallery(final int j) {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.custom_horizontal_homeworkedit,
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

        @SuppressLint("SimpleDateFormat") SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
        Date newDate = null;
        try {
            newDate = spf.parse(hwEditedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        spf = new SimpleDateFormat("yyyy-MM-dd");

        final SimpleDateFormat finalSpf = spf;
        final Date finalNewDate = newDate;
        imageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditHomeworkActivity.this, ViewFullSizeImage.class);
//                intent.putExtra("url", urlmy + "/" + hwEditedDate + "/" + imagename);
                intent.putExtra("url", urlmy + "/" + finalSpf.format(finalNewDate) + "/" + imagename);
                startActivity(intent);
            }
        });

        downloadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditHomeworkActivity.this, "Downloading  " + file_name, Toast.LENGTH_SHORT).show();
                downloadHomework(file_name);
            }
        });

        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*meventImagesArrayListEdit.remove(j);
                updateListImagesFromGallery();*/

                //Remove attachment api
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                HashMap<String, String> params = new HashMap<>();
                params.put("upload_date", hwEditedDate);
                params.put("random_no", homeworkid);
                params.put("short_name", name);
                params.put("doc_type_folder", "homework");
                params.put("filename", fileName);
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
                                Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(EditHomeworkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(EditHomeworkActivity.this);
                requestQueue1.add(jsonObjectRequest);

            }
        });

        linearLayoutlistview1.addView(baseView);
    }

    @SuppressLint("SimpleDateFormat")
    private void downloadHomework(String fName) {
        if (isReadStorageAllowed()) {
            String downloadUrl;
            @SuppressLint("SimpleDateFormat") SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
            Date newDate = null;
            try {
                newDate = spf.parse(hwEditedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spf = new SimpleDateFormat("yyyy-MM-dd");

            if (name.equals("SACS")) {
                downloadUrl = prourl + "uploads/homework/" + spf.format(newDate) + "/" + homeworkid + "/" + fName;
            } else {
                downloadUrl = prourl + "uploads/" + name + "/homework/" + spf.format(newDate) + "/" + homeworkid + "/" + fName;
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
            Toast.makeText(EditHomeworkActivity.this, "To Download Attachment Please Allow the Storage Permission", Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission
        int STORAGE_PERMISSION_CODE = 23;
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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

//==================================================================================================

    private void getId() {
        micici_listViewpdf = findViewById(R.id.micici_listViewpdf);
        Button btnupdate = findViewById(R.id.btnupdatehmk1);
        Button btnreset = findViewById(R.id.btnResethmk1);
        linearLayoutlistview = findViewById(R.id.viewAttachmentlistview);
        linearLayoutlistview1 = findViewById(R.id.viewAttachmentlistview1);
        edtdesc = findViewById(R.id.edtdeschmk1);
        edtmy = findViewById(R.id.edthmkmy);
        edtmy1 = findViewById(R.id.edt1hmk1);
        datepick = findViewById(R.id.txtdatehmk);
        edtdatehmk1 = findViewById(R.id.edtdatehmk1);
        selectimage = findViewById(R.id.selectimage);
        micici_listViewpdf = findViewById(R.id.micici_listViewpdf);
        micici_listViewpdf1 = findViewById(R.id.micici_listViewpdf1);
        urltxt = findViewById(R.id.txturl12);

        edtdatehmk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datepick.setFocusable(false);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 1);

                //Set yesterday time milliseconds as date pickers minimum date
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditHomeworkActivity.this, startDate1,
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
        btnupdate.setOnClickListener(this);
        btnreset.setOnClickListener(this);
    }

    private void updateLabe2() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtdatehmk1.setText(sdf.format(calendar.getTimeInMillis()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnupdatehmk1:
                if (validate()) {
                    updateHomework();
                } else {

                    dialogBox();
                }
                break;
            case R.id.btnResethmk1:
                clear();
                break;

        }
    }

    private void dialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditHomeworkActivity.this);
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

    private boolean validate() {
        boolean b = true;
        if (edtdesc.getText().toString().equals("")) {
            b = false;
        }

        if (edtdatehmk1.getText().toString().equals("")) {

            b = false;
        }
        return b;
    }

    @Override
    public void onBackPressed() {
        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        Intent in = new Intent(EditHomeworkActivity.this, HomeworkActivity.class);
        startActivity(in);
        in.putExtra("reg_id", reg_id);
        in.putExtra("academic_yr", academic_yr);
        startActivity(in);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void clear() {
        edtdesc.setText("");
        edtdesc.setText(homeworkdesc);
        edtdatehmk1.setText("");
        edtdatehmk1.setText(submitdate);
    }

    private void updateHomework() {
        progressDialog.show();
        progressDialog.setMessage("Updating Homework...");
        desc = edtdesc.getText().toString();
        submitdate = edtdatehmk1.getText().toString();
        logintype = SharedClass.getInstance(this).loadSharedPreference_role_id();

        HashMap<String, String> params = new HashMap<>();
        params.put("academic_yr", acdyr);
        params.put("teacher_id", teacherid);
        params.put("section_id", sectionid);
        params.put("class_id", classid);
        params.put("sm_id", subjectid);
        params.put("description", desc);
        params.put("publish", "N");
        params.put("end_date", submitdate);
        params.put("start_date", hwEditedDate);//dd-mm-yyyy
        params.put("login_type", logintype);
        params.put("operation", "edit");
        params.put("homework_id", homeworkid);
        params.put("short_name", name);
        params.put("random_no", homeworkid);

        //==========================================================================================
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

//            atchPDF = attachmentsfile.toString();
//            fName = attachmentsfile1.toString();
            //params.put("filename", fName);

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
        System.out.println("HW_EDIT_PARAM" + params);

        //==========================================================================================

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/homework", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("true")) {
                        progressDialog.dismiss();
                        Toast.makeText(EditHomeworkActivity.this, "Homework updated successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EditHomeworkActivity.this, HomeworkActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        progressDialog.dismiss();
                        System.out.println(response.toString());
                        Toast.makeText(EditHomeworkActivity.this, response.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println(error.getMessage());
                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        requestQueue.add(jsonObjectRequest);
    }
}
