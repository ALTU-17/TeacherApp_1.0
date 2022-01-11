package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

/**
 * Created by preeti on 5/6/18.
 */

public class TeacherNoteePojo implements Serializable {
    private String notes_id, date, classnm, teacher_id, section_id, subjectnm, description, classname, sectionname, subjectname, academic_yr, publish, pDate,class_id;
    private boolean isExpanded;

    public TeacherNoteePojo(String notes_id, String date, String classnm, String teacher_id, String section_id, String subjectnm, String description, String classname, String sectionname, String subjectname, String academic_yr, String publish, String pDate, boolean isExpanded, String class_id, String sectionId) {
        this.notes_id = notes_id;
        this.date = date;
        this.classnm = classnm;
        this.teacher_id = teacher_id;
        this.section_id = section_id;
        this.subjectnm = subjectnm;
        this.description = description;
        this.classname = classname;
        this.sectionname = sectionname;
        this.subjectname = subjectname;
        this.academic_yr = academic_yr;
        this.publish = publish;
        this.isExpanded = isExpanded;
        this.pDate = pDate;
        this.class_id = class_id;
        this.section_id = section_id;
    }

    public boolean getExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getpDate() {
        return pDate;
    }

    public void setpDate(String pDate) {
        this.pDate = pDate;
    }


    public String getSectionname() {
        return sectionname;
    }

    public void setSectionname(String sectionname) {
        this.sectionname = sectionname;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getNotes_id() {
        return notes_id;
    }

    public void setNotes_id(String notes_id) {
        this.notes_id = notes_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClassnm() {
        return classnm;
    }

    public void setClassnm(String classnm) {
        this.classnm = classnm;
    }

    public String getSubjectnm() {
        return subjectnm;
    }

    public void setSubjectnm(String subjectnm) {
        this.subjectnm = subjectnm;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getAcademic_yr() {
        return academic_yr;
    }

    public void setAcademic_yr(String academic_yr) {
        this.academic_yr = academic_yr;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }
}
