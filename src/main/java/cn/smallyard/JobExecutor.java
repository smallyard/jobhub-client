package cn.smallyard;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 任务执行程序
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
        } catch (Exception e) {
            e.printStackTrace();
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
