package QQClient.qqframe;

import QQClient.service.QQServer;

/**
 * @Author: Ronnie LEE
 * @Date: 2022/9/21 - 09 - 21 - 上午9:51
 * @Description: com.llq.qq.qqframe
 * @version: 1.0
 * 创建QQServer，启动后台服务
 */
public class QQFrame {
    public static void main(String[] args) {
        new QQServer();
    }
}
