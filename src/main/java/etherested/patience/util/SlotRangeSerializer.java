package etherested.patience.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

// Gson adapter for SlotRange — serializes to compact string form ("0-3,5") and back
public class SlotRangeSerializer implements JsonSerializer<SlotRange>, JsonDeserializer<SlotRange> {

    @Override
    public JsonElement serialize(SlotRange src, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public SlotRange deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        return SlotRange.parse(json.getAsString());
    }
}
