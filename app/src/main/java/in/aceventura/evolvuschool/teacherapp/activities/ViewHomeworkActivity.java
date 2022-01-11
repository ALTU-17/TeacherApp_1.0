package in.aceventura.evolvuschool.teacherapp.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
import java.util.Map;
import java.util.Objects;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.adapter.ViewHmkAdapter;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImages;
import in.aceventura.evolvuschool.teacherapp.pojo.ViewHmkPojo;


public class ViewHomeworkActivity extends AppCompatActivity {

    public static ArrayList<ViewHmkPojo> viewHmkPojoArrayList;
    public static ArrayList<ViewHmkPojo> checkedArrayList;
    public static String selectedHomeworkStatus = "Assigned";
    DownloadManager dm;
    TextView urltxt, urltxt1;
    LinearLayout micici_listView, micici_listViewpdf, attachments;
    ArrayList<EventImages> meventImagesArrayList = new ArrayList<>();
    ArrayList<EventImages> meventImagesArrayListPDF = new ArrayList<>();
    ViewHmkPojo viewHmkPojo;
    LinearLayoutManager manager;
    Context context;
    ArrayList<String> hmkidArrayList = new ArrayList<>();
    Button btnUpdate;
    String operation = "HWStatus";
    RequestQueue requestQueue;
    String statushmk, newUrl, dUrl;
    ProgressDialog progressDialog;
    DatabaseHelper mDatabaseHelper;
    private TextView txtClassnm, txtSubnm, txtDate, txtDesc, txtenddate, txtstatusmenu1;
    private ViewHmkAdapter viewHmkAdapter;
    private RecyclerView recyclerView;
    private ArrayList<String> hwStatusList = new ArrayList<>();
    private ArrayList<String> statushmklist = new ArrayList<>();
    private String classname, subjectname, sectionname, date, submitdate, desc, name, hmkid, studid, teachercomment, hmkStatus, prourl, dateImage;

