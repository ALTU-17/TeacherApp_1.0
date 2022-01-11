package in.aceventura.evolvuschool.teacherapp;


/**
 * Created by Administrator on 7/6/2017.
 */

public class Config {
    // Arnolds Test Server
    public static final String DATA_URL = "http://aceventura.in/demo/services/";

    // TODO: Change the message before uploading apk
    public static final String LOCAL_ANDROID_VERSION_MESSAGE = "New update is available for EvolvU Teacher App\nIn this release\n " +
            "1.,\n" +
            "2.,\n" +
            "3..";

    // DemoTest Server
    public static final String REST_URL_MY_Login = "http://aceventura.in/demo/SACSv4test/index.php/LoginApi/login";

    //Parent KEYS
    public static final String REG_ID = "reg_id";
    public static final String PARENT_ID = "parent_id";
    public static final String TEACHER_NAME = "teacher_name";
    public static final String FATHER_OCCUPATION = "father_occupation";
    public static final String F_OFFICE_ADD = "f_office_add";
    public static final String F_OFFICE_TEL = "f_office_tel";
    public static final String F_MOBILE = "f_mobile";
    public static final String F_EMAIL = "f_email";
    public static final String MOTHER_OCCUPATION = "mother_occupation";
    public static final String M_OFFICE_ADD = "m_office_add";
    public static final String M_OFFICE_TEL = "m_office_tel";
    public static final String MOTHER_NAME = "mother_name";
    public static final String M_MOBILE = "m_mobile";
    public static final String M_EMAILID = "m_emailid";
    public static final String PARENT_ADHAR_NO = "parent_adhar_no";

    public static final String JSON_ARRAY = "result";

    //Student KEYS
    public static final String STUD_ID = "student_id";
    public static final String FIRST_NAME = "first_name";
    public static final String MIDDLE_NAME = "mid_name";
    public static final String LAST_NAME = "last_name";
    public static final String DOB = "dob";
    public static final String DOA = "admission_date";
    public static final String GRNNO = "reg_no";
    public static final String SADHAR_NO = "stu_aadhaar_no";
    public static final String HOUSE = "house";
    public static final String SCLASS = "class_id";
    public static final String ROLLNO = "roll_no";
    public static final String GENDER = "gender";
    public static final String BLOODGROUP = "blood_group";
    public static final String DIVISION = "section_id";
    public static final String NATIONALITY = "nationality";
    public static final String ADDRESS = "permant_add";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String PINCODE = "pincode";
    public static final String CASTE = "caste";
    public static final String RELIGION = "religion";
    public static final String CATEGORY = "category";
    public static final String EMERNAME = "emergency_name";
    public static final String EMERADD = "emergency_add";
    public static final String EMERCONTACT = "emergency_contact";
    public static final String TRANSPORT_MODE = "transport_mode";
    public static final String VEHICLE_NO = "vehicle_no";
    public static final String ALLERGY = "allergies";
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "weight";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_NAME = "name";
    public static final String KEY_REGID = "reg_id";
    public static final String KEY_EMAIL = "username";
    public static final String KEY_PASSWORD = "password";

    //Firebase KEYS

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "480";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String SHARED_PREF = "spf_firebase";

    public static final String LOCAL_ANDROID_VERSION_DAILOG_TITLE = "Update Available";

    // TODO: Change the version before uploading apk
    public static final String LOCAL_ANDROID_VERSION = "1.65";

    //TODO:  Aceventura Server DEMO
    //public static final String NEW_LOGIN = "http://aceventura.in/demo/evolvuUserService/validate_teacher_user";

    //TODO:  Live Server URL
      public static final  String NEW_LOGIN = "http://aceventura.in/evolvuUserService/validate_teacher_user";

}


/*Teacher Ids
 * */