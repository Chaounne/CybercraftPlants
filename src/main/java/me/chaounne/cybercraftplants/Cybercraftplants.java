package me.chaounne.cybercraftplants;

import me.chaounne.cybercraftplants.block.GrowingPotBlock;
import me.chaounne.cybercraftplants.block.PottedSugarCaneBlock;
import me.chaounne.cybercraftplants.block.PottedWheatBlock;
import me.chaounne.cybercraftplants.item.WateringCanItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cybercraftplants implements ModInitializer {
    public static final String MOD_ID = "cybercraftplants";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Block POTTED_SUGAR_CANE = new PottedSugarCaneBlock();
    public static final Block POTTED_WHEAT = new PottedWheatBlock();
    public static final Block GROWING_POT = new GrowingPotBlock();
    public static final Item WATERING_CAN = new WateringCanItem(new FabricItemSettings().maxCount(1));

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Cybercraft Plants Mod");

        // Config
        ModConfig.load();

        Identifier id = new Identifier(MOD_ID, "potted_sugar_cane");
        Identifier id_wheat = new Identifier(MOD_ID, "potted_wheat");
        Identifier id_pot = new Identifier(MOD_ID, "growing_pot");

        // Block
        Registry.register(Registries.BLOCK, id, POTTED_SUGAR_CANE);
        Registry.register(Registries.BLOCK, id_wheat, POTTED_WHEAT);
        Registry.register(Registries.BLOCK, id_pot, GROWING_POT);

        // Item
        Registry.register(Registries.ITEM, id_pot, new BlockItem(GROWING_POT, new FabricItemSettings()));
        Identifier id_watering_can = new Identifier(MOD_ID, "watering_can");
        Registry.register(Registries.ITEM, id_watering_can, WATERING_CAN);

        // Item Group
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
            content.add(GROWING_POT);
            content.add(WATERING_CAN);
        });

        //UseBlockCallback.EVENT.register(this::onUseBlock);

        LOGGER.info("Cybercraft Plants Mod initialized successfully");
    }

    /**
    private ActionResult onUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        Item item = stack.getItem();

        if (stack.getItem() != Items.SUGAR_CANE && stack.getItem() != Items.WHEAT_SEEDS) {
            return ActionResult.PASS;
        }

        BlockPos pos = hit.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (state.isOf(Blocks.FLOWER_POT)) {

            if (!world.isClient) {
                long day = world.getTimeOfDay() / 24000L;

                if (item == Items.WHEAT_SEEDS) {
                    world.setBlockState(pos, POTTED_WHEAT.getDefaultState()
                            .with(PottedWheatBlock.PLANT_DAY, (int) (day % 8)));
                }
                else if (item == Items.SUGAR_CANE) {
                    world.setBlockState(pos, Cybercraftplants.POTTED_SUGAR_CANE.getDefaultState()
                            .with(PLANT_DAY, (int) (day % 4)));
                }

                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }

                world.playSound(null, pos, SoundEvents.BLOCK_CROP_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }

            return ActionResult.SUCCESS;
        }


        return ActionResult.PASS;
    }
     */
}

