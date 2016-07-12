package cn.linjk.testim.app;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import cn.linjk.testim.jpush.JPushUtil;
import cn.linjk.testim.receiver.NotificationClickEventReceiver;

/**
 * Created by Ye on 2016/7/7.
 */
public class APP extends Application{

    private static final String TAG ="APP";
    public static final String MESSAGE_RECEIVED_ACTION="cn.linjk.testim.MESSAGE_RECEIVED_ACTION";
    protected static APP INSTANCE;
    private PushMessageReceiver pushMessageReceiver=null;

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

    //推送
    private void startJPush(){
        Log.e(TAG,"init");
        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);
        registerMessageReceiver();
    }
    /**
     *设置通知栏样式 - 定义通知栏Layout
     */
    private void setStyleCustom(Context context){
//        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(context,
//                R.layout.layout_notification_custom,
//                R.id.icon, R.id.title, R.id.text);
//        builder.layoutIconDrawable = R.mipmap.icon_haodi;
//        builder.developerArg0 = "developerArg2";
//        JPushInterface.setPushNotificationBuilder(2, builder);

    }
    public void stopJPush(){

            JPushInterface.stopPush(this);
    }
    /**
     * 设置tag
     * @param mobile
     */
    public void setTag(String mobile) {
        Log.e(TAG, mobile);
        //检查有效性
        if (TextUtils.isEmpty(mobile)){
            return;
        }
        // ","隔开的多个 转换成 Set
        String[] sArray = mobile.split(",");
        Set<String> tagSet = new LinkedHashSet<>();
        for (String sTagItem : sArray) {
            if (!JPushUtil.isValidTagAndAlias(sTagItem)) {
                return;
            }
            tagSet.add(sTagItem);
        }
        //调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

    }
    private static final int MSG_SET_TAGS=100;
    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int i, String s, Set<String> set) {
            String logs=null;
            switch (i){
                case 0:
                    logs = "Set tag and alias success";
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    if (JPushUtil.isConnected(getApplicationContext())) {
                        //1分钟后继续推送
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, set), 1000 * 60);
                    }
                    break;
                default:
                    logs = "Failed with errorCode = ";
                    break;

            }
            Log.e("JPush————setTag:" + logs, logs);
        }

    };
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_TAGS:
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;
            }
        }
    };

    //注册
    public void registerMessageReceiver() {
        pushMessageReceiver = new PushMessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        //注册广播
        registerReceiver(pushMessageReceiver, filter);
    }

    /**
     * 注册广播消息接收器
     */
    private class PushMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG,"onReceive");
            if (MESSAGE_RECEIVED_ACTION==intent.getAction()){
                Bundle bundle = intent.getBundleExtra("model");
                if (bundle == null){
                    return;
                }
                Log.e(TAG,bundle.toString());
            }
        }
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
                if (i == 0) {
                    Log.e(TAG, "注册成功");
                } else {
                    Log.e(TAG, "已经注册过");
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
                }else {
                    Log.e(TAG,"不成功");
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
