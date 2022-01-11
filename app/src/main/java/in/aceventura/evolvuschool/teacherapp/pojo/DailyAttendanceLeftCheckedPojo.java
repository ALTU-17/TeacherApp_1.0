package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

public class DailyAttendanceLeftCheckedPojo implements Serializable {
    private String date;
    private String classid;
    private String sectionid;
    private String rollno;

    private String studentid;
    private String fName;
    private String lName;
    private  boolean checkedStatus;
    private String attendencestatus;

    public DailyAttendanceLeftCheckedPojo(String rollno, String fName, String lName, String studid, String attendencestatus, boolean checkedStatus) {
        this.rollno = rollno;
        this.fName = fName;
        this.lName = lName;
        this.checkedStatus = checkedStatus;
        this.studentid = studid;
        this.attendencestatus =attendencestatus;
    }


    public DailyAttendanceLeftCheckedPojo(String rollno, String fName, String lName, String studid, boolean checkedStatus) {
        this.rollno = rollno;
        this.fName = fName;
        this.lName = lName;
        this.checkedStatus = checkedStatus;
        this.studentid = studid;
    }

    public DailyAttendanceLeftCheckedPojo(){

    }

    public String getAttendencestatus() {
        return attendencestatus;
    }

    public void setAttendencestatus(String attendencestatus) {
        this.attendencestatus = attendencestatus;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
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


    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }


    public boolean isCheckedStatus() {
        return checkedStatus;
    }

    public void setCheckedStatus(boolean checkedStatus) {
        this.checkedStatus = checkedStatus;
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

}
