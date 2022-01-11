
package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;


public class EvaluationStudentListModel implements Serializable {
    private String question_bank_id;
    private String mAcademicYr;
    private Object mAdmissionClass;
    private String mAdmissionDate;
    private String mAllergies;
    private Object mBirthPlace;
    private String mBloodGroup;
    private Object mBusId;
    private String mCaste;
    private String mCategory;
    private String mCity;
    private String mClassId;
    private String mDate;
    private Object mDeletedBy;
    private Object mDeletedDate;
    private String mDob;
    private String mEmergencyAdd;
    private String mEmergencyContact;
    private String mEmergencyName;
    private Object mFeesPaid;
    private String mFirstName;
    private String mGender;
    private Object mHasSpecs;
    private String mHeight;
    private String mHouse;
    private String mIsDelete;
    private String mIsModify;
    private String mIsNew;
    private String mIsPromoted;
    private String mLastDate;
    private String mLastName;
    private String mLeavingRemark;
    private String mMidName;
    private Object mMotherTongue;
    private String mNationality;
    private String mParentId;
    private String mPermantAdd;
    private String mPincode;
    private String mRegNo;
    private String mReligion;
    private String mRollNo;
    private String mSectionId;
    private String mSlcIssueDate;
    private String mSlcNo;
    private String mState;
    private String mStuAadhaarNo;
    private Object mStudIdNo;
    private String mStudentId;
    private Object mSubcaste;
    private String mTransportMode;
    private String mVehicleNo;
    private String mWeight;
    private String date ;
    private String up_id ;
    private String question_bank_type ;
    private int count; //1 - evaluated, 0 - not evaluated


    public EvaluationStudentListModel(String mAcademicYr, String mClassId, String mFirstName,
                                      String mLastName, String mMidName, String mParentId,
                                      String mRollNo, String mSectionId, String mStudentId,
                                      String question_bank_id, String date, String up_id,
                                      String question_bank_type,int count) {
        this.mAcademicYr = mAcademicYr;
        this.mClassId = mClassId;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mMidName = mMidName;
        this.mParentId = mParentId;
        this.mRollNo = mRollNo;
        this.mSectionId = mSectionId;
        this.mStudentId = mStudentId;
        this.question_bank_id = question_bank_id;
        this.date = date;
        this.up_id = up_id;
        this.question_bank_type = question_bank_type;
        this.count = count;

    }

    public EvaluationStudentListModel(String mAcademicYr, String mClassId, String mFirstName,
                                      String mLastName, String mMidName, String mParentId,
                                      String mRollNo, String mSectionId, String mStudentId,
                                      String question_bank_id, String date, String up_id,
                                      String question_bank_type) {
        this.mAcademicYr = mAcademicYr;
        this.mClassId = mClassId;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mMidName = mMidName;
        this.mParentId = mParentId;
        this.mRollNo = mRollNo;
        this.mSectionId = mSectionId;
        this.mStudentId = mStudentId;
        this.question_bank_id = question_bank_id;
        this.date = date;
        this.up_id = up_id;
        this.question_bank_type = question_bank_type;
    }




    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getQuestion_bank_type() {
        return question_bank_type;
    }

    public void setQuestion_bank_type(String question_bank_type) {
        this.question_bank_type = question_bank_type;
    }

    public String getUp_id() {
        return up_id;
    }

    public void setUp_id(String up_id) {
        this.up_id = up_id;
    }

    public String getSubmittedDate() {
        return date;
    }

    public void setSubmittedDate(String date) {
        this.date = date;
    }

    public String getQuestion_bank_id() {
        return question_bank_id;
    }

    public void setQuestion_bank_id(String question_bank_id) {
        this.question_bank_id = question_bank_id;
    }

    public String getAcademicYr() {
        return mAcademicYr;
    }

    public void setAcademicYr(String academicYr) {
        mAcademicYr = academicYr;
    }

    public Object getAdmissionClass() {
        return mAdmissionClass;
    }

    public void setAdmissionClass(Object admissionClass) {
        mAdmissionClass = admissionClass;
    }

    public String getAdmissionDate() {
        return mAdmissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        mAdmissionDate = admissionDate;
    }

    public String getAllergies() {
        return mAllergies;
    }

    public void setAllergies(String allergies) {
        mAllergies = allergies;
    }

    public Object getBirthPlace() {
        return mBirthPlace;
    }

    public void setBirthPlace(Object birthPlace) {
        mBirthPlace = birthPlace;
    }

    public String getBloodGroup() {
        return mBloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        mBloodGroup = bloodGroup;
    }

    public Object getBusId() {
        return mBusId;
    }

    public void setBusId(Object busId) {
        mBusId = busId;
    }

    public String getCaste() {
        return mCaste;
    }

