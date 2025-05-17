package nitro.service;

import java.util.List;
import nitro.data.Elixir;
import nitro.data.Ingredient;

public interface IElixirService {
    List<Elixir> getElixirs() throws Exception;
    List<Ingredient> getIngredients();
}
