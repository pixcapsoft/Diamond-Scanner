package lk.pixcapsoft.diamondscanner;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import java.util.List;

public class DiamondResultsScreen extends Screen {
    private final Screen parent;
    private final List<BlockPos> orePositions;
    private final String oreType;
    private int scrollOffset = 0;
    private static final int LINE_HEIGHT = 12;
    private static final int VISIBLE_LINES = 15;

    public DiamondResultsScreen(Screen parent, List<BlockPos> orePositions, String oreType) {
        super(Component.literal(oreType + " Scan Results"));
        this.parent = parent;
        this.orePositions = orePositions;
        this.oreType = oreType;
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(Button.builder(Component.literal("Close"), button -> {
            this.minecraft.setScreen(this.parent);
        }).bounds(this.width / 2 - 50, this.height - 30, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        // Dark background
        context.fill(0, 0, this.width, this.height, 0xE0101010);

        // Title
        MutableComponent titleText = Component.literal(oreType + " Scan Results");
        context.drawCenteredString(this.font, titleText, this.width / 2, 15, 0xFFFFFF);

        // Result count with color
        int countColor = oreType.equals("Ancient Debris") ? 0xFFFF6B3E : 0xFF55FFFF;
        MutableComponent countText = Component.literal("Found " + orePositions.size() + " " + oreType.toLowerCase() + " ore(s)");
        context.drawCenteredString(this.font, countText, this.width / 2, 30, countColor);

        // Keybind reminder
        MutableComponent reminderText = Component.literal("Press H to reopen these results anytime");
        context.drawCenteredString(this.font, reminderText, this.width / 2, 45, 0xFF888888);

        // Box background
        int boxX = this.width / 2 - 150;
        int boxY = 65;
        int boxWidth = 300;
        int boxHeight = VISIBLE_LINES * LINE_HEIGHT + 10;

        context.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0xAA000000);

        // Draw border
        int borderColor = 0xFFFFFFFF;
        context.fill(boxX, boxY, boxX + boxWidth, boxY + 1, borderColor); // Top
        context.fill(boxX, boxY + boxHeight - 1, boxX + boxWidth, boxY + boxHeight, borderColor); // Bottom
        context.fill(boxX, boxY, boxX + 1, boxY + boxHeight, borderColor); // Left
        context.fill(boxX + boxWidth - 1, boxY, boxX + boxWidth, boxY + boxHeight, borderColor); // Right

        // Render ore positions with scroll
        int startIndex = scrollOffset;
        int endIndex = Math.min(startIndex + VISIBLE_LINES, orePositions.size());

        for (int i = startIndex; i < endIndex; i++) {
            BlockPos pos = orePositions.get(i);
            String emoji = oreType.equals("Ancient Debris") ? "[Fire]" : "[Dia]";
            MutableComponent posText = Component.literal(emoji + " X:" + pos.getX() + " Y:" + pos.getY() + " Z:" + pos.getZ());
            int textY = boxY + 5 + (i - startIndex) * LINE_HEIGHT;

            // Left-aligned text
            context.drawString(this.font, posText, boxX + 10, textY, 0xFFFFFFFF);
        }

        // Scroll indicators
        if (scrollOffset > 0) {
            MutableComponent scrollUpText = Component.literal("^ Scroll Up");
            context.drawCenteredString(this.font, scrollUpText, this.width / 2, boxY - 15, 0xFFFFFF55);
        }
        if (endIndex < orePositions.size()) {
            MutableComponent scrollDownText = Component.literal("v Scroll Down");
            context.drawCenteredString(this.font, scrollDownText, this.width / 2, boxY + boxHeight + 5, 0xFFFFFF55);
        }

        // Instructions
        if (orePositions.size() > VISIBLE_LINES) {
            MutableComponent instructionText = Component.literal("Use mouse wheel to scroll");
            context.drawCenteredString(this.font, instructionText, this.width / 2, this.height - 50, 0xFF888888);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int maxScroll = Math.max(0, orePositions.size() - VISIBLE_LINES);

        if (verticalAmount > 0) {
            scrollOffset = Math.max(0, scrollOffset - 1);
        } else if (verticalAmount < 0) {
            scrollOffset = Math.min(maxScroll, scrollOffset + 1);
        }

        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}