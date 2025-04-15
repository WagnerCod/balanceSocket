package cordova.plugin.balanceSocket;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import android.util.Log;
import android.content.Context;

public class BalanceSocket extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("connect".equals(action)) {
            String host = args.getString(0);
            int port = args.getInt(1);

            connect(host, port, callbackContext);
            return true;
        }
        return false;
    }

    private void connect(String host, int port, CallbackContext callbackContext) {
        cordova.getThreadPool().execute(() -> {
            try (Socket socket = new Socket(host, port)) {
                Log.d("TAG", "Conectado a " + host + ":" + port);            

                InputStream in = socket.getInputStream();
                byte[] buffer = new byte[1024];
                int length = in.read(buffer);
                String peso = new String(buffer, 0, length).trim();

                callbackContext.success(peso);
                Log.d("TAG", "Peso recebido: " + peso);

            } catch (Exception e) {
                Log.d("TAG", "Erro ao conectar: " + e.getMessage());
                callbackContext.error("Erro ao conectar: " + e.getMessage());
            }
        });
    }
}
