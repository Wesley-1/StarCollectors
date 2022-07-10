package collectors.data.typeadapter;

import collectors.utility.InventoryUtility;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackTypeAdapter implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {
    public ItemStackTypeAdapter() {
    }

    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            JsonObject obj = json.getAsJsonObject();
            Material material = Material.valueOf(obj.get("material").getAsString());
            int amount = obj.get("amount").getAsInt();
            short durability = obj.get("data").getAsShort();
            ItemStack itemStack = new ItemStack(material, amount, durability);
            ItemMeta meta = itemStack.getItemMeta();
            if (obj.has("title")) {
                meta.setDisplayName(obj.get("title").getAsString());
            }

            if (obj.has("lore")) {
                List<String> lore = new ArrayList();
                Iterator var11 = obj.get("lore").getAsJsonArray().iterator();

                while(var11.hasNext()) {
                    JsonElement element = (JsonElement)var11.next();
                    lore.add(element.getAsString());
                }

                meta.setLore(lore);
            }

            itemStack.setItemMeta(meta);
            return itemStack;
        } catch (Exception var13) {
            Bukkit.getLogger().warning("Error encountered while deserializing a ItemStack.");
            return null;
        }
    }

    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        try {
            obj.addProperty("material", src.getType().name());
            obj.addProperty("amount", src.getAmount());
            obj.addProperty("data", src.getDurability());
            if (src.hasItemMeta()) {
                if (src.getItemMeta().hasDisplayName()) {
                    obj.addProperty("title", src.getItemMeta().getDisplayName());
                }

                if (src.getItemMeta().hasLore()) {
                    JsonArray array = new JsonArray();
                    Iterator var6 = src.getItemMeta().getLore().iterator();

                    while(var6.hasNext()) {
                        String key = (String)var6.next();
                        array.add(key);
                    }

                    obj.add("lore", array);
                }
            }

            obj = (JsonObject)(new Gson()).fromJson(InventoryUtility.toBase64(src), JsonObject.class);
            return obj;
        } catch (Exception var8) {
            Bukkit.getLogger().warning("Error encountered while serializing a ItemStack.");
            return obj;
        }
    }
}
