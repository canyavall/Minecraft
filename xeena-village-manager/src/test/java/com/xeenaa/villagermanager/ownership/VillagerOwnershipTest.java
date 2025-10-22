package com.xeenaa.villagermanager.ownership;

import com.xeenaa.villagermanager.MinecraftTestBase;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link VillagerOwnership}.
 */
class VillagerOwnershipTest extends MinecraftTestBase {

    private UUID villagerUUID;
    private UUID ownerUUID;
    private UUID otherPlayerUUID;
    private ServerPlayerEntity mockOwner;
    private ServerPlayerEntity mockOtherPlayer;
    private VillagerOwnership ownership;

    @BeforeEach
    void setUp() {
        villagerUUID = UUID.randomUUID();
        ownerUUID = UUID.randomUUID();
        otherPlayerUUID = UUID.randomUUID();

        mockOwner = mock(ServerPlayerEntity.class);
        when(mockOwner.getUuid()).thenReturn(ownerUUID);
        when(mockOwner.getName()).thenReturn(Text.literal("TestOwner"));

        mockOtherPlayer = mock(ServerPlayerEntity.class);
        when(mockOtherPlayer.getUuid()).thenReturn(otherPlayerUUID);
        when(mockOtherPlayer.getName()).thenReturn(Text.literal("OtherPlayer"));

        ownership = new VillagerOwnership(villagerUUID, mockOwner);
    }

    @Test
    @DisplayName("Constructor initializes all fields correctly")
    void testConstructorInitialization() {
        assertEquals(villagerUUID, ownership.getVillagerUUID());
        assertEquals(ownerUUID, ownership.getOwnerUUID());
        assertEquals("TestOwner", ownership.getOwnerName());
        assertFalse(ownership.isLocked());
        assertNotNull(ownership.getPermissions());
        assertTrue(ownership.getBindTimestamp() > 0);
        assertTrue(ownership.getBindTimestamp() <= System.currentTimeMillis());
    }

    @Test
    @DisplayName("Constructor throws on null villager UUID")
    void testConstructorThrowsOnNullVillagerUUID() {
        assertThrows(IllegalArgumentException.class, () -> {
            new VillagerOwnership(null, mockOwner);
        });
    }

    @Test
    @DisplayName("Constructor throws on null owner")
    void testConstructorThrowsOnNullOwner() {
        assertThrows(IllegalArgumentException.class, () -> {
            new VillagerOwnership(villagerUUID, null);
        });
    }

    @Test
    @DisplayName("isOwner returns true for owner")
    void testIsOwnerReturnsTrueForOwner() {
        assertTrue(ownership.isOwner(mockOwner));
    }

    @Test
    @DisplayName("isOwner returns false for other player")
    void testIsOwnerReturnsFalseForOtherPlayer() {
        assertFalse(ownership.isOwner(mockOtherPlayer));
    }

