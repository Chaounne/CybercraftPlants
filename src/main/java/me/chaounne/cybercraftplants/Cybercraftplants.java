package me.chaounne.cybercraftplants;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static me.chaounne.cybercraftplants.PottedSugarCaneBlock.PLANT_DAY;

public class Cybercraftplants implements ModInitializer {
    public static final String MOD_ID = "cybercraftplants";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Block POTTED_SUGAR_CANE = new PottedSugarCaneBlock();

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Cybercraft Plants Mod");

        // Config
        ModConfig.load();

        Identifier id = new Identifier(MOD_ID, "potted_sugar_cane");

        // Block
        Registry.register(Registries.BLOCK, id, POTTED_SUGAR_CANE);

        UseBlockCallback.EVENT.register(this::onUseBlock);

        LOGGER.info("Cybercraft Plants Mod initialized successfully");
    }

    private ActionResult onUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);

        if (stack.getItem() != Items.SUGAR_CANE) {
            return ActionResult.PASS;
        }

        BlockPos pos = hit.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (state.isOf(Blocks.FLOWER_POT)) {

            if (!world.isClient) {
                long day = world.getTimeOfDay() / 24000L;
                world.setBlockState(pos,
                        Cybercraftplants.POTTED_SUGAR_CANE.getDefaultState()
                                .with(PLANT_DAY, (int)(day % 4))
                );

                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }

                world.playSound(
                        null,
                        pos,
                        SoundEvents.BLOCK_CROP_BREAK,
                        SoundCategory.BLOCKS,
                        1.0f,
                        1.0f
                );
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}

