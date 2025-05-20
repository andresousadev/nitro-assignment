package nitro.cache;

/**
 * Defines the Cache Service interface.
 */
public interface ICacheService {
    /**
     * Retrieves a value from the cache based on its key.
     * @param key The unique key to identify the cached data.
     * @return The cached string data associated with the key.
     */
    String get(String key);

    /**
     * Stores a key-value pair in the cache with the defined TTL.
     * @param key The unique key for the data.
     * @param value The string data to cache.
     */
    void set(String key, String value);
}
