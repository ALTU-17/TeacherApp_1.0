package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

public class ViewedByListPojo implements Serializable {
    private String rollno, fName, lName, student_id, class_id, section_id,parent_id, read_status;



    public ViewedByListPojo(String rollno, String fName, String lName, String student_id, String class_id, String section_id, String parent_id, String read_status) {
        this.rollno = rollno;
        this.fName = fName;
        this.lName = lName;
        this.student_id = student_id;
        this.class_id = class_id;
        this.section_id = section_id;
        this.parent_id = parent_id;
        this.read_status = read_status;
    }
    public ViewedByListPojo(String rollno, String fName,String mName, String lName, String read_status) {
        this.rollno = rollno;
        this.fName = fName;
        this.lName = mName;
        this.lName = lName;
        this.read_status = read_status;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
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

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }
}
