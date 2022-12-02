package QQClient.qqcommon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author: Ronnie LEE
 * @Date: 2022/9/20 - 09 - 20 - 下午12:22
 * @Description: com.llq.qqcommon
 * @version: 1.0
 * 表示客户端和服务端通信时的消息对象
 *
 */
public class Message implements Serializable {
    //  为了增加兼容性
    private static final long serialVersionUID =1L;

    private String sender;  //  发送者
    private String receiver;    //  接收者
    private String content;    //  消息内容
    private String sendTime;    //  发送时间

    private ArrayList<Message> arrayList;   //  离线消息组

    private ArrayList<Message> file_offLine_db;   //  离线文件组

    public ArrayList<Message> getFile_offLine_db() {
        return file_offLine_db;
    }

    public void setFile_offLine_db(ArrayList<Message> file_offLine_db) {
        this.file_offLine_db = file_offLine_db;
    }

    //  我们未来的消息类型很多，所以我们需要在这里再加一个字段
    private String mesType; //  消息类型 [可以在接口中定义消息类型]

    //  进行扩展 和文件相关的成员
    private byte[] fileBytes;
    private int fileLen = 0;
    private String dest;    //  将文件传输到哪里
    private String src;     //  源文件路径

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }

    public ArrayList<Message> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Message> arrayList) {
        this.arrayList = arrayList;
    }
}