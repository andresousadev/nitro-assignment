package nitro.api;

import java.io.IOException;

public interface IWizardWorldApiClient {
    String getAllIngredients() throws Exception;
    String getAllElixirs() throws Exception;
}
