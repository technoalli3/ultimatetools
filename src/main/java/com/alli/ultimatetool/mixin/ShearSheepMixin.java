package com.alli.ultimatetool.mixin;

import com.alli.ultimatetool.UltimateToolMain;
import com.google.common.collect.Maps;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Random;


@Mixin(SheepEntity.class)
public class ShearSheepMixin {
    private static final Map DROPS = (Map) Util.make(Maps.newEnumMap(DyeColor.class), (map) -> {
        map.put(DyeColor.WHITE, Blocks.WHITE_WOOL);
        map.put(DyeColor.ORANGE, Blocks.ORANGE_WOOL);
        map.put(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL);
        map.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL);
        map.put(DyeColor.YELLOW, Blocks.YELLOW_WOOL);
        map.put(DyeColor.LIME, Blocks.LIME_WOOL);
        map.put(DyeColor.PINK, Blocks.PINK_WOOL);
        map.put(DyeColor.GRAY, Blocks.GRAY_WOOL);
        map.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL);
        map.put(DyeColor.CYAN, Blocks.CYAN_WOOL);
        map.put(DyeColor.PURPLE, Blocks.PURPLE_WOOL);
        map.put(DyeColor.BLUE, Blocks.BLUE_WOOL);
        map.put(DyeColor.BROWN, Blocks.BROWN_WOOL);
        map.put(DyeColor.GREEN, Blocks.GREEN_WOOL);
        map.put(DyeColor.RED, Blocks.RED_WOOL);
        map.put(DyeColor.BLACK, Blocks.BLACK_WOOL);
    });



    public void sheared(SoundCategory shearedSoundCategory) {

        Random r = new Random();
        ((Entity) (Object) this).world.playSoundFromEntity((PlayerEntity) null, ((Entity) (Object) this), SoundEvents.ENTITY_SHEEP_SHEAR, shearedSoundCategory, 1.0F, 1.0F);
        ((SheepEntity)(Object)this).setSheared(true);
        int i = 1 + r.nextInt(3);

        for (int j = 0; j < i; ++j) {
            ItemEntity itemEntity = ((SheepEntity)(Object)this).dropItem((ItemConvertible)DROPS.get(((SheepEntity)(Object)this).getColor()), 1);
            if (itemEntity != null) {
                itemEntity.setVelocity(itemEntity.getVelocity().add((double) ((r.nextFloat() - r.nextFloat()) * 0.1F), (double) (r.nextFloat() * 0.05F), (double) ((r.nextFloat() - r.nextFloat()) * 0.1F)));
            }
        }
    }
    
    @Inject(method="interactMob", at=@At("HEAD"), cancellable = true)
    private void CheckForTool(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isIn(UltimateToolMain.ULTIMATE_TOOLS)) {
            if (!((Entity) (Object) this).world.isClient && ((SheepEntity) (Object) this).isShearable()) {
                sheared(SoundCategory.PLAYERS);
                ((Entity) (Object) this).emitGameEvent(GameEvent.SHEAR, player);
                itemStack.damage(1, player, (playerx) -> {
                    playerx.sendToolBreakStatus(hand);
                });
                cir.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }
}
