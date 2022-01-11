package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

public class ViewOnlyHmkPojo implements Serializable {
    private String hmkid,subject,classname,sectionname,desc,assigndate,submitdate,classid,sectionid,subjectid,publish,pubDate;


    public ViewOnlyHmkPojo(String hmkid, String subject, String classname, String sectionname, String desc, String assigndate, String submitdate, String classid, String sectionid, String subjectid, String publish, String pubDate) {
        this.hmkid = hmkid;
        this.subject = subject;
        this.classname = classname;
        this.sectionname = sectionname;
        this.desc = desc;
        this.assigndate = assigndate;
        this.submitdate = submitdate;
        this.classid = classid;
        this.sectionid = sectionid;
        this.subjectid = subjectid;
        this.publish = publish;
        this.pubDate = pubDate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getHmkid() {
        return hmkid;
    }

    public void setHmkid(String hmkid) {
        this.hmkid = hmkid;
    }

    public String getAssigndate() {
        return assigndate;
    }

    public void setAssigndate(String assigndate) {
        this.assigndate = assigndate;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getSubmitdate() {
        return submitdate;
    }

    public void setSubmitdate(String submitdate) {
        this.submitdate = submitdate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSectionname() {
        return sectionname;
    }

    public void setSectionname(String sectionname) {
        this.sectionname = sectionname;
    }
}
