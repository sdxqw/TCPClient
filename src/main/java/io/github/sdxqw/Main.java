package io.github.sdxqw;

import io.github.sdxqw.server.TCPServer;
import lombok.SneakyThrows;

import java.util.Objects;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        new TCPServer<String>(1212).accept(client -> {
            client.send("Hello, World!");
            client.receive(data -> {
                System.out.println("Received data: " + data);
                if (Objects.equals(data, "exit")) {
                    client.close();
                }
            });
        });
    }
}