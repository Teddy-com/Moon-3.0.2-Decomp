/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonParser
 *  org.lwjgl.opengl.Display
 */
package me.moon;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import me.moon.command.Command;
import me.moon.command.CommandManager;
import me.moon.command.impl.BindCommand;
import me.moon.command.impl.ConfigCommand;
import me.moon.command.impl.FriendCommand;
import me.moon.command.impl.HelpCommand;
import me.moon.command.impl.MacroCommand;
import me.moon.command.impl.ModulesCommand;
import me.moon.command.impl.TeleportCommand;
import me.moon.command.impl.ToggleCommand;
import me.moon.command.impl.VClipCommand;
import me.moon.command.impl.WaypointCommand;
import me.moon.config.ConfigManager;
import me.moon.event.Event;
import me.moon.event.manager.EventManager;
import me.moon.friend.FriendManager;
import me.moon.gui.account.gui.GuiAltManager;
import me.moon.gui.account.notification.NotificationManagerAlt;
import me.moon.gui.account.system.AccountManager;
import me.moon.macro.MacroManager;
import me.moon.module.Module;
import me.moon.module.ModuleManager;
import me.moon.module.impl.combat.AntiBot;
import me.moon.module.impl.combat.AuraDev;
import me.moon.module.impl.combat.AutoApple;
import me.moon.module.impl.combat.BowAimbot;
import me.moon.module.impl.combat.CopsNCrims;
import me.moon.module.impl.combat.Criticals;
import me.moon.module.impl.combat.KillAura;
import me.moon.module.impl.combat.Shooter;
import me.moon.module.impl.combat.TPAura;
import me.moon.module.impl.combat.Velocity;
import me.moon.module.impl.exploit.Blink;
import me.moon.module.impl.exploit.BlinkLag;
import me.moon.module.impl.exploit.Disabler;
import me.moon.module.impl.exploit.FastUse;
import me.moon.module.impl.exploit.Fastbow;
import me.moon.module.impl.exploit.Phase;
import me.moon.module.impl.exploit.PingSpoof;
import me.moon.module.impl.exploit.Regen;
import me.moon.module.impl.exploit.Speedmine;
import me.moon.module.impl.ghost.AimAssist;
import me.moon.module.impl.ghost.Autoclicker;
import me.moon.module.impl.ghost.BackTracing;
import me.moon.module.impl.ghost.Reach;
import me.moon.module.impl.movement.FastLadder;
import me.moon.module.impl.movement.Flight;
import me.moon.module.impl.movement.Jesus;
import me.moon.module.impl.movement.LongJump;
import me.moon.module.impl.movement.NoSlowdown;
import me.moon.module.impl.movement.NoWeb;
import me.moon.module.impl.movement.Scaffold;
import me.moon.module.impl.movement.Sneak;
import me.moon.module.impl.movement.Speed;
import me.moon.module.impl.movement.Sprint;
import me.moon.module.impl.movement.Step;
import me.moon.module.impl.movement.TargetStrafe;
import me.moon.module.impl.movement.Timer;
import me.moon.module.impl.other.Animation;
import me.moon.module.impl.other.AutoGG;
import me.moon.module.impl.other.AutoMatchJoin;
import me.moon.module.impl.other.BedBreaker;
import me.moon.module.impl.other.ChatBypass;
import me.moon.module.impl.other.Detector;
import me.moon.module.impl.other.LightningTracker;
import me.moon.module.impl.other.MurderMystery;
import me.moon.module.impl.other.NoRotate;
import me.moon.module.impl.other.NoScoreBoard;
import me.moon.module.impl.other.Spammer;
import me.moon.module.impl.other.StreamerMode;
import me.moon.module.impl.other.Teleport;
import me.moon.module.impl.other.TimeChanger;
import me.moon.module.impl.other.Unstuck;
import me.moon.module.impl.other.VanishDetector;
import me.moon.module.impl.player.AntiVoid;
import me.moon.module.impl.player.AutoDisable;
import me.moon.module.impl.player.AutoTool;
import me.moon.module.impl.player.ChestStealer;
import me.moon.module.impl.player.Freecam;
import me.moon.module.impl.player.InvManager;
import me.moon.module.impl.player.InvWalk;
import me.moon.module.impl.player.MCF;
import me.moon.module.impl.player.NoFall;
import me.moon.module.impl.visuals.Bloom;
import me.moon.module.impl.visuals.Blur;
import me.moon.module.impl.visuals.BreadCrumbs;
import me.moon.module.impl.visuals.Chams;
import me.moon.module.impl.visuals.ChestESP;
import me.moon.module.impl.visuals.ClickGui;
import me.moon.module.impl.visuals.Crosshair;
import me.moon.module.impl.visuals.DMGParticle;
import me.moon.module.impl.visuals.ESP;
import me.moon.module.impl.visuals.FOV;
import me.moon.module.impl.visuals.FullBright;
import me.moon.module.impl.visuals.Glint;
import me.moon.module.impl.visuals.Glow;
import me.moon.module.impl.visuals.HUD;
import me.moon.module.impl.visuals.IRC;
import me.moon.module.impl.visuals.ItemPhysics;
import me.moon.module.impl.visuals.ItemSize;
import me.moon.module.impl.visuals.MotionGraph;
import me.moon.module.impl.visuals.Nametags;
import me.moon.module.impl.visuals.NetGraph;
import me.moon.module.impl.visuals.NoBobbing;
import me.moon.module.impl.visuals.NoHurtCam;
import me.moon.module.impl.visuals.OffscreenESP;
import me.moon.module.impl.visuals.OutlineESP;
import me.moon.module.impl.visuals.Packets;
import me.moon.module.impl.visuals.PenisESP;
import me.moon.module.impl.visuals.Secret;
import me.moon.module.impl.visuals.SkeletonESP;
import me.moon.module.impl.visuals.Tracers;
import me.moon.module.impl.visuals.Trajectories;
import me.moon.module.impl.visuals.Waypoints;
import me.moon.module.impl.visuals.hud.ComponentManager;
import me.moon.module.impl.visuals.hud.editscreen.notification.NotificationManagerHUD;
import me.moon.notification.NotificationManager;
import me.moon.utils.blockdata.BlockDataUtil;
import me.moon.utils.game.RotationUtil;
import me.moon.utils.thealtening.AltService;
import me.moon.utils.value.Value;
import me.moon.waypoint.WaypointManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.Display;

