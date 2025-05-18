package nitro.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public interface IJsonMapper {
    <T> T mapJsonObject(String json, Class<T> elementType) throws JsonProcessingException;
    <T> List<T> mapJsonList(String json, Class<T> elementType) throws IllegalArgumentException;
}
