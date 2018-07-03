package com.pyrky_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.pyrky_android.activity.SignInActivity;
import com.pyrky_android.activity.SignUpActivity;

/**
 * Created by thulirsoft on 7/3/18.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseAction";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                login();
                return true;
            case R.id.regiser:
                register();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
   /* void accountBal(){
        Intent i = new Intent();
        i.setClass(this, AccountBalance.class);
        startActivity(i);
    }*/
    private void login(){
        Intent i = new Intent();
        i.setClass(this, SignInActivity.class);
        startActivity(i);
    }
    private void register(){
        Intent i = new Intent();
        i.setClass(this, SignUpActivity.class);
        startActivity(i);
    }
    boolean isLogin(){
        String token = getLoginToken();
        if(token == null || token.isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    String getLoginToken(){

        SharedPreferences sharedPref = PreferenceManager.
                getDefaultSharedPreferences(this.getApplication());

        String token = sharedPref.getString(getString(R.string.auth_token), null);
        return token;
    }
   /* void sendMessageToCloudFunction(HttpUrl.Builder httpBuider) {

        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder().
                url(httpBuider.build()).build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "error response firebase cloud function");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BaseAction.this,
                                "Action failed please try gain.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                ResponseBody responseBody = response.body();
                String resp = "";
                if (!response.isSuccessful()) {
                    Log.e(TAG, "action failed");
                    resp = "Failed perform the action, please try again";
                } else {
                    try {
                        resp = responseBody.string();
                        Log.e(TAG, "Response " + resp);
                    } catch (IOException e) {
                        resp = "Problem in reading response";
                        Log.e(TAG, "Problem in reading response " + e);
                    }
                }
                runOnUiThread(responseRunnable(resp));
            }
        });
    }
    abstract  Runnable responseRunnable(final String responseStr);
    */
}
