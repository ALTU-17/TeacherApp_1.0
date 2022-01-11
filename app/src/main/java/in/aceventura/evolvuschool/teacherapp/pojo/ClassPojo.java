package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

public class ClassPojo implements Serializable {
    private String classid,sectionid,classname,sectionname;
    private boolean ischecked;
    private int checkedId;

/*
    public ClassPojo(String a, boolean b) {

    }
*/

    public ClassPojo(String classid, String sectionid, String classname, String sectionname, boolean ischecked, int checkedId) {
        this.classid = classid;
        this.sectionid = sectionid;
        this.classname = classname;
        this.sectionname = sectionname;
        this.ischecked = ischecked;
        this.checkedId = checkedId;
    }

    public int getCheckedId() {
        return checkedId;
    }

    public void setCheckedId(int checkedId) {
        this.checkedId = checkedId;
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

    public boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }
}
