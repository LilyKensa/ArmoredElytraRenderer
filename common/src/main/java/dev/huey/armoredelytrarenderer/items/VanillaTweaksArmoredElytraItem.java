package dev.huey.armoredelytrarenderer.items;

import dev.huey.armoredelytrarenderer.ChestplateWithElytraItem;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record VanillaTweaksArmoredElytraItem(ItemStack stack) implements ChestplateWithElytraItem {
//    public ItemStack getElytra() {
//        CompoundTag armElyData = getArmoredElytraData();
//        if (armElyData != null) return ItemStack.of(armElyData.getCompound("elytra"));
//        return null;
//    }
    
    static int modelDataBaseId = 13522550;
    
    static List<Item> chestplateTypes = List.of(
      Items.AIR,
      Items.LEATHER_CHESTPLATE,
      Items.CHAINMAIL_CHESTPLATE,
      Items.GOLDEN_CHESTPLATE,
      Items.IRON_CHESTPLATE,
      Items.DIAMOND_CHESTPLATE,
      Items.NETHERITE_CHESTPLATE
    );
    
    @Nullable Integer getCustomModelData() {
        CustomModelData customModelData = stack.getComponents()
          .get(DataComponents.CUSTOM_MODEL_DATA);
        
        return customModelData == null ? null : customModelData.value();
    }
    
    public boolean isArmoredElytra() {
        Integer i = getCustomModelData();
        return i != null &&
          modelDataBaseId < i && i < modelDataBaseId + chestplateTypes.size();
    }
    
    public ItemStack getChestplate() {
        if (!this.isArmoredElytra()) return ItemStack.EMPTY;
        
//        Integer i = getCustomModelData();
//        if (i == null) return ItemStack.EMPTY;
        
//        ItemStack chestplate = new ItemStack(chestplateTypes.get(i - modelDataBaseId));
        
        DataComponentMap dcm = stack.getComponents();
        
//        DataComponentMap.Builder DCMBuilder = DataComponentMap.builder();
//
//        ItemEnchantments enchantments = dcm.get(DataComponents.ENCHANTMENTS);
//        if (enchantments != null) DCMBuilder.set(DataComponents.ENCHANTMENTS, enchantments);
//
//        DyedItemColor dyedColor = dcm.get(DataComponents.DYED_COLOR);
//        if (dyedColor != null) DCMBuilder.set(DataComponents.DYED_COLOR, dyedColor);

//        chestplate.applyComponents(DCMBuilder.build());

//        return chestplate.copy();
        
        BundleContents contents = dcm.get(DataComponents.BUNDLE_CONTENTS);
        if (contents == null) return ItemStack.EMPTY;
        
        return contents.getItemUnsafe(0).copy();
    }
}
