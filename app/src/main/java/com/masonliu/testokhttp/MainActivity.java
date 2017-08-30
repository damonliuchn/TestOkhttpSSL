package com.masonliu.testokhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /**
         1、验证系统中信任的根证书（默认）
         */

        /**
         2、验证本地证书（certificate pinning），cer 和 pem 格式都可以
         */
        //client = OkHttpClientUtil.getSSLClient(client,this,"12306.cer");

        /**
         3、不验证任何证书
         */
        //client = OkHttpClientUtil.getTrustAllSSLClient(client);

        /**
         4、验证系统中信任的根证书 和 证书域名
         */
//        OkHttpClient.Builder builder = client.newBuilder();
//        builder.hostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                Log.e("verify",hostname);
//                return true;
//            }
//        });

        /**
         5、验证系统中信任的根证书 和 本地证书但忽略过期时间
         */
        client = OkHttpClientUtil.getSSLClientIgnoreExpire(client, this, "toutiao.pem");

        String url2 = "https://kyfw.12306.cn/otn/";
        String url3 = "https://toutiao.io";
        try {
            Request request = new Request.Builder()
                    .url(url3)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("sssss", e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.e("sssss", response.body().string());
                }
            });

        } catch (Exception e) {
            Log.e("sssss", e.toString());
        }

    }
}
