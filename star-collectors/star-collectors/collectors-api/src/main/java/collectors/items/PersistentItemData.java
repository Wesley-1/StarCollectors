package collectors.items;

import com.google.gson.Gson;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PersistentItemData {
    public void write(@Nonnull ItemStack itemStack, NamespacedKey namespacedKey) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(namespacedKey, PersistentDataType.STRING, new Gson().toJson(this));
        itemStack.setItemMeta(itemMeta);
    }

    @Nullable
    public static <T extends PersistentItemData> T read(ItemStack itemStack, NamespacedKey namespacedKey, Class<T> clazz) {
        if (itemStack == null) {
            return null;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return null;
        }
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        String data = persistentDataContainer.get(namespacedKey, PersistentDataType.STRING);
        if (data == null) {
            return null;
        }
        return new Gson().fromJson(data, clazz);
    }
}
