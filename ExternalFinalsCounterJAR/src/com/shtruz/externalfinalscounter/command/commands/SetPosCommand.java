package com.shtruz.externalfinalscounter.command.commands;

import com.shtruz.externalfinalscounter.ExternalFinalsCounter;
import com.shtruz.externalfinalscounter.command.Command;

import java.lang.reflect.InvocationTargetException;

public class SetPosCommand implements Command {
    @Override
    public String getName() {
        return "setpos";
    }

    @Override
    public void execute(ExternalFinalsCounter externalFinalsCounter, String[] args) {
        if (args.length != 2) {
            return;
        }

        int x;
        int y;

        try {
            x = Integer.parseInt(args[0]);
            y = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
            return;
        }

        if (x < 0 || y < 0) {
            return;
        }

        externalFinalsCounter.getConfig().finalsCounterX = x;
        externalFinalsCounter.getConfig().finalsCounterY = y;

        externalFinalsCounter.saveConfig();

        String output = "Finals HUD position has been set to X: " + x + ", Y: " + y;

        try {
            externalFinalsCounter.addChatComponentText(output);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
