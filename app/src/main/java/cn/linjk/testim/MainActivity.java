package cn.linjk.testim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Message;
import cn.linjk.testim.app.APP;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注册用户
        APP.get().register("leelee","123456");
        APP.get().login("leelee", "123456");
    }

    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        super.onResume();

}

    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }


}
