package nitro.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import nitro.api.IWizardWorldApiClient;
import nitro.cache.ICacheService;
import nitro.data.Elixir;
import nitro.data.Ingredient;
import nitro.mapper.IJsonMapper;

/**
 * Defines the ElixirDataService service.
 */
public class ElixirDataService implements IElixirDataService {
    private static final String CACHE_KEY_ELIXIRS = "elixirs:all";
    private static final String CACHE_KEY_INGREDIENTS = "ingredients:all";
    private static final String CACHE_KEY_INDIVIDUAL_INGREDIENT_PREFIX = "ingredient:name:";
    private static final String CACHE_KEY_ELIXIR_BY_INGREDIENT_PREFIX = "elixir:ingredient:";

    private final IWizardWorldApiClient apiClient;
    private final ICacheService cacheService;
    private final ObjectMapper objectMapper;
    private final IJsonMapper jsonMapper;

    private boolean cacheEnabled = false;

    /**
     * Constructs the ElixirDataService.
     * @param apiClient The WizardWorldApiClient client.
     * @param cacheService The CacheService service.
     * @param objectMapper The objectMapper to serialize objects into JSON.
     * @param jsonMapper The jsonMapper to deserialize JSON into objects.
     */
    public ElixirDataService(
            IWizardWorldApiClient apiClient,
            ICacheService cacheService,
            ObjectMapper objectMapper,
            IJsonMapper jsonMapper
    ) {
        if (apiClient == null) {
            throw new IllegalArgumentException("IWizardWorldApiClient must not be null");
        }

        if (objectMapper == null) {
            throw new IllegalArgumentException("ObjectMapper must not be null");
        }

        if (jsonMapper == null) {
            throw new IllegalArgumentException("ObjectMapper must not be null");
        }

        if (cacheService != null) {
            cacheEnabled = true;
        }

        this.apiClient = apiClient;
        this.cacheService = cacheService;
        this.objectMapper = objectMapper;
        this.jsonMapper = jsonMapper;
    }

    /**
     * Gets all the Elixirs from the cache or from the API as fallback.
     * @return The list of Elixirs.
     */
    @Override
    public List<Elixir> getElixirs() {
        String cachedData = null;

        if (cacheEnabled) {
            cachedData = cacheService.get(CACHE_KEY_ELIXIRS);
        }

        List<Elixir> elixirs;

        if (cachedData != null) {
            try {
                elixirs = jsonMapper.mapJsonList(cachedData, Elixir.class);
                System.out.println("DEBUG: Elixirs loaded from cache");
            } catch (IllegalArgumentException e) {
                System.err.println("Error parsing elixirs from cache, fetching from API: " + e.getMessage());
                elixirs = fetchElixirsFromApi();
            }
        } else {
            elixirs = fetchElixirsFromApi();
        }

        return elixirs;
    }

    /**
     * Gets the Elixirs from the API.
     * @return The list of Elixirs.
     */
    private List<Elixir> fetchElixirsFromApi() {
        try {
            List<Elixir> elixirs = apiClient.getAllElixirs();

            if (cacheEnabled && elixirs != null) {
                try {
                    String jsonToCache = objectMapper.writeValueAsString(elixirs);
                    cacheService.set(CACHE_KEY_ELIXIRS, jsonToCache);
                } catch (JsonProcessingException e) {
                    System.err.println("Warning: Failed to set elixirs cache: " + e.getMessage());
                }
            }

            return elixirs;

        } catch (Exception e) {
            System.err.println("Error fetching elixirs from API: " + e.getMessage());
            throw new RuntimeException("Failed to fetch elixirs from API: " + e.getMessage());
        }
    }

    /**
     * Gets the Elixirs that require the specific Ingredient from cache or from the API
     * as fallback.
     * @param ingredientName The Ingredient name.
     * @return The list of Elixirs that require that Ingredient.
     */
    @Override
    public List<Elixir> getElixirsByIngredientName(String ingredientName) {
        if (ingredientName == null || ingredientName.trim().isEmpty()) {
            throw new IllegalArgumentException("IngredientName must not be null or empty");
        }

        String cacheKey = CACHE_KEY_ELIXIR_BY_INGREDIENT_PREFIX + ingredientName;
        String cachedData = null;

        if (cacheEnabled) {
            cachedData = cacheService.get(cacheKey);
        }

        List<Elixir> elixirs;

        if (cachedData != null) {
            try {
                elixirs = jsonMapper.mapJsonList(cachedData, Elixir.class);
                System.out.println("DEBUG: Elixirs with the ingredient '" + ingredientName + "' loaded from cache");
            } catch (IllegalArgumentException e) {
                System.err.println("Error parsing elixirs with the ingredient '" + ingredientName + "' from cache, fetching from API: " + e.getMessage());
                elixirs = fetchElixirsByIngredientNameFromApi(ingredientName);
            }
        } else {
            elixirs = fetchElixirsByIngredientNameFromApi(ingredientName);
        }

        return elixirs;
    }

