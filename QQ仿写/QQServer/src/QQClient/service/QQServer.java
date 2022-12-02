package QQClient.service;

import QQClient.qqcommon.Message;
import QQClient.qqcommon.MessageType;
import QQClient.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Ronnie LEE
 * @Date: 2022/9/20 - 09 - 20 - 下午8:18
 * @Description: com.llq.qq.service
 * @version: 1.0
 * 这是服务端，监听9999端口，等待客户端链接诶，并保持通信
 */
public class QQServer {

    private ServerSocket serverSocket = null;

    //  创建一个集合，存放多个用户，如果是这些用户登录，就认为合法
    //  这里也可以使用 ConcurrentHashMap，可以处理并发的集合，没有线程安全
    //  HashMap 没有处理线程安全，因此在多线程情况下是不安全的
    //  ConcurrentHashMap 处理的线程安全，即线程同步处理，在多线程情况下是安全
    private static HashMap<String, User> validUsers = new HashMap<>();


    static {    //  在静态代码快中，初始化 validUsers
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123456"));
        validUsers.put("300", new User("300", "123456"));
        validUsers.put("至尊宝", new User("至尊宝", "123456"));
        validUsers.put("紫霞仙子", new User("紫霞仙子", "123456"));
        validUsers.put("菩提老祖", new User("菩提老祖", "123456"));

    }

    public static HashMap<String, User> getValidUsers(){
        return validUsers;
    }

    //  验证用户是否有效
    private boolean checkUser(String uerId, String passwd) {

        User user = validUsers.get(uerId);
        //  过关斩将法
        if (user == null) { //  说明 userId没有存在 validUsers 的key 中
            return false;
        }
        if (!(passwd.equals(user.getPasswd()))){   //  userId 正确，但是密码错误
             return false;
        }
        return true;
    }


    public QQServer() {
        //  注意，端口可以卸载配置文件中
        System.out.println("服务端在9999端口监听...");
        try {
            serverSocket = new ServerSocket(9999);
            //  启动推送新闻的线程
            new Thread(new SendNewsToAllService()).start();

            while (true) {  //  当和某个客户端建立链接后，会继续监听，因此while循环
                Socket socket = serverSocket.accept();  //  如果没有客户端连接，就会阻塞在这里
                //  得到 socket 关联的对象输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User)ois.readObject(); //  我们事先知道，客户端发过来的是 User 对象 ---> 向下转型

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                //  创建一个Message 对象，准备回复给客户端 [不管成功与否，都需要给客户端回复一个 Message]
                Message message = new Message();

                //  验证
                if (checkUser(user.getUserId(), user.getPasswd())){
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    //  将 Message 对象回复
                    oos.writeObject(message);
                    //  创建一个线程，和客户端保持通信，同时该线程需要持有 Socket 对象
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, user.getUserId());
                    //  启动线程
                    serverConnectClientThread.start();
                    //  把该线程对象，放入到一个集合中，进行管理
                    ManageServerConnectClientThread.addServerConnectClientThread(user.getUserId(), serverConnectClientThread);

                    //  处理离线消息
                    ConcurrentHashMap<String, ArrayList<Message>> db = ManageServerConnectClientThread.getDb();

                    if (db.containsKey(user.getUserId())){   //  看下是否有你的离线消息
                        //  注意：第一次登录的时候，服务器发送的 是 User 对象，要根据 usrId 来判定
                        System.out.println("离线的用户" + user.getUserId() + "上线了");
                        ObjectOutputStream oos_db = new ObjectOutputStream(ManageServerConnectClientThread.getServerConnectClientThread(user.getUserId()).getSocket().getOutputStream());
                        //  取出 ArrayList 中的 Message 对象
                        ArrayList<Message> messages = db.get(user.getUserId());
                        Message message_db = new Message();
                        message_db.setMesType(MessageType.MESSAGE_COMM_MES);
                        message_db.setArrayList(messages);
                        oos_db.writeObject(message_db);
                        //  清空 db1
                        db.remove(user.getUserId());
                    }

                    //  处理离线文件
                    //  登录时，客户端发送一个 user 对象过来
                    ConcurrentHashMap<String, ArrayList<Message>> file_offLine_db = ManageServerConnectClientThread.getFile_offLine_db();
                    if (file_offLine_db.containsKey(user.getUserId())){
                        System.out.println("离线的用户" + user.getUserId() + "上线了");
                        System.out.println("准备转送离线文件");
                        ObjectOutputStream oos_fileOffLine = new ObjectOutputStream(ManageServerConnectClientThread.getServerConnectClientThread(user.getUserId()).getSocket().getOutputStream());
                        //  取出 ArrayList 中的 files 对象
                        ArrayList<Message> messages = file_offLine_db.get(user.getUserId());

                        Message message_offLineFiles = new Message();
                        message_offLineFiles.setMesType(MessageType.MESSAGE_FILE_MES);
                        message_offLineFiles.setFile_offLine_db(messages);
                        oos_fileOffLine.writeObject(message_offLineFiles);
                        //  清空 file_offLine_db
                        file_offLine_db.remove(user.getUserId());
                    }
                }else{  //  登录失败
                    System.out.println("用户 id=" + user.getUserId() + "pwd=" + user.getPasswd() + "验证失败");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    //  关闭socket
                    socket.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //  如果服务器退出了 while，说明服务器不再监听，因此关闭 ServerSocket
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
