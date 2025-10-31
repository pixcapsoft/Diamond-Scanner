package lk.pixcapsoft.diamondscanner;

import net.fabricmc.api.ClientModInitializer;

public class PixcapdiamondscannerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBindingHandler.register();
    }
}