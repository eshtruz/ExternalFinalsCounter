package com.shtruz.externalfinalscounter.mapping;

import org.apache.commons.lang3.tuple.MutableTriple;

import java.util.HashMap;
import java.util.Map;

import static com.shtruz.externalfinalscounter.mapping.Mappings.*;

public abstract class Mapping {
    protected final Map<String, MutableTriple<String, Map<String, String>, Map<String, String>>> names = new HashMap<>();

    public Mapping() {
        names.put("auq", new MutableTriple<>(null, null, null));

        names.put("fa", new MutableTriple<>(null, null, null));

        names.put("ff", new MutableTriple<>(null, null, null));

        names.put("auk", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("auk").getRight().put("d()Ljava/lang/String;", null);

        names.put("eu", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("eu").getRight().put("c()Ljava/lang/String;", null);
        names.get("eu").getRight().put("d()Ljava/lang/String;", null);

        names.put("auo", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("auo").getRight().put("a(I)Lauk;", null);
        names.get("auo").getRight().put("i(Lauk;)Ljava/util/Collection;", null);
        names.get("auo").getRight().put("h(Ljava/lang/String;)Laul;", null);

        names.put("bdc", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("bdc").getRight().put("a()Lcom/mojang/authlib/GameProfile;", null);

        names.put("ave", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("ave").getMiddle().put("f", null);
        names.get("ave").getMiddle().put("w", null);
        names.get("ave").getMiddle().put("t", null);
        names.get("ave").getMiddle().put("k", null);
        names.get("ave").getMiddle().put("h", null);
        names.get("ave").getRight().put("A()Lave;", null);
        names.get("ave").getRight().put("D()Lbde;", null);
        names.get("ave").getRight().put("av()V", null);

        names.put("adm", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("adm").getRight().put("Z()Lauo;", null);

        names.put("aum", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("aum").getRight().put("e()Ljava/lang/String;", null);

        names.put("aul", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("aul").getRight().put("a(Lauq;Ljava/lang/String;)Ljava/lang/String;", null);

        names.put("bde", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("bde").getMiddle().put("b", null);

        names.put("nx", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("nx").getRight().put("a(Ljava/lang/String;)Ljava/lang/String;", null);

        names.put("avh", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("avh").getMiddle().put("aB", null);

        names.put("bfl", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("bfl").getRight().put("E()V", null);
        names.get("bfl").getRight().put("a(DDD)V", null);
        names.get("bfl").getRight().put("F()V", null);

        names.put("avn", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("avn").getRight().put("a(Ljava/lang/String;FFIZ)I", null);

        names.put("wn", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("wn").getRight().put("b(Leu;)V", null);

        names.put("ek", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("ek").getRight().put("a(Lff;)V", null);

        names.put("bfk", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("bfk").getRight().put("a(FJ)V", null);

        names.put("awh", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("awh").getRight().put("a(ILauo;Lauk;)V", null);
        names.get("awh").getRight().put("a(Lbdc;)Ljava/lang/String;", null);

        names.put("fy", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("fy").getRight().put("a()Leu;", null);
        names.get("fy").getRight().put("b()Z", null);

        names.put("ie", new MutableTriple<>(null, new HashMap<>(), new HashMap<>()));
        names.get("ie").getRight().put("a()Ljava/lang/String;", null);

        setupNames();
    }

    protected abstract void setupNames();

    public boolean setupMappings() {
        try {
            teamClass = Class.forName(names.get("auq").getLeft());

            chatComponentTextClass = Class.forName(names.get("fa").getLeft());

            packetClass = Class.forName(names.get("ff").getLeft());

            scoreObjectiveClass = Class.forName(names.get("auk").getLeft());
            getDisplayNameMethod = scoreObjectiveClass.getDeclaredMethod(names.get("auk").getRight().get("d()Ljava/lang/String;"));

            iChatComponentClass = Class.forName(names.get("eu").getLeft());
            getUnformattedTextMethod = iChatComponentClass.getDeclaredMethod(names.get("eu").getRight().get("c()Ljava/lang/String;"));
            getFormattedTextMethod = iChatComponentClass.getDeclaredMethod(names.get("eu").getRight().get("d()Ljava/lang/String;"));

            scoreboardClass = Class.forName(names.get("auo").getLeft());
            getObjectiveInDisplaySlotMethod = scoreboardClass.getDeclaredMethod(names.get("auo").getRight().get("a(I)Lauk;"), int.class);
            getSortedScoresMethod = scoreboardClass.getDeclaredMethod(names.get("auo").getRight().get("i(Lauk;)Ljava/util/Collection;"), scoreObjectiveClass);
            getPlayersTeamMethod = scoreboardClass.getDeclaredMethod(names.get("auo").getRight().get("h(Ljava/lang/String;)Laul;"), String.class);

            networkPlayerInfoClass = Class.forName(names.get("bdc").getLeft());
            getGameProfileMethod = networkPlayerInfoClass.getDeclaredMethod(names.get("bdc").getRight().get("a()Lcom/mojang/authlib/GameProfile;"));

            minecraftClass = Class.forName(names.get("ave").getLeft());
            theWorldField = minecraftClass.getDeclaredField(names.get("ave").getMiddle().get("f"));
            inGameHasFocusField = minecraftClass.getDeclaredField(names.get("ave").getMiddle().get("w"));
            gameSettingsField = minecraftClass.getDeclaredField(names.get("ave").getMiddle().get("t"));
            fontRendererObjField = minecraftClass.getDeclaredField(names.get("ave").getMiddle().get("k"));
            thePlayerField = minecraftClass.getDeclaredField(names.get("ave").getMiddle().get("h"));
            getMinecraftMethod = minecraftClass.getDeclaredMethod(names.get("ave").getRight().get("A()Lave;"));
            getCurrentServerDataMethod = minecraftClass.getDeclaredMethod(names.get("ave").getRight().get("D()Lbde;"));
            runGameLoopMethod = minecraftClass.getDeclaredMethod(names.get("ave").getRight().get("av()V"));

            worldClass = Class.forName(names.get("adm").getLeft());
            getScoreboardMethod = worldClass.getDeclaredMethod(names.get("adm").getRight().get("Z()Lauo;"));

            scoreClass = Class.forName(names.get("aum").getLeft());
            sGetPlayerNameMethod = scoreClass.getDeclaredMethod(names.get("aum").getRight().get("e()Ljava/lang/String;"));

            scorePlayerTeamClass = Class.forName(names.get("aul").getLeft());
            formatPlayerNameMethod = scorePlayerTeamClass.getDeclaredMethod(names.get("aul").getRight().get("a(Lauq;Ljava/lang/String;)Ljava/lang/String;"), teamClass, String.class);

            serverDataClass = Class.forName(names.get("bde").getLeft());
            serverIPField = serverDataClass.getDeclaredField(names.get("bde").getMiddle().get("b"));

            stringUtilsClass = Class.forName(names.get("nx").getLeft());
            stripControlCodesMethod = stringUtilsClass.getDeclaredMethod(names.get("nx").getRight().get("a(Ljava/lang/String;)Ljava/lang/String;"), String.class);

            gameSettingsClass = Class.forName(names.get("avh").getLeft());
            showDebugInfoField = gameSettingsClass.getDeclaredField(names.get("avh").getMiddle().get("aB"));

            glStateManagerClass = Class.forName(names.get("bfl").getLeft());
            pushMatrixMethod = glStateManagerClass.getDeclaredMethod(names.get("bfl").getRight().get("E()V"));
            scaleMethod = glStateManagerClass.getDeclaredMethod(names.get("bfl").getRight().get("a(DDD)V"), double.class, double.class, double.class);
            popMatrixMethod = glStateManagerClass.getDeclaredMethod(names.get("bfl").getRight().get("F()V"));

            fontRendererClass = Class.forName(names.get("avn").getLeft());
            drawStringMethod = fontRendererClass.getDeclaredMethod(names.get("avn").getRight().get("a(Ljava/lang/String;FFIZ)I"), String.class, float.class, float.class, int.class, boolean.class);

            entityPlayerClass = Class.forName(names.get("wn").getLeft());
            addChatComponentMessageMethod = entityPlayerClass.getDeclaredMethod(names.get("wn").getRight().get("b(Leu;)V"), iChatComponentClass);

            networkManagerClass = Class.forName(names.get("ek").getLeft());
            sendPacketMethod = networkManagerClass.getDeclaredMethod(names.get("ek").getRight().get("a(Lff;)V"), packetClass);

            entityRendererClass = Class.forName(names.get("bfk").getLeft());
            updateCameraAndRenderMethod = entityRendererClass.getDeclaredMethod(names.get("bfk").getRight().get("a(FJ)V"), float.class, long.class);

            guiPlayerTabOverlayClass = Class.forName(names.get("awh").getLeft());
            renderPlayerlistMethod = guiPlayerTabOverlayClass.getDeclaredMethod(names.get("awh").getRight().get("a(ILauo;Lauk;)V"), int.class, scoreboardClass, scoreObjectiveClass);
            gptoGetPlayerNameMethod = guiPlayerTabOverlayClass.getDeclaredMethod(names.get("awh").getRight().get("a(Lbdc;)Ljava/lang/String;"), networkPlayerInfoClass);

            s02PacketChatClass = Class.forName(names.get("fy").getLeft());
            getChatComponentMethod = s02PacketChatClass.getDeclaredMethod(names.get("fy").getRight().get("a()Leu;"));
            isChatMethod = s02PacketChatClass.getDeclaredMethod(names.get("fy").getRight().get("b()Z"));

            c01PacketChatMessageClass = Class.forName(names.get("ie").getLeft());
            getMessageMethod = c01PacketChatMessageClass.getDeclaredMethod(names.get("ie").getRight().get("a()Ljava/lang/String;"));
        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException exception) {
            exception.printStackTrace();
            return false;
        }

        return true;
    }
}
