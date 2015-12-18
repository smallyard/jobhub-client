

package cn.smallyard;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.*;


/**
 * 任务监听者
 */
public class JobListener {

    private static final String APP_KEY = "567392ee4407a3cd028aacf6";

    /**
     * 监听开始
     */
    public static void start(final String listenTopic) {
        try {
            final MqttAsyncClient mqttAsyncClient = MqttAsyncClient.createMqttClient(APP_KEY);
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

            mqttAsyncClient.connect(new IMqttActionListener() {
                public void onSuccess(IMqttToken arg0) {
                    System.out.println("连接服务器成功.");
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

                public void onFailure(IMqttToken arg0, Throwable arg1) {
                    System.out.println("连接服务器失败");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
