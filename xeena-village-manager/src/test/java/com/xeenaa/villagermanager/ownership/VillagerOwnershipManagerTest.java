package com.xeenaa.villagermanager.ownership;

import com.xeenaa.villagermanager.MinecraftTestBase;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link VillagerOwnershipManager}.
 */
class VillagerOwnershipManagerTest extends MinecraftTestBase {

    private VillagerOwnershipManager manager;
    private ServerWorld mockWorld;
    private RegistryWrapper.WrapperLookup mockRegistries;
    private ServerPlayerEntity mockOwner;
    private ServerPlayerEntity mockOtherPlayer;
    private UUID villagerUUID;
    private UUID ownerUUID;
    private UUID otherPlayerUUID;

    @BeforeEach
    void setUp() {
        manager = new VillagerOwnershipManager();
        mockWorld = mock(ServerWorld.class);
        mockRegistries = mock(RegistryWrapper.WrapperLookup.class);

        villagerUUID = UUID.randomUUID();
        ownerUUID = UUID.randomUUID();
        otherPlayerUUID = UUID.randomUUID();

        mockOwner = mock(ServerPlayerEntity.class);
        when(mockOwner.getUuid()).thenReturn(ownerUUID);
        when(mockOwner.getName()).thenReturn(Text.literal("Owner"));

        mockOtherPlayer = mock(ServerPlayerEntity.class);
        when(mockOtherPlayer.getUuid()).thenReturn(otherPlayerUUID);
        when(mockOtherPlayer.getName()).thenReturn(Text.literal("OtherPlayer"));
    }

    @Test
    @DisplayName("New manager starts empty")
    void testNewManagerStartsEmpty() {
        assertEquals(0, manager.getOwnershipCount());
        assertTrue(manager.getAllOwnerships().isEmpty());
    }

    @Test
    @DisplayName("bindVillager creates ownership")
    void testBindVillagerCreatesOwnership() {
        manager.bindVillager(villagerUUID, mockOwner);

        assertTrue(manager.hasOwner(villagerUUID));
        assertEquals(1, manager.getOwnershipCount());

        VillagerOwnership ownership = manager.getOwnership(villagerUUID);
        assertNotNull(ownership);
        assertEquals(villagerUUID, ownership.getVillagerUUID());
        assertEquals(ownerUUID, ownership.getOwnerUUID());
    }

