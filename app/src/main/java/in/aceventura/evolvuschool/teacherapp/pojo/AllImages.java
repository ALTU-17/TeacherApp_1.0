package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

public class AllImages implements Serializable{
    private String noticeid,imagename;

    public AllImages(String noticeid, String imagename) {
        this.noticeid = noticeid;
        this.imagename = imagename;
    }

    public String getNoticeid() {

        return noticeid;
    }

    public void setNoticeid(String noticeid) {
        this.noticeid = noticeid;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }
}
