package com.shtruz.externalfinalscounter.command.commands;

import com.shtruz.externalfinalscounter.ExternalFinalsCounter;
import com.shtruz.externalfinalscounter.command.Command;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.shtruz.externalfinalscounter.mapping.Mappings.*;

public class PlayerFinalsCommand implements Command {
    @Override
    public String getName() {
        return "playerfinals";
    }

    @Override
    public void execute(ExternalFinalsCounter externalFinalsCounter, String[] args) {
        try {
            Map<String, Integer> reverseSortedMap = externalFinalsCounter.getChatMessageParser().getAllPlayers()
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue,
                            LinkedHashMap::new));

            StringBuilder stringBuilder = new StringBuilder();

            int i = 1;
            for (Map.Entry<String, Integer> entry : reverseSortedMap.entrySet()) {
                String prefix = "";

                if (externalFinalsCounter.getChatMessageParser().getBlue().containsKey(entry.getKey())) {
                    prefix = externalFinalsCounter.getChatMessageParser().getBluePrefix();
                } else if (externalFinalsCounter.getChatMessageParser().getGreen().containsKey(entry.getKey())) {
                    prefix = externalFinalsCounter.getChatMessageParser().getGreenPrefix();
                } else if (externalFinalsCounter.getChatMessageParser().getRed().containsKey(entry.getKey())) {
                    prefix = externalFinalsCounter.getChatMessageParser().getRedPrefix();
                } else if (externalFinalsCounter.getChatMessageParser().getYellow().containsKey(entry.getKey())) {
                    prefix = externalFinalsCounter.getChatMessageParser().getYellowPrefix();
                }

                stringBuilder.append(i).append(". ").append(prefix).append(entry.getKey()).append(": ").append("\u00A7f").append(entry.getValue()).append("\n");
                i++;
            }

            if (!stringBuilder.toString().isEmpty()) {
                stringBuilder.replace(stringBuilder.lastIndexOf("\n"), stringBuilder.lastIndexOf("\n") + 1, "");
            }

            externalFinalsCounter.addChatComponentText(stringBuilder.toString());
        } catch (IllegalAccessException
                 | InvocationTargetException
                 | NoSuchMethodException
                 | InstantiationException exception) {
            exception.printStackTrace();
        }
    }
}
