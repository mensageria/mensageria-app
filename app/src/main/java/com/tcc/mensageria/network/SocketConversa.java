package com.tcc.mensageria.network;

import android.app.Activity;
import android.text.TextUtils;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.tcc.mensageria.model.Autor;
import com.tcc.mensageria.model.BDUtil;
import com.tcc.mensageria.model.Conversa;
import com.tcc.mensageria.model.Mensagem;
import com.tcc.mensageria.utils.Utility;
import com.tcc.mensageria.view.MensageriaApplication;

import java.util.Calendar;


/**
 * A chat fragment containing messages view and input form.
 */
public class SocketConversa {

    private Socket mSocket;
    private Boolean isConnected = true;
    private Activity mActivity;
    private String mUsername;
    private int mIdConversa;


    public SocketConversa(Activity activity, int idConversa) {
        this.mActivity = activity;
        this.mIdConversa = idConversa;
        mUsername = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    public void conectar(MensageriaApplication app) {
        mSocket = app.getSocket();
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on("new message", onNewMessage);

        mSocket.connect();
    }


    public void desconectar() {
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off("new message", onNewMessage);
    }

    public boolean tentarEnvio(String conteudoMensagem) {
        if (!mSocket.connected()) return false;

        if (TextUtils.isEmpty(conteudoMensagem)) {
            return false;
        }
        //TODO salvar o usuario atual e pegar aqui
        int idAutor = 1;

        BDUtil bdUtil = new BDUtil(mActivity);
        Autor autor = bdUtil.findAutor(idAutor);
        Conversa conversa = bdUtil.findConversa(mIdConversa);
        Mensagem mensagem = new Mensagem(conteudoMensagem, Calendar.getInstance().getTimeInMillis(), autor, conversa);
        bdUtil.addListaMensagem(new Mensagem[]{mensagem});

        // perform the sending message attempt.
        mSocket.emit("new message", conteudoMensagem);
        return true;
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String) args[0];
                    Utility.addJSONNoBanco(data, mActivity);
                }
            });
        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isConnected) {
                        if (null != mUsername)
                            mSocket.emit("add user", mUsername);
                        isConnected = true;
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = false;
                }
            });
        }
    };

    public Socket getSocket() {
        return mSocket;
    }
}

