package com.canya.xeenaa_structure_manager.command;

import com.canya.xeenaa_structure_manager.tracking.PlacementTracker;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides the /xeenaa command for viewing placement statistics.
 * <p>
 * This command triggers the detailed statistics logging to help verify
 * that structure spacing multipliers are working correctly.
 * <p>
 * <b>Command Usage:</b>
 * <ul>
 *   <li>/xeenaa stats - Logs detailed placement statistics to console</li>
 *   <li>/xeenaa clear - Clears all tracked placements</li>
 * </ul>
 * <p>
 * <b>Permission Level:</b> Requires OP level 2 (game master commands)
 * <p>
 * <b>Logging Output:</b>
 * The stats command outputs to the server log with detailed statistics including:
 * <ul>
 *   <li>Total placement counts per structure type</li>
 *   <li>Average spacing between placements (chunks and blocks)</li>
 *   <li>Minimum and maximum spacing</li>
 *   <li>Standard deviation of spacing</li>
 * </ul>
 *
 * @see PlacementTracker
 * @since 1.0.0
 */
public class XeenaaCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger("XeenaaStructureManager/Commands");

    /**
     * Registers the /xeenaa command with all subcommands.
     * <p>
     * This method should be called from the mod initializer during command registration.
     * <p>
     * Command tree:
     * <pre>
     * /xeenaa
     *   ├─ stats   - Log detailed placement statistics
     *   └─ clear   - Clear all tracked placements
     * </pre>
     *
     * @param dispatcher the command dispatcher to register with
     * @param registryAccess the registry access (unused, but required by API)
     * @param environment the command environment (unused, but required by API)
     */
    public static void register(
        CommandDispatcher<ServerCommandSource> dispatcher,
        net.minecraft.command.CommandRegistryAccess registryAccess,
        CommandManager.RegistrationEnvironment environment
    ) {
        dispatcher.register(
            CommandManager.literal("xeenaa")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("stats")
                    .executes(XeenaaCommand::executeStats))
                .then(CommandManager.literal("clear")
                    .executes(XeenaaCommand::executeClear))
        );

        LOGGER.info("Registered /xeenaa command");
    }

    /**
     * Executes the /xeenaa stats command.
     * <p>
     * This triggers the {@link PlacementTracker#logDetailedStatistics()} method,
     * which outputs comprehensive spacing analysis to the server log.
     * <p>
     * The player receives an in-game confirmation message, but all detailed
     * statistics are written to the log for analysis.
     *
     * @param context the command context
     * @return 1 for success (Brigadier convention)
     */
    private static int executeStats(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        PlacementTracker tracker = PlacementTracker.getInstance();

        // Get basic stats for in-game message
        int totalPlacements = tracker.getTotalPlacements();
        int trackedStructures = tracker.getTrackedStructures().size();

        // Log detailed statistics to console (parseable output)
        LOGGER.info("Player {} requested placement statistics", source.getName());
        tracker.logDetailedStatistics();

        // Send brief confirmation to player
        source.sendFeedback(
            () -> Text.literal(String.format(
                "§a[Xeenaa]§r Logged detailed statistics for %d placements across %d structure types. Check server logs for full analysis.",
                totalPlacements,
                trackedStructures
            )),
            false
        );

        return 1;
    }

    /**
     * Executes the /xeenaa clear command.
     * <p>
     * This clears all tracked placements from the {@link PlacementTracker}.
     * Useful for:
     * <ul>
     *   <li>Resetting tracking for a new test</li>
     *   <li>Freeing memory on long-running servers</li>
     *   <li>Starting fresh after changing multiplier configuration</li>
     * </ul>
     *
     * @param context the command context
     * @return 1 for success (Brigadier convention)
     */
    private static int executeClear(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        PlacementTracker tracker = PlacementTracker.getInstance();

        int previousCount = tracker.getTotalPlacements();
        tracker.clear();

        LOGGER.info("Player {} cleared placement tracking ({} records removed)", source.getName(), previousCount);

        source.sendFeedback(
            () -> Text.literal(String.format(
                "§a[Xeenaa]§r Cleared %d placement records. Tracking reset.",
                previousCount
            )),
            false
        );

        return 1;
    }
}
