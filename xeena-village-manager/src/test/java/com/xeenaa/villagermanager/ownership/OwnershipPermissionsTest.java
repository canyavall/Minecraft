package com.xeenaa.villagermanager.ownership;

import com.xeenaa.villagermanager.MinecraftTestBase;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link OwnershipPermissions}.
 */
class OwnershipPermissionsTest extends MinecraftTestBase {

    private OwnershipPermissions permissions;
    private UUID ownerUUID;
    private UUID player1UUID;
    private UUID player2UUID;
    private ServerPlayerEntity mockOwner;
    private ServerPlayerEntity mockPlayer1;
    private ServerPlayerEntity mockPlayer2;

    @BeforeEach
    void setUp() {
        permissions = new OwnershipPermissions();
        ownerUUID = UUID.randomUUID();
        player1UUID = UUID.randomUUID();
        player2UUID = UUID.randomUUID();

        mockOwner = mock(ServerPlayerEntity.class);
        when(mockOwner.getUuid()).thenReturn(ownerUUID);

        mockPlayer1 = mock(ServerPlayerEntity.class);
        when(mockPlayer1.getUuid()).thenReturn(player1UUID);

        mockPlayer2 = mock(ServerPlayerEntity.class);
        when(mockPlayer2.getUuid()).thenReturn(player2UUID);
    }

    @Test
    @DisplayName("Default constructor creates EVERYONE permissions")
    void testDefaultConstructor() {
        assertEquals(OwnershipPermissions.TradePermission.EVERYONE, permissions.getTradePermission());
        assertFalse(permissions.isAllowOthersToManage());
        assertTrue(permissions.getWhitelist().isEmpty());
    }

    @Test
    @DisplayName("Parameterized constructor sets values correctly")
    void testParameterizedConstructor() {
        OwnershipPermissions custom = new OwnershipPermissions(
            OwnershipPermissions.TradePermission.OWNER_ONLY, true);

        assertEquals(OwnershipPermissions.TradePermission.OWNER_ONLY, custom.getTradePermission());
        assertTrue(custom.isAllowOthersToManage());
    }

    @Test
    @DisplayName("EVERYONE mode allows all players to trade")
    void testEveryoneModeAllowsAllPlayers() {
        permissions.setTradePermission(OwnershipPermissions.TradePermission.EVERYONE);

        assertTrue(permissions.canTrade(mockOwner, ownerUUID), "Owner should be able to trade");
        assertTrue(permissions.canTrade(mockPlayer1, ownerUUID), "Player1 should be able to trade");
        assertTrue(permissions.canTrade(mockPlayer2, ownerUUID), "Player2 should be able to trade");
    }

    @Test
    @DisplayName("OWNER_ONLY mode restricts trade to owner")
    void testOwnerOnlyModeRestrictsTrade() {
        permissions.setTradePermission(OwnershipPermissions.TradePermission.OWNER_ONLY);

        assertTrue(permissions.canTrade(mockOwner, ownerUUID), "Owner should be able to trade");
        assertFalse(permissions.canTrade(mockPlayer1, ownerUUID), "Player1 should not be able to trade");
        assertFalse(permissions.canTrade(mockPlayer2, ownerUUID), "Player2 should not be able to trade");
    }

    @Test
    @DisplayName("WHITELIST mode allows owner and whitelisted players")
    void testWhitelistModeAllowsWhitelistedPlayers() {
        permissions.setTradePermission(OwnershipPermissions.TradePermission.WHITELIST);
        permissions.addToWhitelist(player1UUID);

        assertTrue(permissions.canTrade(mockOwner, ownerUUID), "Owner should be able to trade");
        assertTrue(permissions.canTrade(mockPlayer1, ownerUUID), "Whitelisted player should be able to trade");
        assertFalse(permissions.canTrade(mockPlayer2, ownerUUID), "Non-whitelisted player should not be able to trade");
    }

    @Test
    @DisplayName("Add and remove from whitelist works correctly")
    void testWhitelistManagement() {
        assertTrue(permissions.getWhitelist().isEmpty(), "Whitelist should start empty");

        permissions.addToWhitelist(player1UUID);
        assertTrue(permissions.isWhitelisted(player1UUID), "Player1 should be whitelisted");
        assertEquals(1, permissions.getWhitelist().size());

        permissions.addToWhitelist(player2UUID);
        assertTrue(permissions.isWhitelisted(player2UUID), "Player2 should be whitelisted");
        assertEquals(2, permissions.getWhitelist().size());

        permissions.removeFromWhitelist(player1UUID);
        assertFalse(permissions.isWhitelisted(player1UUID), "Player1 should no longer be whitelisted");
        assertTrue(permissions.isWhitelisted(player2UUID), "Player2 should still be whitelisted");
        assertEquals(1, permissions.getWhitelist().size());
    }

    @Test
    @DisplayName("Adding duplicate to whitelist has no effect")
    void testAddDuplicateToWhitelist() {
        permissions.addToWhitelist(player1UUID);
        permissions.addToWhitelist(player1UUID);

        assertEquals(1, permissions.getWhitelist().size(), "Duplicate should not be added");
    }

