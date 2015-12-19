

package cn.smallyard;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.*;


/**
 * 任务监听者
 */
public class JobHandler {

    private static final String APP_KEY = "567392ee4407a3cd028aacf6";

    private static MqttAsyncClient mqttAsyncClient;

    private static String listenTopic;

    static {
        try {
            mqttAsyncClient = MqttAsyncClient.createMqttClient(APP_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听开始
     */
    public static void start(final String topic) {
        listenTopic = topic;
        connect();
        listen();
    }

    public static void publish(String msg) {
        try {
            mqttAsyncClient.publish(listenTopic + APP_KEY, msg.getBytes(), 1, false, null, new IMqttActionListener() {

                public void onFailure(IMqttToken arg0, Throwable arg1) {
                    System.out.println("消息返回失败");
                }

                public void onSuccess(IMqttToken arg0) {
                    System.out.println("消息返回成功");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // 连接服务器
    private static void connect() {
        try {
            mqttAsyncClient.connect(new IMqttActionListener() {
                public void onSuccess(IMqttToken arg0) {
                    System.out.println("连接服务器成功.");
                    subscribe();
                }

                public void onFailure(IMqttToken arg0, Throwable arg1) {
                    System.out.println("连接服务器失败");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private static void subscribe() {
        try {
            mqttAsyncClient.subscribe(listenTopic, 1, null, new IMqttActionListener() {
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("成功监听主题: " + StringUtils.join(asyncActionToken.getTopics(), ","));
                }

                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.err.println("监听失败");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 监听消息
    private static void listen() {
        mqttAsyncClient.setCallback(new MqttCallback() {
            public void connectionLost(Throwable throwable) {

            }

            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String cmd = new String(message.getPayload());
                System.out.println("接收到命令：" + cmd);
                Thread thread = new Thread(new JobExecutor(cmd));
                thread.start();
            }

            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }
}
