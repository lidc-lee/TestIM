package cn.linjk.testim.receiver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by Ye on 2016/7/7.
 */
public class NotificationClickEventReceiver{
    private static final String TAG = NotificationClickEventReceiver.class.getSimpleName();

    private Context mContext;
    public NotificationClickEventReceiver(Context applicationContext) {
        mContext = applicationContext;
        //注册接收消息事件
        JMessageClient.registerEventReceiver(this);
        Log.e(TAG,"注册接收消息事件");
    }

    /**
     * 接收信息处理
     * @param event
     */
    public void onEvent(MessageEvent event){
        Message msg =event.getMessage();
        Log.e(TAG,msg.toString());
        switch (msg.getContentType()){
            case text:
                //处理文字消息
                TextContent textContent = (TextContent) msg.getContent();
                textContent.getText();
                break;
            case image:
                //处理图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.getLocalPath();//图片本地地址
                imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                break;
            case voice:
                //处理语音消息
                VoiceContent voiceContent = (VoiceContent) msg.getContent();
                voiceContent.getLocalPath();//语音文件本地地址
                voiceContent.getDuration();//语音文件时长
                break;
            case custom:
                //处理自定义消息
                CustomContent customContent = (CustomContent) msg.getContent();
                customContent.getNumberValue("custom_num"); //获取自定义的值
                customContent.getBooleanValue("custom_boolean");
                customContent.getStringValue("custom_string");
                break;
            case eventNotification:
                //处理事件提醒消息
                EventNotificationContent eventNotificationContent = (EventNotificationContent)msg.getContent();
                switch (eventNotificationContent.getEventNotificationType()){
                    case group_member_added:

                        break;
                    case group_member_removed:

                        break;
                    case group_member_exit:

                        break;
                }
                break;
        }

    }
}
