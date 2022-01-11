package in.aceventura.evolvuschool.teacherapp.pojo;


/**
 * Created by Vinod Patil on 11/07/2016.
 */

public class DataSet {
    //Student Gender
    @Override
    public String toString() {
        return "DataSet{" +
                "maleGender='" + maleGender + '\'' +
                ", femaleGender='" + femaleGender + '\'' +
                ", hClass='" + hClass + '\'' +
                ", hSubject='" + hSubject + '\'' +
                ", hSDate='" + hSDate + '\'' +
                ", hEDate='" + hEDate + '\'' +
                ", hDescription='" + hDescription + '\'' +
                ", hStatus='" + hStatus + '\'' +
                ", hTComment='" + hTComment + '\'' +
                ", hPComment='" + hPComment + '\'' +
                ", hSection='" + hSection + '\'' +
                ", hId='" + hId + '\'' +
                ", cId='" + cId + '\'' +
                ", tnId='" + tnId + '\'' +
                ", tnClass='" + tnClass + '\'' +
                ", tnSubject='" + tnSubject + '\'' +
                ", tnDate='" + tnDate + '\'' +
                ", tnDescription='" + tnDescription + '\'' +
                ", tnFile='" + tnFile + '\'' +
                ", tnTname='" + tnTname + '\'' +
                ", nClass='" + nClass + '\'' +
                ", nDate='" + nDate + '\'' +
                ", nFromDate='" + nFromDate + '\'' +
                ", nToDate='" + nToDate + '\'' +
                ", nStartTime='" + nStartTime + '\'' +
                ", nEndTime='" + nEndTime + '\'' +
                ", nSubject='" + nSubject + '\'' +
                ", nDescription='" + nDescription + '\'' +
                ", nFile='" + nFile + '\'' +
                ", nType='" + nType + '\'' +
                ", nTeacher='" + nTeacher + '\'' +
                ", rSubject='" + rSubject + '\'' +
                ", rDate='" + rDate + '\'' +
                ", rRemarkSubject='" + rRemarkSubject + '\'' +
                ", rDescription='" + rDescription + '\'' +
                ", rTeachername='" + rTeachername + '\'' +
                ", rId='" + rId + '\'' +
                ", rAck='" + rAck + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", year=" + year +
                ", source='" + source + '\'' +
                ", worth='" + worth + '\'' +
                '}';
    }private String maleGender, femaleGender;


    //Remark - Teacher Module Functions Starts Here
    String tmRPublish, tmRRID, tmRAcknoledge, tmRTid, tmRSubName, tmRClsName, tmRSecName ,tmRSectionID, tmRSubjectID, tmRClassID, tmRDate, tmRSubject, tmRDescription, tmRStudID, tmRStudFName, tmRStudMName, tmRStudLName;


    public String getTmRRID() {
        return tmRRID;
    }

    public void setTmRRID(String tmRRID) {
        this.tmRRID = tmRRID;
    }



    public String getTmRPublish() {
        return tmRPublish;
    }

    public void setTmRPublish(String tmRPublish) {
        this.tmRPublish = tmRPublish;
    }

    public String getTmRAcknoledge() {
        return tmRAcknoledge;
    }

    public void setTmRAcknoledge(String tmRAcknoledge) {
        this.tmRAcknoledge = tmRAcknoledge;
    }

    public String getTmRTid() {
        return tmRTid;
    }

    public void setTmRTid(String tmRTid) {
        this.tmRTid = tmRTid;
    }

    public String getTmRSubName() {
        return tmRSubName;
    }

    public void setTmRSubName(String tmRSubName) {
        this.tmRSubName = tmRSubName;
    }

    public String getTmRClsName() {
        return tmRClsName;
    }

    public void setTmRClsName(String tmRClsName) {
        this.tmRClsName = tmRClsName;
    }

    public String getTmRSecName() {
        return tmRSecName;
    }

    public void setTmRSecName(String tmRSecName) {
        this.tmRSecName = tmRSecName;
    }

    public String getTmRSectionID() {
        return tmRSectionID;
    }

    public void setTmRSectionID(String tmRSectionID) {
        this.tmRSectionID = tmRSectionID;
    }

    public String getTmRSubjectID() {
        return tmRSubjectID;
    }

    public void setTmRSubjectID(String tmRSubjectID) {
        this.tmRSubjectID = tmRSubjectID;
    }

