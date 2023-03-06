package io.github.sdxqw;

import io.github.sdxqw.server.TCPServer;
import lombok.SneakyThrows;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        TCPServer<String> server = new TCPServer<>("google.com", 8080);
        server.accept(client -> {
            client.send("Hello, world!");
            client.receive(request -> System.out.println("Received request: " + request));
        });

        server.close();
    }
}