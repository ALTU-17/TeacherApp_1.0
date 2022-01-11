package in.aceventura.evolvuschool.teacherapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.pojo.AllotDeallotModel;

/**
 * Created by "Manoj Waghmare" on 04,Sep,2020
 **/

public class AllotDeallotListAdapter
        extends RecyclerView.Adapter<AllotDeallotListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AllotDeallotModel> mAllotDeallotList;
    private DatabaseHelper mDatabaseHelper;
    private String name, dUrl, newUrl;
    private ProgressDialog progressDialog;

    //constructor
    public AllotDeallotListAdapter(Context context,
                                   ArrayList<AllotDeallotModel> mAllotDeallotList) {
        this.context = context;
        this.mAllotDeallotList = mAllotDeallotList;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");

        mDatabaseHelper = new DatabaseHelper(context);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }

    }

    //setting layout file
    @NonNull
    @Override
    public AllotDeallotListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                 int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.allot_deallot_list_item, parent, false)
        );
    }

    //Logic & Setting the values to UI
    @Override
    public void onBindViewHolder(@NonNull AllotDeallotListAdapter.ViewHolder holder,
                                 final int position) {

        final AllotDeallotModel allotDeallotModel = mAllotDeallotList.get(position);
        holder.tv_qbName.setText(allotDeallotModel.getQuestionBankName());
        holder.tv_ClassName.setText(allotDeallotModel.getClassName() + allotDeallotModel.getDivision());
        holder.tv_SubjectName.setText(allotDeallotModel.getSubjectName());

        holder.btn_deallot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressDialog.show();
                HashMap<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("operation", "delete");
                params.put("class_id", allotDeallotModel.getClass_id());
                params.put("section_id", allotDeallotModel.getSection_id());
                params.put("subject_id", allotDeallotModel.getSubject_id());
                params.put("question_bank_id", allotDeallotModel.getQuestion_bank_id());
                params.put("acd_yr", allotDeallotModel.getAcademic_yr());
                params.put("teacher_id", allotDeallotModel.getTeacher_id());
                System.out.println("DE_ALLOT_QB" + params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl +
                        "OnlineExamApi/question_bank_allot_dellot", new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("DE_ALLOT_QB", response.toString());
                                try {
                                    if (response.getString("status").equals("true")) {
                                        //removing the item
                                        mAllotDeallotList.remove(position);
                                        notifyDataSetChanged();
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Question Bank DeAllotted",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        //false
                                        System.out.println(response);
                                        progressDialog.dismiss();
                                        Toast.makeText(context, response.toString(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch (JSONException e) {
                                    progressDialog.dismiss();
                                    e.printStackTrace();
                                    System.out.println("ERROR" + e.getMessage());
                                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, error -> {
                            progressDialog.dismiss();
                            System.out.println("ERROR_RESPONSE - " + error.getMessage());
                            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                        });
                RequestQueue requestQueue1 = Volley.newRequestQueue(context);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue1.add(jsonObjectRequest);
            }
        });
    }

    //setting count
    @Override
    public int getItemCount() {
        return mAllotDeallotList.size();
    }

    //Get ItemId from position
    @Override
    public long getItemId(int position) {
        return position;
    }

    //Binding the views with components from layout file
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_qbName, tv_ClassName, tv_SubjectName;
        Button btn_deallot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_qbName = itemView.findViewById(R.id.tv_qbName);
            tv_ClassName = itemView.findViewById(R.id.tv_ClassName);
            tv_SubjectName = itemView.findViewById(R.id.tv_SubjectName);
            btn_deallot = itemView.findViewById(R.id.btn_deallot);
        }
    }
}