    public void setCaste(String caste) {
        mCaste = caste;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getClassId() {
        return mClassId;
    }

    public void setClassId(String classId) {
        mClassId = classId;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public Object getDeletedBy() {
        return mDeletedBy;
    }

    public void setDeletedBy(Object deletedBy) {
        mDeletedBy = deletedBy;
    }

    public Object getDeletedDate() {
        return mDeletedDate;
    }

    public void setDeletedDate(Object deletedDate) {
        mDeletedDate = deletedDate;
    }

    public String getDob() {
        return mDob;
    }

    public void setDob(String dob) {
        mDob = dob;
    }

    public String getEmergencyAdd() {
        return mEmergencyAdd;
    }

    public void setEmergencyAdd(String emergencyAdd) {
        mEmergencyAdd = emergencyAdd;
    }

    public String getEmergencyContact() {
        return mEmergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        mEmergencyContact = emergencyContact;
    }

    public String getEmergencyName() {
        return mEmergencyName;
    }

    public void setEmergencyName(String emergencyName) {
        mEmergencyName = emergencyName;
    }

    public Object getFeesPaid() {
        return mFeesPaid;
    }

    public void setFeesPaid(Object feesPaid) {
        mFeesPaid = feesPaid;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public Object getHasSpecs() {
        return mHasSpecs;
    }

    public void setHasSpecs(Object hasSpecs) {
        mHasSpecs = hasSpecs;
    }

    public String getHeight() {
        return mHeight;
    }

    public void setHeight(String height) {
        mHeight = height;
    }

    public String getHouse() {
        return mHouse;
    }

    public void setHouse(String house) {
        mHouse = house;
    }

    public String getIsDelete() {
        return mIsDelete;
    }

    public void setIsDelete(String isDelete) {
        mIsDelete = isDelete;
    }

    public String getIsModify() {
        return mIsModify;
    }

    public void setIsModify(String isModify) {
        mIsModify = isModify;
    }

    public String getIsNew() {
        return mIsNew;
    }

    public void setIsNew(String isNew) {
        mIsNew = isNew;
    }

    public String getIsPromoted() {
        return mIsPromoted;
    }

    public void setIsPromoted(String isPromoted) {
        mIsPromoted = isPromoted;
    }

    public String getLastDate() {
        return mLastDate;
    }

    public void setLastDate(String lastDate) {
        mLastDate = lastDate;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getLeavingRemark() {
        return mLeavingRemark;
    }

    public void setLeavingRemark(String leavingRemark) {
        mLeavingRemark = leavingRemark;
    }

    public String getMidName() {
        return mMidName;
    }

    public void setMidName(String midName) {
        mMidName = midName;
    }

    public Object getMotherTongue() {
        return mMotherTongue;
    }

    public void setMotherTongue(Object motherTongue) {
        mMotherTongue = motherTongue;
    }

    public String getNationality() {
        return mNationality;
    }

    public void setNationality(String nationality) {
        mNationality = nationality;
    }

    public String getParentId() {
        return mParentId;
    }

    public void setParentId(String parentId) {
        mParentId = parentId;
    }

    public String getPermantAdd() {
        return mPermantAdd;
    }

    public void setPermantAdd(String permantAdd) {
        mPermantAdd = permantAdd;
    }

    public String getPincode() {
        return mPincode;
    }

    public void setPincode(String pincode) {
        mPincode = pincode;
    }

    public String getRegNo() {
        return mRegNo;
    }

    public void setRegNo(String regNo) {
        mRegNo = regNo;
    }

    public String getReligion() {
        return mReligion;
    }

    public void setReligion(String religion) {
        mReligion = religion;
    }

    public String getRollNo() {
        return mRollNo;
    }

    public void setRollNo(String rollNo) {
        mRollNo = rollNo;
    }

    public String getSectionId() {
        return mSectionId;
    }

    public void setSectionId(String sectionId) {
        mSectionId = sectionId;
    }

    public String getSlcIssueDate() {
        return mSlcIssueDate;
    }

    public void setSlcIssueDate(String slcIssueDate) {
        mSlcIssueDate = slcIssueDate;
    }

    public String getSlcNo() {
        return mSlcNo;
    }

    public void setSlcNo(String slcNo) {
        mSlcNo = slcNo;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getStuAadhaarNo() {
        return mStuAadhaarNo;
    }

    public void setStuAadhaarNo(String stuAadhaarNo) {
        mStuAadhaarNo = stuAadhaarNo;
    }

    public Object getStudIdNo() {
        return mStudIdNo;
    }

    public void setStudIdNo(Object studIdNo) {
        mStudIdNo = studIdNo;
    }

    public String getStudentId() {
        return mStudentId;
    }

    public void setStudentId(String studentId) {
        mStudentId = studentId;
    }

    public Object getSubcaste() {
        return mSubcaste;
    }

    public void setSubcaste(Object subcaste) {
        mSubcaste = subcaste;
    }

    public String getTransportMode() {
        return mTransportMode;
    }

    public void setTransportMode(String transportMode) {
        mTransportMode = transportMode;
    }

    public String getVehicleNo() {
        return mVehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        mVehicleNo = vehicleNo;
    }

    public String getWeight() {
        return mWeight;
    }

    public void setWeight(String weight) {
        mWeight = weight;
    }

}
