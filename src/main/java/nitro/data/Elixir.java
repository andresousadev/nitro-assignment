package nitro.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Elixir {
    private String id;
    private String name;
    private String effect;
    private String sideEffects;
    private String characteristics;
    private String time;
    private String difficulty;
    private List<Ingredient> ingredients;
    private String manufacturer;

    public Elixir() {
    }

    public Elixir(String id,
                  String name,
                  String effect,
                  String sideEffects,
                  String characteristics,
                  String time,
                  String difficulty,
                  List<Ingredient> ingredients,
                  String manufacturer
    ) {
        this.id = id;
        this.name = name;
        this.effect = effect;
        this.sideEffects = sideEffects;
        this.characteristics = characteristics;
        this.time = time;
        this.difficulty = difficulty;
        this.ingredients = ingredients;
        this.manufacturer = manufacturer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        List<Ingredient> ingredients = getIngredients();

        String ingredientsString = (ingredients == null || ingredients.isEmpty()) ?
                "None" :
                ingredients.stream()
                        .map(Ingredient::toString)
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining(","));

        return "Name: " + (name != null ? name : "-") + "\n"
                + "Effect: " + (effect != null ? effect : "-") + "\n"
                + "SideEffects: " + (sideEffects != null ? sideEffects : "-") + "\n"
                + "Characteristics: " + (characteristics != null ? characteristics : "-") + "\n"
                + "Time: " + (time != null ? time : "-") + "\n"
                + "Difficulty: " + (difficulty != null ? difficulty : "-") + "\n"
                + "Ingredients: [" + ingredientsString + "]\n"
                + "Manufacturer: " + (manufacturer != null ? manufacturer : "-");
    }
}
