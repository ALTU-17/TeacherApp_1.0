package in.aceventura.evolvuschool.teacherapp.pojo;

/**
 * Created by "Manoj Waghmare" on 12,Aug,2020
 **/
public class ExaminationPojo {
    String exam_id,name,start_date,end_date,open_day,term_id,comment,academic_yr;

    public ExaminationPojo(String exam_id, String name, String start_date, String end_date, String open_day, String term_id, String comment, String academic_yr) {
        this.exam_id = exam_id;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.open_day = open_day;
        this.term_id = term_id;
        this.comment = comment;
        this.academic_yr = academic_yr;
    }

    public String getExam_id() {
        return exam_id;
    }

    public String getName() {
        return name;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getOpen_day() {
        return open_day;
    }

    public String getTerm_id() {
        return term_id;
    }

    public String getComment() {
        return comment;
    }

    public String getAcademic_yr() {
        return academic_yr;
    }
}
