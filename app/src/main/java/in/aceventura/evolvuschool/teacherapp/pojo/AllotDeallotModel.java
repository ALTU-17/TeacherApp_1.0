package in.aceventura.evolvuschool.teacherapp.pojo;

/**
 * Created by "Manoj Waghmare" on 04,Sep,2020
 **/
public class AllotDeallotModel {
    String question_bank_id, className, division, subjectName, class_id, section_id, subject_id, teacher_id, status, academic_yr, questionBankName, exam_id, qb_type, instructions, weightage, create_date, complete, sm_id;

    public AllotDeallotModel(String question_bank_id, String class_id, String section_id, String subject_id, String teacher_id, String status, String academic_yr, String exam_id, String questionBankName, String qb_type, String instructions, String weightage, String create_date, String complete, String className, String division, String sm_id, String subjectName) {
        this.question_bank_id = question_bank_id;
        this.class_id = class_id;
        this.section_id = section_id;
        this.subject_id = subject_id;
        this.teacher_id = teacher_id;
        this.status = status;
        this.academic_yr = academic_yr;
        this.questionBankName = questionBankName;
        this.exam_id = exam_id;
        this.qb_type = qb_type;
        this.instructions = instructions;
        this.weightage = weightage;
        this.create_date = create_date;
        this.complete = complete;
        this.sm_id = sm_id;
        this.className = className;
        this.division = division;
        this.subjectName = subjectName;
    }

    public String getQuestion_bank_id() {
        return question_bank_id;
    }

    public void setQuestion_bank_id(String question_bank_id) {
        this.question_bank_id = question_bank_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAcademic_yr() {
        return academic_yr;
    }

    public void setAcademic_yr(String academic_yr) {
        this.academic_yr = academic_yr;
    }

    public String getQuestionBankName() {
        return questionBankName;
    }

    public void setQuestionBankName(String questionBankName) {
        this.questionBankName = questionBankName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
