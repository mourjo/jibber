package org.example;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;


public class Funk implements HttpFunction {
    private static final String htmlTemplate = """
                <html>
                <body>
                <h1> Hello %s! </h1>
                <h2>Current Users:</h2>
                %s
                <body>
                </html>
                """;

    @Override
    public void service(HttpRequest request, HttpResponse response)
            throws IOException {
        BufferedWriter writer = response.getWriter();
        var userParams = request.getQueryParameters().get("user");
        String user = UUID.randomUUID().toString();
        if (userParams == null || userParams.isEmpty()) {
            var clientIP = request.getFirstHeader("X-Forwarded-For");
            if (clientIP.isPresent()) {
                user = clientIP.get();
            } else {
                var userAgent = request.getFirstHeader("User-Agent");
                if (userAgent.isPresent()) {
                    var hash = Integer.toString(userAgent.get().hashCode());
                    user = Base64.getEncoder().encodeToString(hash.getBytes());
                }
            }
        } else {
            user = userParams.get(0);
        }

        try {
            String sessionInfo = SessionInfo.getInfo(user);
            writer.write(htmlTemplate.formatted(user, sessionInfo));
        } catch (Exception e) {
            writer.write("Too many users, try again later");
        }
    }
}