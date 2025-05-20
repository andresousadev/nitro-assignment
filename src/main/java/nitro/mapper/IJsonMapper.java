package nitro.mapper;

import java.util.List;

/**
 * Defines the Json Mapper interface.
 */
public interface IJsonMapper {
    /**
     * Maps a JSON string to an object.
     * @param json The json string.
     * @param elementType The element type to be serialized to.
     * @return The serialized element.
     * @throws IllegalArgumentException if element type is null.
     */
    <T> T mapJsonObject(String json, Class<T> elementType) throws IllegalArgumentException;

    /**
     * Maps a JSON string to a list of objects.
     * @param json The json string.
     * @param elementType The element type to be serialized to.
     * @return The list of serialized elements.
     * @throws IllegalArgumentException if element type is null.
     */
    <T> List<T> mapJsonList(String json, Class<T> elementType) throws IllegalArgumentException;
}
