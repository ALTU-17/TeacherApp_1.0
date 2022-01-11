package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

public class NoticeStaffPojo implements Serializable {
    private String notice_id,subject,notice_desc,notice_date,start_date,end_date,class_id,section_id,notice_type,teacher_id,
            start_time,end_time,academic_yr,publish,name,imagename,classname,detailnoticeid;
/*

    public NoticeStaffPojo(String notice_id, String subject, String notice_desc, String notice_date, String start_date, String end_date, String class_id, String section_id, String notice_type, String teacher_id, String start_time, String end_time, String academic_yr, String publish, String name,String classname) {
        this.notice_id = notice_id;
        this.subject = subject;
        this.notice_desc = notice_desc;
        this.notice_date = notice_date;
        this.start_date = start_date;
        this.end_date = end_date;
        this.class_id = class_id;
        this.section_id = section_id;
        this.notice_type = notice_type;
        this.teacher_id = teacher_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.academic_yr = academic_yr;
        this.publish = publish;
        this.name = name;
        this.classname=classname;
    }


    public NoticeStaffPojo(String noticeId, String s, String notice_desc, String reqNoticedate, String notice_type, String teacher_id, String academic_yr, String notice_id, String subject) {
        this.notice_id = notice_id;
        this.subject = subject;

    }
*/

    public NoticeStaffPojo(String notice_id, String subject, String notice_desc, String reqNoticedate, String notice_type,String name) {
        this.notice_id = notice_id;
        this.subject = subject;
        this.name = name;
        this.notice_desc = notice_desc;
        this.notice_date = reqNoticedate;
        this.notice_type = notice_type;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(String notice_id) {
        this.notice_id = notice_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNotice_desc() {
        return notice_desc;
    }

    public void setNotice_desc(String notice_desc) {
        this.notice_desc = notice_desc;
    }

    public String getNotice_date() {
        return notice_date;
    }

    public void setNotice_date(String notice_date) {
        this.notice_date = notice_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
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

    public String getNotice_type() {
        return notice_type;
    }

    public void setNotice_type(String notice_type) {
        this.notice_type = notice_type;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getAcademic_yr() {
        return academic_yr;
    }

    public void setAcademic_yr(String academic_yr) {
        this.academic_yr = academic_yr;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}