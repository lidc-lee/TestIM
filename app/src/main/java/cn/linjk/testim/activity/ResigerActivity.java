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
import cn.jpush.im.api.BasicCallback;
import cn.linjk.testim.R;
import cn.linjk.testim.app.APP;

/**
 * Created by Ye on 2016/7/12.
 */
public class ResigerActivity extends Activity {

    private static final String TAG="ResigerActivity";
    private EditText et_account;
    private EditText et_password;
    private Button btn_login;
    String account;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resig);
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
                    register(account, password);
                }
            }
        });
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
                Log.e(TAG, i + "");
                if (i == 0) {
                    Log.e(TAG, "注册成功");
                    Intent intent = new Intent(ResigerActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Log.e(TAG, "已经注册过");
                }
            }
        });
    }
}
