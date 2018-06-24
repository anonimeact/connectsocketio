package com.anonimeact.connectsocketio;

/**
 * Created by anonimeact on 24/06/18.
 * Email didiyuliantos@gmail.com
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anonimeact.connectsocketio.socketio.SocketService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(MainActivity.this, SocketService.class));
    }
}
