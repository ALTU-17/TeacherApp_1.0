package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

/**
 * Created by "Manoj Waghmare" on 25,Aug,2020
 **/

public class EvaluationQuestionModel implements Serializable {

    public String qb_id,question_bank_id,question_id,qb_name,question_type,question,weightage,status,ans,
    correct_ans,student_id,marks_obt,ans_papers,ques_papers,download_url,download_url_for_questions,viewOnly,
            question_bank_type;


    public EvaluationQuestionModel(String qb_id, String question_bank_id, String question_id, String qb_name,
                                   String question_type, String question, String weightage, String status,String ans,
                                   String correct_ans,String student_id, String ques_papers, String ans_papers,
                                   String download_url,String download_url_for_questions) {
        this.qb_id = qb_id;
        this.question_bank_id = question_bank_id;
        this.question_id = question_id;
        this.qb_name = qb_name;
        this.question_type = question_type;
        this.question = question;
        this.weightage = weightage;
        this.status = status;
        this.ans = ans;
        this.correct_ans = correct_ans;
        this.student_id = student_id;
        this.ques_papers = ques_papers;
        this.ans_papers = ans_papers;
        this.download_url = download_url;
        this.download_url_for_questions = download_url_for_questions;
    }



    public EvaluationQuestionModel(String qb_id, String question_bank_id, String question_id, String qb_name, String question_type, String question, String weightage, String status, String ans,
                                   String correct_ans, String student_id, String ques_papers, String ans_papers, String download_ans, String download_ques, String viewOnly) {

        this.qb_id = qb_id;
        this.question_bank_id = question_bank_id;
        this.question_id = question_id;
        this.qb_name = qb_name;
        this.question_type = question_type;
        this.question = question;
        this.weightage = weightage;
        this.status = status;
        this.ans = ans;
        this.correct_ans = correct_ans;
        this.student_id = student_id;
        this.ans_papers = ans_papers;
        this.ques_papers = ques_papers;
        this.download_url = download_ans;
        this.download_url_for_questions = download_ques;
        this.viewOnly = viewOnly;
    }

    public EvaluationQuestionModel(String qb_id, String question_bank_id, String question_id, String qb_name, String question_type, String question, String weightage, String status, String ans,
                                   String correct_ans, String student_id, String ques_papers, String ans_papers,
                                   String download_ans, String download_ques, String viewOnly,
                                   String question_bank_type,String marks_obt) {

        this.qb_id = qb_id;
        this.question_bank_id = question_bank_id;
        this.question_id = question_id;
        this.qb_name = qb_name;
        this.question_type = question_type;
        this.question = question;
        this.weightage = weightage;
        this.status = status;
        this.ans = ans;
        this.correct_ans = correct_ans;
        this.student_id = student_id;
        this.ans_papers = ans_papers;
        this.ques_papers = ques_papers;
        this.download_url = download_ans;
        this.download_url_for_questions = download_ques;
        this.viewOnly = viewOnly;
        this.question_bank_type = question_bank_type;
        this.marks_obt = marks_obt;
    }

    public String getQuestion_bank_type() {
        return question_bank_type;
    }

    public void setQuestion_bank_type(String question_bank_type) {
        this.question_bank_type = question_bank_type;
    }

    public String getViewOnly() {
        return viewOnly;
    }

    public void setViewOnly(String viewOnly) {
        this.viewOnly = viewOnly;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getDownload_url_for_questions() {
        return download_url_for_questions;
    }

    public void setDownload_url_for_questions(String download_url_for_questions) {
        this.download_url_for_questions = download_url_for_questions;
    }

    public String getQues_papers() {
        return ques_papers;
    }

    public void setQues_papers(String ques_papers) {
        this.ques_papers = ques_papers;
    }

    public String getAns_papers() {
        return ans_papers;
    }

    public void setAns_papers(String ans_papers) {
        this.ans_papers = ans_papers;
    }

    public String getMarks_obt() {
        return marks_obt;
    }

    public void setMarks_obt(String marks_obt) {
        this.marks_obt = marks_obt;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getCorrect_ans() {
        return correct_ans;
    }

    public void setCorrect_ans(String correct_ans) {
        this.correct_ans = correct_ans;
    }

    public String getQb_id() {
        return qb_id;
    }

    public void setQb_id(String qb_id) {
        this.qb_id = qb_id;
    }

    public String getQuestion_bank_id() {
        return question_bank_id;
    }

    public void setQuestion_bank_id(String question_bank_id) {
        this.question_bank_id = question_bank_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getQb_name() {
        return qb_name;
    }

    public void setQb_name(String qb_name) {
        this.qb_name = qb_name;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getWeightage() {
        return weightage;
    }

    public void setWeightage(String weightage) {
        this.weightage = weightage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
