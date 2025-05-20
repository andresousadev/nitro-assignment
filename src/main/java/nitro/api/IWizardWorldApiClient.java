package nitro.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import nitro.data.Elixir;
import nitro.data.Ingredient;

/**
 * Defines the IWizardWorldApiClient interface.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public interface IWizardWorldApiClient {

    /**
     * Gets all the existing ingredients.
     * @return The list of Ingredients.
     */
    @GET
    @Path("ingredients")
    List<Ingredient> getAllIngredients();

    /**
     * Searches the Ingredient by name.
     * @param name The Ingredient name.
     * @return A list of the found Ingredients.
     */
    @GET
    @Path("ingredients")
    List<Ingredient> getIngredientByName(@QueryParam("name") String name);

    /**
     * Gets all the craftable elixirs.
     * @return A list of the Elixirs.
     */
    @GET
    @Path("elixirs")
    List<Elixir> getAllElixirs();

    /**
     * Searches all the Elixirs that need a specific Ingredient.
     * @param ingredient The Ingredient.
     * @return A list of elixirs that need that Ingredient.
     */
    @GET
    @Path("elixirs")
    List<Elixir> getElixirsByIngredient(@QueryParam("ingredient") String ingredient);
}