    public String getTmRClassID() {
        return tmRClassID;
    }

    public void setTmRClassID(String tmRClassID) {
        this.tmRClassID = tmRClassID;
    }

    public String getTmRDate() {
        return tmRDate;
    }

    public void setTmRDate(String tmRDate) {
        this.tmRDate = tmRDate;
    }

    public String getTmRSubject() {
        return tmRSubject;
    }

    public void setTmRSubject(String tmRSubject) {
        this.tmRSubject = tmRSubject;
    }


    public String getTmRDescription() {
        return tmRDescription;
    }

    public void setTmRDescription(String tmRDescription) {
        this.tmRDescription = tmRDescription;
    }

    public String getTmRStudID() {
        return tmRStudID;
    }

    public void setTmRStudID(String tmRStudID) {
        this.tmRStudID = tmRStudID;
    }

    public String getTmRStudFName() {
        return tmRStudFName;
    }

    public void setTmRStudFName(String tmRStudFName) {
        this.tmRStudFName = tmRStudFName;
    }

    public String getTmRStudMName() {
        return tmRStudMName;
    }

    public void setTmRStudMName(String tmRStudMName) {
        this.tmRStudMName = tmRStudMName;
    }

    public String getTmRStudLName() {
        return tmRStudLName;
    }

    public void setTmRStudLName(String tmRStudLName) {
        this.tmRStudLName = tmRStudLName;
    }

    //Homework Teacher Module Functions Starts Here
    String tmHPublish, tmHTid, tmHHid, tmHSubName, tmHClsName, tmHSecName ,tmHSection, tmHSubject, tmHClass, tmHSDate, tmHEDate, tmHDescription;

    public String getTmHSubName() {
        return tmHSubName;
    }

    public void setTmHSubName(String tmHSubName) {
        this.tmHSubName = tmHSubName;
    }

    public String getTmHClsName() {
        return tmHClsName;
    }

    public void setTmHClsName(String tmHClsName) {
        this.tmHClsName = tmHClsName;
    }

    public String getTmHSecName() {
        return tmHSecName;
    }

    public void setTmHSecName(String tmHSecName) {
        this.tmHSecName = tmHSecName;
    }

    public String getTmHDescription() {
        return tmHDescription;
    }

    public void setTmHDescription(String tmHDescription) {
        this.tmHDescription = tmHDescription;
    }

    public String getTmHPublish() {
        return tmHPublish;
    }

    public void setTmHPublish(String tmHPublish) {
        this.tmHPublish = tmHPublish;
    }

    public String getTmHTid() {
        return tmHTid;
    }

    public void setTmHTid(String tmHTid) {
        this.tmHTid = tmHTid;
    }

    public String getTmHHid() {
        return tmHHid;
    }

    public void setTmHHid(String tmHHid) {
        this.tmHHid = tmHHid;
    }

    public String getTmHSection() {
        return tmHSection;
    }

    public void setTmHSection(String tmHSection) {
        this.tmHSection = tmHSection;
    }

    public String getTmHSubject() {
        return tmHSubject;
    }

    public void setTmHSubject(String tmHSubject) {
        this.tmHSubject = tmHSubject;
    }

    public String getTmHClass() {
        return tmHClass;
    }

    public void setTmHClass(String tmHClass) {
        this.tmHClass = tmHClass;
    }

    public String getTmHSDate() {
        return tmHSDate;
    }

    public void setTmHSDate(String tmHSDate) {
        this.tmHSDate = tmHSDate;
    }

    public String getTmHEDate() {
        return tmHEDate;
    }

    public void setTmHEDate(String tmHEDate) {
        this.tmHEDate = tmHEDate;
    }




    //Book Availability
    private String bCategory, bTitle, bAuthor, bCategoryName;

    //Class Timetable
    private String classIn, classOut, satClassIn, satClassOut, ttMonday, ttTuesday, ttWednesday, ttThursday, ttFriday, ttSaturday  ;
    private Integer periodNo;


    //Homework
    private String hClass, hSubject, hSDate, hEDate, hDescription, hStatus, hTComment, hPComment, hSection, hId, cId;

    //Teacher Note
    private String tnId, tnClass, tnSubject, tnDate, tnDescription, tnFile,tnTname, tnSection, tnSectionId;

    //Notice
    private String nClass, nDate, nFromDate, nToDate, nStartTime, nEndTime, nSubject, nDescription, nFile, nType, nTeacher, nId;

