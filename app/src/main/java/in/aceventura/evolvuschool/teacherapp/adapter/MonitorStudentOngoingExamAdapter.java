package in.aceventura.evolvuschool.teacherapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.activities.EvaluationQuestionActivity;
import in.aceventura.evolvuschool.teacherapp.activities.PenPaperEvaluationActivity;
import in.aceventura.evolvuschool.teacherapp.pojo.EvaluationQuestionModel;
import in.aceventura.evolvuschool.teacherapp.pojo.EvaluationStudentListModel;
import in.aceventura.evolvuschool.teacherapp.pojo.MonitoringStudentsModel;

/**
 * Created by "Manoj Waghmare" on 25,Aug,2020
 **/
public class MonitorStudentOngoingExamAdapter extends RecyclerView.Adapter<MonitorStudentOngoingExamAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MonitoringStudentsModel> evaluationStudentList;
    private ArrayList<EvaluationQuestionModel> evaluationQuestionsList = new ArrayList<>();
    String name, dUrl, newUrl, qb_id, question_bank_id, question_id, qb_name, question_type,
            question, weightage, status, date, student_id;

    //constructor
    public MonitorStudentOngoingExamAdapter(Context context, ArrayList<MonitoringStudentsModel> evaluationStudentList) {
        this.context = context;
        this.evaluationStudentList = evaluationStudentList;
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
        }
    }

    //Binding the views with components from layout file
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_rollNo, tv_studentName, tv_question_no,tv_status_of_exam;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_rollNo = itemView.findViewById(R.id.tv_rollNo);
            tv_studentName = itemView.findViewById(R.id.tv_studentName);
            tv_question_no = itemView.findViewById(R.id.tv_question_no);
            tv_status_of_exam = itemView.findViewById(R.id.tv_status_of_exam);
        }
    }

    //setting layout file
    @NonNull
    @Override
    public MonitorStudentOngoingExamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.monitoring_studentlist_item, parent, false)
        );
    }


    //Logic & Setting the values to UI
    @Override
    public void onBindViewHolder(@NonNull MonitorStudentOngoingExamAdapter.ViewHolder holder, final int position) {
        final MonitoringStudentsModel model = evaluationStudentList.get(position);
        holder.tv_rollNo.setText(model.getRoll_no() + ".");
        holder.tv_studentName.setText(model.getStudent_name());
        holder.tv_question_no.setText(model.getAttempting_que_no());
        holder.tv_status_of_exam.setText(model.getExam_status());
    }

    //setting count
    @Override
    public int getItemCount() {
        return evaluationStudentList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

