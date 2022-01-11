package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

/**
 * Created by "Manoj Waghmare" on 03,Sep,2020
 **/
public class QbankModel implements Serializable {
    String questionBankName, createdOn, status, question_bank_id, exam_id, class_id, subject_id, qbtype,
    instructions, weightage, teacher_id, complete, academic_yr, allotted_answered, class_name, subject_name,
            exam_name,reg_id;

    public QbankModel(String questionBankName, String createdOn, String status, String question_bank_id, String exam_id, String class_id, String subject_id, String qbtype, String instructions, String weightage, String teacher_id, String complete, String academic_yr, String allotted_answered, String class_name, String subject_name, String exam_name) {
        this.questionBankName = questionBankName;
        this.createdOn = createdOn;
        this.status = status;
        this.question_bank_id = question_bank_id;
        this.exam_id = exam_id;
        this.class_id = class_id;
        this.subject_id = subject_id;
        this.qbtype = qbtype;
        this.instructions = instructions;
        this.weightage = weightage;
        this.teacher_id = teacher_id;
        this.complete = complete;
        this.academic_yr = academic_yr;
        this.allotted_answered = allotted_answered;
        this.class_name = class_name;
        this.subject_name = subject_name;
        this.exam_name = exam_name;
    }

    public QbankModel(String questionBankName, String createdOn, String status, String question_bank_id,
                      String exam_id, String class_id, String subject_id, String qbtype, String instructions,
                      String weightage, String teacher_id, String complete, String academic_yr,
                      String allotted_answered, String class_name, String subject_name, String exam_name,
                      String reg_id) {
        this.questionBankName = questionBankName;
        this.createdOn = createdOn;
        this.status = status;
        this.question_bank_id = question_bank_id;
        this.exam_id = exam_id;
        this.class_id = class_id;
        this.subject_id = subject_id;
        this.qbtype = qbtype;
        this.instructions = instructions;
        this.weightage = weightage;
        this.teacher_id = teacher_id;
        this.complete = complete;
        this.academic_yr = academic_yr;
        this.allotted_answered = allotted_answered;
        this.class_name = class_name;
        this.subject_name = subject_name;
        this.exam_name = exam_name;
        this.reg_id = reg_id;
    }

    public String getReg_id() {
        return reg_id;
    }

    public void setReg_id(String reg_id) {
        this.reg_id = reg_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public String getQuestionBankName() {
        return questionBankName;
    }

    public void setQuestionBankName(String questionBankName) {
        this.questionBankName = questionBankName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuestion_bank_id() {
        return question_bank_id;
    }

    public void setQuestion_bank_id(String question_bank_id) {
        this.question_bank_id = question_bank_id;
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

    public String getQbtype() {
        return qbtype;
    }

    public void setQbtype(String qbtype) {
        this.qbtype = qbtype;
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

    public String getAcademic_yr() {
        return academic_yr;
    }

    public void setAcademic_yr(String academic_yr) {
        this.academic_yr = academic_yr;
    }

    public String getAllotted_answered() {
        return allotted_answered;
    }

    public void setAllotted_answered(String allotted_answered) {
        this.allotted_answered = allotted_answered;
    }
}
