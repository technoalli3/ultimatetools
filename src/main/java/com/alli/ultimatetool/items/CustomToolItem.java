package com.alli.ultimatetool.items;

import com.alli.ultimatetool.UltimateToolClient;
import com.alli.ultimatetool.UltimateToolMain;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.alli.ultimatetool.UltimateToolClient.toggleShovelAndHoe;

public class CustomToolItem extends MiningToolItem {

    protected static final Map<Block, Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>> TILLING_ACTIONS;//hoe
    protected static final ImmutableMap STRIPPED_BLOCKS;//axe
    protected static final HashMap PATH_STATES;//shovel

    protected CustomToolItem(ToolMaterial material, float attackDamage, float attackSpeed,  Settings settings) {
        super(attackDamage, attackSpeed, material, UltimateToolMain.ULTIMATE_MINEABLE, settings);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        //Axe start
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        ItemStack itemStack = context.getStack();
        PlayerEntity playerEntity = context.getPlayer();
        Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> pair = (Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>)TILLING_ACTIONS.get(world.getBlockState(blockPos).getBlock());
        Optional<BlockState> optional = this.getStrippedState(blockState);
        Optional<BlockState> optional2 = Oxidizable.getDecreasedOxidationState(blockState);
        Optional<BlockState> optional3 = Optional.ofNullable((Block)((BiMap)HoneycombItem.WAXED_TO_UNWAXED_BLOCKS.get()).get(blockState.getBlock())).map((block2) -> {
            return block.getStateWithProperties(blockState);
        });
        Optional<BlockState> optional4 = Optional.empty();
        if (optional.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            optional4 = optional;
        } else if (optional2.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.syncWorldEvent(playerEntity, 3005, blockPos, 0);
            optional4 = optional2;
        } else if (optional3.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.syncWorldEvent(playerEntity, 3004, blockPos, 0);
            optional4 = optional3;
        }

        if (optional4.isPresent()) {
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos, itemStack);
            }

            world.setBlockState(blockPos, (BlockState) optional4.get(), 11);
            if (playerEntity != null) {
                itemStack.damage(1, playerEntity, (p) -> {
                    p.sendToolBreakStatus(context.getHand());
                });
            }

