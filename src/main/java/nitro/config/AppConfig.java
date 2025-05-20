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

    /**
     * Gets the WizardWorld api url from the environment variables.
     * @return The api url.
     */
    public String getApiBaseUrl() {
        return getEnvVariable("API_BASE_URL");
    }

    /**
     * Gets the redis host from the environment variables.
     * @return The redis host.
     */
    public String getRedisHost() {
        return getEnvVariable("REDIS_HOST");
    }

    /**
     * Gets the redis port from the environment variables.
     * @return The redis port.
     */
    public int getRedisPort() {
        return Integer.parseInt(getEnvVariable("REDIS_PORT"));
    }

    /**
     * Gets the default TTL value from the environment variables.
     * @return The TTL value.
     */
    public String getCacheTtlSeconds() {
        return getEnvVariable("CACHE_TTL_SECONDS");
    }
}
