package lk.pixcapsoft.diamondscanner;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import java.util.List;

public class DiamondResultsScreen extends Screen {
    private final Screen parent;
    private final List<BlockPos> orePositions;
    private final String oreType;
    private int scrollOffset = 0;
    private static final int LINE_HEIGHT = 12;
    private static final int VISIBLE_LINES = 15;

    public DiamondResultsScreen(Screen parent, List<BlockPos> orePositions, String oreType) {
        super(Text.literal(oreType + " Scan Results"));
        this.parent = parent;
        this.orePositions = orePositions;
        this.oreType = oreType;
    }

    @Override
    protected void init() {
        super.init();
        
        // Close button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> {
            if (this.client != null) {
                this.client.setScreen(this.parent);
            }
        }).dimensions(this.width / 2 - 50, this.height - 30, 100, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Simple dark background without blur (safe for all versions)
        context.fill(0, 0, this.width, this.height, 0xE0101010);
        
        // Debug: Send to chat (only once per screen open)
        if (this.client != null && this.client.player != null && scrollOffset == 0) {
            if (orePositions.isEmpty()) {
                //this.client.player.sendMessage(Text.literal("§c[DEBUG] orePositions is EMPTY!"), false);
            } else {
                //this.client.player.sendMessage(Text.literal("§a[DEBUG] Rendering " + orePositions.size() + " positions"), false);
            }
        }
        
        // Title with ore type - using Text object
        Text titleText = Text.literal(oreType + " Scan Results");
        context.drawCenteredTextWithShadow(this.textRenderer, titleText, this.width / 2, 15, 0xFFFFFF);
        
        // Result count with appropriate color
        int countColor = oreType.equals("Ancient Debris") ? 0xFFFF6B3E : 0xFF55FFFF;
        Text countText = Text.literal("Found " + orePositions.size() + " " + oreType.toLowerCase() + " ore(s)");
        context.drawCenteredTextWithShadow(this.textRenderer, countText, this.width / 2, 30, countColor);
        
        // Keybind reminder
        Text reminderText = Text.literal("Press H to reopen these results anytime");
        context.drawCenteredTextWithShadow(this.textRenderer, reminderText, this.width / 2, 45, 0xFF888888);
        
        // Box background
        int boxX = this.width / 2 - 150;
        int boxY = 65;
        int boxWidth = 300;
        int boxHeight = VISIBLE_LINES * LINE_HEIGHT + 10;
        
        // Debug box info (send once)
        if (this.client != null && this.client.player != null && scrollOffset == 0) {
            //this.client.player.sendMessage(Text.literal("§e[DEBUG] Box at X:" + boxX + " Y:" + boxY), false);
        }
        
        context.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0xAA000000);
        
        // Draw border
        int borderColor = 0xFFFFFFFF; // Changed to white for visibility
        context.fill(boxX, boxY, boxX + boxWidth, boxY + 1, borderColor); // Top
        context.fill(boxX, boxY + boxHeight - 1, boxX + boxWidth, boxY + boxHeight, borderColor); // Bottom
        context.fill(boxX, boxY, boxX + 1, boxY + boxHeight, borderColor); // Left
        context.fill(boxX + boxWidth - 1, boxY, boxX + boxWidth, boxY + boxHeight, borderColor); // Right
        
        // Render ore positions with scroll
        int startIndex = scrollOffset;
        int endIndex = Math.min(startIndex + VISIBLE_LINES, orePositions.size());
        
        // Debug render range (send once)
        if (this.client != null && this.client.player != null && scrollOffset == 0) {
            //this.client.player.sendMessage(Text.literal("§b[DEBUG] Rendering index " + startIndex + " to " + endIndex), false);
        }
        
        for (int i = startIndex; i < endIndex; i++) {
            BlockPos pos = orePositions.get(i);
            String emoji = oreType.equals("Ancient Debris") ? "[Fire]" : "[Dia]";
            Text posText = Text.literal(emoji + " X:" + pos.getX() + " Y:" + pos.getY() + " Z:" + pos.getZ());
            int textY = boxY + 5 + (i - startIndex) * LINE_HEIGHT;
            
            // Debug first position only
            if (i == startIndex && this.client != null && this.client.player != null && scrollOffset == 0) {
                //this.client.player.sendMessage(Text.literal("§d[DEBUG] First pos at Y=" + textY + ": " + posText.getString()), false);
            }
            
            context.drawTextWithShadow(this.textRenderer, posText, boxX + 10, textY, 0xFFFFFFFF);
        }
        
        // Scroll indicators
        if (scrollOffset > 0) {
            Text scrollUpText = Text.literal("^ Scroll Up");
            context.drawCenteredTextWithShadow(this.textRenderer, scrollUpText, this.width / 2, boxY - 15, 0xFFFFFF55);
        }
        if (endIndex < orePositions.size()) {
            Text scrollDownText = Text.literal("v Scroll Down");
            context.drawCenteredTextWithShadow(this.textRenderer, scrollDownText, this.width / 2, boxY + boxHeight + 5, 0xFFFFFF55);
        }
        
        // Instructions
        if (orePositions.size() > VISIBLE_LINES) {
            Text instructionText = Text.literal("Use mouse wheel to scroll");
            context.drawCenteredTextWithShadow(this.textRenderer, instructionText, this.width / 2, this.height - 50, 0xFF888888);
        }
        
        // Render widgets (buttons) LAST so they appear on top
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int maxScroll = Math.max(0, orePositions.size() - VISIBLE_LINES);
        
        if (verticalAmount > 0) {
            // Scroll up
            scrollOffset = Math.max(0, scrollOffset - 1);
        } else if (verticalAmount < 0) {
            // Scroll down
            scrollOffset = Math.min(maxScroll, scrollOffset + 1);
        }
        
        return true;
    }

    @Override
    public boolean shouldPause() {
        return false; // Don't pause the game
    }
}