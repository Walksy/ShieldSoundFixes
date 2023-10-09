package walksy.shieldfixes.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import walksy.shieldfixes.ShieldFixes;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    //Way too buggy
    //Sometimes doesn't show the player using a shield

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void fixPlayerShieldRendering(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        if (ShieldFixes.config.shieldRendering) {
            ItemStack itemStack = player.getStackInHand(player.getActiveHand());
            UseAction useAction = itemStack.getUseAction();
            if (useAction == UseAction.BLOCK && !checkShieldState(player)) {
                cir.setReturnValue(BipedEntityModel.ArmPose.ITEM);
            } else if (checkShieldState(player)) {
                cir.setReturnValue(BipedEntityModel.ArmPose.BLOCK);
            }
        }
    }

    @Unique
    private static boolean checkShieldState(LivingEntity player) {
        ItemStack itemStack = player.getStackInHand(player.getActiveHand());
        UseAction useAction = itemStack.getUseAction();
        return useAction == UseAction.BLOCK && player.isBlocking();
    }
}