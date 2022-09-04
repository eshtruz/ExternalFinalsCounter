package com.shtruz.externalfinalscounter.command.commands;

import com.shtruz.externalfinalscounter.ExternalFinalsCounter;
import com.shtruz.externalfinalscounter.command.Command;

import java.lang.reflect.InvocationTargetException;

public class DisplayFinalsCounterCommand implements Command {
    @Override
    public String getName() {
        return "displayfinalscounter";
    }

    @Override
    public void execute(ExternalFinalsCounter externalFinalsCounter, String[] args) {
        externalFinalsCounter.getConfig().displayFinalsCounter = !externalFinalsCounter.getConfig().displayFinalsCounter;

        externalFinalsCounter.saveConfig();

        try {
            String output = (externalFinalsCounter.getConfig().displayFinalsCounter ? "Enabled" : "Disabled") + " finals counter HUD";

            externalFinalsCounter.addChatComponentText(output);
        } catch (IllegalAccessException
                 | InvocationTargetException
                 | NoSuchMethodException
                 | InstantiationException exception) {
            exception.printStackTrace();
        }
    }
}
