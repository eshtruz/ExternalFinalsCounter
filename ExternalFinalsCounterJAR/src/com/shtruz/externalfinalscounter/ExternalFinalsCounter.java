package com.shtruz.externalfinalscounter;

import com.google.gson.Gson;
import com.shtruz.externalfinalscounter.command.CommandManager;
import com.shtruz.externalfinalscounter.finalscounter.ChatMessageParser;
import com.shtruz.externalfinalscounter.finalscounter.FinalsCounterRenderer;
import com.shtruz.externalfinalscounter.instrument.Instrumentation;
import com.shtruz.externalfinalscounter.instrument.transformer.transformers.GuiPlayerTabOverlayTransformer;
import com.shtruz.externalfinalscounter.instrument.transformer.transformers.MinecraftTransformer;
import com.shtruz.externalfinalscounter.instrument.transformer.transformers.NetworkManagerTransformer;
import com.shtruz.externalfinalscounter.mapping.Mapping;
import com.shtruz.externalfinalscounter.mapping.mappings.Vanilla;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static com.shtruz.externalfinalscounter.mapping.Mappings.*;

public class ExternalFinalsCounter {
    public static ExternalFinalsCounter instance;
    private final ChatMessageParser chatMessageParser = new ChatMessageParser(this);
    private final FinalsCounterRenderer finalsCounterRenderer = new FinalsCounterRenderer(this);
    private final CommandManager commandManager = new CommandManager(this);
    private final Instrumentation instrumentation = new Instrumentation();
    private final File configFile = new File("ExternalFinalsCounter.json");
    private Config config = new Config();
    private final Gson gson = new Gson();

    public ExternalFinalsCounter() {
        instance = this;

        if (configFile.exists()) {
            try {
                String jsonConfig = new String(Files.readAllBytes(configFile.toPath()), StandardCharsets.UTF_8);
                config = gson.fromJson(jsonConfig, Config.class);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            saveConfig();
        }
    }

    public boolean initialize(Client client, ClassLoader classLoader) {
        Mapping mapping;

        switch (client) {
            case VANILLA:
                mapping = new Vanilla();
                break;

            default:
                mapping = new Vanilla();
        }

        if (!mapping.setupMappings()) {
            JOptionPane.showMessageDialog(null, "Failed to setup mappings");
            return false;
        }

        try {
            System.load(new File("ExternalFinalsCounterJARDLL.dll").getAbsolutePath());
        } catch (SecurityException | UnsatisfiedLinkError | NullPointerException exception) {
            JOptionPane.showMessageDialog(null, "Failed to load JAR DLL");
            exception.printStackTrace();
            return false;
        }

        if (!initialize(classLoader)) {
            JOptionPane.showMessageDialog(null, "Failed to initialize JAR DLL");
            return false;
        }

        instrumentation.addTransformer(new NetworkManagerTransformer());
        instrumentation.addTransformer(new MinecraftTransformer());
        instrumentation.addTransformer(new GuiPlayerTabOverlayTransformer());

        if (!instrumentation.retransformClass(networkManagerClass)
                || !instrumentation.retransformClass(minecraftClass)
                || !instrumentation.retransformClass(guiPlayerTabOverlayClass)) {
            JOptionPane.showMessageDialog(null, "Failed to retransform classes");
            return false;
        }

        return true;
    }

    private native boolean initialize(ClassLoader classLoader);

    public boolean onPacket(Object packet) {
        try {
            if (s02PacketChatClass.isInstance(packet)) {
                if ((boolean) isChatMethod.invoke(packet)) {
                    Object chatComponent = getChatComponentMethod.invoke(packet);
                    chatMessageParser.onChat(chatComponent);
                }

                return false;
            }

            if (c01PacketChatMessageClass.isInstance(packet)) {
                String message = (String) getMessageMethod.invoke(packet);

                message = message.trim();

                if (message.startsWith(".")) {
                    message = message.substring(1);

                    if (!message.isEmpty()) {
                        return commandManager.executeCommand(message);
                    }
                }

                return false;
            }
        } catch (InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public void onRender() {
        finalsCounterRenderer.render();
    }

    public ChatMessageParser getChatMessageParser() {
        return chatMessageParser;
    }

    public FinalsCounterRenderer getFinalsCounterRenderer() {
        return finalsCounterRenderer;
    }

    public Config getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            if (!configFile.exists()) {
                if (!configFile.createNewFile()) {
                    System.err.println("Failed to create config file!");
                    return;
                }
            }
            
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile));
            bufferedWriter.write(gson.toJson(config));
            bufferedWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
