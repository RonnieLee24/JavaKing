package QQClient.service;

import QQClient.qqcommon.Message;
import QQClient.qqcommon.MessageType;

import java.io.*;
import java.net.Socket;

/**
 * @Author: Ronnie LEE
 * @Date: 2022/12/1 - 12 - 01 - 0:06
 * @Description: QQClient.service
 * @version: 1.0
 * 该类 / 对象完成 文件传输服务
 */
public class FileClientService {

    public void sendFileToOne(String src, String dest, String senderId, String receiverId){
        //  读取 src 文件 ---> message 对象
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(senderId);
        message.setReceiver(receiverId);
        message.setSrc(src);
        message.setDest(dest);

        //  需要将文件读取
        FileInputStream fileInputStream = null;
        byte[] fileBytes = new byte[(int)new File(src).length()];
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);    //  将 src 文件读入到程序的字节数组
            //  将文件对应的字节数组设置 message
            message.setFileBytes(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //  关闭流
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //  提示信息
        System.out.println("\n" + senderId + " 给 " + receiverId + " 发送文件 " + src + " 到对方目录 " + dest);

        //  发送 [拿到 socket 输出流]
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
