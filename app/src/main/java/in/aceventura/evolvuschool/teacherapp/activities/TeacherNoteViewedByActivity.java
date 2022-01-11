package in.aceventura.evolvuschool.teacherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
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
import in.aceventura.evolvuschool.teacherapp.adapter.TeacherNoteViewedByAdapter;
import in.aceventura.evolvuschool.teacherapp.pojo.ViewedByListPojo;

public class TeacherNoteViewedByActivity extends AppCompatActivity {

    TextView tv_note_desc;
    ProgressBar progressBar;
    DatabaseHelper mDatabaseHelper;
    String name, newUrl, dUrl, class_id, section_id, academic_yr, rollNo, fName,mName,lName, student_id, new_class_id, new_section_id, parent_id;
    private JSONArray result;
    ArrayList<ViewedByListPojo> viewdByPojoArrayList;
    RecyclerView recyclerview;
    TeacherNoteViewedByAdapter teacherNoteViewedByAdapter;
    LinearLayoutManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_note_viewed_by);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }


        getIds();
        progressBar.setVisibility(View.GONE);

        viewdByPojoArrayList = new ArrayList<>();
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(manager);


        //get values
        Intent intent = getIntent();
        String note_desc = intent.getStringExtra("note_desc");
        String notes_id = intent.getStringExtra("note_id");
        class_id = intent.getStringExtra("class_id");
        section_id = intent.getStringExtra("section_id");
        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());

        //getting all students list...
        getStudentsList(class_id, section_id, academic_yr, notes_id);


        tv_note_desc.setText(note_desc);

    }

    private void getStudentsList(String class_id, String section_id, final String academic_yr, final String notes_id) {
        progressBar.setVisibility(View.VISIBLE);
        final HashMap<String, String> params = new HashMap<>();
        params.put("acd_yr", academic_yr);
        params.put("class_id", class_id);
        params.put("section_id", section_id);
        params.put("notes_id", notes_id);
        params.put("short_name", name);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "get_students_notes_viewed", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("MANOJRES" + response.toString());
                try {
                    if (response.getString("status").equals("true")) {
                        progressBar.setVisibility(View.GONE);

                        result = response.getJSONArray("student_list");

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject json = result.getJSONObject(i);
                            rollNo = json.getString("roll_no");
                            fName = json.getString("first_name");
                            mName = json.getString("first_name");
                            lName = json.getString("last_name");
                            String read_status = json.getString("read_status");
                            System.out.println(response.toString());

                            if (lName.equals("null")) {
                                lName = "";
                                ViewedByListPojo viewedByListPojo = new ViewedByListPojo(rollNo, fName,mName, lName,read_status);
                                viewdByPojoArrayList.add(viewedByListPojo);
                            }else if (mName.equals("null" )|| mName.equals("")) {
                                lName = "";
                                ViewedByListPojo viewedByListPojo = new ViewedByListPojo(rollNo, fName,mName, lName,read_status);
                                viewdByPojoArrayList.add(viewedByListPojo);
                            }
                            else if (rollNo.equals("null")) {
                                rollNo = "";
                                ViewedByListPojo viewedByListPojo = new ViewedByListPojo(rollNo, fName,mName, lName,read_status);
                                viewdByPojoArrayList.add(viewedByListPojo);
                            } else {
                                ViewedByListPojo viewedByListPojo = new ViewedByListPojo(rollNo, fName,mName, lName,read_status);
                                viewdByPojoArrayList.add(viewedByListPojo);
                            }
                            teacherNoteViewedByAdapter = new TeacherNoteViewedByAdapter(getApplicationContext(), viewdByPojoArrayList);
                            recyclerview.setAdapter(teacherNoteViewedByAdapter);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(TeacherNoteViewedByActivity.this, "No Record", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);

        jsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        requestQueue1.add(jsonObjectRequest);

    }

    private void getIds() {
        tv_note_desc = findViewById(R.id.tv_note_desc);
        recyclerview = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressBar);

    }

    //what to do backpressed of < mobile and <- backarrow...
    @Override
    public void onBackPressed() {
        String reg_id = (SharedClass.getInstance(this).getRegId().toString());
        String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        Intent in = new Intent(TeacherNoteViewedByActivity.this, TeachersNoteACtivity.class);
        startActivity(in);
        in.putExtra("reg_id", reg_id);
        in.putExtra("academic_yr", academic_yr);
        TeacherNoteViewedByActivity.this.finish();
    }


    //enable back arrow
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

