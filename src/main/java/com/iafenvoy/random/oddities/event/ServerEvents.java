package com.iafenvoy.random.oddities.event;

import com.iafenvoy.random.oddities.config.ROCommonConfig;
import com.iafenvoy.random.oddities.registry.ROBlocks;
import com.iafenvoy.random.oddities.registry.ROEnchantments;
import com.iafenvoy.random.oddities.registry.ROItems;
import com.iafenvoy.random.oddities.util.RegistryUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber
public final class ServerEvents {
    @SubscribeEvent
    public static void handleDamage(AttackEntityEvent event) {
        Entity entity = event.getTarget();
        Player player = event.getEntity();
        Level level = player.level();
        InteractionHand hand = player.getUsedItemHand();
        ItemStack stack = player.getItemInHand(hand);
        //Plastic chair
        if (stack.is(ItemTags.AXES) && entity instanceof LivingEntity living && living.getItemBySlot(EquipmentSlot.HEAD).is(ROBlocks.PLASTIC_CHAIR.get().asItem())) {
            SoundEvent soundEvent = SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR;
            if (Math.random() < 0.25) {
                living.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                soundEvent = SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR;
            }
            level.playSound(null, entity, soundEvent, SoundSource.BLOCKS, 1, 0);
            event.setCanceled(true);
        }
        //Kindness Enchantment
        if (entity instanceof LivingEntity living && living.isBaby() && ROCommonConfig.INSTANCE.enchantments.kindness.getValue() && stack.getEnchantmentLevel(RegistryUtil.enchantment(level.registryAccess(), ROEnchantments.KINDNESS)) > 0)
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getState().is(ROBlocks.PLASTIC_CHAIR.get()) && !event.getPlayer().isCreative())
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void addSmithingTemplate(LootTableLoadEvent event) {
        event.getTable().addPool(new LootPool.Builder().add(LootItem.lootTableItem(ROItems.ELYTRA_COMBINE_SMITHING_TEMPLATE.get()).setQuality(1).setWeight(1)).when(LootItemRandomChanceCondition.randomChance(1.0f / 15)).build());
    }
}
