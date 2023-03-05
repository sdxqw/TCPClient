package io.github.sdxqw.server;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.function.Consumer;

@Getter(AccessLevel.MODULE)
public class TCPServer<T> {
    private final ServerSocket serverSocket;

    @SneakyThrows
    public TCPServer(String host, int port) {
        serverSocket = new ServerSocket(port, 0, InetAddress.getByName(host));
    }

    @SneakyThrows
    public TCPServer(int port) {
        serverSocket = new ServerSocket(port, 0, InetAddress.getLocalHost());
    }

    public void accept(Consumer<TCPClient<T>> clientHandler) {
        while (!serverSocket.isClosed() && !Thread.currentThread().isInterrupted()) {
            TCPClient<T> client = new TCPClient<>(this);
            clientHandler.accept(client);
            client.close();
        }
    }
}