package nitro.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.ClientBuilder;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import nitro.api.IWizardWorldApiClient;
import nitro.cache.ICacheService;
import nitro.cache.RedisCacheService;
import nitro.config.AppConfig;
import nitro.data.Elixir;
import nitro.data.Ingredient;
import nitro.mapper.JsonMapper;
import nitro.service.ElixirCraftService;
import nitro.service.ElixirDataService;
import nitro.service.IElixirCraftService;
import nitro.service.IElixirDataService;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

/**
 * The type Console application.
 */
public class ConsoleApplication {
    private final Scanner scanner;
    private final IElixirDataService elixirService;
    private final IElixirCraftService elixirCraftService;

    /**
     * Configure all dependencies.
     */
    public ConsoleApplication() {
        this.scanner = new Scanner(System.in);

        try {
            AppConfig appConfig = new AppConfig();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonMapper jsonMapper = new JsonMapper(objectMapper);

            // Configure caching service.
            ICacheService cacheService;
            if (appConfig.getRedisUrl() != null || !appConfig.getRedisUrl().isEmpty()) {
                try {
                    cacheService = new RedisCacheService(appConfig.getRedisUrl(), appConfig.getCacheTtlSeconds());
                    System.out.println("Redis cache initialized.");
                } catch (Exception e) {
                    System.err.println("Redis cache initialization failed. Running without cache.");
                    cacheService = null;
                }
            } else {
                cacheService = null;
            }

            // Create a new RESTEasy JAX-RS client.
            ResteasyClient client = (ResteasyClient) ClientBuilder.newClient();

            // Defines the request web target.
            ResteasyWebTarget target = client.target(appConfig.getApiBaseUrl());

            // Defines the proxy service which will be translated into the http requests.
            IWizardWorldApiClient apiClient = target.proxy(IWizardWorldApiClient.class);

            this.elixirService = new ElixirDataService(apiClient, cacheService, objectMapper, jsonMapper);
            this.elixirCraftService = new ElixirCraftService(elixirService);

        } catch (IllegalStateException e) {
            System.err.println("Application configuration error: " + e.getMessage());
            System.err.println("Please set the required environment variables and run again.");

            throw new RuntimeException("Application failed to start due to configuration error.", e);
        } catch (Exception e) {
            System.err.println("Application startup error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Application failed to start.", e);
        }
    }

    /**
     * Starts the console program and prints the menu.
     */
    public void run() {
        System.out.println("Welcome to the Wizard World Potion Crafter!");

        boolean running = true;
        while (running) {
            if (elixirService == null) {
                System.err.println("Core services are not initialized. Cannot run menu options. Exiting.");
                running = false;
            }

            printMenu();
            int choice = getUserChoice();

            try {
                switch (choice) {
                    case 1:
                        listAllIngredients();
                        break;
                    case 2:
                        listAllElixirs();
                        break;
                    case 3:
                        findCreatableElixirs();
                        break;
                    case 4:
                        running = false;
                        System.out.println("Exiting Potion Crafter. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                }
            } catch (Exception e) {
                System.err.println("An unexpected error occurred during menu action: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Prints the console menu.
     */
    private void printMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. List All Ingredients");
        System.out.println("2. List All Elixirs");
        System.out.println("3. Find Craftable Elixirs by Ingredients");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Lists all the existing ingredients.
     */
    private void listAllIngredients() {
        System.out.println("\n--- All Ingredients ---");
        List<Ingredient> ingredients = elixirService.getIngredients();
        if (ingredients == null || ingredients.isEmpty()) {
            System.out.println("No ingredients found.");
        } else {
            System.out.println("Found " + ingredients.size() + " ingredients:");
            ingredients.forEach(System.out::println);
        }
    }

    /**
     * List all the craftable elixirs.
     */
    private void listAllElixirs() {
        System.out.println("\n--- All Elixirs ---");
        List<Elixir> elixirs = elixirService.getElixirs();
        if (elixirs == null || elixirs.isEmpty()) {
            System.out.println("No elixirs found.");
        } else {
            System.out.println("Found " + elixirs.size() + " elixirs:");
            elixirs.forEach(System.out::println);
        }
    }

    /**
     * Lists all the craftable elixirs by a set of available ingredients.
     */
    private void findCreatableElixirs() {
        Set<String> ingredientsAvailable = enterAvailableIngredients();

        if (ingredientsAvailable.isEmpty()) {
            return;
        }

        Set<Elixir> craftableElixirs = elixirCraftService.findCraftableIngredients(ingredientsAvailable);

        if (craftableElixirs.isEmpty()) {
            System.out.println("\n--- No elixirs found. ---");
            return;
        }

        System.out.println("\n--- Creatable Elixirs ---");
        craftableElixirs.forEach(System.out::println);
    }

    /**
     * Reads the user entered choice.
     * @return The chosen option number.
     */
    private int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
            System.out.print("Enter your choice: ");
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    /**
     * Reads the user available ingredients.
     * @return A set of the user available ingredients.
     */
    private Set<String> enterAvailableIngredients() {
        System.out.println("\n--- Enter Available Ingredients ---");
        System.out.println("Enter the ingredients you have, separated by commas.");
        System.out.print("Your ingredients: ");

        Set<String> userIngredients = new HashSet<>();

        String inputLine = scanner.nextLine();

        if (inputLine != null && !inputLine.trim().isEmpty()) {
            String[] ingredients = inputLine.split(",");
            for (String ingredient : ingredients) {
                String trimmedIngredient = ingredient.trim();
                if (!trimmedIngredient.isEmpty()) {
                    if (userIngredients.isEmpty() || !userIngredients.contains(trimmedIngredient)) {
                        if (!elixirService.validateIngredientName(trimmedIngredient)) {
                            System.err.println("Invalid ingredient name: " + trimmedIngredient);
                            return Collections.emptySet();
                        }
                        userIngredients.add(trimmedIngredient);
                    }
                } else {
                    System.err.println("Empty ingredient name found");
                    return Collections.emptySet();
                }
            }
        } else {
            System.err.println("No ingredients entered.");
            return Collections.emptySet();
        }

        return userIngredients;
    }
}
