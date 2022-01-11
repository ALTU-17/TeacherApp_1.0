package in.aceventura.evolvuschool.teacherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import in.aceventura.evolvuschool.teacherapp.adapter.RemarkStudentsListAdapter;
import in.aceventura.evolvuschool.teacherapp.pojo.RemarkStudentListPojo;

public class StudentsRemarkList extends AppCompatActivity {

    RecyclerView rvRemarksStudents;
    TextView tvSelectStudent;
    Button btnSubmit;
    DatabaseHelper mDatabaseHelper;
    String name, dUrl, newUrl, class_id, section_id, academic_yr;
    private JSONArray result;
    LinearLayoutManager rvManager;
    RemarkStudentsListAdapter remarkStudentsListAdapter;
    ArrayList<RemarkStudentListPojo> remarkStudentListPojoArrayList;
    ArrayList idArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_remark_list);

        rvRemarksStudents = findViewById(R.id.rvRemarksStudents);
        tvSelectStudent = findViewById(R.id.tvSelectStudent);
        btnSubmit = findViewById(R.id.btnSubmit);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        remarkStudentListPojoArrayList = new ArrayList<>();
        idArrayList = new ArrayList();
        Intent in = getIntent();
        class_id = in.getStringExtra("class_id");
        section_id = in.getStringExtra("section_id");

        academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        rvManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvRemarksStudents.setLayoutManager(rvManager);

        try {
            remarkStudentsListAdapter = new RemarkStudentsListAdapter(this, remarkStudentListPojoArrayList, idArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }


        final HashMap<String, String> params = new HashMap<>();
        params.put("academic_yr", academic_yr);
        params.put("class_id", class_id);
        params.put("section_id", section_id);
        params.put("short_name", name);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "AdminApi/" + "get_students", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response);
                    if (response.getString("status").equals("true")) {
                        result = response.getJSONArray("students");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject json = result.getJSONObject(i);
                            String rollNo = json.getString("roll_no");
                            String fName = json.getString("first_name");
                            String lName = json.getString("last_name");
                            String student_id = json.getString("student_id");
                            String class_id = json.getString("class_id");
                            String section_id = json.getString("section_id");

                            if (lName.equals("null")) {
                                lName = "";
                                RemarkStudentListPojo remarkStudentListPojo = new RemarkStudentListPojo(rollNo, fName, lName, student_id, class_id, section_id,false);
                                remarkStudentListPojoArrayList.add(remarkStudentListPojo);
                            } else if (rollNo.equals("null")) {
                                rollNo = "";
                                RemarkStudentListPojo remarkStudentListPojo = new RemarkStudentListPojo(rollNo, fName, lName, student_id, class_id, section_id,false);
                                remarkStudentListPojoArrayList.add(remarkStudentListPojo);
                            } else {
                                RemarkStudentListPojo remarkStudentListPojo = new RemarkStudentListPojo(rollNo, fName, lName, student_id, class_id, section_id,false);
                                remarkStudentListPojoArrayList.add(remarkStudentListPojo);
                            }

                            rvRemarksStudents.setAdapter(remarkStudentsListAdapter);

                        }
                    } else {

                        Toast.makeText(StudentsRemarkList.this, "No Record", Toast.LENGTH_SHORT).show();
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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(jsonObjectRequest);
    }

    public void SendStudentIds(View view) {
        Intent intent = new Intent(StudentsRemarkList.this, CreateRemarkActivity.class);
        intent.putExtra("studentIds_array", idArrayList.toString());
        startActivity(intent);

        finish();

    }
}
