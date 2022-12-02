package QQClient.service;

import QQClient.qqcommon.Message;
import QQClient.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @Author: Ronnie LEE
 * @Date: 2022/10/24 - 10 - 24 - 16:39
 * @Description: QQClient.service
 * @version: 1.0
 * 该类 提供和消息相关的服务方法
 */
public class Private_Group_Chat {

    /**
     *
     * @param content 内容
     * @param senderId 发送者 id
     * @param receiverId 接收者 id
     */
    public void sendMessageToOne(String content, String senderId, String receiverId){
        //  构建 message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);   //   普通的聊天消息类型
        message.setSender(senderId);
        message.setReceiver(receiverId);
        message.setContent(content);
        message.setSendTime(new java.util.Date().toString());

        System.out.println(senderId + " 对 " + receiverId + " 说 " + content);

        //  发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param content   内容
     * @param senderId  发送者
     */
    public void sendMessageToALL(String content, String senderId){
        //  构建 message 对象
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_TO_ALL_MES);   //    群发消息这种类型
        message.setSender(senderId);
        message.setContent(content);
        message.setSendTime(new java.util.Date().toString());

        System.out.println(senderId + " 对大家说 " + content);

        //  发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
