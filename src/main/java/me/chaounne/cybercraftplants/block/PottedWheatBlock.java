package me.chaounne.cybercraftplants.block;

import me.chaounne.cybercraftplants.Cybercraftplants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PottedWheatBlock extends Block {

    public static final IntProperty AGE = IntProperty.of("age", 0, 7);
    public static final IntProperty PLANT_DAY = IntProperty.of("plant_day", 0, 7);

    protected static final VoxelShape SHAPE = VoxelShapes.union(
            Block.createCuboidShape(2, 0, 2, 14, 8, 14),
            Block.createCuboidShape(3, 8, 3, 13, 8.1, 13)
    );

    public PottedWheatBlock() {
        super(Settings.copy(Blocks.FLOWER_POT).ticksRandomly());
        setDefaultState(getStateManager().getDefaultState()
                .with(AGE, 0)
                .with(PLANT_DAY, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE, PLANT_DAY);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, net.minecraft.block.ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(AGE) < 7;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        updateAge(world, pos, state);
    }

    private void updateAge(World world, BlockPos pos, BlockState state) {
        long currentDay = world.getTimeOfDay() / 24000L;
        int plantDay = state.get(PLANT_DAY);

        int elapsed = (int)((currentDay % 8) - plantDay);
        if (elapsed < 0) elapsed += 8;

        int newAge = Math.min(elapsed, 7);

        if (state.get(AGE) != newAge) {
            world.setBlockState(pos, state.with(AGE, newAge), Block.NOTIFY_ALL);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {

        int age = state.get(AGE);

        if (age == 7) {

            if (!world.isClient) {

                dropStack(world, pos, new ItemStack(Items.WHEAT, 1 + world.random.nextInt(5)));

                int seeds = 1 + world.random.nextInt(3);
                dropStack(world, pos, new ItemStack(Items.WHEAT_SEEDS, seeds));

                world.setBlockState(pos, Cybercraftplants.GROWING_POT.getDefaultState());

                world.playSound(null, pos,
                        SoundEvents.BLOCK_CROP_BREAK,
                        SoundCategory.BLOCKS,
                        1.0f,
                        1.0f);
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(AGE) == 7) {
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

    public void addAge(World world, BlockPos pos, BlockState state, int amount) {
        if (world.isClient) return;

        int plantDay = state.get(PLANT_DAY);

        int newPlantDay = (plantDay - amount + 8) % 8;

        world.setBlockState(pos, state.with(PLANT_DAY, newPlantDay), Block.NOTIFY_ALL);
        updateAge(world, pos, state);
    }
}