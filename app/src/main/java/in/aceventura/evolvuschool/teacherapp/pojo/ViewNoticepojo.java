package in.aceventura.evolvuschool.teacherapp.pojo;

import java.util.List;

public class ViewNoticepojo {
    private String notice_id,subject,notice_desc,notice_date,start_date,end_date,start_time,end_time;



/*
    public ViewNoticepojo(String notice_id, String subject, String notice_desc, String notice_date, String start_date, String end_date, String start_time, String end_time) {
        this.notice_id = notice_id;
        this.subject = subject;
        this.notice_desc = notice_desc;
        this.notice_date = notice_date;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_time = start_time;
        this.end_time = end_time;
    }
*/

    public ViewNoticepojo(String notice_id, String subject, String notice_desc, String notice_date, List<AllImages> allImagesList) {
        this.notice_id = notice_id;
        this.subject = subject;
        this.notice_desc = notice_desc;
        this.notice_date = notice_date;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.allImagesList = allImagesList;
    }

    public List<AllImages> allImagesList;


    public List<AllImages> getAllImagesList() {
        return allImagesList;
    }

    public void setAllImagesList(List<AllImages> allImagesList) {
        this.allImagesList = allImagesList;
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
}
