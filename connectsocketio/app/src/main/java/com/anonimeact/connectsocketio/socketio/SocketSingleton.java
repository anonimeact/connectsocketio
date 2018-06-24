package com.anonimeact.connectsocketio.socketio;

/**
 * Created by anonimeact on 24/06/18.
 * Email didiyuliantos@gmail.com
 */

import android.content.Context;

import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketSingleton {
    private static SocketSingleton instance;
    private Socket mSocket;
    private Context context;

    public SocketSingleton(Context ctx) {
        this.context = ctx;
        this.mSocket = getServerSocket();
    }

    public static SocketSingleton get(Context ctx){
        if(instance == null){
            instance = getSync(ctx);
        }
        instance.context = ctx;
        return instance;
    }

    private static synchronized SocketSingleton getSync(Context context) {
        if(instance == null){
            instance = new SocketSingleton(context);
        }
        return instance;
    }

    public Socket getSocket(){
        return this.mSocket;
    }

    public Socket getServerSocket() {
        try {
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.reconnection = true;
            mSocket = IO.socket("String url socket service", opts);
            return mSocket;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
