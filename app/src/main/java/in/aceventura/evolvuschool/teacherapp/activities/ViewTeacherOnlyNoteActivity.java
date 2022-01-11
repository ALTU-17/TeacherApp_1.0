package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImages;


public class ViewTeacherOnlyNoteActivity extends AppCompatActivity {

    private TextView txtClassnm;
    private TextView txtSubnm;
    private TextView txtDate;
    private TextView txtDesc;
    private TextView txtsubn;
    private TextView imageName, colon, pDate;
    private int STORAGE_PERMISSION_CODE = 23;
    LinearLayout micici_listView, micici_listViewpdf;
    ArrayList<EventImages> meventImagesArrayList = new ArrayList<>();
    ArrayList<EventImages> meventImagesArrayListPDF = new ArrayList<>();
    private Button btnViewAttachment;
    private String sectionid;
    private String noteid;
    ImageLoader imageLoader;
    TextView urltxt, urltxt1;
    String dateImage, name, newUrl, dUrl;
    private ProgressDialog progressDialog;
    ProgressBar progressBar;
    DownloadManager dm;
    String prourl;
    DatabaseHelper mDatabaseHelper;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teacher_only_note);
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
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String classname = intent.getStringExtra("classname");
        String subjectname = intent.getStringExtra("subjectname");
        String sectionname = intent.getStringExtra("sectionname");
        String date = intent.getStringExtra("date");
        String pddate = intent.getStringExtra("publish_date");


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateImage = newformat.format(date1);
        String desc = intent.getStringExtra("desc1");
        sectionid = intent.getStringExtra("sectionid");
        noteid = intent.getStringExtra("noteid");
        meventImagesArrayList = new ArrayList<>();

        if (subjectname.equalsIgnoreCase("null")) {
            txtSubnm.setVisibility(View.GONE);
            txtsubn.setVisibility(View.GONE);
            colon.setVisibility(View.GONE);

        } else {
            txtSubnm.setText(subjectname);
        }
        txtClassnm.setText(classname + (sectionname));

        txtDate.setText(date);
        txtDesc.setText(desc);
        pDate.setText(pddate);


        if (imageLoader == null)

            getAttachments();

    }

    //ViewAttachments
    private void getAttachments() {
        meventImagesArrayListPDF.clear();
        meventImagesArrayList.clear();
        HashMap<String, String> params = new HashMap<>();
        params.put("note_id", noteid);
        params.put("dailynote_date", dateImage);
        params.put("section_id", sectionid);
        params.put("short_name", name);
        progressBar.setVisibility(View.VISIBLE);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "get_images_daily_notes", new JSONObject(params), new Response.Listener<JSONObject>() {
            //        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newdUrl+"AdminApi/" +"get_images_daily_notes", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressBar.setVisibility(View.GONE);
                try {
                    if (response.getString("status").equals("true")) {
                        String url = response.getString("url");
                        JSONArray jsonArray = response.getJSONArray("images");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
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
                                meventImagesArrayListPDF.add(eventImages);
                                urltxt.setText(url);
                                updateListPdf();
                            } else {
                                EventImages eventImages = new EventImages();
                                eventImages.setImgName(imagename);
                                meventImagesArrayList.add(eventImages);
                                urltxt.setText(url);
                                updateList();
                            }
                            //----------------------------------------
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewTeacherOnlyNoteActivity.this, ViewNotes.class);
        startActivity(intent);
        ViewTeacherOnlyNoteActivity.this.finish();
    }

    private void setView(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseView = layoutInflater.inflate(R.layout.custom_horizontalimage_view,
                null);

        final TextView imageName = baseView.findViewById(R.id.imageName);
        ImageView imgView = baseView.findViewById(R.id.imgView);
        String img_data = meventImagesArrayList.get(i).getFileBase64Code();
        String urlmy = urltxt.getText().toString();


        String imagename = meventImagesArrayList.get(i).getImgName();

        imageName.setVisibility(View.VISIBLE);
        imageName.setText(meventImagesArrayList.get(i).getImgName());
        final String loadurl = urlmy + "/"/*+dateImage+"/"*/ + imagename;

        imageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewTeacherOnlyNoteActivity.this, ViewFullSizeImage.class);
                intent.putExtra("url", loadurl);
                startActivity(intent);
            }
        });

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadNotice(imageName.getText().toString());
                Toast.makeText(ViewTeacherOnlyNoteActivity.this, "Downloading  " + imageName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        micici_listView.addView(baseView);
    }

    private void setViewPdf(final int m) {

        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseViewpdf = layoutInflater.inflate(R.layout.custom_horizontal_pdf_layout,
                null);

        final TextView txtpdfname = baseViewpdf.findViewById(R.id.txtimagenamedisplay);
        Button dwndimagepdf = baseViewpdf.findViewById(R.id.imgdwndpdf);

        if (txtpdfname != null) {
            txtpdfname.setVisibility(View.VISIBLE);
            txtpdfname.setText(meventImagesArrayListPDF.get(m).getImgName());
        } else {
            txtpdfname.setVisibility(View.GONE);
            txtpdfname.setText("");
        }
        txtpdfname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txtpdfname = baseViewpdf.findViewById(R.id.txtimagenamedisplay);
                String fName = txtpdfname.getText().toString();
                downloadNotice(fName);
                Toast.makeText(ViewTeacherOnlyNoteActivity.this, "Downloading to Downloads Folder " + fName, Toast.LENGTH_LONG).show();
            }
        });

        micici_listViewpdf.addView(baseViewpdf);
    }

    private void downloadNotice(String fName) {
        if (isReadStorageAllowed()) {
            String downloadUrl;

            if (name.equals("SACS")) {
                downloadUrl = prourl + "uploads/daily_notes/" + dateImage + "/" + noteid + "/" + fName;
//                downloadUrl = prourl + "uploads/daily_notes/" + dateImage + "/" + sectionid + "/" + fName;
            } else {
                downloadUrl = prourl + "uploads/" + name + "/daily_notes/" + dateImage + "/" + sectionid + "/" + fName;
                downloadUrl = prourl + "uploads/" + name + "/daily_notes/" + dateImage + "/" + noteid + "/" + fName;
            }

            dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(downloadUrl);
            DownloadManager.Request request = new DownloadManager.Request(uri);
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
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

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void updateList() {

        micici_listView.removeAllViews();
        if (meventImagesArrayList.size() > 0) {

            for (int i = 0; i < meventImagesArrayList.size(); i++) {
                setView(i);
            }
            btnViewAttachment.setVisibility(View.GONE);
        }
    }

    private void updateListPdf() {
        micici_listViewpdf.removeAllViews();
        if (meventImagesArrayListPDF.size() > 0) {

            for (int m = 0; m < meventImagesArrayListPDF.size(); m++) {
                setViewPdf(m);
            }
            btnViewAttachment.setVisibility(View.GONE);

        }

    }

    private void getId() {

        txtClassnm = findViewById(R.id.txtclassname);
        txtSubnm = findViewById(R.id.txtsubname);
        txtDate = findViewById(R.id.txtDate);
        txtDesc = findViewById(R.id.txtdesc);
        Button btnback = findViewById(R.id.btnback);
        txtsubn = findViewById(R.id.subntxt);
        micici_listView = findViewById(R.id.viewattchmentscrollview);
        micici_listViewpdf = findViewById(R.id.micici_listViewpdf);
        urltxt = findViewById(R.id.txturl1);
        urltxt1 = findViewById(R.id.txturl2);
        imageName = findViewById(R.id.imageName);
        colon = findViewById(R.id.colon);
        imgView = findViewById(R.id.imgView);
        pDate = findViewById(R.id.pDate);

        btnViewAttachment = findViewById(R.id.viewAttachment);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                Intent intent = new Intent(ViewTeacherOnlyNoteActivity.this, ViewNotes.class);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            Toast.makeText(ViewTeacherOnlyNoteActivity.this, "To Download Notice Attachment Please Allow the Storage Permission", Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                // Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();

            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops!! you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
