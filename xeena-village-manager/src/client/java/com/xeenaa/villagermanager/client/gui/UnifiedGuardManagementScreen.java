package com.xeenaa.villagermanager.client.gui;

import com.xeenaa.villagermanager.XeenaaVillagerManager;
import com.xeenaa.villagermanager.client.data.ClientGuardDataCache;
import com.xeenaa.villagermanager.client.network.ownership.ClientOwnershipCache;
import com.xeenaa.villagermanager.config.GuardBehaviorConfig;
import com.xeenaa.villagermanager.config.GuardMode;
import com.xeenaa.villagermanager.config.ModConfig;
import com.xeenaa.villagermanager.data.GuardData;
import com.xeenaa.villagermanager.data.rank.GuardPath;
import com.xeenaa.villagermanager.data.rank.GuardRank;
import com.xeenaa.villagermanager.network.GuardConfigPacket;
import com.xeenaa.villagermanager.network.GuardProfessionChangePacket;
import com.xeenaa.villagermanager.network.PurchaseRankPacket;
import com.xeenaa.villagermanager.network.SelectProfessionPacket;
import com.xeenaa.villagermanager.network.ownership.BindVillagerPacket;
import com.xeenaa.villagermanager.network.ownership.UnbindVillagerPacket;
import com.xeenaa.villagermanager.profession.ModProfessions;
import com.xeenaa.villagermanager.registry.ProfessionData;
import com.xeenaa.villagermanager.registry.ProfessionManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Unified single-screen interface for villager/guard management.
 * Combines profession selection, rank management, and configuration in one view.
 */
public class UnifiedGuardManagementScreen extends Screen {
    // Screen dimensions
    private static final int FRAME_WIDTH = 640; // Increased from 600 to prevent overlap
    private static final int FRAME_HEIGHT = 340; // Increased to accommodate 8 rows of professions

    // Panel dimensions
    private static final int LEFT_PANEL_WIDTH = 240; // 40% of 600
    private static final int RIGHT_PANEL_WIDTH = 340; // 60% of 600 (minus padding)
    private static final int PANEL_PADDING = 10;

    // Colors matching the desired UI
    private static final int FRAME_BORDER_COLOR = 0xFF8B7355; // Brown/tan border
    private static final int FRAME_CORNER_COLOR = 0xFFC4A55E; // Gold corners
    private static final int LEFT_PANEL_BG = 0xFF000000; // Black background
    private static final int RIGHT_PANEL_BG = 0xFF2C2C2C; // Dark gray background
    private static final int BUTTON_COLOR = 0xFF606060; // Gray buttons
    private static final int BUTTON_HOVER_COLOR = 0xFF808080; // Lighter gray on hover
    private static final int SELECTED_COLOR = 0xFFFFFF00; // Yellow for selected

    private final VillagerEntity targetVillager;
    private GuardData guardData;

    // Layout coordinates
    private int frameX, frameY;
    private int leftPanelX, leftPanelY;
    private int rightPanelX, rightPanelY;

    // Profession buttons (2 columns)
    private final List<ProfessionButton> professionButtons = new ArrayList<>();
    private final List<ProfessionData> availableProfessions = new ArrayList<>();

    // Control buttons
    private ButtonWidget lockProfessionButton;
    private ButtonWidget meleePathButton;
    private ButtonWidget rangedPathButton;
    private ButtonWidget detectionRangeButton;
    private ButtonWidget guardModeButton;
    private ButtonWidget saveConfigButton;

    // Ownership buttons
    private ButtonWidget claimVillagerButton;
    private ButtonWidget releaseOwnershipButton;

    // Confirmation dialog state
    private boolean showingConfirmDialog = false;
    private Identifier pendingProfessionId = null;
    private String pendingProfessionName = null;

    // Release confirmation dialog state
    private boolean showingReleaseConfirmDialog = false;

    // Confirmation dialog buttons
    private ButtonWidget confirmButton;
    private ButtonWidget cancelButton;

    public UnifiedGuardManagementScreen(VillagerEntity villager) {
        super(Text.literal("Villager Management"));
        this.targetVillager = villager;

        // Load guard data if this is a guard
        if (villager.getVillagerData().getProfession() == ModProfessions.GUARD) {
            ClientGuardDataCache cache = ClientGuardDataCache.getInstance();
            this.guardData = cache.getGuardData(villager);
        }
    }

    @Override
    protected void init() {
        super.init();

        // Reload guard data in case profession changed since screen was created
        reloadGuardData();

        calculateLayout();
        loadAvailableProfessions();
        createProfessionButtons();
        createControlButtons();

        XeenaaVillagerManager.LOGGER.debug("Initialized UnifiedGuardManagementScreen");
    }

