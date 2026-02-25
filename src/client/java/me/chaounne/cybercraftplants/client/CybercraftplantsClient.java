package me.chaounne.cybercraftplants.client;

import me.chaounne.cybercraftplants.Cybercraftplants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class CybercraftplantsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Cybercraftplants.POTTED_SUGAR_CANE, RenderLayer.getCutout());
    }
}
