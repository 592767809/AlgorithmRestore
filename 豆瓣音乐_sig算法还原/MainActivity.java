package com.YotaGit.db;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button generateSigBtn;
    private TextView showRealSig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generateSigBtn = findViewById(R.id.bbt);
        showRealSig = findViewById(R.id.ttt);
        generateSigBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.bbt){
            generateSig();
        }
    }


    private void generateSig(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String salt="bf7dddc7c9cfe6f7";
                    String str="https://frodo.douban.com/api/v2/elessar/subject/27229153/";
                    StringBuilder sb = new StringBuilder();
                    String encodedPath = HttpUrl.parse(str).encodedPath();
                    System.out.println(encodedPath);
                    Log.i("encodedPath",encodedPath);
                    String decode = Uri.decode(encodedPath);
                    sb.append("GET");
                    sb.append("&");
                    Log.i("Uri.encode(decode)",Uri.encode(decode));
                    sb.append(Uri.encode(decode));
                    long currentTimeMillis = System.currentTimeMillis() / 1000;
                    sb.append("&");
                    sb.append(currentTimeMillis);
                    String final_sb = sb.toString();
                    SecretKeySpec secretKeySpec = new SecretKeySpec(salt.getBytes(), "HmacSHA1");
                    Mac instance = Mac.getInstance("HmacSHA1");
                    instance.init(secretKeySpec);
                    String _sig = Base64.encodeToString(instance.doFinal(final_sb.getBytes()), 2);
                    showSig(_sig);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {

                }
            }
        }).start();
    }


    private void showSig(final String realSig){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showRealSig.setText(realSig);
            }
        });

    }
}
