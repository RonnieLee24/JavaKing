package QQClient.qqcommon;

import java.io.Serializable;

/**
 * @Author: Ronnie LEE
 * @Date: 2022/9/20 - 09 - 20 - 下午2:38
 * @Description: com.llq.qqcommon
 * @version: 1.0
 * 表示一个用户/客户信息
 *
 */
public class User implements Serializable {
    //  为了增加兼容性
    private static final long serialVersionUID =1L;

    private String userId;
    private String passwd;

    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

}
