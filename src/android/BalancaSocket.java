package cordova.plugin.balancaSocket;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import android.util.Log;
import android.content.Context;

public class BalancaSocket extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("conectarBalanca".equals(action)) {
            String ip = args.getString(0);
            int porta = args.getInt(1);

            conectarBalanca(ip, porta, callbackContext);
            return true;
        }
        return false;
    }

    private void conectarBalanca(String ip, int porta, CallbackContext callbackContext) {
        cordova.getThreadPool().execute(() -> {
            try (Socket socket = new Socket(ip, porta)) {
                Log.d("TAG", "Conectado a " + ip + ":" + porta);            

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
