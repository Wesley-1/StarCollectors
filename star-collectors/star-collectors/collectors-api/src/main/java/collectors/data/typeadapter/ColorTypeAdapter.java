package collectors.data.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.bukkit.Bukkit;
import org.bukkit.Color;

public class ColorTypeAdapter implements JsonDeserializer<Color>, JsonSerializer<Color> {
    public ColorTypeAdapter() {
    }

    public Color deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            JsonObject obj = json.getAsJsonObject();
            return Color.fromRGB(obj.get("R").getAsInt(), obj.get("G").getAsInt(), obj.get("B").getAsInt());
        } catch (Exception var5) {
            Bukkit.getLogger().warning("Error encountered while deserializing a Color.");
            return null;
        }
    }

    public JsonElement serialize(Color src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        try {
            obj.addProperty("R", src.getRed());
            obj.addProperty("G", src.getGreen());
            obj.addProperty("B", src.getBlue());
            return obj;
        } catch (Exception var6) {
            Bukkit.getLogger().warning("Error encountered while deserializing a Color.");
            return obj;
        }
    }
}

