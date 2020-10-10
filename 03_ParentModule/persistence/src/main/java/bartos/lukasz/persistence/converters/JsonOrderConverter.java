package bartos.lukasz.persistence.converters;

import bartos.lukasz.persistence.model.Order;

import java.util.Set;

public class JsonOrderConverter extends JsonConverter<Set<Order>> {
    public JsonOrderConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
