package in.aceventura.evolvuschool.teacherapp.pojo;

/**
 * Created by "Manoj Waghmare" on 07,Sep,2020
 **/
public class AnsPaperThumbnailModel {
    String imgName,imgSize,imgUrl;

    public AnsPaperThumbnailModel(String imgName, String imgSize, String imgUrl) {
        this.imgName = imgName;
        this.imgSize = imgSize;
        this.imgUrl = imgUrl;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgSize() {
        return imgSize;
    }

    public void setImgSize(String imgSize) {
        this.imgSize = imgSize;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
