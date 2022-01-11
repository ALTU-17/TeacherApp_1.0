package in.aceventura.evolvuschool.teacherapp.pojo;

/**
 * Created by "Manoj Waghmare" on 25,Aug,2020
 **/
public class QBPojo {
    String question_bank_id,qb_name,qb_type;

    public QBPojo(String question_bank_id, String qb_name, String qb_type) {
        this.question_bank_id = question_bank_id;
        this.qb_name = qb_name;
        this.qb_type = qb_type;
    }

    public String getQb_type() {
        return qb_type;
    }

    public void setQb_type(String qb_type) {
        this.qb_type = qb_type;
    }

    public String getQuestion_bank_id() {
        return question_bank_id;
    }

    public void setQuestion_bank_id(String question_bank_id) {
        this.question_bank_id = question_bank_id;
    }

    public String getQb_name() {
        return qb_name;
    }

    public void setQb_name(String qb_name) {
        this.qb_name = qb_name;
    }
}
