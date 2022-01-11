package in.aceventura.evolvuschool.teacherapp.pojo;

/**
 * Created by Admin on 7/10/2018.
 */

public class LeavePojo {

    private String leave_app_id,staff_id,leave_type_id,leave_start_date,leave_end_date,no_of_date,approved_by,status,reason,reason_for_rejection,academic_yr,name;

    public String getLeave_app_id() {
        return leave_app_id;
    }

    public void setLeave_app_id(String leave_app_id) {
        this.leave_app_id = leave_app_id;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getLeave_type_id() {
        return leave_type_id;
    }

    public void setLeave_type_id(String leave_type_id) {
        this.leave_type_id = leave_type_id;
    }

    public String getLeave_start_date() {
        return leave_start_date;
    }

    public void setLeave_start_date(String leave_start_date) {
        this.leave_start_date = leave_start_date;
    }

    public String getLeave_end_date() {
        return leave_end_date;
    }

    public void setLeave_end_date(String leave_end_date) {
        this.leave_end_date = leave_end_date;
    }

    public String getNo_of_date() {
        return no_of_date;
    }

    public void setNo_of_date(String no_of_date) {
        this.no_of_date = no_of_date;
    }

    public String getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(String approved_by) {
        this.approved_by = approved_by;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason_for_rejection() {
        return reason_for_rejection;
    }

    public void setReason_for_rejection(String reason_for_rejection) {
        this.reason_for_rejection = reason_for_rejection;
    }

    public String getAcademic_yr() {
        return academic_yr;
    }

    public void setAcademic_yr(String academic_yr) {
        this.academic_yr = academic_yr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}