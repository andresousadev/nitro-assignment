package nitro.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Defines a Redis service.
 */
public class RedisCacheService implements ICacheService, AutoCloseable {
    private static final int DEFAULT_TTL_SECONDS = 3600;

    private final Jedis jedis;
    private final int ttlSeconds;

    /**
     * Constructs the RedisCacheService.
     * @param redisHost The redis url.
     * @param redisPort The redis port.
     * @param ttlSeconds The TTL value.
     */
    public RedisCacheService(String redisHost, int redisPort, String ttlSeconds) {
        if (redisHost == null || redisHost.trim().isEmpty()) {
            throw new IllegalArgumentException("Redis URL cannot be null or empty");
        }

        if (redisPort <= 0) {
            throw new IllegalArgumentException("Redis port should be a positive number");
        }

        if (ttlSeconds == null || ttlSeconds.isEmpty() || Integer.parseInt(ttlSeconds) < 0) {
            this.ttlSeconds = DEFAULT_TTL_SECONDS;
        } else {
            this.ttlSeconds = Integer.parseInt(ttlSeconds);
        }

        this.jedis = new Jedis(redisHost, redisPort);
    }

    /**
     * Retrieves a value from the cache based on its key.
     * @param key The unique key to identify the cached data.
     * @return The data string found.
     */
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

    /**
     * Stores a key-value pair in the cache with the defined TTL.
     * @param key The unique key for the data.
     * @param value The string data to cache.
     */
    @Override
    public void set(String key, String value) {
        if (key == null || key.trim().isEmpty() || value == null || value.trim().isEmpty()) {
            return;
        }

        try {
            jedis.setex(key, ttlSeconds, value);
        } catch (JedisException e) {
            System.err.println("Error setting key '" + key + "': " + e.getMessage());
        }
    }

    /**
     * Close the redis connection.
     */
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