            return ActionResult.success(world.isClient);
            //axe end
        }else if (block instanceof AbstractPlantStemBlock abstractPlantStemBlock) {//shears
            if (!abstractPlantStemBlock.hasMaxAge(blockState)) {
                if (playerEntity instanceof ServerPlayerEntity) {
                    Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
                }

                world.playSound(playerEntity, blockPos, SoundEvents.BLOCK_GROWING_PLANT_CROP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.setBlockState(blockPos, abstractPlantStemBlock.withMaxAge(blockState));
                if (playerEntity != null) {
                    itemStack.damage(1, playerEntity, (player) -> {
                        player.sendToolBreakStatus(context.getHand());
                    });
                }

                return ActionResult.success(world.isClient);
            }
        } else {//hoe start
            if (toggleShovelAndHoe) {//hoe
                if (pair == null) {
                    return ActionResult.PASS;
                } else {

                    Predicate<ItemUsageContext> predicate = (Predicate<ItemUsageContext>) pair.getFirst();
                    Consumer<ItemUsageContext> consumer = (Consumer<ItemUsageContext>) pair.getSecond();
                    if (predicate.test(context)) {
                        world.playSound(playerEntity, blockPos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        if (!world.isClient) {
                            consumer.accept(context);
                            if (playerEntity != null) {
                                context.getStack().damage(1, playerEntity, (p) -> {
                                    p.sendToolBreakStatus(context.getHand());
                                });
                            }
                        }

                        return ActionResult.success(world.isClient);
                    } else {
                        return ActionResult.PASS;
                    }
                }
            } else {//shovel
                if (context.getSide() == Direction.DOWN) {
                    return ActionResult.PASS;
                } else {
                    BlockState blockState2 = (BlockState)PATH_STATES.get(blockState.getBlock());
                    BlockState blockState3 = null;
                    if (blockState2 != null && world.getBlockState(blockPos.up()).isAir()) {
                        world.playSound(playerEntity, blockPos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        blockState3 = blockState2;
                    } else if (blockState.getBlock() instanceof CampfireBlock && (Boolean)blockState.get(CampfireBlock.LIT)) {
                        if (!world.isClient()) {
                            world.syncWorldEvent((PlayerEntity)null, 1009, blockPos, 0);
                        }

                        CampfireBlock.extinguish(context.getPlayer(), world, blockPos, blockState);
                        blockState3 = (BlockState)blockState.with(CampfireBlock.LIT, false);
                    }

                    if (blockState3 != null) {
                        if (!world.isClient) {
                            world.setBlockState(blockPos, blockState3, 11);
                            if (playerEntity != null) {
                                context.getStack().damage(1, playerEntity, (p) -> {
                                    p.sendToolBreakStatus(context.getHand());
                                });
                            }
                        }

                        return ActionResult.success(world.isClient);
                    }
                }
            }
        }
        return ActionResult.PASS;
    }//onUse end

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if(UltimateToolClient.toggleShears) {
            if(state.isIn(UltimateToolMain.SHEARABLE_PLANTS)){
                Block block = state.getBlock();
                Item item = block.asItem();
                ItemStack stack2 = item.getDefaultStack();
                Block.dropStack(world, pos, stack2);
            }
        }
        return super.postMine(stack, world, state, pos, miner);
    }

    public static Consumer<ItemUsageContext> createTillAction(BlockState result) {
        return (context) -> {
            context.getWorld().setBlockState(context.getBlockPos(), result, 11);
        };
    }

    public static Consumer<ItemUsageContext> createTillAndDropAction(BlockState result, ItemConvertible droppedItem) {
        return (context) -> {
            context.getWorld().setBlockState(context.getBlockPos(), result, 11);
            Block.dropStack(context.getWorld(), context.getBlockPos(), context.getSide(), new ItemStack(droppedItem));
        };
    }

    public static boolean canTillFarmland(ItemUsageContext context) {
        return context.getSide() != Direction.DOWN && context.getWorld().getBlockState(context.getBlockPos().up()).isAir();
    }

    private Optional<BlockState> getStrippedState(BlockState state) {
        return Optional.ofNullable((Block)STRIPPED_BLOCKS.get(state.getBlock())).map((block) -> {
            return (BlockState)block.getDefaultState().with(PillarBlock.AXIS, (Direction.Axis)state.get(PillarBlock.AXIS));
        });
    }

    static {
        TILLING_ACTIONS = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT_PATH, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.COARSE_DIRT, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.DIRT.getDefaultState())), Blocks.ROOTED_DIRT, Pair.of((itemUsageContext) -> {
            return true;
        }, createTillAndDropAction(Blocks.DIRT.getDefaultState(), Items.HANGING_ROOTS))));
        STRIPPED_BLOCKS = (new ImmutableMap.Builder()).put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD).put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG).put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD).put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG).put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD).put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG).put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD).put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG).put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD).put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG).put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD).put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG).put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM).put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE).put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM).put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE).build();
        PATH_STATES = Maps.newHashMap((new ImmutableMap.Builder()).put(Blocks.GRASS_BLOCK, Blocks.DIRT_PATH.getDefaultState()).put(Blocks.DIRT, Blocks.DIRT_PATH.getDefaultState()).put(Blocks.PODZOL, Blocks.DIRT_PATH.getDefaultState()).put(Blocks.COARSE_DIRT, Blocks.DIRT_PATH.getDefaultState()).put(Blocks.MYCELIUM, Blocks.DIRT_PATH.getDefaultState()).put(Blocks.ROOTED_DIRT, Blocks.DIRT_PATH.getDefaultState()).build());
    }
}
