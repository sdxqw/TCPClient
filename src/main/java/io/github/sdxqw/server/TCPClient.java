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
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter(AccessLevel.MODULE)
public class TCPClient<T> implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(TCPClient.class.getName());

    private final Socket socket;
    private final Consumer<T> dataHandler;
    private final long startTime;

    public TCPClient(Socket socket, Consumer<T> dataHandler) {
        this.socket = socket;
        this.dataHandler = dataHandler;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    @SneakyThrows
    public void run() {
        LOGGER.log(Level.INFO, "New client connection from {0}", socket.getInetAddress().getHostAddress());
        try (InputStream inputStream = socket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            String request = reader.readLine();
            if (request != null) {
                LOGGER.log(Level.INFO, "Received data from {0}: {1}", new Object[]{socket.getInetAddress().getHostAddress(), request});
                dataHandler.accept((T) request);
            }
            out.println("Message received: " + request);
            LOGGER.log(Level.INFO, "Sent response to {0}: {1}", new Object[]{socket.getInetAddress().getHostAddress(), request});
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error handling client connection", e);
        } finally {
            socket.close();
            LOGGER.log(Level.INFO, "Closed client connection from {0}, duration: {1}", new Object[]{socket.getInetAddress().getHostAddress(), (System.currentTimeMillis() - startTime)});
        }
    }
}