    /**
     * Reloads guard data from the client cache to ensure UI reflects current profession.
     * <p>
     * This method is called during init() to handle profession changes. When a villager's
     * profession is changed to Guard, the server creates GuardData and syncs it to the
     * client cache. This method reads that fresh data so rank controls appear immediately.
     * </p>
     * <p>
     * <strong>Bug Fix (P3-TASK-008):</strong> Before this method existed, guardData was
     * only loaded in the constructor. After profession changes, the screen would refresh
     * but guardData remained stale, causing rank buttons to not appear.
     * </p>
     *
     * @since 1.0.0
     */
    private void reloadGuardData() {
        if (targetVillager.getVillagerData().getProfession() == ModProfessions.GUARD) {
            ClientGuardDataCache cache = ClientGuardDataCache.getInstance();
            this.guardData = cache.getGuardData(targetVillager);
            XeenaaVillagerManager.LOGGER.debug("Reloaded guard data for villager {} - guardData is {}",
                targetVillager.getUuid(), guardData != null ? "present" : "null");
        } else {
            // Clear guard data if villager is no longer a guard
            this.guardData = null;
            XeenaaVillagerManager.LOGGER.debug("Villager {} is not a guard, cleared guardData",
                targetVillager.getUuid());
        }
    }

    private void calculateLayout() {
        // Center the frame
        frameX = (width - FRAME_WIDTH) / 2;
        frameY = (height - FRAME_HEIGHT) / 2;

        // Left panel (profession list)
        leftPanelX = frameX + PANEL_PADDING;
        leftPanelY = frameY + PANEL_PADDING;

        // Right panel (rank/config)
        rightPanelX = frameX + LEFT_PANEL_WIDTH + PANEL_PADDING * 2;
        rightPanelY = frameY + PANEL_PADDING;
    }

    private void loadAvailableProfessions() {
        availableProfessions.clear();

        ProfessionManager manager = ProfessionManager.getInstance();
        ModConfig config = ModConfig.getInstance();

        VillagerProfession currentProfession = targetVillager.getVillagerData().getProfession();
        Identifier currentProfessionId = Registries.VILLAGER_PROFESSION.getId(currentProfession);

        XeenaaVillagerManager.LOGGER.info("=== Loading Professions for UI ===");
        XeenaaVillagerManager.LOGGER.info("Current profession: {}", currentProfessionId);

        List<ProfessionData> allProfessions = manager.getAllProfessionData();
        XeenaaVillagerManager.LOGGER.info("ProfessionManager returned {} professions", allProfessions.size());

        for (ProfessionData professionData : allProfessions) {
            XeenaaVillagerManager.LOGGER.info("Checking profession: {} (blacklisted: {})",
                professionData.getId(),
                config.isProfessionBlacklisted(professionData.getId()));

            // Skip blacklisted professions
            if (config.isProfessionBlacklisted(professionData.getId())) {
                XeenaaVillagerManager.LOGGER.debug("Skipping blacklisted profession: {}", professionData.getId());
                continue;
            }

            // ALWAYS include Guard profession regardless of config for UI display
            // The server will enforce the enabled/disabled check
            availableProfessions.add(professionData);
            XeenaaVillagerManager.LOGGER.info("Added profession to UI: {}", professionData.getId());
        }

        XeenaaVillagerManager.LOGGER.info("=== Loaded {} professions for UI ===", availableProfessions.size());
    }

    private void createProfessionButtons() {
        professionButtons.clear();

        int buttonWidth = (LEFT_PANEL_WIDTH - PANEL_PADDING * 3) / 2; // 2 columns
        int buttonHeight = 24;
        int buttonSpacing = 5;

        int startX = leftPanelX + 10;
        int startY = leftPanelY + 30; // Leave space for title

        int row = 0;
        int col = 0;

        VillagerProfession currentProfession = targetVillager.getVillagerData().getProfession();
        Identifier currentProfessionId = Registries.VILLAGER_PROFESSION.getId(currentProfession);

        for (int i = 0; i < availableProfessions.size() && i < 16; i++) { // Max 8 rows x 2 columns
            ProfessionData professionData = availableProfessions.get(i);

            int buttonX = startX + col * (buttonWidth + buttonSpacing);
            int buttonY = startY + row * (buttonHeight + buttonSpacing);

            boolean isSelected = professionData.getId().equals(currentProfessionId);

            ProfessionButton button = new ProfessionButton(
                buttonX, buttonY, buttonWidth, buttonHeight,
                professionData,
                btn -> selectProfession(((ProfessionButton) btn).getProfessionData()),
                isSelected
            );

            professionButtons.add(button);
            addDrawableChild(button); // Make buttons clickable!

            col++;
            if (col >= 2) {
                col = 0;
                row++;
            }
        }
    }

