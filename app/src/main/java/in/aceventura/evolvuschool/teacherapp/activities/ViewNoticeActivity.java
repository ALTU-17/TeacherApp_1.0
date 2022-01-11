package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.pojo.AllImages;
import in.aceventura.evolvuschool.teacherapp.pojo.ViewNoticepojo;


public class ViewNoticeActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    TextView txtsub, txtdesc, txtstartdate, txtdate, txtenddate, txtstarttime, txtendtime, txtclassnm;
    ArrayList<ViewNoticepojo> viewNoticepojoArrayList;
    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearnotice;
    String id, type, imagename, classname, classid;
    Button back;
    String dest_file_path = "test.pdf";
    int downloadedSize = 0;
    String urlidwise,mainUrlDownload;
    int totalSize = 0;
    ProgressBar pb;
    Dialog dialog;
    private static final String TAG = ViewNoticeActivity.class.getSimpleName();
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    TextView cur_val;
    String txtimagename;
    // String url="http://192.168.0.32:8082/schoolv3/uploads/notice/";
    String url;
    float per = 0;
    ArrayList<AllImages> allImagesArrayList;
    private DownloadManager dm;
    TextView tv_loading, txturl;
    TextView txtimagenm, txt;
    LinearLayout linearLayout;
    File file;
    TextView attachment;
    String name,newUrl,dUrl,pUrl,prourl;
    DatabaseHelper mDatabaseHelper;

    public ViewNoticeActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notice);

//        Sname = (SharedClass.getInstance(this).getShortname());
//        newUrl=(SharedClass.getInstance(this).getUrl());
       pUrl=(SharedClass.getInstance(this).getdUrl());

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl=mDatabaseHelper.getURL(1);
        newUrl=mDatabaseHelper.getTURL(1);
        prourl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")){
            name = mDatabaseHelper.getName(1);
            dUrl=mDatabaseHelper.getURL(1);
            newUrl=mDatabaseHelper.getTURL(1);
            prourl = mDatabaseHelper.getPURL(1);

        }


        progressDialog = new ProgressDialog(this);
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        getIds();
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
  //      StrictMode.setThreadPolicy(policy);
        //url = url.replace(" ", "%20");

        viewNoticepojoArrayList = new ArrayList<>();
        Intent intent = getIntent();
        id = intent.getStringExtra("Noticeid");
        type = intent.getStringExtra("noticetype");
        imagename = intent.getStringExtra("imagename");
        classname = intent.getStringExtra("classname");
        classid = intent.getStringExtra("classid");

        txturl = findViewById(R.id.txturl);

        url = txturl.getText().toString();
        txtclassnm.setText(classname);

//        if (type.equalsIgnoreCase("event")){
//            linearnotice.setVisibility(View.GONE);
//        }
        /*if(imagename.equalsIgnoreCase("null")){
            linearnotice.setVisibility(View.GONE);

        }

        else {
            linearnotice.setVisibility(View.VISIBLE);
        }*/
        //  urlidwise=url+id+"/"+imagename;
/*
        if (type.equalsIgnoreCase("Event")) {
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.VISIBLE);
        }*/