    @Test
    @DisplayName("Clear whitelist removes all players")
    void testClearWhitelist() {
        permissions.addToWhitelist(player1UUID);
        permissions.addToWhitelist(player2UUID);
        assertEquals(2, permissions.getWhitelist().size());

        permissions.clearWhitelist();
        assertTrue(permissions.getWhitelist().isEmpty(), "Whitelist should be empty after clear");
    }

    @Test
    @DisplayName("Get whitelist returns immutable copy")
    void testGetWhitelistReturnsImmutableCopy() {
        permissions.addToWhitelist(player1UUID);

        Set<UUID> whitelist = permissions.getWhitelist();
        whitelist.add(player2UUID);

        assertFalse(permissions.isWhitelisted(player2UUID),
            "Modifying returned set should not affect internal whitelist");
    }

    @Test
    @DisplayName("Trade permission enum from string works")
    void testTradePermissionFromString() {
        assertEquals(OwnershipPermissions.TradePermission.EVERYONE,
            OwnershipPermissions.TradePermission.fromString("EVERYONE"));
        assertEquals(OwnershipPermissions.TradePermission.OWNER_ONLY,
            OwnershipPermissions.TradePermission.fromString("owner_only"));
        assertEquals(OwnershipPermissions.TradePermission.WHITELIST,
            OwnershipPermissions.TradePermission.fromString("WhItElIsT"));
        assertEquals(OwnershipPermissions.TradePermission.EVERYONE,
            OwnershipPermissions.TradePermission.fromString("invalid"));
        assertEquals(OwnershipPermissions.TradePermission.EVERYONE,
            OwnershipPermissions.TradePermission.fromString(null));
    }

    @Test
    @DisplayName("NBT serialization round-trip preserves data")
    void testNbtSerializationRoundTrip() {
        permissions.setTradePermission(OwnershipPermissions.TradePermission.WHITELIST);
        permissions.addToWhitelist(player1UUID);
        permissions.addToWhitelist(player2UUID);
        permissions.setAllowOthersToManage(true);

        NbtCompound nbt = permissions.toNbt();
        OwnershipPermissions deserialized = OwnershipPermissions.fromNbt(nbt);

        assertEquals(permissions.getTradePermission(), deserialized.getTradePermission());
        assertEquals(permissions.getWhitelist(), deserialized.getWhitelist());
        assertEquals(permissions.isAllowOthersToManage(), deserialized.isAllowOthersToManage());
    }

    @Test
    @DisplayName("NBT serialization with empty whitelist works")
    void testNbtSerializationEmptyWhitelist() {
        permissions.setTradePermission(OwnershipPermissions.TradePermission.OWNER_ONLY);

        NbtCompound nbt = permissions.toNbt();
        OwnershipPermissions deserialized = OwnershipPermissions.fromNbt(nbt);

        assertEquals(OwnershipPermissions.TradePermission.OWNER_ONLY, deserialized.getTradePermission());
        assertTrue(deserialized.getWhitelist().isEmpty());
    }

    @Test
    @DisplayName("NBT deserialization handles missing fields gracefully")
    void testNbtDeserializationMissingFields() {
        NbtCompound nbt = new NbtCompound();

        OwnershipPermissions deserialized = OwnershipPermissions.fromNbt(nbt);

        assertEquals(OwnershipPermissions.TradePermission.EVERYONE, deserialized.getTradePermission());
        assertTrue(deserialized.getWhitelist().isEmpty());
        assertFalse(deserialized.isAllowOthersToManage());
    }

    @Test
    @DisplayName("canTrade throws on null player")
    void testCanTradeThrowsOnNullPlayer() {
        assertThrows(IllegalArgumentException.class, () -> {
            permissions.canTrade(null, ownerUUID);
        });
    }

    @Test
    @DisplayName("canTrade throws on null owner UUID")
    void testCanTradeThrowsOnNullOwnerUUID() {
        assertThrows(IllegalArgumentException.class, () -> {
            permissions.canTrade(mockPlayer1, null);
        });
    }

    @Test
    @DisplayName("setTradePermission throws on null")
    void testSetTradePermissionThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            permissions.setTradePermission(null);
        });
    }

    @Test
    @DisplayName("addToWhitelist throws on null")
    void testAddToWhitelistThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            permissions.addToWhitelist(null);
        });
    }

    @Test
    @DisplayName("removeFromWhitelist throws on null")
    void testRemoveFromWhitelistThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            permissions.removeFromWhitelist(null);
        });
    }

    @Test
    @DisplayName("fromNbt throws on null NBT")
    void testFromNbtThrowsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            OwnershipPermissions.fromNbt(null);
        });
    }

    @Test
    @DisplayName("toString returns valid string representation")
    void testToString() {
        permissions.setTradePermission(OwnershipPermissions.TradePermission.WHITELIST);
        permissions.addToWhitelist(player1UUID);

        String str = permissions.toString();
        assertNotNull(str);
        assertTrue(str.contains("WHITELIST"));
        assertTrue(str.contains("whitelistSize=1"));
    }
}
