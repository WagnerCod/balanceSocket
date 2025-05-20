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
    private Socket socket;
    private boolean isReading = false;
    private String ultimoPeso = "";
    private CallbackContext streamingCallbackContext;
    private static final String TAG = "BalanceSocket";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("connect".equals(action)) {
            Log.d(TAG, "Conectando...");
            String host = args.getString(0);
            int port = args.getInt(1);

            connect(host, port, callbackContext);
            return true;
        }
        if ("disconnect".equals(action)) {
            Log.d(TAG, "Desconectando...");
            disconnect(callbackContext);
            return true;
        }
        return false;
    }

    private void connect(String host, int port, CallbackContext callbackContext) {
        cordova.getThreadPool().execute(() -> {
            try {
                socket = new Socket(host, port);
                Log.d(TAG, "Conectado a " + host + ":" + port);
                InputStream in = socket.getInputStream();
                byte[] buffer = new byte[1024];
                isReading = true;
                streamingCallbackContext = callbackContext;
                while (!socket.isClosed() && isReading)  {
                    int length = in.read(buffer);
                    if (length > 0) {
                        String peso = new String(buffer, 0, length).trim();
                        if (!peso.equals(ultimoPeso)) {
                            ultimoPeso = peso;
                            Log.d(TAG, "Peso novo recebido: " + ultimoPeso);

                            PluginResult result = new PluginResult(PluginResult.Status.OK, ultimoPeso);
                            result.setKeepCallback(true);
                            streamingCallbackContext.sendPluginResult(result);
                        }
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, "Erro ao conectar: " + e.getMessage());
                callbackContext.error("Erro ao conectar: " + e.getMessage());
            }
        });
    }

    private void disconnect(CallbackContext callbackContext) {
        try {
            isReading = false;
            ultimoPeso = "";
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (streamingCallbackContext != null) {
                PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
                result.setKeepCallback(false);
                streamingCallbackContext.sendPluginResult(result);
            }
            callbackContext.success("Desconectado com sucesso.");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao desconectar: " + e.getMessage());
            callbackContext.error("Erro ao desconectar: " + e.getMessage());
        } 
    }
}
