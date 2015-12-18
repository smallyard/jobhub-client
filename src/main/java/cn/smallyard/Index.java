package cn.smallyard;

/**
 * 程序入口
 */
public class Index {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("请输入参数topic");
        } else {
            JobListener.start(args[0]);
        }
    }
}
