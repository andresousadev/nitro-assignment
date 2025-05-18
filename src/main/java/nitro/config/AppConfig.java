package nitro.config;

public class AppConfig {

    /**
     * Gets environment variable by name.
     * @param envVarName The environment var name.
     * @return The value of the environment variable.
     */
    private String getEnvVariable(String envVarName) {
        String value = System.getenv(envVarName);

        if(value == null) {
            return "";
        }

        return value.trim();
    }

    public String getApiBaseUrl() {
        return getEnvVariable("API_BASE_URL");
    }

    public String getRedisUrl() {
        return getEnvVariable("REDIS_URL");
    }

    public String getCacheTtlSeconds() {
        return getEnvVariable("CACHE_TTL_SECONDS");
    }
}
