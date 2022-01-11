package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

/**
 * Created by Admin on 7/12/2018.
 */

public class ViewHmkPojo implements Serializable {
  /* private String homework_id,description,teacher_id,section_id,sm_id,class_id,end_date,start_date,academic_yr,publish,
            comment_id,student_id,parent_id,homework_status,comment,parent_comment,first_name,mid_name,last_name,dob,
            gender,admission_date,roll_no,fees_paid,blood_group,religion,caste,subcaste,transport_mode,vehicle_no,
           bus_id,emergency_name,emergency_contact,emergency_add,height,weight,allergies,nationality,permant_add,city,
           state,pincode,IsDelete,isPromoted,isNew,isModify,reg_no,house,stu_aadhaar_no,category,last_date,slc_no,
           slc_issue_date,leaving_remark;*/




    private String studFname,studLname,parentcomment,teachercommet,homework_status,studId,homework_id;
    private  boolean checkedStatus;



    public ViewHmkPojo(String studFname, String studLname, String parentcomment, String teachercommet, String homework_status, String studId, String homework_id, boolean checked_status) {
        this.studFname = studFname;
        this.studLname = studLname;
        this.parentcomment = parentcomment;
        this.teachercommet = teachercommet;
        this.homework_status = homework_status;
        this.studId = studId;
        this.homework_id = homework_id;
        this.checkedStatus = checked_status;
    }
    public  ViewHmkPojo(){

    }

    public boolean getCheckedStatus() {
        return checkedStatus;
    }

    public void setCheckedStatus(boolean checkedStatus) {
        this.checkedStatus = checkedStatus;
    }
    public String getStudFname() {
        return studFname;
    }

    public void setStudFname(String studFname) {
        this.studFname = studFname;
    }

    public String getStudLname() {
        return studLname;
    }

    public void setStudLname(String studLname) {
        this.studLname = studLname;
    }

    public String getParentcomment() {
        return parentcomment;
    }

    public void setParentcomment(String parentcomment) {
        this.parentcomment = parentcomment;
    }

    public String getTeachercommet() {
        return teachercommet;
    }

    public void setTeachercommet(String teachercommet) {
        this.teachercommet = teachercommet;
    }

    public String getHomework_status() {
        return homework_status;
    }

    public void setHomework_status(String homework_status) {
        this.homework_status = homework_status;
    }

    public String getStudId() {
        return studId;
    }

    public void setStudId(String studId) {
        this.studId = studId;
    }

    public String getHomework_id() {
        return homework_id;
    }

    public void setHomework_id(String homework_id) {
        this.homework_id = homework_id;
    }
}

