package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

/**
 * Created by Admin on 7/5/2018.
 */

public class HomeworkPojo implements Serializable {

    private String classname,subjectnm,assigndate,submissiondate,homeworkid,classid,teacherid,sectionid,subjectid,description,acdyr,publish,sectionname,publishdate,parent_comment_count;
    private boolean isExpanded;

    public HomeworkPojo(String classname, String subjectnm, String assigndate, String submissiondate, String homeworkid, String classid, String teacherid, String sectionid, String subjectid, String description, String acdyr, String publish, String sectionname, String publishdate, boolean expanded,String parent_comment_count) {
        this.classname = classname;
        this.subjectnm = subjectnm;
        this.assigndate = assigndate;
        this.submissiondate = submissiondate;
        this.homeworkid = homeworkid;
        this.classid = classid;
        this.teacherid = teacherid;
        this.sectionid = sectionid;
        this.subjectid = subjectid;
        this.description = description;
        this.acdyr = acdyr;
        this.publish = publish;
        this.sectionname = sectionname;
        this.isExpanded = expanded;
        this.publishdate = publishdate;
        this.parent_comment_count = parent_comment_count;
    }

    public String getParent_comment_count() {
        return parent_comment_count;
    }

    public void setParent_comment_count(String parent_comment_count) {
        this.parent_comment_count = parent_comment_count;
    }

    public String getHomeworkid() {
        return homeworkid;
    }

    public void setHomeworkid(String homeworkid) {
        this.homeworkid = homeworkid;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAcdyr() {
        return acdyr;
    }

    public void setAcdyr(String acdyr) {
        this.acdyr = acdyr;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getSectionname() {
        return sectionname;
    }

    public void setSectionname(String sectionname) {
        this.sectionname = sectionname;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSubjectnm() {
        return subjectnm;
    }

    public void setSubjectnm(String subjectnm) {
        this.subjectnm = subjectnm;
    }

    public String getAssigndate() {
        return assigndate;
    }

    public void setAssigndate(String assigndate) {
        this.assigndate = assigndate;
    }

    public String getSubmissiondate() {
        return submissiondate;
    }

    public void setSubmissiondate(String submissiondate) {
        this.submissiondate = submissiondate;
    }

    public String getPublishdate() {
        return publishdate;
    }

    public void setPublishdate(String publishdate) {
        this.publishdate = publishdate;
    }

    public boolean getExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
