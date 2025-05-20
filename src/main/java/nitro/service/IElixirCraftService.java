package nitro.service;

import java.util.Set;
import nitro.data.Elixir;

public interface IElixirCraftService {
    Set<Elixir> findCraftableIngredients(Set<String> ingredients);
}
