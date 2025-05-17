package nitro.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class WizardWorldApiClient implements IWizardWorldApiClient {

    private final HttpClient httpClient;
    private final String baseUrl;

    public WizardWorldApiClient(String baseUrl) {
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("API base url cannot be null or empty");
        }

        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public String getAllElixirs() throws Exception {
        String url = baseUrl + "/elixirs";
        return sendGetRequest(url);
    }

    @Override
    public String getAllIngredients() throws Exception {
        String url = baseUrl + "/ingredients";
        return sendGetRequest(url);
    }

    private String sendGetRequest(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build();

        HttpResponse<String> response;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new IOException("Network or IO error during API request to " + url + ": " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InterruptedException("API request interrupted: " + e.getMessage());
        }

        if (response.statusCode() != 200) {
            System.err.println("API request returned status code " + response.statusCode() + ": " + response.body());
            throw new Exception("API returned status code: " + response.statusCode() + " for " + url);
        }

        return response.body();
    }
}
