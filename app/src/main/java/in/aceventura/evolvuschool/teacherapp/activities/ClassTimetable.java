package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import in.aceventura.evolvuschool.teacherapp.Config;
import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.adapter.ClassTimetableAdapter;
import in.aceventura.evolvuschool.teacherapp.pojo.DataSet;


public class ClassTimetable extends AppCompatActivity {
   // private static final String tag = RemarkActivity.class.getSimpleName();
    private static final String url = "http://androidlearnings.com/school/services/";
    private List<DataSet> list = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ListView listView;
    private ClassTimetableAdapter timetableadapter;
    String classid;
    String sectionid;
    RequestQueue requestQueue;
    ArrayList<DataSet> timetablepojosarraylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue= Volley.newRequestQueue(getBaseContext());
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_class_timetable);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Time Table");
timetablepojosarraylist=new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        Bundle bundle = getIntent().getExtras();

      /*  classid = bundle.getString("CLASSID");
        sectionid = bundle.getString("SECTIONID");
*/
        listView = findViewById(R.id.timetable_listView);
        timetableadapter = new ClassTimetableAdapter(this, list);
        listView.setAdapter(timetableadapter);


        //get_timetable();
        get_teacher_timetable();
        // Toast.makeText(BookItemsActivity.this,"Category:"+book_category_id.toString()+" "+"Title:"+book_title+" "+"Author:"+book_author,Toast.LENGTH_LONG).show();

    }

    private void get_teacher_timetable() {
        final String reg_id = (SharedClass.getInstance(this).getRegId().toString());

        final String day="Monday";
        final String date="1";
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DATA_URL+"get_teacher_timetable",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            if (response != null) {

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("timetable");

                                Log.d("test11", "onResponse: " + jsonArray);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                    DataSet dataSet=new DataSet();
                                    dataSet.setSubject(jsonObject1.getString("suject"));
                                    /*String section = jsonObject1.getString("section");
                                    String classnm = jsonObject1.getString("class");
                                    String subject = jsonObject1.getString("subject");
                                    Timetablepojo vpojo = new Timetablepojo(section,classnm, subject);
                                    timetablepojosarraylist.add(vpojo);
*/

                                    timetablepojosarraylist.add(dataSet);


                                    timetableadapter.notifyDataSetChanged();
                                }

                            } else {
                                Toast.makeText(ClassTimetable.this, "No Data Found", Toast.LENGTH_SHORT).show();

                                Log.d("test11", "onResponse: " + "No Data");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            // System.out.println(" Response CallR_Post_api-->" + e.getMessage());
                        }
                        //Toast.makeText(RemarkActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(RemarkActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put(SharedClass.KEY_ACADEMIC_YEAR,academic_yr);
                params.put("teacher_id",reg_id);
                params.put("column_name",day);
                params.put("period_no",date);

                return params;
            }

        };
        requestQueue.add(stringRequest);

    }


    public void get_timetable(){
        final String academic_yr = (SharedClass.getInstance(this).loadSharedPreference_acdYear());
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DATA_URL+"get_timetable",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if(response.length()<1){
                            Toast.makeText(ClassTimetable.this,"Timetable not available for this class",Toast.LENGTH_LONG).show();
                        }else
                            try {
                                JSONArray result = new JSONArray(response);

                                for (int i = 0; i < result.length(); i++) {

                                    JSONObject obj = result.getJSONObject(i);

                                    DataSet dataSet = new DataSet();

                                    dataSet.setPeriodNo(obj.getInt("period_no"));
                                    dataSet.setClassIn(obj.getString("time_in"));
                                    dataSet.setClassOut(obj.getString("time_out"));
                                    dataSet.setTtMonday(obj.getString("monday"));
                                    dataSet.setTtTuesday(obj.getString("tuesday"));
                                    dataSet.setTtWednesday(obj.getString("wednesday"));
                                    dataSet.setTtThursday(obj.getString("thursday"));
                                    dataSet.setTtFriday(obj.getString("friday"));

//                                    dataSet.setSatClassIn(obj.getString("sat_in"));
//                                    dataSet.setSatClassOut(obj.getString("sat_out"));


                                    String saturday_in =obj.getString("sat_in");
                                    String saturday_out =obj.getString("sat_out");
                                    if (saturday_in.equals("0") && saturday_out.equals("0")){
                                        dataSet.setSatClassIn("");
                                        dataSet.setSatClassOut("");
                                    }else {
                                        dataSet.setSatClassIn(obj.getString("sat_in"));
                                        dataSet.setSatClassOut(obj.getString("sat_out"));
                                    }

                                    String saturday_period =obj.getString("saturday");
                                    if (saturday_period.equals("0")){
                                        dataSet.setTtSaturday("--");
                                    }else
                                    dataSet.setTtSaturday(obj.getString("saturday"));
                                    list.add(dataSet);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        timetableadapter.notifyDataSetChanged();
                        //Toast.makeText(RemarkActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(RemarkActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(SharedClass.KEY_ACADEMIC_YEAR,academic_yr);
                params.put("class_id",classid);
                params.put("section_id",sectionid);

                return params;
            }

        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }

}
