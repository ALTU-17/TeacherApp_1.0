package in.aceventura.evolvuschool.teacherapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import in.aceventura.evolvuschool.teacherapp.Config;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImages;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImagesEdit;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImagesPdfEdit;
import in.aceventura.evolvuschool.teacherapp.utils.PermissionUtils;

public class EditRemarkActivity extends AppCompatActivity implements View.OnClickListener {

    public static int REQUEST_CODE_GET_CAMERA_FILE_PATH = 1888, REQUEST_CODE_GET_FILE_PATH = 1;
    public static String fileBase64Code, fileName;
    static Uri uri;
    final Random rand = new Random();
    public String atchPDF, atchIMG, fName;
    ProgressBar progressBar;
    ArrayList<String> deletefile = new ArrayList<>();
    String section_id;
    String prourl;
    Uri selectedIDImage, selectedPDF;
    String PDFpath, reg_id, academic_yr;
    File pdfFile;
    DownloadManager dm;
    LinearLayout micici_ListViewPdf, micici_listViewpdf1;
    ArrayList<String> attachmentsFile = new ArrayList<>();
    ArrayList<String> attachmentsFile1 = new ArrayList<>();
    String displayName = null;
    String strFile = null;
    ArrayList<EventImages> mEventImagesArrayList;
    ArrayList<EventImages> mEventImagesArrayList1;
    ArrayList<EventImages> mEventImagesArrayListDelete;
    ArrayList<EventImagesEdit> mEventImagesArrayListEdit;
    ArrayList<EventImagesPdfEdit> mEventImagesArrayListPDFEdit;
    TextView urltxt;
    EventImages eventImages;
    EventImagesEdit eventImagesEdit;
    EventImagesPdfEdit eventImagesPdfEdit;
    Button btnviewattchment, selectimage1;
    LinearLayout linearLayoutListView, linearLayoutListView1;
    TextView txtPdfName;
    int diceRoll;
    Uri outputFileUri, filePathPdf;
    Bitmap bitmap;
    Context context;
    String name, dUrl, newUrl, rmkid, acdyr, teacherid, sectionid, secname, classid, subid, student_id, remark_desc, remark_subject, classnm, fname, mnmae, lname, subjectnm;
    DatabaseHelper mDatabaseHelper;
    private int PICK_PDF_REQUEST = 12;
    private EditText edtSUbofRmk, Desc;
    private EditText txtclass, txtsubject, txtstudent;
    private ProgressDialog progressDialog;
    long totalMb = 0;
    CheckBox chkEditObservation;
    LinearLayout selectimage;
    String valObservation, remark_type;
    String rem_date, remark_id;
    ArrayList rmkIdArray;
    String singleStudArray;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_remark);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getId();
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


        txtclass.setEnabled(false);
        txtsubject.setEnabled(false);
        txtstudent.setEnabled(false);
        progressDialog = new ProgressDialog(this);

        rmkIdArray = new ArrayList<>();
        Intent in = getIntent();
        remark_id = in.getStringExtra("rmkid");
        rmkid = '"' + remark_id + '"';//["rmkid"]
        rmkIdArray.add(rmkid);
        singleStudArray = String.valueOf(rmkIdArray);

        acdyr = in.getStringExtra("acdyr");
        teacherid = in.getStringExtra("teacherid");
        sectionid = in.getStringExtra("sectionid");
        secname = in.getStringExtra("sectionname");
        classid = in.getStringExtra("classid");
        subid = in.getStringExtra("subject_id");
        student_id = in.getStringExtra("student_id");
        remark_desc = in.getStringExtra("remark_desc");
        remark_subject = in.getStringExtra("remark_subject");
        fname = in.getStringExtra("studfnm");
        mnmae = in.getStringExtra("studmnm");
        lname = in.getStringExtra("studlnm");
        subjectnm = in.getStringExtra("subnm");
        classnm = in.getStringExtra("classnm");
        rem_date = in.getStringExtra("rem_date");//dd mm yyyy
        System.out.println("DATE" + rem_date);

        // TODO: Checking Observation CheckBox from intent
        remark_type = in.getStringExtra("remark_type");
        selectimage.setVisibility(View.VISIBLE);

        if (remark_type.equals("Observation")) {
            selectimage.setVisibility(View.GONE);
            chkEditObservation.setChecked(true);
            valObservation = "Observation";
        } else {
            chkEditObservation.setChecked(false);
            valObservation = "Remark";
            selectimage.setVisibility(View.VISIBLE);

        }

        // TODO: when click on Observation CheckBox , attachment option visibility
        chkEditObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkEditObservation.isChecked()) {
                    valObservation = "Observation";
                    selectimage.setVisibility(View.GONE);
                } else {
                    valObservation = "Remark";
                    selectimage.setVisibility(View.VISIBLE);
                }
            }
        });


        if (subjectnm.equals("null")) {
            txtsubject.setText("No Subject");
        } else {
            txtsubject.setText(subjectnm);

        }

        txtclass.setText(classnm + secname);

        if (mnmae.equals("")) {
            txtstudent.setText(fname + " " + lname);
        } else if (lname.equals("") && mnmae.equals("")) {
            txtstudent.setText(fname);
        } else {
            txtstudent.setText(fname + " " + mnmae + " " + lname);
        }


        edtSUbofRmk.setText(remark_subject);
        Desc.setText(remark_desc);

