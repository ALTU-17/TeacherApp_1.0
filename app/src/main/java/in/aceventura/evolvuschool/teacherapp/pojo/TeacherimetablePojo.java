package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

public class TeacherimetablePojo implements Serializable {
    private String subject,periodno,classnm,sectionnm;

    public TeacherimetablePojo(String subject, String periodno, String classnm, String sectionnm) {
        this.subject = subject;
        this.periodno = periodno;
        this.classnm = classnm;
        this.sectionnm = sectionnm;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPeriodno() {
        return periodno;
    }

    public void setPeriodno(String periodno) {
        this.periodno = periodno;
    }

    public String getClassnm() {
        return classnm;
    }

    public void setClassnm(String classnm) {
        this.classnm = classnm;
    }

    public String getSectionnm() {
        return sectionnm;
    }

    public void setSectionnm(String sectionnm) {
        this.sectionnm = sectionnm;
    }
}
