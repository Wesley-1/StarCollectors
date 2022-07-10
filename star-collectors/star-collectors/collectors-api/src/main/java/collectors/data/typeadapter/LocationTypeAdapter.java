package collectors.data.typeadapter;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationTypeAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {
    public LocationTypeAdapter() {
    }

    public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();

        try {
            object.add("x", new JsonPrimitive(location.getX()));
            object.add("y", new JsonPrimitive(location.getY()));
            object.add("z", new JsonPrimitive(location.getZ()));
            object.add("world", new JsonPrimitive(location.getWorld().getName()));
            if (location.getYaw() != 0.0F) {
                object.add("yaw", new JsonPrimitive(location.getYaw()));
            }

            if (location.getPitch() != 0.0F) {
                object.add("pitch", new JsonPrimitive(location.getPitch()));
            }

            return object;
        } catch (Exception var6) {
            Bukkit.getLogger().warning("Error encountered while serializing a Location.");
            return object;
        }
    }

    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        JsonObject object = jsonElement.getAsJsonObject();

        try {
            return new Location(Bukkit.getWorld(object.get("world").getAsString()), object.get("x").getAsDouble(), object.get("y").getAsDouble(), object.get("z").getAsDouble(), object.has("yaw") ? object.get("yaw").getAsFloat() : 0.0F, object.has("pitch") ? object.get("pitch").getAsFloat() : 0.0F);
        } catch (Exception var6) {
            Bukkit.getLogger().warning("Error encountered while deserializing a Location.");
            return null;
        }
    }
}
