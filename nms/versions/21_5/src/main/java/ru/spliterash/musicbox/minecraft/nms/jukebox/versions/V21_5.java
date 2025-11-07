package ru.spliterash.musicbox.minecraft.nms.jukebox.versions;

import org.bukkit.block.Jukebox;
import org.bukkit.inventory.ItemStack;
import ru.spliterash.musicbox.minecraft.nms.jukebox.IJukebox;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class V21_5 implements IJukebox {
    private final Object tileEntity;
    private final Class<?> craftItemStackClass;
    private final Class<?> nmsItemStackClass;

    public V21_5(Jukebox jukebox) {
        try {
            Class<?> blockEntityClass = Class.forName("net.minecraft.world.level.block.entity.BlockEntity");
            Object foundTileEntity = null;

            Class<?> current = jukebox.getClass();
            while (current != null && foundTileEntity == null) {
                for (Field field : current.getDeclaredFields()) {
                    if (blockEntityClass.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);
                        foundTileEntity = field.get(jukebox);
                        break;
                    }
                }
                current = current.getSuperclass();
            }

            if (foundTileEntity == null) {
                throw new NoSuchFieldException("Unable to locate BlockEntity field on CraftJukebox");
            }

            tileEntity = foundTileEntity;

            craftItemStackClass = Class.forName("org.bukkit.craftbukkit.inventory.CraftItemStack");
            nmsItemStackClass = Class.forName("net.minecraft.world.item.ItemStack");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize V21_5", e);
        }
    }

    public void setJukebox(ItemStack item) {
        try {
            Method asNMSCopy = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);
            Object converted = asNMSCopy.invoke(null, item);

            Method setSongItem = tileEntity.getClass().getMethod("setSongItemWithoutPlaying", nmsItemStackClass, long.class);
            setSongItem.invoke(tileEntity, converted, 0L);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set jukebox item", e);
        }
    }

    public ItemStack getJukebox() {
        try {
            Method getTheItem = tileEntity.getClass().getMethod("getTheItem");
            Object nmsItem = getTheItem.invoke(tileEntity);

            if (nmsItem == null) {
                return null;
            }

            Method isEmpty = nmsItem.getClass().getMethod("isEmpty");
            boolean empty = (boolean) isEmpty.invoke(nmsItem);
            if (empty) {
                return null;
            }

            Method asBukkitCopy = craftItemStackClass.getMethod("asBukkitCopy", nmsItemStackClass);
            return (ItemStack) asBukkitCopy.invoke(null, nmsItem);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get jukebox item", e);
        }
    }
}
