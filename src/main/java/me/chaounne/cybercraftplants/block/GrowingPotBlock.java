package me.chaounne.cybercraftplants.block;

import me.chaounne.cybercraftplants.Cybercraftplants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class GrowingPotBlock extends Block {
    protected static final VoxelShape SHAPE = VoxelShapes.union(
            Block.createCuboidShape(2, 0, 2, 14, 8, 14),
            Block.createCuboidShape(3, 8, 3, 13, 8.1, 13)
    );

    public GrowingPotBlock() {
        super(Settings.copy(Blocks.FLOWER_POT)
                .ticksRandomly());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);

        if (stack.isOf(Items.SUGAR_CANE)) {
            if (!world.isClient) {
                long day = world.getTimeOfDay() / 24000L;
                world.setBlockState(pos, Cybercraftplants.POTTED_SUGAR_CANE.getDefaultState()
                        .with(PottedSugarCaneBlock.AGE, 0)
                        .with(PottedSugarCaneBlock.PLANT_DAY, (int) (day % 4)));

                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }

                world.playSound(null, pos, SoundEvents.BLOCK_CROP_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
            return ActionResult.SUCCESS;
        }

        if (stack.isOf(Items.WHEAT_SEEDS)){
            if (!world.isClient) {
                long day = world.getTimeOfDay() / 24000L;
                world.setBlockState(pos, Cybercraftplants.POTTED_WHEAT.getDefaultState()
                        .with(PottedWheatBlock.AGE, 0)
                        .with(PottedWheatBlock.PLANT_DAY, (int) (day % 4)));

                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }

                world.playSound(null, pos, SoundEvents.BLOCK_CROP_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
