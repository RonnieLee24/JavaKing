package QQClient.service;

import QQClient.qqcommon.Message;
import QQClient.qqcommon.MessageType;
import QQClient.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.invoke.VarHandle;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @Author: Ronnie LEE
 * @Date: 2022/9/20 - 09 - 20 - 16:38
 * @Description: QQClient.service
 * @version: 1.0
 * 该类完成用户登录验证和用户注册等功能
 */
public class UserClientService {

    //  因为我们可能在其它地方使用user信息，因此设计成成员属性（可以利用set和get方法进行访问）
    private User user = new User();
    //  因为 Socket 在其它地方也要使用，因此也做成属性【将来还要存放在线程里面的】
    private Socket socket;

    //  根据 userId 和 pwd 到服务器验证该用户是否合法
    public boolean checkUser(String userId, String pwd) {
        boolean b = false;  //  变量 b 判断 checkUser 是否通过

        //  设置 user对象属性
        user.setUserId(userId);
        user.setPasswd(pwd);
        //  连接到服务端，发送 user对象
        try {
            socket = new Socket("192.168.8.228", 9999);
            //  得到 ObjectOutputStream 对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);  // 发送 user 对象

            //  读取从服务器回复的 Message 对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();  //  向下转型

            if ((MessageType.MESSAGE_LOGIN_SUCCEED).equals(ms.getMesType())){   //  登录成功了

                b = true;
                //  创建一个和服务器端保持通信的线程  ---> 创建一个类 ClientConnectServerThread
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                //  启动客户端的线程
                clientConnectServerThread.start();

                //  这里为了后面客户端的扩展，我们将线程放入到集合管理，再创建一个类 ManageClientConnectServerThread
                ManageClientConnectServerThread.addClientConnectServerThread(userId, clientConnectServerThread);

            } else {
                //  如果登录失败，就不能启动和服务器通信的线程，要关闭 Socket
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;
    }

    //  向服务器端请求在线用户列表
    public void getOnLineUsersList() {

        //  发送一个 Message，类型是 String MESSAGE_GET_ONLINE_USERS
        Message message = new Message();
        message.setSender(user.getUserId());    //  设置发送方标识，即：userId
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_USERS);
        try {
            //  发送给服务器
            //  将来客户端也有可能有多个线程 ---> 应该获得当前线程的 Socket 对应的 oos 对象
            ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(user.getUserId()); //    根据 UserID 获取对应线程
            Socket socket = clientConnectServerThread.getSocket();  //  获取对应的 socket
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  编写方法，退出客户端，并给服务端发送一个退出系统得 message 对象
    public void logout() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getUserId());    //  一定要指定我是哪个 客户端 id
        
        //  发送 message
        try {
            //  将来如果有多个 socket 时，按照如下方法来写
            //  不然的话，你这个退出的消息就不知道怎么发了
            //  从管理线程得 集合（ManageClientConnectServerThread）里面获取一个线程（根据userId），再拿到对应得 socket，然后再去获得它的一个输出流对象
             ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(user.getUserId()).getSocket().getOutputStream());
//            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println(user.getUserId() + "退出系统 ");
            System.exit(0); //  结束进程
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}