//        if (type.equalsIgnoreCase("Notice")) {
//            linearLayout1.setVisibility(View.GONE);
//            linearLayout2.setVisibility(View.GONE);
//            linearLayout3.setVisibility(View.GONE);
//            linearLayout4.setVisibility(View.GONE);
//        }

        //   txtimagenm.setText(imagename);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getParticularNotice();
    }

    /**
     * Showing Dialog
     * */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                //Progress Dialog
                ProgressDialog pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    private void getIds() {
        linearLayout1 = findViewById(R.id.noticelayout1);
        linearLayout2 = findViewById(R.id.noticelayout2);
        linearLayout3 = findViewById(R.id.noticelayout3);
        linearLayout4 = findViewById(R.id.noticelayout4);
        txtsub = findViewById(R.id.txtsub);
        Button btndownload = findViewById(R.id.downlodpdf);
        linearnotice = findViewById(R.id.linernotice);
        attachment = findViewById(R.id.attachmentdownlod);

        linearLayout = findViewById(R.id.linearimages);
        txtimagenm = findViewById(R.id.imagenm);
        txtclassnm = findViewById(R.id.txtclassnmnotice);

        tv_loading = findViewById(R.id.tvload);
        txtdesc = findViewById(R.id.txtnoticesDesc);
        back = findViewById(R.id.btnback);
        txtstartdate = findViewById(R.id.txtnoticesstartdate);
        txtdate = findViewById(R.id.txtnoticedate);
        txtenddate = findViewById(R.id.txtnoticesenddate);
        txtstarttime = findViewById(R.id.txtnoticesstartTime);
        txtendtime = findViewById(R.id.txtnoticesendTime);
    }

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;

        //If permission is not granted returning false
    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            Toast.makeText(ViewNoticeActivity.this, "To Download Notice Attachment Please Allow the Storage Permission", Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission
        int STORAGE_PERMISSION_CODE = 23;
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(ViewNoticeActivity.this, NoticeActivity.class);
        intent.putExtra("classid", classid);

        startActivity(intent);
        finish();
        return true;
    }

    void setTextError(final String message, final int color) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setTextColor(color);
                tv_loading.setText(message);
            }
        });

    }

    void setText(final String txt) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setText(txt);
            }
        });

    }

    private void getParticularNotice() {
        final String reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        HashMap<String, String> params = new HashMap<>();
        params.put("notice_id", id);
        params.put("short_name", name);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl+"AdminApi/" + "get_notices_by_id", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("true")) {
                        url = response.getString("url");

                        JSONArray jsonArray = response.getJSONArray("notice_info");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String sub = jsonObject.getString("subject");
                            String desc = jsonObject.getString("notice_desc");
                            String notice_id = jsonObject.getString("notice_id");
                            String notice_date = jsonObject.getString("notice_date");
//                            String start_date = jsonObject.getString("start_date");
//                            String end_date = jsonObject.getString("end_date");
//                            String start_time = jsonObject.getString("start_time");
//                            String end_time = jsonObject.getString("end_time");

                            JSONArray jsonArray1 = jsonObject.getJSONArray("images");
                            allImagesArrayList = new ArrayList<>();

                                for (int j = 0; j < jsonArray1.length(); j++) {

                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                    String imagename = jsonObject1.getString("image_name");
                                    String noticeid = jsonObject1.getString("notice_id");

                                    AllImages allImages = new AllImages(noticeid, imagename);
                                    allImagesArrayList.add(allImages);

                                    linearLayout.removeAllViews();

//                                    for (int z = 0; z < allImagesArrayList.size(); z++) {
//                                        setView(z);
//                                    }
//                               /*
                                if (allImagesArrayList.size()==0) {
                                    linearnotice.setVisibility(View.GONE);
                                } else {
                                    linearnotice.setVisibility(View.VISIBLE);
                                    for (int z = 0; z < allImagesArrayList.size(); z++) {
                                        setView(z);
                                    }
                                }
//                                */

                            }

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date date1 = null;
                            Date date2 = null;
                            Date date3 = null;

                            try {
                                date1 = simpleDateFormat.parse(notice_date);
//                                date2 = simpleDateFormat.parse(start_date);
//                                date3 = simpleDateFormat.parse(end_date);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //HH:mm:ss.SSS'Z'
                            SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            String reqNoticedate = newformat.format(date1);
//                            String reqStartdate = newformat.format(date2);
//                            String reqEnddate = newformat.format(date3);
                            ViewNoticepojo viewNoticepojo = new ViewNoticepojo(notice_id, sub, desc, reqNoticedate, allImagesArrayList);

                            txtsub.setText(sub);

                            // txtimagenm.setText(allImagesArrayList.get(i).getImagename());
                            txtdate.setText(reqNoticedate);
                            txtdesc.setText(desc);
//                            txtstartdate.setText(reqStartdate + "  " + start_time);
//                            txtenddate.setText(reqEnddate + "  " + end_time);
                            txturl.setText(url);
                            // txtstarttime.setText(start_time);
                            //txtendtime.setText(end_time);


                            viewNoticepojoArrayList.add(viewNoticepojo);

                            // FancyToast.makeText(CreateTeachernote.this,"Hello World !",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //classSpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listClass));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(jsonObjectRequest);
    }

    private void setView(final int z) {

        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseView = layoutInflater.inflate(R.layout.custom_textview,
                null);
        final TextView txt = baseView.findViewById(R.id.txtimagedisplay);
        Button imageView = baseView.findViewById(R.id.downlodpdfmultiple);

//        LinearLayout linearLayoutdownload=baseView.findViewById(R.id.downlodpdfmultiplelayout);

        txtimagename = allImagesArrayList.get(z).getImagename();
        txt.setText(txtimagename);

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             requestStoragePermission();


                txtimagename = allImagesArrayList.get(z).getImagename();


                downloadNotice(txtimagename);
            }
        });
        linearLayout.addView(baseView);
    }

    private void downloadNotice(String txtimagename) {

        if (isReadStorageAllowed()) {
            String dowloadUrl;
            Toast.makeText(this, "Downloading file"+ txtimagename+ "to Download folder", Toast.LENGTH_LONG).show();

            if (name.equals("SACS")){
                dowloadUrl = prourl+"uploads/notice/"+id +"/"+ txtimagename;
            }
            else{
                dowloadUrl = prourl+"uploads/" +name +"/notice/"+id +"/"+ txtimagename;
            }

            dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(dowloadUrl);
            System.out.println(uri);
            DownloadManager.Request request = new DownloadManager.Request(uri);
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

            String folder = "Evolvuschool/Teacher";
            request.setDestinationInExternalPublicDir(folder,txtimagename);

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewNoticeActivity.this, NoticeActivity.class);
        intent.putExtra("classid", classid);
        startActivity(intent);
        ViewNoticeActivity.this.finish();
    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(ViewNoticeActivity.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }


        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
//                URL urlDownloadpath = new URL(f_url[0]);
                URL urlDownloadpath = new URL(dUrl+"");

                URLConnection connection = urlDownloadpath.openConnection();

                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();

                // input stream to read file - with 8k buffer
                // InputStream input = new BufferedInputStream(urlDownloadpath.openStream(), 8192);
                InputStream input = new BufferedInputStream(urlDownloadpath.openStream(), 8192);

               // InputStream input=connection.getInputStream();
               // DataInputStream input = new DataInputStream(urlDownloadpath.openStream());
                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
               fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1);
                //fileName = "testfile";

                //Append timestamp to file name

              // fileName = timestamp + "_" + fileName;

              fileName = "" + fileName;

                //External directory path to save file
             //   folder = Environment.getExternalStorageDirectory().getPath();
                 folder = Environment.getExternalStorageDirectory() + File.separator + "Teacherapp/";

                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte[] data = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Unable to download file";
        }
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }
        @Override
        protected void onPostExecute(String s) {
            this.progressDialog.dismiss();
            if(s.equals(fileName)){
                //Toast.makeText(getApplicationContext(), "File saved in "+folder +" folder", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewNoticeActivity.this);
                {
                    builder.setCancelable(true);
                    builder.setTitle("Success");
                  //  builder.setMessage("File saved in "+folder +" folder");
                   builder.setMessage("File saved in Internal storage/TeacherApp");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
                  builder.create().show();
                }
            }else{

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewNoticeActivity.this);
                {
                    builder.setCancelable(true);
                    builder.setTitle("Failed");
                    builder.setMessage("Unable to Download file");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
/*
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
*/
                    builder.create().show();

                }
            }
            }
    }

    private void dialogBoxSuccess(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewNoticeActivity.this);
        {
            builder.setCancelable(true);
            builder.setTitle("Success");
            builder.setMessage("File saved in \"+folder +\" folder");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
/*
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
*/
            builder.create().show();

        }
    }

}
