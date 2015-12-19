package cn.smallyard;

/**
 * 程序入口
 */
public class Index {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("请输入密码");
        } else {
            JobHandler.start(args[0]);
        }
    }
}
