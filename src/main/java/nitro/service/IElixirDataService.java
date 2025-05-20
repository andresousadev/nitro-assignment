package nitro.service;

import java.util.List;
import nitro.data.Elixir;
import nitro.data.Ingredient;

public interface IElixirDataService {
    List<Elixir> getElixirs();

    List<Elixir> getElixirsByIngredientName(String ingredientName);

    List<Ingredient> getIngredients();

    Ingredient getIngredientByName(String name);

    boolean validateIngredientName(String ingredient);
}
