package io.github.sdxqw.server;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@Getter(AccessLevel.MODULE)
public class TCPClient<T> {
    private final Socket socket;

    @SneakyThrows
    public TCPClient(TCPServer<T> server) {
        socket = server.getServerSocket().accept();
    }

    @SneakyThrows
    public void receive(Consumer<T> dataHandler) {
        InputStream inputStream = socket.getInputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = inputStream.read(buffer);
        String request = new String(buffer, 0, bytesRead).trim();
        if (!request.isEmpty()) {
            dataHandler.accept((T) request);
        }
    }

    @SneakyThrows
    public void send(T data) {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(data);
        System.out.println("Sending data: " + data);
    }

    @SneakyThrows
    public void close() {
        socket.close();
    }
}