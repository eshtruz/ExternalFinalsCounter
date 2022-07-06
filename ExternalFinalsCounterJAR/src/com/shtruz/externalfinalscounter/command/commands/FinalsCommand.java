package com.shtruz.externalfinalscounter.command.commands;

import com.shtruz.externalfinalscounter.ExternalFinalsCounter;
import com.shtruz.externalfinalscounter.command.Command;

import java.lang.reflect.InvocationTargetException;

import static com.shtruz.externalfinalscounter.mapping.Mappings.*;

public class FinalsCommand implements Command {
    @Override
    public String getName() {
        return "finals";
    }

    @Override
    public void execute(ExternalFinalsCounter externalFinalsCounter, String[] args) {
        try {
            int blueFinals = externalFinalsCounter.getChatMessageParser().getBlue()
                    .values()
                    .stream()
                    .reduce(0, Integer::sum);

            int greenFinals = externalFinalsCounter.getChatMessageParser().getGreen()
                    .values()
                    .stream()
                    .reduce(0, Integer::sum);

            int redFinals = externalFinalsCounter.getChatMessageParser().getRed()
                    .values()
                    .stream()
                    .reduce(0, Integer::sum);

            int yellowFinals = externalFinalsCounter.getChatMessageParser().getYellow()
                    .values()
                    .stream()
                    .reduce(0, Integer::sum);

            String finals = externalFinalsCounter.getChatMessageParser().getBluePrefix() + "BLUE: " + "\u00A7f" + blueFinals + "\n"
                    + externalFinalsCounter.getChatMessageParser().getGreenPrefix() + "GREEN: " + "\u00A7f" + greenFinals + "\n"
                    + externalFinalsCounter.getChatMessageParser().getRedPrefix() + "RED: " + "\u00A7f" + redFinals + "\n"
                    + externalFinalsCounter.getChatMessageParser().getYellowPrefix() + "YELLOW: " + "\u00A7f" + yellowFinals;

            Object minecraft = getMinecraftMethod.invoke(null);

            Object thePlayer = thePlayerField.get(minecraft);

            Object finalsChatComponentText = chatComponentTextClass
                    .getDeclaredConstructor(String.class)
                    .newInstance(finals);

            addChatComponentMessageMethod.invoke(thePlayer, finalsChatComponentText);
        } catch (IllegalAccessException
                 | InvocationTargetException
                 | NoSuchMethodException
                 | InstantiationException exception) {
            exception.printStackTrace();
        }
    }
}
