package in.aceventura.evolvuschool.teacherapp.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 30-May-18.
 */

public class LoginData implements Serializable {
    private String user_id, password, user_name, reg_id, login_type,acd_yr,default_pwd;

    public String getDefault_pwd() {
        return default_pwd;
    }

    public void setDefault_pwd(String default_pwd) {
        this.default_pwd = default_pwd;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getReg_id() {
        return reg_id;
    }

    public void setReg_id(String reg_id) {
        this.reg_id = reg_id;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getAcd_yr() {
        return acd_yr;
    }

    public void setAcd_yr(String acd_yr) {
        this.acd_yr = acd_yr;
    }
}
