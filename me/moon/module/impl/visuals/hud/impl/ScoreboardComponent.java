/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 *  org.lwjgl.opengl.GL11
 */
package me.moon.module.impl.visuals.hud.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import me.moon.Moon;
import me.moon.event.impl.player.UpdateEvent;
import me.moon.module.Module;
import me.moon.module.impl.other.AutoMatchJoin;
import me.moon.module.impl.visuals.HUD;
import me.moon.module.impl.visuals.hud.Component;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

public class ScoreboardComponent
extends Component {
    public ScoreboardComponent(String name) {
        super(name, 2.0, 2.0);
    }

    @Override
    public void onCompRender(ScaledResolution sr) {
        ScoreObjective scoreobjective1;
        int j1;
        GL11.glPushMatrix();
        GL11.glEnable((int)3008);
        Scoreboard scoreboardL = this.mc.theWorld.getScoreboard();
        ScoreObjective scoreobjectiveL = null;
        ScorePlayerTeam scoreplayerteamL = scoreboardL.getPlayersTeam(this.mc.thePlayer.getName());
        if (scoreplayerteamL != null && (j1 = scoreplayerteamL.getChatFormat().getColorIndex()) >= 0) {
            scoreobjectiveL = scoreboardL.getObjectiveInDisplaySlot(3 + j1);
        }
        ScoreObjective scoreObjective = scoreobjective1 = scoreobjectiveL != null ? scoreobjectiveL : scoreboardL.getObjectiveInDisplaySlot(1);
        if (scoreobjective1 != null) {
            String wtf1;
            ScorePlayerTeam wtf;
            String s1;
            ScorePlayerTeam scoreplayerteam1;
            HUD hud = (HUD)Moon.INSTANCE.getModuleManager().getModule("hud");
            Scoreboard scoreboard = scoreobjective1.getScoreboard();
            Collection<Score> collection = scoreboard.getSortedScores(scoreobjective1);
            ArrayList arraylist = Lists.newArrayList((Iterable)Iterables.filter(collection, (Predicate)new Predicate(){
                private static final String __OBFID = "CL_00001958";

                public boolean apply(Score p_apply_1_) {
                    return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
                }

                public boolean apply(Object p_apply_1_) {
                    return this.apply((Score)p_apply_1_);
                }
            }));
            ArrayList arraylist1 = arraylist.size() > 15 ? Lists.newArrayList((Iterable)Iterables.skip((Iterable)arraylist, (int)(collection.size() - 15))) : arraylist;
            int i = this.mc.fontRendererObj.getStringWidth(scoreobjective1.getDisplayName());
            for (Object score : arraylist1) {
                ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(((Score)score).getPlayerName());
                String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, ((Score)score).getPlayerName()) + ": " + (Object)((Object)EnumChatFormatting.RED) + ((Score)score).getScorePoints();
                i = Math.max(i, this.mc.fontRendererObj.getStringWidth(s));
            }
            float y1 = this.mc.fontRendererObj.FONT_HEIGHT * 2;
            for (Module mod : Moon.INSTANCE.getModuleManager().getModuleMap().values()) {
                if (mod.isHidden() || !mod.isEnabled()) continue;
                y1 += (float)(this.mc.fontRendererObj.FONT_HEIGHT + 13);
            }
            float y = y1 > (float)(sr.getScaledHeight() / 2) ? y1 : (float)(sr.getScaledHeight() / 2);
            double j12 = this.getY();
            double k1 = (double)(arraylist1.size() * this.mc.fontRendererObj.FONT_HEIGHT) + this.getY() + (double)(this.mc.fontRendererObj.FONT_HEIGHT * 2);
            int b0 = 3;
            int j = (int)this.getX();
            int k = 0;
            ArrayList<Object> arraylist2 = new ArrayList<Object>(arraylist1);
            arraylist2.sort(Comparator.comparingDouble(module -> {
                ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(((Score)module).getPlayerName());
                return -this.mc.fontRendererObj.getStringWidth(ScorePlayerTeam.formatPlayerName(scoreplayerteam1, ((Score)module).getPlayerName()));
            }));
            for (Object score2 : arraylist1) {
                scoreplayerteam1 = scoreboard.getPlayersTeam(((Score)score2).getPlayerName());
                s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, ((Score)score2).getPlayerName());
                wtf = scoreboard.getPlayersTeam(((Score)arraylist2.toArray()[0]).getPlayerName());
                wtf1 = ScorePlayerTeam.formatPlayerName(wtf, ((Score)arraylist2.toArray()[0]).getPlayerName());
                int n = (int)(this.x + (double)Math.max(this.mc.fontRendererObj.getStringWidth(wtf1) + 5, this.mc.fontRendererObj.getStringWidth(scoreobjective1.getDisplayName()) + 5));
            }
            for (Object score1 : arraylist1) {
                ++k;
                scoreplayerteam1 = scoreboard.getPlayersTeam(((Score)score1).getPlayerName());
                s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, ((Score)score1).getPlayerName());
                wtf = scoreboard.getPlayersTeam(((Score)arraylist2.toArray()[0]).getPlayerName());
                wtf1 = ScorePlayerTeam.formatPlayerName(wtf, ((Score)arraylist2.toArray()[0]).getPlayerName());
                String s2 = (Object)((Object)EnumChatFormatting.RED) + "";
                String s3 = scoreobjective1.getDisplayName();
                double l = k1 - (double)(k * this.mc.fontRendererObj.FONT_HEIGHT);
                int i1 = (int)(this.x + (double)Math.max(this.mc.fontRendererObj.getStringWidth(wtf1) + 5, this.mc.fontRendererObj.getStringWidth(scoreobjective1.getDisplayName()) + 5));
                Gui.drawRect(j - 2, l, i1, l + (double)this.mc.fontRendererObj.FONT_HEIGHT, 0x50000000);
                this.mc.fontRendererObj.drawString(s1, j, (float)l, 0x20FFFFFF);
                this.mc.fontRendererObj.drawString(s2, i1 - this.mc.fontRendererObj.getStringWidth(s2), (float)l, 0x20FFFFFF);
                AutoMatchJoin autoMatchJoin = (AutoMatchJoin)Moon.INSTANCE.getModuleManager().getModule("AutoMatchJoin");
                if (autoMatchJoin.isEnabled() && autoMatchJoin.autoDetect.isEnabled()) {
                    if (s1.toLowerCase().contains("insane")) {
                        autoMatchJoin.type.setValue(AutoMatchJoin.Type.INSANE);
                    }
                    if (s1.toLowerCase().contains("normal")) {
                        autoMatchJoin.type.setValue(AutoMatchJoin.Type.NORMAL);
                    }
                    autoMatchJoin.teams.setValue(s1.toLowerCase().contains("teams"));
                }
                if (k != arraylist1.size()) continue;
                Gui.drawRect(j - 2, l - (double)this.mc.fontRendererObj.FONT_HEIGHT - 1.0, i1, l - 1.0, 0x60000000);
                Gui.drawRect(j - 2, l - 1.0, i1, l, 0x50000000);
                this.mc.fontRendererObj.drawString(s3, j + i / 2 - this.mc.fontRendererObj.getStringWidth(s3) / 2, (float)(l - (double)this.mc.fontRendererObj.FONT_HEIGHT), 0x20FFFFFF);
            }
        }
        GL11.glPopMatrix();
    }

    @Override
    public void onCompUpdate(UpdateEvent event) {
    }

    @Override
    public void onInit() {
    }
}

