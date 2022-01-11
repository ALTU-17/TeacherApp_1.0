package in.aceventura.evolvuschool.teacherapp.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.activities.ViewFullSizeImage;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImages;

public class ViewRemarkAttachmentsActivity extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper;
    String name, dUrl, newUrl, prourl, description, rm_id, rem_date, sectionid, dateImage;
    private int STORAGE_PERMISSION_CODE = 23;
    LinearLayout micici_listView, micici_listViewpdf, remarks_attachments;
    ArrayList<EventImages> meventImagesArrayList = new ArrayList<>();
    ArrayList<EventImages> meventImagesArrayListPDF = new ArrayList<>();
    TextView urltxt, urltxt1, txtdesc;
    DownloadManager dm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_remark_attachments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        description = i.getStringExtra("description");
        rm_id = i.getStringExtra("rm_id");
        rem_date = i.getStringExtra("rem_date");


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(rem_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd");
        dateImage = newformat.format(date2);


        sectionid = i.getStringExtra("sectionid");

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

        urltxt = findViewById(R.id.txturl1);
        remarks_attachments = findViewById(R.id.remarks_attachments);
        urltxt1 = findViewById(R.id.txturl2);
        txtdesc = findViewById(R.id.txtdesc);
        txtdesc.setText(description);

        micici_listView = findViewById(R.id.viewattchmentscrollview);
        micici_listViewpdf = findViewById(R.id.micici_listViewpdf);
        meventImagesArrayList = new ArrayList<>();

        remarks_attachments.setVisibility(View.GONE);
        getAttachments(rm_id, rem_date);
    }

    private void getAttachments(String rm_id, String rem_date) {
        meventImagesArrayListPDF.clear();
        meventImagesArrayList.clear();
        HashMap<String, String> params = new HashMap<>();
        params.put("remark_id", rm_id);
        params.put("remark_date", rem_date);
        params.put("short_name", name);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "get_images_remark", new JSONObject(params), new Response.Listener<JSONObject>() {
            //        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newdUrl+"AdminApi/" +"get_images_daily_notes", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getString("status").equals("true")) {
                        remarks_attachments.setVisibility(View.VISIBLE);
                        String url = response.getString("url");
                        JSONArray jsonArray = response.getJSONArray("images");
                        Log.i("", "RemarkAttachments: " + jsonArray);
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                remarks_attachments.setVisibility(View.GONE);
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(jsonObjectRequest);
    }

    private void updateList() {

        micici_listView.removeAllViews();
        if (meventImagesArrayList.size() > 0) {

            for (int i = 0; i < meventImagesArrayList.size(); i++) {
                setView(i);
            }
        }
    }

    private void setView(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseView = layoutInflater.inflate(R.layout.custom_horizontalimage_view,
                null);

        final TextView imageName = baseView.findViewById(R.id.imageName);
        ImageView imgView = baseView.findViewById(R.id.imgView);
        ImageView viewimg = baseView.findViewById(R.id.imgView1);
        final String imagename = meventImagesArrayList.get(i).getImgName();
        final String loadurl;
        imageName.setVisibility(View.VISIBLE);
        imageName.setText(meventImagesArrayList.get(i).getImgName());


        if (name.equals("SACS")) {
            loadurl = prourl + "uploads/remark/" + dateImage + "/" + rm_id + "/" + imagename;
        } else {
            loadurl = prourl + "uploads/" + name + "/remark/" + dateImage + "/" + rm_id + "/" + imagename;
        }

        imageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewRemarkAttachmentsActivity.this, ViewFullSizeImage.class);
                intent.putExtra("url", loadurl);
                startActivity(intent);
            }
        });

        viewimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewRemarkAttachmentsActivity.this, ViewFullSizeImage.class);
                intent.putExtra("url", loadurl);
                startActivity(intent);
            }
        });

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadNotice(imageName.getText().toString());
                Toast.makeText(ViewRemarkAttachmentsActivity.this, "Downloading  " + imageName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        micici_listView.addView(baseView);
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
                Toast.makeText(ViewRemarkAttachmentsActivity.this, "Downloading to Download Folder  " + fName, Toast.LENGTH_LONG).show();
            }
        });

        micici_listViewpdf.addView(baseViewpdf);
    }

    private void downloadNotice(String fName) {
        if (isReadStorageAllowed()) {
            String downloadUrl;

            if (name.equals("SACS")) {
                downloadUrl = prourl + "uploads/remark/" + dateImage + "/" + rm_id + "/" + fName;

            } else {
                downloadUrl = prourl + "uploads/" + name + "/remark/" + dateImage + "/" + rm_id + "/" + fName;
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
            }else {

            }

            dm.enqueue(request);
            return;

        }
        requestStoragePermission();
    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            Toast.makeText(ViewRemarkAttachmentsActivity.this, "To Download Notice Attachment Please Allow the Storage Permission", Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;

        //If permission is not granted returning false
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
}
