package cn.linjk.testim.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import cn.linjk.testim.MainActivity;
import cn.linjk.testim.R;
import cn.linjk.testim.app.APP;

/**
 * Created by Ye on 2016/7/12.
 */
public class LoginActivity extends Activity{
    private static final String TAG="LoginActivity";
    private EditText et_account;
    private EditText et_password;
    private Button btn_login;
    String account;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        et_account= (EditText) findViewById(R.id.et_account);
        et_password= (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                account = et_account.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if ((!TextUtils.isEmpty(account)) && password.length() != 0) {
                    btn_login.setSelected(true);
                } else {
                    btn_login.setSelected(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_login.isSelected()) {
                    login(account, password);
                }
            }
        });
    }

    /**
     * 登录用户
     * @param username
     * @param password
     */
    public void login(final String username,String password){
        JMessageClient.login(username, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    Log.e(TAG, "登录成功");
                    //创建消息对象
                    Message message = JMessageClient.createSingleTextMessage(username, "你好吗");
                    JMessageClient.sendMessage(message);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Log.e(TAG, "不成功");
                }
            }
        });
    }
}
