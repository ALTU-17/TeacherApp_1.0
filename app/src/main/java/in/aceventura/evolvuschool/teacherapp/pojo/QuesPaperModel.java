package in.aceventura.evolvuschool.teacherapp.pojo;

/**
 * Created by "Manoj Waghmare" on 19,Aug,2020
 **/
public class QuesPaperModel {
    private String up_id,  question_bank_id, academic_yr, image_name, file_size,
            url,question_bank_type,qb_id;




    public QuesPaperModel(String up_id,  String question_bank_id,
                           String academic_yr, String image_name,
                          String file_size, String url,
                          String question_bank_type) {
        this.up_id = up_id;
        this.question_bank_id = question_bank_id;
        this.academic_yr = academic_yr;
        this.image_name = image_name;
        this.file_size = file_size;
        this.url = url;
        this.question_bank_type = question_bank_type;
    }



    public QuesPaperModel(String question_bank_id, String image_name, String url) {
        this.question_bank_id = question_bank_id;
        this.image_name = image_name;
        this.url = url;
    }

    public QuesPaperModel(String question_bank_id,String qb_id, String image_name, String url) {
        this.question_bank_id = question_bank_id;
        this.qb_id = qb_id;
        this.image_name = image_name;
        this.url = url;

    }



    public String getQb_id() {
        return qb_id;
    }

    public void setQb_id(String qb_id) {
        this.qb_id = qb_id;
    }

    public String getQuestion_bank_type() {
        return question_bank_type;
    }

    public void setQuestion_bank_type(String question_bank_type) {
        this.question_bank_type = question_bank_type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUp_id() {
        return up_id;
    }

    public void setUp_id(String up_id) {
        this.up_id = up_id;
    }

    public String getQuestion_bank_id() {
        return question_bank_id;
    }

    public void setQuestion_bank_id(String question_bank_id) {
        this.question_bank_id = question_bank_id;
    }


    public String getAcademic_yr() {
        return academic_yr;
    }

    public void setAcademic_yr(String academic_yr) {
        this.academic_yr = academic_yr;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }
}


