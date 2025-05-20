package nitro.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.util.Collections;
import java.util.List;

/**
 * Defines the Json Mapper.
 */
public class JsonMapper implements IJsonMapper {
    private final ObjectMapper objectMapper;

    /**
     * Constructs the JsonMapper.
     * @param objectMapper The object mapper.
     */
    public JsonMapper(ObjectMapper objectMapper) {
        if (objectMapper == null) {
            throw new IllegalArgumentException("ObjectMapper must not be null");
        }

        this.objectMapper = objectMapper;
    }

    /**
     * Maps a JSON string to an object
     * @param json The JSON string to be mapped.
     * @param elementType The type of the element to be converted to.
     * @return The mapped object.
     * @throws RuntimeException
     */
    @Override
    public <T> T mapJsonObject(String json, Class<T> elementType) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }

        if (elementType == null) {
            throw new IllegalArgumentException("ElementType must not be null");
        }

        try {
            return objectMapper.readValue(json, elementType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to map JSON string to " + elementType.getSimpleName(), e);
        }
    }

    /**
     * Maps a JSON string to a list of objects.
     * @param json The json string.
     * @param elementType The element type to be serialized to.
     * @return The mappes list of objects.
     * @throws RuntimeException
     */
    @Override
    public <T> List<T> mapJsonList(String json, Class<T> elementType) throws RuntimeException {
        if (json == null || json.trim().isEmpty()) {
            return Collections.emptyList();
        }

        if (elementType == null) {
            throw new IllegalArgumentException("ElementType must not be null");
        }

        try {
            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, elementType);
            return objectMapper.readValue(json, collectionType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to map JSON string to List<" + elementType.getSimpleName() + ">", e);
        }
    }
}
