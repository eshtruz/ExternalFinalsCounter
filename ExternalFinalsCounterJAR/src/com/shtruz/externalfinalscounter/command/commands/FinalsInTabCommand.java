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

        String output = externalFinalsCounter.getConfig().finalsInTab ? "Enabled" : "Disabled" + " finals counter in tab";

        try {
            externalFinalsCounter.addChatComponentText(output);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
