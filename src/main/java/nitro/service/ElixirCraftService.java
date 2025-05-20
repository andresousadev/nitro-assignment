package nitro.service;

import java.util.HashSet;
import java.util.Set;
import nitro.data.Elixir;

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
            for (String ingredient : ingredients) {
                if (!elixirIngredientsContainIngredientName(elixir, ingredient)) {
                    break;
                }
            }

            craftableElixirs.add(elixir);
        }

        return craftableElixirs;
    }

    private boolean elixirIngredientsContainIngredientName(Elixir elixir, String ingredientName) {
        //TODO: Fix this method. User should have all the ingredients the Elixir requires.
        return elixir.getIngredients().stream().anyMatch(ingredient -> ingredient.getName().equals(ingredientName));
    }
}