    @Test
    @DisplayName("isOwner throws on null player")
    void testIsOwnerThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            ownership.isOwner(null);
        });
    }

    @Test
    @DisplayName("canInteract allows owner regardless of lock status")
    void testCanInteractAllowsOwnerWhenLocked() {
        ownership.setLocked(true);
        assertTrue(ownership.canInteract(mockOwner), "Owner should interact even when locked");

        ownership.setLocked(false);
        assertTrue(ownership.canInteract(mockOwner), "Owner should interact when unlocked");
    }

    @Test
    @DisplayName("canInteract allows others when unlocked")
    void testCanInteractAllowsOthersWhenUnlocked() {
        ownership.setLocked(false);
        assertTrue(ownership.canInteract(mockOtherPlayer), "Others should interact when unlocked");
    }

    @Test
    @DisplayName("canInteract denies others when locked")
    void testCanInteractDeniesOthersWhenLocked() {
        ownership.setLocked(true);
        assertFalse(ownership.canInteract(mockOtherPlayer), "Others should not interact when locked");
    }

    @Test
    @DisplayName("canInteract throws on null player")
    void testCanInteractThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            ownership.canInteract(null);
        });
    }

    @Test
    @DisplayName("Lock status can be changed")
    void testLockStatusChange() {
        assertFalse(ownership.isLocked(), "Should start unlocked");

        ownership.setLocked(true);
        assertTrue(ownership.isLocked(), "Should be locked after setLocked(true)");

        ownership.setLocked(false);
        assertFalse(ownership.isLocked(), "Should be unlocked after setLocked(false)");
    }

    @Test
    @DisplayName("Permissions can be changed")
    void testPermissionsChange() {
        OwnershipPermissions newPermissions = new OwnershipPermissions(
            OwnershipPermissions.TradePermission.OWNER_ONLY, false);

        ownership.setPermissions(newPermissions);
        assertEquals(newPermissions, ownership.getPermissions());
    }

    @Test
    @DisplayName("setPermissions throws on null")
    void testSetPermissionsThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            ownership.setPermissions(null);
        });
    }

    @Test
    @DisplayName("NBT serialization round-trip preserves all data")
    void testNbtSerializationRoundTrip() {
        ownership.setLocked(true);
        ownership.getPermissions().setTradePermission(OwnershipPermissions.TradePermission.WHITELIST);
        ownership.getPermissions().addToWhitelist(otherPlayerUUID);

        NbtCompound nbt = ownership.toNbt();
        VillagerOwnership deserialized = VillagerOwnership.fromNbt(nbt);

        assertEquals(ownership.getVillagerUUID(), deserialized.getVillagerUUID());
        assertEquals(ownership.getOwnerUUID(), deserialized.getOwnerUUID());
        assertEquals(ownership.getOwnerName(), deserialized.getOwnerName());
        assertEquals(ownership.getBindTimestamp(), deserialized.getBindTimestamp());
        assertEquals(ownership.isLocked(), deserialized.isLocked());
        assertEquals(ownership.getPermissions().getTradePermission(),
            deserialized.getPermissions().getTradePermission());
        assertEquals(ownership.getPermissions().getWhitelist(),
            deserialized.getPermissions().getWhitelist());
    }

    @Test
    @DisplayName("NBT serialization with default values works")
    void testNbtSerializationDefaultValues() {
        NbtCompound nbt = ownership.toNbt();
        VillagerOwnership deserialized = VillagerOwnership.fromNbt(nbt);

        assertFalse(deserialized.isLocked());
        assertEquals(OwnershipPermissions.TradePermission.EVERYONE,
            deserialized.getPermissions().getTradePermission());
    }

    @Test
    @DisplayName("fromNbt throws on null NBT")
    void testFromNbtThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            VillagerOwnership.fromNbt(null);
        });
    }

    @Test
    @DisplayName("fromNbt throws on missing villager UUID")
    void testFromNbtThrowsOnMissingVillagerUUID() {
        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("OwnerUUID", ownerUUID);
        nbt.putString("OwnerName", "TestOwner");
        nbt.putLong("BindTimestamp", System.currentTimeMillis());

        assertThrows(IllegalArgumentException.class, () -> {
            VillagerOwnership.fromNbt(nbt);
        });
    }

    @Test
    @DisplayName("fromNbt throws on missing owner UUID")
    void testFromNbtThrowsOnMissingOwnerUUID() {
        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("VillagerUUID", villagerUUID);
        nbt.putString("OwnerName", "TestOwner");
        nbt.putLong("BindTimestamp", System.currentTimeMillis());

        assertThrows(IllegalArgumentException.class, () -> {
            VillagerOwnership.fromNbt(nbt);
        });
    }

    @Test
    @DisplayName("fromNbt throws on missing owner name")
    void testFromNbtThrowsOnMissingOwnerName() {
        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("VillagerUUID", villagerUUID);
        nbt.putUuid("OwnerUUID", ownerUUID);
        nbt.putLong("BindTimestamp", System.currentTimeMillis());

        assertThrows(IllegalArgumentException.class, () -> {
            VillagerOwnership.fromNbt(nbt);
        });
    }

    @Test
    @DisplayName("fromNbt throws on missing bind timestamp")
    void testFromNbtThrowsOnMissingBindTimestamp() {
        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("VillagerUUID", villagerUUID);
        nbt.putUuid("OwnerUUID", ownerUUID);
        nbt.putString("OwnerName", "TestOwner");

        assertThrows(IllegalArgumentException.class, () -> {
            VillagerOwnership.fromNbt(nbt);
        });
    }

    @Test
    @DisplayName("fromNbt uses default permissions if missing")
    void testFromNbtUsesDefaultPermissionsIfMissing() {
        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("VillagerUUID", villagerUUID);
        nbt.putUuid("OwnerUUID", ownerUUID);
        nbt.putString("OwnerName", "TestOwner");
        nbt.putLong("BindTimestamp", System.currentTimeMillis());
        nbt.putBoolean("Locked", false);

        VillagerOwnership deserialized = VillagerOwnership.fromNbt(nbt);

        assertNotNull(deserialized.getPermissions());
        assertEquals(OwnershipPermissions.TradePermission.EVERYONE,
            deserialized.getPermissions().getTradePermission());
    }

    @Test
    @DisplayName("fromNbt handles newer version gracefully")
    void testFromNbtHandlesNewerVersion() {
        NbtCompound nbt = ownership.toNbt();
        nbt.putInt("DataVersion", 999);

        assertDoesNotThrow(() -> {
            VillagerOwnership.fromNbt(nbt);
        });
    }

    @Test
    @DisplayName("toString returns valid string representation")
    void testToString() {
        String str = ownership.toString();
        assertNotNull(str);
        assertTrue(str.contains(villagerUUID.toString()));
        assertTrue(str.contains(ownerUUID.toString()));
        assertTrue(str.contains("TestOwner"));
    }

    @Test
    @DisplayName("Bind timestamp is close to current time")
    void testBindTimestampIsRecent() {
        long now = System.currentTimeMillis();
        long bindTime = ownership.getBindTimestamp();

        assertTrue(bindTime <= now, "Bind timestamp should not be in the future");
        assertTrue(bindTime >= now - 1000, "Bind timestamp should be within last second");
    }

    @Test
    @DisplayName("Multiple ownerships have different timestamps")
    void testMultipleOwnershipsHaveDifferentTimestamps() throws InterruptedException {
        VillagerOwnership ownership1 = new VillagerOwnership(UUID.randomUUID(), mockOwner);
        Thread.sleep(10); // Small delay to ensure different timestamps
        VillagerOwnership ownership2 = new VillagerOwnership(UUID.randomUUID(), mockOwner);

        assertTrue(ownership2.getBindTimestamp() >= ownership1.getBindTimestamp(),
            "Later ownership should have equal or later timestamp");
    }
}
