package nitro.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nitro.data.Elixir;
import nitro.data.Ingredient;

public class ElixirCraftService implements IElixirCraftService {
    private final IElixirDataService elixirDataService;

    public ElixirCraftService(IElixirDataService service) {

        if (service == null) {
            throw new IllegalArgumentException("IElixirCraftService must not be null");
        }

        this.elixirDataService = service;
    }

    @Override
    public Set<Elixir> findCraftableIngredients(Set<String> ingredients) {
        Set<Elixir> potentialElixirs = new HashSet<>();
        Set<Elixir> craftableElixirs = new HashSet<>();

        for (String ingredient : ingredients) {
            potentialElixirs.addAll(elixirDataService.getElixirsByIngredientName(ingredient));
        }

        for (Elixir elixir : potentialElixirs) {
            if (elixir.getIngredients().size() > ingredients.size()) continue;

            if (userHasAllIngredientsForElixir(elixir.getIngredients(), ingredients)) craftableElixirs.add(elixir);
        }

        return craftableElixirs;
    }

    private boolean userHasAllIngredientsForElixir(List<Ingredient> elixirIngredients, Set<String> ingredientNames) {
        if (elixirIngredients.isEmpty()) return false;

        for (Ingredient ingredient : elixirIngredients) {
            if (!ingredientNames.contains(ingredient.getName())) return false;
        }

        return true;
    }
}
