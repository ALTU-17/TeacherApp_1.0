package in.aceventura.evolvuschool.teacherapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.activities.EditQuestionPaperActivity;
import in.aceventura.evolvuschool.teacherapp.activities.SharedClass;
import in.aceventura.evolvuschool.teacherapp.activities.ViewQuestionPaperActivity;
import in.aceventura.evolvuschool.teacherapp.pojo.QbankModel;

public class QuestionBankListAdapter extends RecyclerView.Adapter<QuestionBankListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<QbankModel> qBanksList;
    private String name, newUrl, academic_yr, reg_id;
    private ProgressDialog progressDialog;


    //constructor
    public QuestionBankListAdapter(Context context, ArrayList<QbankModel> qBanksList) {
        this.context = context;
        this.qBanksList = qBanksList;
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }
        progressDialog = new ProgressDialog(context);
    }

    //setting layout file
    @NonNull
    @Override
    public QuestionBankListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.qbankslist_item, parent, false));
    }

    //Logic & Setting the values to UI
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        reg_id = (SharedClass.getInstance(context).getRegId().toString());
        final QbankModel qbankModel = qBanksList.get(position);
        holder.tv_qBankName.setText(qbankModel.getQuestionBankName());
        holder.tv_ExamName.setText(qbankModel.getExam_name());
        holder.tv_ClassName.setText(qbankModel.getClass_name());
        holder.tv_SubjectName.setText(qbankModel.getSubject_name());
        holder.tv_CreatedOnDate.setText(qbankModel.getCreatedOn());

        if (qbankModel.getStatus().equals("Y")) {
            holder.tv_StatusName.setText("Complete");
        }
        else if (qbankModel.getStatus().equals("N")) {
            holder.tv_StatusName.setText("Incomplete");
        }


        String teacher_id = qbankModel.getTeacher_id();
        String allotted_answered = qbankModel.getAllotted_answered();
        final String qbId = qbankModel.getQuestion_bank_id();
        final String qbType = qbankModel.getQbtype();


        if (!teacher_id.equals(reg_id)) {
            holder.editQBank.setVisibility(View.GONE);
            holder.deleteQBank.setVisibility(View.GONE);
        }
        // 0 - deallotted, 1 - alloted
        else if (allotted_answered.equals("1")) {
            holder.editQBank.setVisibility(View.GONE);
            holder.deleteQBank.setVisibility(View.GONE);
        }
        else {
            holder.editQBank.setVisibility(View.VISIBLE);
            holder.deleteQBank.setVisibility(View.VISIBLE);
        }
        holder.viewQBank.setVisibility(View.VISIBLE);

        //View Question Bank
        holder.viewQBank.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewQuestionPaperActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("user", qbankModel);
            intent.putExtras(b); //pass bundle to your intent
            context.startActivity(intent);
        });

        //Edit Question Bank
        holder.editQBank.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditQuestionPaperActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("user", qbankModel);
            intent.putExtras(b); //pass bundle to your intent
            intent.putExtra("reg_id", reg_id);
            context.startActivity(intent);
        });

        //Delete Question Bank
        holder.deleteQBank.setOnClickListener(v -> {
            androidx.appcompat.app.AlertDialog.Builder dialog =
                    new androidx.appcompat.app.AlertDialog.Builder(context);
            dialog.setCancelable(false);
            dialog.setIcon(android.R.drawable.ic_dialog_alert);
            dialog.setTitle("Delete");
            dialog.setMessage("Are sure to delete this Question Bank?");
            dialog.setPositiveButton("Yes", (dialog1, id) -> deleteQuestionBank(qbId, qbType, position));
            dialog.setNegativeButton("No", (dialog12, which) -> dialog12.dismiss());
            final androidx.appcompat.app.AlertDialog alert = dialog.create();
            alert.show();

        });
    }

    private void deleteQuestionBank(final String qbId, final String qbType, final int position) {
        reg_id = (SharedClass.getInstance(context).getRegId().toString());
        academic_yr = (SharedClass.getInstance(context).loadSharedPreference_acdYear());
        progressDialog.setMessage("Deleting Question Bank...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "OnlineExamApi" +
                "/question_bank_delete", response -> {
            try {
                Log.d("OnDelete", response);
                if (response != null) {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        qBanksList.remove(position);
                        Toast.makeText(context, "Question Bank deleted " + "successfully!!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        notifyDataSetChanged();
                    }
                    else {
                        //false
                        progressDialog.dismiss();
                        System.out.println(response);
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Unable to delete question bank", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                progressDialog.dismiss();
                e.printStackTrace();
                Log.d("ERROR", e.toString());
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            progressDialog.dismiss();
            error.getMessage();
            Log.d("ERROR", error.toString());
            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                final HashMap<String, String> params = new HashMap<>();
                params.put("question_bank_type", qbType);
                params.put("short_name", name);
                params.put("question_bank_id", qbId);
                Log.d("DELETE_PARAMS", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(context);
        requestQueue1.add(stringRequest);
    }

    //setting count
    @Override
    public int getItemCount() {
        return qBanksList.size();
    }

    //Get ItemId from position
    @Override
    public long getItemId(int position) {
        return position;
    }

    //Binding the views with components from layout file
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_qBankName, tv_ExamName, tv_ClassName, tv_SubjectName, tv_CreatedOnDate, tv_StatusName;
        LinearLayout viewQBank, editQBank, deleteQBank;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_qBankName = itemView.findViewById(R.id.tv_QbName);
            tv_ExamName = itemView.findViewById(R.id.tv_ExamName);
            tv_ClassName = itemView.findViewById(R.id.tv_ClassName);
            tv_SubjectName = itemView.findViewById(R.id.tv_SubjectName);
            tv_CreatedOnDate = itemView.findViewById(R.id.tv_CreatedOnDate);
            tv_StatusName = itemView.findViewById(R.id.tv_StatusName);
            viewQBank = itemView.findViewById(R.id.viewQBank);
            editQBank = itemView.findViewById(R.id.editQBank);
            deleteQBank = itemView.findViewById(R.id.deleteQBank);
        }
    }
}
