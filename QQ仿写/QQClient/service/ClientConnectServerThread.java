package QQClient.service;

import QQClient.qqcommon.Message;
import QQClient.qqcommon.MessageType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Spliterator;

/**
 * @Author: Ronnie LEE
 * @Date: 2022/9/20 - 09 - 20 - 17:03
 * @Description: QQClient.service
 * @version: 1.0
 */
public class ClientConnectServerThread extends Thread{
    //  该线程需要持有 Socket
    private Socket socket;

    //  构造器可以接收一个 Socket 对象
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    //  为了更方便地得到 Socket
    public Socket getSocket() {
        return socket;
    }


    @Override
    public void run() {
        //  因为 Thread 需要在后台和服务进行通信，因此使用 while循环
        while (true) {
            System.out.println("客户端线程，等待读取从服务器发来的消息");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream()); //  通过 socket 获取 流对象
                Message ms = (Message) ois.readObject();    //  如果服务器没有发送 Message 对象，线程会阻塞在这里

                //  注意，后面我们需要去使用 message
                //  判断这个 message 类型，然后做相应的业务处理
                //  如果读取到的是 服务器返回的在线用户列表
                if (MessageType.MESSAGE_RET_ONLINE_USERS.equals(ms.getMesType())){
                    //  取出在线用户列表信息，并打印
                    String[] onLineUsers = ms.getContent().split(" ");
                    System.out.println("\n==========当前在线用户列表=========");
                    for (int i = 0; i < onLineUsers.length; i++) {
                        System.out.println("用户：" + onLineUsers[i]);
                    }
                } else if ((MessageType.MESSAGE_COMM_MES.equals(ms.getMesType()))){ //  如果接收到的是 普通聊天消息
                    //  把从服务器端转发的消息，显示到控制台即可
                    if (ms.getArrayList() != null){
                        System.out.println("收到离线留言如下：");
                        ArrayList<Message> arrayList = ms.getArrayList();
                        for (Message message : arrayList) {
                            System.out.println("收到来自 " + message.getSender() + "的离线留言");
                            System.out.println(message.getContent());
                            System.out.println("===============");
                        }
                    }else {
                        System.out.println("\n" + ms.getSender() + " 对 " + ms.getReceiver() + " 说： " + ms.getContent());
                    }
                } else if ((MessageType.MESSAGE_TO_ALL_MES.equals(ms.getMesType()))){
                    //  收到来自 ××× 的群发消息
                    System.out.println("\n" +"收到来自 " + ms.getSender() + " 的群发消息" + "\n" + ms.getContent());
                } else if (MessageType.MESSAGE_FILE_MES.equals(ms.getMesType())){
                    if(ms.getFile_offLine_db()!=null) {   //  离线文件处理
                        System.out.println("收到离线文件");
                        ArrayList<Message> file_offLine_db = ms.getFile_offLine_db();
                        for (Message message : file_offLine_db) {
                            System.out.println("\n收到来自" + message.getSender() + " 给 " + message.getReceiver() + " 发送文件 "+
                                    message.getSrc() + "到我的电脑的" + message.getDest());

                            //  取出字节数组，通过文件输出流写出到磁盘
                            byte[] fileBytes = message.getFileBytes();
                            FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                            fileOutputStream.write(fileBytes);
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            System.out.println("\n 保存文件成功~");
                        }
                    }else {
                        System.out.println("\n" + ms.getSender() + " 给 " + ms.getReceiver() + " 发送文件 " +
                                ms.getSrc() + " 到我的电脑的" + ms.getDest());
                        //  取出 ms 的文件字节数组，通过文件输出流写出到磁盘
                        byte[] fileBytes = ms.getFileBytes();
                        FileOutputStream fileOutputStream = new FileOutputStream(ms.getDest());
                        fileOutputStream.write(fileBytes);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        System.out.println("\n 保存文件成功~");
                    }

                }else if (MessageType.MESSAGE_UNREGISTER.equals(ms.getMesType())){
                    System.out.println("你请求发送的用户" + ms.getReceiver() + "未注册，请从注册用户中选取");
                }

                else {
                    System.out.println("是其它类型的message，暂时不处理");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
