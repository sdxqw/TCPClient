# TCPClient
Simple TCP client connection

# Usage
```java
TCPServer<String> server = new TCPServer<>("localhost", 8080);
  server.accept(client -> {
    client.send("Hello, world!");
    client.receive(request -> System.out.println("Received request: " + request));
});

server.close();
```