    //Remark
    private String rSubject, rDate, rRemarkSubject,rDescription, rTeachername, rId, rAck;

    private String name, image;
    private int year;
    private String source;
    private String worth;

    //Remark Constructors Start Here
    public String getrSubject() {

        return rSubject;
    }

    public void setrSubject(String rSubject) {

        this.rSubject = rSubject;
    }


    public String getrDate() {
        return rDate;
    }

    public void setrDate(String rDate) {
        this.rDate = rDate;
    }

    public String getrRemarkSubject() {

        return rRemarkSubject;
    }

    public void setrRemarkSubject(String rRemarkSubject) {

        this.rRemarkSubject = rRemarkSubject;
    }


    public String getrDescription() {

        return rDescription;
    }

    public void setrDescription(String rDescription)
    {
        this.rDescription = rDescription;
    }

    public String getrTName() {

        return rTeachername;
    }

    public void setrTName(String rTeachername)
    {
        this.rTeachername = rTeachername;
    }


    public String getrId() {

        return rId;
    }

    public void setrId(String rId)
    {
        this.rId = rId;
    }

    public String getrAck() {

        return rAck;
    }

    public void setrAck(String rAck)
    {
        this.rAck = rAck;
    }

    //Remark Constructors Ends Here



    //Teacher Note Functions Start Here

    public String getTnId() {
        return tnId;
    }

    public void setTnId(String tnId) {
        this.tnId = tnId;
    }


    public String getTnClass() {
        return tnClass;
    }

    public void setTnClass(String tnClass) {
        this.tnClass = tnClass;
    }

    public String getTnSection() {
        return tnSection;
    }

    public void setTnSection(String tnSection) {
        this.tnSection = tnSection;
    }

    public String getTnSectionId() {
        return tnSectionId;
    }

    public void setTnSectionId(String tnSectionId) {
        this.tnSectionId = tnSectionId;
    }
    public String getTnSubject() {
        return tnSubject;
    }

    public void setTnSubject(String tnSubject) {
        this.tnSubject = tnSubject;
    }

    public String getTnDate() {
        return tnDate;
    }

    public void setTnDate(String tnDate) {
        this.tnDate = tnDate;
    }



    public String getTnDescription() {
        return tnDescription;
    }

    public void setTnDescription(String tnDescription) {
        this.tnDescription = tnDescription;
    }


    public String getTnFile() {
        return tnFile;
    }

    public void setTnFile(String tnFile) {
        this.tnFile = tnFile;
    }

    public String getTnTname() {
        return tnTname;
    }

    public void setTnTname(String tnTname) {
        this.tnTname = tnTname;
    }
    //Teacher Note Functions End Here



    //Homework Functions Start Here
    public String getsClass() {
        return hClass;
    }

    public void setsClass(String hClass) {
        this.hClass = hClass;
    }

    public String getSubject() {
        return hSubject;
    }

    public void setSubject(String hSubject) {
        this.hSubject = hSubject;
    }

    public String getsDate() {
        return hSDate;
    }

    public void setsDate(String hSDate) {
        this.hSDate = hSDate;
    }

    public String geteDate() {
        return hEDate;
    }

    public void seteDate(String hEDate) {
        this.hEDate = hEDate;
    }

    public String getDescription() {
        return hDescription;
    }

    public void setDescription(String hDescription) {
        this.hDescription = hDescription;
    }


    public String getStatus() {
        return hStatus;
    }

    public void setStatus(String hStatus) {
        this.hStatus = hStatus;
    }

    public String getPcomment() {
        return hPComment;
    }

    public void setPcomment(String hPComment) {
        this.hPComment = hPComment;
    }

    public String getTcomment() {
        return hTComment;
    }

    public void setTcomment(String hTComment) {
        this.hTComment = hTComment;
    }

    public String getSection() {
        return hSection;
    }

    public void setSection(String hSection) {
        this.hSection = hSection;
    }
    public String getHomeworkId() {
        return hId;
    }

    public void setHomeworkId(String hId) {
        this.hId = hId;
    }

    public String getCommentId() {
        return cId;
    }

    public void setCommentId(String cId) {
        this.cId = cId;
    }
    //Homework Functions End Here


    //Notice Functions Start Here
    public String getnClass() {
        return nClass;
    }

    public void setnClass(String nClass) {
        this.nClass = nClass;
    }


