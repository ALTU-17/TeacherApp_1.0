package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

public class EventImagesPdfEdit extends EventImages {

    private String FileName,PdfString,extension;

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getPdfString() {
        return PdfString;
    }

    public void setPdfString(String pdfString) {
        PdfString = pdfString;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
