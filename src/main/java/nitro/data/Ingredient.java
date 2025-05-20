package nitro.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;

/**
 * Defines the Ingredient model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingredient {
    /**
     * The unique identifier for the ingredient from the external API.
     */
    private String id;

    /**
     * The name of the ingredient.
     */
    private String name;

    /**
     * Default no-argument constructor required by Jackson for deserialization.
     */
    public Ingredient() {
    }

    /**
     * Constructor for creating Ingredient instances.
     * @param id The unique identifier for the ingredient.
     * @param name The name of the ingredient.
     */
    public Ingredient(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the unique identifier of the ingredient.
     * @return The ingredient ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the ingredient.
     * @param id The ingredient ID to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of the ingredient.
     * @return The ingredient name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the ingredient.
     * @param name The ingredient name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a string representation of the Ingredient object.
     * @return A string representation of the Ingredient.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Compares this Ingredient object to another object for equality.
     * Equality is based solely on the ingredient's {@link #name},
     * performing a case-insensitive comparison.
     * @param o The object to compare with.
     * @return true if the objects are equal (have the same name ignoring case), false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient ingredient = (Ingredient) o;

        return Objects.equals(name.toLowerCase(), ingredient.name.toLowerCase());
    }

    /**
     * Generates a hash code for this Ingredient object.
     * The hash code is based solely on the ingredient's {@link #name},
     * using the lowercase version of the name to ensure consistency with
     * the case-insensitive {@link #equals(Object)} method.
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }
}
