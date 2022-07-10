package collectors.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public final class InventoryUtility {
    public static boolean freeSpace(Inventory inventory) {
        ItemStack[] var1 = inventory.getContents();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ItemStack itemStack = var1[var3];
            if (itemStack == null) {
                return true;
            }
        }

        return false;
    }

    public static boolean canFitItemStack(Inventory inventory, ItemStack itemStack) {
        if (freeSpace(inventory)) {
            return true;
        } else {
            ItemStack[] var2 = inventory.getContents();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                ItemStack inventoryItem = var2[var4];
                if (inventoryItem.isSimilar(itemStack) && inventoryItem.getAmount() < inventoryItem.getMaxStackSize()) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean inventoryContainsItemStack(Inventory inventory, ItemStack itemStack) {
        ItemStack[] var2 = inventory.getContents();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            ItemStack content = var2[var4];
            if (content.isSimilar(itemStack)) {
                return true;
            }
        }

        return false;
    }

    public static String toBase64(ItemStack itemStack) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(0);
            dataOutput.writeObject(itemStack);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception var3) {
            throw new IllegalStateException("Unable to parse itemstack.", var3);
        }
    }

    public static ItemStack toItemStack(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            dataInput.close();
            return (ItemStack)dataInput.readObject();
        } catch (Exception var3) {
            throw new IllegalStateException("Unable to decode class type.");
        }
    }

    public static String toBase64(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);

            for(int i = 0; i < items.length; ++i) {
                dataOutput.writeObject(items[i]);
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception var4) {
            throw new IllegalStateException("Unable to save item stacks.", var4);
        }
    }

    public static ItemStack[] toItemStacks(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            for(int i = 0; i < items.length; ++i) {
                items[i] = (ItemStack)dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (Exception var5) {
            throw new IllegalStateException("Unable to decode class type.", var5);
        }
    }

    private InventoryUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
