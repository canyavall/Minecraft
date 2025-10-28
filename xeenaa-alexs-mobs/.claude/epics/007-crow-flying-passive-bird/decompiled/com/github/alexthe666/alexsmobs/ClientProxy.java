/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.BufferBuilder
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMap
 *  it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
 *  net.minecraft.client.CameraType
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.MenuScreens
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.particle.ParticleProvider
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderers
 *  net.minecraft.client.renderer.entity.EntityRenderers
 *  net.minecraft.client.renderer.entity.ItemRenderer
 *  net.minecraft.client.renderer.entity.ThrownItemRenderer
 *  net.minecraft.client.renderer.item.ItemProperties
 *  net.minecraft.client.resources.model.BakedModel
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.item.DyeableLeatherItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  net.minecraftforge.client.event.EntityRenderersEvent$RegisterLayerDefinitions
 *  net.minecraftforge.client.event.ModelEvent$ModifyBakingResult
 *  net.minecraftforge.client.event.RegisterColorHandlersEvent$Block
 *  net.minecraftforge.client.event.RegisterColorHandlersEvent$Item
 *  net.minecraftforge.client.event.RegisterParticleProvidersEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.eventbus.api.IEventBus
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
 */
package com.github.alexthe666.alexsmobs;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.CommonProxy;
import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.client.ClientLayerRegistry;
import com.github.alexthe666.alexsmobs.client.event.ClientEvents;
import com.github.alexthe666.alexsmobs.client.gui.GUIAnimalDictionary;
import com.github.alexthe666.alexsmobs.client.gui.GUITransmutationTable;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.client.particle.ParticleBearFreddy;
import com.github.alexthe666.alexsmobs.client.particle.ParticleBirdSong;
import com.github.alexthe666.alexsmobs.client.particle.ParticleBunfungusTransformation;
import com.github.alexthe666.alexsmobs.client.particle.ParticleDna;
import com.github.alexthe666.alexsmobs.client.particle.ParticleFungusBubble;
import com.github.alexthe666.alexsmobs.client.particle.ParticleGusterSandShot;
import com.github.alexthe666.alexsmobs.client.particle.ParticleGusterSandSpin;
import com.github.alexthe666.alexsmobs.client.particle.ParticleHemolymph;
import com.github.alexthe666.alexsmobs.client.particle.ParticleInvertDig;
import com.github.alexthe666.alexsmobs.client.particle.ParticlePlatypus;
import com.github.alexthe666.alexsmobs.client.particle.ParticleSimpleHeart;
import com.github.alexthe666.alexsmobs.client.particle.ParticleSkulkBoom;
import com.github.alexthe666.alexsmobs.client.particle.ParticleSmelly;
import com.github.alexthe666.alexsmobs.client.particle.ParticleStaticSpark;
import com.github.alexthe666.alexsmobs.client.particle.ParticleSunbirdFeather;
import com.github.alexthe666.alexsmobs.client.particle.ParticleTeethGlint;
import com.github.alexthe666.alexsmobs.client.particle.ParticleWhaleSplash;
import com.github.alexthe666.alexsmobs.client.particle.ParticleWormPortal;
import com.github.alexthe666.alexsmobs.client.render.AMRenderTypes;
import com.github.alexthe666.alexsmobs.client.render.RenderAlligatorSnappingTurtle;
import com.github.alexthe666.alexsmobs.client.render.RenderAnaconda;
import com.github.alexthe666.alexsmobs.client.render.RenderAnacondaPart;
import com.github.alexthe666.alexsmobs.client.render.RenderAnteater;
import com.github.alexthe666.alexsmobs.client.render.RenderBaldEagle;
import com.github.alexthe666.alexsmobs.client.render.RenderBananaSlug;
import com.github.alexthe666.alexsmobs.client.render.RenderBison;
import com.github.alexthe666.alexsmobs.client.render.RenderBlobfish;
import com.github.alexthe666.alexsmobs.client.render.RenderBlueJay;
import com.github.alexthe666.alexsmobs.client.render.RenderBoneSerpent;
import com.github.alexthe666.alexsmobs.client.render.RenderBoneSerpentPart;
import com.github.alexthe666.alexsmobs.client.render.RenderBunfungus;
import com.github.alexthe666.alexsmobs.client.render.RenderCachalotEcho;
import com.github.alexthe666.alexsmobs.client.render.RenderCachalotWhale;
import com.github.alexthe666.alexsmobs.client.render.RenderCaiman;
import com.github.alexthe666.alexsmobs.client.render.RenderCapuchinMonkey;
import com.github.alexthe666.alexsmobs.client.render.RenderCatfish;
import com.github.alexthe666.alexsmobs.client.render.RenderCentipedeBody;
import com.github.alexthe666.alexsmobs.client.render.RenderCentipedeHead;
import com.github.alexthe666.alexsmobs.client.render.RenderCentipedeTail;
import com.github.alexthe666.alexsmobs.client.render.RenderCockroach;
import com.github.alexthe666.alexsmobs.client.render.RenderCombJelly;
import com.github.alexthe666.alexsmobs.client.render.RenderCosmaw;
import com.github.alexthe666.alexsmobs.client.render.RenderCosmicCod;
import com.github.alexthe666.alexsmobs.client.render.RenderCrimsonMosquito;
import com.github.alexthe666.alexsmobs.client.render.RenderCrocodile;
import com.github.alexthe666.alexsmobs.client.render.RenderCrow;
import com.github.alexthe666.alexsmobs.client.render.RenderDevilsHolePupfish;
import com.github.alexthe666.alexsmobs.client.render.RenderDropBear;
import com.github.alexthe666.alexsmobs.client.render.RenderElephant;
import com.github.alexthe666.alexsmobs.client.render.RenderEmu;
import com.github.alexthe666.alexsmobs.client.render.RenderEndergrade;
import com.github.alexthe666.alexsmobs.client.render.RenderEnderiophage;
import com.github.alexthe666.alexsmobs.client.render.RenderFarseer;
import com.github.alexthe666.alexsmobs.client.render.RenderFart;
import com.github.alexthe666.alexsmobs.client.render.RenderFlutter;
import com.github.alexthe666.alexsmobs.client.render.RenderFly;
import com.github.alexthe666.alexsmobs.client.render.RenderFlyingFish;
import com.github.alexthe666.alexsmobs.client.render.RenderFrilledShark;
import com.github.alexthe666.alexsmobs.client.render.RenderFroststalker;
import com.github.alexthe666.alexsmobs.client.render.RenderGazelle;
import com.github.alexthe666.alexsmobs.client.render.RenderGeladaMonkey;
import com.github.alexthe666.alexsmobs.client.render.RenderGiantSquid;
import com.github.alexthe666.alexsmobs.client.render.RenderGorilla;
import com.github.alexthe666.alexsmobs.client.render.RenderGrizzlyBear;
import com.github.alexthe666.alexsmobs.client.render.RenderGust;
import com.github.alexthe666.alexsmobs.client.render.RenderGuster;
import com.github.alexthe666.alexsmobs.client.render.RenderHammerheadShark;
import com.github.alexthe666.alexsmobs.client.render.RenderHemolymph;
import com.github.alexthe666.alexsmobs.client.render.RenderHummingbird;
import com.github.alexthe666.alexsmobs.client.render.RenderIceShard;
import com.github.alexthe666.alexsmobs.client.render.RenderJerboa;
import com.github.alexthe666.alexsmobs.client.render.RenderKangaroo;
import com.github.alexthe666.alexsmobs.client.render.RenderKomodoDragon;
import com.github.alexthe666.alexsmobs.client.render.RenderLaviathan;
import com.github.alexthe666.alexsmobs.client.render.RenderLeafcutterAnt;
import com.github.alexthe666.alexsmobs.client.render.RenderLobster;
import com.github.alexthe666.alexsmobs.client.render.RenderManedWolf;
import com.github.alexthe666.alexsmobs.client.render.RenderMantisShrimp;
import com.github.alexthe666.alexsmobs.client.render.RenderMimicOctopus;
import com.github.alexthe666.alexsmobs.client.render.RenderMimicube;
import com.github.alexthe666.alexsmobs.client.render.RenderMoose;
import com.github.alexthe666.alexsmobs.client.render.RenderMosquitoSpit;
import com.github.alexthe666.alexsmobs.client.render.RenderMudBall;
import com.github.alexthe666.alexsmobs.client.render.RenderMudskipper;
import com.github.alexthe666.alexsmobs.client.render.RenderMungus;
import com.github.alexthe666.alexsmobs.client.render.RenderMurmurBody;
import com.github.alexthe666.alexsmobs.client.render.RenderMurmurHead;
import com.github.alexthe666.alexsmobs.client.render.RenderOrca;
import com.github.alexthe666.alexsmobs.client.render.RenderPlatypus;
import com.github.alexthe666.alexsmobs.client.render.RenderPollenBall;
import com.github.alexthe666.alexsmobs.client.render.RenderPotoo;
import com.github.alexthe666.alexsmobs.client.render.RenderRaccoon;
import com.github.alexthe666.alexsmobs.client.render.RenderRainFrog;
import com.github.alexthe666.alexsmobs.client.render.RenderRattlesnake;
import com.github.alexthe666.alexsmobs.client.render.RenderRhinoceros;
import com.github.alexthe666.alexsmobs.client.render.RenderRoadrunner;
import com.github.alexthe666.alexsmobs.client.render.RenderRockyRoller;
import com.github.alexthe666.alexsmobs.client.render.RenderSandShot;
import com.github.alexthe666.alexsmobs.client.render.RenderSeaBear;
import com.github.alexthe666.alexsmobs.client.render.RenderSeagull;
import com.github.alexthe666.alexsmobs.client.render.RenderSeal;
import com.github.alexthe666.alexsmobs.client.render.RenderSharkToothArrow;
import com.github.alexthe666.alexsmobs.client.render.RenderShoebill;
import com.github.alexthe666.alexsmobs.client.render.RenderSkelewag;
import com.github.alexthe666.alexsmobs.client.render.RenderSkreecher;
import com.github.alexthe666.alexsmobs.client.render.RenderSkunk;
import com.github.alexthe666.alexsmobs.client.render.RenderSnowLeopard;
import com.github.alexthe666.alexsmobs.client.render.RenderSoulVulture;
import com.github.alexthe666.alexsmobs.client.render.RenderSpectre;
import com.github.alexthe666.alexsmobs.client.render.RenderSquidGrapple;
import com.github.alexthe666.alexsmobs.client.render.RenderStraddleboard;
import com.github.alexthe666.alexsmobs.client.render.RenderStraddler;
import com.github.alexthe666.alexsmobs.client.render.RenderStradpole;
import com.github.alexthe666.alexsmobs.client.render.RenderSugarGlider;
import com.github.alexthe666.alexsmobs.client.render.RenderSunbird;
import com.github.alexthe666.alexsmobs.client.render.RenderTarantulaHawk;
import com.github.alexthe666.alexsmobs.client.render.RenderTasmanianDevil;
import com.github.alexthe666.alexsmobs.client.render.RenderTendonSegment;
import com.github.alexthe666.alexsmobs.client.render.RenderTerrapin;
import com.github.alexthe666.alexsmobs.client.render.RenderTiger;
import com.github.alexthe666.alexsmobs.client.render.RenderTossedItem;
import com.github.alexthe666.alexsmobs.client.render.RenderToucan;
import com.github.alexthe666.alexsmobs.client.render.RenderTriops;
import com.github.alexthe666.alexsmobs.client.render.RenderTusklin;
import com.github.alexthe666.alexsmobs.client.render.RenderUnderminer;
import com.github.alexthe666.alexsmobs.client.render.RenderVineLasso;
import com.github.alexthe666.alexsmobs.client.render.RenderVoidPortal;
import com.github.alexthe666.alexsmobs.client.render.RenderVoidWormBody;
import com.github.alexthe666.alexsmobs.client.render.RenderVoidWormHead;
import com.github.alexthe666.alexsmobs.client.render.RenderVoidWormShot;
import com.github.alexthe666.alexsmobs.client.render.RenderWarpedMosco;
import com.github.alexthe666.alexsmobs.client.render.RenderWarpedToad;
import com.github.alexthe666.alexsmobs.client.render.item.AMItemRenderProperties;
import com.github.alexthe666.alexsmobs.client.render.item.CustomArmorRenderProperties;
import com.github.alexthe666.alexsmobs.client.render.item.GhostlyPickaxeBakedModel;
import com.github.alexthe666.alexsmobs.client.render.tile.RenderCapsid;
import com.github.alexthe666.alexsmobs.client.render.tile.RenderTransmutationTable;
import com.github.alexthe666.alexsmobs.client.render.tile.RenderVoidWormBeak;
import com.github.alexthe666.alexsmobs.client.sound.SoundBearMusicBox;
import com.github.alexthe666.alexsmobs.client.sound.SoundLaCucaracha;
import com.github.alexthe666.alexsmobs.client.sound.SoundWormBoss;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityBlueJay;
import com.github.alexthe666.alexsmobs.entity.EntityCockroach;
import com.github.alexthe666.alexsmobs.entity.EntityGrizzlyBear;
import com.github.alexthe666.alexsmobs.entity.EntityVoidWorm;
import com.github.alexthe666.alexsmobs.entity.util.RainbowUtil;
import com.github.alexthe666.alexsmobs.inventory.AMMenuRegistry;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.item.ItemBloodSprayer;
import com.github.alexthe666.alexsmobs.item.ItemHemolymphBlaster;
import com.github.alexthe666.alexsmobs.item.ItemTarantulaHawkElytra;
import com.github.alexthe666.alexsmobs.item.ItemTendonWhip;
import com.github.alexthe666.alexsmobs.tileentity.AMTileEntityRegistry;
import com.mojang.blaze3d.vertex.BufferBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@OnlyIn(value=Dist.CLIENT)
@Mod.EventBusSubscriber(modid="alexsmobs", value={Dist.CLIENT})
public class ClientProxy
extends CommonProxy {
    public static final Int2ObjectMap<SoundBearMusicBox> BEAR_MUSIC_BOX_SOUND_MAP = new Int2ObjectOpenHashMap();
    public static final Int2ObjectMap<SoundLaCucaracha> COCKROACH_SOUND_MAP = new Int2ObjectOpenHashMap();
    public static final Int2ObjectMap<SoundWormBoss> WORMBOSS_SOUND_MAP = new Int2ObjectOpenHashMap();
    public static final List<UUID> currentUnrenderedEntities = new ArrayList<UUID>();
    public static int voidPortalCreationTime = 0;
    public CameraType prevPOV = CameraType.FIRST_PERSON;
    public boolean initializedRainbowBuffers = false;
    private int pupfishChunkX = 0;
    private int pupfishChunkZ = 0;
    private int singingBlueJayId = -1;
    private final ItemStack[] transmuteStacks = new ItemStack[3];

    @SubscribeEvent
    @OnlyIn(value=Dist.CLIENT)
    public static void onItemColors(RegisterColorHandlersEvent.Item event) {
        AlexsMobs.LOGGER.info("loaded in item colorizer");
        if (AMItemRegistry.STRADDLEBOARD.isPresent()) {
            event.register((stack, colorIn) -> colorIn < 1 ? -1 : ((DyeableLeatherItem)stack.m_41720_()).m_41121_(stack), new ItemLike[]{(ItemLike)AMItemRegistry.STRADDLEBOARD.get()});
        } else {
            AlexsMobs.LOGGER.warn("Could not add straddleboard item to colorizer...");
        }
    }

    @SubscribeEvent
    @OnlyIn(value=Dist.CLIENT)
    public static void onBlockColors(RegisterColorHandlersEvent.Block event) {
        AlexsMobs.LOGGER.info("loaded in block colorizer");
        event.register((state, tintGetter, pos, tint) -> tintGetter != null && pos != null ? RainbowUtil.calculateGlassColor(pos) : -1, new Block[]{(Block)AMBlockRegistry.RAINBOW_GLASS.get()});
    }

    @Override
    public void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(ClientProxy::onBakingCompleted);
        bus.addListener(ClientProxy::onItemColors);
        bus.addListener(ClientProxy::onBlockColors);
        bus.addListener(ClientLayerRegistry::onAddLayers);
        bus.addListener(ClientProxy::setupParticles);
    }

    @Override
    public void clientInit() {
        MinecraftForge.EVENT_BUS.register((Object)new ClientEvents());
        this.initRainbowBuffers();
        ItemRenderer itemRendererIn = Minecraft.m_91087_().m_91291_();
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.GRIZZLY_BEAR.get()), RenderGrizzlyBear::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.ROADRUNNER.get()), RenderRoadrunner::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.BONE_SERPENT.get()), RenderBoneSerpent::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.BONE_SERPENT_PART.get()), RenderBoneSerpentPart::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.GAZELLE.get()), RenderGazelle::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.CROCODILE.get()), RenderCrocodile::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.FLY.get()), RenderFly::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.HUMMINGBIRD.get()), RenderHummingbird::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.ORCA.get()), RenderOrca::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.SUNBIRD.get()), RenderSunbird::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.GORILLA.get()), RenderGorilla::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.CRIMSON_MOSQUITO.get()), RenderCrimsonMosquito::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.MOSQUITO_SPIT.get()), RenderMosquitoSpit::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.RATTLESNAKE.get()), RenderRattlesnake::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.ENDERGRADE.get()), RenderEndergrade::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.HAMMERHEAD_SHARK.get()), RenderHammerheadShark::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.SHARK_TOOTH_ARROW.get()), RenderSharkToothArrow::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.LOBSTER.get()), RenderLobster::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.KOMODO_DRAGON.get()), RenderKomodoDragon::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.CAPUCHIN_MONKEY.get()), RenderCapuchinMonkey::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.TOSSED_ITEM.get()), RenderTossedItem::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.CENTIPEDE_HEAD.get()), RenderCentipedeHead::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.CENTIPEDE_BODY.get()), RenderCentipedeBody::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.CENTIPEDE_TAIL.get()), RenderCentipedeTail::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.WARPED_TOAD.get()), RenderWarpedToad::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.MOOSE.get()), RenderMoose::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.MIMICUBE.get()), RenderMimicube::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.RACCOON.get()), RenderRaccoon::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.BLOBFISH.get()), RenderBlobfish::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.SEAL.get()), RenderSeal::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.COCKROACH.get()), RenderCockroach::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.COCKROACH_EGG.get()), render -> new ThrownItemRenderer(render, 0.75f, true));
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.SHOEBILL.get()), RenderShoebill::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.ELEPHANT.get()), RenderElephant::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.SOUL_VULTURE.get()), RenderSoulVulture::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.SNOW_LEOPARD.get()), RenderSnowLeopard::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.SPECTRE.get()), RenderSpectre::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.CROW.get()), RenderCrow::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.ALLIGATOR_SNAPPING_TURTLE.get()), RenderAlligatorSnappingTurtle::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.MUNGUS.get()), RenderMungus::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.MANTIS_SHRIMP.get()), RenderMantisShrimp::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.GUSTER.get()), RenderGuster::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.SAND_SHOT.get()), RenderSandShot::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.GUST.get()), RenderGust::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.WARPED_MOSCO.get()), RenderWarpedMosco::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.HEMOLYMPH.get()), RenderHemolymph::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.STRADDLER.get()), RenderStraddler::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.STRADPOLE.get()), RenderStradpole::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.STRADDLEBOARD.get()), RenderStraddleboard::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.EMU.get()), RenderEmu::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.EMU_EGG.get()), render -> new ThrownItemRenderer(render, 0.75f, true));
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.PLATYPUS.get()), RenderPlatypus::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.DROPBEAR.get()), RenderDropBear::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.TASMANIAN_DEVIL.get()), RenderTasmanianDevil::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.KANGAROO.get()), RenderKangaroo::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.CACHALOT_WHALE.get()), RenderCachalotWhale::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.CACHALOT_ECHO.get()), RenderCachalotEcho::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.LEAFCUTTER_ANT.get()), RenderLeafcutterAnt::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.ENDERIOPHAGE.get()), RenderEnderiophage::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.ENDERIOPHAGE_ROCKET.get()), render -> new ThrownItemRenderer(render, 0.75f, true));
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.BALD_EAGLE.get()), RenderBaldEagle::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.TIGER.get()), RenderTiger::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.TARANTULA_HAWK.get()), RenderTarantulaHawk::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.VOID_WORM.get()), RenderVoidWormHead::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.VOID_WORM_PART.get()), RenderVoidWormBody::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.VOID_WORM_SHOT.get()), RenderVoidWormShot::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.VOID_PORTAL.get()), RenderVoidPortal::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.FRILLED_SHARK.get()), RenderFrilledShark::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.MIMIC_OCTOPUS.get()), RenderMimicOctopus::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.SEAGULL.get()), RenderSeagull::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.FROSTSTALKER.get()), RenderFroststalker::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.ICE_SHARD.get()), RenderIceShard::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.TUSKLIN.get()), RenderTusklin::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.LAVIATHAN.get()), RenderLaviathan::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.COSMAW.get()), RenderCosmaw::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.TOUCAN.get()), RenderToucan::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.MANED_WOLF.get()), RenderManedWolf::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.ANACONDA.get()), RenderAnaconda::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.ANACONDA_PART.get()), RenderAnacondaPart::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.VINE_LASSO.get()), RenderVineLasso::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.ANTEATER.get()), RenderAnteater::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.ROCKY_ROLLER.get()), RenderRockyRoller::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.FLUTTER.get()), RenderFlutter::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.POLLEN_BALL.get()), RenderPollenBall::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.GELADA_MONKEY.get()), RenderGeladaMonkey::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.JERBOA.get()), RenderJerboa::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.TERRAPIN.get()), RenderTerrapin::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.COMB_JELLY.get()), RenderCombJelly::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.COSMIC_COD.get()), RenderCosmicCod::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.BUNFUNGUS.get()), RenderBunfungus::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.BISON.get()), RenderBison::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.GIANT_SQUID.get()), RenderGiantSquid::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.SQUID_GRAPPLE.get()), RenderSquidGrapple::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.SEA_BEAR.get()), RenderSeaBear::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.DEVILS_HOLE_PUPFISH.get()), RenderDevilsHolePupfish::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.CATFISH.get()), RenderCatfish::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.FLYING_FISH.get()), RenderFlyingFish::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.SKELEWAG.get()), RenderSkelewag::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.RAIN_FROG.get()), RenderRainFrog::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.POTOO.get()), RenderPotoo::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.MUDSKIPPER.get()), RenderMudskipper::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.MUD_BALL.get()), RenderMudBall::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.RHINOCEROS.get()), RenderRhinoceros::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.SUGAR_GLIDER.get()), RenderSugarGlider::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.FARSEER.get()), RenderFarseer::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.SKREECHER.get()), RenderSkreecher::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.UNDERMINER.get()), RenderUnderminer::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.MURMUR.get()), RenderMurmurBody::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.MURMUR_HEAD.get()), RenderMurmurHead::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.TENDON_SEGMENT.get()), RenderTendonSegment::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.SKUNK.get()), RenderSkunk::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.FART.get()), RenderFart::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.BANANA_SLUG.get()), RenderBananaSlug::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.BLUE_JAY.get()), RenderBlueJay::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.CAIMAN.get()), RenderCaiman::new);
        EntityRenderers.m_174036_((EntityType)((EntityType)AMEntityRegistry.TRIOPS.get()), RenderTriops::new);
        try {
            ItemProperties.register((Item)((Item)AMItemRegistry.BLOOD_SPRAYER.get()), (ResourceLocation)new ResourceLocation("empty"), (stack, p_239428_1_, p_239428_2_, j) -> !ItemBloodSprayer.isUsable(stack) || p_239428_2_ instanceof Player && ((Player)p_239428_2_).m_36335_().m_41519_((Item)AMItemRegistry.BLOOD_SPRAYER.get()) ? 1.0f : 0.0f);
            ItemProperties.register((Item)((Item)AMItemRegistry.HEMOLYMPH_BLASTER.get()), (ResourceLocation)new ResourceLocation("empty"), (stack, p_239428_1_, p_239428_2_, j) -> !ItemHemolymphBlaster.isUsable(stack) || p_239428_2_ instanceof Player && ((Player)p_239428_2_).m_36335_().m_41519_((Item)AMItemRegistry.HEMOLYMPH_BLASTER.get()) ? 1.0f : 0.0f);
            ItemProperties.register((Item)((Item)AMItemRegistry.TARANTULA_HAWK_ELYTRA.get()), (ResourceLocation)new ResourceLocation("broken"), (stack, p_239428_1_, p_239428_2_, j) -> ItemTarantulaHawkElytra.isUsable(stack) ? 0.0f : 1.0f);
            ItemProperties.register((Item)((Item)AMItemRegistry.SHIELD_OF_THE_DEEP.get()), (ResourceLocation)new ResourceLocation("blocking"), (stack, p_239421_1_, p_239421_2_, j) -> p_239421_2_ != null && p_239421_2_.m_6117_() && p_239421_2_.m_21211_() == stack ? 1.0f : 0.0f);
            ItemProperties.register((Item)((Item)AMItemRegistry.SOMBRERO.get()), (ResourceLocation)new ResourceLocation("silly"), (stack, p_239421_1_, p_239421_2_, j) -> AlexsMobs.isAprilFools() ? 1.0f : 0.0f);
            ItemProperties.register((Item)((Item)AMItemRegistry.TENDON_WHIP.get()), (ResourceLocation)new ResourceLocation("active"), (stack, p_239421_1_, holder, j) -> ItemTendonWhip.isActive(stack, holder) ? 1.0f : 0.0f);
            ItemProperties.register((Item)((Item)AMItemRegistry.PUPFISH_LOCATOR.get()), (ResourceLocation)new ResourceLocation("in_chunk"), (stack, world, entity, j) -> {
                int x = this.pupfishChunkX * 16;
                int z = this.pupfishChunkZ * 16;
                if (entity != null && entity.m_20185_() >= (double)x && entity.m_20185_() <= (double)(x + 16) && entity.m_20189_() >= (double)z && entity.m_20189_() <= (double)(z + 16)) {
                    return 1.0f;
                }
                return 0.0f;
            });
            ItemProperties.register((Item)((Item)AMItemRegistry.SKELEWAG_SWORD.get()), (ResourceLocation)new ResourceLocation("blocking"), (stack, p_239421_1_, p_239421_2_, j) -> p_239421_2_ != null && p_239421_2_.m_6117_() && p_239421_2_.m_21211_() == stack ? 1.0f : 0.0f);
        }
        catch (Exception e) {
            AlexsMobs.LOGGER.warn("Could not load item models for weapons");
        }
        BlockEntityRenderers.m_173590_((BlockEntityType)((BlockEntityType)AMTileEntityRegistry.CAPSID.get()), RenderCapsid::new);
        BlockEntityRenderers.m_173590_((BlockEntityType)((BlockEntityType)AMTileEntityRegistry.VOID_WORM_BEAK.get()), RenderVoidWormBeak::new);
        BlockEntityRenderers.m_173590_((BlockEntityType)((BlockEntityType)AMTileEntityRegistry.TRANSMUTATION_TABLE.get()), RenderTransmutationTable::new);
        MenuScreens.m_96206_((MenuType)((MenuType)AMMenuRegistry.TRANSMUTATION_TABLE.get()), GUITransmutationTable::new);
    }

    private void initRainbowBuffers() {
        Minecraft.m_91087_().m_91269_().f_110093_.put(AMRenderTypes.COMBJELLY_RAINBOW_GLINT, new BufferBuilder(AMRenderTypes.COMBJELLY_RAINBOW_GLINT.m_110507_()));
        Minecraft.m_91087_().m_91269_().f_110093_.put(AMRenderTypes.VOID_WORM_PORTAL_OVERLAY, new BufferBuilder(AMRenderTypes.VOID_WORM_PORTAL_OVERLAY.m_110507_()));
        Minecraft.m_91087_().m_91269_().f_110093_.put(AMRenderTypes.STATIC_PORTAL, new BufferBuilder(AMRenderTypes.STATIC_PORTAL.m_110507_()));
        Minecraft.m_91087_().m_91269_().f_110093_.put(AMRenderTypes.STATIC_PARTICLE, new BufferBuilder(AMRenderTypes.STATIC_PARTICLE.m_110507_()));
        Minecraft.m_91087_().m_91269_().f_110093_.put(AMRenderTypes.STATIC_ENTITY, new BufferBuilder(AMRenderTypes.STATIC_ENTITY.m_110507_()));
        this.initializedRainbowBuffers = true;
    }

    private static void onBakingCompleted(ModelEvent.ModifyBakingResult e) {
        String ghostlyPickaxe = "alexsmobs:ghostly_pickaxe";
        for (ResourceLocation id : e.getModels().keySet()) {
            if (!id.toString().contains(ghostlyPickaxe)) continue;
            e.getModels().put(id, new GhostlyPickaxeBakedModel((BakedModel)e.getModels().get(id)));
        }
    }

    @Override
    public void openBookGUI(ItemStack itemStackIn) {
        Minecraft.m_91087_().m_91152_((Screen)new GUIAnimalDictionary(itemStackIn));
    }

    @Override
    public void openBookGUI(ItemStack itemStackIn, String page) {
        Minecraft.m_91087_().m_91152_((Screen)new GUIAnimalDictionary(itemStackIn, page));
    }

    @Override
    public Player getClientSidePlayer() {
        return Minecraft.m_91087_().f_91074_;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public Object getArmorModel(int armorId, LivingEntity entity) {
        switch (armorId) {
            default: 
        }
        return null;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void onEntityStatus(Entity entity, byte updateKind) {
        if (updateKind == 67) {
            if (entity instanceof EntityCockroach && entity.m_6084_()) {
                SoundLaCucaracha sound;
                if (COCKROACH_SOUND_MAP.get(entity.m_19879_()) == null) {
                    sound = new SoundLaCucaracha((EntityCockroach)entity);
                    COCKROACH_SOUND_MAP.put(entity.m_19879_(), (Object)sound);
                } else {
                    sound = (SoundLaCucaracha)((Object)COCKROACH_SOUND_MAP.get(entity.m_19879_()));
                }
                if (!Minecraft.m_91087_().m_91106_().m_120403_((SoundInstance)sound) && sound.m_7767_() && sound.isOnlyCockroach()) {
                    Minecraft.m_91087_().m_91106_().m_120367_((SoundInstance)sound);
                }
            } else if (entity instanceof EntityVoidWorm && entity.m_6084_()) {
                float f2 = Minecraft.m_91087_().f_91066_.m_92147_(SoundSource.MUSIC);
                if (f2 <= 0.0f) {
                    WORMBOSS_SOUND_MAP.clear();
                } else {
                    SoundWormBoss sound;
                    if (WORMBOSS_SOUND_MAP.get(entity.m_19879_()) == null) {
                        sound = new SoundWormBoss((EntityVoidWorm)entity);
                        WORMBOSS_SOUND_MAP.put(entity.m_19879_(), (Object)sound);
                    } else {
                        sound = (SoundWormBoss)((Object)WORMBOSS_SOUND_MAP.get(entity.m_19879_()));
                    }
                    if (!Minecraft.m_91087_().m_91106_().m_120403_((SoundInstance)sound) && sound.isNearest()) {
                        Minecraft.m_91087_().m_91106_().m_120367_((SoundInstance)sound);
                    }
                }
            } else if (entity instanceof EntityGrizzlyBear && entity.m_6084_()) {
                SoundBearMusicBox sound;
                if (BEAR_MUSIC_BOX_SOUND_MAP.get(entity.m_19879_()) == null) {
                    sound = new SoundBearMusicBox((EntityGrizzlyBear)entity);
                    BEAR_MUSIC_BOX_SOUND_MAP.put(entity.m_19879_(), (Object)sound);
                } else {
                    sound = (SoundBearMusicBox)((Object)BEAR_MUSIC_BOX_SOUND_MAP.get(entity.m_19879_()));
                }
                if (!Minecraft.m_91087_().m_91106_().m_120403_((SoundInstance)sound) && sound.m_7767_() && sound.isOnlyMusicBox()) {
                    Minecraft.m_91087_().m_91106_().m_120367_((SoundInstance)sound);
                }
            } else if (entity instanceof EntityBlueJay && entity.m_6084_()) {
                this.singingBlueJayId = entity.m_19879_();
            }
        }
        if (entity instanceof EntityBlueJay && entity.m_6084_() && updateKind == 68) {
            this.singingBlueJayId = -1;
        }
    }

    @Override
    public void updateBiomeVisuals(int x, int z) {
        Minecraft.m_91087_().f_91060_.m_109494_(x - 32, 0, x - 32, z + 32, 255, z + 32);
    }

    public static void setupParticles(RegisterParticleProvidersEvent registry) {
        AlexsMobs.LOGGER.debug("Registered particle factories");
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.GUSTER_SAND_SPIN.get(), ParticleGusterSandSpin.Factory::new);
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.GUSTER_SAND_SHOT.get(), ParticleGusterSandShot.Factory::new);
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.GUSTER_SAND_SPIN_RED.get(), ParticleGusterSandSpin.FactoryRed::new);
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.GUSTER_SAND_SHOT_RED.get(), ParticleGusterSandShot.FactoryRed::new);
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.GUSTER_SAND_SPIN_SOUL.get(), ParticleGusterSandSpin.FactorySoul::new);
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.GUSTER_SAND_SHOT_SOUL.get(), ParticleGusterSandShot.FactorySoul::new);
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.HEMOLYMPH.get(), ParticleHemolymph.Factory::new);
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.PLATYPUS_SENSE.get(), ParticlePlatypus.Factory::new);
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.WHALE_SPLASH.get(), ParticleWhaleSplash.Factory::new);
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.DNA.get(), ParticleDna.Factory::new);
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.SHOCKED.get(), ParticleSimpleHeart.Factory::new);
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.WORM_PORTAL.get(), ParticleWormPortal.Factory::new);
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.INVERT_DIG.get(), ParticleInvertDig.Factory::new);
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.TEETH_GLINT.get(), ParticleTeethGlint.Factory::new);
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.SMELLY.get(), ParticleSmelly.Factory::new);
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.BUNFUNGUS_TRANSFORMATION.get(), ParticleBunfungusTransformation.Factory::new);
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.FUNGUS_BUBBLE.get(), ParticleFungusBubble.Factory::new);
        registry.registerSpecial((ParticleType)AMParticleRegistry.BEAR_FREDDY.get(), (ParticleProvider)new ParticleBearFreddy.Factory());
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.SUNBIRD_FEATHER.get(), ParticleSunbirdFeather.Factory::new);
        registry.registerSpecial((ParticleType)AMParticleRegistry.STATIC_SPARK.get(), (ParticleProvider)new ParticleStaticSpark.Factory());
        registry.registerSpecial((ParticleType)AMParticleRegistry.SKULK_BOOM.get(), (ParticleProvider)new ParticleSkulkBoom.Factory());
        registry.registerSpriteSet((ParticleType)AMParticleRegistry.BIRD_SONG.get(), ParticleBirdSong.Factory::new);
    }

    @Override
    public void setRenderViewEntity(Entity entity) {
        this.prevPOV = Minecraft.m_91087_().f_91066_.m_92176_();
        Minecraft.m_91087_().m_91118_(entity);
        Minecraft.m_91087_().f_91066_.m_92157_(CameraType.THIRD_PERSON_BACK);
    }

    @Override
    public void resetRenderViewEntity() {
        Minecraft.m_91087_().m_91118_((Entity)Minecraft.m_91087_().f_91074_);
    }

    @Override
    public int getPreviousPOV() {
        return this.prevPOV.ordinal();
    }

    @Override
    public boolean isFarFromCamera(double x, double y, double z) {
        Minecraft lvt_1_1_ = Minecraft.m_91087_();
        return lvt_1_1_.f_91063_.m_109153_().m_90583_().m_82531_(x, y, z) >= 256.0;
    }

    @Override
    public void resetVoidPortalCreation(Player player) {
    }

    @SubscribeEvent
    @OnlyIn(value=Dist.CLIENT)
    public void onRegisterEntityRenders(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }

    @Override
    public Object getISTERProperties() {
        return new AMItemRenderProperties();
    }

    @Override
    public Object getArmorRenderProperties() {
        return new CustomArmorRenderProperties();
    }

    @Override
    public void spawnSpecialParticle(int type) {
        if (type == 0) {
            Minecraft.m_91087_().f_91073_.m_7106_((ParticleOptions)AMParticleRegistry.BEAR_FREDDY.get(), Minecraft.m_91087_().f_91074_.m_20185_(), Minecraft.m_91087_().f_91074_.m_20186_(), Minecraft.m_91087_().f_91074_.m_20189_(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void processVisualFlag(Entity entity, int flag) {
        if (entity == Minecraft.m_91087_().f_91074_ && flag == 87) {
            ClientEvents.renderStaticScreenFor = 60;
        }
    }

    @Override
    public void setPupfishChunkForItem(int chunkX, int chunkZ) {
        this.pupfishChunkX = chunkX;
        this.pupfishChunkZ = chunkZ;
    }

    @Override
    public void setDisplayTransmuteResult(int slot, ItemStack stack) {
        this.transmuteStacks[Mth.m_14045_((int)slot, (int)0, (int)2)] = stack;
    }

    @Override
    public ItemStack getDisplayTransmuteResult(int slot) {
        ItemStack stack = this.transmuteStacks[Mth.m_14045_((int)slot, (int)0, (int)2)];
        return stack == null ? ItemStack.f_41583_ : stack;
    }

    @Override
    public int getSingingBlueJayId() {
        return this.singingBlueJayId;
    }
}