public enum Moon {
    INSTANCE;

    private File dir;
    public EventManager<Event> eventBus = new EventManager();
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private NotificationManager notificationManager;
    private ComponentManager componentManager;
    private BlockDataUtil blockDataUtil;
    private RotationUtil rotationUtil;
    private NotificationManagerHUD customHudNotification;
    private AltService altService;
    private NotificationManagerAlt accountManagerNotification;
    private AccountManager accountManager;
    private ConfigManager configManager;
    private WaypointManager waypointManager;
    private FriendManager friendManager;
    private MacroManager macroManager;
    public String username;
    public String uid;
    public VersionType versionType = VersionType.DEVELOPER;
    public String version = "3.0.2";
    public Socket client;
    public PrintStream output;

    public void init() {
        this.username = "Free Build";
        System.out.println("Welcome back, " + this.username);
        Moon.INSTANCE.username = this.username;
        PlayerControllerMP.survivalBlockReach = Float.valueOf(4.5f);
        EntityLivingBase.jumpMotion = 0.42f;
        EntityRenderer.split = "Moon\n";
        EntityPlayerSP.logsList = new ArrayList<String>();
        File file = new File(Minecraft.getMinecraft().mcDataDir, "Moon 3.0");
        INSTANCE.setModuleManager(new ModuleManager());
        INSTANCE.setCommandManager(new CommandManager());
        INSTANCE.setNotificationManager(new NotificationManager());
        INSTANCE.setComponentManager(new ComponentManager());
        INSTANCE.setBlockDataUtil(new BlockDataUtil());
        INSTANCE.setRotationUtil(new RotationUtil());
        INSTANCE.setCustomHudNotification(new NotificationManagerHUD());
        INSTANCE.setAccountManagerNotification(new NotificationManagerAlt());
        INSTANCE.setAltService(new AltService());
        INSTANCE.setDir(file);
        INSTANCE.getModuleManager().setDir(new File(INSTANCE.getDir(), "modules"));
        ArrayList<Class> modules = new ArrayList<Class>(Arrays.asList(AntiBot.class, AuraDev.class, AutoApple.class, BowAimbot.class, CopsNCrims.class, Criticals.class, KillAura.class, Shooter.class, TPAura.class, Velocity.class, Blink.class, BlinkLag.class, Disabler.class, Fastbow.class, FastUse.class, Phase.class, PingSpoof.class, Regen.class, Speedmine.class, AimAssist.class, Autoclicker.class, BackTracing.class, Reach.class, FastLadder.class, Flight.class, Jesus.class, LongJump.class, NoSlowdown.class, NoWeb.class, Scaffold.class, Sneak.class, Speed.class, Sprint.class, Step.class, TargetStrafe.class, Timer.class, Animation.class, AutoGG.class, AutoMatchJoin.class, BedBreaker.class, ChatBypass.class, Detector.class, LightningTracker.class, MurderMystery.class, NoRotate.class, NoScoreBoard.class, Spammer.class, StreamerMode.class, Teleport.class, TimeChanger.class, Unstuck.class, VanishDetector.class, AntiVoid.class, AutoDisable.class, AutoTool.class, ChestStealer.class, Freecam.class, InvManager.class, InvWalk.class, MCF.class, NoFall.class, Bloom.class, Blur.class, BreadCrumbs.class, Chams.class, ChestESP.class, ClickGui.class, Crosshair.class, DMGParticle.class, ESP.class, FOV.class, FullBright.class, Glint.class, Glow.class, HUD.class, IRC.class, ItemPhysics.class, ItemSize.class, MotionGraph.class, Nametags.class, NetGraph.class, NoBobbing.class, NoHurtCam.class, OffscreenESP.class, OutlineESP.class, Packets.class, PenisESP.class, Secret.class, SkeletonESP.class, Tracers.class, Trajectories.class, Waypoints.class));
        for (Class moduleClass : modules) {
            try {
                Module createdModule = (Module)moduleClass.newInstance();
                for (Field field : createdModule.getClass().getDeclaredFields()) {
                    try {
                        field.setAccessible(true);
                        Object obj = field.get(createdModule);
                        if (!(obj instanceof Value)) continue;
                        createdModule.getValues().add((Value)obj);
                    }
                    catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                ModuleManager.moduleHashMap.put(createdModule.getLabel().toLowerCase(), createdModule);
            }
            catch (Exception createdModule) {}
        }
        INSTANCE.getModuleManager().initializeModules();
        ModuleManager.moduleHashMap.values().forEach(module -> {
            File moduleFile = new File(INSTANCE.getDir(), module.getLabel() + ".json");
            if (!moduleFile.exists() || !Gui.protectionIsPassed) {
                return;
            }
            try (FileReader reader = new FileReader(moduleFile);){
                JsonElement node = new JsonParser().parse((Reader)reader);
                if (!node.isJsonObject()) {
                    return;
                }
                module.load(node.getAsJsonObject(), true);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        });
        INSTANCE.setFriendManager(new FriendManager(INSTANCE.getDir()));
        INSTANCE.setConfigManager(new ConfigManager(new File(INSTANCE.getDir(), "configs")));
        INSTANCE.getConfigManager().load();
        INSTANCE.setWaypointManager(new WaypointManager(INSTANCE.getDir()));
        INSTANCE.setMacroManager(new MacroManager(INSTANCE.getDir()));
        INSTANCE.getCommandManager().initialize();
        ArrayList<Class> commands = new ArrayList<Class>(Arrays.asList(BindCommand.class, ConfigCommand.class, FriendCommand.class, HelpCommand.class, MacroCommand.class, ModulesCommand.class, TeleportCommand.class, ToggleCommand.class, VClipCommand.class, WaypointCommand.class));
        for (Class commandClass : commands) {
            try {
                Command createdCommand = (Command)commandClass.newInstance();
                CommandManager.map.put(createdCommand.getLabel().toLowerCase(), createdCommand);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        INSTANCE.getEventBus().registerListener(INSTANCE.getCommandManager());
        INSTANCE.setAccountManager(new AccountManager(INSTANCE.getDir()));
        Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
        Display.setTitle((String)"Moon\n");
    }

    public void endClient() {
        System.out.println("Saving Moon Stuff.");
        this.moduleManager.saveModules();
        this.friendManager.getFriendSaving().saveFile();
        this.macroManager.save();
        this.accountManager.save();
    }

    public void blur() {
        Minecraft.getMinecraft().gameSettings.ofFastRender = false;
        HUD hud = (HUD)INSTANCE.getModuleManager().getModule("HUD");
        ESP esp = (ESP)INSTANCE.getModuleManager().getModule("ESP");
        Chams chams = (Chams)INSTANCE.getModuleManager().getModule("Chams");
        if (Minecraft.getMinecraft().currentScreen instanceof GuiAltManager) {
            GuiAltManager currentScreen = (GuiAltManager)Minecraft.getMinecraft().currentScreen;
            currentScreen.blur();
            return;
        }
        if (this.uid == null) {
            return;
        }
        hud.blur();
        esp.blur();
        this.notificationManager.blur();
        chams.blur();
        Minecraft.getMinecraft().ingameGUI.blur();
        Minecraft.getMinecraft().ingameGUI.getChatGUI().blur();
        GuiMainMenu.blur();
    }

    public void switchToMojang() {
        try {
            this.altService.switchService(AltService.EnumAltService.MOJANG);
        }
        catch (IllegalAccessException | NoSuchFieldException e) {
            System.out.println("Couldnt switch to mojang altservice");
        }
    }

    public void switchToTheAltening() {
        try {
            this.altService.switchService(AltService.EnumAltService.THEALTENING);
        }
        catch (IllegalAccessException | NoSuchFieldException e) {
            System.out.println("Couldnt switch to altening altservice");
        }
    }

    public VersionType getVersionType() {
        return this.versionType;
    }

    public String getVersion() {
        return this.version;
    }

    public File getDir() {
        return this.dir;
    }

    public EventManager<Event> getEventBus() {
        return this.eventBus;
    }

    public MacroManager getMacroManager() {
        return this.macroManager;
    }

    public ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public FriendManager getFriendManager() {
        return this.friendManager;
    }

    public NotificationManager getNotificationManager() {
        return this.notificationManager;
    }

    public WaypointManager getWaypointManager() {
        return this.waypointManager;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public AccountManager getAccountManager() {
        return this.accountManager;
    }

    public ComponentManager getComponentManager() {
        return this.componentManager;
    }

    public NotificationManagerHUD getCustomHudNotification() {
        return this.customHudNotification;
    }

    public NotificationManagerAlt getAccountManagerNotification() {
        return this.accountManagerNotification;
    }

    public BlockDataUtil getBlockData() {
        return this.blockDataUtil;
    }

    public RotationUtil getRotationUtil() {
        return this.rotationUtil;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public void setFriendManager(FriendManager friendManager) {
        this.friendManager = friendManager;
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void setWaypointManager(WaypointManager waypointManager) {
        this.waypointManager = waypointManager;
    }

    public void setMacroManager(MacroManager macroManager) {
        this.macroManager = macroManager;
    }

    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void setModuleManager(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    public void setBlockDataUtil(BlockDataUtil blockDataUtil) {
        this.blockDataUtil = blockDataUtil;
    }

    public void setAltService(AltService altService) {
        this.altService = altService;
    }

    public void setAccountManagerNotification(NotificationManagerAlt accountManagerNotification) {
        this.accountManagerNotification = accountManagerNotification;
    }

    public void setComponentManager(ComponentManager componentManager) {
        this.componentManager = componentManager;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void setCustomHudNotification(NotificationManagerHUD customHudNotification) {
        this.customHudNotification = customHudNotification;
    }

    public void setEventBus(EventManager<Event> eventBus) {
        this.eventBus = eventBus;
    }

    public void setNotificationManager(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    public void setRotationUtil(RotationUtil rotationUtil) {
        this.rotationUtil = rotationUtil;
    }

    public static enum VersionType {
        RELEASE("Release"),
        BETA("Beta"),
        DEVELOPER("Development");

        String display;

        private VersionType(String display) {
            this.display = display;
        }

        public String getDisplay() {
            return this.display;
        }
    }
}

