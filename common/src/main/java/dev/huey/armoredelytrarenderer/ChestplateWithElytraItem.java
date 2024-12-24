package dev.huey.armoredelytrarenderer;

import dev.huey.armoredelytrarenderer.items.VanillaTweaksArmoredElytraItem;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public interface ChestplateWithElytraItem {
    List<Function<ItemStack, @NotNull ChestplateWithElytraItem>> decodeItemStack = List.of(
        VanillaTweaksArmoredElytraItem::new
    );

    static ChestplateWithElytraItem fromItemStack(ItemStack stack) {
        for (Function<ItemStack, @NotNull ChestplateWithElytraItem> f : decodeItemStack) {
            ChestplateWithElytraItem item = f.apply(stack);
            if (item.isArmoredElytra()) return item;
        }
        return null;
    }

    boolean isArmoredElytra();
//    default boolean isArmoredElytra() {
//        ItemStack elytra = getElytra();
//        ItemStack chestplate = getChestplate();
//        return elytra != null && !elytra.isEmpty() && elytra.is(Items.ELYTRA) && chestplate != null && !chestplate.isEmpty() && InternalArrays.isItemChestplate(chestplate.getItem());
//    }
    
    int DEFAULT_LEATHER_COLOR = 10511680;

    default int getLeatherChestplateColor() {
        ItemStack leatherChestplate = getChestplate();
        if (leatherChestplate.getItem() != Items.LEATHER_CHESTPLATE) return -1;
        DataComponentMap components = leatherChestplate.getComponents();
        if (components.isEmpty() || !components.has(DataComponents.DYED_COLOR)) return DEFAULT_LEATHER_COLOR;
        DyedItemColor color = components.get(DataComponents.DYED_COLOR);
        return color == null ? DEFAULT_LEATHER_COLOR : color.rgb();
    }

//    ItemStack getElytra();

    ItemStack getChestplate();
}
