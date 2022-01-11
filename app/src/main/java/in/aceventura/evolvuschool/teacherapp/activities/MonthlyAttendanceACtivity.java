package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.adapter.VewOnlyNoteAdapter;
import in.aceventura.evolvuschool.teacherapp.pojo.Viewonlynotespojo;

public class MonthlyAttendanceACtivity extends AppCompatActivity {
    ArrayList<Viewonlynotespojo> viewonlynotespojoArrayList;
    private ArrayList<String> listClass;
    String class_id, section_id;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    ProgressBar progressBar;
    private Spinner spinclass;
    private TextView btnsearch;
    VewOnlyNoteAdapter vewOnlyNoteAdapter;
    private JSONArray result;
    String clssnm,name,newUrl,dUrl;
    DatabaseHelper mDatabaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_attendance_activity);
        getId();
        progressBar.setVisibility(View.GONE);
        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl=mDatabaseHelper.getURL(1);
        newUrl=mDatabaseHelper.getTURL(1);

        if (name ==null || name.equals("")){
            name = mDatabaseHelper.getName(1);
            dUrl=mDatabaseHelper.getURL(1);
            newUrl=mDatabaseHelper.getTURL(1);
        }

    }

    private void getId() {
        RecyclerView recyclerView = findViewById(R.id.recycleridmonthattendance);
        TextView txtpickmonth = findViewById(R.id.monthpick);
        spinclass=findViewById(R.id.classspinmontattendance);
        btnsearch=findViewById(R.id.searchbtnmonthattendance);
//        progressDialog=new ProgressDialog(this);
        progressBar=findViewById(R.id.progressBar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        viewonlynotespojoArrayList=new ArrayList<>();
       // vewOnlyNoteAdapter=new VewOnlyNoteAdapter(this,viewonlynotespojoArrayList);
        requestQueue= Volley.newRequestQueue(getBaseContext());
        recyclerView.setAdapter(vewOnlyNoteAdapter);
        listClass = new ArrayList<>();
//        name = (SharedClass.getInstance(this).getShortname());
//        newUrl=(SharedClass.getInstance(this).getUrl());

        spinclass.setOnTouchListener(classSpinnerOnTouch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtpickmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    private View.OnTouchListener classSpinnerOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                //Your code
                listClass.clear();
                getClasses();
            }
            return false;
        }
    };
    private void getClasses() {

        final String reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        HashMap<String, String> params = new HashMap<>();
        params.put("academic_yr", academic_yr);
        params.put("reg_id", reg_id);
        params.put("short_name", name);
//        progressDialog.setMessage("Loading Classes...");
//        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Config.getClass1, new JSONObject(params), new Response.Listener<JSONObject>() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/"+"getClassAndSection_classteacheralloted", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

//                progressDialog.dismiss();
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
                            clssnm = a;
                            // FancyToast.makeText(CreateTeachernote.this,"Hello World !",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true);
                        }
                    } else {
                        Toast.makeText(MonthlyAttendanceACtivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
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
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

}
