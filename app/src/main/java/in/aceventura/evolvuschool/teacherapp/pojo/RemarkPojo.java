package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

/**
 * Created by Admin on 7/5/2018.
 */

public class RemarkPojo implements Serializable {
    private String rmkClassnm, rmkSubNm, rmkDate, rmkdesc, rmkid, remarksubject, rmkclassid, rmksecid, rmksubid, rmkstudid,
            rmkteacherid, rmkacdyr, rmkpublish, rmkack, rmkfnm, rmkmnm, rmklnm, publishremDate, rmksecnm, remark_type;
    private boolean isExpanded;
    private String read_status;

    public RemarkPojo(String rmkClassnm, String rmkSubNm, String rmkDate, String rmkdesc, String rmkid, String remarksubject, String rmkclassid, String rmksecid, String rmksubid, String rmkstudid, String rmkteacherid, String rmkacdyr, String rmkpublish, String rmkack, String rmkfnm, String rmkmnm, String rmklnm, String rmksecnm, String remark_type, boolean isExpanded, String read_status) {
        this.rmkClassnm = rmkClassnm;
        this.rmkSubNm = rmkSubNm;
        this.rmkDate = rmkDate;
        this.rmkdesc = rmkdesc;
        this.rmkid = rmkid;
        this.remarksubject = remarksubject;
        this.rmkclassid = rmkclassid;
        this.rmksecid = rmksecid;
        this.rmksubid = rmksubid;
        this.rmkstudid = rmkstudid;
        this.rmkteacherid = rmkteacherid;
        this.rmkacdyr = rmkacdyr;
        this.rmkpublish = rmkpublish;
        this.rmkack = rmkack;
        this.rmkfnm = rmkfnm;
        this.rmkmnm = rmkmnm;
        this.rmklnm = rmklnm;
        this.rmksecnm = rmksecnm;
        this.isExpanded = isExpanded;
        this.remark_type = remark_type;
        this.read_status = read_status;
    }

    public boolean getExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getRmkClassnm() {
        return rmkClassnm;
    }

    public String getRmksecnm() {
        return rmksecnm;
    }

    public void setRmksecnm(String rmksecnm) {
        this.rmksecnm = rmksecnm;
    }

    public void setRmkClassnm(String rmkClassnm) {
        this.rmkClassnm = rmkClassnm;
    }


    public String getRmkSubNm() {
        return rmkSubNm;
    }

    public void setRmkSubNm(String rmkSubNm) {
        this.rmkSubNm = rmkSubNm;
    }

    public String getRmkDate() {
        return rmkDate;
    }

    public void setRmkDate(String rmkDate) {
        this.rmkDate = rmkDate;
    }

    public String getPublishremDate() {
        return publishremDate;
    }

    public void setPublishremDate(String publishremDate) {
        this.publishremDate = publishremDate;
    }


    public String getRmkdesc() {
        return rmkdesc;
    }

    public void setRmkdesc(String rmkdesc) {
        this.rmkdesc = rmkdesc;
    }

    public String getRmkid() {
        return rmkid;
    }

    public void setRmkid(String rmkid) {
        this.rmkid = rmkid;
    }

    public String getRemarksubject() {
        return remarksubject;
    }

    public void setRemarksubject(String remarksubject) {
        this.remarksubject = remarksubject;
    }

    public String getRmkclassid() {
        return rmkclassid;
    }

    public void setRmkclassid(String rmkclassid) {
        this.rmkclassid = rmkclassid;
    }

    public String getRmksecid() {
        return rmksecid;
    }

    public void setRmksecid(String rmksecid) {
        this.rmksecid = rmksecid;
    }

    public String getRmksubid() {
        return rmksubid;
    }

    public void setRmksubid(String rmksubid) {
        this.rmksubid = rmksubid;
    }

    public String getRmkstudid() {
        return rmkstudid;
    }

    public void setRmkstudid(String rmkstudid) {
        this.rmkstudid = rmkstudid;
    }

    public String getRmkteacherid() {
        return rmkteacherid;
    }

    public void setRmkteacherid(String rmkteacherid) {
        this.rmkteacherid = rmkteacherid;
    }

    public String getRmkacdyr() {
        return rmkacdyr;
    }

    public void setRmkacdyr(String rmkacdyr) {
        this.rmkacdyr = rmkacdyr;
    }

    public String getRmkpublish() {
        return rmkpublish;
    }

    public void setRmkpublish(String rmkpublish) {
        this.rmkpublish = rmkpublish;
    }

    public String getRmkack() {
        return rmkack;
    }

    public void setRmkack(String rmkack) {
        this.rmkack = rmkack;
    }

    public String getRmkfnm() {
        return rmkfnm;
    }

    public void setRmkfnm(String rmkfnm) {
        this.rmkfnm = rmkfnm;
    }

    public String getRmkmnm() {
        return rmkmnm;
    }

    public void setRmkmnm(String rmkmnm) {
        this.rmkmnm = rmkmnm;
    }

    public String getRmklnm() {
        return rmklnm;
    }

    public void setRmklnm(String rmklnm) {
        this.rmklnm = rmklnm;
    }

    public String getRemark_type() {
        return remark_type;
    }

    public void setRemark_type(String remark_type) {
        this.remark_type = remark_type;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }
}
