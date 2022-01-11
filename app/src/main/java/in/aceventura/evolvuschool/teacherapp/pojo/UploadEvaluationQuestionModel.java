package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

/**
 * Created by "Manoj Waghmare" on 11,Sep,2020
 **/
public class UploadEvaluationQuestionModel implements Serializable {
    /*String up_id, student_id, question_bank_id, date, academic_yr,
            ques_image_name, ques_file_size, uploaded_qp_id, ans_image_name, ans_file_size,
            exam_id, class_id, subject_id, qb_name, qb_type, instructions, weightage, create_date,
            teacher_id, complete;*/

    String exam_id, class_id, subject_id,
            qb_name, qb_type, instructions,
            weightage, create_date,
            teacher_id, complete, ques_array,
            ans_array,classname,subjectname,question_bank_id,marks_obt,download_url;


    public UploadEvaluationQuestionModel(String exam_id, String class_id, String subject_id,
                                         String qb_name, String qb_type, String instructions,
                                         String weightage, String create_date,
                                         String teacher_id, String complete,
                                         String ques_array, String ans_array) {
        this.exam_id = exam_id;
        this.class_id = class_id;
        this.subject_id = subject_id;
        this.qb_name = qb_name;
        this.qb_type = qb_type;
        this.instructions = instructions;
        this.weightage = weightage;
        this.create_date = create_date;
        this.teacher_id = teacher_id;
        this.complete = complete;
        this.ques_array = ques_array;
        this.ans_array = ans_array;
    }

    public UploadEvaluationQuestionModel(String exam_id, String class_id, String subject_id,
                                         String qb_name, String qb_type, String instructions,
                                         String weightage, String create_date,
                                         String teacher_id, String complete,
                                         String ques_array, String ans_array,
                                         String classname, String subjectname,
                                         String question_bank_id,String marks_obt,String download_url) {
        this.exam_id = exam_id;
        this.class_id = class_id;
        this.subject_id = subject_id;
        this.qb_name = qb_name;
        this.qb_type = qb_type;
        this.instructions = instructions;
        this.weightage = weightage;
        this.create_date = create_date;
        this.teacher_id = teacher_id;
        this.complete = complete;
        this.ques_array = ques_array;
        this.ans_array = ans_array;
        this.classname = classname;
        this.subjectname = subjectname;
        this.question_bank_id = question_bank_id;
        this.marks_obt = marks_obt;
        this.download_url = download_url;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getMarks_obt() {
        return marks_obt;
    }

    public void setMarks_obt(String marks_obt) {
        this.marks_obt = marks_obt;
    }

    public String getQuestion_bank_id() {
        return question_bank_id;
    }

    public void setQuestion_bank_id(String question_bank_id) {
        this.question_bank_id = question_bank_id;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getQues_array() {
        return ques_array;
    }

    public void setQues_array(String ques_array) {
        this.ques_array = ques_array;
    }

    public String getAns_array() {
        return ans_array;
    }

    public void setAns_array(String ans_array) {
        this.ans_array = ans_array;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getQb_name() {
        return qb_name;
    }

    public void setQb_name(String qb_name) {
        this.qb_name = qb_name;
    }

    public String getQb_type() {
        return qb_type;
    }

    public void setQb_type(String qb_type) {
        this.qb_type = qb_type;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getWeightage() {
        return weightage;
    }

    public void setWeightage(String weightage) {
        this.weightage = weightage;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

}
