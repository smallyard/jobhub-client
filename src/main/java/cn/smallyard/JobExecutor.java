package cn.smallyard;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 任务执行
 */
public class JobExecutor implements Runnable {

    private String cmd;

    public JobExecutor(String cmd) {
        this.cmd = cmd;
    }

    public void run() {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(this.cmd);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            System.out.println(sb.toString());
            JobHandler.publish(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JobHandler.publish("执行失败");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
