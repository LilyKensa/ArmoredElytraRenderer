package dev.huey.armoredelytrarenderer;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import dev.huey.armoredelytrarenderer.models.ArmoredElytraModelProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ArmoredElytraRenderer {
    public static final String MOD_ID = "armored_elytra_renderer";
    
    public static final Map<UUID, ChestplateWithElytraItem> armoredElytraMappings = new HashMap<>();
    
    public static void init() {
        // Listen for the end of every tick
        ClientTickEvent.CLIENT_POST.register(ArmoredElytraRenderer::tick);
        
        // Create a new model provider
        ArmoredElytraModelProvider armElyModelProvider = new ArmoredElytraModelProvider();
        
        // Set up the model provider and color provider for the elytra item
        ItemPropertiesRegistry.register(Items.ELYTRA, ResourceLocation.withDefaultNamespace("armored_elytra_type"), armElyModelProvider);
        ColorHandlerRegistry.registerItemColors((itemStack, i) -> {
            if (itemStack == null) return -1;
            ChestplateWithElytraItem item = ChestplateWithElytraItem.fromItemStack(itemStack);
            if (item == null) return -1;
            return i > 0 ? -1 : item.getLeatherChestplateColor();
        }, Items.ELYTRA);
        
        // Set up the model provider for all chestplate items
        for (Item chestplateType : InternalArrays.CHESTPLATES)
            ItemPropertiesRegistry.register(chestplateType, ResourceLocation.withDefaultNamespace("armored_elytra_type"), armElyModelProvider);
        
        // Clear armored elytra mappings when quitting a level
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(player -> armoredElytraMappings.clear());
        
        ClientTooltipEvent.ITEM.register((stack, lines, ctx, flag) -> {
            ChestplateWithElytraItem item = ChestplateWithElytraItem.fromItemStack(stack);
            if (item == null) return;
            if (!item.isArmoredElytra()) return;
            ItemStack chestplateItemStack = item.getChestplate();
            if (chestplateItemStack == null) return;
            Minecraft mc = Minecraft.getInstance();
            List<Component> tooltipLines = chestplateItemStack.getTooltipLines(ctx, mc.player, flag);
            int i = lines.indexOf(CommonComponents.EMPTY);
            int j = tooltipLines.indexOf(CommonComponents.EMPTY);
            if (i == -1 || j == -1) return;
            lines.addAll(i, tooltipLines.subList(1, j));
        });
    }
    
    private static void tick(Minecraft minecraft) {
        if (minecraft.level == null) return;
        
        // rip fps
        // kinda surprised this doesn't kill the fps
        for (Entity entity : minecraft.level.entitiesForRendering()) {
            if (entity == null) continue;
            if (entity instanceof LivingEntity livingEntity) updateWearingArmoredElytra(livingEntity);
        }
    }
    
    private static void updateWearingArmoredElytra(LivingEntity livingEntity) {
        ItemStack chestSlot = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        ChestplateWithElytraItem item = ChestplateWithElytraItem.fromItemStack(chestSlot);
        if (item != null) armoredElytraMappings.put(livingEntity.getUUID(), item);
        else armoredElytraMappings.remove(livingEntity.getUUID());
    }
}
