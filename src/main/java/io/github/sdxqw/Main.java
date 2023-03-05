package io.github.sdxqw;

import io.github.sdxqw.server.TCPServer;
import lombok.SneakyThrows;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        new TCPServer<>(1212).accept(client -> {
            client.send("Hello, World!");
            client.receive(data -> {
                System.out.println("Received data: " + data);
            });
        });

        new TCPServer<>(1213).accept(client -> {
            client.receive(data -> {
                System.out.println("Received data: " + data);
            });
        });
    }
}