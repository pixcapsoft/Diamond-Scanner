package lk.pixcapsoft.diamondscanner;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import  net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class KeyBindingHandler {
    public static KeyMapping scanKey;
    public static KeyMapping openResultsKey;

    // Store last scan results
    private static List<BlockPos> lastScanResults = new ArrayList<>();
    private static String lastScanType = "No scan performed yet";

    // Create the category once and reuse it
    private static KeyMapping keyBinding;
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.tryBuild("pixcapdiamondscanner", "category"));
    //private static final KeyBindingRegistryImpl CATEGORY = KeyBindingRegistryImpl.registerKeyBinding().setKey(
        //Identifier.tryBuild("pixcapdiamondscanner", "category")
    //);

    public static void register() {
        scanKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.pixcapdiamondscanner.scan",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                CATEGORY
        ));
//        scanKey = new KeyBindingHelper(
//                "key.pixcapdiamondscanner.scan",
//                Input.Type.KEYSYM,
//                GLFW.GLFW_KEY_G,
//                CATEGORY
//        );

        openResultsKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.pixcapdiamondscanner.openresults",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                CATEGORY
        ));

//        KeyBindingHelper.registerKeyBinding(scanKey);
//        KeyBindingHelper.registerKeyBinding(openResultsKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (scanKey.consumeClick()) {
                scanForOres(client);
            }

            while (openResultsKey.consumeClick()) {
                openLastResults(client);
            }
        });
    }

    private static void scanForOres(Minecraft client) {
        if (client.player == null || client.level == null) return;

        //Advancment

        // Only allow in singleplayer
        if (!client.isSingleplayer()) {
            client.player.sendSystemMessage(Component.literal("§cScanner only works in singleplayer worlds."));
            return;
        }

        BlockPos playerPos = client.player.getBlockPosBelowThatAffectsMyMovement();
        int radius = 64;
        List<BlockPos> foundPositions = new ArrayList<BlockPos>();

        // Check if player is in the Nether
        boolean isInNether = client.level.dimension() == Level.NETHER;

        //if (isInNether) {
        if (isInNether) {
            client.player.sendSystemMessage(Component.literal("§6Starting Ancient Debris scan..."));
            client.player.playSound(SoundEvents.BEACON_ACTIVATE, 1.0F, 1.0F);
            lastScanType = "Ancient Debris";

            // Scan for Ancient Debris in the Nether
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos scanPos = playerPos.offset(x, y, z);
                        if (client.level.getBlockState(scanPos).is(Blocks.ANCIENT_DEBRIS)) {
                            foundPositions.add(scanPos);
                        }
                    }
                }
            }
        } else {
            client.player.sendSystemMessage(Component.literal("§bStarting Diamond scan..."));
            client.player.playSound(SoundEvents.BEACON_ACTIVATE, 1.0F, 1.0F);
            lastScanType = "Diamonds";

            // Scan for Diamonds in Overworld/End
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos scanPos = playerPos.offset(x, y, z);
                        if (client.level.getBlockState(scanPos).is(Blocks.DIAMOND_ORE) ||
                                client.level.getBlockState(scanPos).is(Blocks.DEEPSLATE_DIAMOND_ORE)) {
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
            client.player.sendSystemMessage(Component.literal("§eNo " + lastScanType.toLowerCase() + " found nearby."));
        } else {
            client.player.sendSystemMessage(Component.literal("§aScan complete! Found " + foundPositions.size() + " ore(s). Opening results..."));
            client.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);
            client.execute(() -> {
                client.setScreen(new DiamondResultsScreen(null, foundPositions, lastScanType));
            });
        }
    }


    private static void openLastResults(Minecraft client) {
        if (client.player == null) return;

        client.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);

        if (lastScanResults.isEmpty()) {
            client.player.sendSystemMessage(Component.literal("§eNo previous scan results available. Press G(Default) to scan."));
            return;
        }

        client.execute(() -> {
            client.setScreen(new DiamondResultsScreen(null, lastScanResults, lastScanType));
        });
    }
}