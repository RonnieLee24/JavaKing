package QQClient.qqclient.view;

import QQClient.service.FileClientService;
import QQClient.service.Private_Group_Chat;
import QQClient.service.UserClientService;
import QQClient.utils.Utility;
import jdk.jshell.execution.Util;

/**
 * @Author: Ronnie LEE
 * @Date: 2022/9/20 - 09 - 20 - 15:45
 * @Description: QQClient.qqclient.view
 * @version: 1.0
 * 客户端菜单界面
 */
public class QQView {

    private boolean loop = true;    //  控制是否显示菜单
    private String key = "";    //  接收用户的键盘输入
    private UserClientService userClientService = new UserClientService();  //  对象用于登录服务器 / 注册用户
    private Private_Group_Chat privateGroupChat = new Private_Group_Chat();    //  对象用户 私聊 / 群聊
    private FileClientService fileClientService = new FileClientService();      //  对象用于传输文件

    public static void main(String[] args) {
        QQView qqView = new QQView();
        qqView.mainMenu();
        System.out.println("客户端退出系统");
    }

    //  显示主菜单
    private void mainMenu() {
        while (loop){
            System.out.println("=============欢迎登录网络通信系统==============");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.println("请输入你的选择：");
            
            key = Utility.readString(1);    //  限定输入字符串的长度

            //  根据用户的输入，来处理不通的逻辑
            switch (key){
                case "1":
                    System.out.println("请输入用户号：");
                    String userID = Utility.readString(50);
                    System.out.println("请输入密  码：");
                    String pwd = Utility.readString(50);
                    //  这里就比较麻烦了，需要到服务端去验证该用户是否合法 （很多代码）
                    //  编写一个类 UserClientService 【用户登录 / 注册】

                    if (userClientService.checkUser(userID, pwd)) { //  还没写完，先把整个逻辑打通
                        System.out.println("=============欢迎 (用户" + userID + " 登录成功) ==============");
                        //  进入到二级菜单
                        while (loop){
                            System.out.println("\n=============网络通信系统二级菜单(用户 " + userID + " )=========");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.println("请输入你的选择： ");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    //  写一个方法，来获取在线用户列表
                                    userClientService.getOnLineUsersList();
                                    break;
                                case "2":
                                    System.out.println("请输入想对大家说的话：");
                                    String s = Utility.readString(100);
                                    //  调用一个方法，将消息封装成 message 对象，发送给服务端
                                    privateGroupChat.sendMessageToALL(s, userID);
                                    break;
                                case "3":
                                    System.out.println("请输入想要聊天的用户号（在线）：");
                                    String receiverId = Utility.readString(50);
                                    System.out.println("请输入想输入的话");
                                    String content = Utility.readString(100);
                                    //  编写一个方法，将消息发送给服务器
                                    privateGroupChat.sendMessageToOne(content, userID, receiverId);
                                    System.out.println("私聊消息");
                                    break;
                                case "4":
                                    System.out.println("你想把文件发送给谁：（在线）");
                                    String receiverId1 = Utility.readString(50);
                                    System.out.println("请输入要发送文件的路径：（形式：d:/xx.jpg）");
                                    String src = Utility.readString(100);
                                    System.out.println("请输入你要发送到对方的路径");
                                    String dest = Utility.readString(100);
                                    fileClientService.sendFileToOne(src, dest, userID, receiverId1);
                                    break;
                                case "9":
                                    //  调用方法，给服务器发送一个退出系统得 message
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }
                        }
                    }else { //  登录服务器失败了
                        System.out.println("========登录失败=========");
                    }

                    break;
                case "9":
//                    //  调用方法，给服务器发送一个退出系统得 message
//                    userClientService.logout();
                    loop = false;
                    break;
            }
        }
    }
}
