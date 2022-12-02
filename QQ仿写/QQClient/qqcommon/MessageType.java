package QQClient.qqcommon;

/**
 * @Author: Ronnie LEE
 * @Date: 2022/9/20 - 09 - 20 - 下午3:32
 * @Description: com.llq.qqcommon
 * @version: 1.0
 * 表示消息类型
 */
public interface MessageType {
    //  1）在接口中定义了一些常量
    //  2) 不同的常量值，表示不同的消息类型
    String MESSAGE_LOGIN_SUCCEED = "1"; //  表示登录成功
    String MESSAGE_LOGIN_FAIL = "2"; //  表示登录失败
    String MESSAGE_COMM_MES = "3";  //  普通信息包
    String MESSAGE_GET_ONLINE_USERS = "4";  //  请求返回在线用户列表
    String MESSAGE_RET_ONLINE_USERS = "5";  //  返回在线用户列表
    String MESSAGE_CLIENT_EXIT = "6";  //  客户端退出请求
    String MESSAGE_TO_ALL_MES = "7";  //  群发消息报
    String MESSAGE_FILE_MES = "8";  //  文件消息（发送文件）
    String MESSAGE_UNREGISTER = "9";  //  目的用户未注册
}
