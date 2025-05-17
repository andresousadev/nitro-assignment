package nitro.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.util.List;
import nitro.api.IWizardWorldApiClient;
import nitro.cache.ICacheService;
import nitro.data.Elixir;
import nitro.data.Ingredient;

public class ElixirService implements IElixirService {
    private static final String CACHE_KEY_ELIXIRS = "elixirs:all";
    private static final String CACHE_KEY_INGREDIENTS = "ingredients:all";

    private final IWizardWorldApiClient apiClient;
    private final ICacheService cacheService;
    private final ObjectMapper objectMapper;

    private final CollectionType elixirListType;
    private final CollectionType ingredientListType;

    public ElixirService(IWizardWorldApiClient apiClient, ICacheService cacheService
    ) {
        if (apiClient == null) {
            throw new IllegalArgumentException("IWizardWorldApiClient must not be null");
        }

        this.apiClient = apiClient;
        this.cacheService = cacheService;
        this.objectMapper = new ObjectMapper();

        this.elixirListType = objectMapper.getTypeFactory().constructCollectionType(List.class, Elixir.class);
        this.ingredientListType = objectMapper.getTypeFactory().constructCollectionType(List.class, Ingredient.class);
    }

    @Override
    public List<Elixir> getElixirs() throws Exception {
        String cachedData = null;

        if (cacheService != null) {
            cachedData = cacheService.get(CACHE_KEY_ELIXIRS);
        }

        String jsonData;

        if (cachedData != null) {
            jsonData = cachedData;
        } else {
            try {
                jsonData = apiClient.getAllElixirs();

                if (cacheService != null && jsonData != null) {
                    cacheService.set(CACHE_KEY_ELIXIRS, jsonData);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve elixirs", e);
            }
        }

        try {
            return objectMapper.readValue(jsonData, elixirListType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse elixir JSON data");
        }
    }

    @Override
    public List<Ingredient> getIngredients() {
        String cachedData = null;

        if (cacheService != null) {
            cachedData = cacheService.get(CACHE_KEY_INGREDIENTS);
        }

        String jsonData;

        if (cachedData != null) {
            jsonData = cachedData;
        } else {
            try {
                jsonData = apiClient.getAllIngredients();

                if (cacheService != null && jsonData != null) {
                    cacheService.set(CACHE_KEY_ELIXIRS, jsonData);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve elixirs", e);
            }
        }

        try {
            return objectMapper.readValue(jsonData, ingredientListType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse elixir JSON data");
        }
    }
}