    private void createControlButtons() {
        // Ownership buttons (top of right panel)
        createOwnershipButtons();

        // Lock Profession button (bottom of left panel)
        int lockButtonY = frameY + FRAME_HEIGHT - 40;
        boolean isLocked = guardData != null && guardData.getBehaviorConfig().professionLocked();

        lockProfessionButton = ButtonWidget.builder(
            Text.literal("Lock Profession: " + (isLocked ? "ON" : "OFF")),
            button -> toggleProfessionLock()
        ).dimensions(leftPanelX + 10, lockButtonY, LEFT_PANEL_WIDTH - 20, 20).build();

        addDrawableChild(lockProfessionButton);

        // Only create guard-specific buttons if this is a guard
        if (guardData != null) {
            // Path selection buttons (middle of right panel)
            int pathButtonY = rightPanelY + 120;
            int pathButtonWidth = (RIGHT_PANEL_WIDTH - 30) / 2;

            meleePathButton = ButtonWidget.builder(
                Text.literal("⚔ Melee Path"),
                button -> selectPath(GuardPath.MELEE)
            ).dimensions(rightPanelX + 10, pathButtonY, pathButtonWidth, 24).build();

            rangedPathButton = ButtonWidget.builder(
                Text.literal("🏹 Ranged Path"),
                button -> selectPath(GuardPath.RANGED)
            ).dimensions(rightPanelX + 15 + pathButtonWidth, pathButtonY, pathButtonWidth, 24).build();

            addDrawableChild(meleePathButton);
            addDrawableChild(rangedPathButton);

            // Detection Range and Guard Mode buttons (bottom of right panel)
            int bottomButtonY = frameY + FRAME_HEIGHT - 40;
            int bottomButtonWidth = (RIGHT_PANEL_WIDTH - 30) / 2;

            GuardBehaviorConfig config = guardData.getBehaviorConfig();

            detectionRangeButton = ButtonWidget.builder(
                Text.literal("Detection Range: " + (int)config.detectionRange() + " blocks"),
                button -> cycleDetectionRange()
            ).dimensions(rightPanelX + 10, bottomButtonY, bottomButtonWidth, 20).build();

            guardModeButton = ButtonWidget.builder(
                Text.literal("Guard Mode: " + config.guardMode().getDisplayName()),
                button -> cycleGuardMode()
            ).dimensions(rightPanelX + 15 + bottomButtonWidth, bottomButtonY, bottomButtonWidth, 20).build();

            addDrawableChild(detectionRangeButton);
            addDrawableChild(guardModeButton);
        }

        // Save and Cancel buttons (outside frame, bottom center)
        int buttonWidth = 100;
        int buttonSpacing = 10;
        int totalWidth = (buttonWidth * 2) + buttonSpacing;
        int saveButtonX = frameX + (FRAME_WIDTH - totalWidth) / 2;
        int cancelButtonX = saveButtonX + buttonWidth + buttonSpacing;
        int buttonY = frameY + FRAME_HEIGHT + 10;

        saveConfigButton = ButtonWidget.builder(
            Text.literal("Save Configuration"),
            button -> saveConfiguration()
        ).dimensions(saveButtonX, buttonY, buttonWidth, 20).build();

        ButtonWidget cancelButton = ButtonWidget.builder(
            Text.literal("Cancel"),
            button -> close()
        ).dimensions(cancelButtonX, buttonY, buttonWidth, 20).build();

        addDrawableChild(saveConfigButton);
        addDrawableChild(cancelButton);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // Semi-opaque dark overlay (60% opaque) - balanced for readability and world blocking
        context.fill(0, 0, this.width, this.height, 0x99000000);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);

        // Render main frame background (black)
        context.fill(frameX, frameY, frameX + FRAME_WIDTH, frameY + FRAME_HEIGHT, 0xFF000000);

        // Render chest-style frame border
        renderFrameBorder(context);

        // Render left panel (black background) - already matches frame
        context.fill(leftPanelX, leftPanelY, leftPanelX + LEFT_PANEL_WIDTH,
            leftPanelY + FRAME_HEIGHT - PANEL_PADDING * 2, LEFT_PANEL_BG);

        // Render right panel (dark gray background)
        context.fill(rightPanelX, rightPanelY, rightPanelX + RIGHT_PANEL_WIDTH,
            rightPanelY + FRAME_HEIGHT - PANEL_PADDING * 2, RIGHT_PANEL_BG);

        // Render profession list title
        context.drawText(textRenderer, Text.literal("Profession").styled(style -> style.withBold(true)),
            leftPanelX + 10, leftPanelY + 10, 0xFFFFFF, true);

        // Render profession buttons
        for (ProfessionButton button : professionButtons) {
            button.render(context, mouseX, mouseY, delta);
        }

        // Render owner badge (top-right of right panel)
        renderOwnerBadge(context);

        // Render right panel content
        if (guardData != null) {
            renderGuardInfo(context);
        } else {
            renderNonGuardInfo(context);
        }

        // Render all widgets (buttons)
        super.render(context, mouseX, mouseY, delta);

