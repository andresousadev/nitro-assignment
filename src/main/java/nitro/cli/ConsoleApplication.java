package nitro.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import nitro.api.IWizardWorldApiClient;
import nitro.cache.ICacheService;
import nitro.cache.RedisCacheService;
import nitro.config.AppConfig;
import nitro.data.Elixir;
import nitro.data.Ingredient;
import nitro.mapper.JsonMapper;
import nitro.service.ElixirService;
import nitro.service.IElixirService;

public class ConsoleApplication {
    private final AppConfig appConfig;
    private final Scanner scanner;
    private final ObjectMapper objectMapper;
    private final JsonMapper jsonMapper;
    private final ICacheService cacheService;
    private final IWizardWorldApiClient apiClient;
    private IElixirService elixirService;
    private Set<Ingredient> availableIngredients;
    private Set<Elixir> availableElixirs;

    public ConsoleApplication() {
        this.scanner = new Scanner(System.in);
        this.availableElixirs = new HashSet<>();
        this.availableIngredients = new HashSet<>();

        try {
            this.appConfig = new AppConfig();
            this.objectMapper = new ObjectMapper();
            this.jsonMapper = new JsonMapper(this.objectMapper);
            this.cacheService = new RedisCacheService(appConfig.getRedisUrl(), appConfig.getCacheTtlSeconds());
            this.elixirService = new ElixirService(apiClient, cacheService);
        } catch (IllegalStateException e) {
            System.err.println("Application configuration error: " + e.getMessage());
            System.err.println("Please set the required environment variables and run again.");

            throw new RuntimeException("Application failed to start due to configuration error.", e);
        } catch (Exception e) {
            System.err.println("Application startup error: " + e.getMessage());

            throw new RuntimeException("Application failed to start.", e);
        }
    }

    public void run() {
        System.out.println("Welcome to the Wizard World Potion Crafter by Nitro!");

        boolean running = true;

        while (running) {
            //TODO: Also check if all the services are available
            if (appConfig == null) {
                System.err.println("Application configuration error. Exiting..");
                running = false;
                continue;
            }

            //TODO: printMenu();getUserChoice();listAllIngredients();listAllElixirs();createElixir()
        }

        scanner.close();
        //TODO: Close other services
    }
}
