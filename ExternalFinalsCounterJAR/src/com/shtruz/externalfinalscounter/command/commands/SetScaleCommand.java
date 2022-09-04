package com.shtruz.externalfinalscounter.command.commands;

import com.shtruz.externalfinalscounter.ExternalFinalsCounter;
import com.shtruz.externalfinalscounter.command.Command;

import java.lang.reflect.InvocationTargetException;

public class SetScaleCommand implements Command {
    @Override
    public String getName() {
        return "setscale";
    }

    @Override
    public void execute(ExternalFinalsCounter externalFinalsCounter, String[] args) {
        if (args.length != 1) {
            return;
        }

        double scale;

        try {
            scale = Double.parseDouble(args[0]);
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
            return;
        }

        if (scale <= 0) {
            return;
        }

        externalFinalsCounter.getConfig().finalsCounterScale = scale;

        externalFinalsCounter.saveConfig();

        try {
            String output = "Set scale to " + scale + "%";

            externalFinalsCounter.addChatComponentText(output);
        } catch (IllegalAccessException
                 | InvocationTargetException
                 | NoSuchMethodException
                 | InstantiationException exception) {
            exception.printStackTrace();
        }
    }
}