    /**
     * Gets the Elixirs that require the specific Ingredient  from the API
     * @param ingredientName The Ingredient name.
     * @return The list of Elixirs that require that Ingredient.
     */
    private List<Elixir> fetchElixirsByIngredientNameFromApi(String ingredientName) {
        try {
            List<Elixir> elixirs = apiClient.getElixirsByIngredient(ingredientName);

            if (cacheEnabled && elixirs != null) {
                try {
                    String jsonToCache = objectMapper.writeValueAsString(elixirs);
                    cacheService.set(CACHE_KEY_ELIXIR_BY_INGREDIENT_PREFIX + ingredientName, jsonToCache);
                } catch (JsonProcessingException e) {
                    System.err.println("Warning: Failed to set elixirs with the ingredient '" + ingredientName + "' cache: " + e.getMessage());
                }
            }

            return elixirs;
        } catch (Exception e) {
            System.err.println("Error fetching elixirs with the ingredient '" + ingredientName + "' from API: " + e.getMessage());
            throw new RuntimeException("Failed to fetch elixirs with the ingredient '" + ingredientName + "' from cache: " + e.getMessage());
        }
    }

    /**
     * Gets all the Ingredients from cache or from API as fallback.
     * @return The list of the existing Ingredients.
     */
    @Override
    public List<Ingredient> getIngredients() {
        String cachedData = null;

        if (cacheEnabled) {
            cachedData = cacheService.get(CACHE_KEY_INGREDIENTS);
        }

        List<Ingredient> ingredients;

        if (cachedData != null) {
            try {
                ingredients = jsonMapper.mapJsonList(cachedData, Ingredient.class);
                System.out.println("DEBUG: Ingredients loaded from cache");
            } catch (IllegalArgumentException e) {
                System.err.println("Error parsing ingredients from cache, fetching from API: " + e.getMessage());
                ingredients = fetchIngredientsFromApi();
            }
        } else {
            ingredients = fetchIngredientsFromApi();
        }

        return ingredients;
    }

    /**
     * Gets all the Ingredients from the API.
     * @return The list of the existing Ingredients.
     */
    private List<Ingredient> fetchIngredientsFromApi() {
        try {
            List<Ingredient> ingredients = apiClient.getAllIngredients();

            if (cacheEnabled && ingredients != null) {
                try {
                    String jsonToCache = objectMapper.writeValueAsString(ingredients);
                    cacheService.set(CACHE_KEY_INGREDIENTS, jsonToCache);
                } catch (JsonProcessingException e) {
                    System.err.println("Warning: Failed to set ingredients cache: " + e.getMessage());
                }
            }

            return ingredients;

        } catch (Exception e) {
            System.err.println("Error fetching ingredients from API: " + e.getMessage());
            throw new RuntimeException("Failed to fetch ingredients from API: " + e.getMessage());
        }
    }

    /**
     * Gets an Ingredient by name.
     * @param name The name of the Ingredient.
     * @return The Ingredient found.
     */
    @Override
    public Ingredient getIngredientByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingredient name must not be null or empty");
        }

        String cacheKey = CACHE_KEY_INDIVIDUAL_INGREDIENT_PREFIX + name;
        String cachedData = null;

        if (cacheEnabled) {
            cachedData = cacheService.get(cacheKey);
        }

        Ingredient ingredient;

        if (cachedData != null) {
            try {
                ingredient = jsonMapper.mapJsonObject(cachedData, Ingredient.class);
                System.out.println("DEBUG: Ingredient '" + name + "' loaded from cache");
            } catch (IllegalArgumentException e) {
                System.err.println("Error parsing ingredient '" + name + "' from cache, fetching from API: " + e.getMessage());
                ingredient = fetchIngredientFromApi(name);
            }
        } else {
            ingredient = fetchIngredientFromApi(name);
        }

        return ingredient;
    }

    /**
     * Gets an Ingredient by name from the API.
     * @param name The Ingredient name.
     * @return The Ingredient found.
     */
    private Ingredient fetchIngredientFromApi(String name) {
        try {
            List<Ingredient> searchResult = apiClient.getIngredientByName(name);

            if (searchResult.isEmpty()) {
                return null;
            }

            Ingredient ingredient = searchResult.getFirst();

            if (cacheEnabled && ingredient != null) {
                try {
                    String jsonToCache = objectMapper.writeValueAsString(ingredient);
                    cacheService.set(CACHE_KEY_INDIVIDUAL_INGREDIENT_PREFIX + name, jsonToCache);
                } catch (JsonProcessingException e) {
                    System.err.println("Warning: Failed to set ingredient '" + name + "' cache: " + e.getMessage());
                }
            }

            return ingredient;
        } catch (Exception e) {
            System.err.println("Error fetching ingredient '" + name + "' from cache: " + e.getMessage());
            throw new RuntimeException("Failed to fetch ingredient '" + name + "' from cache: " + e.getMessage());
        }
    }

    /**
     * Validates the Ingredient by name.
     * @param ingredient The Ingredient name.
     * @return True if the Ingredient exists.
     */
    @Override
    public boolean validateIngredientName(String ingredient) {
        if (ingredient.trim().isEmpty()) {
            return false;
        }

        Ingredient ingredientFound = getIngredientByName(ingredient);

        return ingredientFound != null;
    }
}
