package com.shtruz.externalfinalscounter.command.commands;

import com.shtruz.externalfinalscounter.ExternalFinalsCounter;
import com.shtruz.externalfinalscounter.command.Command;

import java.lang.reflect.InvocationTargetException;

public class FinalsInTabCommand implements Command {
    @Override
    public String getName() {
        return "finalsintab";
    }

    @Override
    public void execute(ExternalFinalsCounter externalFinalsCounter, String[] args) {
        externalFinalsCounter.getConfig().finalsInTab = !externalFinalsCounter.getConfig().finalsInTab;

        externalFinalsCounter.saveConfig();

        try {
            String output = (externalFinalsCounter.getConfig().finalsInTab ? "Enabled" : "Disabled") + " finals in tab";

            externalFinalsCounter.addChatComponentText(output);
        } catch (IllegalAccessException
                 | InvocationTargetException
                 | NoSuchMethodException
                 | InstantiationException exception) {
            exception.printStackTrace();
        }
    }
}
