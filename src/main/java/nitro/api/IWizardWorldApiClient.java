package nitro.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import nitro.data.Elixir;
import nitro.data.Ingredient;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public interface IWizardWorldApiClient {

    @GET
    @Path("ingredients")
    List<Ingredient> getAllIngredients();

    @GET
    @Path("ingredients")
    List<Ingredient> getIngredientByName(@QueryParam("name") String name);

    @GET
    @Path("elixirs")
    List<Elixir> getAllElixirs();

    @GET
    @Path("elixirs")
    List<Elixir> getElixirsByIngredient(@QueryParam("ingredient") String ingredient);
}
