package caterpillow;

import java.util.*;
import net.minecraft.util.*;
import org.lwjgl.input.*;
import net.minecraft.command.*;

public class Bind extends CommandBase
{
    public String func_71517_b() {
        return "andromedaBind";
    }
    
    public int func_82362_a() {
        return 0;
    }
    
    public String func_71518_a(final ICommandSender sender) {
        return "/andromedaBind <key>";
    }
    
    public List<String> func_71514_a() {
        final ArrayList<String> aliases = new ArrayList<String>();
        aliases.add("andromedabind");
        aliases.add("bindandromeda");
        aliases.add("bind");
        return aliases;
    }
    
    public void func_71515_b(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length == 0) {
            sender.func_145747_a((IChatComponent)new ChatComponentText("§cPlease specify a key"));
        }
        else {
            Settings.setKey(Keyboard.getKeyIndex(args[0].toUpperCase()));
            if (Settings.getKey() == 0) {
                sender.func_145747_a((IChatComponent)new ChatComponentText("§cPlease use a valid key"));
            }
            else {
                sender.func_145747_a((IChatComponent)new ChatComponentText("§aMod bound to " + Keyboard.getKeyName(Settings.getKey())));
            }
        }
    }
}
