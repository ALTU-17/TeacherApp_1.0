package in.aceventura.evolvuschool.teacherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.adapter.MonitorStudentOngoingExamAdapter;
import in.aceventura.evolvuschool.teacherapp.pojo.EvaluationStudentListModel;
import in.aceventura.evolvuschool.teacherapp.pojo.MonitoringStudentsModel;

import static in.aceventura.evolvuschool.teacherapp.R.layout;

public class MonitorStudentsListActivity extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView rv_evaluationStudents;
    TextView nodata;
    MonitorStudentOngoingExamAdapter monitorStudentOngoingExamAdapter;
    LinearLayoutManager manager;
    ArrayList<MonitoringStudentsModel> mEvaluationStudentsList = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_monitor_students_list);

        nodata = findViewById(R.id.nodata);
        rv_evaluationStudents = findViewById(R.id.rv_evaluationStudents);
        progressBar = findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        mEvaluationStudentsList.clear();

        /*Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            nodata.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            rv_evaluationStudents.setVisibility(View.VISIBLE);
            MonitoringStudentsModel monitoringStudentsModel = (MonitoringStudentsModel) bundle.getSerializable("students_lists");
            mEvaluationStudentsList.add(monitoringStudentsModel);
        }
        else {
            progressBar.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
            rv_evaluationStudents.setVisibility(View.GONE);
        }*/

        MonitoringStudentsModel model1 = new MonitoringStudentsModel("1","Oliver Queen","1","Not Started");
        MonitoringStudentsModel model2 = new MonitoringStudentsModel("2","Felicity Smoke 2","2","Not Started");
        mEvaluationStudentsList.add(model1);
        mEvaluationStudentsList.add(model2);

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_evaluationStudents.setLayoutManager(manager);
        monitorStudentOngoingExamAdapter = new MonitorStudentOngoingExamAdapter(getApplicationContext(), mEvaluationStudentsList);
        rv_evaluationStudents.setAdapter(monitorStudentOngoingExamAdapter);
    }
}