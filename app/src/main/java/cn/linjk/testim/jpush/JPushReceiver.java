package cn.linjk.testim.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;
import cn.linjk.testim.app.APP;

/**
 * Created by LinJK on 6/21/16.
 */
public class JPushReceiver extends BroadcastReceiver{

    private static final String TAG = "JPushReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[JPushREceiver] onReceive: " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())){
            //SDK 向 JPush Server 注册所得到的注册 全局唯一的 ID ，可以通过此 ID 向对应的客户端发送消息和通知。
            String regID = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[接收Registration Id]: " + regID);
        }
        else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[接收到通知推送]");

            try {
                Intent msgIntent = new Intent(APP.MESSAGE_RECEIVED_ACTION);

                if (bundle != null){
                    msgIntent.putExtra("model", bundle);
                    context.sendBroadcast(msgIntent);
                }
                else{
                    Log.e(TAG, "推送消息格式错误，不作处理");
                }
            }
            catch (Exception e){
                Log.e(TAG, "[处理通知推送发送异常:]");
                e.printStackTrace();
            }
        }
        else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())){
            Log.d(TAG, "[用户点击打开了通知]");

        }
        else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, intent.getAction() + " connected state change to " + connected);
        }
        else{
            Log.e(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private static String printBundle(Bundle bundle){
        StringBuilder sb = new StringBuilder();

        for (String key : bundle.keySet()){
            if ( key.equals(JPushInterface.EXTRA_NOTIFICATION_ID) ){
                sb.append("\nkey: " + key + ", value: " + bundle.getInt(key));
            }
            else if ( key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE) ) {
                sb.append("\nkey: " + key + ", value: " + bundle.getBoolean(key));
            }
            else if ( key.equals(JPushInterface.EXTRA_EXTRA) ){
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no extra data.");
                    continue;
                }
            }
            else{
                sb.append("\nkey: " + key + ", value: " + bundle.getString(key));
            }
        }

        return sb.toString();
    }
}
