package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

public class Timetablepojo implements Serializable {
    private String acdyr,teacherid,sectioname,classname,subject,colnmname;
    private String classIn, classOut, satClassIn, satClassOut, ttMonday, ttTuesday, ttWednesday, ttThursday, ttFriday, ttSaturday  ;
    private Integer periodNo;

    public String getClassIn() {
        return classIn;
    }

    public void setClassIn(String classIn) {
        this.classIn = classIn;
    }

    public String getClassOut() {
        return classOut;
    }

    public void setClassOut(String classOut) {
        this.classOut = classOut;
    }

    public String getSatClassIn() {
        return satClassIn;
    }

    public void setSatClassIn(String satClassIn) {
        this.satClassIn = satClassIn;
    }

    public String getSatClassOut() {
        return satClassOut;
    }

    public void setSatClassOut(String satClassOut) {
        this.satClassOut = satClassOut;
    }

    public String getTtMonday() {
        return ttMonday;
    }

    public void setTtMonday(String ttMonday) {
        this.ttMonday = ttMonday;
    }

    public String getTtTuesday() {
        return ttTuesday;
    }

    public void setTtTuesday(String ttTuesday) {
        this.ttTuesday = ttTuesday;
    }

    public String getTtWednesday() {
        return ttWednesday;
    }

    public void setTtWednesday(String ttWednesday) {
        this.ttWednesday = ttWednesday;
    }

    public String getTtThursday() {
        return ttThursday;
    }

    public void setTtThursday(String ttThursday) {
        this.ttThursday = ttThursday;
    }

    public String getTtFriday() {
        return ttFriday;
    }

    public void setTtFriday(String ttFriday) {
        this.ttFriday = ttFriday;
    }

    public String getTtSaturday() {
        return ttSaturday;
    }

    public void setTtSaturday(String ttSaturday) {
        this.ttSaturday = ttSaturday;
    }

    public Integer getPeriodNo() {
        return periodNo;
    }

    public void setPeriodNo(Integer periodNo) {
        this.periodNo = periodNo;
    }
/* public Timetablepojo(String acdyr, String teacherid, String periodno, String sectioname, String classname, String colnmname, String subject) {
        this.acdyr = acdyr;
        this.teacherid = teacherid;
        this.periodno = periodno;
        this.sectioname = sectioname;
        this.classname = classname;
        this.colnmname = colnmname;
        this.subject = subject;
    }*/

    public Timetablepojo(String sectioname, String classname, String subject) {
        this.sectioname = sectioname;
        this.classname = classname;
        this.subject = subject;
    }

    public String getAcdyr() {
        return acdyr;
    }

    public void setAcdyr(String acdyr) {
        this.acdyr = acdyr;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }



    public String getSectioname() {
        return sectioname;
    }

    public void setSectioname(String sectioname) {
        this.sectioname = sectioname;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getColnmname() {
        return colnmname;
    }

    public void setColnmname(String colnmname) {
        this.colnmname = colnmname;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
