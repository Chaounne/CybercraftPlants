package me.chaounne.cybercraftplants.item;

import me.chaounne.cybercraftplants.block.PottedSugarCaneBlock;
import me.chaounne.cybercraftplants.block.PottedWheatBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WateringCanItem extends Item {

    public WateringCanItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();

        BlockState state = world.getBlockState(pos);

        if (state.contains(PottedSugarCaneBlock.AGE)) {
            int age = context.getWorld().getBlockState(pos).get(PottedSugarCaneBlock.AGE);
            int plantDay = context.getWorld().getBlockState(pos).get(PottedSugarCaneBlock.PLANT_DAY);
            System.out.println("PLANT DAY " + plantDay);
            System.out.println("AGE " + age);
            if (age < 3) {
                world.setBlockState(pos, state.with(PottedSugarCaneBlock.AGE, age + 1));
                world.setBlockState(pos, state.with(PottedSugarCaneBlock.PLANT_DAY, (plantDay+1)%4));
                playEffects(world, pos);
                return ActionResult.SUCCESS;
            }
        }

        if (state.contains(PottedWheatBlock.AGE)) {
            int age = context.getWorld().getBlockState(pos).get(PottedWheatBlock.AGE);
            int plantDay = context.getWorld().getBlockState(pos).get(PottedWheatBlock.PLANT_DAY);
            System.out.println("PLANT DAY " + plantDay);
            System.out.println("AGE " + age);
            if (age < 7) {
                world.setBlockState(pos, state.with(PottedWheatBlock.AGE, age + 1));
                world.setBlockState(pos, state.with(PottedWheatBlock.PLANT_DAY, (plantDay+1)%4));
                playEffects(world, pos);
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    private void playEffects(World world, BlockPos pos) {
        if (!world.isClient) return;

        for (int i = 0; i < 5; i++) {
            world.addParticle(
                    ParticleTypes.SPLASH,
                    pos.getX() + 0.5,
                    pos.getY() + 1,
                    pos.getZ() + 0.5,
                    0, 0.1, 0
            );
        }

        world.playSound(
                pos.getX(), pos.getY(), pos.getZ(),
                SoundEvents.ITEM_BUCKET_EMPTY,
                SoundCategory.BLOCKS,
                0.5f,
                1.2f,
                false
        );
    }
}