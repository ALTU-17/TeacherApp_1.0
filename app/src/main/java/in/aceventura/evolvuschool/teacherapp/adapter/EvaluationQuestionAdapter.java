package in.aceventura.evolvuschool.teacherapp.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.teacherapp.pojo.AnsPaperModel;
import in.aceventura.evolvuschool.teacherapp.pojo.EvaluationQuestionModel;
import in.aceventura.evolvuschool.teacherapp.pojo.QuesPaperModel;

import static in.aceventura.evolvuschool.teacherapp.R.layout.evaluation_que_item;

/**
 * Created by "Manoj Waghmare" on 25,Aug,2020
 **/
public class EvaluationQuestionAdapter extends RecyclerView.Adapter<EvaluationQuestionAdapter.ViewHolder> {

    String name, newUrl, dUrl, ques_paper_attachments, ans_paper_attachments;
    QuestionPaperAdapter questionPaperAdapter;
    AnswerPaperAdapter answerPaperAdapter;
    private Context context;
    private ArrayList<EvaluationQuestionModel> evaluationQuestionsList;
    private List<QuesPaperModel> qpList = new ArrayList<>();
    private List<AnsPaperModel> apList = new ArrayList<>();

    //constructor
    public EvaluationQuestionAdapter(Context context, ArrayList<EvaluationQuestionModel> evaluationQuestionsList) {
        this.context = context;
        this.evaluationQuestionsList = evaluationQuestionsList;
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
    public EvaluationQuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(evaluation_que_item, parent, false));
    }

    //Logic & Setting the values to UI
    @Override
    public void onBindViewHolder(@NonNull EvaluationQuestionAdapter.ViewHolder holder, int position) {
        EvaluationQuestionModel evaluationQuestionModel = evaluationQuestionsList.get(position);

        holder.question_no.setText(String.format("%s .", evaluationQuestionModel.getQuestion_id()));
        holder.question.setText(evaluationQuestionModel.getQuestion());
        holder.marks.setText(String.format("Marks %s", evaluationQuestionModel.getWeightage()));
        holder.ans.setText(!evaluationQuestionModel.getAns().equals("null") ?
                evaluationQuestionModel.getAns().replace("[", "").replace("]", "") : "");
        holder.correct_ans.setText(!evaluationQuestionModel.getCorrect_ans().equals("null") ?
                evaluationQuestionModel.getCorrect_ans() : "");

        String ques_type = evaluationQuestionModel.getQuestion_type();
        String ques_bank__type = evaluationQuestionModel.getQuestion_bank_type();

        ans_paper_attachments = evaluationQuestionsList.get(position).getAns_papers();


        JSONArray ans_papers = null;
        try {
            ans_papers = new JSONArray(ans_paper_attachments);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        if (ans_papers == null) {

            holder.answers.setVisibility(View.GONE);
            holder.rv_ansPaper.setVisibility(View.GONE);
            holder.ansPapersTextView.setVisibility(View.GONE);
        }else{
            holder.answers.setVisibility(View.VISIBLE);
            holder.rv_ansPaper.setVisibility(View.VISIBLE);
            holder.ansPapersTextView.setVisibility(View.VISIBLE);
        }

        //edit text are disabled for view only condition i.e evaluation is completed and teacher is only viewing
        // the screen.
        String viewOnly = evaluationQuestionModel.getViewOnly();

        if (viewOnly != null) {
            if (viewOnly.equals("true")) {
                holder.obtainedMarks.setEnabled(false);
            }
            else if (viewOnly.equals("false")) {
                holder.obtainedMarks.setEnabled(true);
            }
        }
        else {
            holder.obtainedMarks.setEnabled(true);
        }

        Log.d("ADAPTER", "" + position);


        final boolean b = ques_type.equals("trueorfalse") | ques_type.equals("objective");
        if (b && (holder.ans.getText().toString().equals(holder.correct_ans.getText().toString()))) {
            holder.correct_ans.setVisibility(View.VISIBLE);
            holder.tv_correct_answer.setVisibility(View.VISIBLE);
            holder.obtainedMarks.setText(evaluationQuestionModel.getWeightage());
            evaluationQuestionsList.get(position).setMarks_obt(evaluationQuestionModel.getWeightage());
            holder.rv_ansPaper.setVisibility(View.GONE);// TODO: 07-09-2020
        }
        else if (b && (!holder.ans.getText().toString().equals(holder.correct_ans.getText().toString()))) {
            holder.tv_correct_answer.setVisibility(View.VISIBLE);
            holder.correct_ans.setVisibility(View.VISIBLE);
            holder.obtainedMarks.setText("0");
            evaluationQuestionsList.get(position).setMarks_obt("0");
            holder.answers.setVisibility(View.GONE);// TODO: 07-09-2020
        }
        //subjective
        else {
            holder.tv_correct_answer.setVisibility(View.GONE);
            holder.correct_ans.setVisibility(View.GONE);
            holder.obtainedMarks.setText(evaluationQuestionModel.getMarks_obt());
            holder.rv_ansPaper.setVisibility(View.VISIBLE);// TODO: 07-09-2020
        }

        /*holder.obtainedMarks.setOnClickListener(view -> {

        });*/

        holder.obtainedMarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String obt_marks = holder.obtainedMarks.getText().toString();
                if (!obt_marks.isEmpty()) {
                    if (Integer.parseInt(obt_marks) > Integer.parseInt(evaluationQuestionModel.getWeightage())) {
                        Toast.makeText(context, "Marks cannot be more than weightage", Toast.LENGTH_SHORT).show();
                        holder.obtainedMarks.setText("");
                    }
                    else {
                        evaluationQuestionsList.get(position).setMarks_obt(holder.obtainedMarks.getText().toString());
                    }
                }
                else {
                    Toast.makeText(context, "Marks cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ques_paper_attachments = evaluationQuestionsList.get(position).getQues_papers();

        //Question Paper attachments
        JSONArray ques_papers = null;
        try {
            ques_papers = new JSONArray(ques_paper_attachments);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }


        String ques_download = evaluationQuestionsList.get(position).getDownload_url_for_questions();
        if (ques_papers != null && ques_papers.length() > 0) {
            qpList.clear();
            for (int j = 0; j < ques_papers.length(); j++) {
                try {
                    JSONObject quesObj = ques_papers.getJSONObject(j);
                    String question_bank_id = quesObj.getString("question_bank_id");
                    String image_name = quesObj.getString("image_name");
                    String ques_id = quesObj.getString("question_id");

                    if (ques_bank__type.equals("mcq")) {
                        QuesPaperModel qpModel = new QuesPaperModel(ques_id,question_bank_id,"", image_name,"",
                                ques_download,
                                ques_bank__type);
                        qpList.add(qpModel);
                    }
                    else {
                        QuesPaperModel qpModel = new QuesPaperModel(question_bank_id, image_name, ques_download);
                        qpList.add(qpModel);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            holder.rv_quesPaper.setVisibility(View.GONE);
        }


        String ans_download = evaluationQuestionsList.get(position).getDownload_url_for_questions();
        //Answer Paper attachments
        if (ans_papers != null && ans_papers.length() > 0) {
            apList.clear();
            for (int j = 0; j < ans_papers.length(); j++) {
                try {
                    JSONObject ansObj = ans_papers.getJSONObject(j);
                    String qb_id = ansObj.getString("qb_id");
                    String question_bank_id = ansObj.getString("question_bank_id");
                    String file_size = ansObj.getString("file_size");
                    String image_name = ansObj.getString("image_name");
//                    String ans_id = ansObj.getString("ans_id");


                    if (ques_bank__type.equals("mcq")) {
                        AnsPaperModel ansModel = new AnsPaperModel(qb_id, question_bank_id, file_size,
                                image_name, ans_download, ques_bank__type, "");
                        apList.add(ansModel);
                    }
                    else {
                        AnsPaperModel ansModel = new AnsPaperModel(qb_id, question_bank_id, file_size,
                                image_name, ans_download);
                        apList.add(ansModel);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            holder.rv_ansPaper.setVisibility(View.GONE);
            holder.ansPapersTextView.setVisibility(View.GONE);
        }

        questionPaperAdapter = new QuestionPaperAdapter(context, qpList);
        holder.rv_quesPaper.setAdapter(questionPaperAdapter);
        final LinearLayoutManager newsLinearLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);
        holder.rv_quesPaper.setLayoutManager(newsLinearLayoutManager);

        answerPaperAdapter = new AnswerPaperAdapter(context, apList);
        holder.rv_ansPaper.setAdapter(answerPaperAdapter);
        final LinearLayoutManager ansPaperLinearLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);
        holder.rv_ansPaper.setLayoutManager(ansPaperLinearLayoutManager);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    //setting count
    @Override
    public int getItemCount() {
        return evaluationQuestionsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    //Binding the views with components from layout file
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView question_no, question, marks, ans, correct_ans, tv_correct_answer, ansPapersTextView;
        EditText obtainedMarks;
        RecyclerView rv_ansPaper, rv_quesPaper;
        RelativeLayout answers;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            question_no = itemView.findViewById(R.id.question_no);
            question = itemView.findViewById(R.id.question);
            marks = itemView.findViewById(R.id.marks);
            ans = itemView.findViewById(R.id.ans);
            correct_ans = itemView.findViewById(R.id.correct_ans);
            obtainedMarks = itemView.findViewById(R.id.obtainedMarks);
            tv_correct_answer = itemView.findViewById(R.id.tv_correct_answer);
            rv_ansPaper = itemView.findViewById(R.id.rv_ansPaper);
            ansPapersTextView = itemView.findViewById(R.id.ansPapers);
            rv_quesPaper = itemView.findViewById(R.id.rv_quesPaper);
            answers = itemView.findViewById(R.id.answers);
        }
    }
}

