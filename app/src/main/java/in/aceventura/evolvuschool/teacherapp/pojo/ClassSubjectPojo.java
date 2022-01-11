package in.aceventura.evolvuschool.teacherapp.pojo;

/**
 * Created by "Manoj Waghmare" on 25,Aug,2020
 **/
public class ClassSubjectPojo {
    String section_name,class_name,class_id,section_id,sm_id,subject_name;

    /*public ClassSubjectPojo( String section_name,String class_name, String class_id, String section_id) {
        this.section_name = section_name;
        this.class_name = class_name;
        this.class_id = class_id;
        this.section_id = section_id;
    }*/

    public ClassSubjectPojo(String class_name,String sm_id, String subject_name,  String class_id) {
        this.sm_id = sm_id;
        this.class_name = class_name;
        this.subject_name=subject_name;
        this.class_id = class_id;
        /*this.section_name = section_name;
        this.section_id = section_id;*/
    }

    public ClassSubjectPojo(String sm_id, String subject_name, String class_name, String class_id, String section_id, String section_name) {
        this.sm_id = sm_id;
        this.class_name = class_name;
        this.subject_name=subject_name;
        this.class_id = class_id;
        this.section_name = section_name;
        this.section_id = section_id;
    }

    public String getSm_id() {
        return sm_id;
    }

    public void setSm_id(String sm_id) {
        this.sm_id = sm_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
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

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }
}
