package cn.linjk.testim.app;

import android.app.Application;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import cn.linjk.testim.receiver.NotificationClickEventReceiver;

/**
 * Created by Ye on 2016/7/7.
 */
public class APP extends Application{

    private static final String TAG ="APP";
    protected static APP INSTANCE;
    public APP() {
        INSTANCE = this;
    }

    public static APP get() {
        return INSTANCE;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("JMessageDemoApplication", "Application onCreate");
        JMessageClient.init(getApplicationContext());
        //是否打印LOG
        JPushInterface.setDebugMode(true);
        //设置Notification的模式
        JMessageClient.setNotificationMode(JMessageClient.NOTI_MODE_DEFAULT);
        //注册Notification点击的接收器
        new NotificationClickEventReceiver(getApplicationContext());
    }

    private void startJPush(){
        JPushInterface.init(getApplicationContext());
        JPushInterface.setDebugMode(true);
    }
    /**
     * 注册用户
     * @param username
     * @param password
     */
    public void register(String username,String password){
        JMessageClient.register(username, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i==0){
                    Log.e(TAG,"注册成功");
                }else {
                    Log.e(TAG,"已经注册过");
                }
            }
        });
    }

    /**
     * 登录用户
     * @param username
     * @param password
     */
    public void login(String username,String password){
        JMessageClient.login(username, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i==0){
                    Log.e(TAG,"登录成功");
                    //创建消息对象
                    Message message =JMessageClient.createSingleTextMessage("leelee","你好吗");
                    JMessageClient.sendMessage(message);
                }
            }
        });
    }

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     */
    public void updateUserPassword(String oldPassword, String newPassword){
        JMessageClient.updateUserPassword(oldPassword, newPassword, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {

            }
        });
    }
}
