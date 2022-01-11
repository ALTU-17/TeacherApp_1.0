package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;
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
import java.util.List;


import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.pojo.TeacherNoteePojo;


public class TeacherNoteListTableACtiity extends AppCompatActivity {
    private List<TeacherNoteePojo> list = new ArrayList<TeacherNoteePojo>();
    private ProgressDialog progressDialog;
    private ListView listView;
    String name,newUrl,dUrl;
    DatabaseHelper mDatabaseHelper;
   // private TeacherNoteTAbleADapter teacherNoteTAbleADapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_note_list_table_actiity);
        listView = findViewById(R.id.timetable_listView);
      /*  teacherNoteTAbleADapter = new TeacherNoteTAbleADapter(list, this);
        listView.setAdapter(teacherNoteTAbleADapter);
*/
        progressDialog=new ProgressDialog(this);
        getALlNotes();
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


    }

    private void getALlNotes() {
        final String reg_id = (SharedClass.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("acd_yr", academic_yr);
        params.put("reg_id", reg_id);
        params.put("short_name", name);
        progressDialog.setMessage("Loading Classes...");
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl+"AdminApi/" + "get_daily_notes", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();
                try {

                    if (response.getString("status").equals("true")) {
                        JSONArray jsonArray = response.getJSONArray("daily_notes");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String date = jsonObject.getString("date");
                            String notes_id = jsonObject.getString("notes_id");
                            String class_id = jsonObject.getString("class_id");
                            String teacher_id = jsonObject.getString("teacher_id");
                            String section_id = jsonObject.getString("section_id");
                            String subject_id = jsonObject.getString("subject_id");
                            String description = jsonObject.getString("description");
                            String classname = jsonObject.getString("classname");
                            String sectionname = jsonObject.getString("sectionname");
                            String subjectname = jsonObject.getString("subjectname");
                            String academic_yr = jsonObject.getString("academic_yr");
                            String publish = jsonObject.getString("publish");
                            String pDate = jsonObject.getString("publish_date");

                            TeacherNoteePojo teacherNoteePojo = new TeacherNoteePojo(notes_id, date, class_id, subject_id, teacher_id, section_id, description, classname, sectionname, subjectname, publish, academic_yr, pDate, false, class_id, section_id);

                            list.add(teacherNoteePojo);
                            //teacherNoteTAbleADapter.notifyDataSetChanged();

                            // FancyToast.makeText(CreateTeachernote.this,"Hello World !",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true);
                        }
                    } else {
                        Toast.makeText(TeacherNoteListTableACtiity.this, "No Record Found", Toast.LENGTH_SHORT).show();
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
}