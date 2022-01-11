package in.aceventura.evolvuschool.teacherapp.pojo;

/**
 * Created by admin on 11/16/2017.
 */

public class EventImages {

    private String ImgName,FileBase64Code,extension,imguri,PdfString,imgUrl;
    private boolean checkedstatus;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

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
    }
}
