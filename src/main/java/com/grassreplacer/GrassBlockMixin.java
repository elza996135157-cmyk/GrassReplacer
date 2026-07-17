package com.grassreplacer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrassBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrassBlock.class)
public class GrassBlockMixin extends Block {
    public GrassBlockMixin(Settings settings) { super(settings); }
    
    @Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
    private void onGetColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
        if (GrassReplacerMod.isRealisticMode() && tintIndex == 0) {
            cir.setReturnValue(0xFFFFFF);
        }
    }
}
