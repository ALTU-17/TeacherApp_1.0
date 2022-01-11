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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
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
import in.aceventura.evolvuschool.teacherapp.pojo.UploadEvaluationQuestionModel;

/**
 * Created by "Manoj Waghmare" on 25,Aug,2020
 **/

public class EvaluationStudentListAdapter extends RecyclerView.Adapter<EvaluationStudentListAdapter.ViewHolder> {

    ArrayList<UploadEvaluationQuestionModel> uploadEvaluationQuestionsList = new ArrayList<>();
    String name, dUrl, newUrl, qb_id, question_bank_id, question_id, qb_name, question_type, question, weightage
            , status, date, student_id,ques_array,ans_array;
    String up_id, upload_student_id, upload_question_bank_id, upload_date, academic_yr, ques_image_name,
            ques_file_size, uploaded_qp_id, ans_image_name, ans_file_size, exam_id, class_id, subject_id,
            qb_type, instructions, create_date, teacher_id, complete, classname, subjectname, url, marks_obt,
            ques_paper_attachments, ans_paper_attachments;
    private Context context;
    private ArrayList<EvaluationStudentListModel> evaluationStudentList;
    private ArrayList<EvaluationQuestionModel> evaluationQuestionsList = new ArrayList<>();

    //constructor
    public EvaluationStudentListAdapter(Context context,
                                        ArrayList<EvaluationStudentListModel> evaluationStudentList) {
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

    //setting layout file
    @NonNull
    @Override
    public EvaluationStudentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.evaluation_studentlist_item, parent,
                false));
    }

    //Logic & Setting the values to UI
    @Override
    public void onBindViewHolder(@NonNull EvaluationStudentListAdapter.ViewHolder holder, final int position) {
        final EvaluationStudentListModel model = evaluationStudentList.get(position);

        if (!model.getRollNo().equals("null")) {
            holder.tv_rollNo.setText(model.getRollNo() + ".");
        }
        else {
            holder.tv_rollNo.setText("");
        }

        holder.tv_studentName.setText(model.getFirstName() + " " + model.getLastName());
        String submittedDate = model.getSubmittedDate();
        /*final String student_id = model.getStudentId();*/

        //data to pass
        date = model.getDate();
        student_id = model.getStudentId();


        //Formatting Date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(submittedDate);

        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat newformat = new SimpleDateFormat("dd" + "-MM-yyyy");
        String newDate = null;
        if (date2 != null) {
            newDate = newformat.format(date2);
        }

        holder.tv_date.setText(newDate);

        /*IMPORTANT*/


        //show view icon i.e count = 1
//        if (model.getCount() == 1) {
        if (model.getCount() > 0) {
            //evaluated
            holder.iv_view_evaluation.setVisibility(View.VISIBLE);
            holder.btn_evaluate.setVisibility(View.GONE);
        }
        //show evaluate button i.e count = 0
        else if (model.getCount() == 0) {
            //not evaluated
            holder.iv_view_evaluation.setVisibility(View.GONE);
            holder.btn_evaluate.setVisibility(View.VISIBLE);
        }


        //view icon
        holder.iv_view_evaluation.setOnClickListener(v -> {
            String qbType = model.getQuestion_bank_type();

            //mcq
            if (qbType.equals("mcq")) {
                evaluationQuestionsList.clear();
                EvaluationStudentListModel model12 = evaluationStudentList.get(position);
                HashMap<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("student_id", model12.getStudentId());
                params.put("question_bank_id", model12.getQuestion_bank_id());
                System.out.println("MCQ_EVALUATE_PARAMS" + params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                        "/evaluate_question_bank", new JSONObject(params), response -> {
                    System.out.println("MCQ_RESPONSE" + response);
                    try {
                        if (response.getString("status").equals("true")) {
                            JSONArray jsonArray = response.getJSONArray("Question_details");
                            Log.d("QUESTIONS_ARRAY", String.valueOf(jsonArray));

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                marks_obt = jsonObject.getString("marks_obt") == null ? "" :
                                        jsonObject.getString("marks_obt");

                                qb_id = jsonObject.getString("qb_id");
                                question_bank_id = jsonObject.getString("question_bank_id");
                                question_id = jsonObject.getString("question_id");
                                qb_name = jsonObject.getString("qb_name");
                                question_type = jsonObject.getString("question_type");
                                question = jsonObject.getString("question");
                                weightage = jsonObject.getString("weightage");
                                status = jsonObject.getString("status");
                                String ans = jsonObject.getString("answer");
                                String correct_ans = jsonObject.getString("correct_answer");

                                //Question paper
                                String ques_papers = jsonObject.getString("image");

                                //Answer paper Array
                                String ans_papers = jsonObject.getString("image_answer");

                                String download_ans = jsonObject.getString("download_url");
                                String download_ques = jsonObject.getString("download_url_for_questions");

                                EvaluationQuestionModel evaluationQuestionModel =
                                        new EvaluationQuestionModel(qb_id, question_bank_id, question_id,
                                                qb_name, question_type, question, weightage, status, ans,
                                                correct_ans, student_id, ques_papers, ans_papers, download_ans,
                                                download_ques, "true", "mcq",marks_obt);
                                evaluationQuestionsList.add(evaluationQuestionModel);
                                notifyDataSetChanged();
                            }

                            Intent intent = new Intent(context, EvaluationQuestionActivity.class);
                            intent.putExtra("qbank", evaluationQuestionsList);
                            intent.putExtra("student_id", student_id);
                            intent.putExtra("date", date);
                            intent.putExtra("viewOnly", "true");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        else {
                            Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    System.out.println("ERROR_RESPONSE - " + error.getMessage());
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(context);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue1.add(jsonObjectRequest);
            }

            //upload type
            else {
                EvaluationStudentListModel model1 = evaluationStudentList.get(position);
                HashMap<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("student_id", model1.getStudentId());
                params.put("question_bank_id", model1.getQuestion_bank_id());
                System.out.println("UPLOAD_EVALUATE_PARAMS" + params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                        "/get_upload_evaluate_data", new JSONObject(params), response -> {
                    System.out.println("UPLOAD_RESPONSE" + response);

                    try {
                        if (response.getString("status").equals("true")) {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                //Question paper attachments array
                                String ques_array = jsonObject.getString("Question_bank");

                                //Answer paper attachments array
                                String ans_array = jsonObject.getString("Answer");

                                url = jsonObject.getString("download_url");
                                marks_obt = jsonObject.getString("marks_obt") == null ? "" :
                                        jsonObject.getString("marks_obt");

                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                Log.d("data_array", dataArray.toString());
                                for (int l = 0; l < dataArray.length(); l++) {
                                    JSONObject dataObject = dataArray.getJSONObject(l);

                                    exam_id = dataObject.getString("exam_id");
                                    class_id = dataObject.getString("class_id");
                                    subject_id = dataObject.getString("subject_id");
                                    qb_name = dataObject.getString("qb_name");
                                    qb_type = dataObject.getString("qb_type");
                                    instructions = dataObject.getString("instructions");
                                    weightage = dataObject.getString("weightage");
                                    create_date = dataObject.getString("create_date");
                                    teacher_id = dataObject.getString("teacher_id");
                                    complete = dataObject.getString("complete");
                                    classname = dataObject.getString("classname");
                                    subjectname = dataObject.getString("subjectname");
                                    question_bank_id = dataObject.getString("question_bank_id");

                                }

                                UploadEvaluationQuestionModel uploadEvaluationQuestionModel =
                                        new UploadEvaluationQuestionModel(exam_id, class_id, subject_id, qb_name
                                                , qb_type, instructions, weightage, create_date, teacher_id,
                                                complete, ques_array, ans_array, classname, subjectname,
                                                question_bank_id, marks_obt, url);
                                uploadEvaluationQuestionsList.add(uploadEvaluationQuestionModel);
                                notifyDataSetChanged();
                            }

                            Intent intent = new Intent(context, PenPaperEvaluationActivity.class);
                            intent.putExtra("upload_model", uploadEvaluationQuestionsList);
                            intent.putExtra("student_id", student_id);
                            intent.putExtra("viewOnly", "true");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        else {
                            Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    System.out.println("ERROR_RESPONSE - " + error.getMessage());
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(context);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue1.add(jsonObjectRequest);
            }
        });

        //evaluate button
        holder.btn_evaluate.setOnClickListener(v -> {
            String qbType = model.getQuestion_bank_type();
            //mcq type
            if (qbType.equals("mcq")) {

                EvaluationStudentListModel model12 = evaluationStudentList.get(position);
                HashMap<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("student_id", model12.getStudentId());
                params.put("question_bank_id", model12.getQuestion_bank_id());
                System.out.println("MCQ_EVALUATE_PARAMS" + params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                        "/evaluate_question_bank", new JSONObject(params), response -> {
                    System.out.println("MCQ_RESPONSE" + response);
                    try {
                        if (response.getString("status").equals("true")) {
                            evaluationQuestionsList.clear();
                            JSONArray jsonArray = response.getJSONArray("Question_details");
                            Log.d("QUESTIONS_ARRAY", String.valueOf(jsonArray));

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                qb_id = jsonObject.getString("qb_id");
                                question_bank_id = jsonObject.getString("question_bank_id");
                                question_id = jsonObject.getString("question_id");
                                qb_name = jsonObject.getString("qb_name");
                                question_type = jsonObject.getString("question_type");
                                question = jsonObject.getString("question");
                                weightage = jsonObject.getString("weightage");
                                status = jsonObject.getString("status");
                                String ans = jsonObject.getString("answer");
                                String correct_ans = jsonObject.getString("correct_answer");

                                //Question paper array
                                String ques_papers = jsonObject.getString("image");

                                //Answer paper Array
                                String ans_papers = jsonObject.getString("image_answer");

                                //Download urls for ans_paper & ques_paper
                                String download_ans = jsonObject.getString("download_url");
                                String download_ques = jsonObject.getString("download_url_for_questions");

                                EvaluationQuestionModel evaluationQuestionModel =
                                        new EvaluationQuestionModel(qb_id, question_bank_id, question_id,
                                                qb_name, question_type, question, weightage, status, ans,
                                                correct_ans, student_id, ques_papers, ans_papers, download_ans,
                                                download_ques, "false", "mcq","");
                                evaluationQuestionsList.add(evaluationQuestionModel);
                            }

                            Intent intent = new Intent(context, EvaluationQuestionActivity.class);
                            intent.putExtra("qbank", evaluationQuestionsList);
                            intent.putExtra("student_id", student_id);
                            intent.putExtra("date", date);
                            intent.putExtra("viewOnly", "false");
                            intent.putExtra("question_bank_type", "mcq");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        else {
                            Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    System.out.println("ERROR_RESPONSE - " + error.getMessage());
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(context);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue1.add(jsonObjectRequest);
            }

            //upload type
            else {
                EvaluationStudentListModel model12 = evaluationStudentList.get(position);
                HashMap<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("student_id", model12.getStudentId());
                params.put("question_bank_id", model12.getQuestion_bank_id());
                System.out.println("UPLOAD_EVALUATE_PARAMS" + params);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(newUrl + "OnlineExamApi" +
                        "/get_upload_evaluate_data", new JSONObject(params), response -> {
                    System.out.println("UPLOAD_RESPONSE" + response);
                    try {
                        if (response.getString("status").equals("true")) {

                            evaluationQuestionsList.clear();
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                //Question paper attachments array
                             ques_array = jsonObject.getString("Question_bank");

                                //Answer paper attachments array
                             ans_array = jsonObject.getString("Answer");

                                //attachments download url
                                url = jsonObject.getString("download_url");

                                marks_obt = jsonObject.getString("marks_obt");

                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                Log.d("data_array", dataArray.toString());

                                for (int l = 0; l < dataArray.length(); l++) {
                                    JSONObject dataObject = dataArray.getJSONObject(l);

                                    exam_id = dataObject.getString("exam_id");
                                    class_id = dataObject.getString("class_id");
                                    subject_id = dataObject.getString("subject_id");
                                    qb_name = dataObject.getString("qb_name");
                                    qb_type = dataObject.getString("qb_type");
                                    instructions = dataObject.getString("instructions");
                                    weightage = dataObject.getString("weightage");
                                    create_date = dataObject.getString("create_date");
                                    teacher_id = dataObject.getString("teacher_id");
                                    complete = dataObject.getString("complete");
                                    classname = dataObject.getString("classname");
                                    subjectname = dataObject.getString("subjectname");
                                    question_bank_id = dataObject.getString("question_bank_id");
                                }
                            }
                            UploadEvaluationQuestionModel uploadEvaluationQuestionModel =
                                    new UploadEvaluationQuestionModel(exam_id,
                                            class_id, subject_id, qb_name, qb_type, instructions, weightage,
                                            create_date, teacher_id, complete, ques_array, ans_array, classname,
                                            subjectname, question_bank_id, marks_obt, url);
                            uploadEvaluationQuestionsList.add(uploadEvaluationQuestionModel);
                            notifyDataSetChanged();

                            Intent intent = new Intent(context, PenPaperEvaluationActivity.class);
                            intent.putExtra("upload_model", uploadEvaluationQuestionsList);
                            intent.putExtra("student_id", student_id);
                            intent.putExtra("viewOnly", "false");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        else {
                            Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    System.out.println("ERROR_RESPONSE - " + error.getMessage());
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(context);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue1.add(jsonObjectRequest);

            }
        });
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

    //Binding the views with components from layout file
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_rollNo, tv_studentName, tv_date;
        Button btn_evaluate;
        ImageView iv_view_evaluation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_rollNo = itemView.findViewById(R.id.tv_rollNo);
            tv_studentName = itemView.findViewById(R.id.tv_studentName);
            tv_date = itemView.findViewById(R.id.tv_date);
            btn_evaluate = itemView.findViewById(R.id.evaluate);
            iv_view_evaluation = itemView.findViewById(R.id.view_evaluation);
        }
    }
}

