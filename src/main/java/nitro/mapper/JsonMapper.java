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
     * Maps Json to an object
     * @param json
     * @param elementType
     * @return
     * @param <T>
     * @throws RuntimeException
     */
    @Override
    public <T> T mapJsonObject(String json, Class<T> elementType) throws RuntimeException {
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
