package me.mindlessly.coinsclient;

import me.mindlessly.coinsclient.commands.CCCommand;
import me.mindlessly.coinsclient.commands.subcommands.Help;
import me.mindlessly.coinsclient.commands.subcommands.Subcommand;
import me.mindlessly.coinsclient.commands.subcommands.Toggle;
import me.mindlessly.coinsclient.events.OnChatReceived;
import me.mindlessly.coinsclient.events.OnWorldJoin;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class Main
{
	public static Config config = new Config();
    public static CCCommand commandManager = new CCCommand(new Subcommand[]{
            new Toggle(),
            new Help(),
        });
    public static String uuid;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	MinecraftForge.EVENT_BUS.register(new OnWorldJoin());	
    	MinecraftForge.EVENT_BUS.register(new OnChatReceived());
    	ClientCommandHandler.instance.registerCommand(commandManager);
    	config.preload();
    	uuid = Minecraft.getMinecraft().getSession().getPlayerID();
    }
}
