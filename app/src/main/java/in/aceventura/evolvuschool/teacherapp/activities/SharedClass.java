package in.aceventura.evolvuschool.teacherapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedClass {
    Context mContext;
    private static SharedClass mInstance;
    public SharedClass(Context mContext) {
        super();
        this.mContext = mContext;
    }
    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String KEY_USER_NAME = "name";
    public static final String KEY_REG_ID = "reg_id";
    private static final String KEY_USER_ID = "user_id";
    public static final String KEY_ROLE_ID = "role_id";
    public static final String KEY_PARENT_ID = "parent_id";
    public static final String KEY_STUDENT_ID = "student_id";
    public static final String KEY_STUDENT_CLASS = "class_id";
    public static final String KEY_STUDENT_SECTION = "section_id";
    public static final String KEY_STUDENT_NAME = "first_name";
    public static final String KEY_ACADEMIC_YEAR = "academic_yr";
    public static final String KEY_COMMENT_ID = "comment_id";
    public static final String KEY_HOMEWORK_ID = "homework_id";
    public static final String KEY_PARENT_COMMENT = "parent_comment";
    public static final String KEY_PHONE_NO = "phone_no";
    public static final String T_USER_ID = "user_id";
    public static final String T_NAME = "name";
    public static final String T_REG_ID = "reg_id";
    public static final String T_ROLE_ID = "role_id";
    public static final String T_ACADEMIC_YEAR = "academic_yr";


    private static final String KEY_SHORT_NAME = "short_name";
    private static final String KEY_URL = "url";
    private static final String KEY_DURL = "dUrl";
    private static final String KEY_APPVERSION = "app_version";
    private static final String KEY_PWD = "password";
    private static final String KEY_OnlineExamScreen1 = "OnlineExam";
    private static final String KEY_AllotDeallotScreen1 = "AllotDeallot1";
    private static final String KEY_AllotDeallotScreen2 = "AllotDeallot2";
    private static final String KEY_EvaluationScreen1 = "EvaluationScreen1";
    private static final String KEY_EvaluationScreen2 = "EvaluationScreen2";
    private static final String KEY_EvaluationScreen3 = "EvaluationScreen3";


    private static final String OnlineExamNewFeature = "OnlineExamNewFeature";
    private static final String AllotDeallotNewFeature = "AllotDeallotNewFeature";
    private static final String EvaluationNewFeature = "EvaluationNewFeature";

    public static synchronized SharedClass getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedClass(context);
        }
        return mInstance;
    }


    public SharedClass saveSharedPrefrences(String key, String value) {
        Log.i("key " + key, " value " + value);
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(key, value).commit();
        return null;
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_NAME, null) != null;
    }

    boolean userLogin(String user_id, int reg_id, String name, String academic_yr){

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_ID, user_id);
        editor.putInt(KEY_REG_ID, reg_id);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_ACADEMIC_YEAR, academic_yr);
        editor.apply();

        return true;
    }

    //----------------------------------------



    public boolean TeacherName(String teacher_name){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(T_NAME, teacher_name);
        editor.apply();
        return true;
    }

    void setPassword(String pwd){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PWD, pwd);
        editor.apply();
    }

    public String getPassword(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PWD, null);
    }

    public boolean newLogin(String short_name){

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_SHORT_NAME, short_name);
        editor.apply();

        return true;
    }


    public boolean newUrl(String url){

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_URL, url);
        editor.apply();

        return true;
    }


    public boolean newdUrl(String dUrl){

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_DURL, dUrl);
        editor.apply();

        return true;
    }

    public boolean newAppVersion(String app_version){

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_APPVERSION,app_version);
        editor.apply();

        return true;
    }

    //----------------------------------------



    String loadSharedPreference_UserId() {
        SharedPreferences pref = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String stateid = pref.getString("T_name", "");
        return stateid;
    }

    String loadSharedPreference_TName() {
        SharedPreferences pref = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String stateid = pref.getString("user_id", "");
        return stateid;
    }

    public String getUserName(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ID, "");
    }

    public Integer getRegId(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_REG_ID, 0);
    }
    String loadSharedPreference_Password() {
        SharedPreferences pref = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String stateid = pref.getString("password", "");
        return stateid;
    }


    public String loadSharedPreference_PasswordNew() {
        SharedPreferences pref = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String stateid = pref.getString("password_new", "");
        return stateid;
    }
    public String loadSharedPreference_DefaultPassword() {
        SharedPreferences pref = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String stateid = pref.getString("default_pwd", "");
        return stateid;
    }

    public String loadSharedPreference_userName() {
        SharedPreferences pref = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String stateid = pref.getString("name", "");
        return stateid;
    }

    public String loadSharedPreference_regId() {
        SharedPreferences pref = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String stateid = pref.getString("reg_id", "");
        return stateid;
    }
    public String loadSharedPreference_acdYear() {
        SharedPreferences pref = mContext.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        String stateid = pref.getString("academic_yr", "");
        return stateid;
    }
    public String loadSharedPreference_role_id() {
        SharedPreferences pref = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String stateid = pref.getString("role_id", "");
        return stateid;
    }
    public boolean logout(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences.Editor editor1 = sharedPreferences.edit();

        editor.clear();
        editor1.clear();
        editor.apply();
        editor1.apply();
        return true;
    }


    public String getTeachername(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(T_NAME, null);
    }

    public String getShortname(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SHORT_NAME, null);
    }


    public String getUrl(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_URL, null);
    }
    public String getdUrl(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_DURL, null);
    }


    public String getAppVersion(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_APPVERSION, null);
    }

    public boolean setAcademicYear(String academic_yr) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACADEMIC_YEAR, academic_yr);
        editor.apply();
        return true;
    }

    public String getAcademicYear() {



        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACADEMIC_YEAR, "2021-2022");


    }
    //showcase view prefs

    public void setOnlineExamNewFeature(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OnlineExamNewFeature, "Yes");
        editor.apply();
    }

    public String getOnlineExamNewFeature(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(OnlineExamNewFeature, "No");
    }

    public void setAllotDeallotNewFeature(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AllotDeallotNewFeature, "Yes");
        editor.apply();
    }

    public String getAllotDeallotNewFeature(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(AllotDeallotNewFeature, "No");
    }


    public void setEvaluationNewFeature(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EvaluationNewFeature, "Yes");
        editor.apply();
    }

    public String getEvaluationNewFeature(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(EvaluationNewFeature, "No");
    }

    //---------------------------
    void setOnlineExamScreen1(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_OnlineExamScreen1, "Yes");
        editor.apply();
    }

    public String getOnlineExamScreen1(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_OnlineExamScreen1, "No");
    }

    void setAllotDeallotScreen1(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AllotDeallotScreen1, "Yes");
        editor.apply();
    }

    public String getAllotDeallotScreen1(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_AllotDeallotScreen1, "No");
    }

    void setAllotDeallotScreen2(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AllotDeallotScreen2, "Yes");
        editor.apply();
    }

    public String getAllotDeallotScreen2(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_AllotDeallotScreen2, "No");
    }


    void setEvaluationScreen1(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EvaluationScreen1, "Yes");
        editor.apply();
    }

    public String getEvaluationScreen1(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EvaluationScreen1, "No");
    }


    void setEvaluationScreen2(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EvaluationScreen2, "Yes");
        editor.apply();
    }

    public String getEvaluationScreen2(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EvaluationScreen2, "No");
    }

    void setEvaluationScreen3(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EvaluationScreen3, "Yes");
        editor.apply();
    }

    public String getEvaluationScreen3(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EvaluationScreen3, "No");
    }
}
