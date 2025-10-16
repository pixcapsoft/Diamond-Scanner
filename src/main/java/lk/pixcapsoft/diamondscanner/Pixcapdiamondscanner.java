package lk.pixcapsoft.diamondscanner;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
//import net.fabricmc.loader.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.Blocks;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
//import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pixcapdiamondscanner implements ModInitializer {
    public static final String MOD_ID = "pixcapdiamondscanner";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
public void onInitialize() {
    LOGGER.info("Pixcap Diamond Scanner mod loaded!");

    // Only register keybinding on client
    //if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
        //KeyBindingHandler.register();
   // }
}
}