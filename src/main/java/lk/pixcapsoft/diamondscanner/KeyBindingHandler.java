package lk.pixcapsoft.diamondscanner;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
<<<<<<< HEAD
import org.lwjgl.glfw.GLFW;

public class KeyBindingHandler {
    public static KeyBinding scanKey;
=======
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class KeyBindingHandler {
    public static KeyBinding scanKey;
    public static KeyBinding openResultsKey;
    
    // Store last scan results
    private static List<BlockPos> lastScanResults = new ArrayList<>();
    private static String lastScanType = "No scan performed yet";
    
    // Create the category once and reuse it
    private static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(
        Identifier.of("pixcapdiamondscanner", "category")
    );
>>>>>>> f1eaac0 (V 0.0.3 Stable)

    public static void register() {
        scanKey = new KeyBinding(
                "key.pixcapdiamondscanner.scan",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
<<<<<<< HEAD
                KeyBinding.Category.create(Identifier.of("pixcapdiamondscanner", "category"))
        );

        KeyBindingHelper.registerKeyBinding(scanKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (scanKey.wasPressed()) {
                scanForDiamonds(client);
=======
                CATEGORY
        );
        
        openResultsKey = new KeyBinding(
                "key.pixcapdiamondscanner.openresults",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                CATEGORY
        );

        KeyBindingHelper.registerKeyBinding(scanKey);
        KeyBindingHelper.registerKeyBinding(openResultsKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (scanKey.wasPressed()) {
                scanForOres(client);
            }
            
            while (openResultsKey.wasPressed()) {
                openLastResults(client);
>>>>>>> f1eaac0 (V 0.0.3 Stable)
            }
        });
    }

<<<<<<< HEAD
    private static void scanForDiamonds(MinecraftClient client) {
    if (client.player == null || client.world == null) return;
    
    // Only allow in singleplayer
    if (!client.isInSingleplayer()) {
        client.player.sendMessage(Text.literal("§cDiamond Scanner only works in singleplayer worlds."), false);
        return;
    }
    
    client.player.sendMessage(Text.literal("Starting scanning engine..."), false);

    BlockPos playerPos = client.player.getBlockPos();
    int radius = 32;
    int found = 0;

    for (int x = -radius; x <= radius; x++) {
        for (int y = -radius; y <= radius; y++) {
            for (int z = -radius; z <= radius; z++) {
                BlockPos scanPos = playerPos.add(x, y, z);
                if (client.world.getBlockState(scanPos).isOf(Blocks.DIAMOND_ORE) ||
                        client.world.getBlockState(scanPos).isOf(Blocks.DEEPSLATE_DIAMOND_ORE)) {
                    client.player.sendMessage(Text.literal("💎 Found diamond at: " + scanPos.toShortString()), false);
                    found++;
                }
            }
        }
    }

    if (found == 0) {
        client.player.sendMessage(Text.literal("No diamonds found nearby."), false);
    } else {
        client.player.sendMessage(Text.literal("Scan complete. Found " + found + " diamond ores."), false);
        client.player.sendMessage((Text.literal("Open chat if you only seeing few...")), false);
    }
}
=======
    private static void scanForOres(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        
        // Only allow in singleplayer
        if (!client.isInSingleplayer()) {
            client.player.sendMessage(Text.literal("§cScanner only works in singleplayer worlds."), false);
            return;
        }
        
        BlockPos playerPos = client.player.getBlockPos();
        int radius = 64;
        List<BlockPos> foundPositions = new ArrayList<>();
        
        // Check if player is in the Nether
        boolean isInNether = client.world.getRegistryKey() == World.NETHER;
        
        if (isInNether) {
            client.player.sendMessage(Text.literal("§6Starting Ancient Debris scan..."), false);
            lastScanType = "Ancient Debris";
            
            // Scan for Ancient Debris in the Nether
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos scanPos = playerPos.add(x, y, z);
                        if (client.world.getBlockState(scanPos).isOf(Blocks.ANCIENT_DEBRIS)) {
                            foundPositions.add(scanPos);
                        }
                    }
                }
            }
        } else {
            client.player.sendMessage(Text.literal("§bStarting Diamond scan..."), false);
            lastScanType = "Diamonds";
            
            // Scan for Diamonds in Overworld/End
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos scanPos = playerPos.add(x, y, z);
                        if (client.world.getBlockState(scanPos).isOf(Blocks.DIAMOND_ORE) ||
                                client.world.getBlockState(scanPos).isOf(Blocks.DEEPSLATE_DIAMOND_ORE)) {
                            foundPositions.add(scanPos);
                        }
                    }
                }
            }
        }

        // Store results
        lastScanResults = foundPositions;

        // Show results in GUI
        if (foundPositions.isEmpty()) {
            client.player.sendMessage(Text.literal("§eNo " + lastScanType.toLowerCase() + " found nearby."), false);
        } else {
            client.player.sendMessage(Text.literal("§aScan complete! Found " + foundPositions.size() + " ore(s). Opening results..."), false);
            client.execute(() -> {
                client.setScreen(new DiamondResultsScreen(null, foundPositions, lastScanType));
            });
        }
    }
    
    private static void openLastResults(MinecraftClient client) {
        if (client.player == null) return;
        
        if (lastScanResults.isEmpty()) {
            client.player.sendMessage(Text.literal("§eNo previous scan results available. Press G(Default) to scan."), false);
            return;
        }
        
        client.execute(() -> {
            client.setScreen(new DiamondResultsScreen(null, lastScanResults, lastScanType));
        });
    }
>>>>>>> f1eaac0 (V 0.0.3 Stable)
}