        // Render confirmation dialogs LAST (on top of everything)
        if (showingConfirmDialog) {
            renderConfirmationDialog(context, mouseX, mouseY, delta);
        }
        if (showingReleaseConfirmDialog) {
            renderReleaseConfirmationDialog(context, mouseX, mouseY, delta);
        }
    }

    private void renderFrameBorder(DrawContext context) {
        // Main frame border (chest-style)
        int borderThickness = 4;

        // Top border
        context.fill(frameX, frameY, frameX + FRAME_WIDTH, frameY + borderThickness, FRAME_BORDER_COLOR);
        // Bottom border
        context.fill(frameX, frameY + FRAME_HEIGHT - borderThickness, frameX + FRAME_WIDTH, frameY + FRAME_HEIGHT, FRAME_BORDER_COLOR);
        // Left border
        context.fill(frameX, frameY, frameX + borderThickness, frameY + FRAME_HEIGHT, FRAME_BORDER_COLOR);
        // Right border
        context.fill(frameX + FRAME_WIDTH - borderThickness, frameY, frameX + FRAME_WIDTH, frameY + FRAME_HEIGHT, FRAME_BORDER_COLOR);

        // Corner decorations (gold squares)
        int cornerSize = 8;
        // Top-left
        context.fill(frameX, frameY, frameX + cornerSize, frameY + cornerSize, FRAME_CORNER_COLOR);
        // Top-right
        context.fill(frameX + FRAME_WIDTH - cornerSize, frameY, frameX + FRAME_WIDTH, frameY + cornerSize, FRAME_CORNER_COLOR);
        // Bottom-left
        context.fill(frameX, frameY + FRAME_HEIGHT - cornerSize, frameX + cornerSize, frameY + FRAME_HEIGHT, FRAME_CORNER_COLOR);
        // Bottom-right
        context.fill(frameX + FRAME_WIDTH - cornerSize, frameY + FRAME_HEIGHT - cornerSize,
            frameX + FRAME_WIDTH, frameY + FRAME_HEIGHT, FRAME_CORNER_COLOR);
    }

    private void renderGuardInfo(DrawContext context) {
        // Title
        GuardRank currentRank = guardData.getRankData().getCurrentRank();
        String title = "Guard Ranking - Choose Path";
        if (currentRank.getTier() > 0) {
            title = "Guard Ranking - " + currentRank.getDisplayName();
        }
        context.drawText(textRenderer, Text.literal(title).styled(style -> style.withBold(true)),
            rightPanelX + 10, rightPanelY + 10, 0xFFFFFF, true);

        // Current rank box
        int boxY = rightPanelY + 35;
        context.fill(rightPanelX + 10, boxY, rightPanelX + RIGHT_PANEL_WIDTH - 10, boxY + 40, 0xFF1A1A1A);
        context.drawBorder(rightPanelX + 10, boxY, RIGHT_PANEL_WIDTH - 20, 40, 0xFF404040);

        String rankText = currentRank.getDisplayName() + " (Tier " + currentRank.getTier() + "/4)";
        context.drawText(textRenderer, Text.literal(rankText).formatted(Formatting.WHITE),
            rightPanelX + 15, boxY + 5, 0xFFFFFF, false);

        // Stats
        String stats = "Health: " + (int)currentRank.getStats().getMaxHealth() + " HP | Damage: " + (int)currentRank.getStats().getAttackDamage();
        context.drawText(textRenderer, Text.literal(stats).formatted(Formatting.GRAY),
            rightPanelX + 15, boxY + 20, 0xCCCCCC, false);

        // Tier progress
        String tierText = "Tier " + currentRank.getTier() + "/4";
        context.drawText(textRenderer, Text.literal(tierText),
            rightPanelX + 10, boxY + 50, 0xFFFFFF, false);

        // Emerald cost info
        int costY = rightPanelY + 155;
        int playerEmeralds = countEmeralds();

        context.drawText(textRenderer, Text.literal("You have: " + playerEmeralds + " emeralds").formatted(Formatting.GREEN),
            rightPanelX + 15, costY, 0x00FF00, false);

        if (currentRank.getTier() < 4) {
            String meleePathCost = "✖ Melee Path: 15 emeralds";
            String rangedPathCost = "✖ Ranged Path: 15 emeralds";

            if (currentRank.getTier() == 0) {
                if (playerEmeralds >= 15) {
                    context.drawText(textRenderer, Text.literal("✓ Can afford both paths").formatted(Formatting.GREEN),
                        rightPanelX + RIGHT_PANEL_WIDTH - 180, costY, 0x00FF00, false);
                }
            }

            context.drawText(textRenderer, Text.literal(meleePathCost).formatted(Formatting.GREEN),
                rightPanelX + 15, costY + 12, 0x00FF00, false);
            context.drawText(textRenderer, Text.literal(rangedPathCost).formatted(Formatting.GREEN),
                rightPanelX + 15, costY + 24, 0x00FF00, false);
        }
    }

    private void renderNonGuardInfo(DrawContext context) {
        // Show message for non-guards
        String message = "Select Guard profession to access ranking system";
        int messageWidth = textRenderer.getWidth(message);
        context.drawText(textRenderer, Text.literal(message).formatted(Formatting.GRAY),
            rightPanelX + (RIGHT_PANEL_WIDTH - messageWidth) / 2, rightPanelY + 100, 0xCCCCCC, true);
    }

    /**
     * Handles profession selection with Guard profession change confirmation.
     * <p>
     * If changing FROM Guard TO another profession, shows a confirmation dialog
     * warning about data loss. Otherwise proceeds immediately with the change.
     * </p>
     *
     * @param professionData the profession to change to
     */
    private void selectProfession(ProfessionData professionData) {
        XeenaaVillagerManager.LOGGER.info("Selecting profession: {}", professionData.getId());

        // Check if changing FROM guard TO another profession
        if (isCurrentlyGuard() && !isProfessionGuard(professionData.getId())) {
            // Store pending change and show confirmation dialog
            this.pendingProfessionId = professionData.getId();
            this.pendingProfessionName = professionData.getTranslatedName().getString();
            this.showingConfirmDialog = true;
            initConfirmDialog();
            XeenaaVillagerManager.LOGGER.info("Showing confirmation dialog for Guard -> {} profession change",
                professionData.getId());
            return; // Don't proceed with change yet
        }

        // Otherwise proceed normally with profession change
        sendProfessionChangePacket(professionData.getId());
    }

    /**
     * Sends the profession change packet to the server and refreshes the UI.
     *
     * @param professionId the profession identifier to change to
     */
    private void sendProfessionChangePacket(Identifier professionId) {
        SelectProfessionPacket packet = new SelectProfessionPacket(
            targetVillager.getId(),
            professionId
        );
        ClientPlayNetworking.send(packet);

        // Refresh screen after short delay to reflect changes
        MinecraftClient.getInstance().execute(() -> {
            new Thread(() -> {
                try {
                    Thread.sleep(300);
                    MinecraftClient.getInstance().execute(() -> {
                        this.clearAndInit();
                    });
                } catch (InterruptedException e) {
                    // Ignore interruption
                }
            }).start();
        });
    }

    private void selectPath(GuardPath path) {
        if (guardData == null) return;

        GuardRank currentRank = guardData.getRankData().getCurrentRank();

        // Get the next rank in progression
        GuardRank nextRank = null;

        // If at recruit, select the first rank of the chosen path
        if (currentRank == GuardRank.RECRUIT) {
            if (path == GuardPath.MELEE) {
                nextRank = GuardRank.MAN_AT_ARMS_I;
            } else if (path == GuardPath.RANGED) {
                nextRank = GuardRank.MARKSMAN_I;
            }
        } else {
            // If already on a path, get the next rank in that path
            nextRank = currentRank.getNextRank();
        }

        if (nextRank != null) {
            XeenaaVillagerManager.LOGGER.info("Purchasing next rank: {} (current: {})",
                nextRank.getDisplayName(), currentRank.getDisplayName());
            PurchaseRankPacket packet = new PurchaseRankPacket(targetVillager.getUuid(), nextRank);
            ClientPlayNetworking.send(packet);
        } else {
            XeenaaVillagerManager.LOGGER.warn("No next rank available (current: {})", currentRank.getDisplayName());
        }
    }

    private void toggleProfessionLock() {
        if (guardData == null) return;

        GuardBehaviorConfig config = guardData.getBehaviorConfig();
        GuardBehaviorConfig newConfig = new GuardBehaviorConfig(
            config.detectionRange(),
            config.guardMode(),
            !config.professionLocked(),
            config.followTargetPlayerId()
        );

        GuardConfigPacket packet = new GuardConfigPacket(targetVillager.getUuid(), newConfig);
        ClientPlayNetworking.send(packet);

        // Update button text
        lockProfessionButton.setMessage(Text.literal("Lock Profession: " + (newConfig.professionLocked() ? "ON" : "OFF")));
    }

    private void cycleDetectionRange() {
        if (guardData == null) return;

        GuardBehaviorConfig config = guardData.getBehaviorConfig();
        double newRange = config.detectionRange() + 5;
        if (newRange > 30) newRange = 10;

        GuardBehaviorConfig newConfig = new GuardBehaviorConfig(
            newRange,
            config.guardMode(),
            config.professionLocked(),
            config.followTargetPlayerId()
        );

        GuardConfigPacket packet = new GuardConfigPacket(targetVillager.getUuid(), newConfig);
        ClientPlayNetworking.send(packet);

        detectionRangeButton.setMessage(Text.literal("Detection Range: " + (int)newRange + " blocks"));
    }

    private void cycleGuardMode() {
        if (guardData == null) return;

        GuardBehaviorConfig config = guardData.getBehaviorConfig();
        GuardMode[] modes = GuardMode.values();
        int currentIndex = config.guardMode().ordinal();
        GuardMode newMode = modes[(currentIndex + 1) % modes.length];

        GuardBehaviorConfig newConfig = new GuardBehaviorConfig(
            config.detectionRange(),
            newMode,
            config.professionLocked(),
            config.followTargetPlayerId()
        );

        GuardConfigPacket packet = new GuardConfigPacket(targetVillager.getUuid(), newConfig);
        ClientPlayNetworking.send(packet);

        guardModeButton.setMessage(Text.literal("Guard Mode: " + newMode.getDisplayName()));
    }

    private void saveConfiguration() {
        // Configuration is saved automatically when buttons are clicked
        // This button provides user feedback
        this.close();
    }

    private int countEmeralds() {
        if (client == null || client.player == null) return 0;

        int count = 0;
        for (ItemStack stack : client.player.getInventory().main) {
            if (stack.getItem() == Items.EMERALD) {
                count += stack.getCount();
            }
        }
        return count;
    }

    /**
     * Renders the confirmation dialog for Guard profession changes.
     * <p>
     * Displays a modal dialog with semi-transparent overlay, warning message,
     * current guard information, and Confirm/Cancel buttons.
     * </p>
     *
     * @param context the draw context for rendering
     * @param mouseX current mouse X position
     * @param mouseY current mouse Y position
     * @param delta frame time delta
     */
    private void renderConfirmationDialog(DrawContext context, int mouseX, int mouseY, float delta) {
        // 1. Draw semi-transparent overlay (80% opacity)
        context.fill(0, 0, this.width, this.height, 0xCC000000);

        // 2. Calculate dialog position (centered)
        int dialogWidth = 300;
        int dialogHeight = 150;
        int dialogX = (this.width - dialogWidth) / 2;
        int dialogY = (this.height - dialogHeight) / 2;

        // 3. Draw dialog background and border
        context.fill(dialogX, dialogY, dialogX + dialogWidth, dialogY + dialogHeight, 0xFF2C2C2C);
        context.drawBorder(dialogX, dialogY, dialogWidth, dialogHeight, 0xFFFFFFFF);

        // 4. Draw title
        Text title = Text.literal("Confirm Profession Change").styled(style -> style.withBold(true));
        int titleWidth = textRenderer.getWidth(title);
        context.drawText(textRenderer, title,
            dialogX + (dialogWidth - titleWidth) / 2, dialogY + 10, 0xFFFFFF, true);

        // 5. Draw warning message
        Text warning1 = Text.literal("Changing from Guard to " + pendingProfessionName);
        Text warning2 = Text.literal("will reset all guard progression");
        context.drawText(textRenderer, warning1, dialogX + 10, dialogY + 35, 0xFFAA00, false);
        context.drawText(textRenderer, warning2, dialogX + 10, dialogY + 50, 0xFFAA00, false);

        // 6. Draw current guard info
        if (guardData != null) {
            GuardRank currentRank = guardData.getRankData().getCurrentRank();
            String rankInfo = "Current Rank: " + currentRank.getDisplayName() +
                ", Tier: " + currentRank.getTier() + "/4";
            context.drawText(textRenderer, Text.literal(rankInfo), dialogX + 10, dialogY + 70, 0xAAAAAA, false);
        }

        // 7. Render buttons (they handle their own rendering)
        if (confirmButton != null) {
            confirmButton.render(context, mouseX, mouseY, delta);
        }
        if (cancelButton != null) {
            cancelButton.render(context, mouseX, mouseY, delta);
        }
    }

    /**
     * Initializes the confirmation dialog buttons.
     * <p>
     * Creates Confirm and Cancel buttons positioned at the bottom of the dialog.
     * Confirm button proceeds with profession change, Cancel button dismisses dialog.
     * </p>
     * <p>
     * <strong>CRITICAL:</strong> Buttons must be added to drawable children to receive
     * click events. They are also removed when the dialog closes to prevent memory leaks.
     * </p>
     */
    private void initConfirmDialog() {
        int dialogWidth = 300;
        int dialogHeight = 150;
        int dialogX = (this.width - dialogWidth) / 2;
        int dialogY = (this.height - dialogHeight) / 2;

        int buttonWidth = 80;
        int buttonHeight = 20;
        int buttonY = dialogY + 110;

        // Confirm button (left side)
        confirmButton = ButtonWidget.builder(Text.literal("Confirm"), button -> {
            XeenaaVillagerManager.LOGGER.info("User confirmed Guard -> {} profession change", pendingProfessionId);
            closeConfirmDialog();

            // Send GuardProfessionChangePacket with confirmed=true (not SelectProfessionPacket!)
            // This tells the server the user confirmed the emerald loss warning
            GuardProfessionChangePacket packet = new GuardProfessionChangePacket(
                targetVillager.getId(),
                pendingProfessionId,
                true  // confirmed
            );
            ClientPlayNetworking.send(packet);

            this.pendingProfessionId = null;
            this.pendingProfessionName = null;

            // Refresh screen after short delay to reflect changes
            MinecraftClient.getInstance().execute(() -> {
                new Thread(() -> {
                    try {
                        Thread.sleep(300);
                        MinecraftClient.getInstance().execute(() -> {
                            this.clearAndInit();
                        });
                    } catch (InterruptedException e) {
                        // Ignore interruption
                    }
                }).start();
            });
        })
        .dimensions(dialogX + 50, buttonY, buttonWidth, buttonHeight)
        .build();

        // Cancel button (right side)
        cancelButton = ButtonWidget.builder(Text.literal("Cancel"), button -> {
            XeenaaVillagerManager.LOGGER.info("User cancelled Guard profession change");
            closeConfirmDialog();
            this.pendingProfessionId = null;
            this.pendingProfessionName = null;
        })
        .dimensions(dialogX + 170, buttonY, buttonWidth, buttonHeight)
        .build();

        // NOTE: Buttons are NOT added to drawable children because they're rendered
        // manually in renderConfirmationDialog() AFTER the overlay. Adding them to
        // drawable children would cause them to be rendered twice and covered by overlay.
    }

    /**
     * Closes the confirmation dialog and cleans up button references.
     * <p>
     * This method ensures proper cleanup of dialog button references.
     * Buttons are not added to drawable children, so no removal is needed.
     * </p>
     */
    private void closeConfirmDialog() {
        this.showingConfirmDialog = false;

        // Clear button references (they were never added to drawable children)
        confirmButton = null;
        cancelButton = null;

        XeenaaVillagerManager.LOGGER.debug("Confirmation dialog closed and buttons cleared");
    }

    /**
     * Checks if the current villager is a Guard.
     *
     * @return true if the villager's profession is Guard, false otherwise
     */
    private boolean isCurrentlyGuard() {
        return targetVillager.getVillagerData().getProfession() == ModProfessions.GUARD;
    }

    /**
     * Checks if the given profession ID is the Guard profession.
     *
     * @param professionId the profession identifier to check
     * @return true if the profession is Guard, false otherwise
     */
    private boolean isProfessionGuard(Identifier professionId) {
        return professionId.equals(Identifier.of(XeenaaVillagerManager.MOD_ID, "guard"));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Handle ESC key when dialogs are showing
        if (showingConfirmDialog && keyCode == 256) { // GLFW.GLFW_KEY_ESCAPE = 256
            XeenaaVillagerManager.LOGGER.info("User pressed ESC to cancel Guard profession change");
            closeConfirmDialog();
            this.pendingProfessionId = null;
            this.pendingProfessionName = null;
            return true;
        }

        if (showingReleaseConfirmDialog && keyCode == 256) { // GLFW.GLFW_KEY_ESCAPE = 256
            XeenaaVillagerManager.LOGGER.info("User pressed ESC to cancel release ownership");
            closeReleaseConfirmDialog();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Handle profession change dialog button clicks
        if (showingConfirmDialog) {
            if (confirmButton != null) {
                if (confirmButton.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
            if (cancelButton != null) {
                if (cancelButton.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
            // Block all other clicks when dialog is showing
            return true;
        }

        // Handle release ownership dialog button clicks
        if (showingReleaseConfirmDialog) {
            if (confirmButton != null) {
                if (confirmButton.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
            if (cancelButton != null) {
                if (cancelButton.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
            // Block all other clicks when dialog is showing
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    /**
     * Creates ownership-related UI buttons (Claim/Release).
     *
     * <p>Button visibility depends on ownership status:</p>
     * <ul>
     *   <li>Unowned villager: Shows "Claim Villager" button</li>
     *   <li>Owned by player: Shows "Release Ownership" button</li>
     *   <li>Owned by other: Shows owner badge only, no buttons</li>
     * </ul>
     */
    private void createOwnershipButtons() {
        ClientOwnershipCache ownershipCache = ClientOwnershipCache.getInstance();
        UUID villagerUUID = targetVillager.getUuid();
        UUID localPlayerUUID = client != null && client.player != null ? client.player.getUuid() : null;

        boolean isOwned = ownershipCache.hasOwner(villagerUUID);
        UUID ownerUUID = ownershipCache.getOwnerUUID(villagerUUID);
        boolean isOwnedByPlayer = isOwned && localPlayerUUID != null && localPlayerUUID.equals(ownerUUID);

        int buttonY = rightPanelY + 40;
        int buttonWidth = 120;

        if (!isOwned) {
            // Show "Claim Villager" button for unowned villagers
            claimVillagerButton = ButtonWidget.builder(
                Text.literal("Claim Villager").styled(style -> style.withColor(0x00FF00)),
                button -> handleClaimVillager()
            ).dimensions(rightPanelX + 10, buttonY, buttonWidth, 20).build();

            addDrawableChild(claimVillagerButton);
        } else if (isOwnedByPlayer) {
            // Show "Release Ownership" button for villagers owned by the player
            releaseOwnershipButton = ButtonWidget.builder(
                Text.literal("Release Ownership").styled(style -> style.withColor(0xFF4444)),
                button -> showReleaseConfirmation()
            ).dimensions(rightPanelX + 10, buttonY, buttonWidth, 20).build();

            addDrawableChild(releaseOwnershipButton);
        }
        // If owned by another player, no buttons shown (just display owner badge in render)
    }

    /**
     * Handles the "Claim Villager" button click.
     *
     * <p>Sends a {@link BindVillagerPacket} to the server to request ownership.</p>
     */
    private void handleClaimVillager() {
        XeenaaVillagerManager.LOGGER.info("Player requesting to claim villager {}", targetVillager.getUuid());

        BindVillagerPacket packet = new BindVillagerPacket(targetVillager.getId());
        ClientPlayNetworking.send(packet);

        // Refresh screen after short delay to reflect changes
        MinecraftClient.getInstance().execute(() -> {
            new Thread(() -> {
                try {
                    Thread.sleep(300);
                    MinecraftClient.getInstance().execute(() -> {
                        this.clearAndInit();
                    });
                } catch (InterruptedException e) {
                    // Ignore interruption
                }
            }).start();
        });
    }

    /**
     * Shows the release ownership confirmation dialog.
     */
    private void showReleaseConfirmation() {
        XeenaaVillagerManager.LOGGER.info("Showing release ownership confirmation dialog");
        this.showingReleaseConfirmDialog = true;
        initReleaseConfirmDialog();
    }

    /**
     * Handles the confirmed release of villager ownership.
     *
     * <p>Sends an {@link UnbindVillagerPacket} to the server to release ownership.</p>
     */
    private void handleReleaseOwnership() {
        XeenaaVillagerManager.LOGGER.info("Player releasing ownership of villager {}", targetVillager.getUuid());

        UnbindVillagerPacket packet = new UnbindVillagerPacket(targetVillager.getId());
        ClientPlayNetworking.send(packet);

        closeReleaseConfirmDialog();

        // Refresh screen after short delay to reflect changes
        MinecraftClient.getInstance().execute(() -> {
            new Thread(() -> {
                try {
                    Thread.sleep(300);
                    MinecraftClient.getInstance().execute(() -> {
                        this.clearAndInit();
                    });
                } catch (InterruptedException e) {
                    // Ignore interruption
                }
            }).start();
        });
    }

    /**
     * Initializes the release ownership confirmation dialog buttons.
     */
    private void initReleaseConfirmDialog() {
        int dialogWidth = 300;
        int dialogHeight = 120;
        int dialogX = (this.width - dialogWidth) / 2;
        int dialogY = (this.height - dialogHeight) / 2;

        int buttonWidth = 80;
        int buttonHeight = 20;
        int buttonY = dialogY + 80;

        // Confirm button (left side)
        confirmButton = ButtonWidget.builder(Text.literal("Confirm"), button -> {
            XeenaaVillagerManager.LOGGER.info("User confirmed release ownership");
            handleReleaseOwnership();
        })
        .dimensions(dialogX + 50, buttonY, buttonWidth, buttonHeight)
        .build();

        // Cancel button (right side)
        cancelButton = ButtonWidget.builder(Text.literal("Cancel"), button -> {
            XeenaaVillagerManager.LOGGER.info("User cancelled release ownership");
            closeReleaseConfirmDialog();
        })
        .dimensions(dialogX + 170, buttonY, buttonWidth, buttonHeight)
        .build();
    }

    /**
     * Closes the release ownership confirmation dialog.
     */
    private void closeReleaseConfirmDialog() {
        this.showingReleaseConfirmDialog = false;
        confirmButton = null;
        cancelButton = null;
        XeenaaVillagerManager.LOGGER.debug("Release confirmation dialog closed");
    }

    /**
     * Renders the release ownership confirmation dialog.
     */
    private void renderReleaseConfirmationDialog(DrawContext context, int mouseX, int mouseY, float delta) {
        // 1. Draw semi-transparent overlay
        context.fill(0, 0, this.width, this.height, 0xCC000000);

        // 2. Calculate dialog position (centered)
        int dialogWidth = 300;
        int dialogHeight = 120;
        int dialogX = (this.width - dialogWidth) / 2;
        int dialogY = (this.height - dialogHeight) / 2;

        // 3. Draw dialog background and border
        context.fill(dialogX, dialogY, dialogX + dialogWidth, dialogY + dialogHeight, 0xFF2C2C2C);
        context.drawBorder(dialogX, dialogY, dialogWidth, dialogHeight, 0xFFFFFFFF);

        // 4. Draw title
        Text title = Text.literal("Release Ownership?").styled(style -> style.withBold(true));
        int titleWidth = textRenderer.getWidth(title);
        context.drawText(textRenderer, title,
            dialogX + (dialogWidth - titleWidth) / 2, dialogY + 10, 0xFFFFFF, true);

        // 5. Draw warning message
        Text warning = Text.literal("Are you sure you want to release");
        Text warning2 = Text.literal("ownership of this villager?");
        context.drawText(textRenderer, warning, dialogX + 20, dialogY + 35, 0xFFAA00, false);
        context.drawText(textRenderer, warning2, dialogX + 20, dialogY + 50, 0xFFAA00, false);

        // 6. Render buttons
        if (confirmButton != null) {
            confirmButton.render(context, mouseX, mouseY, delta);
        }
        if (cancelButton != null) {
            cancelButton.render(context, mouseX, mouseY, delta);
        }
    }

    /**
     * Renders the owner badge in the right panel header.
     *
     * <p>Displays owner information:</p>
     * <ul>
     *   <li>Owner name with player head icon (if owned)</li>
     *   <li>"Unowned" text (if not owned)</li>
     * </ul>
     */
    private void renderOwnerBadge(DrawContext context) {
        ClientOwnershipCache ownershipCache = ClientOwnershipCache.getInstance();
        UUID villagerUUID = targetVillager.getUuid();

        String ownerName = ownershipCache.getOwnerName(villagerUUID);
        boolean isOwned = ownerName != null;

        int badgeX = rightPanelX + RIGHT_PANEL_WIDTH - 150;
        int badgeY = rightPanelY + 10;

        if (isOwned) {
            // Draw owner badge with name
            Text ownerText = Text.literal("Owner: " + ownerName).styled(style -> style.withColor(0xFFD700));
            context.drawText(textRenderer, ownerText, badgeX, badgeY, 0xFFD700, true);
        } else {
            // Draw "Unowned" badge
            Text unownedText = Text.literal("Unowned").styled(style -> style.withColor(0xAAAAAA));
            context.drawText(textRenderer, unownedText, badgeX, badgeY, 0xAAAAAA, true);
        }
    }
}
