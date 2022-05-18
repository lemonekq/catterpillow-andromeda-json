package caterpillow;

import net.minecraft.client.*;
import net.minecraft.client.settings.*;
import net.minecraft.entity.*;
import net.minecraftforge.common.*;
import java.util.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.block.*;
import org.lwjgl.input.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

public class AndromedaBot
{
    private Minecraft mc;
    private float startYaw;
    private float startPitch;
    private BlockPos lastBlock;
    private int blockCount;
    private boolean jumpNextTick;
    
    public AndromedaBot() {
        this.mc = Minecraft.func_71410_x();
        this.blockCount = 0;
        this.jumpNextTick = false;
    }
    
    public void onEnable() {
        if (this.mc.func_147104_D() != null && !this.canEnable()) {
            return;
        }
        if (this.mc.field_71415_G) {
            this.blockCount = 0;
            this.startYaw = this.mc.field_71439_g.field_70177_z;
            this.startPitch = this.mc.field_71439_g.field_70125_A;
            KeyBinding.func_74510_a(this.mc.field_71474_y.field_74351_w.func_151463_i(), true);
            KeyBinding.func_74510_a(this.mc.field_71474_y.field_151444_V.func_151463_i(), true);
            this.lastBlock = (this.isAirBlock(this.getBlock(new BlockPos((Entity)this.mc.field_71439_g).func_177977_b())) ? null : new BlockPos((Entity)this.mc.field_71439_g).func_177977_b());
            MinecraftForge.EVENT_BUS.register((Object)this);
        }
        else {
            Settings.setToggled(false);
        }
    }
    
