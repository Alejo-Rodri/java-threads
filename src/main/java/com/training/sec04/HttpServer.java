package com.training.sec04;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//  Throughput
public class HttpServer {
    private static final String INPUT_FILE = "src/main/resources/war-peace.txt";
    private static final int NUMBER_OF_THREADS = 4;

    public static void main(String[] args) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
        startServer(text);
    }

    private static void startServer(String text) throws IOException {
        com.sun.net.httpserver.HttpServer httpServer = com.sun.net.httpserver.HttpServer.create(
                new InetSocketAddress(8000),
                0
        );

        httpServer.createContext("/search", new WordCountHandler(text));
        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        httpServer.setExecutor(executor);
        httpServer.start();
    }

    public record WordCountHandler(String text) implements HttpHandler {

        @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                String[] keyValue = query.split("=");
                String action = keyValue[0];
                String word = keyValue[1];

                if (!action.equals("word")) {
                    exchange.sendResponseHeaders(400, 0);
                    return;
                }

                long count = countWord(word);
                byte[] response = Long.toString(count).getBytes();
                exchange.sendResponseHeaders(200, response.length);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(response);
                outputStream.close();
            }

            private long countWord(String word) {
                long count = 0;
                int index = 0;
                while (index >= 0) {
                    index = text.indexOf(word, index);

                    if (index >= 0) {
                        count++;
                        index++;
                    }
                }

                return count;
            }
        }
}
