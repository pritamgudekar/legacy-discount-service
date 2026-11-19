package com.example.legacy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.HashMap;

/**
 * Minimal legacy-style REST-ish service, consistent with the same
 * zero-dependency HttpServer pattern already proven in the Lambda
 * microVM tests (see
 * customer-agent/docs/build-journey/03-local-microvm-deploy-verification.md).
 * Exposes:
 *   GET /health            -> readiness check
 *   GET /discount?amount=100&type=PREMIUM -> computed discount
 */
public class Main {

    private static final DiscountCalculator CALCULATOR = new DiscountCalculator();

    public static void main(String[] args) throws IOException {
        int port = 8081;
        String portOverride = System.getenv("PORT");
        if (portOverride != null) {
            port = Integer.parseInt(portOverride);
        }
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/health", (HttpExchange exchange) -> {
            String response = "{\"status\":\"UP\",\"app\":\"legacy-discount-service\"}";
            writeJson(exchange, 200, response);
        });

        server.createContext("/discount", (HttpExchange exchange) -> {
            try {
                Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
                double amount = Double.parseDouble(params.get("amount"));
                String type = params.get("type");
                double result = CALCULATOR.applyDiscount(amount, type);
                String response = "{\"amount\":" + amount + ",\"customerType\":\"" + type
                        + "\",\"discounted\":" + result + "}";
                writeJson(exchange, 200, response);
            } catch (Exception e) {
                String response = "{\"error\":\"" + e.getMessage() + "\"}";
                writeJson(exchange, 400, response);
            }
        });

        server.start();
        System.out.println("legacy-discount-service started on port " + port);
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> result = new HashMap<String, String>();
        if (query == null) {
            return result;
        }
        String[] pairs = query.split("&");
        for (int i = 0; i < pairs.length; i++) {
            String[] kv = pairs[i].split("=");
            if (kv.length == 2) {
                result.put(kv[0], kv[1]);
            }
        }
        return result;
    }

    private static void writeJson(HttpExchange exchange, int statusCode, String body) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, body.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(body.getBytes());
        os.close();
    }
}
