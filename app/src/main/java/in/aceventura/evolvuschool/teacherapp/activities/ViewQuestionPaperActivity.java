package in.aceventura.evolvuschool.teacherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import in.aceventura.evolvuschool.teacherapp.adapter.QuestionPaperAdapter;
import in.aceventura.evolvuschool.teacherapp.pojo.QbankModel;
import in.aceventura.evolvuschool.teacherapp.pojo.QuesPaperModel;

import static in.aceventura.evolvuschool.teacherapp.R.layout;

public class ViewQuestionPaperActivity extends AppCompatActivity {
    QbankModel qbankModel;
    TextView tv_exam_name, tv_class_subject_name, tv_qb_name, tv_marks, tv_instructions;
    DatabaseHelper mDatabaseHelper;
    QuestionPaperAdapter questionPaperAdapter;
    private String name, newUrl, dUrl, prourl;
    private List<QuesPaperModel> qpList = new ArrayList<>();
    private RecyclerView rv_quesPaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_view_question_paper);
        hooks();

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        dUrl = mDatabaseHelper.getURL(1);
        newUrl = mDatabaseHelper.getTURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            dUrl = mDatabaseHelper.getURL(1);
            newUrl = mDatabaseHelper.getTURL(1);
            prourl = mDatabaseHelper.getPURL(1);
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            qbankModel = (QbankModel) bundle.getSerializable("user");
        }

        if (qbankModel != null) {
            tv_exam_name.setText(qbankModel.getExam_name());
            tv_class_subject_name.setText(String.format("%s - %s", qbankModel.getClass_name(),
                    qbankModel.getSubject_name()));
            tv_qb_name.setText(qbankModel.getQuestionBankName());
            tv_marks.setText(qbankModel.getWeightage());
            tv_instructions.setText(qbankModel.getInstructions());

            //checking attachments
            checkAttachments(qbankModel);
        }


        //IMPORTANT
        // TODO: 09-09-2020 add question papers to qplist
        questionPaperAdapter = new QuestionPaperAdapter(this, qpList);
        rv_quesPaper.setAdapter(questionPaperAdapter);
        final LinearLayoutManager newsLinearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        rv_quesPaper.setLayoutManager(newsLinearLayoutManager);
    }

    private void checkAttachments(QbankModel qbankModel) {
        HashMap<String, String> params = new HashMap<>();
        params.put("question_bank_id", qbankModel.getQuestion_bank_id());
        params.put("short_name", name);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                "/get_images_question_paper", new JSONObject(params), response -> {
            System.out.println("Question_Paper" + response);
            try {
                if (response.getString("status").equals("true")) {
                    qpList.clear();

                    String url = response.getString("url");
                    JSONArray jsonArray = response.getJSONArray("images");
                    String qb_type = qbankModel.getQbtype();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        System.out.println(jsonObject);
                        String uploaded_qp_id = jsonObject.getString("uploaded_qp_id");
                        String question_bank_id = jsonObject.getString("question_bank_id");
                        String image_name = jsonObject.getString("image_name");
                        String file_size = jsonObject.getString("file_size");

//                      {"uploaded_qp_id":"43","question_bank_id":"33","file_size":"275033.00","image_name":" 19286.jpeg"}
                        QuesPaperModel qpModel = new QuesPaperModel(uploaded_qp_id,question_bank_id,"",image_name,
                                file_size,url,qb_type);
                        qpList.add(qpModel);
                        questionPaperAdapter.notifyDataSetChanged();
                    }
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(jsonObjectRequest);
    }


    private void hooks() {
        tv_exam_name = findViewById(R.id.tv_exam_name);
        tv_class_subject_name = findViewById(R.id.tv_class_name);
        tv_qb_name = findViewById(R.id.tv_qb_name);
        tv_marks = findViewById(R.id.tv_marks);
        tv_instructions = findViewById(R.id.tv_instructions);
        rv_quesPaper = findViewById(R.id.rv_quesPaper);
    }
}