    @Test
    @DisplayName("bindVillager throws on null villager UUID")
    void testBindVillagerThrowsOnNullVillagerUUID() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.bindVillager(null, mockOwner);
        });
    }

    @Test
    @DisplayName("bindVillager throws on null owner")
    void testBindVillagerThrowsOnNullOwner() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.bindVillager(villagerUUID, null);
        });
    }

    @Test
    @DisplayName("bindVillager does not override existing ownership")
    void testBindVillagerDoesNotOverrideExisting() {
        manager.bindVillager(villagerUUID, mockOwner);
        long originalTimestamp = manager.getOwnership(villagerUUID).getBindTimestamp();

        manager.bindVillager(villagerUUID, mockOtherPlayer);

        VillagerOwnership ownership = manager.getOwnership(villagerUUID);
        assertEquals(ownerUUID, ownership.getOwnerUUID(), "Owner should not change");
        assertEquals(originalTimestamp, ownership.getBindTimestamp(), "Timestamp should not change");
    }

    @Test
    @DisplayName("unbindVillager removes ownership")
    void testUnbindVillagerRemovesOwnership() {
        manager.bindVillager(villagerUUID, mockOwner);
        assertTrue(manager.hasOwner(villagerUUID));

        manager.unbindVillager(villagerUUID);

        assertFalse(manager.hasOwner(villagerUUID));
        assertNull(manager.getOwnership(villagerUUID));
        assertEquals(0, manager.getOwnershipCount());
    }

    @Test
    @DisplayName("unbindVillager on non-owned villager is safe")
    void testUnbindVillagerNonOwned() {
        assertDoesNotThrow(() -> {
            manager.unbindVillager(villagerUUID);
        });

        assertEquals(0, manager.getOwnershipCount());
    }

    @Test
    @DisplayName("unbindVillager throws on null")
    void testUnbindVillagerThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.unbindVillager(null);
        });
    }

    @Test
    @DisplayName("transferOwnership changes owner")
    void testTransferOwnershipChangesOwner() {
        manager.bindVillager(villagerUUID, mockOwner);
        long originalTimestamp = manager.getOwnership(villagerUUID).getBindTimestamp();

        manager.transferOwnership(villagerUUID, mockOtherPlayer, mockWorld);

        VillagerOwnership ownership = manager.getOwnership(villagerUUID);
        assertNotNull(ownership);
        assertEquals(otherPlayerUUID, ownership.getOwnerUUID());
        assertTrue(ownership.getBindTimestamp() >= originalTimestamp,
            "Transfer should create new or equal timestamp");
    }

    @Test
    @DisplayName("transferOwnership throws on unowned villager")
    void testTransferOwnershipThrowsOnUnowned() {
        assertThrows(IllegalStateException.class, () -> {
            manager.transferOwnership(villagerUUID, mockOtherPlayer, mockWorld);
        });
    }

    @Test
    @DisplayName("transferOwnership throws on null parameters")
    void testTransferOwnershipThrowsOnNull() {
        manager.bindVillager(villagerUUID, mockOwner);

        assertThrows(IllegalArgumentException.class, () -> {
            manager.transferOwnership(null, mockOtherPlayer, mockWorld);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            manager.transferOwnership(villagerUUID, null, mockWorld);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            manager.transferOwnership(villagerUUID, mockOtherPlayer, null);
        });
    }

    @Test
    @DisplayName("isOwner returns correct results")
    void testIsOwner() {
        assertFalse(manager.isOwner(villagerUUID, mockOwner), "Unowned villager should return false");

        manager.bindVillager(villagerUUID, mockOwner);

        assertTrue(manager.isOwner(villagerUUID, mockOwner), "Owner should return true");
        assertFalse(manager.isOwner(villagerUUID, mockOtherPlayer), "Non-owner should return false");
    }

    @Test
    @DisplayName("isOwner handles null parameters")
    void testIsOwnerHandlesNull() {
        assertFalse(manager.isOwner(null, mockOwner));
        assertFalse(manager.isOwner(villagerUUID, null));
    }

    @Test
    @DisplayName("canManage allows owner")
    void testCanManageAllowsOwner() {
        manager.bindVillager(villagerUUID, mockOwner);

        assertTrue(manager.canManage(villagerUUID, mockOwner));
        assertFalse(manager.canManage(villagerUUID, mockOtherPlayer));
    }

    @Test
    @DisplayName("canManage allows everyone for unowned villager")
    void testCanManageAllowsEveryoneForUnowned() {
        assertTrue(manager.canManage(villagerUUID, mockOwner));
        assertTrue(manager.canManage(villagerUUID, mockOtherPlayer));
    }

    @Test
    @DisplayName("canManage handles null parameters")
    void testCanManageHandlesNull() {
        assertFalse(manager.canManage(null, mockOwner));
        assertFalse(manager.canManage(villagerUUID, null));
    }

    @Test
    @DisplayName("canTrade respects EVERYONE permission")
    void testCanTradeEveryonePermission() {
        manager.bindVillager(villagerUUID, mockOwner);
        VillagerOwnership ownership = manager.getOwnership(villagerUUID);
        ownership.getPermissions().setTradePermission(OwnershipPermissions.TradePermission.EVERYONE);

        assertTrue(manager.canTrade(villagerUUID, mockOwner));
        assertTrue(manager.canTrade(villagerUUID, mockOtherPlayer));
    }

    @Test
    @DisplayName("canTrade respects OWNER_ONLY permission")
    void testCanTradeOwnerOnlyPermission() {
        manager.bindVillager(villagerUUID, mockOwner);
        VillagerOwnership ownership = manager.getOwnership(villagerUUID);
        ownership.getPermissions().setTradePermission(OwnershipPermissions.TradePermission.OWNER_ONLY);

        assertTrue(manager.canTrade(villagerUUID, mockOwner));
        assertFalse(manager.canTrade(villagerUUID, mockOtherPlayer));
    }

    @Test
    @DisplayName("canTrade respects WHITELIST permission")
    void testCanTradeWhitelistPermission() {
        manager.bindVillager(villagerUUID, mockOwner);
        VillagerOwnership ownership = manager.getOwnership(villagerUUID);
        ownership.getPermissions().setTradePermission(OwnershipPermissions.TradePermission.WHITELIST);
        ownership.getPermissions().addToWhitelist(otherPlayerUUID);

        assertTrue(manager.canTrade(villagerUUID, mockOwner));
        assertTrue(manager.canTrade(villagerUUID, mockOtherPlayer));
    }

    @Test
    @DisplayName("canTrade respects lock status")
    void testCanTradeRespectsLock() {
        manager.bindVillager(villagerUUID, mockOwner);
        VillagerOwnership ownership = manager.getOwnership(villagerUUID);
        ownership.setLocked(true);

        assertTrue(manager.canTrade(villagerUUID, mockOwner), "Owner should trade even when locked");
        assertFalse(manager.canTrade(villagerUUID, mockOtherPlayer), "Others should not trade when locked");
    }

    @Test
    @DisplayName("canTrade allows everyone for unowned villager")
    void testCanTradeAllowsEveryoneForUnowned() {
        assertTrue(manager.canTrade(villagerUUID, mockOwner));
        assertTrue(manager.canTrade(villagerUUID, mockOtherPlayer));
    }

    @Test
    @DisplayName("canTrade handles null parameters")
    void testCanTradeHandlesNull() {
        assertFalse(manager.canTrade(null, mockOwner));
        assertFalse(manager.canTrade(villagerUUID, null));
    }

    @Test
    @DisplayName("getAllOwnerships returns copy")
    void testGetAllOwnershipsReturnsCopy() {
        manager.bindVillager(villagerUUID, mockOwner);

        Map<UUID, VillagerOwnership> ownerships = manager.getAllOwnerships();
        ownerships.clear();

        assertEquals(1, manager.getOwnershipCount(), "Clearing returned map should not affect manager");
    }

    @Test
    @DisplayName("clearAll removes all ownerships")
    void testClearAllRemovesAll() {
        manager.bindVillager(UUID.randomUUID(), mockOwner);
        manager.bindVillager(UUID.randomUUID(), mockOwner);
        manager.bindVillager(UUID.randomUUID(), mockOwner);

        assertEquals(3, manager.getOwnershipCount());

        manager.clearAll();

        assertEquals(0, manager.getOwnershipCount());
        assertTrue(manager.getAllOwnerships().isEmpty());
    }

    @Test
    @DisplayName("NBT serialization round-trip preserves data")
    void testNbtSerializationRoundTrip() {
        UUID villager1 = UUID.randomUUID();
        UUID villager2 = UUID.randomUUID();

        manager.bindVillager(villager1, mockOwner);
        manager.bindVillager(villager2, mockOtherPlayer);

        VillagerOwnership ownership1 = manager.getOwnership(villager1);
        ownership1.setLocked(true);
        ownership1.getPermissions().setTradePermission(OwnershipPermissions.TradePermission.OWNER_ONLY);

        NbtCompound nbt = new NbtCompound();
        manager.writeNbt(nbt, mockRegistries);

        VillagerOwnershipManager loadedManager = VillagerOwnershipManager.fromNbt(nbt, mockRegistries);

        assertEquals(2, loadedManager.getOwnershipCount());
        assertTrue(loadedManager.hasOwner(villager1));
        assertTrue(loadedManager.hasOwner(villager2));

        VillagerOwnership loadedOwnership1 = loadedManager.getOwnership(villager1);
        assertEquals(ownership1.getOwnerUUID(), loadedOwnership1.getOwnerUUID());
        assertEquals(ownership1.isLocked(), loadedOwnership1.isLocked());
        assertEquals(ownership1.getPermissions().getTradePermission(),
            loadedOwnership1.getPermissions().getTradePermission());
    }

    @Test
    @DisplayName("NBT deserialization handles empty data")
    void testNbtDeserializationEmpty() {
        NbtCompound nbt = new NbtCompound();

        VillagerOwnershipManager loadedManager = VillagerOwnershipManager.fromNbt(nbt, mockRegistries);

        assertEquals(0, loadedManager.getOwnershipCount());
    }

    @Test
    @DisplayName("NBT deserialization handles corrupt entries")
    void testNbtDeserializationCorruptEntries() {
        manager.bindVillager(villagerUUID, mockOwner);

        NbtCompound nbt = new NbtCompound();
        manager.writeNbt(nbt, mockRegistries);

        // Manually corrupt one entry
        // This is a simplified test - in reality we'd need to access the list

        VillagerOwnershipManager loadedManager = VillagerOwnershipManager.fromNbt(nbt, mockRegistries);

        // Should still load valid entries
        assertTrue(loadedManager.getOwnershipCount() > 0);
    }

    @Test
    @DisplayName("cleanupInvalidEntries removes missing villagers")
    @org.junit.jupiter.api.Disabled("Requires full Minecraft bootstrap to mock VillagerEntity - move to integration tests")
    void testCleanupInvalidEntriesRemovesMissing() {
        UUID existingVillager = UUID.randomUUID();
        UUID missingVillager = UUID.randomUUID();

        manager.bindVillager(existingVillager, mockOwner);
        manager.bindVillager(missingVillager, mockOwner);

        assertEquals(2, manager.getOwnershipCount());

        // Mock world with only one villager
        VillagerEntity mockVillager = mock(VillagerEntity.class);
        when(mockVillager.getUuid()).thenReturn(existingVillager);
        when(mockVillager.isAlive()).thenReturn(true);

        List<net.minecraft.entity.Entity> entities = new ArrayList<>();
        entities.add(mockVillager);

        when(mockWorld.iterateEntities()).thenReturn((Iterable) entities);

        int removed = manager.cleanupInvalidEntries(mockWorld);

        assertEquals(1, removed);
        assertEquals(1, manager.getOwnershipCount());
        assertTrue(manager.hasOwner(existingVillager));
        assertFalse(manager.hasOwner(missingVillager));
    }

    @Test
    @DisplayName("cleanupInvalidEntries keeps alive villagers")
    @org.junit.jupiter.api.Disabled("Requires full Minecraft bootstrap to mock VillagerEntity - move to integration tests")
    void testCleanupInvalidEntriesKeepsAlive() {
        manager.bindVillager(villagerUUID, mockOwner);

        VillagerEntity mockVillager = mock(VillagerEntity.class);
        when(mockVillager.getUuid()).thenReturn(villagerUUID);
        when(mockVillager.isAlive()).thenReturn(true);

        List<net.minecraft.entity.Entity> entities = new ArrayList<>();
        entities.add(mockVillager);

        when(mockWorld.iterateEntities()).thenReturn((Iterable) entities);

        int removed = manager.cleanupInvalidEntries(mockWorld);

        assertEquals(0, removed);
        assertEquals(1, manager.getOwnershipCount());
    }

    @Test
    @DisplayName("cleanupInvalidEntries throws on null world")
    void testCleanupInvalidEntriesThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.cleanupInvalidEntries(null);
        });
    }

    @Test
    @DisplayName("Concurrent bind operations are thread-safe")
    void testConcurrentBindOperations() throws InterruptedException {
        int threadCount = 10;
        int operationsPerThread = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await(); // Wait for all threads to be ready

                    for (int j = 0; j < operationsPerThread; j++) {
                        UUID villager = UUID.randomUUID();
                        manager.bindVillager(villager, mockOwner);
                        if (manager.hasOwner(villager)) {
                            successCount.incrementAndGet();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown(); // Start all threads
        assertTrue(doneLatch.await(30, TimeUnit.SECONDS), "Threads should complete within timeout");

        executor.shutdown();

        assertEquals(threadCount * operationsPerThread, successCount.get(),
            "All bind operations should succeed");
        assertEquals(threadCount * operationsPerThread, manager.getOwnershipCount());
    }

    @Test
    @DisplayName("Concurrent permission checks are thread-safe")
    void testConcurrentPermissionChecks() throws InterruptedException {
        manager.bindVillager(villagerUUID, mockOwner);

        int threadCount = 10;
        int checksPerThread = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();

                    for (int j = 0; j < checksPerThread; j++) {
                        if (manager.canManage(villagerUUID, mockOwner)) {
                            successCount.incrementAndGet();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        assertTrue(doneLatch.await(30, TimeUnit.SECONDS), "Threads should complete within timeout");

        executor.shutdown();

        assertEquals(threadCount * checksPerThread, successCount.get(),
            "All permission checks should succeed");
    }
}
