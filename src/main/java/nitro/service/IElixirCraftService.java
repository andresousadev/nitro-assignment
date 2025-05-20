package nitro.service;

import java.util.Set;
import nitro.data.Elixir;

public interface IElixirCraftService {

    /**
     * Gets all the craftable Elixirs by a set of Ingredients.
     * @param ingredients The set of Ingredients.
     * @return The Set of Elixirs.
     */
    Set<Elixir> findCraftableIngredients(Set<String> ingredients);
}