    public static String getSelectedHomeworkStatus() {
        return selectedHomeworkStatus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_homework);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getId();
        context = this;
        hwStatusList.add(operation);
        selectedHomeworkStatus = "Assigned";
        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);
        prourl = mDatabaseHelper.getPURL(1);
        meventImagesArrayList = new ArrayList<>();
        prourl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
            prourl = mDatabaseHelper.getPURL(1);
        }


        requestQueue = Volley.newRequestQueue(getBaseContext());
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        viewHmkPojoArrayList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        viewHmkAdapter = new ViewHmkAdapter(context, viewHmkPojoArrayList);
        recyclerView.setAdapter(viewHmkAdapter);

        Intent intent = getIntent();
        classname = intent.getStringExtra("classname");
        subjectname = intent.getStringExtra("subjectname");
        sectionname = intent.getStringExtra("sectionname");
        date = intent.getStringExtra("date");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd");
        dateImage = newformat.format(date2);

        desc = intent.getStringExtra("desc");
        hmkid = intent.getStringExtra("hmkid");
        submitdate = intent.getStringExtra("enddate");

        txtClassnm.setText(classname + (sectionname));
        txtSubnm.setText(subjectname);
        txtDate.setText(date);
        txtenddate.setText(submitdate);
        txtDesc.setText(desc);
        hmkidArrayList.add(hmkid);

        attachments.setVisibility(View.GONE);
        getAttachments();
        getViews();

    }

    private void getAttachments() {
        meventImagesArrayListPDF.clear();
        meventImagesArrayList.clear();
        HashMap<String, String> params = new HashMap<>();
        params.put("homework_id", hmkid);
        params.put("homework_date", date);
        params.put("short_name", name);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "get_images_homework", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("true")) {
                        attachments.setVisibility(View.VISIBLE);
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
        ImageView view = baseView.findViewById(R.id.imgView1);
        String img_data = meventImagesArrayList.get(i).getFileBase64Code();
        final String imagename = meventImagesArrayList.get(i).getImgName();
        final String loadurl;
        imageName.setVisibility(View.VISIBLE);
        imageName.setText(meventImagesArrayList.get(i).getImgName());

        String urlmy = urltxt.getText().toString();

        if (name.equals("SACS")) {
            loadurl = prourl + "uploads/" + name + "/homework/" + dateImage + "/" + hmkid + "/" + imagename;
        } else {
            loadurl = prourl + "uploads/" + name + "/homework/" + dateImage + "/" + hmkid + "/" + imagename;
        }

        //view img
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewHomeworkActivity.this, ViewFullSizeImage.class);
                intent.putExtra("url", loadurl);
                startActivity(intent);
            }
        });

        imageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewHomeworkActivity.this, ViewFullSizeImage.class);
                intent.putExtra("url", loadurl);
                startActivity(intent);
            }
        });


        //download img
        imgView.setOnClickListener(v -> {
            Toast.makeText(ViewHomeworkActivity.this, "Downloading  " + imageName.getText().toString(), Toast.LENGTH_SHORT).show();
            downloadHomework(imageName.getText().toString());
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
                downloadHomework(fName);
                Toast.makeText(ViewHomeworkActivity.this, "Downloading to Downloads Folder " + fName, Toast.LENGTH_SHORT).show();
            }
        });

        micici_listViewpdf.addView(baseViewpdf);
    }

    private void downloadHomework(String fName) {
        if (isReadStorageAllowed()) {
            String downloadUrl;

            if (name.equals("SACS")) {
                downloadUrl = prourl + "uploads/homework/" + dateImage + "/" + hmkid + "/" + fName;
            } else {
                downloadUrl = prourl + "uploads/" + name + "/homework/" + dateImage + "/" + hmkid + "/" + fName;
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

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(ViewHomeworkActivity.this, "To Download Attachments Please Allow the Storage Permission", Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission
        int STORAGE_PERMISSION_CODE = 23;
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

    //search student
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.contact, menu);

        //getting the search view from the menu
        MenuItem searchViewItem = menu.findItem(R.id.menuSearch);

        //getting search rvManager from systemservice
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        //getting the search view
        final SearchView searchView = (SearchView) searchViewItem.getActionView();

        //you can put a hint for the search input field
        searchView.setQueryHint("Search Student...");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //by setting it true we are making it iconified
        //so the search input will show up after taping the search iconified
        //if you want to make it visible all the time make it false
        searchView.setIconifiedByDefault(true);

        //here we will get the search query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //do the search here
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        return true;
    }

    private void filter(String s) {
        ArrayList<ViewHmkPojo> filtername = new ArrayList<>();
        for (ViewHmkPojo subjectnm : viewHmkPojoArrayList) {
            if (subjectnm.getStudFname().toLowerCase().contains(s.toLowerCase())) {
                filtername.add(subjectnm);
            }
        }
        viewHmkAdapter.filterlist(filtername);
    }

    //get_student_with_homework_status
    private void getViews() {
        {
            progressDialog.setMessage("Loading ...");
            progressDialog.show();
            final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
            final String reg_id = (SharedClass.getInstance(this).getRegId().toString());
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    newUrl + "AdminApi/get_student_with_homework_status",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressDialog.dismiss();
                            try {
                                if (response != null) {

                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("student_details");

                                    Log.d("StudentList", "onResponse: " + jsonArray);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String fname = jsonObject1.getString("first_name");
                                        String lname = jsonObject1.getString("last_name");
                                        String parentcommet = jsonObject1.getString("parent_comment");
                                        String hmkStatus = jsonObject1.getString("homework_status");
                                        String teachercomment = jsonObject1.getString("comment");
                                        studid = jsonObject1.getString("student_id");
                                        String hmkid = jsonObject1.getString("homework_id");

                                        if (parentcommet.equals("NULL") || parentcommet.equals("null")) {
                                            parentcommet = " ";
                                            ViewHmkPojo vpojo = new ViewHmkPojo(fname, lname, parentcommet, teachercomment, hmkStatus, studid, hmkid, false);
                                            viewHmkPojoArrayList.add(vpojo);
                                        } else {
                                            ViewHmkPojo vpojo = new ViewHmkPojo(fname, lname, parentcommet, teachercomment, hmkStatus, studid, hmkid, false);
                                            viewHmkPojoArrayList.add(vpojo);
                                        }
                                        viewHmkAdapter.notifyDataSetChanged();
                                    }

                                } else {
                                    Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                                    Log.d("ViewHomeworkActivity", "" + "No Data");
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
                            System.out.println("Response CallR_Post_api error.getMessage() -->" + error.getMessage());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    final HashMap<String, String> params = new HashMap<>();
                    params.put("homework_id", hmkid);
                    params.put("login_type", "T");
                    params.put("operation", "HWStatus");
                    params.put("teacher_id", reg_id);
                    params.put("academic_yr", academic_yr);
                    params.put("short_name", name);
                    return params;
                }

            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
    }

    private void getId() {
        micici_listView = findViewById(R.id.viewattchmentscrollview);
        micici_listViewpdf = findViewById(R.id.micici_listViewpdf);
        attachments = findViewById(R.id.attachments);

        urltxt = findViewById(R.id.txturl1);
        urltxt1 = findViewById(R.id.txturl2);
        recyclerView = findViewById(R.id.recyclerhmkview);
        txtClassnm = findViewById(R.id.txtclassnamehmk);
        txtSubnm = findViewById(R.id.txtsubnamehmk);
        txtDate = findViewById(R.id.txtDateassign);
        txtstatusmenu1 = findViewById(R.id.txtstatusmenu1);
        txtDesc = findViewById(R.id.txtdeschomwk);
        Button btnback = findViewById(R.id.btnbackhomework);
        txtenddate = findViewById(R.id.txtDatesubmithmk);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnback.setOnClickListener(view -> {
            onBackPressed();
            Intent intent = new Intent(ViewHomeworkActivity.this, HomeworkActivity.class);
            startActivity(intent);
            finish();

        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewHomeworkActivity.this);
                {
                    builder.setCancelable(true);
                    builder.setTitle("Alert");
                    builder.setMessage(
                            "Do you want to Update the Homework Status?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            update();

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }

                //updateviews();
            }
        });

        txtstatusmenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, txtstatusmenu1);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_family_relations, popup.getMenu());

                //registering popup with OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                txtstatusmenu1.setText("Assigned");
                                selectedHomeworkStatus = "Assigned";
                                for (int i = 0; i < viewHmkPojoArrayList.size(); i++) {
                                    ViewHmkPojo currentStudent;
                                    currentStudent = viewHmkPojoArrayList.get(i);
                                    if (currentStudent.getCheckedStatus()) {
                                        viewHmkPojoArrayList.get(i).setHomework_status("Assigned");
                                    }
                                }

                                break;
                            case R.id.menu2:
                                txtstatusmenu1.setText("Completed");
                                selectedHomeworkStatus = "Completed";
                                for (int i = 0; i < viewHmkPojoArrayList.size(); i++) {
                                    ViewHmkPojo currentStudent;
                                    currentStudent = viewHmkPojoArrayList.get(i);
                                    if (currentStudent.getCheckedStatus()) {
                                        viewHmkPojoArrayList.get(i).setHomework_status("Completed");

                                    }
                                }

                                break;
                            case R.id.menu3:
                                txtstatusmenu1.setText("Partial");
                                selectedHomeworkStatus = "Partial";
                                for (int i = 0; i < viewHmkPojoArrayList.size(); i++) {
                                    ViewHmkPojo currentStudent;
                                    currentStudent = viewHmkPojoArrayList.get(i);
                                    if (currentStudent.getCheckedStatus()) {
                                        viewHmkPojoArrayList.get(i).setHomework_status("Partial");

                                    }
                                }
                                break;
                        }
                        viewHmkAdapter.notifyDataSetChanged();
                        return false;
                    }
                });
                popup.show();
            }
        });

        statushmk = txtstatusmenu1.getText().toString();

        statushmklist.add(statushmk);
    }

    private void update() {
        JSONObject mainRequestObject = new JSONObject();
        JSONArray requestArray = new JSONArray();
        for (int i = 0; i < viewHmkPojoArrayList.size(); i++) {
            ViewHmkPojo currentStudent;
            currentStudent = viewHmkPojoArrayList.get(i);
            if (currentStudent.getCheckedStatus()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("homework_id", currentStudent.getHomework_id());
                    jsonObject.put("student_id", currentStudent.getStudId());
                    jsonObject.put("homework_status", currentStudent.getHomework_status());
                    jsonObject.put("teachercomment", currentStudent.getTeachercommet());
                    jsonObject.put("short_name", currentStudent.getTeachercommet());

//                    jsonObject.put("parentcomment", currentStudent.getParentcomment());
//                    jsonObject.put("studFname", currentStudent.getStudFname());
//                    jsonObject.put("studLname", currentStudent.getStudLname());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                requestArray.put(jsonObject);

                /*[{
                    "homework_id":"22",
                    "homework_status":"Assigned",
                    "student_id":"4502",
                    "teachercomment":"test comment"
                    }]*/
            }
        }
        try {
            mainRequestObject.put("arraylist", requestArray);
//            mainRequestObject.put("short_name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String stringTypeRequest = mainRequestObject.toString();
        System.out.println("student_array" + stringTypeRequest);

        if (requestArray.length() == 0) {
            Toast.makeText(context, "Please select at least one Student", Toast.LENGTH_SHORT).show();
        } else {
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest postRequest = new StringRequest(Request.Method.POST, newUrl + "AdminApi/updateHomework", response -> {
                if (response != null) {
                    System.out.println("STRING_RES" + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("true")) {
                            Toast.makeText(context,jsonObject.getString("success_msg") , Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ViewHomeworkActivity.this, HomeworkActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ViewHomeworkActivity.this, "Homework not updated.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ViewHomeworkActivity.this, "Homework not updated.", Toast.LENGTH_SHORT).show();
                }
            }, error -> Toast.makeText(ViewHomeworkActivity.this, error.toString(), Toast.LENGTH_SHORT).show()) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
//                    params.put("arraylist", stringTypeRequest);
                    params.put("data", stringTypeRequest);
                    params.put("short_name", name);
                    return params;
                }
            };
            /*{

                 @Override
                public byte[] getBody() {
                    try {
                        return stringTypeRequest == null ? null : stringTypeRequest.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", stringTypeRequest, "utf-8");
                        return null;
                    }
                }

                @Override
    public String getBodyContentType() {
        return "application/json";
    }
            };*/

            postRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewHomeworkActivity.this, HomeworkActivity.class);
        startActivity(intent);
        ViewHomeworkActivity.this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }
}
