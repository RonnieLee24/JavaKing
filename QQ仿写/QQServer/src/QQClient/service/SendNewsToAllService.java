package QQClient.service;

import QQClient.qqcommon.Message;
import QQClient.qqcommon.MessageType;
import QQClient.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

/**
 * @Author: Ronnie LEE
 * @Date: 2022/12/1 - 12 - 01 - 上午11:36
 * @Description: QQClient.service
 * @version: 1.0
 */
public class SendNewsToAllService implements Runnable {

    @Override
    public void run() {
        while (true){
            System.out.println("请输入服务器要推送的消息： [输入 exit 推出推送服务]");
            String news = Utility.readString(100);
            if ("exit".equals(news)){
                break;
            }
            //  构建一个群发消息
            Message message1 = new Message();
            message1.setSender("服务器");
            message1.setContent(news);
            message1.setSendTime(new Date().toString());
            message1.setMesType(MessageType.MESSAGE_TO_ALL_MES);
            System.out.println("服务器推送消息给所有人说：" + news);

            //  拿到所有与服务器通讯的线程
            HashMap<String, ServerConnectClientThread> hm = ManageServerConnectClientThread.getHm();

            if (hm.isEmpty()){
                System.out.println("没有用户在线，推送失败！！！");
            }


            for (String onLineUserId : hm.keySet()) {
                ServerConnectClientThread serverConnectClientThread = hm.get(onLineUserId);
                Socket socket = serverConnectClientThread.getSocket();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
