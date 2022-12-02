package QQClient.service;

import QQClient.qqcommon.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Ronnie LEE
 * @Date: 2022/9/21 - 09 - 21 - 上午9:15
 * @Description: com.llq.qq.service
 * @version: 1.0
 * 用于管理和客户端通信的线程
 */
public class ManageServerConnectClientThread {
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();
    private static ConcurrentHashMap<String, ArrayList<Message>> db = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, ArrayList<Message>> file_offLine_db = new ConcurrentHashMap<>();

    //  返回 HashMap 集合 hm
    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    //  添加线程对象到 hm 集合
    public static void addServerConnectClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.put(userId, serverConnectClientThread);
    }
    //  根据 userId 返回 ServerConnectClientThread 线程
    public static ServerConnectClientThread getServerConnectClientThread(String userId) {
        return hm.get(userId);
    }

    //  编写方法，返回在线用户列表
    public static String getOnlineUsers() {
        //  集合的遍历，遍历 hm 的 key
        Iterator<String> iterator = hm.keySet().iterator();
        String onLineUserList = "";

        while (iterator.hasNext()) {
            onLineUserList += iterator.next().toString() + " ";
        }

        return onLineUserList;

    }

    //  返回 db 集合
    public static ConcurrentHashMap<String, ArrayList<Message>> getDb() {
        return db;
    }

    //  增加一个方法，从集合中，移除某个线程对象
    public static void removeServerConnectionThread(String userId) {
        hm.remove(userId);
    }

    //  添加 message 对象到 db 集合
    public static void addArrayListMessage(String usrId, ArrayList<Message> messages){
        db.put(usrId, messages);
    }

    //  根据 receiverId 获得 相应的 ArrayList<Message> 对象
    public static ArrayList<Message> getArrayList(String receiverId){
        return db.get(receiverId);
    }

    //  增加一个方法，从db集合中，删除某个 message 对象
    public static void removeMessage(String receiverId){
        db.remove(receiverId);
    }

    //================================================================================//

    //  返回 file_offLine 集合
    public static ConcurrentHashMap<String, ArrayList<Message>> getFile_offLine_db(){
        return file_offLine_db;
    }

    //  增加一个方法，将 byte[] 字节数组添加到 file_offLine集合中：
    public static void addOffLineFile(String userId, ArrayList<Message> messages){
        file_offLine_db.put(userId, messages);
    }

    //  根据 UserId receiverId 获得 相应的 ArrayList<Message> 对象
    public static ArrayList<Message> getFileBytes(String receiverId){
        return file_offLine_db.get(receiverId);
    }

    //  增加一个方法，从 file_offLine 中删除某个 ArrayList<Message> 对象
    public static void removeFileBytes(String receiverId){
        file_offLine_db.remove(receiverId);
    }

}
