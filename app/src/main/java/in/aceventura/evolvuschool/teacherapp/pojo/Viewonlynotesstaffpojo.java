package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

public class Viewonlynotesstaffpojo implements Serializable {
    private String notesid,subjectname,sectionname,clasname,notice_desc,notes_date,classid,sectionid,subjectid,publish;

    public Viewonlynotesstaffpojo(String notesid, String subjectname, String sectionname, String clasname, String notice_desc, String notes_date, String classid, String sectionid, String subjectid, String publish) {
        this.notesid = notesid;
        this.subjectname = subjectname;
        this.sectionname = sectionname;
        this.clasname = clasname;
        this.notice_desc = notice_desc;
        this.notes_date = notes_date;
        this.classid = classid;
        this.sectionid = sectionid;
        this.subjectid = subjectid;
        this.publish = publish;

        }

    public String getNotesid() {
        return notesid;
    }

    public void setNotesid(String notesid) {
        this.notesid = notesid;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getSectionname() {
        return sectionname;
    }

    public void setSectionname(String sectionname) {
        this.sectionname = sectionname;
    }

    public String getClasname() {
        return clasname;
    }

    public void setClasname(String clasname) {
        this.clasname = clasname;
    }

    public String getNotice_desc() {
        return notice_desc;
    }

    public void setNotice_desc(String notice_desc) {
        this.notice_desc = notice_desc;
    }

    public String getNotes_date() {
        return notes_date;
    }

    public void setNotes_date(String notes_date) {
        this.notes_date = notes_date;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getSectionid() {
        return sectionid;
    }

    public void setSectionid(String sectionid) {
        this.sectionid = sectionid;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }
}
