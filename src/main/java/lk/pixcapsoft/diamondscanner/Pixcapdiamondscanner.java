package lk.pixcapsoft.diamondscanner;

import net.fabricmc.api.ModInitializer;
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