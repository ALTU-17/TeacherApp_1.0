package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

/**
 * Created by abhinav on 5/2/18.
 */

public class AddStudentBean implements Serializable {
    private String id,subject,noticedate,createdby;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNoticedate() {
        return noticedate;
    }

    public void setNoticedate(String noticedate) {
        this.noticedate = noticedate;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }
}
