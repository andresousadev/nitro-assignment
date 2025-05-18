package nitro.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.util.List;
import nitro.api.IWizardWorldApiClient;
import nitro.cache.ICacheService;
import nitro.data.Elixir;
import nitro.data.Ingredient;
import nitro.mapper.IJsonMapper;

public class ElixirService implements IElixirService {
    private static final String CACHE_KEY_ELIXIRS = "elixirs:all";
    private static final String CACHE_KEY_INGREDIENTS = "ingredients:all";

    private final IWizardWorldApiClient apiClient;
    private final ICacheService cacheService;
    private final ObjectMapper objectMapper;
    private final IJsonMapper jsonMapper;

    private final CollectionType elixirListType;
    private final CollectionType ingredientListType;

    private boolean cacheEnabled = false;

    public ElixirService(IWizardWorldApiClient apiClient,
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

        this.elixirListType = objectMapper.getTypeFactory().constructCollectionType(List.class, Elixir.class);
        this.ingredientListType = objectMapper.getTypeFactory().constructCollectionType(List.class, Ingredient.class);
    }

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
}
