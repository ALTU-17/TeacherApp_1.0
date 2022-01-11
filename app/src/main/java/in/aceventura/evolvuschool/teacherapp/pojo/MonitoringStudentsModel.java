package in.aceventura.evolvuschool.teacherapp.pojo;

/**
 * Created by "Manoj Waghmare" on 10,Sep,2020
 **/
public class MonitoringStudentsModel {
    String roll_no,student_name,attempting_que_no,exam_status;

    public MonitoringStudentsModel(String roll_no, String student_name, String attempting_que_no, String exam_status) {
        this.roll_no = roll_no;
        this.student_name = student_name;
        this.attempting_que_no = attempting_que_no;
        this.exam_status = exam_status;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getAttempting_que_no() {
        return attempting_que_no;
    }

    public void setAttempting_que_no(String attempting_que_no) {
        this.attempting_que_no = attempting_que_no;
    }

    public String getExam_status() {
        return exam_status;
    }

    public void setExam_status(String exam_status) {
        this.exam_status = exam_status;
    }
}
