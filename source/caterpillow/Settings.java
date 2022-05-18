package caterpillow;

import java.util.*;

public class Settings
{
    private static ArrayList<String> whitelistedServers;
    private static boolean toggled;
    private static int key;
    
    public static ArrayList<String> getServers() {
        return Settings.whitelistedServers;
    }
    
    public static void addServer(final String server) {
        Settings.whitelistedServers.add(server);
    }
    
    public static boolean isToggled() {
        return Settings.toggled;
    }
    
    public static void setToggled(final boolean toggled) {
        Settings.toggled = toggled;
    }
    
    public static int getKey() {
        return Settings.key;
    }
    
    public static void setKey(final int key) {
        Settings.key = key;
    }
    
    public static void toggle() {
        if (Settings.toggled) {
            Settings.toggled = false;
            AndromedaMod.andromedaBot.onDisable();
        }
        else {
            Settings.toggled = true;
            AndromedaMod.andromedaBot.onEnable();
        }
    }
    
    static {
        Settings.whitelistedServers = new ArrayList<String>();
        Settings.toggled = false;
        Settings.key = 0;
    }
}