    private boolean canEnable() {
        for (final String ip : Settings.getServers()) {
            if (this.mc.func_147104_D().field_78845_b.equalsIgnoreCase(ip) || this.mc.func_147104_D().field_78845_b.endsWith("aternos.me")) {
                return true;
            }
        }
        this.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("§cPlease go to #server-whitelist on caterpillow's discord to see what servers this mod can be used on."));
        Settings.setToggled(false);
        return false;
    }
    
    public void onDisable() {
        KeyBinding.func_74510_a(this.mc.field_71474_y.field_74351_w.func_151463_i(), false);
        KeyBinding.func_74510_a(this.mc.field_71474_y.field_74368_y.func_151463_i(), false);
        KeyBinding.func_74510_a(this.mc.field_71474_y.field_74314_A.func_151463_i(), false);
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.lastBlock == null) {
            this.mc.field_71439_g.func_146105_b((IChatComponent)new ChatComponentText("§cError: no blocks nearby found"));
            Settings.toggle();
            return;
        }
        if (this.mc.field_71439_g.field_70163_u == Math.round(this.mc.field_71439_g.field_70163_u)) {
            this.mc.field_71439_g.field_70177_z = this.startYaw;
            this.mc.field_71439_g.field_70125_A = this.startPitch;
            KeyBinding.func_74510_a(this.mc.field_71474_y.field_74351_w.func_151463_i(), true);
            KeyBinding.func_74510_a(this.mc.field_71474_y.field_74314_A.func_151463_i(), true);
        }
        else {
            KeyBinding.func_74510_a(this.mc.field_71474_y.field_74314_A.func_151463_i(), false);
        }
        if (this.placeBlockSimple(new BlockPos((Entity)this.mc.field_71439_g).func_177981_b(2), true, 1.0f)) {
            KeyBinding.func_74510_a(this.mc.field_71474_y.field_74351_w.func_151463_i(), false);
            KeyBinding.func_74510_a(this.mc.field_71474_y.field_74368_y.func_151463_i(), true);
        }
        else if (this.placeBlockSimple(new BlockPos((Entity)this.mc.field_71439_g).func_177977_b(), true, 1.0f)) {
            KeyBinding.func_74510_a(this.mc.field_71474_y.field_74351_w.func_151463_i(), false);
            KeyBinding.func_74510_a(this.mc.field_71474_y.field_74368_y.func_151463_i(), true);
        }
        else {
            this.mc.field_71439_g.field_70177_z = this.startYaw;
            this.mc.field_71439_g.field_70125_A = this.startPitch;
            KeyBinding.func_74510_a(this.mc.field_71474_y.field_74351_w.func_151463_i(), true);
            KeyBinding.func_74510_a(this.mc.field_71474_y.field_74368_y.func_151463_i(), false);
        }
    }
    
    public boolean isAirBlock(final Block block) {
        return block.func_149688_o().func_76222_j() && (!(block instanceof BlockSnow) || block.func_149669_A() <= 0.125);
    }
    
    public boolean placeBlockSimple(final BlockPos pos, final boolean place, final float partialTicks) {
        final Minecraft mc = Minecraft.func_71410_x();
        if (!this.doesSlotHaveBlocks(mc.field_71439_g.field_71071_by.field_70461_c)) {
            mc.field_71439_g.field_71071_by.field_70461_c = this.getFirstHotBarSlotWithBlocks();
        }
        if (!this.isAirBlock(this.getBlock(pos))) {
            return false;
        }
        final Entity entity = this.mc.func_175606_aa();
        final double d0 = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * partialTicks;
        final double d2 = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * partialTicks;
        final double d3 = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * partialTicks;
        final Vec3 eyesPos = new Vec3(d0, d2 + mc.field_71439_g.func_70047_e(), d3);
        for (final EnumFacing side : EnumFacing.values()) {
            if (!side.equals((Object)EnumFacing.UP)) {
                if (!side.equals((Object)EnumFacing.DOWN) || Keyboard.isKeyDown(mc.field_71474_y.field_74314_A.func_151463_i())) {
                    final BlockPos neighbor = pos.func_177972_a(side);
                    final EnumFacing side2 = side.func_176734_d();
                    if (this.getBlock(neighbor).func_176209_a(mc.field_71441_e.func_180495_p(neighbor), false)) {
                        final Vec3 hitVec = new Vec3((Vec3i)neighbor).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3(side2.func_176730_m()));
                        if (eyesPos.func_72436_e(hitVec) <= 36.0) {
                            final float[] angles = this.getRotations(neighbor, side2, partialTicks);
                            if (place) {
                                mc.field_71439_g.field_70177_z = angles[0];
                                mc.field_71439_g.field_70125_A = angles[1];
                                mc.field_71442_b.func_178890_a(mc.field_71439_g, mc.field_71441_e, mc.field_71439_g.func_71045_bC(), neighbor, side2, hitVec);
                                this.lastBlock = pos;
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public boolean doesSlotHaveBlocks(final int slotToCheck) {
        return this.mc.field_71439_g.field_71071_by.func_70301_a(slotToCheck) != null && this.mc.field_71439_g.field_71071_by.func_70301_a(slotToCheck).func_77973_b() instanceof ItemBlock && this.mc.field_71439_g.field_71071_by.func_70301_a(slotToCheck).field_77994_a > 0;
    }
    
    public boolean canPlace(final BlockPos pos) {
        final Minecraft mc = Minecraft.func_71410_x();
        final Vec3 eyesPos = new Vec3(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v);
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = pos.func_177972_a(side);
            final EnumFacing side2 = side.func_176734_d();
            final Vec3 hitVec = new Vec3((Vec3i)neighbor).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3(side2.func_176730_m()));
            if (eyesPos.func_72436_e(hitVec) <= 36.0) {
                return true;
            }
        }
        return false;
    }
    
    public Block getBlock(final BlockPos pos) {
        return Minecraft.func_71410_x().field_71441_e.func_180495_p(pos).func_177230_c();
    }
    
    public int getFirstHotBarSlotWithBlocks() {
        for (int i = 0; i < 9; ++i) {
            if (this.mc.field_71439_g.field_71071_by.func_70301_a(i) != null && this.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock) {
                return i;
            }
        }
        return 0;
    }
    
    public float[] getRotations(final BlockPos block, final EnumFacing face, final float partialTicks) {
        final Entity entity = this.mc.func_175606_aa();
        final double posX = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * partialTicks;
        final double posY = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * partialTicks;
        final double posZ = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * partialTicks;
        final double x = block.func_177958_n() + this.mc.field_71439_g.field_70165_t - Math.floor(this.mc.field_71439_g.field_70165_t) - posX + face.func_82601_c() / 2.0;
        final double z = block.func_177952_p() + this.mc.field_71439_g.field_70161_v - Math.floor(this.mc.field_71439_g.field_70161_v) - posZ + face.func_82599_e() / 2.0;
        final double y = block.func_177956_o() + 0.5;
        final double d1 = posY + this.mc.field_71439_g.func_70047_e() - y;
        final double d2 = MathHelper.func_76133_a(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(Math.atan2(d1, d2) * 180.0 / 3.141592653589793);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[] { yaw, pitch };
    }
}
