package nitro.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

public class RedisCacheService implements ICacheService, AutoCloseable {
    private static final int DEFAULT_TTL_SECONDS = 3600;

    private final Jedis jedis;
    private final int ttlSeconds;

    public RedisCacheService(String redisUrl, String ttlSeconds) {
        if (redisUrl == null || redisUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Redis URL cannot be null or empty");
        }

        if (ttlSeconds == null || ttlSeconds.isEmpty() || Integer.parseInt(ttlSeconds) < 0) {
            this.ttlSeconds = DEFAULT_TTL_SECONDS;
        } else {
            this.ttlSeconds = Integer.parseInt(ttlSeconds);
        }

        this.jedis = new Jedis(redisUrl);
    }

    @Override
    public String get(String key) {
        if (key == null || key.trim().isEmpty()) {
            return null;
        }

        try {
            String data = jedis.get(key.trim());

            if (data != null) {
                System.out.println("DEBUG: Cache hit for key: " + key);
            } else {
                System.out.println("DEBUG: Cache miss for key: " + key);
            }

            return data;
        } catch (JedisException e) {
            System.err.println("Error getting key '" + key.trim() + "': " + e.getMessage());
            return null;
        }
    }

    @Override
    public void set(String key, String value) {
        if (key == null || key.trim().isEmpty() || value == null || value.trim().isEmpty()) {
            return;
        }

        try {
            jedis.setex(key, ttlSeconds, value);
            System.out.println("DEBUG: Cache set for key '" + key + "' with value '" + value + "' in ttl: " + ttlSeconds);
        } catch (JedisException e) {
            System.err.println("Error setting key '" + key + "' with value '" + value + "': " + e.getMessage());
        }
    }

    @Override
    public void close() {
        if (jedis != null && jedis.isConnected()) {
            try {
                jedis.close();
            } catch (JedisException e) {
                System.err.println("Error closing Jedis: " + e.getMessage());
            }
        }
    }
}