    public String getnDate() {
        return nDate;
    }

    public void setnDate(String nDate) {
        this.nDate = nDate;
    }

    public String getnFromDate() {
        return nFromDate;
    }

    public void setnFromDate(String nFromDate) {
        this.nFromDate = nFromDate;
    }

    public String getnToDate() {
        return nToDate;
    }

    public void setnToDate(String nToDate) {
        this.nToDate = nToDate;
    }

    public String getnStartTime() {
        return nStartTime;
    }

    public void setnStartTime(String nStartTime) {
        this.nStartTime = nStartTime;
    }

    public String getnEndTime() {
        return nEndTime;
    }

    public void setnEndTime(String nEndTime) {
        this.nEndTime = nEndTime;
    }
    public String getnFile() {
        return nFile;
    }

    public void setnFile(String nFile) {
        this.nFile = nFile;
    }

    public String getnId() {
        return nId;
    }

    public void setnId(String nId) {
        this.nId = nId;
    }

    public String getnSubject() {
        return nSubject;
    }

    public void setnSubject(String nSubject) {
        this.nSubject = nSubject;
    }

    public String getnDescription() {
        return nDescription;
    }

    public void setnDescription(String nDescription) {
        this.nDescription = nDescription;
    }

    public String getnType() {
        return nType;
    }

    public void setnType(String nType) {
        this.nType = nType;
    }

    public String getnTeacher() {
        return nTeacher;
    }

    public void setnTeacher(String nTeacher) {
        this.nTeacher = nTeacher;
    }
    //Notice Functions End Here


    //Book Availability Abstract Methods Start Here

    public String getBookCategory() {
        return bCategory;
    }

    public void setBookCategory(String bCategory) {
        this.bCategory = bCategory;
    }

    public String getbTitle() {
        return bTitle;
    }

    public void setbTitle(String bTitle) {
        this.bTitle = bTitle;
    }

    public String getbAuthor() {
        return bAuthor;
    }

    public void setbAuthor(String bAuthor) {
        this.bAuthor = bAuthor;
    }

    public String getbCategoryName() {
        return bCategoryName;
    }

    public void setbCategoryName(String bCategoryName) {
        this.bCategoryName = bCategoryName;
    }


    //Book Availability Abstract Methods End Here


    //Class Timetable Abstract Methods Start Here


    public Integer getPeriodNo() {
        return periodNo;
    }

    public void setPeriodNo(Integer periodNo) {
        this.periodNo = periodNo;
    }

    public String getClassIn() {
        return classIn;
    }

    public void setClassIn(String classIn) {
        this.classIn = classIn;
    }

    public String getClassOut() {
        return classOut;
    }

    public void setClassOut(String classOut) {
        this.classOut = classOut;
    }

    public String getSatClassIn() {
        return satClassIn;
    }

    public void setSatClassIn(String satClassIn) {
        this.satClassIn = satClassIn;
    }

    public String getSatClassOut() {
        return satClassOut;
    }

    public void setSatClassOut(String satClassOut) {
        this.satClassOut = satClassOut;
    }

    public String getTtMonday() {
        return ttMonday;
    }

    public void setTtMonday(String ttMonday) {
        this.ttMonday = ttMonday;
    }

    public String getTtTuesday() {
        return ttTuesday;
    }

    public void setTtTuesday(String ttTuesday) {
        this.ttTuesday = ttTuesday;
    }

    public String getTtWednesday() {
        return ttWednesday;
    }

    public void setTtWednesday(String ttWednesday) {
        this.ttWednesday = ttWednesday;
    }

    public String getTtThursday() {
        return ttThursday;
    }

    public void setTtThursday(String ttThursday) {
        this.ttThursday = ttThursday;
    }

    public String getTtFriday() {
        return ttFriday;
    }

    public void setTtFriday(String ttFriday) {
        this.ttFriday = ttFriday;
    }

    public String getTtSaturday() {
        return ttSaturday;
    }

    public void setTtSaturday(String ttSaturday) {
        this.ttSaturday = ttSaturday;
    }

    public String getMaleGender() {
        return maleGender;
    }

    public void setMaleGender(String maleGender) {
        this.maleGender = maleGender;
    }

    public String getFemaleGender() {
        return femaleGender;
    }

    public void setFemaleGender(String femaleGender) {
        this.femaleGender = femaleGender;
    }


}
