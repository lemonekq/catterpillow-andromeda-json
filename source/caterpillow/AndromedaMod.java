package caterpillow;

import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import java.net.*;
import org.apache.commons.io.*;
import java.io.*;
import net.minecraftforge.common.*;
import net.minecraftforge.client.*;
import net.minecraft.command.*;
import com.google.gson.*;
import java.util.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.*;
import org.lwjgl.input.*;
import net.minecraftforge.fml.common.eventhandler.*;

@Mod(modid = "Andromeda-Bot", version = "1.0", acceptedMinecraftVersions = "[1.8.9]")
public class AndromedaMod
{
    public static final String MODID = "Andromeda-Bot";
    public static final String VERSION = "1.0";
    public static AndromedaBot andromedaBot;
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        String whitelist;
        Gson g;
        JsonObject json;
        JsonArray serverList;
        final Iterator<JsonElement> iterator;
        JsonElement obj;
        final Thread servers = new Thread(() -> {
            try {
                whitelist = IOUtils.toString(new URL("https://caterpillowo.github.io/data/json/serverwhitelist.json"));
                g = new Gson();
                json = (JsonObject)g.fromJson(whitelist, (Class)JsonObject.class);
                serverList = json.get("servers").getAsJsonArray();
                serverList.iterator();
                while (iterator.hasNext()) {
                    obj = iterator.next();
                    Settings.addServer(obj.getAsString());
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return;
        });
        servers.start();
        AndromedaMod.andromedaBot = new AndromedaBot();
        MinecraftForge.EVENT_BUS.register((Object)this);
        ClientCommandHandler.instance.func_71560_a((ICommand)new Bind());
    }
    
    @SubscribeEvent
    public void key(final InputEvent.KeyInputEvent e) {
        if (Minecraft.func_71410_x().field_71441_e == null || Minecraft.func_71410_x().field_71439_g == null) {
            return;
        }
        try {
            if (Keyboard.isCreated() && Keyboard.getEventKeyState()) {
                final int keyCode = Keyboard.getEventKey();
                if (keyCode <= 0) {
                    return;
                }
                if (Settings.getKey() == keyCode && keyCode > 0) {
                    Settings.toggle();
                }
            }
        }
        catch (Exception q) {
            q.printStackTrace();
        }
    }
}
