package nitro.cache;

public interface ICacheService {
    String get(String key);
    void set(String key, String value);
}
