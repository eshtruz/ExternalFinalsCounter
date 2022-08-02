package com.shtruz.externalfinalscounter.mapping.mappings;

import com.shtruz.externalfinalscounter.mapping.Mapping;

public class Lunar extends Mapping {
    @Override
    protected void setupNames() {
        names.get("auq").setLeft("net.minecraft.scoreboard.Team");

        names.get("fa").setLeft("net.minecraft.util.ChatComponentText");

        names.get("auk").setLeft("net.minecraft.scoreboard.ScoreObjective");
        names.get("auk").getRight().replace("d()Ljava/lang/String;", "getDisplayName");

        names.get("eu").setLeft("net.minecraft.util.IChatComponent");
        names.get("eu").getRight().replace("c()Ljava/lang/String;", "getUnformattedText");
        names.get("eu").getRight().replace("d()Ljava/lang/String;", "getFormattedText");

        names.get("auo").setLeft("net.minecraft.scoreboard.Scoreboard");
        names.get("auo").getRight().replace("a(I)Lauk;", "getObjectiveInDisplaySlot");
        names.get("auo").getRight().replace("i(Lauk;)Ljava/util/Collection;", "getSortedScores");
        names.get("auo").getRight().replace("h(Ljava/lang/String;)Laul;", "getPlayersTeam");

        names.get("bdc").setLeft("net.minecraft.client.network.NetworkPlayerInfo");
        names.get("bdc").getRight().replace("a()Lcom/mojang/authlib/GameProfile;", "getGameProfile");

        names.get("ave").setLeft("net.minecraft.client.Minecraft");
        names.get("ave").getMiddle().replace("f", "theWorld");
        names.get("ave").getMiddle().replace("w", "inGameHasFocus");
        names.get("ave").getMiddle().replace("t", "gameSettings");
        names.get("ave").getMiddle().replace("k", "fontRendererObj");
        names.get("ave").getMiddle().replace("h", "thePlayer");
        names.get("ave").getRight().replace("A()Lave;", "getMinecraft");
        names.get("ave").getRight().replace("D()Lbde;", "getCurrentServerData");
        names.get("ave").getRight().replace("av()V", "runGameLoop");

        names.get("adm").setLeft("net.minecraft.world.World");
        names.get("adm").getRight().replace("Z()Lauo;", "getScoreboard");

        names.get("aum").setLeft("net.minecraft.scoreboard.Score");
        names.get("aum").getRight().replace("e()Ljava/lang/String;", "getPlayerName");

        names.get("aul").setLeft("net.minecraft.scoreboard.ScorePlayerTeam");
        names.get("aul").getRight().replace("a(Lauq;Ljava/lang/String;)Ljava/lang/String;", "formatPlayerName");

        names.get("bde").setLeft("net.minecraft.client.multiplayer.ServerData");
        names.get("bde").getMiddle().replace("b", "serverIP");

        names.get("nx").setLeft("net.minecraft.util.StringUtils");
        names.get("nx").getRight().replace("a(Ljava/lang/String;)Ljava/lang/String;", "stripControlCodes");

        names.get("avh").setLeft("net.minecraft.client.settings.GameSettings");
        names.get("avh").getMiddle().replace("aC", "showDebugInfo");

        names.get("bfl").setLeft("net.minecraft.client.renderer.GlStateManager");
        names.get("bfl").getRight().replace("E()V", "pushMatrix");
        names.get("bfl").getRight().replace("a(DDD)V", "scale");
        names.get("bfl").getRight().replace("F()V", "popMatrix");

        names.get("avn").setLeft("net.minecraft.client.gui.FontRenderer");
        names.get("avn").getRight().replace("a(Ljava/lang/String;FFIZ)I", "drawString");

        names.get("wn").setLeft("net.minecraft.entity.player.EntityPlayer");
        names.get("wn").getRight().replace("b(Leu;)V", "addChatComponentMessage");

        names.get("avt").setLeft("net.minecraft.client.gui.GuiNewChat");
        names.get("avt").getRight().replace("a(Leu;)V", "printChatMessage");

        names.get("bew").setLeft("net.minecraft.client.entity.EntityPlayerSP");
        names.get("bew").getRight().replace("e(Ljava/lang/String;)V", "sendChatMessage");

        names.get("bfk").setLeft("net.minecraft.client.renderer.EntityRenderer");
        names.get("bfk").getRight().replace("a(FJ)V", "updateCameraAndRender");

        names.get("awh").setLeft("net.minecraft.client.gui.GuiPlayerTabOverlay");
        names.get("awh").getRight().replace("a(ILauo;Lauk;)V", "renderPlayerlist");
        names.get("awh").getRight().replace("a(Lbdc;)Ljava/lang/String;", "getPlayerName");
    }
}
