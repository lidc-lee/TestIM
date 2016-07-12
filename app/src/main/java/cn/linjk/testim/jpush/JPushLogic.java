package cn.linjk.testim.jpush;

/**
 * Created by LinJK on 6/21/16.
 */
public class JPushLogic {
    private static final String TAG = "JPushLogic";

    public static boolean isWarn(String title) {
        return title.contains("告警");
    }
}
