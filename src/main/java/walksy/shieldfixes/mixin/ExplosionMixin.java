package walksy.shieldfixes.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import walksy.shieldfixes.ShieldFixes;

import java.util.List;
import java.util.Objects;

@Mixin(Explosion.class)
public class ExplosionMixin {

    /**
     * Full credit goes to uku3lig for this class of code
     * https://github.com/uku3lig/bettershields/blob/1.19.4/src/main/java/net/uku3lig/bettershields/mixin/MixinExplosion.java
     */
    @Shadow @Final private double x;
    @Shadow @Final
    private double y;
    @Shadow @Final private double z;

    @Shadow @Final private float power;

    @Shadow @Final private World world;

    @Inject(method = "affectWorld", at = @At("HEAD"))
    public void playShieldSoundsFromExplosion(boolean particles, CallbackInfo ci) {
        if (!ShieldFixes.config.shieldSounds) return;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (!this.world.isClient || player == null) return;

        double maxDistance = power * 2;
        Vec3d pos = new Vec3d(this.x, this.y, this.z);

        int x1 = MathHelper.floor(this.x - maxDistance - 1.0);
        int x2 = MathHelper.floor(this.x + maxDistance + 1.0);
        int y1 = MathHelper.floor(this.y - maxDistance - 1.0);
        int y2 = MathHelper.floor(this.y + maxDistance + 1.0);
        int z1 = MathHelper.floor(this.z - maxDistance - 1.0);
        int z2 = MathHelper.floor(this.z + maxDistance + 1.0);
        List<LivingEntity> nearEntities = this.world.getEntitiesByClass(LivingEntity.class, new Box(x1, y1, z1, x2, y2, z2), Objects::nonNull);

        for (LivingEntity nearEntity : nearEntities) {
            if (nearEntity.isAlive()) {
                double distance = Math.sqrt(nearEntity.squaredDistanceTo(pos));
                if (distance < maxDistance && checkShieldState(nearEntity)) {
                    world.playSound(nearEntity.getX(), nearEntity.getY(), nearEntity.getZ(), SoundEvents.ITEM_SHIELD_BLOCK, nearEntity.getSoundCategory(), ShieldFixes.config.shieldSoundVolume, 0.8F + world.random.nextFloat() * 0.4F, false);
                }
            }
        }
    }

    @Unique
    public boolean checkShieldState(LivingEntity player) {
        ItemStack itemStack = player.getStackInHand(player.getActiveHand());
        UseAction useAction = itemStack.getUseAction();
        return useAction == UseAction.BLOCK && player.isBlocking();
    }
}
