package io.github.sdxqw.server;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;
        StringBuilder requestBuilder = new StringBuilder();
        while ((line = in.readLine()) != null) {
            requestBuilder.append(line);
            if (line.isEmpty()) {
                break;
            }
        }
        String request = requestBuilder.toString().trim();
        if (!request.isEmpty()) {
            System.out.println("Received data: " + request);
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