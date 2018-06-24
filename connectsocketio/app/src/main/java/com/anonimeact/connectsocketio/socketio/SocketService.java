package com.anonimeact.connectsocketio.socketio;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.socket.client.Ack;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;

/**
 * Created by anonimeact on 24/06/18.
 * Email didiyuliantos@gmail.com
 */

public class SocketService extends Service {
    private Socket mSocket;
    private String TAG = ".SockerService";
    private Context mContext;


    @Override
    public void onCreate() {
        this.mContext = getApplicationContext();

    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, int startId) {
        SocketSingleton singleton = SocketSingleton.get(mContext);
        mSocket = singleton.getSocket();

        // Set header on evebt transport
        mSocket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Transport transport = (Transport) args[0];
                transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        @SuppressWarnings("unchecked")
                        Map<String, List<String>> headers = (Map<String, List<String>>) args[0];
                        headers.put("YOUR_KEY", Collections.singletonList("KEY/TOKEN"));
                    }
                });
            }
        });

        mSocket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // Log.d(TAG, "CONNECT ERROR\n" + args[0]);
                //mSocket.connect();
            }
        });

        // On ERROR
        mSocket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "SOCKET CONNECT ERROR " + args[0].toString());

            }
        });

        // ON CONNECT
        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "SOCKET CONNECTED");
            }
        });

        // ON DISCONNECT
        mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "SOCKET DISCONNECTED");

            }
        });

        // Custom Listen
        mSocket.on("LISTEN_EVENT", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // Do Something you want
            }
        });

        mSocket.connect();
        return START_STICKY;
    }

    private void EmitSomething() {
        String event = "YOUR_EMIT_EVENT";
        mSocket.emit(event, "YOUR PARAM(Can be object beside server side)", new Ack() {
            @Override
            public void call(Object... args) {
                // Do something to your result
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Disconnect socket when service gone
        mSocket.disconnect();
    }

    /**
     * Keep your service allways work in backgroun in some device (ex:xiaomi phone)
     *
     * @param rootIntent
     */

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceTask = new Intent(getApplicationContext(), this.getClass());
        restartServiceTask.setPackage(getPackageName());
        PendingIntent restartPendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager myAlarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        myAlarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartPendingIntent);

        super.onTaskRemoved(rootIntent);
    }
}