//------------------------------------ Attachments -------------------------------------------------
        checkAttachments();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        mEventImagesArrayListPDFEdit = new ArrayList<>();
        mEventImagesArrayList = new ArrayList<>();
        mEventImagesArrayList1 = new ArrayList<>();
        mEventImagesArrayListEdit = new ArrayList<>();
        mEventImagesArrayListDelete = new ArrayList<>();
        micici_ListViewPdf = findViewById(R.id.micici_listViewpdf);
        micici_listViewpdf1 = findViewById(R.id.micici_listViewpdf1);

        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtils.checkPermission(EditRemarkActivity.this)) {
                    selectImage(savedInstanceState);
                }

            }
        });
    } //OnCreate

    @SuppressLint("SimpleDateFormat")
    private void checkAttachments() {

        HashMap<String, String> params = new HashMap<>();
        params.put("remark_id", remark_id);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
        Date newDate = null;
        try {
            newDate = spf.parse(rem_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("yyyy-MM-dd");

        params.put("remark_date", spf.format(newDate)); //yyyy-MM-dd

        params.put("short_name", name);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/get_images_remark", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("EditRemAttachments" + response.toString());
                try {
                    if (response.getString("status").equals("true")) {
                        mEventImagesArrayListPDFEdit.clear();
                        mEventImagesArrayList.clear();

                        String url = response.getString("url");
                        JSONArray jsonArray = response.getJSONArray("images");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            System.out.println("1" + jsonObject);

                            String imagename = jsonObject.getString("image_name");
                            boolean isFound = imagename.contains(".pdf");
                            boolean isFound1 = imagename.contains(".docx");
                            boolean isFound2 = imagename.contains(".doc");
                            boolean isFound3 = imagename.contains(".xls");
                            boolean isFound4 = imagename.contains(".xlsx");
                            boolean isFound5 = imagename.contains(".txt");
                            boolean isFound6 = imagename.contains(".PDF");

                            //--------------Files----------------------

                            if (isFound || isFound1 || isFound2 || isFound3 || isFound4 || isFound5 || isFound6) {
                                EventImages eventImages = new EventImages();
                                eventImages.setImgName(imagename);
                                mEventImagesArrayList.add(eventImages);
                                urltxt.setText(url);
                                updateListPdf();

                            } else {
                                EventImages eventImages = new EventImages();
                                eventImages.setImgName(imagename);
                                mEventImagesArrayList1.add(eventImages);
                                urltxt.setText(url);
                                updateListUrlImage();
                            }
                        }
                    } else {
                        System.out.println(response);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("EditRemark_Error", "", error);
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.getCache().clear(); // clearing the cache
        requestQueue1.add(jsonObjectRequest);
    }

//-------------------------------------Attachments--------------------------------------------------

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
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //for Files chooser
    private void showPDFChooser() {
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST);
    }

    //From Gallery chooser
    protected void showFileChooser1() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent = Intent.createChooser(intent, "Choose a file");
        startActivityForResult(intent,
                REQUEST_CODE_GET_FILE_PATH);
    }

    //From Camera chooser
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


    @SuppressLint({"SourceLockedOrientationActivity", "SwitchIntDef"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //gallery images
        if (requestCode == REQUEST_CODE_GET_FILE_PATH
                && resultCode == Activity.RESULT_OK) {

            if (data.getData() != null) {
                uri = data.getData();
                InputStream stream;
                try {
                    stream = this.getContentResolver().openInputStream(data.getData());
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
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream1);

                    byte[] byte_arr = stream1.toByteArray();
                    File tmpDir = new File(Environment
                            .getExternalStorageDirectory().getAbsolutePath()
                            + "/SchoolApp/");
                    tmpDir.mkdir();

                    fileBase64Code = Base64.encodeToString(byte_arr,
                            Base64.DEFAULT);

                    Log.d("EditRemarkActivity", "onActivityResult: " + tmpDir.getAbsolutePath());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //duplicate check

                final File f = new File(uri.getPath());
                fileName = f.getName() + ".jpg";
                HashMap<String, String> params = new HashMap<>();
                params.put("remark_id", remark_id);
                params.put("remark_date", rem_date);
                params.put("short_name", name);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/get_images_remark", new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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

                                /*eventImagesEdit = new EventImagesEdit();
                                eventImagesEdit.setImgName(fileName);
                                eventImagesEdit.setFileBase64Code(fileBase64Code);
                                eventImagesEdit.setExtension(":image/jpg;base64");
                                meventImagesArrayListEdit.add(eventImagesEdit);
                                updateListImagesFromGallery();*/

                                //--------------------------upload attachment method----------------

                                progressDialog.show();
                                progressDialog.setMessage("Please wait...");
                                HashMap<String, String> params = new HashMap<>();
                                params.put("upload_date", rem_date);
                                params.put("short_name", name);
                                params.put("doc_type_folder", "remark");
                                params.put("filename", fileName);
                                params.put("datafile", fileBase64Code);
                                params.put("student_id", singleStudArray);//["rmkid"]
                                System.out.println("RESMJ1"+params);


                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_remark_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println("3" + response);
                                        try {
                                            if (response.getString("status").equals("true")) {
                                                eventImagesEdit = new EventImagesEdit();
                                                eventImagesEdit.setImgName(fileName);
                                                eventImagesEdit.setFileBase64Code(fileBase64Code);
                                                eventImagesEdit.setExtension(":image/jpg;base64");
                                                mEventImagesArrayListEdit.add(eventImagesEdit);
                                                updateListImagesFromGallery();
                                                progressDialog.dismiss();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            progressDialog.dismiss();
                                            Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                        Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                                RequestQueue requestQueue1 = Volley.newRequestQueue(EditRemarkActivity.this);
                                requestQueue1.add(jsonObjectRequest);

                                //------------------------------------------------------------------------------


                            }
                            else {
                                fileName = f.getName() + ".jpg";

                                //--------------------------upload attachment method----------------------------

                                progressDialog.show();
                                progressDialog.setMessage("Please wait...");
                                HashMap<String, String> params = new HashMap<>();
                                params.put("upload_date", rem_date);
                                params.put("short_name", name);
                                params.put("doc_type_folder", "remark");
                                params.put("filename", fileName);
                                params.put("datafile", fileBase64Code);
                                params.put("student_id", singleStudArray);//["rmkid"]
                                System.out.println("4" + params);
                                System.out.println("RESMJ"+params);

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_remark_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println("5" + response);
                                        try {
                                            if (response.getString("status").equals("true")) {
                                                eventImagesEdit = new EventImagesEdit();
                                                eventImagesEdit.setImgName(fileName);
                                                eventImagesEdit.setFileBase64Code(fileBase64Code);
                                                eventImagesEdit.setExtension(":image/jpg;base64");
                                                mEventImagesArrayListEdit.add(eventImagesEdit);
                                                updateListImagesFromGallery();
                                                progressDialog.dismiss();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            progressDialog.dismiss();
                                            Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                        Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                                RequestQueue requestQueue1 = Volley.newRequestQueue(EditRemarkActivity.this);
                                requestQueue1.add(jsonObjectRequest);

                                //------------------------------------------------------------------------------
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(this);
                requestQueue1.add(jsonObjectRequest);
            }
        }

        //camera
        else if (requestCode == REQUEST_CODE_GET_CAMERA_FILE_PATH && resultCode == Activity.RESULT_OK) {
            try {
                fileName = diceRoll + ".jpeg";
                File file = null;
                try {
                    file = new File(Environment.getExternalStorageDirectory(), "selectedPhoto" + diceRoll + ".jpeg");
                    fileName = file.getName();
                }
                catch (Exception e) {
                    //unable to create  temporary file
                    Toast.makeText(EditRemarkActivity.this, "Unable to create temporary file", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditRemarkActivity.this, "Unable to convert image to byte array", Toast.LENGTH_SHORT).show();
                }

                //--------------------------upload attachment method--------------------------------

                progressDialog.show();
                progressDialog.setMessage("Please wait...");
                HashMap<String, String> params = new HashMap<>();
                params.put("upload_date", rem_date);
                params.put("short_name", name);
                params.put("doc_type_folder", "remark");
                params.put("filename", fileName);
                params.put("datafile", fileBase64Code);
                params.put("student_id", singleStudArray);//["rmkid"]
                System.out.println("EDIT_REM_CAMERA"+params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_remark_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("7" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                progressDialog.dismiss();
                                eventImagesEdit = new EventImagesEdit();
                                eventImagesEdit.setImgName(fileName);
                                eventImagesEdit.setFileBase64Code(fileBase64Code);
                                eventImagesEdit.setExtension(":image/jpg;base64");
                                mEventImagesArrayListEdit.add(eventImagesEdit);
                                updateListImagesFromGallery();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(EditRemarkActivity.this,error.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));// TODO: 27-07-2020

                RequestQueue requestQueue1 = Volley.newRequestQueue(EditRemarkActivity.this);
                requestQueue1.getCache().clear();// TODO: 27-07-2020
                requestQueue1.add(jsonObjectRequest);

                //------------------------------------------------------------------------------

            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
            }
        }
        //files
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

                    if (mb > 25) {
                        Toast.makeText(this, "File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    } else if (totalMb > 25) {
                        Toast.makeText(this, "Total File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    } else {
                        //Checking for duplicate name
                        final String fName = pdfFile.getName();
                        HashMap<String, String> params = new HashMap<>();
                        params.put("remark_id", remark_id);
                        params.put("remark_date", rem_date);
                        params.put("short_name", name);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/get_images_remark", new JSONObject(params), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
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


                                        //--------------------------upload attachment method----------------------------

                                        progressDialog.show();
                                        progressDialog.setMessage("Please wait...");
                                        HashMap<String, String> params = new HashMap<>();
                                        params.put("upload_date", rem_date);
                                        params.put("short_name", name);
                                        params.put("doc_type_folder", "remark");
                                        params.put("filename", displayName);
                                        params.put("datafile", strFile);
                                        params.put("student_id", singleStudArray);//["rmkid"]

                                        System.out.println("8" + params);

                                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_remark_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                System.out.println("9" + response);
                                                try {
                                                    if (response.getString("status").equals("true")) {
                                                        progressDialog.dismiss();
                                                        eventImagesPdfEdit = new EventImagesPdfEdit();
                                                        eventImagesPdfEdit.setFileName(displayName);
                                                        eventImagesPdfEdit.setPdfString(strFile);
                                                        mEventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                                        updateListPdfView();

                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                                Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                        RequestQueue requestQueue1 = Volley.newRequestQueue(EditRemarkActivity.this);
                                        requestQueue1.add(jsonObjectRequest);
                                        //------------------------------------------------------------------------------

                                    } else {

                                        displayName = pdfFile.getName();
                                        convertFileToString(PDFpath);

                                        //--------------------------upload attachment method----------------------------

                                        progressDialog.show();
                                        progressDialog.setMessage("Please wait...");
                                        HashMap<String, String> params = new HashMap<>();
                                        params.put("upload_date", rem_date);
                                        params.put("short_name", name);
                                        params.put("doc_type_folder", "remark");
                                        params.put("filename", displayName);
                                        params.put("datafile", strFile);
//                                        params.put("student_id", rmkIdArray.toString());//["rmkid"]
                                        params.put("student_id", singleStudArray);//["rmkid"]
                                        System.out.println("10" + params);

                                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_remark_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                System.out.println("11" + response);
                                                try {
                                                    if (response.getString("status").equals("true")) {
                                                        progressDialog.dismiss();
                                                        eventImagesPdfEdit = new EventImagesPdfEdit();
                                                        eventImagesPdfEdit.setFileName(displayName);
                                                        eventImagesPdfEdit.setPdfString(strFile);
                                                        mEventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                                        updateListPdfView();

                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                                Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                        RequestQueue requestQueue1 = Volley.newRequestQueue(EditRemarkActivity.this);
                                        requestQueue1.add(jsonObjectRequest);
                                        //------------------------------------------------------------------------------
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
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
                        params.put("remark_id", remark_id);
                        params.put("remark_date", rem_date);
                        params.put("short_name", name);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/get_images_remark", new JSONObject(params), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    if (response.getString("status").equals("true")) {
                                        Toast.makeText(EditRemarkActivity.this, "" + response, Toast.LENGTH_SHORT).show();
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
                                        progressDialog.setMessage("Please wait...");
                                        HashMap<String, String> params = new HashMap<>();
                                        params.put("upload_date", rem_date);
                                        params.put("short_name", name);
                                        params.put("doc_type_folder", "remark");
                                        params.put("filename", displayName);
                                        params.put("datafile", strFile);
//                                        params.put("student_id", rmkIdArray.toString());//["rmkid"]
                                        params.put("student_id", singleStudArray);//["rmkid"]
                                        System.out.println("12" + params);

                                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_remark_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                System.out.println("13" + response);
                                                try {
                                                    if (response.getString("status").equals("true")) {
                                                        progressDialog.dismiss();
                                                        eventImagesPdfEdit = new EventImagesPdfEdit();
                                                        eventImagesPdfEdit.setFileName(displayName);
                                                        eventImagesPdfEdit.setPdfString(strFile);
                                                        mEventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                                        updateListPdfView();

                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                                Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                        RequestQueue requestQueue1 = Volley.newRequestQueue(EditRemarkActivity.this);
                                        requestQueue1.add(jsonObjectRequest);
                                        //-------------------------------------------------------------------------------------------------

                                    } else {
                                        displayName = pdfFile.getName();
                                        convertFileToString(PDFpath);

                                        /*eventImagesPdfEdit = new EventImagesPdfEdit();
                                        eventImagesPdfEdit.setFileName(displayName);
                                        eventImagesPdfEdit.setPdfString(strFile);
                                        meventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                        updateListPdfView();*/


                                        //--------------------------upload attachment method---------------------------------------------

                                        progressDialog.setMessage("Please wait...");
                                        progressDialog.show();
                                        HashMap<String, String> params = new HashMap<>();
                                        params.put("upload_date", rem_date);
                                        params.put("short_name", name);
                                        params.put("doc_type_folder", "remark");
                                        params.put("filename", displayName);
                                        params.put("datafile", strFile);
//                                        params.put("student_id", rmkIdArray.toString());//["rmkid"]
                                        params.put("student_id", singleStudArray);//["rmkid"]

                                        System.out.println("14" + params);

                                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/upload_remark_files", new JSONObject(params), new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                System.out.println("15" + response);
                                                try {
                                                    if (response.getString("status").equals("true")) {
                                                        progressDialog.dismiss();
                                                        eventImagesPdfEdit = new EventImagesPdfEdit();
                                                        eventImagesPdfEdit.setFileName(displayName);
                                                        eventImagesPdfEdit.setPdfString(strFile);
                                                        mEventImagesArrayListPDFEdit.add(eventImagesPdfEdit);
                                                        updateListPdfView();
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println("ERROR_RESPONSE - " + error.getMessage());
                                                Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                        RequestQueue requestQueue1 = Volley.newRequestQueue(EditRemarkActivity.this);
                                        requestQueue1.add(jsonObjectRequest);
                                        //-------------------------------------------------------------------------------------------------

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();

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

    //Showing Images From Response
    private void updateListUrlImage() {
        linearLayoutListView.removeAllViews();
        if (mEventImagesArrayList1.size() > 0) {
            for (int i = 0; i < mEventImagesArrayList1.size(); i++) {
                setViewImageUrl(i);
            }
        }
    }

    //Selecting Images from gallery
    private void updateListImagesFromGallery() {
        linearLayoutListView1.removeAllViews();
        if (mEventImagesArrayListEdit.size() > 0) {
            for (int j = 0; j < mEventImagesArrayListEdit.size(); j++) {
                setViewGallery(j);
            }
        }
    }

    //Selecting Images from gallery

    private void setViewGallery(final int j) {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.custom_horizontal_imageview,
                null);

        final TextView imageName = baseView.findViewById(R.id.imageName);
        final Button downloadpdf = baseView.findViewById(R.id.downloadpdf);
        final String file_name = mEventImagesArrayListEdit.get(j).getImgName();
        imageName.setVisibility(View.VISIBLE);
        imageName.setText(file_name);

        final ImageView imgcancel = baseView.findViewById(R.id.imgcancel);

        final String urlmy = urltxt.getText().toString();

        imageName.setVisibility(View.VISIBLE);
        imageName.setText(mEventImagesArrayListEdit.get(j).getImgName());

        imageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditRemarkActivity.this, ViewFullSizeImage.class);
                intent.putExtra("url", urlmy + "/" + rem_date + "/" + file_name);
                startActivity(intent);
            }
        });

        downloadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditRemarkActivity.this, "Downloading to Download Folder " + file_name, Toast.LENGTH_LONG).show();
                downloadRemark(file_name);
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
                params.put("upload_date", rem_date);
                params.put("short_name", name);
                params.put("doc_type_folder", "remark");
                params.put("filename", mEventImagesArrayListEdit.get(j).getImgName());
//                params.put("student_id", rmkIdArray.toString());//["rmkid"]
                params.put("student_id", singleStudArray);//["rmkid"]
                System.out.println("16" + params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/delete_uploaded_remark_files", new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("DELETEREMARK" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                progressDialog.dismiss();
                                mEventImagesArrayListEdit.remove(j);
                                updateListImagesFromGallery();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(EditRemarkActivity.this);
                requestQueue1.add(jsonObjectRequest);

            }
        });
        linearLayoutListView1.addView(baseView);
    }

    //Showing Images From Response
    private void setViewImageUrl(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseView = layoutInflater.inflate(R.layout.custom_horizontal_image,
                null);

        final TextView imageName = baseView.findViewById(R.id.imageName);
        final ImageView imgcancel = baseView.findViewById(R.id.imgcancel);
        final Button downloadpdf = baseView.findViewById(R.id.downloadpdf);

        final String name = mEventImagesArrayList1.get(i).getImgName();
        imageName.setVisibility(View.VISIBLE);
        imageName.setText(name);

        downloadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadRemark(name);
                Toast.makeText(EditRemarkActivity.this, "Downloading to Download Folder  " + name, Toast.LENGTH_LONG).show();
            }
        });

        String urlmy = urltxt.getText().toString();

        final String loadurl = urlmy + "/" + name;

        imageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditRemarkActivity.this, ViewFullSizeImage.class);
                intent.putExtra("url", loadurl);
                startActivity(intent);
            }
        });

        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventImages deleted_image = mEventImagesArrayList1.get(i);
                mEventImagesArrayListDelete.add(deleted_image);
                mEventImagesArrayList1.remove(i);
                updateListUrlImage();
            }
        });
        linearLayoutListView.addView(baseView);
    }

    //Showing attachments from Response
    private void updateListPdf() {
        micici_ListViewPdf.removeAllViews();
        if (mEventImagesArrayList.size() > 0) {
            for (int l = 0; l < mEventImagesArrayList.size(); l++) {
                setViewPdf(l);
            }
        }
    }

    //Selecting Files and view files
    private void updateListPdfView() {
        micici_listViewpdf1.removeAllViews();
        if (mEventImagesArrayListPDFEdit.size() > 0) {
            for (int l = 0; l < mEventImagesArrayListPDFEdit.size(); l++) {
                setViewPdfView(l);
            }
        }
    }

    //Showing attachments from response
    private void setViewPdf(final int l) {

        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("InflateParams") final View baseViewpdf = layoutInflater.inflate(R.layout.custom_horizontal_files_layout_view,
                null);

        txtPdfName = baseViewpdf.findViewById(R.id.txtimagenamedisplay);
        ImageView cancelimagepdf = baseViewpdf.findViewById(R.id.imgcancelpdf);
        Button downloadpdf = baseViewpdf.findViewById(R.id.downloadpdf);
        String filename = mEventImagesArrayList.get(l).getImgName();

        if (filename != null) {
            txtPdfName.setVisibility(View.VISIBLE);
            cancelimagepdf.setVisibility(View.VISIBLE);
            downloadpdf.setVisibility(View.VISIBLE);
            txtPdfName.setText(filename);
        } else {
            txtPdfName.setVisibility(View.GONE);
            cancelimagepdf.setVisibility(View.GONE);
            downloadpdf.setVisibility(View.GONE);
        }

        downloadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = mEventImagesArrayList.get(l).getImgName();
                downloadRemark(fName);
                Toast.makeText(EditRemarkActivity.this, "Downloading to Download Folder" + fName, Toast.LENGTH_LONG).show();
            }
        });

        cancelimagepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventImages eventImages = mEventImagesArrayList.get(l);
                mEventImagesArrayListDelete.add(eventImages);
                mEventImagesArrayList.remove(l);
                updateListPdf();
            }
        });

        micici_ListViewPdf.addView(baseViewpdf);
    }

    //Selecting Files and view files
    private void setViewPdfView(final int l) {

        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("InflateParams") final View baseViewpdf = layoutInflater.inflate(R.layout.custom_horizontal_files_layout_view,
                null);

        txtPdfName = baseViewpdf.findViewById(R.id.txtimagenamedisplay);
        ImageView cancelimagepdf = baseViewpdf.findViewById(R.id.imgcancelpdf);
        Button downloadpdf = baseViewpdf.findViewById(R.id.downloadpdf);
        final String file_name = mEventImagesArrayListPDFEdit.get(l).getFileName();

        if (file_name != null) {
            txtPdfName.setVisibility(View.VISIBLE);
            cancelimagepdf.setVisibility(View.VISIBLE);
            downloadpdf.setVisibility(View.GONE);
            txtPdfName.setText(file_name);
        } else {
            txtPdfName.setVisibility(View.GONE);
            cancelimagepdf.setVisibility(View.GONE);
            downloadpdf.setVisibility(View.GONE);
        }

        downloadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = mEventImagesArrayListPDFEdit.get(l).getFileName();
                downloadRemark(fName);
                Toast.makeText(EditRemarkActivity.this, "Downloading to Download Folder " + fName, Toast.LENGTH_LONG).show();
            }
        });

        cancelimagepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*meventImagesArrayListPDFEdit.remove(l);
                updateListPdfView();*/

                //Remove attachment api
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                HashMap<String, String> params = new HashMap<>();
                params.put("upload_date", rem_date);
                params.put("short_name", name);
                params.put("doc_type_folder", "remark");
                params.put("filename", mEventImagesArrayListPDFEdit.get(l).getFileName());
