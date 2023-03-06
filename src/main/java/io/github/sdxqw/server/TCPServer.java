package io.github.sdxqw.server;

import lombok.SneakyThrows;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public class TCPServer<T> implements AutoCloseable {
    private final ServerSocket serverSocket;

    public TCPServer(String host, int port) throws Exception {
        serverSocket = new ServerSocket(port, 0, InetAddress.getByName(host));
    }

    public TCPServer(int port) throws Exception {
        serverSocket = new ServerSocket(port, 0, InetAddress.getLocalHost());
    }

    public void accept(Consumer<Client<T>> clientHandler) {
        while (!serverSocket.isClosed() && !Thread.currentThread().isInterrupted()) {
            try {
                Socket socket = serverSocket.accept();
                Client<T> client = new Client<>(socket);
                clientHandler.accept(client);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() throws Exception {
        serverSocket.close();
    }

    public static class Client<T> implements AutoCloseable {
        private final Socket socket;
        private final LocalDateTime startTime;

        public Client(Socket socket) {
            this.socket = socket;
            this.startTime = LocalDateTime.now();
        }

        @SneakyThrows
        public void receive(Consumer<T> dataHandler) {
            byte[] buffer = new byte[1024];
            int bytesRead = socket.getInputStream().read(buffer);
            String request = new String(buffer, 0, bytesRead).trim();
            if (!request.isEmpty()) {
                dataHandler.accept((T) request);
            }
        }

        @SneakyThrows
        public void send(T data) {
            socket.getOutputStream().write(data.toString().getBytes());
            System.out.println("Sent response to " + socket.getInetAddress().getHostAddress() + ": " + data);
        }

        @SneakyThrows
        @Override
        public void close() {
            socket.close();
            System.out.println("Client connection closed. Duration: " + Duration.between(startTime, LocalDateTime.now()).getSeconds() + " seconds.");
        }
    }
}
