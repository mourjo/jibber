package me.mourjo;

import me.mourjo.exceptions.TooManyUsersException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.Tuple;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

public class SessionInfo {
    private static final DateFormat dft = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
    private static final double timeoutMillis = 10 * 60 * 1000D;
    private static final int MAX_USERS = 100;

    public static Collection<String> getInfo(String user) {
        String keyStorePassword = System.getenv("KEYSTORE_PASS");
        String keystoreLocation = System.getenv("KEYSTORE_LOCATION");
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
        System.setProperty("javax.net.ssl.keyStore", keystoreLocation);
        System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);

        String trustStorePassword = System.getenv("TRUSTSTORE_PASS");
        String trustStoreLocation = System.getenv("TRUSTSTORE_LOCATION");
        System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
        System.setProperty("javax.net.ssl.trustStore", trustStoreLocation);
        System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);

        String host = System.getenv("REDIS_HOST");
        int port = Integer.parseInt(System.getenv("REDIS_PORT"));
        String redisPassword = System.getenv("REDIS_PASSWORD");

        try (Jedis jedis = new Jedis(host, port, 10_000, 10_000, true)) {
            jedis.auth(redisPassword);

            var total_users = jedis.zcount("recent_users", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (total_users > MAX_USERS) {
                throw new TooManyUsersException(total_users);
            }

            jedis.zremrangeByScore("recent_users", Double.NEGATIVE_INFINITY, System.currentTimeMillis() - timeoutMillis);
            jedis.zadd("recent_users", (double) System.currentTimeMillis(), user);

            var activeUsers = jedis.zrangeWithScores("recent_users", 0, System.currentTimeMillis());
            Collections.reverse(activeUsers);

            return activeUsers.stream().map(tup -> tup.getElement() + " active until " + format(tup)).collect(Collectors.toList());
        }
    }

    private static String format(Tuple t) {
        return dft.format(new Date((long) (t.getScore() + timeoutMillis)));
    }
}
