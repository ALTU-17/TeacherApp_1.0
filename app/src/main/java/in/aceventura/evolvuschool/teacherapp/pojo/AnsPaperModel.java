package in.aceventura.evolvuschool.teacherapp.pojo;

/**
 * Created by "Manoj Waghmare" on 19,Aug,2020
 **/
public class AnsPaperModel {
    private String uploaded_qp_id,question_bank_id,file_size,image_name,url,academic_yr,student_id,
    question_bank_type, ans_id;

    public AnsPaperModel(String uploaded_qp_id, String question_bank_id, String file_size,
                         String image_name) {
        this.uploaded_qp_id = uploaded_qp_id;
        this.question_bank_id = question_bank_id;
        this.file_size = file_size;
        this.image_name = image_name;
    }

    public AnsPaperModel(String uploaded_qp_id, String question_bank_id, String file_size,
                         String image_name, String url) {
        this.uploaded_qp_id = uploaded_qp_id;
        this.question_bank_id = question_bank_id;
        this.file_size = file_size;
        this.image_name = image_name;
        this.url = url;
    }

    public AnsPaperModel(String uploaded_qp_id,
                         String question_bank_id,
                         String file_size,
                         String image_name,
                         String url,
                         String question_bank_type,
                         String ans_id) {
        this.uploaded_qp_id = uploaded_qp_id;
        this.question_bank_id = question_bank_id;
        this.file_size = file_size;
        this.image_name = image_name;
        this.url = url;
        this.question_bank_type = question_bank_type;
        this.ans_id = ans_id;
    }

    public String getAns_id() {
        return ans_id;
    }

    public void setAns_id(String ans_id) {
        this.ans_id = ans_id;
    }

    public String getQuestion_bank_type() {
        return question_bank_type;
    }

    public void setQuestion_bank_type(String question_bank_type) {
        this.question_bank_type = question_bank_type;
    }

    public String getAcademic_yr() {
        return academic_yr;
    }

    public String getStudent_id() {
        return student_id;
    }

    /*public AnsPaperModel(String uploaded_qp_id, String question_bank_id, String file_size, String image_name,
                         String url, String academic_yr, String student_id) {
        this.uploaded_qp_id = uploaded_qp_id;
        this.question_bank_id = question_bank_id;
        this.file_size = file_size;
        this.image_name = image_name;
        this.url = url;
        this.academic_yr = academic_yr;
        this.student_id = student_id;
    }*/

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUploaded_qp_id() {
        return uploaded_qp_id;
    }

    public String getQuestion_bank_id() {
        return question_bank_id;
    }

    public String getFile_size() {
        return file_size;
    }

    public String getImage_name() {
        return image_name;
    }
}