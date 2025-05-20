package nitro.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Defines the Elixir model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Elixir {
    /**
     * The unique identifier for the elixir from the external API.
     * Used for defining equality and hash code.
     */
    private String id;

    /**
     * The name of the elixir.
     */
    private String name;

    /**
     * The magical effect of the elixir.
     */
    private String effect;

    /**
     * Any known side effects of the elixir.
     */
    private String sideEffects;

    /**
     * Descriptive characteristics of the elixir (e.g., color, consistency).
     */
    private String characteristics;

    /**
     * The estimated time required to brew the elixir.
     */
    private String time;

    /**
     * The difficulty level of brewing the elixir.
     */
    private String difficulty;

    /**
     * A list of {@link Ingredient} objects required to craft this elixir.
     */
    private List<Ingredient> ingredients;

    /**
     * The name of the manufacturer.
     */
    private String manufacturer;

    /**
     * Default no-argument constructor required by Jackson for deserialization.
     */
    public Elixir() {
    }

    /**
     * Constructor for creating Elixir instances.
     * @param id The unique identifier for the elixir.
     * @param name The name of the elixir.
     * @param difficulty The difficulty level of brewing.
     * @param ingredients A list of required ingredients.
     */
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

    /**
     * Gets the unique identifier of the elixir.
     * @return The elixir ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the elixir.
     * @param id The elixir ID to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of the elixir.
     * @return The elixir name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the elixir.
     * @param name The elixir name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the magical effect of the elixir.
     * @return The elixir effect.
     */
    public String getEffect() {
        return effect;
    }

    /**
     * Sets the magical effect of the elixir.
     * @param effect The elixir effect to set.
     */
    public void setEffect(String effect) {
        this.effect = effect;
    }

    /**
     * Gets the side effects of the elixir.
     * @return The elixir side effects.
     */
    public String getSideEffects() {
        return sideEffects;
    }

    /**
     * Sets the side effects of the elixir.
     * @param sideEffects The elixir side effects to set.
     */
    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    /**
     * Gets the characteristics of the elixir.
     * @return The elixir characteristics.
     */
    public String getCharacteristics() {
        return characteristics;
    }

    /**
     * Sets the characteristics of the elixir.
     * @param characteristics The elixir characteristics to set.
     */
    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    /**
     * Gets the brewing time for the elixir.
     * @return The brewing time.
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the brewing time for the elixir.
     * @param time The brewing time to set.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Gets the difficulty level of the elixir.
     * @return The difficulty level.
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty level of the elixir.
     * @param difficulty The difficulty level to set.
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Gets the list of ingredients required for the elixir.
     * @return A list of {@link Ingredient} objects.
     */
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Sets the list of ingredients required for the elixir.
     * @param ingredients The list of ingredients to set.
     */
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Gets the name of the manufacturer.
     * @return The name of the manufacturer.
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Sets the name of the manufacturer.
     * @param manufacturer The name of the manufacturer to set.
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * Returns a string representation of the Elixir object.
     * @return A string representation of the Elixir.
     */
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
                + "Manufacturer: " + (manufacturer != null ? manufacturer : "-") + "\n"
                + "-----------------------------------------\n";
    }

    /**
     * Compares this Elixir object to another object for equality.
     * Equality is based solely on the unique {@link #id} field.
     * @param o The object to compare with.
     * @return true if the objects are equal (have the same ID), false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elixir elixir = (Elixir) o;

        return Objects.equals(id, elixir.id);
    }

    /**
     * Generates a hash code for this Elixir object.
     * The hash code is based solely on the unique {@link #id} field,
     * ensuring consistency with the {@link #equals(Object)} method.
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