//                params.put("student_id", rmkIdArray.toString());//["rmkid"]
                params.put("student_id", singleStudArray);//["rmkid"]
                System.out.println("17" + params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/delete_uploaded_remark_files", new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("DELETEREMARK" + response);
                        try {
                            if (response.getString("status").equals("true")) {
                                progressDialog.dismiss();
                                mEventImagesArrayListPDFEdit.remove(l);
                                updateListPdfView();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR_RESPONSE - " + error.getMessage());
                        Toast.makeText(EditRemarkActivity.this, "Error..Please Try again...", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(EditRemarkActivity.this);
                requestQueue1.add(jsonObjectRequest);
            }
        });

        micici_listViewpdf1.addView(baseViewpdf);
    }

    @SuppressLint("SimpleDateFormat")
    private void downloadRemark(String fName) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
        Date newDate = null;
        try {
            newDate = spf.parse(rem_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("yyyy-MM-dd");

        if (isReadStorageAllowed()) {
            String downloadUrl;

            if (name.equals("SACS")) {
                downloadUrl = prourl + "uploads/remark/" + spf.format(newDate) + "/" + remark_id + "/" + fName; //yyyy-mm-dd
            } else {
                downloadUrl = prourl + "uploads/" + name + "/remark/" + spf.format(newDate) + "/" + remark_id + "/" + fName;//yyyy-mm-dd
            }

            dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(downloadUrl);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

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
            Toast.makeText(EditRemarkActivity.this, "To Download Notice Attachment Please Allow the Storage Permission", Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission
        int STORAGE_PERMISSION_CODE = 23;
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

//--------------------------------------------------------------------------------------------------


    private void getId() {
        linearLayoutListView = findViewById(R.id.viewAttachmentlistview);
        linearLayoutListView1 = findViewById(R.id.viewAttachmentlistview1);
        urltxt = findViewById(R.id.txturl12);
        edtSUbofRmk = findViewById(R.id.subrmk1);
        selectimage = findViewById(R.id.selectimage);
        txtclass = findViewById(R.id.txtselectclassremark1);
        txtsubject = findViewById(R.id.txtselectsubrmk1);
        txtstudent = findViewById(R.id.txtselectstudrmk1);
        Desc = findViewById(R.id.edtrmk1);
        Button btnreset = findViewById(R.id.btnReset);
        Button btncreate = findViewById(R.id.btnsavermk1);
        Button btnback = findViewById(R.id.btnbackrmk1);
        chkEditObservation = findViewById(R.id.chkEditObservation);

        btnback.setOnClickListener(this);
        btncreate.setOnClickListener(this);
        btnreset.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        reg_id = (SharedClass.getInstance(this).getRegId().toString());
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        Intent intent = new Intent(EditRemarkActivity.this, RemarksActivity.class);
        intent.putExtra("reg_id", reg_id);
        intent.putExtra("academic_yr", academic_yr);
        startActivity(intent);
        EditRemarkActivity.this.finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnsavermk1:
                if (validate()) {
                    updateRemark();
                }
                else {
                    dialogBox();
                }
                break;
            case R.id.btnReset:
                reset();
                break;
            case R.id.btnbackrmk1:
                Intent intent = new Intent(EditRemarkActivity.this, RemarksActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void dialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditRemarkActivity.this);
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
        if (Desc.getText().toString().equals("")) {
            b = false;
        }
        if (txtsubject.getText().toString().equals("")) {
            b = false;
        }
        return b;
    }

    private void updateRemark() {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
        Date newDate = null;
        try {
            newDate = spf.parse(rem_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("yyyy-MM-dd");


        progressDialog.show();
        progressDialog.setMessage("Updating Remark...");
        String desc = Desc.getText().toString();
        String sub = edtSUbofRmk.getText().toString();
        final String versionName = Config.LOCAL_ANDROID_VERSION; // new 18-02-2020

        HashMap<String, String> params = new HashMap<>();
        params.put("academic_yr", acdyr);
        params.put("teacher_id", teacherid);
        params.put("section_id", sectionid);
        params.put("class_id", classid);
        params.put("subject_id", subid);
        params.put("student_id", student_id);
        params.put("remark_id", remark_id);
        params.put("remark_desc", desc);
        params.put("remark_subject", sub);
        params.put("publish", "N");
        params.put("acknowledge", "N");
        params.put("remark_date", rem_date);//from createRemark=>dd-mm-yyyy
        params.put("login_type", "T");
        params.put("operation", "edit");
        params.put("short_name", name);
        params.put("remark_type", valObservation);
        params.put("app_version", versionName);// new 18-02-2020

//==========================================================================================
        if (mEventImagesArrayListPDFEdit.size() > 0 && mEventImagesArrayListEdit.size() > 0) {
            for (int i = 0; i < mEventImagesArrayListPDFEdit.size(); i++) {
                String base = '"' + mEventImagesArrayListPDFEdit.get(i).getPdfString() + '"';
                String fname = '"' + mEventImagesArrayListPDFEdit.get(i).getFileName() + '"';
                attachmentsFile.add(base);
                attachmentsFile1.add(fname);
            }
            for (int j = 0; j < mEventImagesArrayListEdit.size(); j++) {
                String base = '"' + mEventImagesArrayListEdit.get(j).getFileBase64Code() + '"';
                String iname = '"' + mEventImagesArrayListEdit.get(j).getImgName() + '"';
                attachmentsFile.add(base);
                attachmentsFile1.add(iname);
            }

//            atchPDF = attachmentsFile.toString();
//            fName = attachmentsFile1.toString();

        } else if (mEventImagesArrayListPDFEdit.size() > 0) {
            for (int i = 0; i < mEventImagesArrayListPDFEdit.size(); i++) {
                String base = '"' + mEventImagesArrayListPDFEdit.get(i).getPdfString() + '"';
                String fname = '"' + mEventImagesArrayListPDFEdit.get(i).getFileName() + '"';
                attachmentsFile.add(base);
                attachmentsFile1.add(fname);
            }
        } else if (mEventImagesArrayListEdit.size() > 0) {
            for (int i = 0; i < mEventImagesArrayListEdit.size(); i++) {
                String base = '"' + mEventImagesArrayListEdit.get(i).getFileBase64Code() + '"';
                String fname = '"' + mEventImagesArrayListEdit.get(i).getImgName() + '"';
                attachmentsFile.add(base);
                attachmentsFile1.add(fname);
            }
        }

        /*if (attachmentsFile.size() == 0 && attachmentsFile1.size() == 0) {
            params.put("filename", "");
        } else {
            atchPDF = attachmentsFile.toString();
            fName = attachmentsFile1.toString();
            params.put("filename", fName);
        }*/

        if (attachmentsFile.size() > 0 && attachmentsFile1.size() > 0) {
            atchPDF = attachmentsFile.toString();
            fName = attachmentsFile1.toString();
            params.put("filename", fName);
        }

        if (mEventImagesArrayListDelete.size() > 0) {
            for (int i = 0; i < mEventImagesArrayListDelete.size(); i++) {
                String delete = '"' + mEventImagesArrayListDelete.get(i).getImgName() + '"';
                deletefile.add(delete);
                String dfile = deletefile.toString();
                params.put("deleteimagelist", dfile);
            }
        }

        System.out.println("EDIT_REMARK" + params);
      //==========================================================================================

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/remark", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("EDIT_REMARK_RESPONSE" + response.toString());
                try {
                    if (response.getString("status").equals("true")) {
                        progressDialog.dismiss();
                        if (valObservation.equals("Observation")) {
                            Toast.makeText(EditRemarkActivity.this, "Observation edited successfully.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditRemarkActivity.this, "Remark edited successfully.", Toast.LENGTH_LONG).show();
                        }
                        Intent intent = new Intent(EditRemarkActivity.this, RemarksActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        progressDialog.dismiss();
                        System.out.println("18" + response.toString());
                        if (valObservation.equals("Observation")) {
                            Toast.makeText(EditRemarkActivity.this, "Error while editing Observation", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditRemarkActivity.this, "Error while editing Remark", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    System.out.println("19" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                System.out.println("20" + error.getMessage());
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        requestQueue.add(jsonObjectRequest);

    }

    private void reset() {
        edtSUbofRmk.setText("");
        Desc.setText("");
        edtSUbofRmk.setText(remark_subject);
        Desc.setText(remark_desc);
    }
}
