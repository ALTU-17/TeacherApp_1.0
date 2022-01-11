package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

public class Checkbalance implements Serializable {
    private String leaveid,leavetype,staffid,leavesalloted,leavesavailed;

    public Checkbalance(String leaveid, String leavetype, String staffid, String leavesalloted, String leavesavailed) {
        this.leaveid = leaveid;
        this.leavetype = leavetype;
        this.staffid = staffid;
        this.leavesalloted = leavesalloted;
        this.leavesavailed = leavesavailed;
    }

    public String getLeaveid() {
        return leaveid;
    }

    public void setLeaveid(String leaveid) {
        this.leaveid = leaveid;
    }

    public String getLeavetype() {
        return leavetype;
    }

    public void setLeavetype(String leavetype) {
        this.leavetype = leavetype;
    }

    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid;
    }

    public String getLeavesalloted() {
        return leavesalloted;
    }

    public void setLeavesalloted(String leavesalloted) {
        this.leavesalloted = leavesalloted;
    }

    public String getLeavesavailed() {
        return leavesavailed;
    }

    public void setLeavesavailed(String leavesavailed) {
        this.leavesavailed = leavesavailed;
    }
}
