package QQClient.service;

import QQClient.qqcommon.Message;
import QQClient.qqcommon.MessageType;
import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2;

import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Ronnie LEE
 * @Date: 2022/9/20 - 09 - 20 - 下午8:41
 * @Description: com.llq.qq.service
 * @version: 1.0
 * 该类的一个对象和某个客户端保持通信
 */
public class ServerConnectClientThread extends Thread{
    private Socket socket;
    private String userId; //   连接到服务端的用户 id

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() { //  这里线程处于 run 状态，可以发送 / 接收消息
        while (true){
            System.out.println("服务端和客户端" +  userId + "保持通信，读取数据...");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();   //  这里一直会等待客户端发来消息，如果没有的话会一直阻塞在这里


                //  后面会使用 message,根据message的类型，做相应的业务处理
                if(MessageType.MESSAGE_GET_ONLINE_USERS.equals(message.getMesType())) {
                    //  在线用户列表形式 100  200  紫霞仙子 ---》管理线程集合中知道，那么在这里编写方法
                    System.out.println(message.getSender() + "正在请求在线用户列表");
                    String onlineUsers = ManageServerConnectClientThread.getOnlineUsers();
                    //  返回 message
                    //  构建一个 message 对象[类型，内容]，返回给客户端
                    Message message1 = new Message();
                    message1.setMesType(MessageType.MESSAGE_RET_ONLINE_USERS);
                    message1.setContent(onlineUsers);
                    message1.setReceiver(message.getSender());  //  设置message1接收者[即：message 的发送者]，
                    //  返回给客户端
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message1);
                }else if (MessageType.MESSAGE_CLIENT_EXIT.equals(message.getMesType())){    //  客户端退出
                    System.out.println(message.getSender() + " 退出");
                    //  将这个客户端对应线程，从集合删除
                    ManageServerConnectClientThread.removeServerConnectionThread(message.getSender());
                    socket.close(); //  关闭连接
                    //  退出线程
                    break; //   run() 方法结束啦，那么这个线程也就结束了
                }else if (MessageType.MESSAGE_COMM_MES.equals(message.getMesType())){
                    //  根据 message 获取 receiverId，然后再得到对应的线程
                    String receiver = message.getReceiver();
                    if (QQServer.getValidUsers().containsKey(receiver)) {   //  输入的注册用户
                        if (!ManageServerConnectClientThread.getHm().containsKey(receiver)) {  //  接收者离线
                            ConcurrentHashMap<String, ArrayList<Message>> db = ManageServerConnectClientThread.getDb();
                            if (db.get(receiver) == null) {  //  如果之前没有留言信息
                                System.out.println("接收方离线，将消息存到 db");
                                ArrayList<Message> messages = new ArrayList<>();
                                messages.add(message);
                                db.put(receiver, messages);
                            } else { //  之前已经有消息了
                                System.out.println("接收方离线，将消息存到 db(之前已有记录)");
                                db.get(receiver).add(message);  //  就只做 添加操作了
                            }
                        } else { //  接收者在线
                            ServerConnectClientThread serverConnectClientThread = ManageServerConnectClientThread.getServerConnectClientThread(message.getReceiver());
                            //  得到对应 socket 的对象输出流，将 message 对象转发给指定的客户端
                            ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                            oos.writeObject(message);   // 转发，对方用户不在线的话，就可以保存在数据库中，做成离线留言
                        }
                    }else {
                        //  收入的是未注册用户
                        Message message_unregister = new Message();
                        message_unregister.setReceiver(message.getReceiver());
                        message_unregister.setMesType(MessageType.MESSAGE_UNREGISTER);
                        //  因为接收者不存在，所以给客户端返回一个目的用户未注册的 Message
                        ServerConnectClientThread serverConnectClientThread = ManageServerConnectClientThread.getServerConnectClientThread(message.getSender());
                        ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                        oos.writeObject(message_unregister);
                    }
                } else if (MessageType.MESSAGE_TO_ALL_MES.equals(message.getMesType())){
                  //    需要遍历 管理线程的集合，把所有线程的 socket 得到，然后把 message 进行转发即可
                    HashMap<String, ServerConnectClientThread> hm = ManageServerConnectClientThread.getHm();
                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()) {
                        //  取出在线用户 Id
                        String onLineUserId = iterator.next().toString();
                        if (!onLineUserId.equals(message.getSender())){ //  排除自己
                            //  进行转发 message
                            ObjectOutputStream oos = new ObjectOutputStream(hm.get(onLineUserId).getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                    }
                }else if (MessageType.MESSAGE_FILE_MES.equals(message.getMesType())){
                    //  根据 message 获取 receiverId，然后再得到对应的线程
                    //  如果接收者离线
                    String receiver = message.getReceiver();
                    //  根据 message 获取 receiverId，然后再得到对应的线程
                    //  如果接收者离线
                    if (!ManageServerConnectClientThread.getHm().containsKey(receiver)){
                        ConcurrentHashMap<String, ArrayList<Message>> file_offLine_db = ManageServerConnectClientThread.getFile_offLine_db();
                        if (file_offLine_db.get(receiver) == null) {
                            System.out.println("接收方离线，将信息存储到 file_offLine_db");
                            ArrayList<Message> messages = new ArrayList<>();
                            messages.add(message);
                            file_offLine_db.put(receiver, messages);
                        } else { //  之前该用户已有离线文件了
                            System.out.println("接收方离线，将信息保存到 file_offLine_db（之前已有记录）");
                            file_offLine_db.get(receiver).add(message); //  只做添加操作
                        }
                    }else { //  接收方在线
                        ServerConnectClientThread serverConnectClientThread = ManageServerConnectClientThread.getServerConnectClientThread(message.getReceiver());
                        ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                        oos.writeObject(message);
                    }
                }
                else {
                    System.out.println("其他类型的 message，暂时不处理");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
