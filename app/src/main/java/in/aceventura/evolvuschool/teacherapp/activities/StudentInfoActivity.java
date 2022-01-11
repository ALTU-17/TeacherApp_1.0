package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;


public class StudentInfoActivity extends AppCompatActivity {

    private Spinner classSpin,StudSpin;
    private RecyclerView recycler;
    private LinearLayout linearLayoutSearch,linearLayoutview;
    private ArrayList<String> listClass;
    private ArrayList<String> liststudent;
    private ProgressDialog progressDialog;
    String class_id, section_id,student_id,name,newUrl,dUrl;
    private JSONArray result;
    private Button btnview,btnresult;
    DatabaseHelper mDatabaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getId();
        progressDialog = new ProgressDialog(this);

        listClass = new ArrayList<String>();
//        Sname = (SharedClass.getInstance(this).getShortname());
//        newUrl=(SharedClass.getInstance(this).getUrl());

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl=mDatabaseHelper.getURL(1);
        newUrl=mDatabaseHelper.getTURL(1);

        if (name ==null || name.equals("")){
            name = mDatabaseHelper.getName(1);
            dUrl=mDatabaseHelper.getURL(1);
            newUrl=mDatabaseHelper.getTURL(1);
        }


        liststudent = new ArrayList<String>();
        classSpin.setOnTouchListener(classSpinnerOnTouch);
        StudSpin.setOnTouchListener(studentSpinnerOnTouch);
        StudSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {   //String option;

                //listClass.clear();
                //listStudent.clear();
                //notifyAll();
                student_id = getStudentID(position);
                //t4.setText("student_id: "+getStudentID(position));


                class_id = getClassId(position);
                section_id = getSectionId(position);


                //Toast.makeText(getApplicationContext(),"Class: "+class_id+" Section :"+section_id,Toast.LENGTH_LONG).show();;
                //getData1(getClassId(position), getSectionId(position));
                StudSpin.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, liststudent));



//                subjectSpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listSubject));
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        try {
            classSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {   //String option;

                    //listClass.clear();
                    //notifyAll();
                    //t1.setText(getClassId(position));
                    //t2.setText(getSectionId(position));
                    try {
                        class_id = getClassId(position);
                        section_id = getSectionId(position);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Toast.makeText(getApplicationContext(), "Class: " + class_id + " Section :" + section_id, Toast.LENGTH_LONG).show();;
                    //getData1(getClassId(position), getSectionId(position));
                    StudSpin.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, liststudent));

//                subjectSpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listSubject));
                } // to close the onItemSelected

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getId() {
        classSpin=findViewById(R.id.classstudinfo);
        StudSpin=findViewById(R.id.studnm);
        recycler=findViewById(R.id.recyclerstudInfo);
        linearLayoutSearch=findViewById(R.id.searchbtnstud);
        linearLayoutview=findViewById(R.id.linearLayoutview);

        btnview=findViewById(R.id.viewprofile);
        btnresult=findViewById(R.id.result);
        linearLayoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              linearLayoutview.setVisibility(View.VISIBLE);
            }
        });
        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(StudentInfoActivity.this,StudentViewActivity.class);
                startActivity(intent);
            }
        });
    }
    private String getStudentID(int position){
        String classId="";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            classId = json.getString("student_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return classId;
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

    public void getStuds() {

        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        progressDialog.show();
        progressDialog.setMessage("Loading Students...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl+"AdminApi/"+ "get_students",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        JSONObject j = null;
                        try {
                            result = new JSONArray(response);
                            //Toast.makeText(ManageHomework.this,result.toString(),Toast.LENGTH_LONG);

                            //Calling method getStudents to get the students from the JSON Array
                            getStudent(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(RemarkActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Select class ", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("academic_yr", academic_yr);
                params.put("class_id", class_id);
                params.put("section_id", section_id);
                params.put("login_type", "T");
                params.put("short_name", name);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private View.OnTouchListener studentSpinnerOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                liststudent.clear();
                getStuds();
                StudSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {   //String option;
                        // subjectSpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listSubject));
                        student_id = getStudentID(position);

                        //Toast.makeText(getApplicationContext(), "Class: " + class_id + " Section :" + section_id + "Subject: " + subject_id, Toast.LENGTH_LONG).show();;

                    } // to close the onItemSelected

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            return false;
        }
    };


    private void getStudent(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                String rollNo = json.getString("roll_no");

                String fName = json.getString("first_name");
                String lName = json.getString("last_name");
                // listStudents.add(rollNo+". "+fName+" "+lName);

                if (lName.equals("null")){
                    liststudent.add(rollNo+". "+fName);
                }else{
                    liststudent.add(rollNo+". "+fName+" "+lName);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        StudSpin.setAdapter(new ArrayAdapter<String>(StudentInfoActivity.this, android.R.layout.simple_spinner_dropdown_item, liststudent));
    }
    private void getClasses() {

        final String reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("academic_yr", academic_yr);
        params.put("reg_id", reg_id);
        progressDialog.setMessage("Loading Classes...");
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl+"AdminApi/"+ "get_class_of_classteacher", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();
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
                            // FancyToast.makeText(CreateTeachernote.this,"Hello World !",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true);
                        }
                    } else {
                        Toast.makeText(StudentInfoActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                classSpin.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listClass));

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


