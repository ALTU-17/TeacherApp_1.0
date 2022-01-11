package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

public class EventImagesEdit extends EventImages implements Serializable {
    private String ImgName,FileBase64Code,extension,imguri,PdfString;
    private boolean checkedstatus;

    public boolean isCheckedstatus() {
        return checkedstatus;
    }

    public void setCheckedstatus(boolean checkedstatus) {
        this.checkedstatus = checkedstatus;
    }

    public String getImguri() {
        return imguri;
    }

    public void setImguri(String imguri) {
        this.imguri = imguri;
    }

    public String getExtension() {
        return this.extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getImgName() {
        return ImgName;
    }

    public void setImgName(String imgName) {
        ImgName = imgName;
    }

    public String getFileBase64Code() {
        return FileBase64Code;
    }

    public void setFileBase64Code(String fileBase64Code) {
        FileBase64Code = fileBase64Code;
    }

    public String getPdfString() {
        return PdfString;
    }

    public void setPdfString(String pdfString) {
        PdfString = pdfString;
    }}
