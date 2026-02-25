package me.chaounne.cybercraftplants;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PottedSugarCaneBlock extends Block {
    public static final IntProperty AGE = IntProperty.of("age", 0, 3);
    public static final IntProperty PLANT_DAY = IntProperty.of("plant_day", 0, 3);

    // Forme du pot de fleur
    protected static final VoxelShape SHAPE = Block.createCuboidShape(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);

    // Temps de croissance en ticks (5 minutes = 6000 ticks)
    private static final int GROWTH_TIME = 6000;

    public PottedSugarCaneBlock() {
        super(Settings.copy(Blocks.FLOWER_POT)
                .ticksRandomly());
        setDefaultState(getStateManager().getDefaultState()
                .with(AGE, 0)
                .with(PLANT_DAY, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE, PLANT_DAY);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(AGE) < 3;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        updateAge(world, pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        state = world.getBlockState(pos);

        //ItemStack heldItem = player.getStackInHand(hand);
        int age = state.get(AGE);

        if (age == 3) {
            int amount = ModConfig.MIN_SUGAR_CANE_DROP +
                    world.random.nextInt(ModConfig.MAX_SUGAR_CANE_DROP - ModConfig.MIN_SUGAR_CANE_DROP + 1);
            if(amount<2) amount = 2;
            dropStack(world, pos, new ItemStack(Items.SUGAR_CANE, amount));

            world.setBlockState(pos, state.with(AGE, 0), Block.NOTIFY_ALL);

            world.playSound(null, pos, SoundEvents.BLOCK_CROP_BREAK, SoundCategory.BLOCKS, 1.0f, 0.8f + world.random.nextFloat() * 0.4f);
            world.setBlockState(pos, Blocks.FLOWER_POT.getDefaultState(), Block.NOTIFY_ALL);

            return ActionResult.SUCCESS;
        }

        // For testing
        /**
        if(ModConfig.ALLOW_BONEMEAL && heldItem.getItem() == Items.BONE_MEAL){
            if (age < 3) {

                // Faire pousser de 1 à 2 niveaux
                int growth = 1 + world.random.nextInt(2);
                int newAge = Math.min(3, age + growth);

                world.setBlockState(pos, state.with(AGE, newAge), Block.NOTIFY_ALL);

                // Consommer la bone meal (sauf créatif)
                if (!player.getAbilities().creativeMode) {
                    heldItem.decrement(1);
                }

                // Particules de fertilisation (comme vanilla)
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(
                            net.minecraft.particle.ParticleTypes.HAPPY_VILLAGER,
                            pos.getX() + 0.5,
                            pos.getY() + 0.5,
                            pos.getZ() + 0.5,
                            8,
                            0.3, 0.3, 0.3,
                            0.0
                    );
                }

                world.playSound(
                        null,
                        pos,
                        SoundEvents.ITEM_BONE_MEAL_USE,
                        SoundCategory.BLOCKS,
                        1.0f,
                        1.0f
                );

                return ActionResult.SUCCESS;
            }
        }*/

        return ActionResult.PASS;
    }

    /**
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            int age = state.get(AGE);
            dropStack(world, pos, new ItemStack(Blocks.FLOWER_POT));
            if(age > 0) {
                dropStack(world, pos, new ItemStack(Items.SUGAR_CANE, age));
            } else {
                dropStack(world, pos, new ItemStack(Items.SUGAR_CANE));
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }*/

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(AGE) == 3) {
            if (random.nextFloat() < 0.3f) {
                double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.4;
                double y = pos.getY() + 0.6 + random.nextDouble() * 0.4;
                double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.4;

                world.addParticle(
                        net.minecraft.particle.ParticleTypes.HAPPY_VILLAGER,
                        x, y, z,
                        0.0, 0.02, 0.0
                );
            }
        }
    }

    private void updateAge(World world, BlockPos pos, BlockState state) {
        long currentDay = world.getTimeOfDay() / 24000L;
        int plantDay = state.get(PLANT_DAY);

        int elapsed = (int)((currentDay % 4) - plantDay);
        if (elapsed < 0) elapsed += 4;

        int newAge = Math.min(elapsed, 3);

        if (state.get(AGE) != newAge) {
            world.setBlockState(pos, state.with(AGE, newAge), Block.NOTIFY_ALL);
        }
    }
}
