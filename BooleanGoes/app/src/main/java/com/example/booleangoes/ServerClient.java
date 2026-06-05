package com.example.booleangoes;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerClient {

    private static final String HOST = "10.0.2.2"; // emulator -> local PC
    private static final int PORT = 1234;

    public interface ServerCallback {
        void onResult(ArrayList<String> lines);
        void onError(String error);
    }

    public static void sendCommand(String command, ServerCallback callback) {
        new Thread(() -> {
            ArrayList<String> lines = new ArrayList<>();

            try (
                    Socket socket = new Socket(HOST, PORT);
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                writer.println(command);

                String line;
                while ((line = reader.readLine()) != null && !line.equals("END")) {
                    lines.add(line);
                }

                callback.onResult(lines);

            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }).start();
    }
}