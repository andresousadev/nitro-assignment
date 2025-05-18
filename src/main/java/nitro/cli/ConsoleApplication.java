package nitro.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.ClientBuilder;
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
import nitro.service.ElixirService;
import nitro.service.IElixirService;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class ConsoleApplication {
    private final Scanner scanner;
    private final IElixirService elixirService;
    private final Set<String> ingredientNames;
    private final Set<String> userIngredients;

    public ConsoleApplication() {
        this.scanner = new Scanner(System.in);
        this.ingredientNames = new HashSet<>();
        this.userIngredients = new HashSet<>();

        try {
            AppConfig appConfig = new AppConfig();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonMapper jsonMapper = new JsonMapper(objectMapper);

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
            ResteasyClient client = (ResteasyClient) ClientBuilder.newClient();
            ResteasyWebTarget target = client.target(appConfig.getApiBaseUrl());
            IWizardWorldApiClient apiClient = target.proxy(IWizardWorldApiClient.class);

            this.elixirService = new ElixirService(apiClient, cacheService, objectMapper, jsonMapper);

            preFetchAllIngredientNames();

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
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (Exception e) {
                System.err.println("An unexpected error occurred during menu action: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void findCreatableElixirs() {

    }

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

    private void printMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. List All Ingredients");
        System.out.println("2. List All Elixirs");
        System.out.println("3. Find Creatable Elixirs");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

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

    private void enterAvailableIngredients() {
        System.out.println("\n--- Enter Available Ingredients ---");
        System.out.println("Enter the ingredients you have, separated by commas.");
        System.out.println("Valid ingredients based on API list: " + (ingredientNames.isEmpty() ? "Not loaded" : ingredientNames.size() + " ingredients"));
        System.out.print("Your ingredients: ");

        String inputLine = scanner.nextLine();
        userIngredients.clear();

        if (inputLine != null && !inputLine.trim().isEmpty()) {
            String[] ingredients = inputLine.split(",");
            int validCount = 0;
            for (String ingredient : ingredients) {
                String trimmedLower = ingredient.trim().toLowerCase();
                if (!trimmedLower.isEmpty()) {
                    if (ingredientNames.isEmpty() || ingredientNames.contains(trimmedLower)) {
                        userIngredients.add(trimmedLower);
                        validCount++;
                    } else {
                        System.out.println("Warning: '" + ingredient.trim() + "' is not a recognized ingredient and will be ignored.");
                    }
                }
            }
            System.out.println("You entered " + validCount + " recognized ingredient(s).");
        } else {
            System.out.println("No ingredients entered.");
        }

        System.out.println("Current available ingredients: " + (userIngredients.isEmpty() ? "None" : String.join(", ", userIngredients)));
    }

    private void preFetchAllIngredientNames() {
        List<Ingredient> allIngredients = this.elixirService.getIngredients();

        if (allIngredients != null) {
            allIngredients.stream()
                    .map(Ingredient::getName)
                    .filter(name -> name != null && !name.trim().isEmpty())
                    .map(String::toLowerCase)
                    .forEach(this.ingredientNames::add);
        } else {
            System.err.println("No ingredients found.");
            throw new RuntimeException("There was am issue fetching the ingredients");
        }
    }
}
